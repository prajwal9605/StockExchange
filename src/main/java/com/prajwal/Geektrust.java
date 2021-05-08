package com.prajwal;

import com.prajwal.comparator.BuyOrderComparator;
import com.prajwal.comparator.SellOrderComparator;
import com.prajwal.model.StockOrder;
import com.prajwal.model.Transaction;
import com.prajwal.order.Order;
import com.prajwal.order.impl.BuyOrder;
import com.prajwal.order.impl.SellOrder;
import com.prajwal.util.FileUtil;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author prajwal.kulkarni on 06/05/21
 */
public class Geektrust {

    public static void main(String... args) {

        // read the file path from the arguments
        File file = new File(args[0]);

        // if file does not exists, then return from the method
        if (!file.exists()) {
            return;
        }
        String fileContent = FileUtil.readFileAsString(file);

        // return if the file content could not be read
        if (fileContent == null) {
            return;
        }

        List<StockOrder> orderList = convertStringToList(fileContent);

        Map<String, Map<StockOrder.OrderType, PriorityQueue<StockOrder>>> stockByCompany = new HashMap<>();
        List<Transaction> transactionList = new ArrayList<>();
        orderList.forEach(order -> {
            String company = order.getStock();
            StockOrder.OrderType orderType = order.getOrderType();
            StockOrder.OrderType orderTypeToFetch = StockOrder.OrderType.SELL.equals(orderType) ?
                    StockOrder.OrderType.BUY : StockOrder.OrderType.SELL;

            // if there is no existing order for the company add it to the map
            if (!stockByCompany.containsKey(company) ||
                    !stockByCompany.get(company).containsKey(orderTypeToFetch)) {
                // update the map
                updateMap(stockByCompany, order, company, orderType);
            } else {
                Order processingOrder = StockOrder.OrderType.SELL.equals(orderType) ? new SellOrder() : new BuyOrder();

                PriorityQueue<StockOrder> existingOrdersToProcess = stockByCompany.get(company).get(orderTypeToFetch);
                processingOrder.processOrder(order, existingOrdersToProcess, transactionList);

                if (order.getQuantity() > 0) {
                    PriorityQueue<StockOrder> existingOrders = stockByCompany.get(company).get(orderType);
                    if (existingOrders == null) {
                        updateMap(stockByCompany, order, company, orderType);
                    } else {
                        existingOrders.add(order);
                    }
                }
            }
        });
        List<String> transactionStrList = transactionList.stream().map(Transaction::toString).collect(Collectors.toList());
        System.out.println(String.join("\n", transactionStrList));
    }

    private static void updateMap(Map<String, Map<StockOrder.OrderType, PriorityQueue<StockOrder>>> stockByCompany, StockOrder stockOrder,
                           String company, StockOrder.OrderType orderType) {
        Map<StockOrder.OrderType, PriorityQueue<StockOrder>> ordersMap = stockByCompany.get(company);
        if (ordersMap == null) {
            ordersMap = new HashMap<>();
        }
        PriorityQueue<StockOrder> priorityQueue = ordersMap.get(orderType);
        if (priorityQueue == null) {
            // create a priority queue with an appropriate comparator
            Comparator<StockOrder> comparator = StockOrder.OrderType.SELL.equals(orderType) ? new SellOrderComparator() :
                    new BuyOrderComparator();
            priorityQueue = new PriorityQueue<>(comparator);
        }
        priorityQueue.add(stockOrder);
        ordersMap.put(orderType, priorityQueue);

        stockByCompany.put(company, ordersMap);
    }

    static List<StockOrder> convertStringToList(String fileContent) {
        String[] lines = fileContent.split("\n", -1);
        List<StockOrder> orderList = new ArrayList<>();

        for (String line : lines) {
            String regex = "#([\\d]+)[\\s]+([\\d]{2}:[\\d]{2})[\\s]+([A-Z]+)[\\s]+([a-z]+)[\\s]+([\\d]+.[\\d]{2})[\\s]+([\\d]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                StockOrder stockOrder = new StockOrder();
                String text;

                text = matcher.group(1);
                stockOrder.setOrderId(Integer.parseInt(text));

                text = matcher.group(2);
                int hours = Integer.parseInt(text.split(":")[0]);
                int minutes = Integer.parseInt(text.split(":")[1]);
                stockOrder.setTime(hours * 60 + minutes);

                text = matcher.group(3);
                stockOrder.setStock(text);

                text = matcher.group(4);
                stockOrder.setOrderType(StockOrder.OrderType.valueOf(text.toUpperCase()));

                text = matcher.group(5);
                stockOrder.setPrice(Double.parseDouble(text));

                text = matcher.group(6);
                stockOrder.setQuantity(Integer.parseInt(text));

                orderList.add(stockOrder);
            }
        }
        return orderList;
    }
}
