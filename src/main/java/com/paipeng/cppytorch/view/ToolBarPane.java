package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.ImageUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ToolBarPane extends BasePane{

    @FXML
    private Button selectImageButton;

    @FXML
    private Button selectImageFolderButton;

    @FXML
    private TextField inputTextField;

    @FXML
    private Button selectGroupFolderButton;

    private ToolBarPaneInterface toolBarPaneInterface;


    public ToolBarPane() {
        super();
        initView();
    }

    public void setToolBarPaneInterface(ToolBarPaneInterface toolBarPaneInterface) {
        this.toolBarPaneInterface = toolBarPaneInterface;
    }

    @Override
    protected void initView() {
        super.initView();

        selectImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(thisStage);
                if (file != null) {
                    logger.trace("selected image file: " + file.getAbsolutePath());
                    toolBarPaneInterface.selectImage(file);
                    inputTextField.setText(file.getAbsolutePath());
                }
            }
        });

        selectImageFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Open Resource File");

                File selectedFolder = directoryChooser.showDialog(thisStage);

                toolBarPaneInterface.selectImageFolder(selectedFolder.getAbsolutePath());
            }
        });

        selectGroupFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    @Override
    protected void doDecode(BufferedImage bufferedImage) {

    }

    @Override
    public Rectangle getFocusFrameSize() {
        return null;
    }

    public interface ToolBarPaneInterface {
        void selectImage(File file);
        void selectImageFolder(String imageFolder);
    }
}
