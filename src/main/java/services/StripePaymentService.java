package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;

public class StripePaymentService {
    public static void createPayment(double amount) throws StripeException {
        double exchangeRate = 0.032f;
        double usdAmount = amount * exchangeRate;

        if (usdAmount > 999999.99) {
            System.out.println("Error: Amount exceeds the maximum allowed by Stripe.");
        } else {
            Stripe.apiKey = "sk_test_51OqJbyP8fZ7mtZrf1ucCZuWgiG9TwaEZeDfFgVTC7dM2lapkIL1Hiq8LJKjLK2YVbdlYTFCHI3FgIthXHJgTxaa800QGib2xV2";
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (long) amount);
            params.put("currency", "usd");

            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount)
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            System.out.println(paymentIntent);
        }
    }
}
