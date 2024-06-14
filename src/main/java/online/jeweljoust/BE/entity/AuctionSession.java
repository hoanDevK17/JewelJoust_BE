package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AuctionSessionStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class AuctionSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    Account managerSession;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    Account staffSession;
    @Temporal(TemporalType.DATE)
    Date start_time;

    @Temporal(TemporalType.DATE)
    Date end_time;

    @Temporal(TemporalType.DATE)

    String nameSession;

    String nameJewelry;
    double initialPrice;

    double minStepPrice;
    double FeeAmount;

    double depositAmount;


    String description;

    Date createAt;


    @ManyToOne
    @JoinColumn(name="auctionRequest_id")

    AuctionRequest auctionRequest;
//    @JsonProperty("auctionRequest_id")
//    public Long getAuctionRequestId() {
//        return auctionRequest != null ? auctionRequest.getId() : null;
//    }
    @OneToMany(mappedBy = "auctionSession",cascade = CascadeType.ALL)
    Set<AuctionRegistration> auctionRegistration;

    @Enumerated(EnumType.STRING)
    AuctionSessionStatus status;

//    @OneToMany(mappedBy = "auctionSessionResource",cascade = CascadeType.ALL)
//    Set<Resources> resources;

}
