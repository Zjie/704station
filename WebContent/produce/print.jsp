<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
    <script type="text/javascript" src="../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../resources/js/common.js"></script>
</head>
<style type="text/css">
    body {text-align:center;}
    #basicInfo {width: 900px;margin: auto;}
    #basicInfo table {margin: auto; width: 900px;}
    #basicInfo table td {height:25px;line-height: 25px;}
    .noprint{display : none }
</style>
<body>
    <h1>温度传感器车间任务派工单</h1>
    <div id="basicInfo">
        <table>
            <tr>
                <td>订单编号：${order.no}</td>
                <td>产品代号：${order.productCode}</td>
                <td>
                    <!-- 用订单编号去确定一个订单，而不是订单id -->
                	<img src="genCode.do?code=${order.no}-${worker.id}-${order.productCode}">
                </td>
            </tr>
            <tr>
                <td>课题编号：${order.project}</td>
                <td>主管工艺：${order.process}</td>
                <td>产品名称：${order.productName}</td>
            </tr>
            <tr>
                <td>生产批次：${order.productBatchId}</td>
                <td>计划开始时间：${order.onlineDate}</td>
                <td>计划完成时间：${order.finishDate}</td>
            </tr>
            <tr>
                <td>投产数量：${order.produceNum}个</td>
                <td>交付数量：${order.deliveryNum}个</td>
                <td>典试数量：${order.testNum}个</td>
            </tr>
        </table>
        <table border="1px" cellspacing="0px" style="margin-top:10px">
            <tr>
                <td>序号</td>
                <td>工作内容</td>
                <td>备注</td>
                <td>操作单元</td>
            </tr>
            <tr>
                <td>#</td>
                <td>工序组${drm.gup.processGroup}</td>
                <td>${drm.gup.remark}</td>
                <td>${drm.gup.unit}</td>
            </tr>
        </table>
        <div style="margin-top:20px">
            <div style="float:left">打印日期：${printDate}</div>
            <div style="float:left;margin-left:20px;">打印人：${worker.realName}</div>
        </div>
    </div>
<script>
$(function(){
    window.print();
	//printpreview();
})
//打印页面预览

function printpreview() {					
    var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
    //document.getElementById("divButton").style.display = "none"; //隐藏打印及其打印预览页面
    document.body.insertAdjacentHTML('beforeEnd', WebBrowser); 
    window.location.href = "#";
    //在body标签内加入html（WebBrowser activeX控件）
    PageSetup_Null();
    WebBrowser1.Orientation = 'Landscape';
    WebBrowser1.ExecWB(7, 1); //打印预览
}
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";    
 //设置网页打印的页眉页脚为空    
function PageSetup_Null()   
{    
	try {    
		var Wsh=new ActiveXObject("WScript.Shell");    
		HKEY_Key="header";    
		Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
		HKEY_Key="footer";    
		Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
	}  catch(e){}    
}    


</script>
</body>
</html>