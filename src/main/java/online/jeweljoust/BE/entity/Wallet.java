package online.jeweljoust.BE.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

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
    @JsonIgnore
    Account accountWallet;

    Double balance;

    @Temporal(TemporalType.DATE)
    Date createAt;
    @OneToMany(mappedBy = "wallet",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Transaction> transactions;
}
