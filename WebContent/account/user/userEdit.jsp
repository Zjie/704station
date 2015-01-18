<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>用户信息编辑</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="../../extjs/ext-all-debug.js"></script>
    <link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
    <script type="text/javascript" src="../../resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="../../resources/js/common.js"></script>
</head>
<body>
<script type="text/javascript">
Ext.onReady(function() {
	var uem = ${uem};
    var states = Ext.create('Ext.data.Store', {
        fields: ['roleId', 'roleName'],
        data : uem.roles
    });
    Ext.create('Ext.form.Panel', {
        title: '用户信息',
        renderTo: Ext.getBody(),
        bodyPadding: 5,
        width: 350,
        padding: '20, 0, 0, 10',
        url: 'saveOrUpdate.do',
        items: [{
        	xtype: 'textfield',
        	name: 'uid',
        	hidden: 'true',
        	value: uem.uid
        },{
        	xtype: 'textfield',
        	name: 'saveOrNot',
        	hidden: 'true',
        	value: !uem.readOnly
        },{
            xtype: 'textfield',
            fieldLabel: '用户名',
            allowBlank: false ,
            msgTarget: 'under',
            blankText: '该字段不能为空',
            name: 'userName',
            value: uem.userName,
            readOnly: uem.readOnly
        },{
            xtype: 'textfield',
            fieldLabel: '用户昵称',
            allowBlank: false ,
            blankText: '该字段不能为空',
            msgTarget: 'under',
            name: 'nickName',
            value: uem.nickName
        },{
            xtype: 'textfield',
            fieldLabel: '用户密码',
            blankText: '该字段不能为空',
            msgTarget: 'under',
            allowBlank: false,
            name: 'password',
            value: uem.password
        },{
            xtype: 'combobox',
            fieldLabel: '角色',
            store: states,
            msgTarget: 'under',
            queryMode: 'local',
            displayField: 'roleName',
            allowBlank: false ,
            blankText: '该字段不能为空',
            valueField: 'roleId',
            name: 'roleId',
            value: uem.roleId>0?uem.roleId:''
        }],

        buttons: [{
            text: '确认',
            handler: function() {
                var form = this.up('form').getForm();
                if (form.isValid()) {
                	form.submit({
                        success: function(form, action) {
                        	Ext.Msg.alert('系统提示', action.result.msg, function(buttonId, text, opt) {
                            	if (buttonId == 'ok'){
                		            window.location.href = "userList.html";
                            	}
                            });
                        },
                        failure: function(form, action) {
                        	Ext.Msg.alert('系统提示', action.result.msg);
                        }
                    });
                }
            }
        },{
            text: '解绑角色',
            handler: function() {
                var form = this.up('form').getForm();
                var id = form.findField('uid').getValue();
                $.post('unbindRole.do', {'uid':id}, function(response) {
                    if (response.success) {
                        Ext.Msg.alert('系统提示', response.msg, function(buttonId, text, opt) {
                            if (buttonId == 'ok'){
                                window.location.href = "userList.html";
                            }
                        });
                    } else {
                        Ext.Msg.alert('系统提示', response.msg);
                    }
                });
            }
        },{
            text: '取消',
            handler: function() {
                window.location.href = "userList.html";
            }
        }]
	});
});
</script>
</body>
</html>
