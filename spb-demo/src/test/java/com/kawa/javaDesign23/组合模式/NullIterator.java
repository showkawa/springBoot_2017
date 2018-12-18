package com.kawa.javaDesign23.组合模式;

import java.util.Iterator;
import java.util.function.Consumer;

//空迭代器
public class NullIterator  implements Iterator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public void remove() { }

    @Override
    public void forEachRemaining(Consumer action) { }
}
