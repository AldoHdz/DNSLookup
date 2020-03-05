package com.evocalize.dnslookup.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TXTRecord{
    String domain;
    Long ttl;
    List<String> records;
}
