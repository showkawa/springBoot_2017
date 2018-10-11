package com.kawa.spbdemo;

import com.kawa.pojo.Brian;
import com.kawa.pojo.User;
import com.kawa.sercice.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpbDemoApplicationTests {

    @Autowired
    Brian brian;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserService userService;


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void contextLoads() {
//		System.out.println(brian);
//		for(String str: applicationContext.getBeanDefinitionNames()){
//			System.out.println("Bean name: " + str);
//		}
    }

   // @Test
    public void operateStringDate() {
       // User user = userService.queryUserById((long) 1);
        stringRedisTemplate.opsForValue().append("kawa","147筒");
        /*stringRedisTemplate.opsForList().leftPush("kawaList","五条");
        stringRedisTemplate.opsForList().leftPush("kawaList","三万");*/
    }

    @Test
    public void operateDate() {
        User user = userService.queryUserById((long) 1);
        redisTemplate.opsForValue().set("user1",user);
    }

    @Test
    public void getDate() {
        String kawa = stringRedisTemplate.opsForValue().get("kawa");
        System.out.println("---------------------Get string data..." + kawa);
        User user = (User) redisTemplate.opsForValue().get("user1");
        System.out.println("---------------------Get user data..." + user);
    }


}
