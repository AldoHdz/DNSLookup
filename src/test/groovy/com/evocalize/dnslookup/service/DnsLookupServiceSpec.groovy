import com.evocalize.dnslookup.connector.DNSJavaConnector
import com.evocalize.dnslookup.model.BaseType
import com.evocalize.dnslookup.model.DNSLookupRequest
import com.evocalize.dnslookup.model.DNSLookupResponse
import com.evocalize.dnslookup.service.DNSLookupService
import org.xbill.DNS.AAAARecord
import org.xbill.DNS.ARecord
import org.xbill.DNS.MXRecord
import org.xbill.DNS.NSRecord
import org.xbill.DNS.Name
import org.xbill.DNS.SOARecord
import org.xbill.DNS.TXTRecord
import spock.lang.Specification

class DNSLookupServiceSpec extends Specification {

    public static final List<DNSLookupResponse> A_RECORD = [DNSLookupResponse.builder()
                                                                    .type("A")
                                                                    .response([BaseType.builder().domain("google.com.").address("0.0.0.0").ttl(30).build()])
                                                                    .build()]

    public static final List<DNSLookupResponse> AAAA_RECORD = [DNSLookupResponse.builder()
                                                                       .type("AAAA")
                                                                       .response([BaseType.builder().domain("google.com.").address("0:0:0:0:0:0:0:0").ttl(189).build()])
                                                                       .build()]

    public static final List<DNSLookupResponse> TXT_RECORD = [DNSLookupResponse.builder()
                                                                      .type("TXT")
                                                                      .response([BaseType.builder().domain("google.com.").records(["facebook-domain-verification=22rm551cu4k0ab0bxsw536tlds4h95"]).ttl(189).build(),
                                                                                 BaseType.builder().domain("google.com.").records(["lobalsign-smime-dv=CDYX+XFHUw2wml6/Gb8+59BsH31KzUr6c1l2BPvqKX8="]).ttl(189).build(),
                                                                                 BaseType.builder().domain("google.com.").records(["docusign=05958488-4752-4ef2-95eb-aa7ba8a3bd0e"]).ttl(189).build(),
                                                                                 BaseType.builder().domain("google.com.").records(["docusign=1b0a6754-49b1-4db5-8540-d2c12664b289"]).ttl(189).build(),
                                                                                 BaseType.builder().domain("google.com.").records(["v=spf1 include:_spf.google.com ~all"]).ttl(189).build()])
                                                                      .build()]

    public static final List<DNSLookupResponse> NS_RECORD = [DNSLookupResponse.builder()
                                                                     .type("NS")
                                                                     .response([BaseType.builder().domain("google.com.").nsdName("ns3.google.com.").ttl(331576).build(),
                                                                                BaseType.builder().domain("google.com.").nsdName("ns1.google.com.").ttl(331576).build(),
                                                                                BaseType.builder().domain("google.com.").nsdName("ns2.google.com.").ttl(331576).build(),
                                                                                BaseType.builder().domain("google.com.").nsdName("ns4.google.com.").ttl(331576).build()])
                                                                     .build()]

    public static final List<DNSLookupResponse> MX_RECORD = [DNSLookupResponse.builder()
                                                                     .type("MX")
                                                                     .response([BaseType.builder().domain("google.com.").exchange("aspmx.l.google.com.").ttl(417).preference(10).build(),
                                                                                BaseType.builder().domain("google.com.").exchange("alt1.aspmx.l.google.com.").ttl(417).preference(20).build(),
                                                                                BaseType.builder().domain("google.com.").exchange("alt2.aspmx.l.google.com.").ttl(417).preference(30).build(),
                                                                                BaseType.builder().domain("google.com.").exchange("alt3.aspmx.l.google.com.").ttl(417).preference(40).build(),
                                                                                BaseType.builder().domain("google.com.").exchange("alt4.aspmx.l.google.com.").ttl(417).preference(50).build()])
                                                                     .build()]

