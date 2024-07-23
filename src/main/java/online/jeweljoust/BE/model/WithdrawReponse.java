package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter

public class WithdrawReponse {
    private long id;
    private List<ResourceRequest> resourceRequestList;
}
