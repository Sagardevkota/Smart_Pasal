package com.example.smartpasal.util;

import android.util.Log;

import com.example.smartpasal.model.Orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class CartUtil {


    private static final String TAG = "CART_UTIL";
    private final Map<Integer, Integer> itemPositionMap = new HashMap<>(); //{productId,position}
    private List<Orders> orderList = new ArrayList<>();

    public void putItem(int productId, int position) {
        itemPositionMap.put(productId, position);
    }

    public void removeItemFromMap(int productId) {
        itemPositionMap.remove(productId);
    }


    public Map<Integer, Integer> getItemPositionMap() {
        return itemPositionMap;
    }

    public void addOrders(Orders orders) {
        this.orderList.add(orders);
    }

    public void updateOrders(int productId, int qty) {
        orderList.stream().filter(orders -> orders.getProductId() == productId)
                .forEach(orders -> orders.setQuantity(qty)); //get order having the given order id and set new qty

    }

    public void removeOrderFromList(int productId) {
        orderList.removeIf(orders -> orders.getProductId() == productId);
    }

    public List<Orders> getOrderList() {
        return this.orderList;
    }

    public String getTotalPrice() {

        this.orderList = new ArrayList<>(new LinkedHashSet<>(this.orderList)); //to remove any duplicate
        Log.i(TAG, "getTotalPrice: " + orderList.toString());
        int totalPrice = 0;
        for (Orders o : orderList) {
            int quantity = o.getQuantity();
            int price = o.getPrice();
            int discount = o.getDiscount();
            int discountedAmount = price * discount / 100;
            int newPrice = price - discountedAmount;
            totalPrice += newPrice * quantity;

        }

        return String.valueOf(totalPrice);
    }


}
