package com.prajwal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author prajwal.kulkarni on 08/05/21
 */

@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    private int buyOrderId;

    private double sellOrderPrice;

    private int quantity;

    private int sellOrderId;

    @Override
    public String toString() {
        return "#" + this.buyOrderId + " " + this.sellOrderPrice + " " + this.quantity + " #" + this.sellOrderId;
    }
}
