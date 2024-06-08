package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AuctionSessionStatus;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "auctionSession",cascade = CascadeType.ALL)
    List<AuctionRegistration> auctionRegistration;

    @Enumerated(EnumType.STRING)
    AuctionSessionStatus status;



}
