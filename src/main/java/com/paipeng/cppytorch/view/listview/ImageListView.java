package com.paipeng.cppytorch.view.listview;

import com.s2icode.okhttp.api.dto.NanogridDecoderDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;

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

    public void setSearchedNanogridDecoders(List<NanogridDecoderDTO> nanogridDecoders) {
        ObservableList<String> models = FXCollections.observableArrayList();
        for (NanogridDecoderDTO nanogridDecoder : nanogridDecoders) {
            models.add("https://crm.s2icode.com/api" + nanogridDecoder.getImagePath());
        }
        setItems(models);
    }
}
