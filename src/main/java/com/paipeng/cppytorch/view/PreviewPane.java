package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.controllers.MainViewController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class PreviewPane extends BasePane {

    @FXML
    private ImageView previewImageView;
    public PreviewPane() {
        super();

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        previewImageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/images/logo.png"))));

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
    }
}
