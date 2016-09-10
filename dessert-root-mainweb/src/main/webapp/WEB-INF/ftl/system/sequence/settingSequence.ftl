<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>序列配置</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/sequence/findSettingSequenceJson.htm",
                mtype: "POST",
                postData:{seqkey:"${RequestParameters.seqkey}"},
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "序列配置",
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


                colNames: [ "seqsettingid","类型", "类型值",  "顺序"],
                colModel: [
                    {name: "seqsettingid", hidden: true, key: true},
                    {
                        name: "seqtype", width: 150, sortable: false, editable: false, formatter: function (v, x, r) {
                            if(r['seqtype']=="1"){
                                return "静态文本"
                            }else if(r['seqtype']=="2"){
                                return "日期格式"
                            }else {
                                return "流水号位数"
                            }
                        }
                    },
                    {name: "seqtypevalue", width: 100, sortable: false},
                    {name: "seqorder", width: 100, sortable: false, editable: false}

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
            var url = "${ctxPath}/system/sequence/addOrupdateSettingSequence.htm";
            layer.open({
                id:"addOrupdateSettingSequence",
                title: '添加序列配置',
                type: 2,
                closeBtn: 1,
                area: ['500px', '300px'],
                content:['','no'],
                data:{url: url, data: {seqkey: "${RequestParameters.seqkey!''}"}},
            });
        }


        //编辑
        function editModel() {
            var row = getGridData("pagelist");
            if (row != null) {
                var url = "${ctxPath}/system/sequence/addOrupdateSettingSequence.htm";
                var seqsettingid = row.seqsettingid;
                layer.open({
                    id:"addOrupdateSettingSequence",
                    title: '修改序列配置',
                    type: 2,
                    closeBtn: 1,
                    area: ['500px', '300px'],
                    content:['','no'],
                    data:{url: url, data: {seqsettingid: seqsettingid}},
                });
            } else {
                layer.msg('请选择一条数据!');
            }
        }


    </script>
</head>

<body>
<#escape x as x?html>


<div class="wrapper wrapper-content">
    <div class="ibox float-e-margins">
        <div class="ibox-content">
            <div class="form-group">
                <button id="btnAdd" type="button" class="btn btn-primary btn-sm" onclick="addModel()"><i
                        class="fa fa-plus"></i>&nbsp;&nbsp;添加
                </button>
                <button id="btnEdit" type="button" class="btn btn-success btn-sm" onclick="editModel()"><i
                        class="fa fa-pencil-square-o"></i>&nbsp;&nbsp;编辑
                </button>

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