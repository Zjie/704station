Ext.require(['*']);
Ext.define('order', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        //订单是否完成报工---cwm
        {name : 'isReported',type : 'string'},
        //订单配套是否完成---cwm
        {name : 'isAssigned', type : 'string'},
        //序号
        {name : 'no', type : 'string'},
        //课题编号
        {name : 'project', type : 'string'},
        //主管工艺
        {name : 'process', type : 'string'},
        //使用型号
        {name : 'model', type : 'string'},
        //产品名称
        {name : 'productName', type : 'string'},
        //产品代号
        {name : 'productCode', type : 'string'},
        //班组
        {name : 'banzu', type : 'string'},

        /**
         * 科二的投产数量和典试数量
        **/
        {name : 'ke2DianshiNum', type : 'string'},
        {name : 'ke2ProduceNum', type : 'string'},


        //计划调整数量
        {name : 'planToAdjust', type : 'string'},
        //车间计划数量（原投产数量）
        {name : 'produceNum', type : 'int'},
        //交付数量
        {name : 'deliveryNum', type : 'int'},
        //典试计划数量（原典试数量）
        {name : 'testNum', type : 'int'},

        //产品批次及编号
        {name : 'productBatchId', type : 'string'},
        //备注
        {name : 'remark', type : 'string'},

        //计划上线日期
        {name : 'onlineDate', type : 'string'},
        //计划完成日期
        {name : 'finishDate', type : 'string'},
        //更新时间
        {name : 'udate', type : 'string'},
        //更新人
        {name : 'typer', type : 'string'},
        //排产人
        {name : 'scheduler', type : 'string'},

        //物料齐套时间
        {name : 'materialsCompleteness', type : 'string'},
        //订单导入的物资齐套时间
        {name : 'materialsQitaoTime', type : 'string'},
        //零件齐套时间
        {name : 'componentCompleteness', type : 'string'},
        //入库进度
        {name : 'stockInDate', type : 'string'},
        //证明书完成时间
        {name : 'certificateDate', type : 'string'},


        //完工时间
        {name : 'reportFinishedTime', type : 'string'},
        //报工开始时间
        {name : 'reportBeginTime', type : 'string'},

        //工艺员是否确认
        {name : 'infoIsChecked', type : 'int'},
        //确认时间
        {name : 'infoCheckTime', tyep : 'string'},
        //确认者
        {name : 'processChecker', type : 'string'},

        //物料信息是否确认
        {name : 'materialIsChecked', type : 'int'},
        //确认时间
        {name : 'materialCheckTime', type : 'string'},
        //确认者
        {name : 'materialChecker', type : 'string'},

        //装配完成数量
        {name : 'assembledNum', type : 'int'},
        //装配在制数量
        {name : 'assemblingNum', type : 'int'},

        //在验数量
        {name : 'testingNum', type : 'int'},
        //完成验收的数量
        {name : 'testedNum', type : 'int'},
        //可以报试验的数量
        {name : 'canReportTestNum', type : 'int'},
        
        //在典数量
        {name : 'dianshiingNum', type : 'int'},
        //典试完成数量
        {name : 'dianshiedNum', type : 'int'},
        //还可以报典试的数量
        {name : 'canDianshiNum', type : 'int'},

        //物料配套状态
        {name : 'assignStatus', type : 'int'},
        {name : 'assignItems', type : 'string'},
        {name : 'assignNums', type : 'string'},


        /////////////2015-01-20 增加5个字段
        {name : 'factoryRemark', type : 'string'},
        {name : 'gongyiyuan', type : 'string'},
        {name : 'planAdjustNum', type : 'string'},
        {name : 'productType', type : 'string'},
        {name : 'lastAdjustDate', type : 'string'},
    ],
    idProperty: 'id'
});
//角色
Ext.define('role', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'name', type : 'string'},
        {name : 'remark', type : 'string'},
        {name : 'udate', type : 'string'},
		{name : 'updater', type : 'string'}
    ],
    idProperty: 'id'
});
//用户
Ext.define('user', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'userName', type : 'string'},
        {name : 'roleName', type : 'string'},
        {name : 'udate', type : 'string'},
        {name : 'nickName', type : 'string'},
        {name : 'updater', type : 'string'}
    ],
    idProperty: 'id'
});
//物料模板
Ext.define('materialTemplate', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'name', type : 'string'},//模板名称
        {name : 'groupName', type : 'string'}, //族名称
        {name : 'udate', type : 'string'}, // 更新时间
        {name : 'updater', type : 'string'}, //更新人
        {name : 'productCode', type : 'string'} //产品代号
    ],
    idProperty: 'id'
});
//物料
Ext.define('material', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'no', type : 'int'}, //序号
        {name : 'type', type : 'string'},//物料类别
        {name : 'name', type : 'string'}, //物料名称
        {name : 'specification', type : 'string'},//型号规格
        {name : 'level', type : 'string'},//质量等级或标准要求
        {name : 'uom', type : 'string'},//计量单位
        {name : 'singleNum', type : 'int'},//单机数量
        {name : 'bkNum', type : 'int'},//工艺备份数量
        {name : 'udate', type : 'string'}, // 更新时间
        {name : 'updater', type : 'string'},//更新人
        {name : 'typeValue', type: 'int'},//用于分组使用
        {name : 'totalAssign', type: 'int'},//已配套数量---cwm
        {name : 'leftAssign', type: 'int'},//剩余可配套数量---cwm
        {name : 'allNum', type: 'int'},//总需求量=单机*投产数量
        {name : 'diffNum', type: 'int'},//差额=总需求量-已配套数量
    ],
    idProperty: 'id'
});

