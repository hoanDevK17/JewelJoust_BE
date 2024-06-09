package online.jeweljoust.BE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionSession;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Getter
@Setter
public class AuctionRegistrationRequest {

 long auctionSession_id;
long member_id;
}
