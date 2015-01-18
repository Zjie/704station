package cn.edu.ustb.sem.schedule.entity;

public class UnscheduleProcessGroupStatus {
	//0表示外协开始状态，1表示外协重启后的状态，2表示停工重启后的状态 3表示停工确认的状态
	public static final int INITIAL = 0;
	public static final int WAIEXIE_RESTART = 1;
	public static final int TINGGONG_RESTART = 2;
	public static final int TINGGONG_BEGIN = 3;
}
