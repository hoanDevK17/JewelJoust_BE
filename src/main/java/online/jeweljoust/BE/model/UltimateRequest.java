package online.jeweljoust.BE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.enums.AuctionRequestStatus;

import java.time.LocalDateTime;

@Data
@Getter
@Setter

public class UltimateRequest {
    AuctionRequestStatus.ultimateStatus status;
    String reason;
    double price;
    String description;
}

