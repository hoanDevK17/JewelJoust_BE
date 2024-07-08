package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Getter
@Setter
@Data

public class ResourceRequest {
    String path;
    String description;
}
