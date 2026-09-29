package com.example.orchestrator.domain.pojos;

import java.util.List;

public record PageableRequest(int page, int size, List<Sort> sorts) {
    public PageableRequest {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("size must be >= 1");
        sorts = sorts == null ? List.of() : List.copyOf(sorts);
    }

    public static PageableRequest of(int page, int size) {
        return new PageableRequest(page, size, List.of());
    }

    public record Sort(String property, Direction direction) {}
    public enum Direction { ASC, DESC }
}
