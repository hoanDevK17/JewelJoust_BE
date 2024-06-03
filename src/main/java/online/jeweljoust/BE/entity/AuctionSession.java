package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Entity
@Getter
@Setter
@ToString
public class AuctionSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long autionSessionid;
    long auction_request_id;
    long manager_id;
    long staff_id;
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
    String name_jewelrys;
    String description;
    String status;

}
