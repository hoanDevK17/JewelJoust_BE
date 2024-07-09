package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionBidStatus;
import online.jeweljoust.BE.enums.TransactionStatus;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class AuctionBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Double bid_price;
    @ManyToOne
    @JoinColumn(name="registration_id")
    AuctionRegistration auctionRegistration;
    @Temporal(TemporalType.DATE)
    Date bid_time;
    @Enumerated(EnumType.STRING)
    AuctionBidStatus status;
}
