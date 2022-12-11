package com.paipeng.cppytorch.util;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PytorchUtil {
    private static ZooModel model;


    private static class PytorchUtilHolder {
        static final PytorchUtil INSTANCE = new PytorchUtil();
    }

    public static PytorchUtil getInstance() {
        return PytorchUtilHolder.INSTANCE;
    }


    public void init(String modelPath, boolean grayscale) throws ModelNotFoundException, MalformedModelException, IOException, TranslateException {
        Translator<Image, Classifications> translator = null;
        if (grayscale) {
            translator = new Translator<Image, Classifications>() {

                @Override
                public NDList processInput(TranslatorContext ctx, Image input) {
                    // Convert Image to NDArray
                    NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.GRAYSCALE);
                    return new NDList(NDImageUtils.toTensor(array));
                }

                @Override
                public Classifications processOutput(TranslatorContext ctx, NDList list) {
                    // Create a Classifications with the output probabilities
                    NDArray probabilities = list.singletonOrThrow().softmax(0);
                    List<String> classNames = IntStream.range(0, 3).mapToObj(String::valueOf).collect(Collectors.toList());
                    return new Classifications(classNames, probabilities);
                }

                @Override
                public Batchifier getBatchifier() {
                    // The Batchifier describes how to combine a batch together
                    // Stacking, the most common batchifier, takes N [X1, X2, ...] arrays to a single [N, X1, X2, ...] array
                    return Batchifier.STACK;
                }
            };
        } else {
            translator = ImageClassificationTranslator.builder()
                    //.addTransform(new Resize(28))
                    //.addTransform(new CenterCrop(224, 224))
                    .addTransform(new ToTensor())
                    .addTransform(new Normalize(
                            new float[]{0.485f, 0.456f, 0.406f},
                            new float[]{0.229f, 0.224f, 0.225f}))
                    .optApplySoftmax(true)
                    .build();
        }
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                .optModelPath(Paths.get(modelPath))
                //.optOption("mapLocation", "true") // this model requires mapLocation for GPU
                .optTranslator(translator)
                .optProgress(new ProgressBar()).build();

        model = criteria.loadModel();


    }

    public Classifications predict(Image image) throws TranslateException {
        //Image img = ImageFactory.getInstance().from.fromUrl("https://raw.githubusercontent.com/pytorch/hub/master/images/dog.jpg");
        image.getWrappedImage();

        Predictor<Image, Classifications> predictor = model.newPredictor();
        Classifications classifications = predictor.predict(image);
        return classifications;
    }


    public Classifications predict(NDArray ndArray) throws TranslateException {
        Predictor<NDArray, Classifications> predictor = model.newPredictor();
        Classifications classifications = predictor.predict(ndArray);
        return classifications;
    }
}
