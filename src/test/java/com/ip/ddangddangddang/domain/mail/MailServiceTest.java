package com.ip.ddangddangddang.domain.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ip.ddangddangddang.common.AuctionFixture;
import com.ip.ddangddangddang.common.UserFixture;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.mail.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest implements UserFixture, AuctionFixture {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserService userService;

    @Test
    void 퍈매자_낙찰자_메일() {
        // given
        when(userService.getUserByIdOrElseThrow(TEST_AUCTION_BUYER_ID)).thenReturn(
            TEST_BUYER_USER1);

        // when
        mailService.sendMail(
            TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_AUCTION_BUYER_ID,
            TEST_AUCTION_TITLE,
            TEST_AUCTION_PRICE
        );

        // then
        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    @Test
    void 낙찰가가_없는_메일() {
        // given
        // when
        mailService.sendMail(
            TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            null,
            TEST_AUCTION_TITLE,
            TEST_AUCTION_PRICE
        );

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void 낙찰자가_없는_메일_알림() {
        // given
        // when
        mailService.sendNoBuyerNotification(
            TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_AUCTION_TITLE
        );

        // then
        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(
            SimpleMailMessage.class);
        verify(mailSender).send(argument.capture());

        SimpleMailMessage sentMessage = argument.getValue();
        assertEquals(TEST_USER_EMAIL, sentMessage.getTo()[0]);
        assertEquals("땅땅땅! [" + TEST_AUCTION_TITLE + "] 게시글의 경매가 종료되었습니다!",
            sentMessage.getSubject());
        assertEquals(TEST_USER_NICKNAME + "님!\n" +
            "땅땅땅! [" + TEST_AUCTION_TITLE + "] 게시글의 경매가 종료되었습니다!\n" +
            "아쉽게도 경매의 낙찰자가 없습니다.\n" +
            "게시글을 다시 올려주세요!", sentMessage.getText());
    }

    @Test
    void 판매자_낙찰자_메일_알림() {
        // given
        when(userService.getUserByIdOrElseThrow(TEST_AUCTION_BUYER_ID))
            .thenReturn(TEST_BUYER_USER1);

        // when
        mailService.sendSellerBuyerNotification(
            TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_AUCTION_BUYER_ID,
            TEST_AUCTION_TITLE,
            TEST_AUCTION_PRICE
        );

        // then
        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }

}
