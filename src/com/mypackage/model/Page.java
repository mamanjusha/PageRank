package com.mypackage.model;

import java.util.List;

/**
 * 
 * Model class for the page
 * 
 * @author Manjusha
 *
 */
public final class Page {
	
	private String pageId;
	
	private double rank;
	
	private List<String> incomingPageIds;
	
	private List<String> outGoingPageIds;

	public Page(final String pageId) {
		this.pageId = pageId;
	}

	public String getPageId() {
		return pageId;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public List<String> getIncomingPageIds() {
		return incomingPageIds;
	}

	public void setIncomingPageIds(List<String> incomingPageIds) {
		this.incomingPageIds = incomingPageIds;
	}

	public List<String> getOutGoingPageIds() {
		return outGoingPageIds;
	}

	public void setOutGoingPageIds(List<String> outGoingPageIds) {
		this.outGoingPageIds = outGoingPageIds;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Page [pageId=" + pageId + ", rank=" + rank + ", incomingPageIds=" + incomingPageIds
				+ ", outGoingPageIds=" + outGoingPageIds + "]";
	}
	
	
}
