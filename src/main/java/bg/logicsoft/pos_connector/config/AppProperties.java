package bg.logicsoft.pos_connector.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "pos_connector")
public class AppProperties {
    private ERPNextProperties ERPNext = new ERPNextProperties();
    private FPGateProperties FPGate = new FPGateProperties();
//    private SecurityProperties security = new SecurityProperties();

    public ERPNextProperties getERPNext() { return ERPNext; }
    public void setERPNext(ERPNextProperties ERPNext) { this.ERPNext = ERPNext; }
    public FPGateProperties getFPGate() { return FPGate; }
    public void setFPGate(FPGateProperties FPGate) { this.FPGate = FPGate; }

    public static class ERPNextProperties {
        private String url;
        private String apiKey;
        private String apiSecret;
        private boolean submitAfterCreate = true;
        private String company;
        private String defaultCurrency = "EUR";
        // getters/setters
        public String getBaseUrl() { return url; }
        public void setBaseUrl(String baseUrl) { this.url = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getApiSecret() { return apiSecret; }
        public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }
        public boolean isSubmitAfterCreate() { return submitAfterCreate; }
        public void setSubmitAfterCreate(boolean submitAfterCreate) { this.submitAfterCreate = submitAfterCreate; }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getDefaultCurrency() { return defaultCurrency; }
        public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency = defaultCurrency; }
    }

    public static class FPGateProperties {
        private String url;
        private String port;
        private String apiKey;
        private int timeoutSeconds = 20;
        // getters/setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getPort() { return port; }
        public void setPort(String port) { this.port = port; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }

    // TODO: Remove ?!?!?!?
//  public static class SecurityProperties {
//        private java.util.List<String> allowedOrigins = java.util.List.of("*");
//        public java.util.List<String> getAllowedOrigins() { return allowedOrigins; }
//        public void setAllowedOrigins(java.util.List<String> allowedOrigins) { this.allowedOrigins = allowedOrigins; }
//    }
}
