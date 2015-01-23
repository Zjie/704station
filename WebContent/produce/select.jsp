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
    <title>温度传感器车间任务派工单</title>
</head>
<style type="text/css">
body {text-align:center;}
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
                <td></td>
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
                <td>操作单元</td>
                <td>备注</td>
                <td>操作</td>
            </tr>
            <c:forEach var="disRes" items="${drm}" varStatus="status">
            <tr>
                <td>${status.index+1}</td>
                <td>工序组${disRes.gup.processGroup}</td>
                <td>${disRes.gup.unit}</td>
                <td>${disRes.gup.remark}</td>
                <td>
                    <input type="button" onclick="print('${disRes.id}')" value="打印">
                </td>
                <!--
                <td>
                    <c:if test="${disRes.status==1}">
                        <input type="button" onclick="print('${disRes.id}')" value="打印">
                    </c:if>
                    <c:if test="${disRes.status==3}">
                        已打印
                    </c:if>
                </td>
                -->
            </tr>
            </c:forEach>
        </table>
    </div>
<script>
function print(disId) {
    Ext.Msg.alert('系统提示', '您确认要打印该工序组吗？打印之后不可撤销！', function(buttonId, text, opt) {
        if (buttonId == 'ok'){
            window.location.href = 'print.do?srid=' + disId;
        }
    });
}
</script>
</body>
</html>