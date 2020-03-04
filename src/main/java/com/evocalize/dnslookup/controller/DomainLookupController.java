package com.evocalize.dnslookup.controller;

import com.evocalize.dnslookup.exception.ServerNotFoundException;
import com.evocalize.dnslookup.model.DNSLookupRequest;
import com.evocalize.dnslookup.model.DNSLookupResponse;
import com.evocalize.dnslookup.service.DNSLookupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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
    @ApiOperation(value = "Return specified DNS Info")
    public List<DNSLookupResponse> dnsLookup(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.dnsLookup(request);

    }

    @PostMapping("/securedEndpoint")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @ApiOperation(value = "Return specified DNS info but through an endpoint that requires a login")
    public List<DNSLookupResponse> securedDnsLookup(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.dnsLookup(request);

    }

    @PostMapping("/cacheableEndpoint")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @ApiOperation(value = "Return specified DNS info but through an endpoint that uses a cache")
    public List<DNSLookupResponse> cachedEndpoint(@RequestBody DNSLookupRequest request) throws ServerNotFoundException {
        log.info(request.toString());
        return dnsLookupService.cachedDnsLookup(request);

    }
}
