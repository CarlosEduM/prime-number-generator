package com.example.orchestrator.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.domain.exceptions.NotFoundException;
import com.example.orchestrator.domain.pojos.PageableRequest;
import com.example.orchestrator.domain.pojos.PageableResult;
import com.example.orchestrator.infrastructure.config.SecurityConfiguration;

@WebMvcTest(PrimeRequestController.class)
@Import(SecurityConfiguration.class)
class PrimeRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrimeRequestService primeRequestService;

    @Test
    void testCreatePrimeRequestWithoutAuthenticationReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/prime-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":0,"inputNumberMax":10}
                        """))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(primeRequestService);
    }

    @Test
    void testCreatePrimeRequestUsesAuthenticatedSubjectAsOwner() throws Exception {
        UUID createdRequestId = UUID.fromString("739e748d-0c55-49af-9a80-487ed7fac284");
        PrimeRequests createdRequest = new PrimeRequests();
        ReflectionTestUtils.setField(createdRequest, "id", createdRequestId);
        createdRequest.assignOwnerId("user-42");
        createdRequest.setInputNumberRange(BigInteger.ZERO, BigInteger.TEN);
        when(primeRequestService.requestPrimeNumberRange(any(PrimeRequestDTO.class), eq("user-42")))
                .thenReturn(createdRequest);

        mockMvc.perform(post("/api/v1/prime-requests")
                .with(jwt().jwt(token -> token.subject("user-42")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":0,"inputNumberMax":10,"ownerId":"attacker"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + createdRequestId + "\""));

        verify(primeRequestService).requestPrimeNumberRange(
                new PrimeRequestDTO(BigInteger.ZERO, BigInteger.TEN), "user-42");
    }

    @Test
    void testCreatePrimeRequestWithMissingRangeValueReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/prime-requests")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":0}
                        """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(primeRequestService);
    }

    @Test
    void testCreatePrimeRequestWithInvalidRangeReturnsBadRequest() throws Exception {
        when(primeRequestService.requestPrimeNumberRange(any(PrimeRequestDTO.class), eq("user-42")))
                .thenThrow(new InvalidInputException("Invalid range."));

        mockMvc.perform(post("/api/v1/prime-requests")
                .with(jwt().jwt(token -> token.subject("user-42")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":10,"inputNumberMax":12}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid range."));
    }

    @Test
    void testGetPrimeRequestUsesAuthenticatedSubjectAsOwner() throws Exception {
        UUID requestId = UUID.fromString("7b09c7d7-12a6-4270-91d9-85cc3eae8fa3");
        PrimeRequests request = new PrimeRequests();
        request.assignOwnerId("user-42");
        request.setInputNumberRange(BigInteger.ZERO, BigInteger.TEN);
        when(primeRequestService.getPrimeRequest(requestId, "user-42")).thenReturn(request);

        mockMvc.perform(get("/api/v1/prime-requests/{id}", requestId)
                .with(jwt().jwt(token -> token.subject("user-42"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inputNumberMin").value(0))
                .andExpect(jsonPath("$.inputNumberMax").value(10))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.ownerId").value("user-42"));

        verify(primeRequestService).getPrimeRequest(requestId, "user-42");
    }

    @Test
    void testGetPrimeRequestWithoutAuthenticationReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/prime-requests/{id}",
                UUID.fromString("7b09c7d7-12a6-4270-91d9-85cc3eae8fa3")))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(primeRequestService);
    }

    @Test
    void testGetPrimeRequestNotFoundForAuthenticatedOwnerReturnsNotFound() throws Exception {
        UUID requestId = UUID.fromString("7b09c7d7-12a6-4270-91d9-85cc3eae8fa3");
        when(primeRequestService.getPrimeRequest(requestId, "user-42"))
                .thenThrow(new NotFoundException("Prime request not found."));

        mockMvc.perform(get("/api/v1/prime-requests/{id}", requestId)
                .with(jwt().jwt(token -> token.subject("user-42"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Prime request not found."));
    }

    @Test
    void testGetAllPrimeRequestsUsesAuthenticatedSubjectAndPagination() throws Exception {
        PrimeRequests request = new PrimeRequests();
        request.assignOwnerId("user-42");
        request.setInputNumberRange(BigInteger.ZERO, BigInteger.TEN);
        PageableRequest pageableRequest = PageableRequest.of(1, 5);
        PageableResult<PrimeRequests> page = new PageableResult<>(List.of(request), 1, 5, 6);
        when(primeRequestService.getAllPrimeRequests(pageableRequest, "user-42")).thenReturn(page);

        mockMvc.perform(get("/api/v1/prime-requests")
                .with(jwt().jwt(token -> token.subject("user-42")))
                .param("page", "1")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].inputNumberMin").value(0))
                .andExpect(jsonPath("$.content[0].inputNumberMax").value(10))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(6));

        verify(primeRequestService).getAllPrimeRequests(pageableRequest, "user-42");
    }

    @Test
    void testGetAllPrimeRequestsWithoutAuthenticationReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/prime-requests"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(primeRequestService);
    }

    @Test
    void testGetAllPrimeRequestsWithInvalidPageReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/prime-requests")
                .with(jwt())
                .param("page", "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(primeRequestService);
    }
}
