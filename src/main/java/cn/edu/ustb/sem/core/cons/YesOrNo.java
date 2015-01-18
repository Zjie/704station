package cn.edu.ustb.sem.core.cons;

public enum YesOrNo {
	YES("是",(byte)1),NO("否",(byte)0);
	
	private String name;
	private Byte index;
	
	private YesOrNo(String name,Byte index){
		this.name = name;
		this.index = index;
	}

	public static String getName(Byte index){
		for(YesOrNo i : YesOrNo.values()){
			if(i.getIndex() == index){
				return i.getName();
			}
		}
		return NO.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
	
	
}
