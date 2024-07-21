package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class DetailDashboardReponse {
    private String label;
    private long quantity;

    public DetailDashboardReponse(String label, long quantity) {
        this.label = label;
        this.quantity = quantity;
    }
}
