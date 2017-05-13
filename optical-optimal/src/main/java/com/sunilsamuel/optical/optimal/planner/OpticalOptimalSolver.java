/**
 * OpticalOptimalSolver.java 
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
package com.sunilsamuel.optical.optimal.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;

import com.sunilsamuel.optical.optimal.planner.model.FolderPlanningEntity;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDriveBucket;
import com.sunilsamuel.optical.optimal.planner.model.OpticalDrivePlannerSolution;

public class OpticalOptimalSolver {
	private final String config = "optaplanner/config/OpticalOptimalPlannerConfig.xml";
	private OpticalDrivePlannerSolution opticalDrivePlannerSolution;
	private OpticalDrivePlannerSolution solution;
	private Solver<OpticalDrivePlannerSolution> solver;
	private SolverFactory<OpticalDrivePlannerSolution> solverFactory;

	public OpticalOptimalSolver(OpticalDrivePlannerSolution opticalDrivePlannerSolution) {
		this.opticalDrivePlannerSolution = opticalDrivePlannerSolution;
	}

	public OpticalDrivePlannerSolution process() {
		solverFactory = SolverFactory.createFromXmlResource(config);
		solver = solverFactory.buildSolver();
		solution = solver.solve(opticalDrivePlannerSolution);
		return solution;
	}

	public Map<OpticalDriveBucket, List<FolderPlanningEntity>> getByBuckets() {
		List<OpticalDriveBucket> discs = solution.getOpticalDriveBuckets();
		List<FolderPlanningEntity> folders = solution.getFolderPlanningEntities();
		Map<OpticalDriveBucket, List<FolderPlanningEntity>> allocation = new TreeMap<OpticalDriveBucket, List<FolderPlanningEntity>>();
		for (OpticalDriveBucket disc : discs) {
			List<FolderPlanningEntity> foldersAllocated = null;
			for (FolderPlanningEntity folder : folders) {
				if (folder.getOpticalDriveBucket().getId() == disc.getId()) {
					if (foldersAllocated == null) {
						foldersAllocated = new ArrayList<FolderPlanningEntity>();
					}
					foldersAllocated.add(folder);
				}
			}
			if (foldersAllocated != null) {
				allocation.put(disc, foldersAllocated);
			}
		}
		return allocation;
	}

	public boolean isFeasible() {
		return solution.getScore().isFeasible();
	}

	public void explainScore() {
		ScoreDirectorFactory scoreDirectorFactory = solver.getScoreDirectorFactory();
		ScoreDirector guiScoreDirector = scoreDirectorFactory.buildScoreDirector();

		guiScoreDirector.setWorkingSolution(solution);
		for (ConstraintMatchTotal constraintMatchTotal : guiScoreDirector.getConstraintMatchTotals()) {
			// scoreLevel 0 = hard
			// scoreLevel 1 = soft
			if (constraintMatchTotal.getScoreLevel() != 0) {
				continue;
			}
			String constraintName = constraintMatchTotal.getConstraintName();
			Number weightTotal = constraintMatchTotal.getWeightTotalAsNumber();
			System.out.println(weightTotal + " for " + constraintName);
			for (ConstraintMatch constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
				List<Object> justificationList = constraintMatch.getJustificationList();
				Number weight = constraintMatch.getWeightAsNumber();
				int scoreLevel = constraintMatch.getScoreLevel();
				System.out
						.println("   " + weight + " for score level " + scoreLevel + " caused by " + justificationList);
			}
		}
		guiScoreDirector.dispose();
	}
}
