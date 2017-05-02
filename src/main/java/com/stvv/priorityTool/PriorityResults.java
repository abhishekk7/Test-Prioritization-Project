package com.stvv.priorityTool;

import java.util.ArrayList;
import java.util.List;

public class PriorityResults {
	private List<String> total;
	private List<String> additional;
	private String priority;

	public PriorityResults() {
		total = new ArrayList<String>();
		additional = new ArrayList<String>();
		priority = "none";
	}

	public List<String> getTotal() {
		return total;
	}

	public void setTotal(List<String> total) {
		this.total = total;
	}

	public List<String> getAdditional() {
		return additional;
	}

	public void setAdditional(List<String> additional) {
		this.additional = additional;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
