package com.paipeng.cppytorch.view;

import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import com.paipeng.cppytorch.util.ImageUtil;
import com.paipeng.cppytorch.util.PytorchUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class CPPyTorchPane extends BasePane {
    @FXML
    private TextField classCountTextField;
    @FXML
    private Button predictButton;
    @FXML
    private ListView modelListView;

    @FXML
    private TextArea predictResultTextArea;

    private CPPyTorchPaneInterface cpPyTorchPaneInterface;


    public void setCpPyTorchPaneInterface(CPPyTorchPaneInterface cpPyTorchPaneInterface) {
        this.cpPyTorchPaneInterface = cpPyTorchPaneInterface;
    }

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

        predictButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BufferedImage bufferedImage = cpPyTorchPaneInterface.getBufferedImage();
                if (bufferedImage == null) {
                    String imagePath = cpPyTorchPaneInterface.getImagePath();
                    try {
                        bufferedImage = ImageUtil.readImage(new File(imagePath));
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
                if (bufferedImage != null) {
                    doDecode(bufferedImage);
                }
            }
        });

        ObservableList<String> models = readModels();
        for (String f : models) {
            logger.trace("f: " + f);
        }

        modelListView.setItems(models);

        MultipleSelectionModel<String> multipleSelectionModel = modelListView.getSelectionModel();
        multipleSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        multipleSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> changed, String oldVal, String newVal) {
                String selectedModel = "";
                ObservableList<String> selected = modelListView.getSelectionModel().getSelectedItems();

                for (int i = 0; i < selected.size(); i++) {
                    selectedModel = selected.get(i);
                }


                logger.trace("selected: " + selectedModel);

                try {
                    String pwd = System.getProperty("user.dir");
                    PytorchUtil.getInstance().init(pwd + File.separator + "models" + File.separator + selectedModel, true);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }

    @Override
    protected void doDecode(BufferedImage bufferedImage) {
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        Classifications classifications = null;
        try {
            classifications = PytorchUtil.getInstance().predict(img);
            logger.debug(classifications.toString());

            predictResultTextArea.setText(classifications.toString());
        } catch (TranslateException e) {
            logger.error(e.getMessage());
        }
    }

    public void predict(BufferedImage bufferedImage) {
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        Classifications classifications = null;
        try {
            classifications = PytorchUtil.getInstance().predict(img);
            logger.debug(classifications.toString());

            predictResultTextArea.setText(classifications.toString());
        } catch (TranslateException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public Rectangle getFocusFrameSize() {
        return new Rectangle(360, 380);
    }

    private ObservableList<String> readModels() {
        String pwd = System.getProperty("user.dir");
        logger.trace("readModels under: " + pwd);

        File dir = new File(pwd + File.separator + "models");
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ptl");
            }
        });

        ObservableList<String> models = FXCollections.observableArrayList();
        for (File file : files) {
            models.add(file.getName());
        }
        return models;
    }

    public interface CPPyTorchPaneInterface {
        BufferedImage getBufferedImage();

        String getImagePath();
    }
}
