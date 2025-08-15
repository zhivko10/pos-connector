package bg.logicsoft.pos_connector.fpgate;

public record FiscalizeRequest(
        String posProfile,
        String customer,
        java.util.List<FiscalItem> items,
        String paymentType,
        String currency
) {
    public record FiscalItem(
            String itemCode,
            String itemName,
            java.math.BigDecimal qty,
            java.math.BigDecimal rate,
            java.math.BigDecimal discount
    ){}
}
