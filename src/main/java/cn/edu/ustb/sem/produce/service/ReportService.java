package cn.edu.ustb.sem.produce.service;

import java.util.List;
import java.util.Map;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.kpi.web.model.WorkerKpiSearchForm;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.ProduceOtherSearchForm;
import cn.edu.ustb.sem.produce.web.model.ProduceAssembingModel;
import cn.edu.ustb.sem.produce.web.model.ProduceDianshiModel;
import cn.edu.ustb.sem.produce.web.model.ProduceOtherModel;
import cn.edu.ustb.sem.produce.web.model.ProduceReportForSaveModel;
import cn.edu.ustb.sem.produce.web.model.ProduceTestModel;
import cn.edu.ustb.sem.produce.web.model.ScheduledResultModel;
import cn.edu.ustb.sem.produce.web.model.TgSearchForm;
import cn.edu.ustb.sem.produce.web.model.TinggongModel;

public interface ReportService {
	public List<OrderModel> getCanReportOrder(Integer workerId) throws ServiceException;
	public void toReport(ProduceReportForSaveModel model) throws ServiceException;
	//通过id进行报工
	public Map<String, Object> produceReport(Integer id, boolean needAuth, Integer workerId) throws ServiceException;
	//查询一个员工的报工情况
	public Map<String, Object> produceReport(WorkerKpiSearchForm form) throws ServiceException;
	
	//通过条形码进行报工
	public Map<String, Object> produceReport(String code, boolean needAuth) throws ServiceException;
	//通过条形码获取订单的id
	public Integer getOrderIdByCode(String code) throws ServiceException;
	//报停工
	public void reportTinggong(TinggongModel model) throws ServiceException;
	//获取一个订单的排产好的工序组
	public List<ScheduledResultModel> findOrderSrs(String code) throws ServiceException;
	//获取一个工人的报工情况
	public GridModel<ProduceAssembingModel> getProduceReportByForm(WorkerKpiSearchForm form) throws ServiceException;
	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<TinggongModel> getTinggongReportOrders() throws ServiceException;
	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<TinggongModel> getTinggongCommitOrders() throws ServiceException;
	
	public GridModel<TinggongModel> getTinggongHistory(TgSearchForm form) throws ServiceException;
	//根据报工信息，查询一个订单完成的数量
	public OrderModel getFinishedNum(Integer orderId) throws ServiceException;
	//报完成装配的数量
	public void produceAssembledReport(Integer orderId, Integer num, Integer userId, String reportDate) throws ServiceException;
	
	//报在验个数
	public void reportTestingNum(Integer orderId, Integer num, String content, String reportDate) throws ServiceException;
	//获取一个订单的某个员工的全部实验报工情况
	public List<ProduceTestModel> getTestReportByWorker(Integer orderId) throws ServiceException;
	//确认一个实验报工
	public void confirmProduceTest(Integer id) throws ServiceException;
	
	
	//报在典数量
	public void reportDianshiingNum(Integer orderId, Integer num, String content, String reportDate) throws ServiceException;
	//获取一个订单某个工人全部的典试报工情况
	public List<ProduceDianshiModel> getDianshiReportByWorker(Integer orderId) throws ServiceException;
	//确认一个典试报工
	public void confirmProduceDianshi(Integer id) throws ServiceException;
	
	//其他报工
	public void reportOther(Integer orderId, String content, String reportDate) throws ServiceException;
	//查看该工人对该订单的其他报工
	public List<ProduceOtherModel> getOtherReportByWorker(Integer orderId) throws ServiceException;
	
	//查看一个时间段的全部报工情况，日期格式yyyy-mm-dd
	public Map<String, Object> getReportByDate(String begin, String end) throws ServiceException;
	
	//查看一个订单的报工情况
	public GridModel<OrderModel> getOrder(Integer id) throws ServiceException;
	
	public GridModel<ProduceOtherModel> listProduceOther(ProduceOtherSearchForm form) throws ServiceException;
}
