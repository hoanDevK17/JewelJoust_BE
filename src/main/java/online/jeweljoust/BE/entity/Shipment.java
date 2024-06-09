package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDateTime receiveddate;

    @Enumerated(EnumType.STRING)
    AuctionRequestStatus.shipmentStatus status;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "staff_id_received")
    Account accountShipment;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "initial_id")
    InitialValuation initialShipment;

}
