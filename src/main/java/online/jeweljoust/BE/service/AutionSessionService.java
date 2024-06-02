package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AutionSession;

import online.jeweljoust.BE.respository.AutionSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AutionSessionService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    AutionSessionRepository autionSessionRepository;
    public List<AutionSession> getAllAutionSessions(){
        return autionSessionRepository.findAll();
    }
    public AutionSession addAutionSessions(AutionSession autionSession){
        return autionSessionRepository.save(autionSession);
    }
}
