package com.paipeng.javafxapp;

import com.paipeng.javafxapp.controllers.MainViewController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

public class AppApplication extends Application {

    public static HostServices hostServices;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        hostServices = getHostServices();
        try {
            if (java.awt.SplashScreen.getSplashScreen() != null && java.awt.SplashScreen.getSplashScreen().isVisible()) {
                java.awt.SplashScreen.getSplashScreen().close();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        MainViewController.start();
    }


}
