package com.pl.pizzastore;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(Pizza pizza, int qty) {
        for (OrderItem it : items) {
            if (it.getPizza().getName().equals(pizza.getName())) {
                it.setQuantity(it.getQuantity() + qty);
                return;
            }
        }
        items.add(new OrderItem(pizza, qty));
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }
    public void clear() {
        items.clear();
    }
}
