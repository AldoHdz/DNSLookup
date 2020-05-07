package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MXRecord{
    String domain;
    Long ttl;
    Integer preference;
    String exchange;
}
