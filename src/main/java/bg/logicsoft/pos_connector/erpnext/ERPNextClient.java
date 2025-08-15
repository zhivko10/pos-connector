package bg.logicsoft.pos_connector.erpnext;

import bg.logicsoft.pos_connector.config.AppProperties;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ERPNextClient {
    private final WebClient client;
    private final AppProperties props;

    public ERPNextClient(WebClient ERPNextWebClient, AppProperties props) {
        this.client = ERPNextWebClient;
        this.props = props;
    }

    private String authHeader() {
        return "token " + props.getERPNext().getApiKey() + ":" + props.getERPNext().getApiSecret();
    }

    /**
     * Create a POS Invoice (or Sales Invoice with is_pos=1).
     * Uses /api/resource/POS Invoice if available; otherwise falls back to Sales Invoice.
     */
    public Mono<Map> createPosInvoiceDoc(PosInvoiceRequest req, String fiscalNumber, String qrCode) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("doctype", "Sales Invoice");
        doc.put("is_pos", 1);
        if (props.getERPNext().getCompany() != null) doc.put("company", props.getERPNext().getCompany());
        doc.put("customer", req.customer());
        doc.put("pos_profile", req.posProfile());
        doc.put("currency", req.currency() != null ? req.currency() : props.getERPNext().getDefaultCurrency());
        doc.put("remarks", "Fiscal Number: " + (fiscalNumber == null ? "" : fiscalNumber));

        // items
        List<Map<String, Object>> items = req.items().stream().map(i -> {
            Map<String, Object> m = new HashMap<>();
            m.put("item_code", i.itemCode());
            if (i.itemName() != null) m.put("item_name", i.itemName());
            m.put("qty", i.qty());
            m.put("rate", i.rate());
            if (i.discount() != null) {
                // ERPNext supports discount_amount (absolute) or discount_percentage
                m.put("discount_amount", i.discount());
            }
            return m;
        }).toList();
        doc.put("items", items);

        // taxes (optional)
        if (req.taxes() != null && !req.taxes().isEmpty()) {
            List<Map<String, Object>> taxes = req.taxes().stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("charge_type", "On Net Total");
                m.put("account_head", t.accountHead());
                m.put("rate", t.ratePercent());
                return m;
            }).toList();
            doc.put("taxes", taxes);
        }

        // payments
        Map<String, Object> payment = Map.of(
                "mode_of_payment", req.paymentType(),
                "amount", calculateGrandTotal(items)
        );
        doc.put("payments", List.of(payment));

        // custom fields for fiscal data (if you have custom fields in ERPNext, map here)
        doc.put("custom_qr_code", qrCode);
        doc.put("custom_fiscal_number", fiscalNumber);

        return client.post()
                .uri("/api/resource/Sales%20Invoice")
                .header("Authorization", authHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("data", doc))
                .retrieve()
                .bodyToMono(Map.class);
    }

    @SuppressWarnings("unchecked")
    private java.math.BigDecimal calculateGrandTotal(List<Map<String, Object>> items) {
        return items.stream()
                .map(m -> ((java.math.BigDecimal)m.get("rate")).multiply((java.math.BigDecimal)m.get("qty")))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }

    public Mono<Map> submitInvoice(String name) {
        // submit via submit? (docstatus=1) using /api/resource/<doctype>/<name> with PUT
        Map<String, Object> submitPayload = Map.of("docstatus", 1);
        return client.put()
                .uri("/api/resource/Sales%20Invoice/{name}", name)
                .header("Authorization", authHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("data", submitPayload))
                .retrieve()
                .bodyToMono(Map.class);
    }
}
