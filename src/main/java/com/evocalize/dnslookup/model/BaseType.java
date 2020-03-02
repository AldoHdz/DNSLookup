package com.evocalize.dnslookup.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseType {
    String domain;
    String address;
    Long ttl;
    List<String> records;
    String nsdName;
    String mName;
    String rName;
    Long serial;
    Long refresh;
    Long retry;
    Long expire;
    Long minimum;
    Integer preference;
    String exchange;
    Integer flags;
    String tag;
    String value;
}
