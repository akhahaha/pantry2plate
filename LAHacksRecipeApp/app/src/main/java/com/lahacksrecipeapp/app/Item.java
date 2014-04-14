package com.lahacksrecipeapp.app;

/**
 * Created by jeremykao on 4/13/14.
 */
public class Item {
    private String name;
    private int quantity;
    private String units;

    public Item(String name, int quantity, String units) {
        this.name = name;
        this.quantity = quantity;
        this.units = units;
    }

    public String getName(){ return name; }
    public int getQuantity(){ return quantity; }
    public String getUnits() { return units; }

    public void setName(String name){
        this.name = name;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setUnits(String units) { this.units = units; }
}
