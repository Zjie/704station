<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>排产结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../../FusionWidgets/Charts/FusionCharts.js"></script>
    <script type="text/javascript" src="../../resources/js/jquery-2.0.3.js"></script>
</head>
<body>
    <button type="button" onclick="confirm()">确定排产结果</button>
    <button type="button" onclick="cancel()">取消此次计划调整</button>
    <div id="chartContainer"></div>
    <script type="text/javascript">
        var myChart = new FusionCharts("../../FusionWidgets/Charts/Gantt.swf", "生成任务", "100%", "100%", "0", "1");
        var result = '${scheduleResult}';
        myChart.setJSONData(JSON.parse(result));
        myChart.render("chartContainer");
        function confirm() {
            $.ajax({
                type: "POST",
                url: "saveScheResult.do",
                success: function(response) {
                    if (response.success) {
                        Ext.Msg.alert('系统提示', '成功保存排产结果！请点击确认返回', function(buttonId, text, opt) {
                            if (buttonId == 'ok')
                                window.location.href = "list.html";
                        });
                    } else {
                        Ext.Msg.alert('系统提示', response.msg);
                    }
                }
            });
        }
        function cancel() {
            $.ajax({
                type: "POST",
                url: "cancel.do",
                success: function(response) {
                    if (response.success) {
                        Ext.Msg.alert('系统提示', '成功取消此次计划调整！请点击确认返回', function(buttonId, text, opt) {
                            if (buttonId == 'ok')
                                window.location.href = "list.html";
                        });
                    } else {
                        Ext.Msg.alert('系统提示', response.msg);
                    }
                }
            });
        }
    </script>
</body>
</html>
