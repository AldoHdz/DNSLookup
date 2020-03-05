package com.evocalize.dnslookup.service;

import com.evocalize.dnslookup.connector.DNSJavaConnector;
import com.evocalize.dnslookup.exception.ServerNotFoundException;
import com.evocalize.dnslookup.exception.UnsupportedDNSRecordLookupException;
import com.evocalize.dnslookup.model.ARecord;
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

        List<Object> records = new ArrayList<>();

        switch (lookupType) {
            case "A":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildARecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            case "AAAA":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildAAAARecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            case "TXT":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildTXTRecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            case "NS":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildNSRecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            case "MX":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildMXRecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            case "SOA":
                for (Record record : dnsJavaConnector.runDNSLookup(domainName, lookupType)) {
                    buildSOARecordForResponse(records, record);
                }
                return DNSLookupResponse.builder().type(lookupType).response(records).build();
            default:
                throw new UnsupportedDNSRecordLookupException("The DNS record type: " + lookupType + " is currently unsupported.");
        }
    }

    private void buildARecordForResponse(List<Object> baseTypes, Record record) {
        baseTypes.add(ARecord.builder().domain((record).getName().toString())
                .address(((org.xbill.DNS.ARecord) record).getAddress().getHostAddress())
                .ttl(record.getTTL())
                .build());
    }

    private void buildAAAARecordForResponse(List<Object> baseTypes, Record record) {

        baseTypes.add(com.evocalize.dnslookup.model.AAAARecord.builder()
                .domain(record.getName().toString())
                .address(((AAAARecord) record).getAddress().getHostAddress())
                .ttl(record.getTTL())
                .build());


    }

    private void buildTXTRecordForResponse(List<Object> baseTypes, Record record) {

        baseTypes.add(com.evocalize.dnslookup.model.TXTRecord.builder()
                .domain((record).getName().toString())
                .records((((TXTRecord) record).getStrings()))
                .ttl(record.getTTL())
                .build());
    }

    private void buildNSRecordForResponse(List<Object> baseTypes, Record record) {

        baseTypes.add(com.evocalize.dnslookup.model.NSRecord.builder()
                .domain(record.getName().toString())
                .nsdName(record.getAdditionalName().toString())
                .ttl(record.getTTL())
                .build());

    }

    private void buildMXRecordForResponse(List<Object> baseTypes, Record record) {

        baseTypes.add(com.evocalize.dnslookup.model.MXRecord.builder()
                .domain(record.getName().toString())
                .exchange(record.getAdditionalName().toString())
                .preference(((MXRecord) record).getPriority())
                .ttl(record.getTTL())
                .build());
    }

    private void buildSOARecordForResponse(List<Object> baseTypes, Record record) {

        baseTypes.add(com.evocalize.dnslookup.model.SOARecord.builder()
                .domain(record.getName().toString())
                .mName(((SOARecord) record).getHost().toString())
                .rName(((SOARecord) record).getAdmin().toString())
                .serial(((SOARecord) record).getSerial())
                .refresh(((SOARecord) record).getRefresh())
                .retry(((SOARecord) record).getRetry())
                .expire(((SOARecord) record).getExpire())
                .minimum(((SOARecord) record).getMinimum())
                .ttl(record.getTTL())
                .build());
    }
}
