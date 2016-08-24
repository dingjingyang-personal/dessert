<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />


    <title>资源管理</title>

    <style type="text/css">




    </style>

    <script type="text/javascript">

        $(document).ready(function () {
            var topicjson = {
                        "response": getMenuTree()
                    },

                    grid = jQuery("#pagelist");
            grid.jqGrid({
                styleUI: 'Bootstrap',
                viewrecords: true,
                datastr: topicjson,
                datatype: "jsonstring",
                caption: "菜单列表",
                height: "auto",
                autowidth: true,
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

        });

        var selectMenuId;
        var menuMap;
        var menuRoot;
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

        function parseMenu(pid, menuChild, menusArray) {
            var temp;
            var arr;
            for (var i = 0; i < menusArray.length; i++) {
                temp = menusArray[i];
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


        function menuTreeToMap(Tree) {
            var temp = {};
            var menu;
            for (var i = 0; i < Tree.length; i++) {
                menu = Tree[i];
                temp[menu.menuid] = menu;
            }
            return temp;
        }

        function appendArr(arr, obj) {
            arr.push(obj);
            if (!obj.child || obj.child.length == 0) {
                return;
            }
            for (var i = 0; i < obj.child.length; i++) {
                appendArr(arr, obj.child[i]);
            }
        }

        //打开新增菜单页面
        function createmenu(flag) {
            if(flag=="M"){
                var row = getGridData("pagelist");
                if(row==null){
                    layer.msg('请选择一条数据!');
                    return;
                }
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
            if (menu.menulevel == 3 && flag == "A") {
                layer.msg('链接节点不能新增菜单!');
                return;
            }
            var url = "${ctxPath}/system/resources/addOrUpdateResourcesPage.htm";


            parent.layer.open({
                id:"addOrUpdateResourcesPage",
                title: (flag == "A" ? "新增" : "修改"),
                type: 2,
                area: ['550px', '450px'],
                content:['','no'],
                data:{url: url, data: {supmenuid: menu.menuid, flag: flag, menucount: count}},
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


        /**
         * 移动菜单
         * @param flag
         */
        function moveMenu(flag) {
            if (!selectMenuId) {
                layer.msg('请选择一条数据!');
                return;
            }
            var menu = menuMap[selectMenuId];
            var menupid = menu.parentid;
            var menuid = menu.menuid;
            var menuorder = menu.menuorder;
            if (flag == "up" && menuorder == 10) {
                layer.msg('不可向上移动');
                return;
            }
            var spanList = $("span[pid='" + menupid + "']");
            var len = spanList.length;
            if (len == 1) {
                layer.msg('不可移动');
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
                layer.msg('不可向下移动');
                return;
            }
            var tr1 = $('tr[id="' + menuid + '"]');
            var tr2 = $('tr[id="' + id + '"]');
            var tr1Menus = getAllMoveMenu(menuid);
            var tr2Menus = getAllMoveMenu(id);
            if (!tr1Menus || !tr2Menus) return;

            var datas = {"menuid": menuid, "menuorder": order, "movemenuid": id, "movemenuorder": menuorder};
            var url = "${ctxPath}/system/resources/updateMenuOrder.htm";
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
                        layer.alert('系统异常,请稍后重试!', {icon: 5});
                    }
                }
            });

        }


        /**
         * 删除
         * @param flag
         */
        function deleteMenu(flag) {


            if (!selectMenuId) {
                layer.msg('请选择一条数据!');
                return;
            }

            layer.confirm('确定要删除此条数据？', {
                btn: ['确定','取消'] //按钮
            }, function(){



                var menu = menuMap[selectMenuId];
                //var supmenucode = menu.menucode;
                var count = 0;
                for (var map in menuMap) {
                    if (menu.menuid == menuMap[map].parentid) {
                        count++;
                    }
                }
                if (menu.menulevel == 1 && flag == "D") {
                    layer.msg('此链接节点不能删除');
                    return;
                }
                if (menu.menulevel == 2 && flag == "D") {
                    layer.msg('此链接节点不能删除');
                    return;
                }
                var url = "${ctxPath}/system/resources/deleteResources.htm";

                ajaxEx({
                    url: url,
                    isText: true,
                    data: {menuid: menu.menuid, flag: flag, menucount: count},
                    success: function (data) {
                        if (data == "1") {
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
                        else {
                            layer.alert(data, {icon: 5});
                        }
                    }
                });






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
            <h5>资源管理</h5>

        </div>
        <div class="ibox-content">
            <div class="form-group">
                <button id="btnAdd" type="button" class="btn btn-primary" onclick="createmenu('A')"><i
                        class="fa fa-plus"></i>&nbsp;&nbsp;<span>添加</span>
                </button>
                <button id="btnEdit" type="button" class="btn btn-success" onclick="createmenu('M')"><i
                        class="fa fa-pencil-square-o"></i>&nbsp;&nbsp;<span>编辑</span>
                </button>
                <button id="btnEdit" type="button" class="btn btn-info" onclick="moveMenu('up')"><i
                        class="fa fa-arrow-up"></i>&nbsp;&nbsp;<span>上移</span>
                </button>
                <button id="btnEdit" type="button" class="btn btn-primary" onclick="moveMenu('down')"><i
                        class="fa fa-arrow-down"></i>&nbsp;&nbsp;<span>下移</span>
                </button>
                <button id="btnDel" type="button" class="btn btn-danger" onclick="deleteMenu('D')">
                    <i class="fa fa-trash"></i>&nbsp;&nbsp;<span>删除</span>
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