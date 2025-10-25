package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Controller {

    @FXML
    private Label display;

    private StringBuilder fullDis = new StringBuilder();
    private String currInput = "";
    private String op = "";
    private double res = 0;
    private boolean waitingForNextNumber = false;

    @FXML
    public void onStartAnimation() {
        String name = "MADE BY RUPEN";
        fullDis.setLength(0);
        display.setText("");

        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        int delay = 200;

        for (int i = 0; i < name.length(); i++) {
            final int index = i;
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(delay * (i + 1)),
                    e -> {
                        fullDis.append(name.charAt(index));
                        display.setText(fullDis.toString());
                    }
            );
            timeline.getKeyFrames().add(kf);
        }

        javafx.animation.KeyFrame resetFrame = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(delay * (name.length() + 1)),
                e -> {
                    fullDis.setLength(0);
                    fullDis.append("0");
                    display.setText("0");
                    waitingForNextNumber = false;
                }
        );
        timeline.getKeyFrames().add(resetFrame);

        timeline.play();
    }

    @FXML
    private void onDigitClick(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if (waitingForNextNumber) {
            currInput = value;
            fullDis.append(value);
            waitingForNextNumber = false;
        } else {
            if (currInput.equals("0")) currInput = "";
            currInput += value;

            if (fullDis.toString().equals("0")) fullDis.setLength(0);
            fullDis.append(value);
        }

        display.setText(fullDis.toString());
    }

    @FXML
    private void onDotClick(ActionEvent event) {
        if (!currInput.contains(".")) {
            if (currInput.isEmpty() || waitingForNextNumber) {
                currInput = "0.";
                if (fullDis.toString().equals("0") || waitingForNextNumber) {
                    fullDis.setLength(0);
                }
                fullDis.append(currInput);
                waitingForNextNumber = false;
            } else {
                currInput += ".";
                fullDis.append(".");
            }
        }
        display.setText(fullDis.toString());
    }

    @FXML
    private void onOperatorClick(ActionEvent event) {
        String newOperator = ((Button) event.getSource()).getText();

        if (!currInput.isEmpty()) {
            if (!op.isEmpty()) {
                calculate();
            } else {
                res = Double.parseDouble(currInput);
            }
        }

        if (waitingForNextNumber && !op.isEmpty()) {
            fullDis.setLength(fullDis.length() - 3);
            fullDis.append(" ").append(newOperator).append(" ");
        } else {
            fullDis.append(" ").append(newOperator).append(" ");
        }

        op = newOperator;
        waitingForNextNumber = true;
        currInput = "";
        display.setText(fullDis.toString());
    }

    @FXML
    private void onEqualsClick(ActionEvent event) {
        if (!op.isEmpty() && !currInput.isEmpty()) {
            calculate();
            fullDis.setLength(0);
            fullDis.append(formatResult(res));
            display.setText(fullDis.toString());

            op = "";
            waitingForNextNumber = true;
            currInput = "";
        }
    }

    @FXML
    private void onClearClick(ActionEvent event) {
        currInput = "";
        op = "";
        res = 0;
        fullDis.setLength(0);
        fullDis.append("0");
        display.setText("0");
        waitingForNextNumber = false;
    }

    @FXML
    private void onClearEntryClick(ActionEvent event) {
        if (!currInput.isEmpty()) {
            int len = currInput.length();
            int sbLen = fullDis.length();
            fullDis.delete(sbLen - len, sbLen);
            currInput = "";
        }

        if (fullDis.length() == 0) {
            fullDis.append("0");
        }

        display.setText(fullDis.toString());
        waitingForNextNumber = true;
    }

    private void calculate() {
        double secondOperand = currInput.isEmpty() ? res : Double.parseDouble(currInput);
        double result = 0;

        switch (op) {
            case "+":
                result = this.res + secondOperand;
                break;
            case "-":
                result = this.res - secondOperand;
                break;
            case "*":
                result = this.res * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) result = this.res / secondOperand;
                else {
                    display.setText("Error");
                    onClearClick(null);
                    return;
                }
                break;
        }

        this.res = result;
    }

    private String formatResult(double value) {
        return new java.math.BigDecimal(String.valueOf(value))
                .stripTrailingZeros()
                .toPlainString();
    }
}