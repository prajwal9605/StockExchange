package com.prajwal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author prajwal.kulkarni on 06/05/21
 */

@Getter
@Setter
@ToString
public class StockOrder {

    private int orderId;

    private int time;

    private String stock;

    private OrderType orderType;

    private double price;

    private int quantity;

    public enum OrderType {SELL, BUY}

}
