package online.jeweljoust.BE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.enums.TransactionType;

import java.util.Date;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    double amount;
    @ManyToOne
    @JoinColumn(name="registration_id")
    AuctionRegistration auctionRegistration;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    Wallet wallet;

    @Enumerated(EnumType.STRING)
    TransactionType transaction_type;
    Date date;
    String description;
    @Enumerated(EnumType.STRING)
    TransactionStatus status;



}
