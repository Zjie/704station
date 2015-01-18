package cn.edu.ustb.sem.schedule.web.model;

import java.util.List;

public class Tasks {
	public String showlabels = "1";
	public List<Task> task;
	public static class Task {
		public String label;
		public String processid;
		public String start;
		public String end;
		public String id;
		public String color;
		public String height;
		public String toppadding;
	}
}
