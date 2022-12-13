package com.paipeng.cppytorch.view.listview;

import com.paipeng.cppytorch.util.ImageUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BaseImageListView extends ListView {
    protected Logger logger;

    protected double cropFactor = 1;

    protected ImageListView.ImageListViewInterface imageListViewInterface;

    public BaseImageListView() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());
        setCustomCellFactory();

        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("onDragOver");
                if (event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("setOnDragDropped");

            }
        });
    }

    public void setImageListViewInterface(ImageListViewInterface imageListViewInterface) {
        this.imageListViewInterface = imageListViewInterface;
    }

    protected void setCustomCellFactory(){
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
                                logger.debug("update imageView " + cropFactor);
                                if (item.startsWith("http")) {
                                    Image image = new Image(item);
                                    imageView.setImage(image);
                                } else {
                                    BufferedImage bufferedImage = null;
                                    try {
                                        bufferedImage = ImageUtil.readImage(new File(item));
                                        if (cropFactor != 1.0) {
                                            int cropWidth = (int)(bufferedImage.getWidth()* cropFactor);
                                            int cropHeight = (int)(bufferedImage.getHeight() * cropFactor);
                                            int cropOffsetX = (int)((bufferedImage.getWidth() - cropWidth)/2.0);
                                            int cropOffsetY = (int)((bufferedImage.getHeight() - cropHeight)/2.0);

                                            bufferedImage = ImageUtil.cropBufferedImage(bufferedImage, cropOffsetX, cropOffsetY, cropWidth, cropHeight);
                                        }
                                    } catch (IOException e) {
                                        logger.error(e.getMessage());
                                    }
                                    if (bufferedImage != null) {
                                        logger.debug("bufferedImage size: " + bufferedImage.getWidth() + "-" + bufferedImage.getHeight());
                                        imageView.setImage(ImageUtil.convertToFxImage(bufferedImage));
                                    }
                                }

                                imageView.setFitHeight(190);
                                imageView.setFitWidth(190);

                                imageView.setOnDragOver(new EventHandler<DragEvent>() {
                                    @Override
                                    public void handle(DragEvent event) {
                                        logger.debug("onDragOver");
                                        if (event.getDragboard().hasImage()) {
                                            event.acceptTransferModes(TransferMode.ANY);
                                        }
                                    }
                                });

                                imageView.setOnDragDropped(new EventHandler<DragEvent>() {
                                    @Override
                                    public void handle(DragEvent event) {
                                        logger.debug("setOnDragDropped");
                                        Image image = event.getDragboard().getImage();
                                        imageView.setImage(image);

                                    }
                                });
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

    }

    public interface ImageListViewInterface {
        void updateImageView(ImageView imageView, String filePath);
    }

    public void setZoomIn() {
        cropFactor -= 0.1;
        refresh();
    }

    public void setZoomOut() {
        cropFactor += 0.1;
        if (cropFactor > 1) {
            cropFactor = 1;
        }
        refresh();
    }
}
