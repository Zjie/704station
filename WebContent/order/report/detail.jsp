<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../../resources/js/common.js"></script>
</head>
<body style="padding:10px">
	<div id="basicInfo"></div>
	<div id="bom" style="padding: 10 0 10 0"></div>
	<div id="paPanel" style="padding: 10 0 10 0"></div>
	<div id="ptPanel" style="padding: 10 0 10 0"></div>
	<div id="pdPanel" style="padding: 10 0 10 0"></div>
	<div id="poPanel" style="padding: 10 0 10 0"></div>
	<div id="button">
		<button type="button" onclick="history.back()">返回</button>
	</div>
<script type="text/javascript">
var orderPanel;
Ext.onReady(function() {
	orderPanel = new Ext.form.FormPanel({
		renderTo: 'basicInfo',
		border: 1,
		//id: 'orderPanel',
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
	    title: '工序报工总计',
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
	    	text: "计划开始时间",
	    	dataIndex: 'beginDate',
	    	width: 130
	    },{
	    	text: "计划结束时间",
	    	dataIndex: 'endDate',
	    	width: 130
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
	    }]
	});
	//装配报工详情
	var paStore = new Ext.data.Store({
		autoLoad: true,
	    model: 'process',
	    id: 'produceAssembleStore',
	    data: [],
	});
	var paGridPanel = Ext.create('Ext.grid.Panel', {
	    //margin: '0 0 10 0',
	    disableSelection: false,
	    title: '工序报工详情',
	    loadMask: true,
	    id: 'paGridPanel',
	    store: paStore,
	    renderTo: 'paPanel',
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
	        text: "已报工耗时",
	        dataIndex: 'reportTime',
	        //sortable: false
	    },{
	    	text: "上报时间",
	    	dataIndex: 'udate',
	    },{
	    	text: "上报工人",
	    	dataIndex: 'updater',
	    }]
	});
	//试验报工详情
	var ptStore = new Ext.data.Store({
		autoLoad: true,
	    model: 'produceTest',
	    id: 'produceTestsStore',
	    data: []
	});
	var ptPanel = Ext.create('Ext.grid.Panel', {
	    margin: '0 0 10 0',
	    disableSelection: false,
	    title: '试验报工详情',
	    loadMask: true,
	    id: 'ptPanel',
	    store: ptStore,
	    renderTo: 'ptPanel',
	    columns:[{
	        hidden: true,
	        text: "试验报工id",
	        dataIndex: 'id',
	        //sortable: false
	    },{
	    	text: '是否完成',
	    	dataIndex: 'statusName',
	    },{
	        text: "试验内容",
	        dataIndex: 'content',
	        //sortable: false
	    },{
	        text: "试验个数",
	        dataIndex: 'num',
	        //sortable: false
	    },{
	    	text: "上报日期",
	    	dataIndex : 'udate',
	    	//sortable: false
	    },{
	        text: "确认日期",
	        dataIndex: 'confirmDate',
	        //sortable: false
	    },{
	    	test: '上报工人',
	    	dataIndex: 'worker',
	    }]
	});
	//典试报工详情
	var pdStore = new Ext.data.Store({
		autoLoad: true,
	    model: 'produceDianshi',
	    id: 'produceDianshiStore',
	    data: [],
	});
	var pdPanel = Ext.create('Ext.grid.Panel', {
	    margin: '0 0 10 0',
	    disableSelection: false,
	    loadMask: true,
	    id: 'pdPanel',
	    store: pdStore,
	    title: '典试报工详情',
	    renderTo: 'pdPanel',
	    columns:[{
	        hidden: true,
	        text: "试验报工id",
	        dataIndex: 'id',
	        //sortable: false
	    },{
            text: "是否完成",
            dataIndex: 'statusName',
        },{
        	text: '典试内容',
        	dataIndex: 'content',
        },{
	        text: "典试个数",
	        dataIndex: 'num',
	        //sortable: false
	    },{
	    	text: "上报日期",
	    	dataIndex : 'udate',
	    	//sortable: false
	    },{
	        text: "确认日期",
	        dataIndex: 'confirmDate',
	        //sortable: false
	    },{
	    	text: "上报工人",
	    	dataIndex: 'worker',
	    }]
	});
	//其他报工
	var poStore = new Ext.data.Store({
		autoLoad: true,
	    model: 'produceOther',
	    id: 'produceOtherStore',
	    data: [],
	});
	var poPanel = Ext.create('Ext.grid.Panel', {
	    margin: '0 0 10 0',
	    title: '质技报工详情',
	    disableSelection: false,
	    loadMask: true,
	    id: 'poPanel',
	    store: poStore,
	    renderTo: 'poPanel',
	    columns:[{
	        hidden: true,
	        text: "试验报工id",
	        dataIndex: 'id',
	        //sortable: false
	    },{
	        text: "上报内容",
	        dataIndex: 'content',
	        //sortable: false
	    },{
	    	text: "上报日期",
	    	dataIndex : 'udate',
	    	//sortable: false
	    },{
	    	text: "上报工人",
	    	dataIndex: "worker"
	    }]
	});
	initial();
});

//加载初始数据
function initial() {
	var order = ${order};
	var ptStore = Ext.getStore('processStore');
	//装配报工汇总信息
	ptStore.add(order.orderProcesses);
	var form = orderPanel.getForm();
	form.findField('no').setValue(order.no);
	form.findField('process').setValue(order.process);
	form.findField('model').setValue(order.model);
	form.findField('project').setValue(order.project);
	form.findField('productName').setValue(order.productName);
	form.findField('productCode').setValue(order.productCode);
	form.findField('productBatchId').setValue(order.productBatchId);
	//装配报工，详细信息
	var produceAssembles = ${zhuangpei};
	var paStore = Ext.getStore('produceAssembleStore');
	paStore.add(produceAssembles);
	//试验报工详情
	var produceTests = ${shiyan};
	var ptStore = Ext.getStore('produceTestsStore');
	ptStore.add(produceTests);
	//典试报工详情
	var produceDianshi = ${dianshi};
	var pdStore = Ext.getStore('produceDianshiStore');
	pdStore.add(produceDianshi);
	//其他报工
	var produceOther = ${qita};
	var poStore = Ext.getStore('produceOtherStore');
	poStore.add(produceOther);
}
</script>
</body>
</html>
