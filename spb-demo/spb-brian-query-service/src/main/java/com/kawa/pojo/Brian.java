package com.kawa.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ConfigurationProperties
 *      告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定
 */
@Component
//@PropertySource(value = {"classpath:application.yml"}) 加载指定的配置文件
@ConfigurationProperties(prefix = "brian")
//@Validated  对配置文件加校验
public class Brian {
    /**
     *@ConfigurationProperties 可以批量注入
     *
     * @Value  注入单个单个的属性
     *
     */
//    @Value("${brian.kawaname}")
    private Date  kawadate;
    private Map<String, Object> obj;
    private List<String> lists;
    private User user;

    public Date getKawadate() {
        return kawadate;
    }

    public void setKawadate(Date kawadate) {
        this.kawadate = kawadate;
    }

    public Map<String, Object> getObj() {
        return obj;
    }

    public void setObj(Map<String, Object> obj) {
        this.obj = obj;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Brian{" +
                "kawadate=" + kawadate +
                ", obj=" + obj +
                ", lists=" + lists +
                ", user=" + user +
                '}';
    }
}
