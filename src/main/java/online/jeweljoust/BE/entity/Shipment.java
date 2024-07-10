package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString

public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Date date;

    @Enumerated(EnumType.STRING)
    ShipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    Account accountShipment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "request_id")
    AuctionRequest auctionRequestShipment;

}
