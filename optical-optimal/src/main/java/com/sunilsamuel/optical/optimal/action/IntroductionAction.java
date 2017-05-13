/**
 * IntroductionAction.java 
 *
 * Sunil Samuel CONFIDENTIAL
 *
 *  [2014] Sunil Samuel
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */
package com.sunilsamuel.optical.optimal.action;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class IntroductionAction {
	private Stage stage;
	private String fxmlFileName;
	private String propertyFileName;
	private String cssFileName;
	private String iconFileName;
	private String title;

	public IntroductionAction() {

	}

	public IntroductionAction(Stage stage) {
		this.stage = stage;
	}

	public IntroductionAction setIconFileName(String iconFileName) {
		this.iconFileName = iconFileName;
		return this;
	}

	public IntroductionAction setTitle(String title) {
		this.title = title;
		return this;
	}

	public IntroductionAction setStage(Stage stage) {
		this.stage = stage;
		return this;
	}

	public IntroductionAction setFxmlFile(String fxmlFileName) {
		this.fxmlFileName = fxmlFileName;
		return this;
	}

	public IntroductionAction setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
		return this;
	}

	public IntroductionAction setProperty(String propertyFileName) {
		this.propertyFileName = propertyFileName;
		return this;
	}

	public void process() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		ResourceBundle bundle = ResourceBundle.getBundle(propertyFileName);
		loader.setResources(bundle);
		Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFileName));
		// Scene scene = new Scene(rootNode);
		Scene scene = stage.getScene();
		if (scene == null) {
			scene = new Scene(rootNode);
		} else {
			scene.setRoot(rootNode);
		}
		scene.getStylesheets().add(cssFileName);
		if (title != null) {
			stage.setTitle(title);
		}
		if (iconFileName != null) {
			stage.getIcons().add(new Image(iconFileName));
		}
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

}
