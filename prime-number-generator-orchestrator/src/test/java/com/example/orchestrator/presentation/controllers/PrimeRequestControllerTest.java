package com.example.orchestrator.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.orchestrator.application.dtos.PrimeRequestDTO;
import com.example.orchestrator.application.services.PrimeRequestService;
import com.example.orchestrator.domain.entities.PrimeRequests;
import com.example.orchestrator.domain.exceptions.InvalidInputException;
import com.example.orchestrator.infrastructure.security.SecurityConfiguration;

@WebMvcTest(PrimeRequestController.class)
@Import(SecurityConfiguration.class)
class PrimeRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrimeRequestService primeRequestService;

    @Test
    void testCreatePrimeRequestWithoutAuthenticationReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/prime-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":0,"inputNumberMax":10}
                        """))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(primeRequestService);
    }

    @Test
    void testCreatePrimeRequestUsesAuthenticatedSubjectAsOwner() throws Exception {
        PrimeRequests createdRequest = new PrimeRequests();
        createdRequest.assignOwnerId("user-42");
        createdRequest.setInputNumberRange(BigInteger.ZERO, BigInteger.TEN);
        when(primeRequestService.requestPrimeNumberRange(any(PrimeRequestDTO.class), eq("user-42")))
                .thenReturn(createdRequest);

        mockMvc.perform(post("/api/prime-requests")
                .with(jwt().jwt(token -> token.subject("user-42")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":0,"inputNumberMax":10,"ownerId":"attacker"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inputNumberMin").value(0))
                .andExpect(jsonPath("$.inputNumberMax").value(10))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.ownerId").doesNotExist());

        verify(primeRequestService).requestPrimeNumberRange(
                new PrimeRequestDTO(BigInteger.ZERO, BigInteger.TEN), "user-42");
    }

    @Test
    void testCreatePrimeRequestWithMissingRangeValueReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/prime-requests")
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

        mockMvc.perform(post("/api/prime-requests")
                .with(jwt().jwt(token -> token.subject("user-42")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"inputNumberMin":10,"inputNumberMax":12}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid range."));
    }
}
