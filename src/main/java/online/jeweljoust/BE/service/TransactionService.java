package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.model.WithdrawRequest;
import online.jeweljoust.BE.respository.TransactionRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Transaction> getAll(Pageable pageable){
        return transactionRepository.findByWalletId(accountUtils.getAccountCurrent().getWallet().getId(),pageable);
//        return transactionRepository.findAll();
    }
    public List<Transaction> getAllWithDrawRequest(){
        return transactionRepository.findAllWithDrawRequest();
//        return transactionRepository.findAll();
    }
    public Transaction confirmWithDraw(long id){
        Transaction transaction = transactionRepository.findTransactionById(id);
        if(transaction.getStatus().equals(TransactionStatus.PENDING)){
            System.out.println("Oke");
            transaction.setStatus(TransactionStatus.COMPLETED);
        }
        else{
            throw new IllegalStateException("Not support confirm this transactions");
        }
        return transactionRepository.save(transaction);
//        return transactionRepository.findAll();
    }


    @Transactional
    public Transaction withdraw(WithdrawRequest withdrawRequest){

//        Double amountDeposit = auctionRegistration.getAuctionSession().getDepositAmount();
        Wallet wallet = accountUtils.getAccountCurrent().getWallet();
        if (wallet.getBalance() >= withdrawRequest.getUsd() && wallet.getBalance() > 0){
            Transaction transaction =  walletService.withdrawBalance(wallet.getId(), withdrawRequest.getUsd(),TransactionType.WITHDRAW,
                    withdrawRequest.getRecipientName() + " has been successfully withdrawn "
                    +withdrawRequest.getBankName() +" " + withdrawRequest.getAccountNumber() +" "  + withdrawRequest.getAmountWithDraw() + "VND") ;
            return transactionRepository.save(transaction);
        } else {
            throw new IllegalStateException("The balance is not enough to complete the transaction");
        }

    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        if(!accountUtils.getAccountCurrent().getRole().equals(AccountRole.MEMBER))
        return transactionRepository.findAll(pageable);
        return transactionRepository.findByWalletId(accountUtils.getAccountCurrent().getWallet().getId(),pageable);
    }
}
