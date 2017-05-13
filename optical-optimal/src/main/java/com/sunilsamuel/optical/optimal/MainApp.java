package com.sunilsamuel.optical.optimal;

import java.util.ResourceBundle;

import com.sunilsamuel.optical.optimal.controller.OpticalOptimalController;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("properties/messages");
		new OpticalOptimalController().handleIntroductionPage(stage, bundle);
	}
}
