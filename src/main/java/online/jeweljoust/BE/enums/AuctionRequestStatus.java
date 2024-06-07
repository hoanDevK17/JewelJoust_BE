package online.jeweljoust.BE.enums;

public enum AuctionRequestStatus {
    PENDING,        //chờ xác nhận
    CONFIRMED,      //đã xác nhận
    REJECTED,       //bị từ chối
    RECEIVED,       //đã nhận được hàng
    MISSED,         //trễ thời gian gửi hàng
    REVIEW,         //chờ xem xét (định giá chính thức)
    ACCEPTED,       //đã xem xét và xác nhận
    APPROVED,       //quản lý phê duyệt
    CANCEL,         //hủy bỏ
}
