package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DNSLookupResponse{
    private String type;
    private List<Object> response;
}
