package com.kawa.javaDesign23.组合模式;

import java.util.Iterator;


/**
 *  组合模式依据树形结构来组合对象，用来表示部分以及整体层次。这种类型的设计模式属于结构型模式，
 *  它创建了对象组的树形结构。
 */
public abstract class MenuComponent {

    public String getName(){
        return "";
    }

    public String getPrice() {
        return "0 $";
    }

    public abstract void print();

    public Iterator getIterator (){
        return new NullIterator();
    }
}
