package services.store;

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
            try{
            Stripe.apiKey = "sk_test_51OqJbyP8fZ7mtZrf1ucCZuWgiG9TwaEZeDfFgVTC7dM2lapkIL1Hiq8LJKjLK2YVbdlYTFCHI3FgIthXHJgTxaa800QGib2xV2";

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount)
                    .setCurrency("usd")
                    .setPaymentMethod("pm_card_visa")
                    .build();
                PaymentIntent intent = PaymentIntent.create(params);
            // If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
// If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
        }
    }
}
