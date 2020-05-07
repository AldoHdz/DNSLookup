package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ARecord{
    String domain;
    Long ttl;
    String address;
}
