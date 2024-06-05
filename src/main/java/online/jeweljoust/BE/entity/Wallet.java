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
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToOne
    @JoinColumn(name="account_id")
    Account account;

    Double balance;
    @Temporal(TemporalType.DATE)
    Date createAt;
    @Temporal(TemporalType.DATE)
    Date updateAt;
}
