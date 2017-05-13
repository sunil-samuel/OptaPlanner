/**
 * OpticalOptimalController.java 
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
package com.sunilsamuel.optical.optimal.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sunilsamuel.optical.optimal.action.DirectoryListingAction;
import com.sunilsamuel.optical.optimal.action.IntroductionAction;
import com.sunilsamuel.optical.optimal.javafx.model.Folder;
import com.sunilsamuel.optical.optimal.planner.OpticalOptimalSolver;
import com.sunilsamuel.optical.optimal.planner.PrepareOpticalForPlanner;
import com.sunilsamuel.optical.optimal.planner.model.FolderPlanningEntity;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDriveBucket;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDrivePlannerSolution;
import com.sunilsamuel.optical.optimal.action.DirectoryListingAction.DOT;

public class OpticalOptimalController implements Initializable {

	@FXML
	private TextField numOfDiscs;

	@FXML
	private Slider sizeSlider;

	@FXML
	private TextField discSize;

	@FXML
	private ComboBox sizeType;

	@FXML
	private ComboBox discType;

	@FXML
	private Label mbView;

	@FXML
	private Label gbView;

	@FXML
	private Button validateInput;

	private ResourceBundle resources;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
	}

	/**
	 * Scene 01 - Introduction<br>
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleIntroductionPage(ActionEvent event) throws IOException {
		Stage stage = getStageFromEvent(event);
		handleIntroductionPage(stage, resources);
	}

	public void handleIntroductionPage(Stage stage, ResourceBundle bundle) throws IOException {
		new IntroductionAction(stage).setIconFileName("/images/CD.png").setTitle(bundle.getString("intro-title"))
				.setCssFileName("/styles/styles.css").setFxmlFile("/fxml/01.introduction.fxml")
				.setProperty("properties/messages").process();
	}

	/**
	 * Scene 02 - Read User Input on Disc Size and Number of Discs<br>
	 * Create the stage for letting the user select the total number of discs
	 * and the size of each disc.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void handleReadUserInput(ActionEvent event) throws IOException {
		Stage stage = getStageFromEvent(event);
		new IntroductionAction(stage).setIconFileName("/images/CD.png")
				.setTitle(resources.getString("select-disc-type")).setCssFileName("/styles/styles.css")
				.setFxmlFile("/fxml/02.readUserInput.fxml").setProperty("properties/messages").process();

		Slider slider = (Slider) stage.getScene().lookup("#sizeSlider");
		TextField discSize = (TextField) stage.getScene().lookup("#discSize");
		Label mbView = (Label) stage.getScene().lookup("#mbView");
		Label gbView = (Label) stage.getScene().lookup("#gbView");

		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Integer iValue = newValue.intValue();
				discSize.setText(NumberFormat.getIntegerInstance().format(iValue));
				mbView.setText(getMb(newValue));
				gbView.setText(getGb(newValue));
			}
		});

		discSize.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null || newValue.length() <= 0) {
					return;
				}
				newValue = newValue.replace(",", "");
				mbView.setText(getMb(newValue));
				gbView.setText(getGb(newValue));
				discSize.setText(NumberFormat.getIntegerInstance().format(Long.valueOf(newValue)));
			}
		});
	}

	/**
	 * Scene 02 helper - Change the Slider<br>
	 * When the user selects the Disc Type, we want to change the slider
	 * accordingly to match the sizes of the disc type. That is, CDs are only
	 * around 800mb, whereas DVDs are around 8gb.
	 * 
	 * @param event
	 */
	@FXML
	public void onDiscTypeChange(ActionEvent event) {
		Stage stage = getStageFromEvent(event);
		Slider slider = (Slider) stage.getScene().lookup("#sizeSlider");

		switch ((String) discType.getValue()) {
		case "CD":
			slider.setMax(1000000);
			slider.setMajorTickUnit(100000);
			slider.setMinorTickCount(0);
			break;
		case "DVD":
			slider.setMax(100000000);
			slider.setMajorTickUnit(10000000);
			slider.setMinorTickCount(0);
			break;
		case "Blu-Ray":
			slider.setMax(1000000000);
			slider.setMajorTickUnit(100000000);
			slider.setMinorTickCount(0);
			break;
		}
		event.consume();
	}

	/**
	 * Scene 03 - Inform User of Selecting Root Folder The user completed scene
	 * 2, selected the number of discs and the size of each disc. Now we need to
	 * present a new page just letting them know that the next page is going to
	 * show them a directory. They will pick one directory that will have
	 * several sub-directories.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void onProcessUserInput(ActionEvent event) throws IOException {
		Stage stage = getStageFromEvent(event);
		Map<String, Object> userData = new HashMap<String, Object>();
		userData.put("numOfDiscs", numOfDiscs.getText());
		userData.put("discSize", discSize.getText());
		stage.setUserData(userData);
		new IntroductionAction(stage).setIconFileName("/images/CD.png").setTitle(resources.getString("info-title"))
				.setCssFileName("/styles/styles.css").setFxmlFile("/fxml/03.infoSelectDirectory.fxml")
				.setProperty("properties/messages").process();
	}

	/**
	 * Scene 04 - Select a Root Folder<br>
	 * The user has selected the number of discs and the size of each disc. Now
	 * we need to present a new page just letting them know that the next page
	 * is going to show them a directory. They will pick one directory that will
	 * have several sub-directories.
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void onProcessRootFolder(ActionEvent event) throws IOException, ParseException {
		Stage stage = getStageFromEvent(event);
		Map<String, Object> userData = (Map<String, Object>) stage.getUserData();
		new IntroductionAction(stage).setIconFileName("/images/CD.png").setTitle(resources.getString("info-title"))
				.setCssFileName("/styles/styles.css").setFxmlFile("/fxml/04.progress.fxml")
				.setProperty("properties/messages").process();

		DirectoryListingAction dla = new DirectoryListingAction();
		dla.setStage(stage).setInclude(DOT.Include).process();

		OpticalDrivePlannerSolution opticalDrivePlannerSolution = new PrepareOpticalForPlanner(dla.getFileList(),
				userData).process();
		OpticalOptimalSolver solution = new OpticalOptimalSolver(opticalDrivePlannerSolution);
		solution.process();
		Map<OpticalDriveBucket, List<FolderPlanningEntity>> result = solution.getByBuckets();
		createFinalPage(stage, result, solution, userData);
	}

	/**
	 * Scene 05 - Display the Results<b> This is called by scene 4 that once all
	 * of the input is received and the process is complete will print the
	 * results.
	 * 
	 * @param stage
	 * @param results
	 * @param solution
	 * @param userData
	 * @throws IOException
	 */
	private void createFinalPage(Stage stage, Map<OpticalDriveBucket, List<FolderPlanningEntity>> results,
			OpticalOptimalSolver solution, Map<String, Object> userData) throws IOException {
		new IntroductionAction(stage).setIconFileName("/images/CD.png").setTitle(resources.getString("info-title"))
				.setCssFileName("/styles/styles.css").setFxmlFile("/fxml/05.results.fxml")
				.setProperty("properties/messages").process();
		Scene scene = stage.getScene();
		ScrollPane scrollPane = (ScrollPane) scene.getRoot();

		VBox mainBox = (VBox) scrollPane.getContent();
		mainBox.setPrefWidth(scrollPane.getWidth() - 20);
		mainBox.prefWidthProperty().bind(Bindings.max(2, scrollPane.widthProperty()).subtract(20));

		feasibility(solution, mainBox.getChildren());
		topLevelLabel(mainBox.getChildren(), userData, results.size());
		for (OpticalDriveBucket disc : results.keySet()) {
			VBox discBox = new VBox();
			TableView<Folder> table = createFolder(scrollPane.getWidth());
			discBox.getChildren().add(table);
			table.setItems(getData(results.get(disc)));
			table.prefHeightProperty().bind(Bindings.max(2, Bindings.size(table.getItems())).multiply(30));
			TitledPane tp = createTitledPane(disc.getId() + 1, getSizeOfDisc(results.get(disc)), scrollPane.getWidth());
			tp.setContent(table);
			tp.prefHeightProperty().bind(table.heightProperty());
			mainBox.getChildren().add(tp);
		}
		stage.show();
	}

	/**
	 * Create the titled pane which is a scrollable interface where we can print
	 * as many dics and folders inside it.
	 * 
	 * @param id
	 * @param size
	 * @param width
	 * @return
	 */
	private TitledPane createTitledPane(int id, long size, double width) {
		TitledPane tp = new TitledPane();
		tp.setText(getString("pane-title", (id), NumberFormat.getIntegerInstance().format(size), getMb(size),
				getGb(size)));
		tp.setPrefWidth(width);
		tp.setAnimated(true);
		tp.setExpanded(false);

		return tp;
	}

	@SuppressWarnings("unchecked")
	private TableView<Folder> createFolder(double width) {
		TableView<Folder> table = new TableView<Folder>();
		table.setEditable(false);
		table.setPrefWidth(width);
		TableColumn<Folder, String> colName = new TableColumn<Folder, String>(resources.getString("folder-name"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Folder, String> colSize = new TableColumn<Folder, String>(resources.getString("folder-size"));
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		table.getColumns().addAll(colName, colSize);

		table.minHeightProperty().bind(table.prefHeightProperty());
		table.maxHeightProperty().bind(table.prefHeightProperty());

		colName.prefWidthProperty().bind(table.widthProperty().multiply(0.75));
		colSize.prefWidthProperty().bind(table.widthProperty().multiply(0.25));

		return table;
	}

	private ObservableList<Folder> getData(List<FolderPlanningEntity> folders) {
		ObservableList<Folder> data = FXCollections.observableArrayList();
		for (FolderPlanningEntity folder : folders) {
			data.add(new Folder(folder.getName(), folder.getSize()));
		}
		return data;
	}

	/**
	 * Add a label if the solution is not feasible. That is, there is no
	 * solution given the number of discs and the total size of the directories.
	 * 
	 * @param solution
	 * @param node
	 */
	private void feasibility(OpticalOptimalSolver solution, ObservableList<Node> node) {
		if (!solution.isFeasible()) {
			TextArea textArea = new TextArea();
			textArea.getStyleClass().add("not-feasible");
			textArea.setText(resources.getString("not-feasible"));
			textArea.setWrapText(true);
			textArea.setEditable(false);
			textArea.autosize();
			textArea.setMinHeight(80);
			node.add(textArea);
		}
	}

	private void topLevelLabel(ObservableList<Node> node, Map<String, Object> userData, int totalDiscs) {
		Label topLabel = new Label();
		topLabel.getStyleClass().add("center");
		topLabel.setText(getString("top-label-1", userData.get("numOfDiscs"), userData.get("discSize")));
		topLabel.setMaxWidth(Double.MAX_VALUE);
		topLabel.setAlignment(Pos.CENTER);
		node.add(topLabel);
		Label line2Label = new Label();
		line2Label.getStyleClass().add("center");
		line2Label.setText(getString("top-label-2", totalDiscs));
		line2Label.setMaxWidth(Double.MAX_VALUE);
		line2Label.setAlignment(Pos.CENTER);
		node.add(line2Label);
	}

	/**
	 * Iterate over the folders in this list and calculate the total size.
	 * 
	 * @param folders
	 * @return
	 */
	private long getSizeOfDisc(List<FolderPlanningEntity> folders) {
		long size = 0;
		for (FolderPlanningEntity folder : folders) {
			size += folder.getSize();
		}
		return size;
	}

	private Stage getStageFromEvent(ActionEvent event) {
		Node source = (Node) event.getSource();
		return (Stage) source.getScene().getWindow();

	}

	private String getGb(Number num) {
		return getGb(num.doubleValue());
	}

	private String getGb(String num) {
		return getGb(Double.valueOf(num));
	}

	private String getGb(double val) {
		return new DecimalFormat("#.###").format(Double.valueOf(val) / 1073741824) + resources.getString("gb");
	}

	private String getMb(Number num) {
		return getGb(num.doubleValue());
	}

	private String getMb(String num) {
		return getMb(Double.valueOf(num));
	}

	private String getMb(double val) {
		return new DecimalFormat("#.###").format(Double.valueOf(val) / 1024) + resources.getString("mb");
	}

	private String getString(String key, Object... params) {
		return MessageFormat.format(resources.getString(key), params);
	}

}
