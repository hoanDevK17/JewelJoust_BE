package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    String status;
    @OneToOne (mappedBy = "auctionRequest",cascade = CascadeType.ALL)
    AuctionSession auctionSession;
    @ManyToOne
    @JoinColumn(name = "member_id")
    Account account;

    @OneToOne(mappedBy = "request_id",cascade = CascadeType.ALL)
    Set<InitialValuation> initialValuations;


}
