package com.starland.xyqp.gmback.vo;

import java.util.List;

public class MultipleChoice {

	private Integer id;
	
	private List<Option> options;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public static class Option {
		
		private Integer id;
		
		private String name;
		
		private boolean selected;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
	}
	
}
