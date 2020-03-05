package com.evocalize.dnslookup.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DNSLookupRequest {
    private String lookup;
    private List<String> recordTypes;

    @JsonCreator
    public DNSLookupRequest(@JsonProperty("lookup") String lookup,
                            @JsonProperty("recordTypes") List<String> recordTypes) {
        this.lookup = lookup;
        this.recordTypes = recordTypes;
    }
}
