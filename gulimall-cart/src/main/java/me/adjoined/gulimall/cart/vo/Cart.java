package me.adjoined.gulimall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
    List<CartItem> items;
    private Integer totalCount;
    private Integer typeCount;
    private BigDecimal totalAmount;
    private BigDecimal discount;

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getTotalCount() {
        totalCount = 0;
        if (items != null) {
            for (CartItem item : items) {
                totalCount += item.getCount();
            }
        }
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTypeCount() {
        typeCount = 0;
        if (items != null) {
            totalCount = items.size();
        }

        return typeCount;
    }

    public void setTypeCount(Integer typeCount) {
        this.typeCount = typeCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
