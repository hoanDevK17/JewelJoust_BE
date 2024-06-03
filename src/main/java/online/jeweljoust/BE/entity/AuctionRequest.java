package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

public class AuctionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long auctionrequestid;

    long userid;

    @Column(nullable = false)
    LocalDateTime requestdate;

    String jewelryname;

    String jewelrydescription;

    double jewelryinitialprice;

    String status;

}