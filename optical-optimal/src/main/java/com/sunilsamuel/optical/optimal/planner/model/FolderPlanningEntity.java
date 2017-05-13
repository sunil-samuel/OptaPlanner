package com.sunilsamuel.optical.optimal.planner.model;

import java.io.File;
import java.io.Serializable;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class FolderPlanningEntity implements Serializable {
	private static final long serialVersionUID = -8632291625593860853L;

	private String name;
	private long size;
	private File file;

	@PlanningVariable(valueRangeProviderRefs = { "bucketRange" })
	private OpticalDriveBucket opticalDriveBucket;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the opticalDriveBucket
	 */
	public OpticalDriveBucket getOpticalDriveBucket() {
		return opticalDriveBucket;
	}

	/**
	 * @param opticalDriveBucket
	 *            the opticalDriveBucket to set
	 */
	public void setOpticalDriveBucket(OpticalDriveBucket opticalDriveBucket) {
		this.opticalDriveBucket = opticalDriveBucket;
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
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((opticalDriveBucket == null) ? 0 : opticalDriveBucket.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
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
		FolderPlanningEntity other = (FolderPlanningEntity) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (opticalDriveBucket == null) {
			if (other.opticalDriveBucket != null)
				return false;
		} else if (!opticalDriveBucket.equals(other.opticalDriveBucket))
			return false;
		if (size != other.size)
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
		builder.append("FolderPlanningEntity [name=").append(name).append(", size=").append(size).append(", file=")
				.append(file).append(", opticalDriveBucket=").append(opticalDriveBucket).append("]");
		return builder.toString();
	}
}
