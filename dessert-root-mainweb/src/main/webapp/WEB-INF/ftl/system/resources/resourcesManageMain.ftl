<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />


    <title>资源管理</title>

    <style type="text/css">


        /*.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default, .ui-button, html .ui-button.ui-state-disabled:hover, html .ui-button.ui-state-disabled:active {*/
        /*border: 1px solid #28a4c9;*/
        /*height: 35px;*/
        /*text-align: center;*/
        /*}*/

        .ui-jqgrid-bdiv {
            overflow: hidden !important
        }


        /*.ui-widget.ui-widget-content {*/
        /*border: 0px*/
        /*}*/

        /*.ui-jqgrid .ui-jqgrid-btable {*/
        /*border-left: 1px solid;*/
        /*}*/

        /*.ui-jqgrid{width:auto;}*/
        /*.ui-jqgrid TR.jqgrow TD {padding:0 0 0 10px;border:1px solid #e3e3e3;word-break:break-all;height:30px;}*/
        /*.ui-widget-content{background:none;border:none}*/
        /*.ui-widget{font:12px/1.5 arial,tahoma,宋体,color:#333}*/
        /*.ui-jqgrid .ui-jqgrid-view{font-size:13px;}*/
        /*tr{height:28px;}*/
        /*tr:hover{background:#f5f5f5;}*/
        /*.manage-panelBd{padding:5px;}*/
        /*.ui-widget-content .ui-state-default{border-bottom:0px;background:none}*/
        /*.ui-jqgrid .ui-jqgrid-htable TH.ui-th-ltr{}*/
        /*.ui-jqgrid .ui-jqgrid-htable TH.ui-th-column{border-left:1px solid #e3e3e3;border-top:1px solid #e3e3e3;border-right:1px solid #e3e3e3;padding-right:3px;}*/
        /*.ui-jqgrid .ui-jqgrid-htable TH{height:30px;padding-left:0px;}*/
        /*.ui-jqgrid-bdiv{overflow:hidden!important}*/
        /*.ui-jqgrid{margin:0 auto!important;}*/


    </style>

    <script type="text/javascript">
        function parseMenu(pid, menuChild, menusArray) {
            var temp;
            var arr;
            for (var i = 0; i < menusArray.length; i++) {
                var temp = menusArray[i];
                if (temp.parentid == pid) {
                    if (temp.action) {
                        menuChild.push(temp);
                        continue;
                    }
                    arr = [];
                    parseMenu(temp.menuid, arr, menusArray);
                    temp.child = arr;
                    menuChild.push(temp);
                }
            }
        }
        var selectMenuId;
        $(document).ready(function () {
            var topicjson = {
                        "response": getMenuTree()
                    },
                    grid;


            var lastsel;
            grid = jQuery("#treegrid");
            grid.jqGrid({
                styleUI: 'Bootstrap',
                viewrecords: true,
                datastr: topicjson,
                datatype: "jsonstring",
                caption : "菜单列表",
                autowidth:true,
                height: "auto",
                loadui: "disable",
                treeGrid: true,
                treeGridModel: "adjacency",
                ExpandColumn: "menuname",
                rowNum: 10000,
                treeIcons: {leaf: 'ui-icon-document-b'},
                ExpandColClick: true,
                jsonReader: {
                    repeatitems: false,
                    root: "response"
                },
                colNames: ["id", "菜单名称", "类型", "序号", "状态", "页面链接"],
                colModel: [
                    {name: "id", hidden: true, key: true},
                    {name: "menuname", width: 200, sortable: false},
                    {
                        name: "menutype", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['menutype'] == "1" ? "菜单" : "按钮";
                    }
                    },
                    {
                        name: "menuorder", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return '<span mid="' + r['menuid'] + '" pid="' + r['parentid'] + '">' + v + '</span>';
                    }
                    },
                    {
                        name: "status", width: 60, sortable: false, editable: false, formatter: function (v, x, r) {
                        return r['status'] == "1" ? "有效" : "无效";
                    }
                    },
                    {name: "action", width: 400, sortable: false, editable: false}
                ],
                treeReader: {
                    level_field: "level",
                    parent_id_field: "parentid",
                    leaf_field: "isLeaf",
                    expanded_field: "expanded"
                },
                onSelectRow: function (id) {
                    selectMenuId = id;
                },
                pager: "#jqGridPager"
            });
            $('.tree-minus,.tree-plus').click(function () {
                resizeParentHeight();
            });

        });

        function appendArr(arr, obj) {
            arr.push(obj);
            if (!obj.child || obj.child.length == 0) {
                return;
            }
            for (var i = 0; i < obj.child.length; i++) {
                appendArr(arr, obj.child[i]);
            }
        }
        var menuMap, menuRoot;
        function getMenuTree() {
            var menuTree = ${(resourcesList)!''};
            menuRoot = [];
            var allMenus = [];
            menuMap = menuTreeToMap(menuTree);
            parseMenu(0, menuRoot, menuTree);
            for (var k = 0; k < menuRoot.length; k++) {
                appendArr(allMenus, menuRoot[k]);
            }
            menuTree = allMenus;
            var menu;
            for (var i = 0; i < menuTree.length; i++) {
                menu = menuTree[i];
                menu.id = menu.menuid + "";
                if (menu.parentid == 0) {
                    menu.parentid = "";
                } else {
                    menu.parentid = menu.parentid + "";
                }
                if (menu.action) {
                    menu.isLeaf = true;
                } else {
                    menu.isLeaf = false;
                }
                menu.level = menu.menulevel - 1;
                menu.loaded = false;
                menu.expanded = true;
                menu.parent = menu.parentid;
            }
            return menuTree;
        }

        function menuTreeToMap(Tree) {
            var temp = {};
            var menu;
            for (var i = 0; i < Tree.length; i++) {
                menu = Tree[i];
                temp[menu.menuid] = menu;
            }
            return temp;
        }
        //打开新增菜单页面
        function createmenu(flag) {
            if (!selectMenuId) {
                selectMenuId = 0;
                var root = {menuid: 0, menucode: 0, menulevel: 0};
                menuMap[0] = root;
                //alert("请选择！");
                //return;
            }
            var menu = menuMap[selectMenuId];
            //var supmenucode = menu.menucode;
            var count = 0;
            for (var map in menuMap) {
                if (menu.menuid == menuMap[map].parentid) {
                    count++;
                }
            }
            if (menu.menulevel == 3 && flag == "A") {
                alert("链接节点不能新增菜单");
                return;
            }
            var url = "${ctxPath}/menumanager/toAddMenuPage.action";
            parent.showDlg({
                url: url,
                title: (flag == "A" ? "新增" : "修改"),
                width: 460,
                height: (flag == "A" ? 360 : 380),
                id: "createMenuPager",
                data: {supmenuid: menu.menuid, flag: flag, menucount: count}
            });
        }

        //打开菜单权限管理页面
        function influenceInquiry() {
            if (!selectMenuId) {
                selectMenuId = 0;
                var root = {menuid: 0, menucode: 0, menulevel: 0};
                menuMap[0] = root;
                //alert("请选择！");
                //return;
            }
            var menu = menuMap[selectMenuId];

            if (menu.menulevel != 3) {
                alert("链接节点无法管理权限");
                return;
            }
            var url = "${ctxPath}/menumanager/influenceInquiry.action";
            parent.showDlg({
                url: url,
                title: "权限管理",
                width: 680,
                height: 500,
                id: "influenceInquiry",
                data: {menuid: menu.menuid}
            });
        }

        function resizeParentHeight() {
            if (parent && parent.resizeHeight) {
                parent.resizeHeight();
            }
        }
        function putAllMenuId(menu, menuArr) {
            menuArr.push(menu.menuid);
            if (!menu.child || menu.child.length == 0) return;
            for (var i = 0; i < menu.child.length; i++) {
                putAllMenuId(menu.child[i], menuArr);
            }
        }
        function getChild(menuid, arr) {
            if (!arr || arr.length == 0) return;
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].menuid == menuid) {
                    return arr[i];
                }
                var temp = getChild(menuid, arr[i].child)
                if (temp)
                    return temp;
            }
            return 0;
        }
        function getAllMoveMenu(menuid) {
            var temp = getChild(menuid, menuRoot);
            if (!temp) return 0;
            var menuidArr = [];
            putAllMenuId(temp, menuidArr);
            return menuidArr;
        }
        function moveMenu(flag) {
            if (!selectMenuId) {
                alert('请选择菜单');
                return;
            }
            var menu = menuMap[selectMenuId];
            var menupid = menu.parentid;
            var menuid = menu.menuid;
            var menuorder = menu.menuorder;
            if (flag == "up" && menuorder == 10) {
                alert("不可向上移动");
                return;
            }
            var spanList = $("span[pid='" + menupid + "']");
            var len = spanList.length;
            if (len == 1) {
                alert("不可移动");
            }
            var curIndex;
            for (var i = 0; i < len; i++) {
                if (menuid == spanList.eq(i).attr('mid')) {
                    curIndex = i;
                    break;
                }
            }
            var id;
            var order;
            if (flag == "up") {
                id = spanList.eq(curIndex - 1).attr('mid');
                order = spanList.eq(curIndex - 1).html();
            }

            if (flag == "down") {
                id = spanList.eq(curIndex + 1).attr('mid');
                order = spanList.eq(curIndex + 1).html();
            }

            if (flag == "down" && (curIndex + 1) == len) {
                alert("不可向下移动");
                return;
            }
            var tr1 = $('tr[id="' + menuid + '"]');
            var tr2 = $('tr[id="' + id + '"]');
            var tr1Menus = getAllMoveMenu(menuid);
            var tr2Menus = getAllMoveMenu(id);
            if (!tr1Menus || !tr2Menus) return;

            var datas = {"menuid": menuid, "menuorder": order, "movemenuid": id, "movemenuorder": menuorder};
            var url = "${ctxPath}/menumanager/updateMenuOrder.action";
            ajaxEx({
                type: "post",
                async: false,
                data: datas,
                cache: false,
                url: url, isText: true,
                success: function (result) {
                    if (result == "true") {
                        tr1.children().eq(3).find('span').html(order);
                        tr2.children().eq(3).find('span').html(menuorder);
                        menuMap[menuid].menuorder = order;
                        menuMap[id].menuorder = menuorder;
                        tr1 = $('#' + tr1Menus.join(',#'));
                        tr2 = $('#' + tr2Menus.join(',#'));
                        if (flag == "down")
                            tr1.insertAfter(tr2[tr2.length - 1]);
                        else {
                            tr1.insertBefore(tr2[0]);
                        }
                        // parent.reloadPageWindow();
                    } else {
                        alert('操作失败');
                    }
                }
            });

        }

        function deleteMenu(flag) {

            if (!confirm("确认要删除？")) {
                window.event.returnValue = false;
                return;
            }

            if (!selectMenuId) {
                selectMenuId = 0;
                var root = {menuid: 0, menucode: 0, menulevel: 0};
                menuMap[0] = root;
                //alert("请选择！");
                //return;
            }
            var menu = menuMap[selectMenuId];
            //var supmenucode = menu.menucode;
            var count = 0;
            for (var map in menuMap) {
                if (menu.menuid == menuMap[map].parentid) {
                    count++;
                }
            }
            if (menu.menulevel == 1 && flag == "D") {
                alert("此链接节点不能删除");
                return;
            }
            if (menu.menulevel == 2 && flag == "D") {
                alert("此链接节点不能删除");
                return;
            }
            var url = "${ctxPath}/menumanager/deleteMenu.action";

            ajaxEx({
                url: url,
                isText: true,
                data: {supmenuid: menu.menuid, flag: flag, menucount: count},
                success: function (data) {
                    if (data == "1") {
                        alert("删除成功");
                        parent.reloadPageWindow();
                        parent.closeDlg();
                    }
                    else {
                        showMessage({text: data, top: 5});
                    }
                }
            });
        }


        //关闭弹框
        function closeAgreeWin(obj) {
            closeWin($("#" + obj));
        }
        function inquiryRefresh() {
            $('#frmPage').submit();
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
                        class="fa fa-plus"></i>&nbsp;添加
                </button>
                <button id="btnEdit" type="button" class="btn btn-success" onclick="editModel()"><i
                        class="fa fa-pencil-square-o"></i> 编辑
                </button>
                <button id="btnDel" type="button" class="btn btn-danger" onclick="delData()">
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

            <div class="jqGrid_wrapper" >
                <table id="treegrid"></table>
                <div id="jqGridPager"></div>
            </div>
        </div>
    </div>
</div>








</#escape>
</body>

</html>