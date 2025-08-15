package bg.logicsoft.pos_connector.pos.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record PosInvoiceRequest(
        @NotBlank String posProfile,
        @NotBlank String customer,
        @NotNull List<Item> items,
        List<Tax> taxes,
        @NotBlank String paymentType,      // e.g. CASH, CARD
        String currency                    // optional, defaults from config
) {
    public record Item(
            @NotBlank String itemCode,       // ERPNext Item Code or barcode mapping result
            String itemName,
            @NotNull @Positive BigDecimal qty,
            @NotNull @Positive BigDecimal rate,
            @PositiveOrZero BigDecimal discount, // absolute per-line discount (optional)
            @PositiveOrZero BigDecimal taxRate   // optional percent, if using per-line tax
    ) {}
    public record Tax(
            @NotBlank String accountHead,    // ERPNext account name for tax
            @NotNull @PositiveOrZero BigDecimal ratePercent
    ) {}
}
