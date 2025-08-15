package bg.logicsoft.pos_connector.pos.dto;

public record PosInvoiceResponse(
        String erpnextName,
        String status,               // CREATED | SUBMITTED
        String fiscalNumber,
        String fiscalQRCode,
        String message
){}
