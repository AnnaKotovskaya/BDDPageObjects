package ru.netology.web.page;

import ru.netology.web.data.DataHelper;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement from = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMessage = $("[data-test-id=error-message]");

    public TransferPage() {
        amount.shouldBe(visible);
    }

    public DashboardPage validTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        amount.setValue(amountTransfer);
        from.setValue(cardInfo.getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public void findErrorMesage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(10)).shouldBe(visible);
    }


}
