package com.kawa.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class BrianMail {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    public void sendEmail() {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            //multipart:true表示开启附件添加
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //邮件设置
            mimeMessageHelper.setSubject("测试邮件");
            mimeMessageHelper.setText("<p style=\"background-color:rgb(255,255,0)\">\n" +
                    "通过 rbg 值设置背景颜色\n" +
                    "</p>\n" +
                    "<p style=\"background-color:rgba(255,255,0,0.25)\">\n" +
                    "通过 rbg 值设置背景颜色\n" +
                    "</p>\n" +
                    "<p style=\"background-color:rgba(255,255,0,0.5)\">\n" +
                    "通过 rbg 值设置背景颜色\n" +
                    "</p>\n" +
                    "<p style=\"background-color:rgba(255,255,0,0.75)\">\n" +
                    "通过 rbg 值设置背景颜色\n" +
                    "</p>",true);
            mimeMessageHelper.setFrom("1092249319@qq.com");
            mimeMessageHelper.setTo("2296398814@qq.com");
            mimeMessageHelper.setCc("1092249319@qq.com");
            //附件
            mimeMessageHelper.addAttachment("10086.jpg",new File("C:\\Users\\LiangHuang\\Desktop\\backup\\10086.jpg"));
            javaMailSender.send(mimeMessage);
            System.out.println("邮件发送成功...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