//工序
Ext.define('process', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'id', type:'int'},
        {name:'phase', type:'string'},
        {name:'content', type:'string'},
        {name:'groupName', type:'int'},
        {name:'measure', type:'int'},
        {name:'remark', type:'string'},
        {name:'updater', type:'string'},
        {name:'udate', type:'string'},
        {name:'base', type:'int'},
        {name:'reportNum', type:'int'},//排产数量---cwm
        {name:'reportedNum',type:'int'},//已排产数量---cwm
        {name:'leftNum',type:'int'},//未排产数量---cwm
        {name:'reportTime',type:'int'},//已排产工序花费的时间---cwm
        //排产开始时间
        {name:'beginDate', type:'string'},
        //排产结束时间
        {name:'endDate', type:'string'},
    ],
    idProperty: 'id'
});
//工序模板
Ext.define('processTemplate', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'id', type:'int'},
        {name:'groupName', type:'string'},
        {name:'name', type:'string'},
        {name:'productCodeString', type:'string'},
        {name:'updater', type:'string'},
        {name:'udate', type:'string'}
    ],
    idProperty: 'id'
});

//排产结果
Ext.define('disptachResult', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'oid', type:'int'},
        //序号
        {name : 'order.no', type : 'string'},
        //课题编号
        {name : 'order.project', type : 'string'},
        //主管工艺
        {name : 'order.process', type : 'string'},
        //使用型号
        {name : 'order.model', type : 'string'},
        //产品名称
        {name : 'order.productName', type : 'string'},
        //产品代号
        {name : 'order.productCode', type : 'string'},

        //产品批次及编号
        {name : 'order.productBatchId', type : 'string'},
        //备注
        {name : 'order.remark', type : 'string'},

        //计划上线日期
        {name : 'order.onlineDate', type : 'string'},
        //计划完成日期
        {name : 'order.finishDate', type : 'string'},

        //工序组id
        {name:'gupId', type:'int'},
        //工作内容
        {name:'groupId', type:'string'},
        //订单id
        {name:'id', type:'int'}
    ],
    idProperty: 'id'
});
//员工---cwm
Ext.define('worker', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'realName', type : 'string'},
        {name : 'unit', type : 'string'},
        {name : 'uid', type : 'int'},
        {name : 'userName', type : 'string'},
        {name : 'isFreezed', type : 'string'},
    ],
    idProperty: 'id'
});

//排产结果工序组
Ext.define('scheduledResult', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'name', type : 'string'}
    ],
    idProperty: 'id'
});

