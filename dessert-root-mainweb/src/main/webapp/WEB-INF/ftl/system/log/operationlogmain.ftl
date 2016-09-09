<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>操作日志</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/log/findOperationLogJson.htm",
                mtype: "POST",
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "操作日志",
                rowNum: 10,
                rownumbers: true,
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


                colNames: ["operatinglogid", "用户名", "模块", "操作", "IP","运行时间","创建时间"],
                colModel: [
                    {name: "operatinglogid", hidden: true, key: true},
                    {name: "username", width: 100, sortable: false},
                    {name: "module", width: 200, sortable: false},
                    {name: "methods", width: 200, sortable: false},
                    {name: "ip", width: 100, sortable: false, editable: false},
                    {name: "runtime", width: 100, sortable: false, editable: false},
                    {name: "createdate", width: 150, sortable: false, editable: false,formatter:"date",formatoptions: {srcformat:'Y-m-d H:i:s',newformat:'Y-m-d H:i:s'}}
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
            <h3>操作日志</h3>

        </div>
        <div class="ibox-content">

            <div class="form-group">

                <form class="form-inline" id="findData">
                    <div class="form-group">
                        <input  type="text" class="form-control "   id="username" name="username" placeholder="操作员"/>
                    </div>
                    <div class="form-group">
                        <input  type="text" class="form-control "   id="ip" name="ip" placeholder="IP"/>
                    </div>
                        <span class="">
                                <a id="btnSearch" class="btn btn btn-primary" href="javascript:querySubmit()"> <i class="glyphicon glyphicon-search"></i> 搜索</a>
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