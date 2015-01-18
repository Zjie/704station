package cn.edu.ustb.sem.core.exception;

public class DaoException extends Exception {
	private static final long serialVersionUID = 4709995942105394026L;
	private String msg;
	public DaoException(String msg) {
		super(msg);
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
