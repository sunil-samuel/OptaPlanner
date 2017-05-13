package com.sunilsamuel.optical.optimal.planner;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunilsamuel.optical.optimal.planner.model.FolderPlanningEntity;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDriveBucket;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDrivePlannerSolution;

public class PrepareOpticalForPlanner {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<File, Long> folders;
	private Integer numOfDiscs;
	private Long discSize;

	private OpticalDrivePlannerSolution opticalDrivePlannerSolution = new OpticalDrivePlannerSolution();

	public PrepareOpticalForPlanner() {

	}

	public PrepareOpticalForPlanner(Map<File, Long> folders, Integer numOfDiscs, Long discSize) {
		this.folders = folders;
		this.numOfDiscs = numOfDiscs;
		this.discSize = discSize;
	}

	public PrepareOpticalForPlanner(Map<File, Long> folders, Map<String, Object> discs) throws ParseException {
		this.folders = folders;
		try {
			numOfDiscs = NumberFormat.getNumberInstance(java.util.Locale.US).parse((String) discs.get("numOfDiscs"))
					.intValue();
		} catch (Exception e) {
			numOfDiscs = 100;
		}
		discSize = NumberFormat.getNumberInstance(java.util.Locale.US).parse((String) discs.get("discSize"))
				.longValue();

		logger.debug("Total number of discs [{}], total disc size [{}]", numOfDiscs, discSize);
	}

	public PrepareOpticalForPlanner setFolders(Map<File, Long> folders) {
		this.folders = folders;
		return this;
	}

	public PrepareOpticalForPlanner setNumOfDiscs(Integer numOfDiscs) {
		this.numOfDiscs = numOfDiscs;
		return this;
	}

	public PrepareOpticalForPlanner setDiscSize(Long discSize) {
		this.discSize = discSize;
		return this;
	}

	public OpticalDrivePlannerSolution process() {
		logger.debug("Starting to prepare optical optimal");
		createPlanningEntities();
		createPlanningBuckets();
		return opticalDrivePlannerSolution;
	}

	private void createPlanningEntities() {
		List<FolderPlanningEntity> folderPlanningEntities = new ArrayList<FolderPlanningEntity>();

		for (File file : folders.keySet()) {
			FolderPlanningEntity folderPlanningEntity = new FolderPlanningEntity();
			folderPlanningEntity.setFile(file);
			folderPlanningEntity.setName(file.getName());
			folderPlanningEntity.setSize(folders.get(file));
			folderPlanningEntities.add(folderPlanningEntity);
			logger.debug("Adding file [{}] with size [{}]", file.getName(), folders.get(file));
		}
		opticalDrivePlannerSolution.setFolderPlanningEntities(folderPlanningEntities);
	}

	private void createPlanningBuckets() {
		List<OpticalDriveBucket> opticalDriveBuckets = new ArrayList<OpticalDriveBucket>();
		logger.debug("Creating a total of [{}] buckets.", numOfDiscs);
		for (int count = 0; count < numOfDiscs; count++) {
			OpticalDriveBucket opticalDriveBucket = new OpticalDriveBucket();
			opticalDriveBucket.setId(count);
			opticalDriveBucket.setSize(discSize);
			opticalDriveBuckets.add(opticalDriveBucket);
			logger.debug("Adding bucket [{}] with size [{}]", count, discSize);
		}
		opticalDrivePlannerSolution.setOpticalDriveBuckets(opticalDriveBuckets);
	}
}
