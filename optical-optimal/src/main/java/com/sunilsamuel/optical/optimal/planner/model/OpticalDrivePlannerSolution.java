package com.sunilsamuel.optical.optimal.planner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@PlanningSolution
public class OpticalDrivePlannerSolution implements Serializable, Solution<HardSoftLongScore> {
	private static final long serialVersionUID = 6666456780154524612L;

	private HardSoftLongScore score;

	@PlanningEntityCollectionProperty
	private List<FolderPlanningEntity> folderPlanningEntities;

	@ValueRangeProvider(id = "bucketRange")
	List<OpticalDriveBucket> opticalDriveBuckets;

	@Override
	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(opticalDriveBuckets);
		return facts;
	}

	@Override
	public HardSoftLongScore getScore() {
		return score;
	}

	@Override
	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

	/**
	 * @return the folderPlanningEntities
	 */
	public List<FolderPlanningEntity> getFolderPlanningEntities() {
		return folderPlanningEntities;
	}

	/**
	 * @param folderPlanningEntities
	 *            the folderPlanningEntities to set
	 */
	public void setFolderPlanningEntities(List<FolderPlanningEntity> folderPlanningEntities) {
		this.folderPlanningEntities = folderPlanningEntities;
	}

	/**
	 * @return the opticalDriveBuckets
	 */
	public List<OpticalDriveBucket> getOpticalDriveBuckets() {
		return opticalDriveBuckets;
	}

	/**
	 * @param opticalDriveBuckets
	 *            the opticalDriveBuckets to set
	 */
	public void setOpticalDriveBuckets(List<OpticalDriveBucket> opticalDriveBuckets) {
		this.opticalDriveBuckets = opticalDriveBuckets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((folderPlanningEntities == null) ? 0 : folderPlanningEntities.hashCode());
		result = prime * result + ((opticalDriveBuckets == null) ? 0 : opticalDriveBuckets.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpticalDrivePlannerSolution other = (OpticalDrivePlannerSolution) obj;
		if (folderPlanningEntities == null) {
			if (other.folderPlanningEntities != null)
				return false;
		} else if (!folderPlanningEntities.equals(other.folderPlanningEntities))
			return false;
		if (opticalDriveBuckets == null) {
			if (other.opticalDriveBuckets != null)
				return false;
		} else if (!opticalDriveBuckets.equals(other.opticalDriveBuckets))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpticalDrivePlannerSolution [score=").append(score).append(", folderPlanningEntities=")
				.append(folderPlanningEntities).append(", opticalDriveBuckets=").append(opticalDriveBuckets)
				.append("]");
		return builder.toString();
	}
}
