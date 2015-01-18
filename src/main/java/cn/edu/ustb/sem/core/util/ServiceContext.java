package cn.edu.ustb.sem.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceContext {
	private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	private static final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	public static void setResponse(HttpServletResponse re) {
		response.set(re);
	}
	public static HttpServletResponse getResponse() {
		return response.get();
	}
	public static void setRequest(HttpServletRequest req) {
		request.set(req);
	}
	public static HttpServletRequest getRequest() {
		return request.get();
	}
}
