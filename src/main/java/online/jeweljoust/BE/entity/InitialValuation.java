package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.InitialValuationsStatus;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString

public class InitialValuation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Date initialdate;

    @Enumerated(EnumType.STRING)
    InitialValuationsStatus status;

    double price;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "staff_id")
    Account accountInitial;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "request_id")
    AuctionRequest auctionRequestInitial;

}
