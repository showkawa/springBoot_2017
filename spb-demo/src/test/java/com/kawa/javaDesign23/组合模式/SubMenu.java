package com.kawa.javaDesign23.组合模式;

import java.util.ArrayList;
import java.util.Iterator;

//子菜单
public class SubMenu extends MenuComponent {

    private ArrayList<MenuComponent> menuItems;

    public SubMenu() {
        menuItems = new ArrayList<>();
        addItem("纸巾","1$");
        addItem("茶叶","17$");
    }

    public void addItem(String name,String price){
        MenuItem menuItem = new MenuItem(name,price);
        menuItems.add(menuItem);
    }

    @Override
    public Iterator getIterator() {
        return new ComposeIterator(menuItems.iterator());
    }

    @Override
    public void print() {
        System.out.println("---------子类菜单---------------");
    }
}
