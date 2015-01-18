package cn.edu.ustb.sem.core.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -880015127896012545L;
	private String msg;
	public ServiceException(String msg) {
		super(msg);
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
