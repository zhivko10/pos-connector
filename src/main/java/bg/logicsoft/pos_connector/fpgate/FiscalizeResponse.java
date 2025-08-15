package bg.logicsoft.pos_connector.fpgate;

public record FiscalizeResponse(
        String status,            // OK / ERROR
        String fiscalNumber,
        String qrCode,
        String message
){}
