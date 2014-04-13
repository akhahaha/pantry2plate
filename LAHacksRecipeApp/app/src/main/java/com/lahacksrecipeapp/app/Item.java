package com.lahacksrecipeapp.app;

/**
 * Created by jeremykao on 4/13/14.
 */
public class Item {
    private String name;
    private String quantity;

    public Item(String name, String quantity){
        this.name = name;
        this.quantity = quantity;
    }
    public String getName(){
        return name;
    }
    public String getQuantity(){
        return quantity;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
}
