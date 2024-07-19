package online.jeweljoust.BE.service;

import online.jeweljoust.BE.enums.AuctionBidStatus;
import online.jeweljoust.BE.respository.AuctionBidRepository;
import online.jeweljoust.BE.respository.AuctionRequestRepository;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    AuctionRequestRepository auctionRepository;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    AuctionSessionRepository auctionSessionRepository;

    @Autowired
    AuctionBidRepository auctionBidRepository;

    public Map<String, Long> totalDashboard() {
        Map<String, Long> reponse = new HashMap<>();
        long totalRequest = auctionRepository.countTotalAuctionRequests();
        long totalAccount = authenticationRepository.countTotalAccounts();
        long totalSession = auctionSessionRepository.countTotalAuctionSessions();
        long totalBid = auctionBidRepository.countAuctionBidsByStatus(AuctionBidStatus.WON);
        reponse.put("totalRequest", totalRequest);
        reponse.put("totalAccount", totalAccount);
        reponse.put("totalSession", totalSession);
        reponse.put("totalBid", totalBid);
        return reponse;
    }
}
