package com.kawa.javaDesign23.组合模式;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//早餐菜单
public class MorningMenu extends MenuComponent{
    private List<MenuComponent> list;

    public MorningMenu() {
        list = new ArrayList<>();
        addItem("菜包","5$");
        addItem("油条","3$");
        addItem("炒粉","7$");
        addItem("鸡蛋","2$");
    }

    public void addItem(String name ,String price){
        MenuItem menuItem = new MenuItem(name,price);
        this.list.add(menuItem);
    }

    @Override
    public Iterator getIterator() {
        return new ComposeIterator(list.iterator());
    }

    @Override
    public void print() {
        System.out.println("-----早餐菜单列表-----");

    }
}
