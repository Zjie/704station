package cn.edu.ustb.sem.schedule.web.model;

import java.util.List;

public class Processes {
	public String headertext;
	public String headerfontsize = "16";
	public String fontsize = "12";
	public List<Process> process;
	public static class Process {
		public String label;
		public String id;
	}
}
