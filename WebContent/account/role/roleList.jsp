<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>全部角色</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../../resources/js/common.js"></script>
</head>
<body>
    <div id="roleGrid" ></div>
    <div id='addRole' style="text-align:right;margin:5 40 0 0">
	    <input type='button' value='新增角色' onclick="addRole()"></input>
	</div>
<script type="text/javascript">
Ext.onReady(function(){
    var store = Ext.create('Ext.data.Store', {
        //pageSize: 1,
        model: 'role',
        autoLoad: true,
        data : eval('(${data})')
    });
    var grid = Ext.create('Ext.grid.Panel', {
        title: '所有角色',
        store: store,
        region: 'center',
        disableSelection: false,
        loadMask: true,
        viewConfig: {
            id: 'orderVidewConfig',
            trackOver: false,
            stripeRows: false
        },
        columns:[{
            hidden: true,
            text: "角色id",
            dataIndex: 'id',
            ////sortable: false
        },{
            text: "角色名称",
            dataIndex: 'name',
            ////sortable: false
        },{
            text: "描述",
            dataIndex: 'remark',
            ////sortable: false
        },{
            text: "更新时间",
            dataIndex: 'udate',
            ////sortable: false
        },{
			text: "更新人",
			dataIndex: "updater",
			////sortable: false
		},{
            text: "操作",
            xtype: "templatecolumn",
            tpl: "<a href='roleEdit.do?roleId={id}'>编辑</a>&nbsp<a style='color: red;' onclick='deleteRole({id})'>删除</a>"
        }],
    });
    var viewport = Ext.create('Ext.Viewport', {
        id: 'border-example',
        layout: 'border',
        items: [
                grid, 
                {
                	height: 40,
                    region: 'south',
                    xtype: 'panel',
                    contentEl: 'addRole'
                }
        ]
    });
});
function deleteRole(roleId) {
	Ext.Msg.show({
	    title: '系统提示',
	    msg: '您确定要删除该角色？',
	    buttons: Ext.Msg.OKCANCEL,
	    fn: function(buttonId, text, opt) {
	    	if (buttonId == 'ok'){
				$.ajax({
			        type: "POST",
			        url: "delete.do",
			        data: "roleId=" + roleId,
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
	    },
	    icon: Ext.window.MessageBox.WARNING
	});
}
function addRole() {
	window.location.href = "roleEdit.do";
}
</script>
</body>
</html>
