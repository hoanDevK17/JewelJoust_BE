package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    String status;

    String reason;
//
//    @ManyToOne
//    @JoinColumn(name = "staff_id")
//    Account accountInitial;
//
//    @OneToOne
//    @JoinColumn(name = "request_id")
//    AuctionRequest auctionRequest;

}
