package sample;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label charsPM;

    @FXML
    private Label charsPMAverage;

    private int charsCount = 0;
    private long timeStart;
    private long timeFinish;

    private boolean firstChar = true;

    private int attempts = 0;
    private int totalCount = 0;

    @FXML
    void initialize() {

        AtomicReference<String> curString = new AtomicReference<>(getRandomString());

        text.setText(curString.get());
        AtomicInteger position = new AtomicInteger();
        AtomicInteger finalTextLength = new AtomicInteger(curString.get().length());

        textform.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                event.consume(); // to cancel character-removing keys
            }
        });

        textform.setOnKeyReleased(event -> {
            if (firstChar) {
                timeStart = System.currentTimeMillis();
                firstChar = false;
            }

            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {

            } else if (position.get() > finalTextLength.get() || event.getText().charAt(0) != curString.get().charAt(position.get())) {
                textform.setText(textform.getText().substring(0, textform.getText().length()-1));
                textform.positionCaret(textform.getText().length());
            } else {
                position.getAndIncrement();
                if (position.get() == finalTextLength.get()) {
                    firstChar = true;
                    timeFinish = System.currentTimeMillis();
                    curString.set(getRandomString());
                    finalTextLength.set(curString.get().length());
                    text.setText(curString.get());
                    textform.setText("");
                    textform.positionCaret(0);
                    position.set(0);
                    updateWordsPM();
                }
            }
        });
    }

    private void updateWordsPM() {
        int value = (int)(charsCount / (((double)timeFinish - timeStart) / 60000));
        charsPM.setText(String.valueOf(value));
        totalCount += value;
        attempts++;
        charsPMAverage.setText(String.valueOf(totalCount / attempts));
    }

    public String getRandomString() {
        int lineLength = 52;
        int textLength = 0;
        StringBuilder rndString = new StringBuilder();
        charsCount = 0;
        while (true) {
            String rndWord = RandomWordGenerator.getRandomWord();
            if (textLength + rndWord.length() <= lineLength) {
                textLength += rndWord.length();
                rndString.append(rndWord + " ");
            } else {
                charsCount = textLength;
                break;
            }
        }
        return rndString.substring(0, rndString.length()-1);
    }
}
