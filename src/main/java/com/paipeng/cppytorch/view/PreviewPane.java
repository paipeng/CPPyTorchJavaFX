package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.controllers.MainViewController;
import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class PreviewPane extends BasePane {

    @FXML
    private ImageView previewImageView;

    @FXML
    private Button zoomInButton;

    @FXML
    private Button zoomOutButton;

    private double zoomFactor = 1.0;
    private double imageWidth;
    private double imageHeight;

    public PreviewPane() {
        super();

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        previewImageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/images/logo.png"))));
        imageWidth = previewImageView.getFitWidth();
        imageHeight = previewImageView.getFitHeight();

        previewImageView.preserveRatioProperty().set(true);


        previewImageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (previewImageView.getImage() != null) {
                    Dragboard dragboard = previewImageView.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putImage(previewImageView.getImage());
                    dragboard.setContent(clipboardContent);
                }
            }
        });

        zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomFactor += 0.1;
                previewImageView.setFitWidth(imageWidth * zoomFactor );
                previewImageView.setFitHeight(imageHeight * zoomFactor);
            }
        });

        zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomFactor -= 0.1;
                previewImageView.setFitWidth(imageWidth * zoomFactor );
                previewImageView.setFitHeight(imageHeight * zoomFactor);
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

    public void setPreviewImage(Image image) {
        this.previewImageView.setImage(image);
        imageWidth = previewImageView.getFitWidth();
        imageHeight = previewImageView.getFitHeight();
    }
}
