package cn.edu.ustb.sem.core.cons;

public enum Colors {
	 
	CYAN("00FFFF", 1), 
	BLUE("0000FF", 2), 
	RED("FF0000", 3), 
	MAGENTA("FF00FF", 4), 
	DIMGRAV("666666", 5), 
	YELLOW("FFFF00", 6),
	BLACK("000000", 7), 
	LIGHTREY("CCCCCC", 8), 
	LIME("00FF00", 9), 
	INDIANRED("0033cc", 10);

	private String name;

	private int index;

	private Colors(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index){
		for(Colors i : Colors.values()){
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
