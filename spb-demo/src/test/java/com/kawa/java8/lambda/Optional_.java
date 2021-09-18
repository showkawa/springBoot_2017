package com.kawa.java8.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 1. Optional
 * <p>
 * 2.方法引用是lambda的特殊情况一种语法唐,可以将方法引用看作一种函数指针,指向实际执行的方法
 * <p>
 * 方法引用共分4类：
 * 1.类名：：静态方法名
 * 2.引用名（对象名）：：实例方法名
 * 3.类名：：实例方法名
 * 4.构造方法引用: 类名：：new
 */
public class Optional_ {

    @Test
    public void Test_Optional() {
        Bank b1 = new Bank("ICBC", "china", 50);
        Optional<Bank> bankOptional = Optional.of(b1);
        bankOptional.ifPresent(bank -> System.out.println(bank.getName()));
        System.out.println("--------------------");
        Optional<Object> brian = Optional.empty().or(() -> Optional.of("Brian"));
        brian.ifPresent(b -> System.out.println("brian = " + b));
        System.out.println("--------------------");

        List<Account> accountList =
                // Arrays.asList(new Account("zs", "ICBC", "2001-10-10", "V1"), new Account("ls", "ICBC", "1993-10-10", "V5"));
                Collections.emptyList();
        List<Account> accounts = Optional.of(accountList).orElse(Collections.emptyList());
        accounts.forEach(ac -> System.out.println(ac.getBankName()));
    }


    @Test
    public void Test_Method_Reference() {
        Account a1 = new Account("brian", "ICBC", "2001-10-10", "V1");
        Account a2 = new Account("cassiel", "ICBC", "2003-01-09", "V2");
        Account a3 = new Account("jacky", "ICBC", "2005-10-10", "V1");
        Account a4 = new Account("jumper", "ICBC", "2007-10-10", "V2");
        Account a5 = new Account("alan", "ICBC", "1998-12-23", "V3");

        System.out.println("-----------类名：：静态方法名------------------");
        List<Account> list = Arrays.asList(a1, a2, a3, a4, a5);
//        list.sort((at1,at2) -> Account.compareYear(at1,at2));
        list.sort(Account::compareYear);
        list.forEach(account -> System.out.println(account.getYear()));
        System.out.println("-----------引用名（对象名）：：实例方法名------------------");
        AccountComparator ac = new AccountComparator();
        List<Account> list2 = Arrays.asList(a1, a2, a3, a4, a5);
        list2.sort(ac::compareUserName);
        list.forEach(account -> System.out.println(account.getUserName()));

        System.out.println("--------类名：：实例方法名---------------------");
        List<Account> list3 = Arrays.asList(a1, a2, a3, a4, a5);
        list3.sort(Account::compareUserName);
        list.forEach(account -> System.out.println(account.getUserName()));

        System.out.println("--------类名：：实例方法名-------------------");
        String str = getString(String::new);
        System.out.println(str);
        String str2 = getString2("brian", String::new);
        System.out.println(str2);
    }

    public String getString(Supplier<String> supplier){
       return supplier.get().concat(" <> ");
    }

    public String getString2(String userName,Function<String,String> function){
        return function.apply(userName);
    }
}
