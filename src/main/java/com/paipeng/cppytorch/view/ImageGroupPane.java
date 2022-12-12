package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.CommonUtil;
import com.paipeng.cppytorch.view.listview.ImageGroupListView;
import javafx.fxml.FXML;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageGroupPane extends BasePane {

    @FXML
    private ImageGroupListView imageGroupListView;
    private static final String[] filters = {
            ".jpeg",
            ".bmp",
            ".jpg",
            ".png",
            ".tif",
            ".tiff"
    };
    private String imageFolder;

    public ImageGroupPane() {

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

        imageGroupListView.setImageFiles(this.imageFolder, files);
    }

}
