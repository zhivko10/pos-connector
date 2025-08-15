package bg.logicsoft.pos_connector.pos;

import bg.logicsoft.pos_connector.erpnext.ERPNextClient;
import bg.logicsoft.pos_connector.fpgate.FPGateClient;
import bg.logicsoft.pos_connector.fpgate.FiscalizeResponse;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceRequest;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceResponse;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PosInvoiceService {
    private static final Logger log = LoggerFactory.getLogger(PosInvoiceService.class);
    private final FPGateClient fpGate;
    private final ERPNextClient erp;
    private final bg.logicsoft.pos_connector.config.AppProperties props;

    public PosInvoiceService(FPGateClient fpGate, ERPNextClient erp, bg.logicsoft.pos_connector.config.AppProperties props) {
        this.fpGate = fpGate; this.erp = erp; this.props = props;
    }

    public reactor.core.publisher.Mono<PosInvoiceResponse> create(PosInvoiceRequest request) {
        // 1) Fiscalize via FPGate
        return fpGate.fiscalize(request)
                .flatMap(fiscal -> {
                    if (fiscal == null || (fiscal.status() != null && !fiscal.status().equalsIgnoreCase("OK"))) {
                        String msg = fiscal == null ? "No response from FPGate" : fiscal.message();
                        return reactor.core.publisher.Mono.error(new RuntimeException("FPGate fiscalization failed: " + msg));
                    }
                    // 2) Create in ERPNext
                    return erp.createPosInvoiceDoc(request, fiscal.fiscalNumber(), fiscal.qrCode())
                            .flatMap(created -> {
                                String name = (String)((java.util.Map<String, Object>)created.get("data")).get("name");
                                if (props.getERPNext().isSubmitAfterCreate()) {
                                    return erp.submitInvoice(name).map(submitted -> new PosInvoiceResponse(
                                            name,
                                            "SUBMITTED",
                                            fiscal.fiscalNumber(),
                                            fiscal.qrCode(),
                                            "POS invoice created and submitted"
                                    ));
                                } else {
                                    return reactor.core.publisher.Mono.just(new PosInvoiceResponse(
                                            name,
                                            "CREATED",
                                            fiscal.fiscalNumber(),
                                            fiscal.qrCode(),
                                            "POS invoice created (draft)"
                                    ));
                                }
                            });
                });
    }
}
