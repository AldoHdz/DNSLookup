package com.evocalize.dnslookup.service;

import com.evocalize.dnslookup.connector.DNSJavaConnector;
import com.evocalize.dnslookup.exception.ServerNotFoundException;
import com.evocalize.dnslookup.exception.UnsupportedDNSRecordLookupException;
import com.evocalize.dnslookup.model.BaseType;
import com.evocalize.dnslookup.model.DNSLookupRequest;
import com.evocalize.dnslookup.model.DNSLookupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xbill.DNS.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = {"results"})
public class DNSLookupService {
    private DNSJavaConnector dnsJavaConnector;

    @Autowired
    public DNSLookupService(DNSJavaConnector dnsJavaConnector) {
        this.dnsJavaConnector = dnsJavaConnector;
    }

    public List<DNSLookupResponse> dnsLookup(DNSLookupRequest request) throws ServerNotFoundException {
        List<DNSLookupResponse> dnsLookupResponses = new ArrayList<>();
        for (String type : request.getRecordTypes()) {
            dnsLookupResponses.add(domainNameProcessor(request.getLookup(), type));
        }
        return dnsLookupResponses;
    }

    @Cacheable
    public List<DNSLookupResponse> cachedDnsLookup(DNSLookupRequest request) throws ServerNotFoundException {
        List<DNSLookupResponse> dnsLookupResponses = new ArrayList<>();
        for (String type : request.getRecordTypes()) {
            dnsLookupResponses.add(domainNameProcessor(request.getLookup(), type));
        }
        return dnsLookupResponses;
    }

    private DNSLookupResponse domainNameProcessor(String domainName, String lookupType) throws ServerNotFoundException {

        List<BaseType> baseTypes = new ArrayList<>();

        switch (lookupType) {
            case "A":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildARecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            case "AAAA":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildAAAARecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            case "TXT":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildTXTRecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            case "NS":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildNSRecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            case "MX":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildMXRecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            case "SOA":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildSOARecordForResponse(baseTypes, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(baseTypes).build();
            default:
                throw new UnsupportedDNSRecordLookupException("The DNS record type: " + lookupType + " is currently unsupported.");
        }
    }

    private void buildARecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .address(((ARecord) record).getAddress().getHostAddress())
                .ttl((record).getTTL())
                .build());
    }

    private void buildAAAARecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .address(((AAAARecord) record).getAddress().getHostAddress())
                .ttl((record).getTTL())
                .build());
    }

    private void buildTXTRecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .records((((TXTRecord) record).getStrings()))
                .ttl((record).getTTL())
                .build());
    }

    private void buildNSRecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .nsdName((record).getAdditionalName().toString())
                .ttl((record).getTTL())
                .build());
    }

    private void buildMXRecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .exchange((record).getAdditionalName().toString())
                .preference(((MXRecord) record).getPriority())
                .ttl((record).getTTL())
                .build());
    }

    private void buildSOARecordForResponse(List<BaseType> baseTypes, Record record) {
        baseTypes.add(BaseType.builder()
                .domain((record).getName().toString())
                .mName(((SOARecord) record).getHost().toString())
                .rName(((SOARecord) record).getAdmin().toString())
                .serial(((SOARecord) record).getSerial())
                .refresh(((SOARecord) record).getRefresh())
                .retry(((SOARecord) record).getRetry())
                .expire(((SOARecord) record).getExpire())
                .minimum(((SOARecord) record).getMinimum())
                .ttl((record).getTTL())
                .build());
    }
}
