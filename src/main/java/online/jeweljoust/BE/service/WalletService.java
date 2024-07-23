package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.model.DepositRequest;
import online.jeweljoust.BE.model.RechargeRequestDTO;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.respository.TransactionRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    AccountUtils accountUtils;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;

//    public Wallet registerWallet(Account account) {

////        Wallet resposeWallet = walletRepository.save(wallet);
////        account.setWallet(resposeWallet);
//
//
////        wallet.setAccountWallet(account);
//        return walletRepository.save(wallet);
//    }

    @Transactional
    public Transaction changBalance(Long id, double amount, TransactionType type,String description) {
        Wallet wallet = walletRepository.findWalletById(id);

        double newBalance = wallet.getBalance() + amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Insufficient funds in the wallet.");
        }
        System.out.println(newBalance);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransaction_type(type);
        transaction.setDate(new Date());
        transaction.setDescription(description);
        if (type.equals(TransactionType.BIDDING)){
            transaction.setStatus(TransactionStatus.COMPLETED);
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdrawBalance(Long id, double amount, TransactionType type,String description) {
        Wallet wallet = walletRepository.findWalletById(id);

        double newBalance = wallet.getBalance() - amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Insufficient funds in the wallet.");
        }
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(-amount);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransaction_type(type);
        transaction.setDate(new Date());
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }
//    @Transactional
//    public Transaction changBalance(Long id, double amount, TransactionType type,String description,Long id_auctionRegistration) {
//        Wallet wallet = walletRepository.findWalletById(id);
//
//        double newBalance = wallet.getBalance() + amount;
//        if (newBalance < 0) {
//            throw new IllegalStateException("Insufficient funds in the wallet.");
//        }
//        System.out.println(newBalance);
//        wallet.setBalance(newBalance);
//        walletRepository.save(wallet);
//        Transaction transaction = new Transaction();
//        transaction.setWallet(wallet);
//        transaction.setAmount(amount);
//        transaction.setTransaction_type(type);
//        transaction.setDate(new Date());
//        transaction.setDescription(description);
////        transaction.setAuctionRegistration(auctionRegistrationRepository.findAuctionRegistrationById(id_auctionRegistration));
//        System.out.println(transactionRepository.save(transaction));
//        return transaction;
//    }
    @Transactional
    public Transaction deposit( DepositRequest depositRequest) {
//        Wallet wallet = accountUtils.getAccountCurrent().getWallet();

        Transaction transaction = this.changBalance(depositRequest.getWalletId(),depositRequest.getAmount(), TransactionType.DEPOSIT,depositRequest.getDescription());
        return transaction;
    }

    public double refreshBalance() {
        Wallet wallet = accountUtils.getAccountCurrent().getWallet();
        return wallet.getBalance();
    }
    public List<Transaction> getWalletActivityHistory() {
        List<TransactionType> types = new ArrayList<TransactionType>();
        types.add(TransactionType.DEPOSIT);
        types.add(TransactionType.WITHDRAW);
        List<Transaction> transactions = transactionRepository.findWalletActivityByWalletId(accountUtils.getAccountCurrent().getWallet().getId(),types);
        return transactions;
    }
    public List<Transaction> getTransactionHistory() {
        List<TransactionType> types = new ArrayList<TransactionType>();
        types.add(TransactionType.DEPOSIT);
        types.add(TransactionType.WITHDRAW);
        List<Transaction> transactions = transactionRepository.findTransactionHistoryByWalletId(accountUtils.getAccountCurrent().getWallet().getId(),types);
        return transactions;
    }


    public String createUrl(RechargeRequestDTO rechargeRequestDTO) throws NoSuchAlgorithmException, InvalidKeyException, Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        Date createDate = new Date();
        String formattedCreateDate = formatter.format(createDate);

        Account account = accountUtils.getAccountCurrent();

        String orderId = UUID.randomUUID().toString().substring(0, 6);

        Wallet wallet = walletRepository.findWallelByAccountWalletId(account.getId());
        Transaction transaction = new Transaction();
        transaction.setAmount(rechargeRequestDTO.getUsd());
        transaction.setTransaction_type(TransactionType.RECHARGE);
        transaction.setWallet(wallet);
        transaction.setDate(createDate);
        transaction.setDescription("Recharge");
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTxnRef(orderId);
        transactionRepository.save(transaction);

        String tmnCode = "BOKXIUP2";
        String secretKey = "XQ4J4XZWOHQE7CXBH1LTGOSAYDPEJ22C";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://jeweljoust.online/payment";

        String currCode = "VND";
            Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", rechargeRequestDTO.getAmount() +"00");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");
//        vnpParams.put("vnp_USD",rechargeRequestDTO.getUsd());
        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }

    public Transaction handleVnpayResponse(String url) throws Exception {

        Map<String, String> params = extractParamsFromUrl(url);

        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        Transaction transaction = transactionRepository.findByTxnRef(vnp_TxnRef);
        if(!transaction.getWallet().getAccountWallet().getId().equals(accountUtils.getAccountCurrent().getId())){
            throw new IllegalStateException("You can not access this transaction");
        }
        // Kiểm tra mã phản hồi từ VNPAY
        if(transaction == null){
            throw new IllegalStateException ("Can not found this transaction");
        }
        if(!transaction.getStatus().equals(TransactionStatus.PENDING)){
            throw new IllegalStateException ("This transaction is handled");
        }
        if ("00".equals(vnp_ResponseCode)) {
            // Tìm giao dịch bằng mã tham chiếu
//            Transaction foundTransaction = transactionRepository.findByTxnRef(vnp_TxnRef);
                // Cập nhật số dư ví của khách hàng
                Wallet wallet = transaction.getWallet();
                double amount = transaction.getAmount();
                wallet.setBalance(wallet.getBalance() + amount);
                walletRepository.save(wallet);
                // Cập nhật trạng thái giao dịch thành COMPLETED
                transaction.setStatus(TransactionStatus.COMPLETED);

        } else {
//            Transaction errTransaction = transactionRepository.findByTxnRef(vnp_TxnRef);
            transaction.setStatus(TransactionStatus.FAILED);

        }
        return  transactionRepository.save(transaction);
    }

    private Map<String, String> extractParamsFromUrl(String url) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                params.put(key, value);
            }
        }
        return params;
    }

    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
