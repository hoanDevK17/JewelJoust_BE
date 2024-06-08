package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    Account accountRequest;

    @OneToMany(mappedBy = "auctionRequest",cascade = CascadeType.ALL)
    List<AuctionSession> auctionSessions;

//    @OneToOne(mappedBy = "request_id",cascade = CascadeType.ALL)
//    Set<InitialValuation> initialValuations;
}
