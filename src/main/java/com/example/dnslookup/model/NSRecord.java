package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NSRecord{
    String domain;
    Long ttl;
    String nsdName;

}
