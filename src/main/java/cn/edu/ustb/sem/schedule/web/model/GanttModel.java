package cn.edu.ustb.sem.schedule.web.model;

import java.util.List;

public class GanttModel {
	public static final String DATEFORMAT = "dd/mm/yyyy";
	public Chart chart;
	public List<Categories> categories;
	public Processes processes;
	public DataTable datatable;
	public Tasks tasks;
	public Connectors connectors;
	
	public static class Chart {
		public String palette = "3";
		public String caption;
		public String dateformat = DATEFORMAT;
		//甘特图的时长
		public Integer ganttPaneDuration = 14;
		//甘特图的时长单位 d-day m-month
		public String ganttPaneDurationUnit = "d";
		public String outputdateformat = "mm月dd号";
	}
	public static class Category {
		public String start;
		public String end;
		public String label;
	}
	public static class Categories {
		public List<Category> category;
	}
	public static class Connetor {
		public String fromtaskid;
		public String totaskid;
		public String thickness;
	}
	public static class Connectors {
		public List<Connetor> connetor;
	}
}
