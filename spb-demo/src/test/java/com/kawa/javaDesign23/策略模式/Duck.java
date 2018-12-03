package com.kawa.javaDesign23.策略模式;


import com.kawa.javaDesign23.策略模式.behavior.FlyBehavior;
import com.kawa.javaDesign23.策略模式.behavior.QuackBehavior;

/**
 * 策略模式分析新需求（应对项目的扩展性，降低复杂度）
 *
 *  1.分析项目的变化与不变的部分，提取变化的部分，抽象成接口 + 实现
 *  2.Duck 类中鸭子哪些功能会会根据新需求变化的？ 叫声，飞行。。。
 */
//鸭子类
public abstract class Duck {
    //飞行的行为
    FlyBehavior flyBehavior;
    //叫声的行为
    QuackBehavior quackBehavior;

    public Duck() {
    }

    public void Fly() {
        flyBehavior.fly();
    }

    public void Quack() {
        quackBehavior.quack();
    }

    public abstract void display();

    public FlyBehavior getFlyBehavior() {
        return flyBehavior;
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public QuackBehavior getQuackBehavior() {
        return quackBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}
