package com.kawa.javaDesign23.组合模式;

import java.util.Iterator;
import java.util.Stack;

public class ComposeIterator implements Iterator {

    private Stack<Iterator> stack = new Stack<>();

    public ComposeIterator(Iterator iterator) {
       stack.push(iterator);
    }

    @Override
    public boolean hasNext() {
        if(stack.empty()) {
            return false;
        }
        Iterator iterator = stack.peek();
        if(!iterator.hasNext()){
            stack.pop();
            return hasNext();
        } else {
            return true;
        }
    }

    @Override
    public Object next() {
        if(hasNext()){
            Iterator iterator = stack.peek();
            MenuComponent menuComponent = (MenuComponent) iterator.next();
            stack.push(menuComponent.getIterator());
            return menuComponent;
        }
        return null;
    }

    @Override
    public void remove() {

    }
}
