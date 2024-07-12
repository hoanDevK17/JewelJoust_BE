package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AuctionRegistrationStatus;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString

public class AuctionRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Temporal(TemporalType.DATE)
    Date create_at;

    @Enumerated(EnumType.STRING)
    AuctionRegistrationStatus status;
    //    Khởi tạo (chờ đặt cọc),đã đặt cọc, đã hủy, đấu giá thành công, đã hoàn tiền đấu giá
//Pending,Deposited,Cancelled,WaitingForPayment(chờ thanh toán tiền),
// Refunded,Completed(khi thanh toán thành công, và kết thúc đấu giá)
    @ManyToOne
    @JoinColumn(name="auctionSession_id")

    AuctionSession auctionSession;
//    @JsonProperty("auctionSession_id")
//    public Long getAuctionSessionId() {
//        return auctionSession != null ? auctionSession.getId() : null;
//    }
    @ManyToOne
    @JoinColumn(name="member_id")
    Account accountRegistration;
//    @OneToMany(mappedBy = "auctionRegistration",cascade = CascadeType.ALL)
//    @JsonIgnore
//    Set<Transaction> transactions;
    @OneToMany(mappedBy = "auctionRegistration",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionBid> auctionBids;
}
