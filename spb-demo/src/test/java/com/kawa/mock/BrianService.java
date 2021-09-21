package com.kawa.mock;

import java.io.Serializable;
import java.util.Collection;

public class BrianService {

    public String mt(int i, String s, Collection<?> c, Serializable se) {
        throw new RuntimeException();
    }

    public void mtVoid(int i, String s, Collection<?> c, Serializable se) {
        throw new RuntimeException();
    }
}
