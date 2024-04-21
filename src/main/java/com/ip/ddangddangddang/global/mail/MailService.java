package com.ip.ddangddangddang.global.mail;

import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j(topic = "MailService")
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${sender.mail}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public void sendMail(String sellerEmail, String sellerNickname, Long buyerId,
        String t, Long price) {

        if (buyerId == null) {
            sendNoBuyerNotification(sellerEmail, sellerNickname, t);
        } else {
            sendSellerBuyerNotification(sellerEmail, sellerNickname, buyerId, t, price);
        }
    }

    private void sendNoBuyerNotification(String sellerEmail, String sellerNickname, String t) {
        String title = "땅땅땅! [" + t + "] 게시글의 경매가 종료되었습니다!";
        String content =
            sellerNickname + "님!"
                + "\n땅땅땅! [" + t + "] 게시글의 경매가 종료되었습니다!"
                + "\n아쉽게도 경매의 낙찰자가 없습니다."
                + "\n게시글을 다시 올려주세요!";

        sendEmail(sellerEmail, title, content);
    }

    private void sendSellerBuyerNotification(String sellerEmail, String sellerNickname,
        Long buyerId, String t, Long price) {
        User buyer = userService.getUserByIdOrElseThrow(buyerId);

        String sellerTitle = "땅땅땅! [" + t + "] 게시글의 경매가 종료되었습니다!";
        String sellerContent =
            sellerNickname + "님!"
                + "\n땅땅땅! [" + t + "] 게시글의 경매가 종료되었습니다!"
                + "\n낙찰가 : " + price + "원"
                + "\n낙찰자 : " + buyer.getNickname()
                + "\n지금 바로 접속해서 거래를 시작하세요!";

        String buyerTitle = "땅땅땅! [" + t + "] 게시글에 낙찰되었습니다!";
        String buyerContent =
            buyer.getNickname() + "님 축하합니다!"
                + "\n땅땅땅! [" + t + "] 게시글에 낙찰되었습니다!"
                + "\n낙찰가 : " + price + "원"
                + "\n지금 바로 접속해서 거래를 시작하세요!";

        sendEmail(sellerEmail, sellerTitle, sellerContent);
        sendEmail(buyer.getEmail(), buyerTitle, buyerContent);
    }

    private void sendEmail(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setFrom(senderEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
