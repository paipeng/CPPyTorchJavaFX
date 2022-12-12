package com.paipeng.cppytorch.controllers;

import com.paipeng.cppytorch.util.ImageUtil;
import com.paipeng.cppytorch.view.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public static Logger logger = LoggerFactory.getLogger(MainViewController.class);
    private static Stage stage;
    private static final String FXML_FILE = "/fxml/MainViewController.fxml";


    @FXML
    private CPPyTorchPane cpPyTorchPane;

    @FXML
    private ImageListPane imageListPane;

    @FXML
    private PreviewPane previewPane;

    @FXML
    private ImageGroupPane imageGroupPane;

    @FXML
    private ToolBarPane toolBarPane;

    private BufferedImage bufferedImage;

    private String imagePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cpPyTorchPane.setCpPyTorchPaneInterface(new CPPyTorchPane.CPPyTorchPaneInterface() {
            @Override
            public BufferedImage getBufferedImage() {
                return bufferedImage;
            }

            @Override
            public String getImagePath() {
                return imagePath;
            }
        });

        toolBarPane.setToolBarPaneInterface(new ToolBarPane.ToolBarPaneInterface() {
            @Override
            public void selectImage(File file) {
                try {
                    bufferedImage = ImageUtil.readImage(file);
                    if (bufferedImage != null) {
                        previewPane.setPreviewImage(ImageUtil.convertToFxImage(bufferedImage));
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            @Override
            public void selectImageFolder(String imageFolder) {
                if (imageFolder != null) {
                    logger.debug("selectedFolder: " + imageFolder);
                    imageListPane.setSelectedImageFolder(imageFolder);
                }
            }

            @Override
            public void selectGroupFolder(String groupFolder) {
                imageGroupPane.setSelectedImageFolder(groupFolder);
            }
        });


        imageListPane.setImageListPaneInterface(new ImageListPane.ImageListPaneInterface() {
            @Override
            public void updateImageView(ImageView imageView, String filePath) {
                logger.debug("updateImageView: " + filePath);
                imagePath = filePath;
                previewPane.setPreviewImage(imageView.getImage());
                bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                cpPyTorchPane.predict(bufferedImage);
            }
        });
    }

    public static void start() {
        logger.trace("start");
        try {
            ResourceBundle resources = ResourceBundle.getBundle("bundles.languages", new Locale("zh", "Zh"));
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainViewController.class.getResource(FXML_FILE)), resources);

            Scene scene = new Scene(root);
            stage = new Stage();
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(resources.getString("title"));
            stage.getIcons().add(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/images/logo.png"))));
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}