package online.jeweljoust.BE.model;

import online.jeweljoust.BE.entity.Account;

import java.util.List;

public class PagedResponse<T> {
    private List<T> items;
    private long totalItems;

    public PagedResponse(List<T> items, long totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}