package bg.logicsoft.pos_connector.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class POSInvoice {
    @GetMapping("/pos-invoice")
    public ResponseEntity<Map<String, Object>> posInvoice() {
        Map<String, Object> posInvoiceResp = new HashMap<>();

        posInvoiceResp.put("pos-invoice", "OK");
        posInvoiceResp.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(posInvoiceResp);
    }
}
