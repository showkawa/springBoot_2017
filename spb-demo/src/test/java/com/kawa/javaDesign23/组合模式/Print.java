package com.kawa.javaDesign23.组合模式;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//打印菜单
public class Print {
    private List<MenuComponent> list = new ArrayList<>();

    public Print() {
    }

    public void addComponent(MenuComponent menuComponent){
        list.add(menuComponent);
    }

    public void printMenu(){
        Iterator iterator;
        MenuComponent menuComponent;
        for (int i = 0; i <list.size() ; i++) {
            list.get(i).print();
            iterator = list.get(i).getIterator();

            while (iterator.hasNext()){
                menuComponent  = (MenuComponent) iterator.next();
                menuComponent.print();
            }

        }
    }

}
