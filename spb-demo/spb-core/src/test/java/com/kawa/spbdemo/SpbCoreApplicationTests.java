package com.kawa.spbdemo;

import com.kawa.config.Contents;
import com.kawa.mq.ManageMQService;
import com.kawa.mq.SendMessageService;
import com.kawa.pojo.Brian;
import com.kawa.pojo.User;
import com.kawa.sercice.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Ignore
public class SpbCoreApplicationTests {

    @Autowired
    Brian brian;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserService userService;

    @Autowired
    ManageMQService manageMQService;

    @Autowired
    SendMessageService sendMessageService;

    @Test
    public void contextLoads() {
//		System.out.println(brian);
//		for(String str: applicationContext.getBeanDefinitionNames()){
//			System.out.println("Bean name: " + str);
//		}
    }

    @Test
    public void removeQueue(){
        manageMQService.removeQueue("brian.sh");
        manageMQService.removeQueue("brian.sz");
        manageMQService.removeQueue("huang.sz");
    }

    @Test
    public void removeExchange(){
        manageMQService.removeExchange("brian.direct");
        manageMQService.removeExchange("brian.fanout");
        manageMQService.removeExchange("brian.topic");
    }

    @Test
    public void sendMessage() {
        manageMQService.createQueue("brian.test");
        manageMQService.createExchange("brian",Contents.DIRECT_EXCHANGE);
        manageMQService.createBinding("brian.test","brian","mymq");
        Brian brian = new Brian();
        User user = new User();
        user.setId((long) 12345678);
        user.setUsername("cassiel");
        user.setPassword("#fyds");
        List<String> list = new ArrayList<>();
        list.add("我");
        list.add("爱");
        list.add("你");
        list.add("中");
        list.add("国");
        Map<String,Object> map = new HashMap<>();
        map.put("123","包邮");
        brian.setKawadate(new Date());
        brian.setLists(list);
        brian.setObj(map);
        brian.setUser(user);
        sendMessageService.sendMessage("brian","mymq",brian);
    }


}
