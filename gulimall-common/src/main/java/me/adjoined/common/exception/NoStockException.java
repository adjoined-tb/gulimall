package me.adjoined.common.exception;

public class NoStockException extends  RuntimeException{
    private Long skuId;
    public NoStockException(Long skuId) {
        super("out of stock, skuId: " + skuId);
        this.skuId = skuId;
    }

    public Long getSkuId() {
        return skuId;
    }
}
