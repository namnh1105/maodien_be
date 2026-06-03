package com.hainam.worksphere.cullingproposal.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CullingProposalStatus {
    PENDING,
    APPROVED,
    REJECTED;

    @JsonCreator
    public static CullingProposalStatus fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return CullingProposalStatus.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
