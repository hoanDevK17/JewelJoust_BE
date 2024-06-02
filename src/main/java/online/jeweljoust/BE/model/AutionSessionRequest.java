package online.jeweljoust.BE.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
@Getter
@Setter
public class AutionSessionRequest {


    long staff_id;
    @Temporal(TemporalType.DATE)
    Date start_time;
    @Temporal(TemporalType.DATE)
    Date end_time;
    double initial_price;
    double min_stepPrice;
    double deposit_amount;
    double Fee_amount;
    String name_session;
    String name_jewelrys;
    String description;

}
