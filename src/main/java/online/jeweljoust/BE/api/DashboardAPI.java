package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionBid;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.service.AuctionBidService;
import online.jeweljoust.BE.service.AuthenticationService;
import online.jeweljoust.BE.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class DashboardAPI {
    @Autowired
    DashboardService dashboardService;

    @GetMapping("/dashboard")
    public Map<String, Long> totalDashboard() {
        return dashboardService.totalDashboard();
    }

}
