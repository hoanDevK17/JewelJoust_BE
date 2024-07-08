package online.jeweljoust.BE.model;

import online.jeweljoust.BE.entity.Account;

import java.util.List;

public class PagedAccountResponse {
    private List<Account> accounts;
    private long totalAccounts;

    public PagedAccountResponse(List<Account> accounts, long totalAccounts) {
        this.accounts = accounts;
        this.totalAccounts = totalAccounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }
}