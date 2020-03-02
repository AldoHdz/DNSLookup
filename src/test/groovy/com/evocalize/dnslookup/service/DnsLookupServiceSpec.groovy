package com.evocalize.dnslookup.service

import com.evocalize.dnslookup.model.BaseType
import com.evocalize.dnslookup.model.DNSLookupRequest
import com.evocalize.dnslookup.model.DNSLookupResponse
import spock.lang.Specification

class DnsLookupServiceSpec extends Specification{
    DNSLookupService dnsLookupService = Mock()


    def "test spec"(){
        given:
        DNSLookupRequest dnsLookupRequest = DNSLookupRequest.builder().lookup("www.google.com").recordTypes(["A"]).build()
        DNSLookupResponse dnsLookupResponse = DNSLookupResponse.builder().response([BaseType.builder().domain("www.gogole.com").ttl(30).t])

        when:
        dnsLookupService.dnsLookup(dnsLookupRequest)

        then:

        null


    }

}
