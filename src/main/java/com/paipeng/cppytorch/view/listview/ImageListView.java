package com.paipeng.cppytorch.view.listview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class ImageListView extends BaseImageListView {

    public ImageListView() {
        super();
    }


    @Override
    public void setImageFiles(String imageFolder, File[] files) {
        ObservableList<String> models = FXCollections.observableArrayList();
        for (File file : files) {
            models.add(imageFolder + File.separator + file.getName());
        }
        setItems(models);
    }

}
