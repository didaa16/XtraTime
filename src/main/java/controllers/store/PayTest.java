package controllers.store;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

public class PayTest {
    public static String createCustomer(String email) throws StripeException {
        Stripe.apiKey = "sk_test_51OqJbyP8fZ7mtZrf1ucCZuWgiG9TwaEZeDfFgVTC7dM2lapkIL1Hiq8LJKjLK2YVbdlYTFCHI3FgIthXHJgTxaa800QGib2xV2";

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();

        Customer customer = Customer.create(params);
        return customer.getId();
    }

    public static void main(String[] args) {
        try {
            String customerId = createCustomer("example@example.com");
            System.out.println("Customer ID: " + customerId);
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
