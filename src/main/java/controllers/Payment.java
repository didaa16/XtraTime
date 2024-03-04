package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class Payment {
    public static void main(String[] args) {
// Set your secret key here
        Stripe.apiKey = "sk_test_51OqJbyP8fZ7mtZrf1ucCZuWgiG9TwaEZeDfFgVTC7dM2lapkIL1Hiq8LJKjLK2YVbdlYTFCHI3FgIthXHJgTxaa800QGib2xV2";

        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }}
