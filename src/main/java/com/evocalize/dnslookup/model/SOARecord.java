package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SOARecord{
    String domain;
    Long ttl;
    String mName;
    String rName;
    Long serial;
    Long refresh;
    Long retry;
    Long expire;
    Long minimum;
}
