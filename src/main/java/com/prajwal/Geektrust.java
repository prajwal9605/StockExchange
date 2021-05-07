package com.prajwal;

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

        File file = new File(args[0]);
        String fileContent = FileUtil.readFileAsString(file);

        List<StockOrder> orderList = convertStringToList(fileContent);
        System.out.println("Read the orders from the file: " + orderList);

        Map<String, List<StockOrder>> stockByCompany = new HashMap<>();
        StringBuilder orderResult = new StringBuilder();
        orderList.forEach(order -> {
            String company = order.getStock();

            // if there is no existing order for the company add it to the map
            if (!stockByCompany.containsKey(company)) {
                List<StockOrder> orders = new ArrayList<>();
                orders.add(order);
                stockByCompany.put(company, orders);
            } else {
                List<StockOrder> existingOrders = stockByCompany.get(company);
                if ("buy".equalsIgnoreCase(order.getOrderType())) {
                    List<StockOrder> eligibleOrders = existingOrders.stream().filter(existingOrder ->
                            "sell".equalsIgnoreCase(existingOrder.getOrderType()) && existingOrder.getPrice() < order.getPrice())
                            .collect(Collectors.toList());

                    if (!eligibleOrders.isEmpty()) {
                        // sort by the lowest price and use time in case the price is the same
                        eligibleOrders.sort((o1, o2) -> {
                            if (o1.getPrice() < o2.getPrice()) {
                                return -1;
                            } else if (o2.getPrice() < o1.getPrice()) {
                                return 1;
                            } else {
                                if (o1.getTime() < o2.getTime()) {
                                    return -1;
                                } else if (o2.getTime() < o1.getTime()) {
                                    return 1;
                                }
                            }
                            return 0;
                        });

                        int quantity = order.getQuantity();
                        Iterator<StockOrder> iterator = eligibleOrders.listIterator();
                        List<StockOrder> itemsToBeRemoved = new ArrayList();
                        while (iterator.hasNext() && quantity > 0) {
                            // execute the order
                            StockOrder nextOrder = iterator.next();
                            if (quantity > nextOrder.getQuantity()) {
                                orderResult.append("#" + order.getOrderId() + " " + nextOrder.getPrice() + " " + nextOrder.getQuantity() + " #" + nextOrder.getOrderId() + "\n");
                                quantity = quantity - nextOrder.getQuantity();

                                order.setQuantity(quantity);
                                itemsToBeRemoved.add(nextOrder);
                            } else {
                                orderResult.append("#" + order.getOrderId() + " " + nextOrder.getPrice() + " " + quantity + " #" + nextOrder.getOrderId() + "\n");
                                order.setQuantity(0);
                                nextOrder.setQuantity(nextOrder.getQuantity() - quantity);

                                quantity = 0;
                            }
                        }

                        // remove the ones whose all units are sold or bought
                        existingOrders.removeAll(itemsToBeRemoved);

                        // add the new order only if some quantity is left
                        if (quantity > 0) {
                            existingOrders.add(order);
                        }

                    } else {
                        existingOrders.add(order);
                    }

                } else {
                    List<StockOrder> eligibleOrders = existingOrders.stream().filter(existingOrder ->
                            "buy".equalsIgnoreCase(existingOrder.getOrderType()) && existingOrder.getPrice() > order.getPrice())
                            .collect(Collectors.toList());

                    if (!eligibleOrders.isEmpty()) {
                        // sort by the highest price and use time in case the price is the same
                        eligibleOrders.sort((o1, o2) -> {
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
                        });

                        int quantity = order.getQuantity();
                        Iterator<StockOrder> iterator = eligibleOrders.listIterator();
                        List<StockOrder> itemsToBeRemoved = new ArrayList();
                        while (iterator.hasNext() && quantity > 0) {
                            // execute the order
                            StockOrder nextOrder = iterator.next();
                            if (quantity > nextOrder.getQuantity()) {
                                orderResult.append("#" + nextOrder.getOrderId() + " " + order.getPrice() + " " + nextOrder.getQuantity() + " #" + order.getOrderId() + "\n");
                                quantity = quantity - nextOrder.getQuantity();

                                order.setQuantity(quantity);
                                itemsToBeRemoved.add(nextOrder);
                            } else {
                                orderResult.append("#" + nextOrder.getOrderId() + " " + nextOrder.getPrice() + " " + quantity + " #" + order.getOrderId() + "\n");
                                order.setQuantity(0);
                                nextOrder.setQuantity(nextOrder.getQuantity() - quantity);

                                quantity = 0;
                            }
                        }

                        // remove the ones whose all units are sold or bought
                        existingOrders.removeAll(itemsToBeRemoved);

                        // add the new order only if some quantity is left
                        if (quantity > 0) {
                            existingOrders.add(order);
                        }

                    } else {
                        existingOrders.add(order);
                    }


                }
            }
        });
        System.out.println(orderResult);
    }

    static List<StockOrder> convertStringToList(String fileContent) {
        String[] lines = fileContent.split("\n", -1);
        List<StockOrder> orderList = new ArrayList();

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
                stockOrder.setTime(hours + 60 * minutes);

                text = matcher.group(3);
                stockOrder.setStock(text);

                text = matcher.group(4);
                stockOrder.setOrderType(text);

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
