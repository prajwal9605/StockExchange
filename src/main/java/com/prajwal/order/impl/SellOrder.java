package com.prajwal.order.impl;

import com.prajwal.model.StockOrder;
import com.prajwal.model.Transaction;
import com.prajwal.order.Order;

import java.util.List;
import java.util.PriorityQueue;

/**
 * @author prajwal.kulkarni on 08/05/21
 */
public class SellOrder implements Order {

    @Override
    public void processOrder(StockOrder newOrder, PriorityQueue<StockOrder> existingOrders, List<Transaction> transactionList) {
        // loop through the existing orders since they are already ordered according to the price

        int quantity = newOrder.getQuantity();
        StockOrder frontOrder = existingOrders.peek();
        while (frontOrder != null && frontOrder.getPrice() >= newOrder.getPrice() && quantity > 0) {
            // execute the order
            if (quantity >= frontOrder.getQuantity()) {
                transactionList.add(new Transaction(frontOrder.getOrderId(), newOrder.getPrice(), frontOrder.getQuantity(), newOrder.getOrderId()));
                quantity = quantity - frontOrder.getQuantity();

                newOrder.setQuantity(quantity);
                // remove the item from priority queue since it is consumed
                existingOrders.poll();
                frontOrder = existingOrders.peek();
            } else {
                transactionList.add(new Transaction(frontOrder.getOrderId(), newOrder.getPrice(), newOrder.getQuantity(), newOrder.getOrderId()));
                newOrder.setQuantity(0);
                frontOrder.setQuantity(frontOrder.getQuantity() - quantity);

                quantity = 0;
            }
        }
    }
}
