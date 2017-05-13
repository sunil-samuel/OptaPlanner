/**
 * DirectoryListingAction.java 
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DirectoryListingAction {
	public enum DOT {
		Include, Exclude
	}

	private Stage stage;
	private File rootDir;
	private Map<File, Long> directories;
	private Map<File, Long> dotDirectories;
	DOT dot = DOT.Exclude;

	public DirectoryListingAction() {

	}

	public DirectoryListingAction(Stage stage) {
		this.stage = stage;
	}

	public DirectoryListingAction setInclude(DOT dot) {
		this.dot = dot;
		return this;
	}

	public DirectoryListingAction setStage(Stage stage) {
		this.stage = stage;
		return this;
	}

	public void process() throws IOException {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Open Resource File");
		rootDir = dirChooser.showDialog(stage);
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(rootDir.listFiles()));
		directories = new TreeMap<File, Long>();
		dotDirectories = new TreeMap<File, Long>();
		for (File file : files) {
			long size = Files.walk(file.toPath()).mapToLong(p -> p.toFile().length()).sum();
			if (file.getName().startsWith(".")) {
				dotDirectories.put(file, size);
				if (dot == DOT.Include) {
					directories.put(file, size);
				}
			} else {
				directories.put(file, size);
			}
		}
		stage.close();
	}

	public Map<File, Long> getFileList() {
		return this.directories;
	}
}
