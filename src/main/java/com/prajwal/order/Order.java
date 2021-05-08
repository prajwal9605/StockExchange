package com.prajwal.order;

import com.prajwal.model.StockOrder;
import com.prajwal.model.Transaction;

import java.util.List;
import java.util.PriorityQueue;

/**
 * @author prajwal.kulkarni on 08/05/21
 */
public interface Order {

    /**
     * @param newOrder        - the newOrder can be a SELL order or a buy ORDER
     * @param existingOrders  - priority queue consisting of the existing orders of the opposite type
     * @param transactionList - list of transactions passed as a reference
     *                        processes a new order by executing transactions from the existing orders
     *                        while maximising the profit (for buyer) and maintaining FIFO if the profit
     *                        for more than 1 transactions are the same
     */
    void processOrder(StockOrder newOrder, PriorityQueue<StockOrder> existingOrders, List<Transaction> transactionList);
}
