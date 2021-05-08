package com.prajwal.comparator;

import com.prajwal.model.StockOrder;

import java.util.Comparator;

/**
 * @author prajwal.kulkarni on 07/05/21
 */
public class BuyOrderComparator implements Comparator<StockOrder> {

    @Override
    public int compare(StockOrder o1, StockOrder o2) {
        if (o1.getPrice() > o2.getPrice()) {
            return -1;
        } else if (o2.getPrice() > o1.getPrice()) {
            return 1;
        } else {
            if (o1.getTime() < o2.getTime()) {
                return -1;
            } else if (o2.getTime() < o1.getTime()) {
                return 1;
            }
        }
        return 0;
    }
}
