package bg.logicsoft.pos_connector.fpgate;

import bg.logicsoft.pos_connector.config.AppProperties;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FPGateClient {
    private final WebClient client;
    private final AppProperties props;

    public FPGateClient(WebClient fpgateWebClient, AppProperties props) {
        this.client = fpgateWebClient;
        this.props = props;
    }

    public Mono<FiscalizeResponse> fiscalize(PosInvoiceRequest payload) {
        FiscalizeRequest req = new FiscalizeRequest(
                payload.posProfile(),
                payload.customer(),
                payload.items().stream().map(i -> new FiscalizeRequest.FiscalItem(
                        i.itemCode(), i.itemName(), i.qty(), i.rate(), i.discount()
                )).toList(),
                payload.paymentType(),
                payload.currency()
        );

        return client.post()
                .uri("/api/fiscalize") // TODO: adjust to real FPGate route
                .header("X-API-KEY", props.getFPGate().getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(FiscalizeResponse.class);
    }
}
