package com.kawa.javaDesign23.组合模式;

public class MenuItem  extends MenuComponent {

    private String name;
    private String price;

    public MenuItem(String name,String price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPrice() {
        return this.price;
    }

    @Override
    public void print() {
        System.out.println(getName()+ " - " + getPrice());
    }
}
