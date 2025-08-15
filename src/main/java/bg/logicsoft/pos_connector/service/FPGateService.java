package bg.logicsoft.pos_connector.service;

import bg.logicsoft.pos_connector.model.FPGateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FPGateService {
    private final RestTemplate restTemplate;

    @Value("${fpGate.url}")
    private String fpUrl;

    public FPGateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSaleToFPGate() {
        FPGateRequest request = new FPGateRequest();
        restTemplate.postForEntity(fpUrl, request, String.class);
    }
}
