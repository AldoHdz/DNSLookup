package com.evocalize.dnslookup.controller;

import com.evocalize.dnslookup.exception.ServerNotFoundException;
import com.evocalize.dnslookup.model.DNSLookupRequest;
import com.evocalize.dnslookup.model.DNSLookupResponse;
import com.evocalize.dnslookup.service.DNSLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class DomainLookupController {
    private final DNSLookupService dnsLookupService;


    @Autowired
    public DomainLookupController(DNSLookupService dnsLookupService){
        this.dnsLookupService = dnsLookupService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public List<DNSLookupResponse> dnsLookup(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.dnsLookup(request);

    }

    @PostMapping("/securedEndpoint")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public List<DNSLookupResponse> securedDnsLookup(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.dnsLookup(request);

    }

    @PostMapping("/cacheableEndpoint")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public List<DNSLookupResponse> cachedEndpoint(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.cachedDnsLookup(request);

    }
}
