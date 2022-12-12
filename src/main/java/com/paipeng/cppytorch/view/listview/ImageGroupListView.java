package com.paipeng.cppytorch.view.listview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class ImageGroupListView extends BaseImageListView {
    public ImageGroupListView() {
        super();
    }

    @Override
    public void setImageFiles(String imageFolder, File[] files) {
        ObservableList<String> models = FXCollections.observableArrayList();
        for (File file : files) {
            logger.debug("file: " + file.getAbsolutePath());
            if (imageFolder != null) {
                models.add(imageFolder + File.separator + file.getName());
            } else {
                models.add(file.getAbsolutePath());
            }
        }
        setItems(models);
    }

}
