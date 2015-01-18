package cn.edu.ustb.sem.schedule.web.model;

import java.util.List;

public class DataTable {
	public List<DataColumn> datacolumn;
	
	public static class DataColumn{
		public String headertext;
		public String headerfontsize = "16";
		public String fontsize = "12";
		public List<Text> text;
	}
	
	public static class Text{
		public String label;
	}
}
