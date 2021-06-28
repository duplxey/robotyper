package com.duplxey.robotyper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    @FXML
    private TextArea messageInput;

    @FXML
    private Button launchButton;

    @FXML
    private Slider keystrokeDelaySlider;

    @FXML
    private Slider startDelaySlider;

    @FXML
    private CheckBox replaceTabsCheck;

    @FXML
    private TextField spacesInput;

    private Robot robot;

    public Controller() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    void pressKey(char c) {
        if (Character.isUpperCase(c)) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.delay(100);
        }
        robot.keyPress(KeyEvent.VK_A);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.keyRelease(KeyEvent.VK_A);
        if (Character.isUpperCase(c)) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.delay(100);
        }
    }

    @FXML
    private void launch() {
        String message = messageInput.getText();

        if (message.length() == 0) {
            launchButton.setText("Message is empty. :(");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                launchButton.setText("Launch");
            }));
            timeline.setCycleCount(1);
            timeline.play();
            return;
        }

        final int[] i = {(int) startDelaySlider.getValue()};
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            launchButton.setText("Starting in " + i[0] + " seconds.");
            i[0]--;

            if (i[0] == -1) {
                launchButton.setText("Launch");

                // press each key
                for (char c : message.toCharArray()) {
                    pressKey(c);

                    // sleep for keystrokeDelay
                    try {
                        Thread.sleep((long) keystrokeDelaySlider.getValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
        timeline.setCycleCount(i[0] + 1);
        timeline.play();
    }
}
