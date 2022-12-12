package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.AsynchronTaskUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BasePane extends Pane {
    protected static Logger logger;
    protected static int count = 0;

    protected static final String[] filters = {
            ".jpeg",
            ".bmp",
            ".jpg",
            ".png",
            ".tif",
            ".tiff"
    };
    private static final String PREFIX = File.separator + "fxml" + File.separator;

    public BasePane() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());

        ResourceBundle resources = ResourceBundle.getBundle("bundles.languages", new Locale("zh", "Zh"));
        //Parent root = FXMLLoader.load(MainViewController.class.getResource(FXML_FILE), resources);

        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(this);
        loader.setControllerFactory(theClass -> this);

        String fileName = PREFIX + this.getClass().getSimpleName() + ".fxml";
        try {
            loader.setResources(resources);
            loader.load(this.getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    protected void initView() {
        logger.trace("initView");
    }

    public synchronized void decode(BufferedImage bufferedImage) {
        logger.trace("decode");
        boolean running = AsynchronTaskUtil.startTask(new AsynchronTaskUtil.AsynchronTaskInterface() {
            @Override
            public void doTask() {
                logger.trace("doTask");
                doDecode(bufferedImage);
                logger.trace("doTask end");
            }

            @Override
            public void taskEnd() {
                logger.trace("taskEnd");
                //updateView();
            }
        });

        if (running) {
            logger.trace("asynchronTask still running, skip this frame");
        }
    }

    protected abstract void doDecode(BufferedImage bufferedImage);
    public abstract Rectangle getFocusFrameSize();
}
