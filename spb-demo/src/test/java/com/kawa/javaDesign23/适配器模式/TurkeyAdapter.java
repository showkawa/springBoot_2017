package com.kawa.javaDesign23.适配器模式;

import com.kawa.javaDesign23.适配器模式.turkey.Turkey;

//通过实现鸭子的接口让火鸡具备鸭子的属性
public class TurkeyAdapter implements Duck {
    //turkey为被适配对象通过构造器注入
    private Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        turkey.fly();
    }
}
