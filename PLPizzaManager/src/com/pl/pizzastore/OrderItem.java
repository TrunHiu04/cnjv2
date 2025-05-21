package com.pl.pizzastore;

public class OrderItem {
    private Pizza pizza;
    private int quantity;

    public OrderItem(Pizza pizza, int quantity) {
        this.pizza = pizza;
        this.quantity = quantity;
    }
    public Pizza getPizza() { return pizza; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() {
        return pizza.getPrice() * quantity;
    }
}
