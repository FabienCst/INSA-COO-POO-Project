package bavard.chat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class MessageController {

    @FXML
    private Label messageText;

    public void setMessageTextTo(String text, OffsetDateTime dt) {
        String minute = "";
        if (dt.getMinute() < 10) {
            minute = "0"+dt.getMinute();
        } else {
          minute = ""+dt.getMinute();
        }
        String time = dt.getDayOfMonth()+"/"+dt.getMonthValue()+" at "+dt.getHour()+":"+minute;
        messageText.setText(time+" : \n"+text);
    }

}
