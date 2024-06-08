package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString

public class AuctionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime requestdate;

    String jewelryname;

    String jewelrydescription;

    double jewelryinitialprice;

    @Enumerated(EnumType.STRING)
    AuctionRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Account accountRequest;

    @OneToOne(mappedBy = "auctionRequest",cascade = CascadeType.ALL)
    AuctionSession auctionSessionRequest;

    @OneToOne(mappedBy = "auctionRequestInitial",cascade = CascadeType.ALL)
    InitialValuation initialValuations;

    @OneToOne(mappedBy = "auctionRequestUltimate",cascade = CascadeType.ALL)
    UltimateValuation ultimateValuation;
}
