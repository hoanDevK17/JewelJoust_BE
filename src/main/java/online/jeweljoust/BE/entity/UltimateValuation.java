package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.UltimateValuationsStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString

public class UltimateValuation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Date ultimatedate;

    Date approvaldanagerdate;

    @Enumerated(EnumType.STRING)
    UltimateValuationsStatus status;

    String reason;

    double price;

    String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "staff_id_sending")
    Account ultimateStaff;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    Account ultimateManager;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "request_id")
    AuctionRequest auctionRequestUltimate;

}
