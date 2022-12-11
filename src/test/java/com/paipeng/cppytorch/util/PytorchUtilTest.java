package com.paipeng.cppytorch.util;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class PytorchUtilTest {

    @Test
    void predict() throws TranslateException, ModelNotFoundException, MalformedModelException, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/dog.jpeg");
        Image img = BufferedImageFactory.getInstance().fromInputStream(inputStream);

        PytorchUtil.getInstance().init("models/resnet18", false);
        Classifications classifications = PytorchUtil.getInstance().predict(img);

        System.out.println(classifications);
        Assertions.assertNotNull(classifications);
        Assertions.assertTrue(classifications.best().getClassName().equals("n02111889 Samoyed, Samoyede"));

    }


    @Test
    public void testPyTorchChessPieces() throws TranslateException, ModelNotFoundException, MalformedModelException, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/chess_269260997447975_5_7.bmp");
        Image img = BufferedImageFactory.getInstance().fromInputStream(inputStream);

        PytorchUtil.getInstance().init("models/bookmodel.ptl", true);
        Classifications classifications = PytorchUtil.getInstance().predict(img);

        System.out.println(classifications);
        Assertions.assertNotNull(classifications);
    }
}