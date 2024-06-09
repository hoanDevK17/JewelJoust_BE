package online.jeweljoust.BE.enums;

public enum AuctionRegistrationStatus {
    PENDING,
    DEPOSITED,
    WAITTINGFORPAYMENT,
    COMPLETED,
    REFUNDED,
    CANCELLED;
// 2 luoồng : PENDING - DEPOSIT(cannot cancel when session BIDDING)-REFUNDED
    //:
}
