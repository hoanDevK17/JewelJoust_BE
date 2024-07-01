package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.model.RechargeRequestDTO;
import online.jeweljoust.BE.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class WalletAPI {
    @Autowired
    WalletService walletService;

    @PostMapping("createUrl")
    public ResponseEntity create(@RequestBody RechargeRequestDTO rechargeRequestDTO) throws Exception {
        String url = walletService.createUrl(rechargeRequestDTO);
        return ResponseEntity.ok(url);
    }

}
