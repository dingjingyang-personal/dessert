<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>序列管理</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/sequence/findSequenceJson.htm",
                mtype: "POST",
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "序列列表",
                rowNum: 10,
                rownumbers: false,
                autowidth: true,
                height: "auto",
                loadui: "enable",
                treeIcons: {leaf: 'ui-icon-document-b'},
                jsonReader: {
                    root: "pageList",
                    page: "currentPage",
                    total: "pageCount",
                    records: "recordCount",
                    repeatitems: false
                },
                prmNames: {
                    page: "currentPage",   // 表示请求页码的参数名称
                    rows: "pageSize",
                },


                colNames: [ "流水号KEY", "描述",  "创建用户","创建时间","操作"],
                colModel: [
                    {name: "seqkey", width: 100, sortable: false},
                    {name: "seqdesc", width: 200, sortable: false},
                    {name: "createuser", width: 100, sortable: false, editable: false},
                    {name: "createdate", width: 100, sortable: false, editable: false,formatter:"date",formatoptions: {srcformat:'Y-m-d H:i:s',newformat:'Y-m-d H:i:s'}},
                    {
                        name: "操作", width: 100, sortable: false, editable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            var deploy= "<a href='#' onclick=\"settingSequence('"+rowObject.seqkey+"')\">配置</a>";
                            var currentValue= "<a href='#' onclick=\"currentValue('"+rowObject.seqkey+"')\">当前序列值</a>";
                            return deploy+"&nbsp&nbsp&nbsp"+currentValue;
                        }
                    }
                ],



            pager: "#jqGridPager"
            });
            jQuery("#pagelist")
                    .jqGrid("navGrid", "#jqGridPager", {
                        edit: false,
                        add: false,
                        del: false,
                        search: false,
                        refresh: true,
                        refreshstate: "current",
                        refreshicon: "fa fa-refresh"
                    })

        }


        //添加
        function addModel() {
            var url = "${ctxPath}/system/sequence/addOrupdateSequence.htm";
            parent.layer.open({
                id:"addOrUpdateSequence",
                title: '添加序列',
                type: 2,
                closeBtn: 1,
                area: ['500px', '250px'],
                content:['','no'],
                data:{url: url, data: {}},
            });
        }


        //编辑
        function editModel() {
            var row = getGridData("pagelist");
            if (row != null) {
                var url = "${ctxPath}/system/sequence/addOrupdateSequence.htm";
                var seqkey = row.seqkey;
                parent.layer.open({
                    id:"addOrupdateSequence",
                    title: '修改序列',
                    type: 2,
                    closeBtn: 1,
                    area: ['500px', '250px'],
                    content:['','no'],
                    data:{url: url, data: {seqkey: seqkey}},
                });
            } else {
                layer.msg('请选择一条数据!');
            }
        }


        //配置
        function settingSequence(seqkey) {
            var url = "${ctxPath}/system/sequence/settingSequencePage.htm";
            var seqkey = seqkey;
            parent.layer.open({
                id:"settingSequencePage",
                title: '序列配置',
                type: 2,
                closeBtn: 1,
                area: ['600px', '500px'],
                content:['','no'],
                data:{url: url, data: {seqkey:seqkey}},
            });
            
        }

        //当前序列值
        function currentValue(seqkey) {
            var url = "${ctxPath}/system/sequence/sequenceValuePage.htm";
            var seqkey = seqkey;
            parent.layer.open({
                id:"settingSequencePage",
                title: '序列值',
                type: 2,
                closeBtn: 1,
                area: ['600px', '500px'],
                content:['','no'],
                data:{url: url, data: {seqkey:seqkey}},
            });

        }
        
        
        

        //搜索
        function querySubmit(){
            var searchParams = getAllInputValue("#findData");// 初始化传参数
            $("#pagelist").jqGrid('setGridParam',{
                postData:searchParams, //发送数据
            }).trigger("reloadGrid"); //重新载入
        }


    </script>
</head>

<body>
<#escape x as x?html>


<div class="wrapper wrapper-content">
    <div class="ibox float-e-margins">
        <div class="ibox-title">
            <h3>字典管理</h3>

        </div>
        <div class="ibox-content">
            <div class="form-group">
                <button id="btnAdd" type="button" class="btn btn-primary btn-sm" onclick="addModel()"><i
                        class="fa fa-plus"></i>&nbsp;&nbsp;添加
                </button>
                <button id="btnEdit" type="button" class="btn btn-success btn-sm" onclick="editModel()"><i
                        class="fa fa-pencil-square-o"></i>&nbsp;&nbsp;编辑
                </button>

                <button id="btnDel" type="button" class="btn btn-danger btn-sm" onclick="delModel()">
                    <i class="fa fa-trash"></i>&nbsp;&nbsp;删除
                </button>
            </div>

            <div class="form-group">

                <form class="form-inline" id="findData">
                    <div class="form-group">
                        <input  type="text" class="form-control "   id="seqkey" name="seqkey" placeholder="流水号KEY"/>
                    </div>
                        <span class="">
                                <a id="btnSearch" class="btn btn btn-primary btn-sm" href="javascript:querySubmit()"> <i class="glyphicon glyphicon-search"></i> 搜索</a>
                        </span>
                </form>
            </div>

            <div class="jqGrid_wrapper">
                <table id="pagelist"></table>
                <div id="jqGridPager"></div>
            </div>
        </div>
    </div>
</div>





</#escape>
</body>

</html>