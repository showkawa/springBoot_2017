package com.kawa.javaDesign23.组合模式;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DinnerMenu extends MenuComponent {
    private List<MenuComponent> list;

    public DinnerMenu() {
        list = new ArrayList<>();
        MenuItem item1 = new MenuItem("七彩肉汤","23$");
        MenuItem item2 = new MenuItem("虾饺","23$");
        MenuItem item3 = new MenuItem("水晶肠粉","23$");
        addItem(item1);
        addItem(item2);
        addItem(item3);
        addItem(new SubMenu());
    }

    private void addItem(MenuComponent menuComponent) {
        list.add(menuComponent);
    }

    @Override
    public Iterator getIterator() {
        return new ComposeIterator(list.iterator());
    }

    @Override
    public void print() {
        System.out.println("-----------晚餐菜单-------------");
    }
}
