package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
    @OneToOne
    @JoinColumn(name="auctionRequest_id")
    AuctionRequest auctionRequest;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    Account manager;
    @JoinColumn(name = "staff_id")
    Account staff;

    @Temporal(TemporalType.DATE)
    Date start_time;
    @Temporal(TemporalType.DATE)
    Date end_time;
    @Temporal(TemporalType.DATE)
    Date create_at;
    double initial_price;
    double min_stepPrice;
    double deposit_amount;
    double Fee_amount;
    String name_session;
    String name_jewelry;
    String description;
    @OneToMany(mappedBy = "auctionSession",cascade = CascadeType.ALL)
    List<AuctionRegistration> auctionRegistrations;

    @Enumerated(EnumType.STRING)
    AuctionSessionStatus status;
//    AccountRole role;


}
