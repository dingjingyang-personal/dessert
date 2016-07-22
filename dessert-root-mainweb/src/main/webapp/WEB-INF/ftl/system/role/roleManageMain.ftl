<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>角色管理</title>

    <style type="text/css">

        .ui-jqgrid-bdiv {
            overflow: hidden !important
        }

    </style>

    <script type="text/javascript">


        $(function () {

            $(window).resize(function () {
                $("#pagelist").setGridWidth($(window).width() * 0.97);
            });

        });


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/role/findRolesJson.htm",
                mtype: "POST",
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "角色列表",
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


                colNames: ["roleid", "角色名称", "角色类型", "状态", "描述"],
                colModel: [
                    {name: "roleid", hidden: true, key: true},
                    {name: "rolename", width: 100, sortable: false},
                    {
                        name: "roletype", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['roletype'] == "1" ? "管理员" : "普通员工";
                    }
                    },
                    {
                        name: "status", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['status'] == "1" ? "有效" : "无效";
                    }
                    },
                    {name: "roledescription", width: 200, sortable: false, editable: false}
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
                    .navButtonAdd('#jqGridPager', {
                        caption: "自定义",
                        buttonicon: "ui-icon-add",
                        onClickButton: function () {
                            alert("Adding Row");
                        },
                        position: "last"
                    });

        }


        //添加
        function addModel() {
            layer.open({
                title: '添加角色',
                type: 2,
                closeBtn: 0,
                area: ['400px', '450px'],
                content: ['${ctxPath}/system/role/addOrUpdateRolePage.htm','no']
            });
        }


        //编辑
        function editModel() {
            var row = getGridData("pagelist");
            if (row != null) {
                var roleid = row.roleid;
                layer.open({
                    title: '修改角色',
                    type: 2,
                    closeBtn: 0,
                    area: ['400px', '450px'],
                    content: ['${ctxPath}/system/role/addOrUpdateRolePage.htm?roleid=' + roleid, 'no']
                });
            } else {
                layer.msg('请选择一条数据!');
            }
        }


        function delModel() {

            var row = getGridData("pagelist");
            if (row != null) {

                var roleid = row.roleid;
                var url = '${ctxPath}/system/role/deleteRole.htm';
                var data = {};
                data.roleid = roleid;
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
                                window.parent.$('#pagelist').trigger('reloadGrid');//列表页面刷新数据
                                closeFrame();//关闭窗口
                            });
                        }
                        else{
                            layer.alert(data, {icon: 5});
                        }
                    }});

                    }
                );


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
        <div class="ibox-title">
            <h5>角色管理</h5>

        </div>
        <div class="ibox-content">
            <div class="form-group">
                <button id="btnAdd" type="button" class="btn btn-primary" onclick="addModel()"><i
                        class="fa fa-plus"></i>&nbsp;添加
                </button>
                <button id="btnEdit" type="button" class="btn btn-success" onclick="editModel()"><i
                        class="fa fa-pencil-square-o"></i> 编辑
                </button>
                <button id="btnDel" type="button" class="btn btn-danger" onclick="delModel()">
                    <i class="fa fa-trash"></i>&nbsp;&nbsp;<span class="bold">删除</span>
                </button>
            </div>

            <div class="form-group">
                <div class="input-group">
                    <input id="txtSearchKey" type="text" class="input form-control" placeholder="搜索关键字"/>
                    <span class="input-group-btn">
                        <button id="btnSearch" class="btn btn btn-primary" type="button"> <i class="fa fa-search"></i> 搜索</button>
                    </span>
                </div>
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