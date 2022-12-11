package com.paipeng.cppytorch.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static BufferedImage getImage(String filename) {
        // This time, you can use an InputStream to load
        try {
            // Grab the InputStream for the image.
            InputStream in = ImageUtil.class.getResourceAsStream(filename);
            // Then read it.
            return ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("The image was not loaded.");
            //System.exit(1);
        }

        return null;
    }

    public static BufferedImage cropBufferedImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        return bufferedImage.getSubimage(x, y, width, height);
    }


    public static BufferedImage resizeImage(BufferedImage bufferedImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
