package online.jeweljoust.BE.service;

import online.jeweljoust.BE.enums.AuctionBidStatus;
import online.jeweljoust.BE.model.DetailDashboardReponse;
import online.jeweljoust.BE.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;

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

    public List<Long> sessionDashboard(long year) {
        List<Object[]> result = auctionSessionRepository.countSessionsByMonth(year);
        return tempDashboard(result);
    }

    public List<Long> requestDashboard(long year) {
        List<Object[]> result = auctionRepository.countRequestsByMonth(year);
        return tempDashboard(result);
    }

    public List<Long> accountDashboard(long year) {
        List<Object[]> result = authenticationRepository.countAccountsByMonth(year);
        return tempDashboard(result);
    }

    public List<Long> bidDashboard(long year) {
        List<Object[]> result = auctionBidRepository.countAuctionBidsByMonth(year);
        return tempDashboard(result);
    }

    public List<Long> tempDashboard(List<Object[]> result){
        List<Long> counts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            counts.add(0L);
        }

        for (Object[] row : result) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            counts.set(month - 1, (long) count.intValue());
        }
        return counts;
    }

    public List<DetailDashboardReponse> requestDetailDashboard() {
        List<Object[]> results = auctionRepository.countAuctionRequestsByStatus();
        return tempDetailDashboard(results);
    }

    public List<DetailDashboardReponse> sessionDetailDashboard() {
        List<Object[]> results = auctionSessionRepository.countAuctionSessionsByStatus();
        return tempDetailDashboard(results);
    }

    public List<DetailDashboardReponse> accountDetailDashboard() {
        long totalAccountRegis = auctionRegistrationRepository.countDistinctMemberId();
        long totalAccount = authenticationRepository.countTotalAccounts();
        List<DetailDashboardReponse> accountList = new ArrayList<>();
        accountList.add(new DetailDashboardReponse("Account Registration", totalAccountRegis));
        accountList.add(new DetailDashboardReponse("Total Account", totalAccount));
        return accountList;
    }

    public List<DetailDashboardReponse> tempDetailDashboard(List<Object[]> results) {
        List<DetailDashboardReponse> statusCountList = new ArrayList<>();
        for (Object[] result : results) {
            String label = (String) result[0];
            Long quantity = ((Number) result[1]).longValue();
            statusCountList.add(new DetailDashboardReponse(label, quantity));
        }
        return statusCountList;
    }

    public List<DetailDashboardReponse> sessionDashboardById(long id) {
        List<DetailDashboardReponse> reponseList = new ArrayList<>();
            long totalReg = auctionRegistrationRepository.countRegistrationById(id);
            long totalBid = auctionBidRepository.countBidsByAuctionSessionId(id);
            long totalPri = auctionBidRepository.sumHighestBidsByAuctionSessionId(id);
            reponseList.add(new DetailDashboardReponse("Total Registration", totalReg));
            reponseList.add(new DetailDashboardReponse("Total Bidding", totalBid));
            reponseList.add(new DetailDashboardReponse("Total Price", totalPri));
        return reponseList;
    }
}
