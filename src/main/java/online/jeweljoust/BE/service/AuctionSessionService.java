package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionSession;

import online.jeweljoust.BE.respository.AuctionSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuctionSessionService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    AuctionSessionRepository autionSessionRepository;
    public List<AuctionSession> getAllAutionSessions(){
        return autionSessionRepository.findAll();
    }
    public AuctionSession addAutionSessions(AuctionSession auctionSession){
        return autionSessionRepository.save(auctionSession);
    }
}