    public static final List<DNSLookupResponse> SOA_RECORD = [DNSLookupResponse.builder()
                                                                      .type("SOA")
                                                                      .response([BaseType.builder().domain("google.com.")
                                                                                         .ttl(21)
                                                                                         .serial(298310260)
                                                                                         .refresh(900)
                                                                                         .retry(900)
                                                                                         .expire(1800)
                                                                                         .minimum(60)
                                                                                         .mName("ns1.google.com.")
                                                                                         .rName("dns-admin.google.com.").build()])
                                                                      .build()]


    DNSJavaConnector dnsJavaConnector = Mock()
    DNSLookupService dnsLookupService = new DNSLookupService(dnsJavaConnector)

    def "DNSLookup returns expected data when A record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["A"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new ARecord(new Name("google.com."), 1, 30, new Inet4Address())]

        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        A_RECORD == actual
    }

    def "DNSLookup returns expected data when AAAA record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["AAAA"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new AAAARecord(new Name("google.com."), 1, 189, new Inet6Address())]

        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        AAAA_RECORD == actual
    }

    def "DNSLookup returns expected data when TXT record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["TXT"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new TXTRecord(new Name("google.com."), 1, 189, ["facebook-domain-verification=22rm551cu4k0ab0bxsw536tlds4h95"]),
                                                new TXTRecord(new Name("google.com."), 1, 189, ["lobalsign-smime-dv=CDYX+XFHUw2wml6/Gb8+59BsH31KzUr6c1l2BPvqKX8="]),
                                                new TXTRecord(new Name("google.com."), 1, 189, ["docusign=05958488-4752-4ef2-95eb-aa7ba8a3bd0e"]),
                                                new TXTRecord(new Name("google.com."), 1, 189, ["docusign=1b0a6754-49b1-4db5-8540-d2c12664b289"]),
                                                new TXTRecord(new Name("google.com."), 1, 189, ["v=spf1 include:_spf.google.com ~all"])]

        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        TXT_RECORD == actual
    }

    def "DNSLookup returns expected data when MX record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["NS"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new NSRecord(new Name("google.com."), 1, 331576, new Name("ns3.google.com.")),
                                                new NSRecord(new Name("google.com."), 1, 331576, new Name("ns1.google.com.")),
                                                new NSRecord(new Name("google.com."), 1, 331576, new Name("ns2.google.com.")),
                                                new NSRecord(new Name("google.com."), 1, 331576, new Name("ns4.google.com."))]

        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        NS_RECORD == actual
    }

    def "DNSLookup returns expected data when NS record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["MX"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new MXRecord(new Name("google.com."), 1, 417, 10, new Name("aspmx.l.google.com.")),
                                                new MXRecord(new Name("google.com."), 1, 417, 20, new Name("alt1.aspmx.l.google.com.")),
                                                new MXRecord(new Name("google.com."), 1, 417, 30, new Name("alt2.aspmx.l.google.com.")),
                                                new MXRecord(new Name("google.com."), 1, 417, 40, new Name("alt3.aspmx.l.google.com.")),
                                                new MXRecord(new Name("google.com."), 1, 417, 50, new Name("alt4.aspmx.l.google.com."))]

        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        MX_RECORD == actual
    }

    def "DNSLookup returns expected data when SOA record is specified"() {
        given:
        def request = new DNSLookupRequest.DNSLookupRequestBuilder().lookup("www.google.com").recordTypes(["SOA"]).build()
        dnsJavaConnector.runDNSLookup(_, _) >> [new SOARecord(new Name("google.com."),
                1,
                21,
                new Name("ns1.google.com."),
                new Name("dns-admin.google.com."),
                298310260,
                900,
                900,
                1800,
                60)]
        when:
        def actual = dnsLookupService.dnsLookup(request)

        then:
        SOA_RECORD == actual
    }
}