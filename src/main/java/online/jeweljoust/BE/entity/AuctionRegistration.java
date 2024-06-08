package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString

public class AuctionRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Temporal(TemporalType.DATE)
    Date create_at;

    String status;

    @ManyToOne
    @JoinColumn(name="auctionSession_id")
    AuctionSession auctionSession;

    @ManyToOne
    @JoinColumn(name="member_id")
    Account accountRegistration;
}
