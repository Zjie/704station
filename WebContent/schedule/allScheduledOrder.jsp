<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>已排产订单</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../FusionWidgets/Charts/FusionCharts.js"></script>
    <script type="text/javascript" src="../resources/js/jquery-2.0.3.js"></script>
    <script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
</head>
<body>
    <div id="selectedDate"></div>
    <div id="chartContainer"></div>
<script type="text/javascript">
    var myChart = new FusionCharts("../FusionWidgets/Charts/Gantt.swf", "已排产订单", "100%", "100%", "0", "1");
    var result = '${scheduleResult}';
    myChart.setJSONData(JSON.parse(result));
    myChart.render("chartContainer");
    Ext.onReady(function(){
        var tinggong = new Ext.form.FormPanel(
        {
            renderTo: 'selectedDate',
            id : 'selectedDate',
            border: 0,
            items : [{
                layout: 'form',
                items: [{
                    layout: 'column',
                    border: 0,
                    items: [{
                        xtype: 'textfield',
                        labelAlign: "right",
                        fieldLabel: '开始时间',
                        allowBlank: false ,
                        msgTarget: 'under',
                        blankText: '该字段不能为空',
                        name: 'begin',
                        id: 'begin',
                        listeners: {
                            render: function(p) {
                                p.getEl().on('click', function() {
                                    WdatePicker({
                                        el: 'begin-inputEl',
                                        dateFmt: 'yyyy-MM-dd'
                                    });

                                });
                            }
                        }
                    },{
                        xtype: 'textfield',
                        labelAlign: "right",
                        fieldLabel: '结束时间',
                        allowBlank: false ,
                        msgTarget: 'under',
                        blankText: '该字段不能为空',
                        name: 'end',
                        id: 'end',
                        listeners: {
                            render: function(p) {
                                p.getEl().on('click', function() {
                                    WdatePicker({
                                        el: 'end-inputEl',
                                        dateFmt: 'yyyy-MM-dd'
                                    });

                                });
                            }
                        }
                    }]
                }]
            }],
            buttons : [{
                text : '查询',
                handler : function() {
                    var values = this.up('form').getForm().getValues();
                    window.location.href = '/schedule/allScheduledOrder.do?dateBegin=' + values['begin'] + '&dateEnd=' + values['end'];
                }
            }]
        });
    })
</script>
</body>
</html>
