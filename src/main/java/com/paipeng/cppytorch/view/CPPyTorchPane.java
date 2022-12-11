package com.paipeng.cppytorch.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

public class CPPyTorchPane extends BasePane {
    @FXML
    private TextField classCountTextField;
    @FXML
    private Button predictButton;
    @FXML
    private ListView modelListView;

    public CPPyTorchPane() {
        super();

        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        classCountTextField.setTextFormatter(textFormatter);
    }

    @Override
    protected void doDecode(BufferedImage bufferedImage) {

    }

    @Override
    public Rectangle getFocusFrameSize() {
        return new Rectangle(360, 380);
    }
}
