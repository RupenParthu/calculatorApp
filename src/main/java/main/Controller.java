package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Controller {

    @FXML
    private Label display;

    private StringBuilder fullDis = new StringBuilder("0");
    private String operator = "";
    private double result = 0;
    private boolean startNewInput = true;
    private boolean lastInputWasOperator = false;
    private boolean lastInputWasEquals = false;

    @FXML
    private void initialize() {
        display.setText(fullDis.toString());
    }

    @FXML
    private void onDigitClick(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if (lastInputWasEquals) {
            // After equals, start fresh
            fullDis.setLength(0);
            operator = "";
            result = 0;
            lastInputWasEquals = false;
            startNewInput = true;
        }

        if (startNewInput) {
            if (lastInputWasOperator) {
                // Append to existing expression (e.g., "9 + " becomes "9 + 9")
                fullDis.append(value);
            } else {
                // Start new number
                fullDis.setLength(0);
                fullDis.append(value);
            }
            startNewInput = false;
        } else {
            // Append digit to current number
            fullDis.append(value);
        }

        display.setText(fullDis.toString());
        lastInputWasOperator = false;
    }

    @FXML
    private void onDotClick(ActionEvent event) {
        if (lastInputWasEquals) {
            fullDis.setLength(0);
            fullDis.append("0.");
            operator = "";
            result = 0;
            lastInputWasEquals = false;
            startNewInput = false;
            display.setText(fullDis.toString());
            return;
        }

        // Check if current number already has a decimal point
        String currentNumber = getCurrentNumber();
        if (!currentNumber.contains(".")) {
            if (startNewInput) {
                if (lastInputWasOperator) {
                    // Append to expression (e.g., "9 + " becomes "9 + 0.")
                    fullDis.append("0.");
                } else {
                    // Start new number
                    fullDis.setLength(0);
                    fullDis.append("0.");
                }
                startNewInput = false;
            } else {
                fullDis.append(".");
            }
        }

        display.setText(fullDis.toString());
        lastInputWasOperator = false;
    }

    @FXML
    private void onOperatorClick(ActionEvent event) {
        String newOperator = ((Button) event.getSource()).getText();

        if (lastInputWasEquals) {
            // Continue from result
            fullDis.setLength(0);
            fullDis.append(removeTrailingZeros(result)).append(" ").append(newOperator).append(" ");
            operator = newOperator;
            lastInputWasEquals = false;
            startNewInput = true;
            lastInputWasOperator = true;
            display.setText(fullDis.toString());
            return;
        }

        if (lastInputWasOperator) {
            // Replace previous operator
            fullDis.setLength(fullDis.length() - 3); // Remove " [op] "
            fullDis.append(" ").append(newOperator).append(" ");
            operator = newOperator;
            display.setText(fullDis.toString());
            return;
        }

        if (!fullDis.toString().equals("0")) {
            if (!operator.isEmpty()) {
                // Compute intermediate result
                calculate();
                fullDis.setLength(0);
                fullDis.append(removeTrailingZeros(result)).append(" ").append(newOperator).append(" ");
            } else {
                // Store first number as result
                result = Double.parseDouble(getCurrentNumber());
                fullDis.append(" ").append(newOperator).append(" ");
            }
        }

        operator = newOperator;
        startNewInput = true;
        lastInputWasOperator = true;
        display.setText(fullDis.toString());
    }

    @FXML
    private void onEqualsClick(ActionEvent event) {
        if (!operator.isEmpty() && !lastInputWasOperator) {
            calculate();
            fullDis.setLength(0);
            fullDis.append(removeTrailingZeros(result));
            display.setText(fullDis.toString());
            operator = "";
            lastInputWasEquals = true;
            startNewInput = true;
        }
    }

    private void calculate() {
        String[] parts = fullDis.toString().split(" ");
        double secondOperand = Double.parseDouble(parts[parts.length - 1]);
        double calcResult = 0;

        switch (operator) {
            case "+":
                calcResult = result + secondOperand;
                break;
            case "-":
                calcResult = result - secondOperand;
                break;
            case "*":
                calcResult = result * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    calcResult = result / secondOperand;
                } else {
                    fullDis.setLength(0);
                    fullDis.append("Error");
                    display.setText("Error");
                    onClearClick(null);
                    return;
                }
                break;
        }

        result = calcResult;
    }

    private String removeTrailingZeros(double value) {
        if (value == (long) value) return String.format("%d", (long) value);
        else return String.valueOf(Math.round(value * 10000000000.0) / 10000000000.0);
    }

    @FXML
    private void onClearClick(ActionEvent event) {
        fullDis.setLength(0);
        fullDis.append("0");
        operator = "";
        result = 0;
        startNewInput = true;
        lastInputWasOperator = false;
        lastInputWasEquals = false;
        display.setText("0");
    }

    @FXML
    private void onClearEntryClick(ActionEvent event) {
        if (lastInputWasEquals) {
            // Casio CE after result = show 0, not full reset
            fullDis.setLength(0);
            fullDis.append("0");
            display.setText("0");
            return;
        }

        if (!lastInputWasOperator && !fullDis.toString().equals("0")) {
            // Remove the current number
            String[] parts = fullDis.toString().split(" ");
            if (parts.length > 1) {
                // Expression like "9 + 5" -> remove "5"
                fullDis.setLength(0);
                fullDis.append(parts[0]).append(" ").append(operator).append(" ");
            } else {
                // Single number, reset to "0"
                fullDis.setLength(0);
                fullDis.append("0");
            }
        }

        startNewInput = true;
        display.setText(fullDis.toString());
    }

    // Helper method to get the current number from fullDis
    private String getCurrentNumber() {
        String[] parts = fullDis.toString().split(" ");
        return parts[parts.length - 1];
    }
}