<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../resources/js/common.js"></script>
    <script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
</head>
<body style="padding:10px">
	<div id="basicInfo"></div>
	<div id="bom" style="padding: 10 0 10 0"></div>
	<div>
		已报在制数量：<span id="zaizhiNum"></span><br />
		已报完成数量：<span id="aleardyFinishedNum"></span><br />
		今日装配完成数量：<input type='text' id="zhuangpeiNum" />
	</div>
	<div id="button">
		<button type="button" onclick="save()">保存</button>
		<button type="button" onclick="history.back()">取消</button>
	</div>
	<div id="addMaterial" style="width: 100%; position: absolute; top: 0px; z-index: 2;"></div>
<script type="text/javascript">
var orderPanel;
Ext.onReady(function() {
	orderPanel = new Ext.form.FormPanel({
		renderTo: 'basicInfo',
		border: 1,
		id: 'orderForm',
		items : [{
			layout: 'form',
			items: [
				{
					layout: 'column',
					border: 0,
					items: [{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '序号',
			            name: 'no'
					},{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '课题编号',
			            name: 'project'
					},{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '主管工艺',
			            name: 'process'
					},{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '使用型号',
			            name: 'model'
					}]
				},{
					layout: 'column',
					border: 0,
					items: [{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '产品名称',
			            name: 'productName'
					},{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '产品代号',
			            name: 'productCode'
					},{
						xtype: 'textfield',
						labelAlign: "right",
			            fieldLabel: '产品批次及编号',
			            name: 'productBatchId'
					}]
				},{
					layout: 'column',
					border: 0,
					items: [{
	                    xtype: 'textfield',
	                    labelAlign: "right",
	                    fieldLabel: '报工时间',
	                    name: 'reportDate',
	                    id: 'reportDate',
	                    listeners: {
	                        render: function(p) {
	                            p.getEl().on('click', function() {
	                                WdatePicker({
	                                    el: 'reportDate-inputEl',
	                                    dateFmt: 'yyyy-MM-dd'
	                                });

	                            });
	                        }
	                    }
	                }]
				}
			]
		}]
	});

	var store = new Ext.data.Store({
		autoLoad: true,
	    model: 'process',
	    id: 'processStore',
	    data: [],
	    groupField: 'phase'
	});
	var groupingFeature= Ext.create('Ext.grid.feature.Grouping',{
		id: 'groupingFeature',
        groupHeaderTpl: '{name} 共{rows.length}个'
    });
	var gridPanel = Ext.create('Ext.grid.Panel', {
	    //margin: '0 0 10 0',
	    disableSelection: false,
	    loadMask: true,
	    id: 'gridPanel',
	    features: [groupingFeature],
	    store: store,
	    renderTo: 'bom',
	    columns:[{
	        hidden: true,
	        text: "工序id",
	        dataIndex: 'id',
	        //sortable: false
	    },{
	    	hidden: true,
	        text: "生产阶段",
	        dataIndex: 'phase',
	        //sortable: false
	    },{
	    	text: "工序组",
	    	dataIndex : 'groupName',
	    	//sortable: false,
	    	width: 60
	    },{
	        text: "工作内容",
	        dataIndex: 'content',
	        //sortable: false
	    },{
	        text: "周期预估",
	        dataIndex: 'measure',
	        //sortable: false,
	        width:60
	    },{
	        text: "备注",
	        dataIndex: 'remark',
	        //sortable: false
	    },{
	    	text: "基数",
	    	dataIndex : 'base',
	    	//sortable: false,
	    	width: 60
	    },{
	    	text: "投产数量",
	    	dataIndex : 'reportNum',
	    	//sortable: false
	    },{
	        text: "已报工数量",
	        dataIndex: 'reportedNum',
	        //sortable: false
	    },{
	        text: "已报工耗时",
	        dataIndex: 'reportTime',
	        //sortable: false
	    },{
	    	text: '报工操作',
	    	xtype: "templatecolumn",
	    	width:130,
	    	tpl: "<input type='text' value='还可报工{leftNum}个' id='report{id}' style='width:120px; color:#999999' onfocus='initText(this)' onkeyup='check(this,{leftNum})' onblur='setText(this,{leftNum})'></input>"
	    },{
	    	text: '操作耗时/小时',
	    	xtype: "templatecolumn",
	    	width:110,
	    	tpl: "<input type='text' style='width:80px' id='time{id}'></input>"
	    }]
	});
	initial();
});

