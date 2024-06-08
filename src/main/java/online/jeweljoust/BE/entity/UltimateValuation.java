package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

public class UltimateValuation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime ultimatedate;

    AuctionRequestStatus status;

    String reason;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    Account accountUltimate;

    @ManyToOne
    @JoinColumn(name = "request_id")
    AuctionRequest auctionRequestUltimate;

}
