package top.mhxi.myshop.payment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jp.ne.paypay.ApiClient;
import jp.ne.paypay.Configuration;
import jp.ne.paypay.api.PaymentApi;
import jp.ne.paypay.model.MoneyAmount;
import jp.ne.paypay.model.PaymentDetails;
import jp.ne.paypay.model.QRCode;
import jp.ne.paypay.model.QRCodeDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.mhxi.myshop.common.utils.R;

import java.math.BigDecimal;


@Tag(name = "Pay管理")
@RestController
@RequestMapping("/pay/pay")
public class PayController {
    @Value("${paypay.api_key}")
    private String ApiKey;

    @Value("${paypay.api_secret_key}")
    private String ApiSecretKey;

    @Value("${paypay.assume_merchant}")
    private String AssumeMerchant;


    @GetMapping("/create/{id}/{price}")
    public R createPayment(@PathVariable Long id, @PathVariable BigDecimal price) {

        try {
            ApiClient apiClient = new Configuration().getDefaultApiClient();

            apiClient.setProductionMode(false);
            apiClient.setApiKey(this.ApiKey);
            apiClient.setApiSecretKey(this.ApiSecretKey);
            apiClient.setAssumeMerchant(this.AssumeMerchant);


            // Creating the payload to create a QR Code, additional parameters can be added basis the API Documentation
            QRCode qrCode = new QRCode();
            qrCode.setAmount(new MoneyAmount().amount(price.intValue()).currency(MoneyAmount.CurrencyEnum.JPY));
            qrCode.setMerchantPaymentId(String.valueOf(id));

            qrCode.setCodeType("ORDER_QR");
            qrCode.setOrderDescription("Myshop订单支付");
            qrCode.isAuthorization(false);

            // Calling the method to create a qr code
            PaymentApi apiInstance = new PaymentApi(apiClient);
            QRCodeDetails response = apiInstance.createQRCode(qrCode);


            String qrUrl = response.getData().getUrl();

            return R.ok().data("record", qrUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error();
    }


    // 查询支付状态
    @GetMapping("/search/{id}/")
    public R search(@PathVariable Long id) {

        try {
            ApiClient apiClient = new Configuration().getDefaultApiClient();

            apiClient.setProductionMode(false);
            apiClient.setApiKey(this.ApiKey);
            apiClient.setApiSecretKey(this.ApiSecretKey);
            apiClient.setAssumeMerchant(this.AssumeMerchant);

            PaymentApi apiInstance = new PaymentApi(apiClient);
            PaymentDetails response = apiInstance.getCodesPaymentDetails(String.valueOf(id));

            return R.ok().data("record", response.getData().getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error();
    }
}