//加载初始数据
function initial() {
	var order = ${order};
	var ptStore = Ext.getStore('processStore');
	//将物料加入grid中
	ptStore.add(order.orderProcesses);
	var form = orderPanel.getForm();
	form.findField('no').setValue(order.no);
	form.findField('process').setValue(order.process);
	form.findField('model').setValue(order.model);
	form.findField('project').setValue(order.project);
	form.findField('productName').setValue(order.productName);
	form.findField('productCode').setValue(order.productCode);
	form.findField('productBatchId').setValue(order.productBatchId);
	$("#aleardyFinishedNum").text(order.assembledNum);
	$("#zaizhiNum").text(order.assemblingNum);
}

//保存报工
function save() {
	var params = {};
	var order = ${order};
	var orderId = order.id;//获取订单id
	//如果报完成数量，就不能报在制数量
	var finishedNum = $("#zhuangpeiNum").val();
	if (finishedNum != "") {
		//在制数量
		var zaizhiNum = order.assemblingNum;
		//已报完成数量
		var yibaoNum = order.assembledNum;
		if (zaizhiNum < finishedNum) {
			Ext.Msg.alert('系统提示', '您可报的装配完成数量不能超过已报在制数量');
			return;
		}
		params['orderId'] = orderId;
		params['num'] = finishedNum;
		var values = Ext.getCmp('orderForm').getValues();
		params['reportDate'] = values['reportDate'];
		$.post('assembledNumReport.do', params, function(response) {
			Ext.Msg.alert('系统提示', response.msg);
			if (response.success) {
				window.location.href = "report.html";
			}
		});
	} else {
		var ptStore = Ext.getStore('processStore');
		var items = ptStore.data.items;
		var j=-1;
		var totalReportNum = items.length * order.produceNum;//该订单所有工序数量总和
		var totalReport=0;//已排产工序总数与将要排产工序总数之和
		for(var i=0; i<items.length; i++){ 
			var record = items[i].data;
			totalReport += record.reportedNum;
			var temp = Ext.getDom('report'+record.id).value;
			var report;
			if(temp.indexOf("还")!=-1||temp==""||temp==null){
				continue;
			}else{
				j++;
				report = parseInt(temp);
				totalReport += report;//加上将要配套的物料总数
			}
			if(report > record.leftNum){
				Ext.Msg.alert("错误","排产数量超过可排产数量！");
				return;
			} 
			var hours = Ext.getDom('time'+record.id).value;
			if(hours == "" || hours == null){
				Ext.Msg.alert("错误","请填写操作耗时！");
				return;
			}else{
				params['produceReports[' + j + '].reportTime'] = parseInt(hours);
			}
			params['produceReports[' + j + '].opid'] = record.id;
			params['produceReports[' + j + '].reportNum'] = report;
			params['produceReports[' + j + '].reportedNum'] = record.reportedNum;
		}
		params['orderId'] = parseInt(orderId);
		params['totalReport'] = totalReport;
		params['totalReportNum'] = totalReportNum;
		var values = Ext.getCmp('orderForm').getValues();
		params['reportDate'] = values['reportDate'];
		if(j == -1){
			Ext.Msg.alert("提示","请填写报工信息！");
			return;
		}
		$.post('toReport.do', params, function(response) {
			var result = eval('('+response+')');
			if (result.success) {
	    		Ext.Msg.alert('系统提示', result.msg, function(buttonId, text, opt) {
	            	if (buttonId == 'ok') {
			            window.location.href = "report.html";
	            	}
	            });
	    	} else {
	    		Ext.Msg.alert('系统提示', result.msg);
	    	}
		});
	}
}
//点击文本框时，初始化文本值及样式
function initText(o){
	if(o.value.indexOf("还")!=-1){
		o.value="";
	}
	o.style.color="black";
}
//文本只能输入数字，且不能超过可配套数
function check(o,num){
	 o.value=o.value.replace(/[^\d]/g,"");
	 if(o.value>num){
	 	o.value="";
	 }
}
//光标离开文本框时
function setText(o,num){
	if(o.value==""){
		o.value="还可排产"+num+"个";
		o.style.color="#999999";
	}
}
//编辑订单模板
function editOrder(){
	var ptId = ${ptid};
	window.location.href="../process/template/edit.html?ptId="+ptId;
}
</script>
</body>
</html>
