package utils;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class SendSMS {
    public static final String ACCOUNT_SID = "ACe8936dada811151837a7c1605e4398fa";
    public static final String AUTH_TOKEN = "3305d321178236f66577f1663b052e3b";

    public static void SendSMS(String msg, String number) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(number),
                new com.twilio.type.PhoneNumber("+19896254702"),
                msg).create();

        System.out.println(message.getSid());
    }
}
