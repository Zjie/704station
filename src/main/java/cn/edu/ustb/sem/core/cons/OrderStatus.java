package cn.edu.ustb.sem.core.cons;

public enum OrderStatus {

	INITIAL("初始状态",(byte)0),
	ASSIGNED("已齐套",(byte)1),
	FORCESCHEDULE("强制排产",(byte)2),
	SCHEDULED("已排产", (byte)3),
	WAIXIE("外协中", (byte)4),
	WAIXIE_WANBI("外协完毕", (byte)5);
	
	private String name;
	private Byte index;
	
	private OrderStatus(String name, Byte index){
		this.name = name;
		this.index = index;
	}

	public static String getName(Byte index){
		for(OrderStatus i : OrderStatus.values()){
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

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
	
	
}
