package com.evocalize.dnslookup.connector;

import com.evocalize.dnslookup.exception.ServerNotFoundException;
import com.evocalize.dnslookup.exception.TextParsingException;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

@Component
public class DNSJavaConnector {
    public Record[] runDNSLookup(String domainName, String lookupType) {
        Record[] lookupRecords;

        try {
            lookupRecords = new Lookup(domainName, Type.value(lookupType)).run();
            if (lookupRecords == null) {
                throw new ServerNotFoundException("DNSLookup was unable to resolve the domainName: " + domainName + " for recordTYpe: " + lookupType);
            }

        } catch (TextParseException e) {
            throw new TextParsingException("DnsLookup was unable to parse the text: " + domainName);
        }
        return lookupRecords;
    }

}
