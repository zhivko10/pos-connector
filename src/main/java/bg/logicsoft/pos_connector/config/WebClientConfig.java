package bg.logicsoft.pos_connector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean(name = "erpnextWebClient")
    public WebClient erpnextWebClient(AppProperties props) {
        HttpClient http = HttpClient.create().responseTimeout(Duration.ofSeconds(30));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(http))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(4 * 1024 * 1024))
                        .build())
                .baseUrl(props.getERPNext().getBaseUrl())
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean(name = "fpgateWebClient")
    public WebClient fpgateWebClient(AppProperties props) {
        HttpClient http = HttpClient.create().responseTimeout(Duration.ofSeconds(props.getFPGate().getTimeoutSeconds()));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(http))
                .baseUrl(props.getFPGate().getUrl())
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
