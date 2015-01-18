Ext.onReady(function() {
    var treeMenuStore = new Ext.data.TreeStore({
        model: 'Item',
        root: {
            text: 'Root',
            expanded: true,
            children: [
                {
                    text: '订单管理',
                    expanded: false,
                    children: [
                        {text: '订单导入', leaf: true, hrefTarget: 'order/addOrder.jsp'},
                        {text: '订单合批', leaf: true},
                        {text: '订单工序修改', leaf: true, hrefTarget: 'order/reviseProcesses.jsp'},
                        {text: '待排订单', leaf: true},
                        {text: '已排订单', leaf: true},
                        {text: '挂起订单', leaf: true},
                        {text: '外协订单', leaf: true},
                        {text: '订单高级检索', leaf: true, hrefTarget: 'order/allOrder.jsp'},
                        {text: '统计分析', leaf: true},
                        {text: '订单入库', leaf: true}
                    ]
                }, {
                    text: '生产排产',
                    expanded: false,
                    children: [
                        {text: '自动排产', leaf: true},
                        {text: '人工调整', leaf: true},
                        {
                            text: '动态调度',
                            children: [
                                {text: '插单', leaf: true},
                                {text: '停工调度', leaf: true},
                                {text: '外协调度', leaf: true},
                                {text: '重启调度', leaf: true}
                            ]
                        }
                    ]
                }, {
                    text: '生产报工',
                    children: [
                        {text: '派工单打印', leaf: true},
                        {text: '报工', leaf: true, hrefTarget: 'produce/report.jsp'}
                    ]
                }, {
                    text: '工序库管理',
                    children: [
                        {text: '工序管理', leaf: true},
                        {text: '工序模板管理', leaf: true},
                        {text: '模板导入', leaf: true}
                    ]
                }, {
                    text: '领料管理',
                    expanded: false,
                    children: [
                        {text: '领料配套', leaf: true},
                        {text: '派工单领料', leaf: true},
                        {text: '任务更改跟踪', leaf: true},
                        {text: '退库跟踪', leaf: true}
                    ]
                }, {
                    text: '工艺文件',
                    expanded: false,
                    children: [
                        {text: '材料定额填写', leaf: true},
                        //履历书，图纸等等
                        {text: '工艺文件配套', leaf: true},
                        {
                            text: '三单管理',
                            children: [
                                {text: '通知单', leaf: true},
                                {text: '更改单', leaf: true},
                                {text: '质疑单', leaf: true}
                            ]
                        }
                    ]
                }, {
                    text: '物料库管理',
                    children: [
                        {text: '物料品类管理', leaf: true},
                        {text: '物料模板管理', leaf: true},
                        {text: '模板导入', leaf: true}
                    ]
                }, {
                    text: '基本信息管理',
                    children: [
                        {text: '人员管理', leaf: true},
                        {text: '班组管理', leaf: true},
                        {text: '部门管理', leaf: true}
                    ]
                }, {
                    text: '系统管理',
                    children: [
                        {text: '账户管理', leaf: true},
                        {text: '角色管理', leaf: true},
                        {text: '权限管理', leaf: true}
                    ]
                }, {
                    text: '其他功能',
                    children: [
                        {text: '成品移动', leaf: true},
                        {text: '临时计划', leaf: true}
                    ]
                }
            ]
        }
    });

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
        id: 'border-example',
        layout: 'border',
        items: [
            // create instance immediately
            Ext.create('Ext.Component', {
                region: 'north',
                height: 30, // give north and south regions a height
                autoEl: {
                    tag: 'div',
                    //html:'<img src="resources/image/logo.jpg"/>'
                    html: ' <div>\
								<div style="float:left;text-align: center; margin: 5px; font-size: 17px; font-weight: 700;">航天九院七〇四所传感器车间</div>\
								<div style="float:right">joller <a href="/logOut">退出</div></div>\
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
                    //autoHeight : true
                    minHeight : document.body.clientHeight
                    //height : '100%'
                }]
            }), 
            Ext.create('Ext.Component', {
                region: 'south',
                height: 20,
                autoEl: {
                    tag: 'div',
                    //html:'<img src="resources/image/logo.jpg"/>'
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