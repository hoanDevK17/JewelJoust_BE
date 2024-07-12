package online.jeweljoust.BE.enums;

public enum AuctionRegistrationStatus {
    PENDING,
    INITIALIZED,
    ACTIVE,
    WON,

    REFUNDED,
    CANCELLED;
// 2 luoồng : PENDING - ACTIVE(cannot cancel when session BIDDING)   ->REFUNDED hoặc là
//                                                                    -> WON-> COMPLETED(GIAO HÀNG THÀNH CÔNG)
    //:
}
