package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionBid;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.model.DetailDashboardReponse;
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

    @GetMapping("/dashboard/auctionSession/{year}")
    public List<Long> sessionDashboard(@PathVariable("year") long year) {
        return dashboardService.sessionDashboard(year);
    }

    @GetMapping("/dashboard/auctionRequest/{year}")
    public List<Long> requestDashboard(@PathVariable("year") long year) {
        return dashboardService.requestDashboard(year);
    }

    @GetMapping("/dashboard/account/{year}")
    public List<Long> accountDashboard(@PathVariable("year") long year) {
        return dashboardService.accountDashboard(year);
    }

    @GetMapping("/dashboard/auctionBid/{year}")
    public List<Long> bidDashboard(@PathVariable("year") long year) {
        return dashboardService.bidDashboard(year);
    }

    @GetMapping("/dashboard/auctionRequest/detail")
    public List<DetailDashboardReponse> requestDetailDashboard() {
        return dashboardService.requestDetailDashboard();
    }

    @GetMapping("/dashboard/auctionSession/detail")
    public List<DetailDashboardReponse> sessionDetailDashboard() {
        return dashboardService.sessionDetailDashboard();
    }

    @GetMapping("/dashboard/account/detail")
    public List<DetailDashboardReponse> accountDetailDashboard() {
        return dashboardService.accountDetailDashboard();
    }

    @GetMapping("/dashboard/auctionSession/detail/{id}")
    public List<DetailDashboardReponse> getSessionDashboardById(@PathVariable("id") long id) {
        return dashboardService.sessionDashboardById(id);
    }
}
