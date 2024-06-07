package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.model.InitialRequest;
import online.jeweljoust.BE.service.InitialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class InitialAPI {

    @Autowired
    InitialService initialService;

    @GetMapping("/change-status-initial-by-id")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> changeStatusInitialById(InitialRequest initialRequest) {
        InitialValuation initialValuation = InitialService.changeStatusInitial(initialRequest);
        return ResponseEntity.ok(initialValuation);
    }

}
