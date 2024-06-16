package online.jeweljoust.BE.enums;

public enum AuctionRequestStatus {

        PENDING,        //chờ xác nhận
        CONFIRMED,      //đã xác nhận
        REJECTED,       //bị từ chối
        CANCEL,         //hủy bỏ
        RECEIVED,       //đã nhận được hàng
        MISSED,         //trễ thời gian gửi hàng
        REVIEW,         //chờ xem xét (đánh giá chính thức)
        UNACCEPTED,       //đã xem xét và không chấp nhận (đánh giá chính thức)
        UNAPPROVED,     //quản lý không phê duyệt
        APPROVED,       //quản lý phê duyệt
        AGREED,         //member đồng ý định giá cuối
        DECLINED        //member từ chối định giá cuối
}

//1.tao don + dinh gia (row)
//2.tu choi dinh gia (ly do), reject request  --> dinh gia (gia), approved request


//3.Bo phAN nhan hang :get all request : (approved) , call api : da nhan hang  ( update status da nhan hang, them filed thoi gian nhan hang
// SINH RA 1 ROW DINH GIA CHINH THUC
//
// )
//Bo phan dinh gia chinh thuc: get alL : (da nhan hang) // 3 status CUA DGCT: chua xy ly, chua phe duyet, da hoan thanh
//
//
//

// dinh gia (gia) (chua xy ly -> cho phe duyet)  ......-> duyet (
//manager : get all ( chua phe duyet)
// duyet : update status DA PHE DUYET, request DA DINH GIA CHINH THUC
// ko duyet: ghi ly do (Update vao reason cua request) , REJCET don ban dau gia (kem huong dan tra hang o FE)




// tu choi dinh gia (ly do ) (chua xy ly -> da hoan thanh).....update request reject

//Trong truong hop duoc duyet nguoi dung se thay dinh gia chinh thuc.....
// chap nhan : Cho dau gia
//ko chap nhan : Reject (huong dan tra hang)
//manager : get all san pham cho dau gia ....