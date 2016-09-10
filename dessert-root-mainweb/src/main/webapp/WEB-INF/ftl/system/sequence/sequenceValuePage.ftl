<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>当前序列</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/sequence/findSequenceValueJson.htm",
                mtype: "POST",
                postData: {seqkey: "${RequestParameters.seqkey}"},
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


                colNames: ["所有者", "值", "创建时间", "操作"],
                colModel: [
                    {name: "seqowner", width: 150, sortable: false},
                    {name: "seqvalue", width: 150, sortable: false},
                    {name: "createdate", width: 150, sortable: false, editable: false},
                    {
                        name: "操作", width: 100, sortable: false, editable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            var deploy = "<a href='#' onclick=\"editModel('" + rowObject.seqowner + "')\">修改</a>";
                            return deploy;
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


        //修改
        function editModel(seqowner) {
            var url = "${ctxPath}/system/sequence/updateSequenceValuePage.htm";
            var seqkey = "${RequestParameters.seqkey}";
            var seqowner = seqowner;
            layer.open({
                id: "updateSequenceValuePage",
                title: '修改值',
                type: 2,
                closeBtn: 1,
                area: ['500px', '200px'],
                content: ['', 'no'],
                data: {url: url, data: {seqkey: seqkey,seqowner:seqowner}},
            });
        }


    </script>
</head>

<body>
<#escape x as x?html>


<div class="wrapper wrapper-content">
    <div class="ibox float-e-margins">
        <div class="ibox-content">
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