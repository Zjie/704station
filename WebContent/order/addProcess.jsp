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
    <div id="form" style="text-align: center; margin: 30px;"></div>
    <div id="processes" style="height:200px;margin: 10 0 0 20">
        <table id="processTable">
            <tr id="processAddButton">
                <td colspan="3">
                    <input type="button" onClick="addProcess()" value="添加新工序"/>
                </td>
            </tr>
        </table>
    </div>
<script type="text/javascript">

Ext.onReady(function() {
    var basicInfo = {
        title: '订单信息',
        width: '100%',
        height: 150,
        collapsible: true,
        layout: {
            type: 'table',
            columns: 4
        },
        defaults: {
            bodyStyle: 'padding:10px',
            border: 0,
            width: '140px'
        },
        items: [{
            html: '订单编号：3',
            colspan: 4
        },{
            html: '课题编号：3'
        },{
            html: '使用型号：型号2'
        },{
            html: '产品名称：产品3'
        }]
    };

    var processes = {
        id: 'processInfoPanel',
        title: '工序信息',
        width: '100%',
        height: '100%',
        collapsible: true,
        contentEl: 'processes'
    }
    // combine all that into one huge form
    var fp = Ext.create('Ext.FormPanel', {
        id: 'processAddPanel',
        title: '工序填写',
        frame: true,
        width: 600,
        renderTo:'form',
        bodyPadding: 10,
        items: [
            basicInfo,
            processes
        ],
        buttons: [{
            text: '保存',
            handler: function(){
               if(fp.getForm().isValid()){
                    Ext.Msg.alert('系统提示', '确定保存工序信息？');
                }
            }
        },{
            text: '重置',
            handler: function(){
                fp.getForm().reset();
            }
        },{
            text: '返回',
            handler: function(){
                history.back();
            }
        }]
    });

});

function deleteProcess(obj) {
    $(obj).remove();
    var oldHeight = Ext.ComponentManager.get("processInfoPanel").getHeight();
    Ext.ComponentManager.get("processInfoPanel").setHeight(oldHeight - 20);
}
function addProcess() {
    var newProcessId = Math.round(Math.random()*10+1);
    $("#processTable").prepend('' + 
        '<tr id="process' + newProcessId + '">' +
            '<td style="width:15%;height:20px">工序' + newProcessId + '：</td>' +
            '<td style="width:67%">XXXX1</td>' +
            '<td style="width:8%"><a herf="#" onClick="deleteProcess(\'#process' + newProcessId + '\')">删除</a></td>' +
        '</tr>');
    var oldHeight = Ext.ComponentManager.get("processInfoPanel").getHeight();
    Ext.ComponentManager.get("processInfoPanel").setHeight(oldHeight + 20);
}
</script>
</body>
</html>