//报工停工
Ext.define('tinggong', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'orderId', type : 'int'},
        {name : 'reason', type : 'string'},
        {name : 'desc', type : 'string'},
        {name : 'start', type : 'string'},
        {name : 'end', type : 'string'},
        {name : 'udate', type : 'string'},

        {name : 'srId', type : 'int'},
        {name : 'srName', type : 'string'},

        {name : 'orderNo', type : 'string'},
        {name : 'reporter', type : 'string'},
        {name : 'commiter', type : 'string'},
        {name : 'restarter', type : 'string'},

        {name : 'commitDate', type : 'string'},
        {name : 'restartDate', type : 'string'},
    ],
    idProperty: 'id'
});
//报工信息
Ext.define('produceReport', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'orderId', type : 'int'},
        {name : 'opid', type : 'int'},
        {name : 'orderNo', type : 'string'},
        {name : 'phase', type : 'string'},
        {name : 'content', type : 'string'},
        {name : 'groupName', type : 'string'},

        {name : 'reportNum', type : 'int'},
        {name : 'reportTime', type : 'int'},

        {name : 'udate', type : 'string'},
        {name : 'updater', type : 'string'},
    ],
    idProperty: 'id'
});

//产品族--产品代号绑定关系
Ext.define('productGroup', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'groupId', type : 'int'},
        {name : 'groupName', type : 'string'},
        {name : 'productCode', type : 'string'},
    ],
    idProperty: 'id'
});
//产品族--工序组绑定关系
Ext.define('processGroup', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'groupId', type : 'int'},
        {name : 'groupName', type : 'string'},
        {name : 'unit', type : 'string'},
        {name : 'processGroup', type : 'int'},
        //工序组类型 0外协 1特殊 2普通
        {name : 'type', type : 'string'},
        {name : 'typeValue', type : 'int'},
        {name : 'afterProcessGroup', type : 'int'},
    ],
    idProperty: 'id'
});
//工序组和工人的映射关系
Ext.define('processWorker', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'gupId', type : 'int'},
        {name : 'base', type : 'int'},
        {name : 'timeToConsume', type : 'int'},
        {name : 'workerId', type : 'int'},
        {name : 'workerName', type : 'string'},
    ],
    idProperty: 'id'
});


//试验报工
Ext.define('produceTest', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'content', type : 'string'},
        {name : 'num', type : 'int'},
        {name : 'udate', type : 'string'},
        {name : 'status', type : 'int'},
        {name : 'statusName', type : 'string'},
        {name : 'worker', type : 'string'},
        {name : 'confirmDate', type : 'string'},
    ],
    idProperty: 'id'
});

//典试报工
Ext.define('produceDianshi', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'num', type : 'int'},
        {name : 'udate', type : 'string'},
        {name : 'statusName', type : 'string'},
        {name : 'status', type : 'int'},
        {name : 'worker', type : 'string'},
        {name : 'confirmDate', type : 'string'},
        {name : 'content', type : 'string'},
    ],
    idProperty: 'id'
});

//其他报工
Ext.define('produceOther', {
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id', type : 'int'},
        {name : 'content', type : 'string'},
        {name : 'udate', type : 'string'},
        {name : 'worker', type : 'string'},
    ],
    idProperty: 'id'
});

//通用函数
/**
 * 获取url中的参数
**/
function getParams(name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
   var r = window.location.search.substr(1).match(reg);
   if (r!=null) return unescape(r[2]); return null;
}

var phase = Ext.create('Ext.data.Store', {
    fields: ['name', 'value'],
    data : [
        {"name":"部件装配", "value":"部件装配"},
        {"name":"调试", "value":"调试"},
        {"name":"总装", "value":"总装"}
    ]
});
//工序组类型
var processTypeStore = Ext.create('Ext.data.Store', {
    fields: ['name', 'value'],
    data : [
        {"name":"外协", "value":0},
        {"name":"特殊", "value":1},
        {"name":"普通", "value":2},
    ]
});
//生产单元
var unitStore = new Ext.data.SimpleStore({
    fields:['value','text'],
    data:[['A','A'],['B','B'],['C','C'],['D','D'],['E','E']]
});