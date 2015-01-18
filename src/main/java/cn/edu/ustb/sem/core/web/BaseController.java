package cn.edu.ustb.sem.core.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.edu.ustb.sem.core.auth.bo.Visitor;

import com.alibaba.fastjson.JSON;

public class BaseController {
	protected final Log logger = LogFactory.getLog(getClass());
	//分页相关的属性
	//分页起始
	protected int pBegin = 0;
	//分页大小
	protected int pSize = 20;
	
	//ajax状态码
	//出错时的状态码
	protected static final int ERROR_STATE = 1;
	//正常时的状态吗
	protected static final int SUCCESS_STATE = 0;
	//用来放一些返回信息
	protected final Map<String, Object> result = new HashMap<String, Object>();
	protected static final String DATA = "data";
	
	/**
	 * 返回错误的json信息
	 * @param msg 错误信息
	 * @param addValue 如果没有附加的信息，输入null即可
	 * @return
	 */
	protected final String getErrorJsonMsg(String msg, Map<String, Object> addValue) {
		Map<String, Object> jsonMsg = new HashMap<String, Object>();
		if (addValue != null) {
			jsonMsg.putAll(addValue);
		}
		jsonMsg.put("msg", msg);
		jsonMsg.put("state", ERROR_STATE);
		return JSON.toJSONString(jsonMsg);
	}
	/**
	 * 返回正确的json信息
	 * @param msg 正确信息
	 * @param addValue 如果没有附加的信息，输入null即可
	 * @return
	 */
	protected final String getSuccessJsonMsg(String msg, Map<String, Object> addValue) {
		Map<String, Object> jsonMsg = new HashMap<String, Object>();
		if (addValue != null) {
			jsonMsg.putAll(addValue);
		}
		jsonMsg.put("msg", msg);
		jsonMsg.put("state", SUCCESS_STATE);
		jsonMsg.put("success", true);
		return JSON.toJSONString(jsonMsg);
	}
	
	protected final Map<String, Object> getSuccessJsonResult(String msg, Map<String, Object> addValue) {
		Map<String, Object> jsonMsg = new HashMap<String, Object>();
		if (addValue != null) {
			jsonMsg.putAll(addValue);
		}
		jsonMsg.put("msg", msg);
		jsonMsg.put("state", SUCCESS_STATE);
		jsonMsg.put("success", true);
		return jsonMsg;
	}
	
	protected final Map<String, Object> getErrorJsonResult(String msg, Map<String, Object> addValue) {
		Map<String, Object> jsonMsg = new HashMap<String, Object>();
		if (addValue != null) {
			jsonMsg.putAll(addValue);
		}
		jsonMsg.put("msg", msg);
		jsonMsg.put("state", ERROR_STATE);
		jsonMsg.put("success", false);
		return jsonMsg;
	}
	/**
	 * 当前的登录用户信息
	 * @return
	 */
	protected Visitor getVisitor() {
		return (Visitor) SecurityContextHolder.getContext().getAuthentication();
	}
}
