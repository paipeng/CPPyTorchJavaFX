package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.CommonUtil;
import com.paipeng.cppytorch.view.listview.ImageGroupListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageGroupPane extends BasePane {

    @FXML
    private ImageGroupListView imageGroupListView;

    @FXML
    private Button zoomOutButton;
    @FXML
    private Button zoomInButton;

    private String imageFolder;

    public ImageGroupPane() {
        super();
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageGroupListView.setZoomOut();
            }
        });

        zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageGroupListView.setZoomIn();
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
        String[] folders = CommonUtil.getSubFolders(this.imageFolder);

        File[] files = new File[folders.length];
        int index = 0;
        for (String folder : folders) {
            logger.debug(folder);
            File[] fs = CommonUtil.getFiles(this.imageFolder + File.separator + folder, filters);
            if (fs != null && fs.length > 0) {
                files[index] = fs[0];
                index++;
            }
        }

        imageGroupListView.setImageFiles(null, files);
    }

}
