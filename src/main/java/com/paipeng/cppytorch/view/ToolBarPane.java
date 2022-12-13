package com.paipeng.cppytorch.view;

import com.paipeng.cppytorch.util.ImageUtil;
import com.s2icode.okhttp.api.NanogridDecoderSearchHttpClient;
import com.s2icode.okhttp.api.dto.NanogridDecoderDTO;
import com.s2icode.okhttp.api.model.NanogridDecoderSearch;
import com.s2icode.okhttp.base.HttpClientCallback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ToolBarPane extends BasePane{
    private final String baseUrl = "https://crm.s2icode.com/api/api";
    private final String token = "3bad8693-7b1c-4a52-8fdb-90e226a5a24d";
    private NanogridDecoderSearchHttpClient nanogridDecoderSearchHttpClient;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button selectImageFolderButton;

    @FXML
    private TextField inputTextField;

    @FXML
    private Button selectGroupFolderButton;

    @FXML
    private Button searchButton;

    private ToolBarPaneInterface toolBarPaneInterface;


    public ToolBarPane() {
        super();
        nanogridDecoderSearchHttpClient = new NanogridDecoderSearchHttpClient(baseUrl, token);
        initView();
    }

    public void setToolBarPaneInterface(ToolBarPaneInterface toolBarPaneInterface) {
        this.toolBarPaneInterface = toolBarPaneInterface;
    }

    @Override
    protected void initView() {
        super.initView();

        selectImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(thisStage);
                if (file != null) {
                    logger.trace("selected image file: " + file.getAbsolutePath());
                    toolBarPaneInterface.selectImage(file);
                    inputTextField.setText(file.getAbsolutePath());
                }
            }
        });

        selectImageFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Open Resource File");

                File selectedFolder = directoryChooser.showDialog(thisStage);

                toolBarPaneInterface.selectImageFolder(selectedFolder.getAbsolutePath());
            }
        });

        selectGroupFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Open Resource File");

                File selectedFolder = directoryChooser.showDialog(thisStage);

                toolBarPaneInterface.selectGroupFolder(selectedFolder.getAbsolutePath());
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search();
            }
        });
    }

    private void search() {
        nanogridDecoderSearchHttpClient.setHttpClientCallback(new HttpClientCallback<List<NanogridDecoderDTO>>() {
            @Override
            public void onSuccess(List<NanogridDecoderDTO> nanogridDecoders) {
                logger.debug("success: " + nanogridDecoders.size());
                toolBarPaneInterface.searched(nanogridDecoders);
            }

            @Override
            public void onFailure(int code, String message) {
                logger.error("getTicketData error: " + code + " msg: " + message);
            }
        });

        NanogridDecoderSearch nanogridDecoderSearch = new NanogridDecoderSearch();
        nanogridDecoderSearch.setBeginTime("2022-12-10 00:00:00");
        nanogridDecoderSearch.setEndTime("2022-12-31 00:00:00");
        nanogridDecoderSearch.setSoftwareName("js");
        nanogridDecoderSearch.setDebug(-1);
        nanogridDecoderSearch.setLowPercentUpload(-1);
        nanogridDecoderSearch.setLatitudeMax(Double.valueOf(10000));
        nanogridDecoderSearch.setLatitudeMin(Double.valueOf(-10000));
        nanogridDecoderSearch.setLongitudeMax(Double.valueOf(10000));
        nanogridDecoderSearch.setLongitudeMin(Double.valueOf(-10000));

        nanogridDecoderSearch.setNanogridSerialNumber("");
        nanogridDecoderSearch.setDeviceName("");
        nanogridDecoderSearch.setDetectedState("");
        nanogridDecoderSearch.setHttpStatusCode("");
        nanogridDecoderSearch.setImagePath("");
        nanogridDecoderSearch.setResultCode("");
        nanogridDecoderSearch.setInspectionState("");
        nanogridDecoderSearch.setShootingMode("");
        nanogridDecoderSearch.setSystemId("");
        nanogridDecoderSearch.setUniqueDeviceUdid("");
        nanogridDecoderSearch.setSoftwareVersion("");

        nanogridDecoderSearchHttpClient.search(nanogridDecoderSearch);

    }

    @Override
    protected void doDecode(BufferedImage bufferedImage) {

    }

    @Override
    public Rectangle getFocusFrameSize() {
        return null;
    }

    public interface ToolBarPaneInterface {
        void selectImage(File file);
        void selectImageFolder(String imageFolder);
        void selectGroupFolder(String groupFolder);
        void searched(List<NanogridDecoderDTO> nanogridDecoders);
    }
}
