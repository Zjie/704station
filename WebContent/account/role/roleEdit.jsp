<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>角色编辑</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../../resources/js/common.js"></script>
</head>
<body>
<script type="text/javascript">
var nameIdMap = eval('(${nameIdMap})');
var listeners = {
    checkchange:function(node, checked, eOpts) {
        dfsNodes(node, checkNodeDFS, checked);
    }
}
var allTreeStore = eval('(${allTreeStore})');
Ext.onReady(function() {
    var allTreeMenu = new Array();
    for (var i = 0; i < allTreeStore.length; i++) {
        var curTs = new Ext.data.TreeStore(allTreeStore[i]);
        var title = curTs.getRootNode().childNodes[0].get("text");
        allTreeMenu.push(
            {
                title: title,
                border : false,
                xtype: 'panel',
                store: curTs,
                rootVisible: false,
                listeners : listeners
            }
        );
    }
    var tms = new Array();
    for (var i = 0; i < allTreeMenu.length; i++) {
        tms.push(new Ext.tree.Panel(allTreeMenu[i]));
    }

    var viewport = Ext.create('Ext.Viewport', {
        id: 'mainPanel',
        layout: 'border',
        items: [
            {
                region: 'north',
                xtype: 'panel',
                title: '角色信息',
                contentEl: 'basic'
            },
            {
                region: 'center',
                xtype: 'panel',
                id: 'auths',
                title: '权限设置',
                split: false,
                collapsible: false,
                xtype: 'panel',
                layout: 'accordion',
                items: tms
            },
            {
            	height: 40,
                region: 'south',
                xtype: 'panel',
                contentEl: 'commit'
            }
        ]
    });
})
//选择当前节点及其父节点
function checkNodeDFS(node, checked) {
    if (checked) {
        //如果有个子节点被选中了，其父节点也要被选中
        node.parentNode.set('checked', checked);
    }
    node.set('checked', checked);
}

//提交角色信息
function updateRoleInfo() {
    var roleName = $("#roleName").val();
    var roleId = '${roleId}';
    if (roleName == '') {
    	Ext.Msg.alert('系统提示', '角色名称不能为空');
        return;
    }
    var desc = $("#desc").val();
    var data_str = "roleName=" + roleName + "&desc=" + desc;
	if (roleId != '') {
		data_str = data_str + "&roleId=" + roleId;
    }
    var idx = 0;
    for (var j = 0; j < allTreeStore.length; j++) {
    	var ts1 = Ext.getStore(allTreeStore[j].id).getRootNode().childNodes[0];
        dfsNodes(ts1, function(node) {
            if (!node.data.checked) {
                return;
            }
            data_str = data_str + "&appIds[" + idx + "]=" + nameIdMap[node.data.text];
            idx = idx + 1;
        });
    }
    $.ajax({
        type: "POST",
        url: "roleSaveOrUpdate.do",
        data: data_str,
        success: function(data) {
            var result = eval('(' + data + ')');
            Ext.Msg.alert('系统提示', result.msg, function(buttonId, text, opt) {
            	if (buttonId == 'ok'){
		            if (result.state == 0) {
		            	window.location.href = "roleList.do";
		            }
            	}
            });
        }
    });
}

//递归遍历所有节点
function dfsNodes(node, func, args) {
    func(node, args);
    if (node.hasChildNodes()) {
        for (var i = 0; i < node.childNodes.length; i++) {
            dfsNodes(node.childNodes[i], func, args);
        }
    }
}
</script>
<div id='basic'>
    <div style="margin: 10px 0px 0px 20px;float:left">角色名称：<input type="text" name="roleName" id="roleName" value="${roleName}" /></div>
    <div style="margin: 10px 0px 10px 20px;float:left">角色用途描述：<input type="text" name="desc" id="desc" value="${remark }"/><div>
</div>
<div id='commit' style="text-align:right;margin:5 40 0 0">
    <input type='button' value='提交' onclick="updateRoleInfo()"></input>&nbsp&nbsp&nbsp&nbsp
    <input type='button' value='返回' onclick="history.back()"></input>
</div>
</body>
</html>
