package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @JoinColumn(name = "member_id")
    Account accountRequest;

    @OneToMany(mappedBy = "auctionRequest",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionSession> auctionSessions;

    @OneToOne(mappedBy = "auctionRequestInitial",cascade = CascadeType.ALL)
    InitialValuation initialValuations;

    @OneToOne(mappedBy = "auctionRequestUltimate",cascade = CascadeType.ALL)
    UltimateValuation ultimateValuation;

    @OneToMany(mappedBy = "auctionRequestResource",cascade = CascadeType.ALL)
    Set<Resources> resources;
}
