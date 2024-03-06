package controllers.Abonnement;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.event.ActionEvent;


public class TestSmsController {

    public static final String ACCOUNT_SID = "AC294826a0d7e01332b57990ad5f8149d6";
    public static final String AUTH_TOKEN = "ac5f577e5b9b37e9e6114295d4cab892";
    @javafx.fxml.FXML
    public void SendSMSclicked(ActionEvent actionEvent) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(
                        new com.twilio.type.PhoneNumber("+21652354494"),
                        new com.twilio.type.PhoneNumber("+13392290039"),
                        "This is the ship that made the Kessel Run in fourteen parsecs?"
                )
                .create();

        System.out.println(message.getSid());

    }
}
