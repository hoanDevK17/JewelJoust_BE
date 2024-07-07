package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.respository.TransactionRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
@Autowired
WalletService walletService;
@Autowired
    AccountUtils accountUtils;

    @Transactional
    public Transaction refundRegistration(AuctionRegistration auctionRegistration){
        Transaction transaction = new Transaction();
        Double amountDeposit = auctionRegistration.getAuctionSession().getDepositAmount();
        Wallet wallet = auctionRegistration.getAccountRegistration().getWallet();
        walletService.changBalance(wallet.getId(), amountDeposit,TransactionType.REFUND,"Refund deposit and bidding for the session" + auctionRegistration.getAuctionSession().getNameSession());
        return transactionRepository.save(transaction);
    }
    public List<Transaction> getAll(){

        return transactionRepository.findByWalletId(accountUtils.getAccountCurrent().getWallet().getId());
//        return transactionRepository.findAll();
    }

}
