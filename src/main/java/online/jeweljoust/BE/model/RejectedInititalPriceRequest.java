package online.jeweljoust.BE.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RejectedInititalPriceRequest {
    long id;
    String reason;

}
