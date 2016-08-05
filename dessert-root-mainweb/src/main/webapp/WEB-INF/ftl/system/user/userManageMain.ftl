<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title>用户管理</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


        $(function () {
            pageInit();
        });

        function pageInit() {
            jQuery("#pagelist").jqGrid({
                url: "${ctxPath}/system/user/findUsersJson.htm",
                mtype: "POST",
                styleUI: 'Bootstrap',
                viewrecords: true,
                emptyrecords: "没有查询到相关数据!",
                datatype: "json",
                caption: "用户列表",
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


                colNames: ["userid", "用户名", "性别", "状态", "邮箱","创建时间"],
                colModel: [
                    {name: "userid", hidden: true, key: true},
                    {name: "username", width: 100, sortable: false},
                    {
                        name: "sex", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['sex'] == "1" ? "男" : "女";
                        }
                    },
                    {
                        name: "status", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['status'] == "1" ? "有效" : "无效";
                        }
                    },
                    {name: "email", width: 200, sortable: false, editable: false},
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
            var url = "${ctxPath}/system/user/addOrUpdateUserPage.htm";
            layer.open({
                id:"addOrUpdateUserPage",
                title: '添加用户',
                type: 2,
                closeBtn: 1,
                area: ['500px', '550px'],
                content:['','no'],
                data:{url: url, data: {}},
            });
        }


        //编辑
        function editModel() {
            var row = getGridData("pagelist");
            if (row != null) {
                var url = "${ctxPath}/system/user/addOrUpdateUserPage.htm";
                var userid = row.userid;
                layer.open({
                    id:"addOrUpdateUserPage",
                    title: '修改用户',
                    type: 2,
                    closeBtn: 1,
                    area: ['500px', '550px'],
                    content:['','no'],
                    data:{url: url, data: {userid: userid}},
                });
            } else {
                layer.msg('请选择一条数据!');
            }
        }


        function delModel() {

            var row = getGridData("pagelist");
            if (row != null) {
                var userid = row.userid;
                var url = '${ctxPath}/system/user/deleteUser.htm';
                var data = {};
                data.userid = userid;
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


        /**
         * 分配角色页面
         */
        function assigningRoles() {

            var row = getGridData("pagelist");
            if (row != null) {
                var url = "${ctxPath}/system/user/assigningRolesPage.htm";
                var userid = row.userid;
                layer.open({
                    id:"assigningRoles",
                    title: '分配角色',
                    type: 2,
                    closeBtn: 1,
                    area: ['860px', '550px'],
                    content:['','no'],
                    data:{url: url, data: {userid: userid}},
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
        <div class="ibox-title">
            <h5>用户管理</h5>

        </div>
        <div class="ibox-content">
            <div class="form-group">
                <button id="btnAdd" type="button" class="btn btn-primary" onclick="addModel()"><i
                        class="fa fa-plus"></i>&nbsp;&nbsp;添加
                </button>
                <button id="btnEdit" type="button" class="btn btn-success" onclick="editModel()"><i
                        class="fa fa-pencil-square-o"></i>&nbsp;&nbsp;编辑
                </button>

                <button id="btnDel" type="button" class="btn btn-danger" onclick="delModel()">
                    <i class="fa fa-trash"></i>&nbsp;&nbsp;删除
                </button>
                <button id="btnEdit" type="button" class="btn btn-info" onclick="assigningRoles()"><i
                        class="fa fa-th-list"></i>&nbsp;&nbsp;分配角色
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