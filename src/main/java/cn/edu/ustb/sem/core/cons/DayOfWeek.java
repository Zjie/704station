package cn.edu.ustb.sem.core.cons;

public enum DayOfWeek {

	MONDAY("星期一", 2),
	TUESDAY("星期二", 3),
	WEDNESDAY("星期三",4),
	THURSDAY("星期四",5),
	FRIDAY("星期五",6),
	SATURDAY("星期六",7),
	SUNDAY("星期日",1);
	
	private String name;
	private int index;
	
	private DayOfWeek(String name, int index){
		this.name = name;
		this.index = index;
	}
	
	public static String getName(int index){
		for(DayOfWeek i : DayOfWeek.values()){
			if(i.getIndex() == index){
				return i.getName();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
