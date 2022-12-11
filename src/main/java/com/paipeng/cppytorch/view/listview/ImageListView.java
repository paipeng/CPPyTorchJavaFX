package com.paipeng.cppytorch.view.listview;

import com.paipeng.cppytorch.util.ImageUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageListView extends ListView {
    protected static Logger logger = LoggerFactory.getLogger(ImageListView.class);
    private List<ImageView> observableImageItems;
    private ImageListViewInterface imageListViewInterface;

    public ImageListView(List<ImageView> observableImageItems) {
        this.observableImageItems = observableImageItems;
    }

    public ImageListView() {
        super();
        setCustomCellFactory();
    }

    public void setImageListViewInterface(ImageListViewInterface imageListViewInterface) {
        this.imageListViewInterface = imageListViewInterface;
    }

    public void setCustomCellFactory(){
        logger.debug("setCustomCellFactory");
        setCellFactory(new Callback<ListView<String>, ListCell<String>>(){

            @Override
            public ListCell<String> call(ListView<String> arg0) {
                logger.debug("call: " + arg0);
                ListCell<String> cell = new ListCell<String>(){
                    private ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean flag) {
                        logger.debug("updateItem: " + item + " flag: " + flag);
                        super.updateItem(item, flag);
                        if (flag) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            if(imageView != null){
                                BufferedImage bufferedImage = null;
                                try {
                                    bufferedImage = ImageUtil.readImage(new File(item));
                                } catch (IOException e) {
                                    logger.error(e.getMessage());
                                }
                                if (bufferedImage != null) {
                                    imageView.setImage(ImageUtil.convertToFxImage(bufferedImage));
                                }
                                imageView.setFitHeight(180);
                                imageView.setFitWidth(180);
                            }
                            setGraphic(imageView);
                        }
                    }
                };
                cell.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {

                            public void handle(
                                    javafx.scene.input.MouseEvent event) {

                                @SuppressWarnings("unchecked")
                                ListCell<String> view = (ListCell<String>) event
                                        .getSource();

                                int index = view.getIndex();
                                logger.debug("selected item index: " + index);
                                ImageView imageView = (ImageView) view.getGraphic();
                                if (imageView != null
                                        && imageView.getImage() != null) {
                                    imageListViewInterface.updateImageView(imageView, (String)getItems().get(index));
                                }
                            }

                        });
                return cell;
            }

        });
    }

    public void setImageFiles(String imageFolder, File[] files) {
        ObservableList<String> models = FXCollections.observableArrayList();
        for (File file : files) {
            models.add(imageFolder + File.separator + file.getName());
        }
        setItems(models);
    }

    public interface ImageListViewInterface {
        void updateImageView(ImageView imageView, String filePath);
    }
}
