package cn.edu.ustb.sem.order.entity;

public class OrderStatus {
	//订单状态 0：初始化的订单 1：物料齐套 2：强制排产  3：已排产 4：外协中 5：外协完毕
	public static final int initial = 0;
	public static final int ready = 1;
	public static final int forced = 2;
	public static final int scheduled = 3;
	public static final int waixie = 4;
	public static final int waiexieEnd = 5;
}
