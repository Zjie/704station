<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="cn.edu.ustb.sem.core.auth.bo.Visitor"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>航天九院七〇四所传感器车间</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="resources/css/index.css" />
    <style type="text/css">
        p {
            margin:5px;
        }
        .producePlanning {
            background-image:url(resources/image/grid.png);
        }
        .processFile {
            background-image:url(resources/image/book.png);
        }
        .pom {
            background-image:url(resources/image/connect.png);
        }
        .settings {
            background-image:url(resources/image/folder_wrench.png);
        }
        .producing {
            background-image:url(resources/image/plugin_add.gif);
        }
        .info {
            background-image:url(resources/image/information.png);
        }
    </style>
    <script type="text/javascript" src="extjs/ext-all-debug.js"></script>
    <script type="text/javascript" src="extjs/ext-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css" />
    <script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="resources/js/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="resources/js/common.js"></script>
    <script type="text/javascript">
    	function loadPage(url) {
			$("#contentFrame", parent.document.body).attr("src", url);
		}

		//frame自适应高度
		function setWinHeight() 
		{
			var main = $(window.parent.document).find("#contentFrame");
			var thisheight = $(document).height() - 50;
			main.height(thisheight);
		}
    </script>
</head>
<body>
    <div id="mainContent">
        <iframe id="contentFrame" width="100%" src="" allowTransparency="true" marginheight="50px" frameborder="0" scrolling="auto" onLoad="setWinHeight()"></iframe>
    </div>
	<script type="text/javascript">
	Ext.onReady(function() {
		var username = '${username}';
		var treeMenuStore = new Ext.data.TreeStore(eval('(${treeStore})'));

		var treeMenu = new Ext.tree.Panel({
			border : false,
			store: treeMenuStore,
			rootVisible: false,
			listeners : {
				itemclick: function(thisView, record, item, index, e, eOpts){
					if (record.isLeaf())
						loadPage(record.data.hrefTarget);
				}
			}
		});
		var viewport = Ext.create('Ext.Viewport', {
			id: 'indexPage',
			layout: 'border',
			items: [
				Ext.create('Ext.Component', {
					region: 'north',
					height: 30, // give north and south regions a height
					autoEl: {
						tag: 'div',
						html: ' <div>\
									<div style="float:left;text-align: center; margin: 5px; font-size: 17px; font-weight: 700;">航天九院七〇四所传感器车间</div>\
									<div style="float:right">' + username + '&nbsp&nbsp<a href="/logOut">退出</a></div>\
								</div>'
					}
				}), 
				{
					region: 'west',
					stateId: 'navigation-panel',
					id: 'west-panel', // see Ext.getCmp() below
					title: '菜单',
					split: false,
					width: 200,
					minWidth: 175,
					maxWidth: 400,
					collapsible: true,
					animCollapse: true,
					autoScroll: true,
					scroll: 'vertical',
					margins: '0 0 0 5',
					//layout: 'accordion',
					items: treeMenu
				},
				Ext.create('Ext.form.Panel', {
					region: 'center', // a center region is ALWAYS required for border layout
					deferredRender: false,
					items: [{
						contentEl : 'mainContent',
						closable : false,
						autoScroll : true,
						autoWidth : true,
						minHeight : document.body.clientHeight
					}]
				}), 
				Ext.create('Ext.Component', {
					region: 'south',
					height: 20,
					autoEl: {
						tag: 'div',
						html: '<div style="text-align: right; font-size: 12px;">power by 北京科技大学 东凌经济管理学院</div>'
					}
				})
			]
		});
		//默认加载第一个菜单的按钮
		var curNode = treeMenuStore.tree.root;
		while (curNode.childNodes.length > 0) {
			curNode = curNode.childNodes[0];
		}
		loadPage(curNode.data.hrefTarget);
	});
	</script>
</body>
</html>
