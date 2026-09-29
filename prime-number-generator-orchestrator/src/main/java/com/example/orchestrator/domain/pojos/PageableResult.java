package com.example.orchestrator.domain.pojos;

import java.util.List;
import java.util.function.Function;

public record PageableResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements) {

    public int totalPages() {
        return (int) Math.ceil((double) totalElements / size);
    }

    public boolean hasNext() {
        return page + 1 < totalPages();
    }

    public <R> PageableResult<R> map(Function<T, R> mapper) {
        return new PageableResult<>(content.stream().map(mapper).toList(), page, size, totalElements);
    }
}
