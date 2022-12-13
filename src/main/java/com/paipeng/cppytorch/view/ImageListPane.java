package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.CommonUtil;
import com.paipeng.cppytorch.util.SleepAsynchronTaskUtil;
import com.paipeng.cppytorch.view.listview.ImageListView;
import com.s2icode.okhttp.api.dto.NanogridDecoderDTO;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ImageListPane extends BasePane {
    @FXML
    private ImageListView imageListView;

    private String imageFolder;

    private ImageListPaneInterface imageListPaneInterface;

    public ImageListPane() {
        super();

        initView();
    }

    public void setImageListPaneInterface(ImageListPaneInterface imageListPaneInterface) {
        this.imageListPaneInterface = imageListPaneInterface;
    }

    @Override
    protected void initView() {
        super.initView();

        imageListView.setImageListViewInterface(new ImageListView.ImageListViewInterface() {
            @Override
            public void updateImageView(ImageView imageView, String filePath) {
                imageListPaneInterface.updateImageView(imageView, filePath);
            }
        });


        SleepAsynchronTaskUtil.startTask(200, () -> {
            logger.trace("update tableview now");

            Pane parentPane = (Pane) getParent();
            if (parentPane != null) {
                logger.trace("parentPane size: " + parentPane.getWidth() + "-" + parentPane.getHeight());

                //imageListView.setPrefWidth(parentPane.getWidth());
                imageListView.setPrefHeight(parentPane.getHeight() - 620);
                //imageListView.setMinWidth(parentPane.getWidth());
                imageListView.setMinHeight(parentPane.getHeight() - 100);
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

    public void setSelectedImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
        initImageListView();
    }

    private void initImageListView() {
        File[] files = CommonUtil.getFiles(this.imageFolder, filters);
        for (File file : files) {
            logger.debug(file.getAbsolutePath());
        }
        imageListView.setImageFiles(this.imageFolder, files);
    }

    public void searched(List<NanogridDecoderDTO> nanogridDecoders) {
        imageListView.setSearchedNanogridDecoders(nanogridDecoders);

    }

    public interface ImageListPaneInterface {
        void updateImageView(ImageView imageView, String filePath);
    }
}
