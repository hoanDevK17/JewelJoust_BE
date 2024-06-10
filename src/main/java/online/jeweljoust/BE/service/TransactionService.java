package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
@Autowired
WalletService walletService;
    public Transaction deposit(AuctionRegistration auctionRegistration){
        Transaction transaction = new Transaction();
        Double amountDeposit = auctionRegistration.getAuctionSession().getDepositAmount();
        Wallet wallet = auctionRegistration.getAccountRegistration().getWallet();
        transaction.setTransaction_type(TransactionType.DEPOSIT);
        transaction.setDate(new Date());
        transaction.setAmount(amountDeposit);
        transaction.setAuctionRegistration(auctionRegistration);
        transaction.setStatus(TransactionStatus.FAILED);
        if(wallet.getBalance() >= amountDeposit){
           Wallet newWallet  = walletService.changBalance(wallet.getId(), -amountDeposit);
           transaction.setStatus(TransactionStatus.SUCCESSFUL);
        }
        return transactionRepository.save(transaction);
    }
    public Transaction refundRegistration(AuctionRegistration auctionRegistration){
        Transaction transaction = new Transaction();
        Double amountDeposit = auctionRegistration.getAuctionSession().getDepositAmount();
        Wallet wallet = auctionRegistration.getAccountRegistration().getWallet();
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setTransaction_type(TransactionType.REFUND);
        transaction.setDate(new Date());
        transaction.setAmount(amountDeposit);
        transaction.setAuctionRegistration(auctionRegistration);
        walletService.changBalance(wallet.getId(), amountDeposit);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);

        return transactionRepository.save(transaction);
    }
}
