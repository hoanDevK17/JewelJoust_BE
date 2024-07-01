package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.model.RechargeRequestDTO;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class WalletService {
        @Autowired
        WalletRepository walletRepository;
        @Autowired
        AccountUtils accountUtils;
        @Autowired
        AuthenticationRepository authenticationRepository;
        public Wallet registerWallet(Account account){
        Wallet wallet = new Wallet();
        wallet.setBalance(0.0);
        wallet.setUpdateAt(new Date());
        wallet.setCreateAt(new Date());
        wallet.setAccountWallet(account);
//        Wallet resposeWallet = walletRepository.save(wallet);
//        account.setWallet(resposeWallet);
        

//        wallet.setAccountWallet(account);
        return walletRepository.save(wallet);
    }
        public Wallet changBalance(Long id,double amount){
                Wallet wallet = walletRepository.findWalletById(id);
                wallet.setBalance(wallet.getBalance()+amount);
                return walletRepository.save(wallet);
        }
        public double refreshBalance(){
                Wallet wallet = accountUtils.getAccountCurrent().getWallet();
                return wallet.getBalance();
        }


    public String createUrl(RechargeRequestDTO rechargeRequestDTO) throws NoSuchAlgorithmException, InvalidKeyException, Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

//        User user = accountUtils.getCurrentUser();

        String orderId = UUID.randomUUID().toString().substring(0,6);
//
//        Wallet wallet = walletRepository.findWalletByUser_Id(user.getId());
//
//        Transaction transaction = new Transaction();
//
//        transaction.setAmount(Float.parseFloat(rechargeRequestDTO.getAmount()));
//        transaction.setTransactionType(TransactionEnum.PENDING);
//        transaction.setTo(wallet);
//        transaction.setTransactionDate(formattedCreateDate);
//        transaction.setDescription("Recharge");
//        Transaction transactionReturn = transactionRepository.save(transaction);

        String tmnCode = "BOKXIUP2";
        String secretKey = "XQ4J4XZWOHQE7CXBH1LTGOSAYDPEJ22C";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://jeweljoust.online/";

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
        vnpParams.put("vnp_Amount", rechargeRequestDTO.getAmount() + "00");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

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
