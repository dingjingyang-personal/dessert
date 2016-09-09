<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>字典管理</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/dictionary/findDictionaryJson.htm",
                mtype: "POST",
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "字典列表",
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


                colNames: ["dicid", "代码", "值",  "类型","语言","状态","创建时间"],
                colModel: [
                    {name: "dicid", hidden: true, key: true},
                    {name: "diccode", width: 100, sortable: false},
                    {name: "dicvalue", width: 100, sortable: false},
                    {name: "dictype", width: 200, sortable: false, editable: false},
                    {
                        name: "diclang", width: 100, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['diclang'] == "zh" ? "中文" : "英语";
                    }
                    },
                    {
                        name: "status", width: 100, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['status'] == "1" ? "有效" : "无效";
                        }
                    },
                    {name: "createdate", width: 200, sortable: false, editable: false,formatter:"date",formatoptions: {srcformat:'Y-m-d H:i:s',newformat:'Y-m-d H:i:s'}}
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
            var url = "${ctxPath}/system/dictionary/addOrupdateDictionary.htm";
            parent.layer.open({
                id:"addOrUpdateDictionary",
                title: '添加字典',
                type: 2,
                closeBtn: 1,
                area: ['500px', '450px'],
                content:['','no'],
                data:{url: url, data: {}},
            });
        }


        //编辑
        function editModel() {
            var row = getGridData("pagelist");
            if (row != null) {
                var url = "${ctxPath}/system/dictionary/addOrupdateDictionary.htm";
                var dicid = row.dicid;
                parent.layer.open({
                    id:"addOrupdateDictionary",
                    title: '修改字典',
                    type: 2,
                    closeBtn: 1,
                    area: ['500px', '450px'],
                    content:['','no'],
                    data:{url: url, data: {dicid: dicid}},
                });
            } else {
                layer.msg('请选择一条数据!');
            }
        }


        function delModel() {

            var row = getGridData("pagelist");
            if (row != null) {
                var dicid = row.dicid;
                var url = '${ctxPath}/system/dictionary/deleteDictionary.htm';
                var data = {};
                data.dicid = dicid;
                //询问框
                layer.confirm('确定要删除此条数据？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    layer.load(1);
                    ajaxEx({url:url,isText:true,data:data,success:function(data){
                        layer.closeAll('loading');
                        if(data==="1"){
                            parent.layer.msg('删除成功!  2秒后窗口关闭',{
                                time:2000,
                                icon: 1,
                                shade:0.3,
                                shadeClose:false
                            },function(){
                                location.reload();
                                closeFrame();//关闭窗口
                            });
                        }
                        else{
                            layer.alert("系统异常,请稍后重试", {icon: 5});
                        }
                    }});

                    }
                );


            } else {
                layer.msg('请选择一条数据!');
            }
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
                        <input  type="text" class="form-control "   id="diccode" name="diccode" placeholder="代码"/>
                    </div>
                    <div class="form-group">
                        <input  type="text" class="form-control "   id="dicvalue" name="dicvalue" placeholder="值"/>
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