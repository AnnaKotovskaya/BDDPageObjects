package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;


import static ru.netology.web.data.DataHelper.*;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.generateInvalidAmount;


class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }


    @Test
    void shouldTransferMoneyFromFirstCard() {
        var cardInfoFirst = DataHelper.getFirstCardInfo();
        var cardInfoSecond = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(cardInfoFirst);
        var secondCardBalance = dashboardPage.getCardBalance(cardInfoSecond);
        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCard(cardInfoFirst);
        transferPage.validTransfer(String.valueOf(amount), cardInfoSecond);

        assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(cardInfoFirst));
        assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(cardInfoSecond));
    }

    @Test
    void shouldTransferMoneyFromSecondCard() {
        var cardInfoFirst = DataHelper.getFirstCardInfo();
        var cardInfoSecond = DataHelper.getSecondCardInfo();
        var amount = generateInvalidAmount(100);
        var firstCardBalance = dashboardPage.getCardBalance(cardInfoFirst);
        var secondCardBalance = dashboardPage.getCardBalance(cardInfoSecond);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCard(cardInfoSecond);
        transferPage.validTransfer(String.valueOf(amount), cardInfoFirst);


        assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(cardInfoFirst));
        assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(cardInfoSecond));
    }

    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {
        var cardInfoFirst = DataHelper.getFirstCardInfo();
        var cardInfoSecond = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(cardInfoFirst);
        var secondCardBalance = dashboardPage.getCardBalance(cardInfoSecond);
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCard(cardInfoSecond);
        transferPage.validTransfer(String.valueOf(amount), cardInfoFirst);
        transferPage.findErrorMesage("Вы ввели сумму, превышающую остаток средств на Вашей карте. Введите другую сумму");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirst);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecond);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);

    }
}

