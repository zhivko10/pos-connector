package bg.logicsoft.pos_connector.controllers;

import bg.logicsoft.pos_connector.pos.PosInvoiceService;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceRequest;
import bg.logicsoft.pos_connector.pos.dto.PosInvoiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pos-invoices")
public class PosInvoiceController {
    private final PosInvoiceService service;

    public PosInvoiceController(PosInvoiceService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public reactor.core.publisher.Mono<PosInvoiceResponse> create(@Valid @RequestBody PosInvoiceRequest request) {
        return service.create(request);
    }
}
