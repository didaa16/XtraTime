package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;

public class StripePaymentService {
    public PaymentIntent createPayment(double amount, String currency, String description) {
        try {
            Stripe.apiKey = "sk_test_51OqJbyP8fZ7mtZrf1ucCZuWgiG9TwaEZeDfFgVTC7dM2lapkIL1Hiq8LJKjLK2YVbdlYTFCHI3FgIthXHJgTxaa800QGib2xV2";

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (amount * 100))
                    .setCurrency(currency)
                    .setDescription(description)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return paymentIntent;
        } catch (StripeException e) {
            // Gérer l'exception
            e.printStackTrace();
            return null;
        }
    }

}

