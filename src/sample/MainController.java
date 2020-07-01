package sample;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class MainController {

    @FXML
    private TextField textform;

    @FXML
    private TextArea text;

    @FXML
    void initialize() {

        AtomicReference<String> curString = new AtomicReference<>(getRandomString());

        text.setPromptText(curString.get());
        AtomicInteger position = new AtomicInteger();
        AtomicInteger finalTextLength = new AtomicInteger(curString.get().length());

//        textform.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            public void handle(KeyEvent event) {
//                if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
//                    event.consume(); // to cancel character-removing keys
//                }
//            }
//        });

        textform.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                event.consume(); // to cancel character-removing keys
            }
        });

        textform.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {

            } else if (position.get() > finalTextLength.get() || event.getText().charAt(0) != curString.get().charAt(position.get())) {
                textform.setText(textform.getText().substring(0, textform.getText().length()-1));
                textform.positionCaret(textform.getText().length());
            } else {
                position.getAndIncrement();
                if (position.get() == finalTextLength.get()) {
                    curString.set(getRandomString());
                    finalTextLength.set(curString.get().length());
                    text.setText(curString.get());
                    textform.setText("");
                    textform.positionCaret(0);
                    position.set(0);
                }
            }
        });
    }

    public String getRandomString() {
        int lineLength = 120;
        int textLength = 0;
        StringBuilder rndString = new StringBuilder();
        while (true) {
            String rndWord = RandomWordGenerator.getRandomWord();
            textLength += rndWord.length();
            if (textLength + rndWord.length() <= lineLength) {
                textLength += rndWord.length();
                rndString.append(rndWord + " ");
            } else {
                break;
            }
        }
        return rndString.substring(0, rndString.length()-1);
    }
}
