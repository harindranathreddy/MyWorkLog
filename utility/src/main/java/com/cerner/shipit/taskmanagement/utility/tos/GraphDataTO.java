package com.cerner.shipit.taskmanagement.utility.tos;

import java.util.List;

public class GraphDataTO {

	List<GraphDatasetTO> datasets;
	
	List<String> labels;

	public List<GraphDatasetTO> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<GraphDatasetTO> datasets) {
		this.datasets = datasets;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	
}
