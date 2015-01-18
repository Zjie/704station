<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>全部订单</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../resources/css/index.css" />
    <script type="text/javascript" src="../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../resources/js/common.js"></script>
</head>
<body>
    <div id="searchForm" style="height:70px;margin:10">
        <table>
            <tr>
                <td>订单编号：<input type="text" /></td>
                <td>课题编号：<input type="text" /></td>
            </tr>
        </table>
    </div>
    <div id="orderGrid" style="margin:10"></div>
<script type="text/javascript">
Ext.onReady(function(){
    var store = Ext.create('Ext.data.Store', {
        pageSize: 2,
        model: 'order',
        autoLoad: true,
        data : [
            {id: 1, taskId: 2, model: '型号1', productName: '产品1'},
            {id: 2, taskId: 2, model: '型号1', productName: '产品2'},
            {id: 3, taskId: 3, model: '型号2', productName: '产品3'},
            {id: 4, taskId: 5, model: '型号1', productName: '产品4'},
            {id: 5, taskId: 3, model: '型号4', productName: '产品5'}
        ]
    });

    var grid = Ext.create('Ext.grid.Panel', {
        width: document.body.clientWidth - 20,
        height: document.body.clientHeight - 100,
        title: '检索结果',
        store: store,
        disableSelection: false,
        loadMask: true,
        viewConfig: {
            id: 'orderVidewConfig',
            trackOver: false,
            stripeRows: false
        },
        // grid columns
        columns:[{
            text: "订单编号",
            dataIndex: 'id',
            //sortable: false
        },{
            text: "课题编号",
            dataIndex: 'taskId',
            //sortable: false
        },{
            text: "使用型号",
            dataIndex: 'model',
            //sortable: false
        },{
            text: "产品名称",
            dataIndex: 'productName',
            //sortable: false
        },{
            text: "操作",
            xtype: "templatecolumn",
            tpl: "<a href='addProcess.html?id={id}'>修改工序</a>"
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display"
        }),
        renderTo: "orderGrid"
    });

})
</script>
</body>
</html>
