package com.prajwal;

/**
 * @author prajwal.kulkarni on 06/05/21
 */

public class StockOrder {

    private int orderId;

    private int time;

    private String stock;

    private String orderType;

    private double price;

    private int quantity;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "StockOrder{" +
                "orderId=" + orderId +
                ", time=" + time +
                ", stock='" + stock + '\'' +
                ", orderType='" + orderType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
