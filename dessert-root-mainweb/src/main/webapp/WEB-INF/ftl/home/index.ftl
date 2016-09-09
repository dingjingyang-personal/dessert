<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["page/index/css/jquery-accordion-menu.css"] />
<@includeRes resUrl=["page/index/js/jquery-accordion-menu.js"] />
    <title>主页</title>

    <style type="text/css">


        html, body {
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
        }

        #page {
            width: 100%;
            height: 100%;
        }

        #top {
            height: 50px;
            width: 100%;
            color: #ffffff;
            background-color: #1b586f;
        }

        #logo {
            float: left;
            width: 200px;
            font-size: 20px;
            margin-top: 10px;
            margin-left: 10px;
        }

        #positionF {
            float: left;
            margin-top: 15px;
        }

        #userinfo {
            float: right;
        }

        #underColumn {
            height: calc(100% - 50px);
            width: 100%;
        }

        #sidebar {
            width: 200px;
            height: 100%;
            float: left;
            background-color: #2F4050
        }

        #pageBody {
            float: left;
            width: calc(100% - 200px);
            height: 100%;
        }

        #pageMain {
            float: left;
            width: 100%;
            height: calc(100% - 30px);
            margin: 0px;
        }

        #footer {
            width: 100%;
            height: 30px;
            clear: both;
            background-color: #e9e9e9;
        }

        a {
            color: #ffffff;
        }

        /* 未访问的链接 */
        a:link {
            color: #ffffff
        }

        /* 已访问的链接 */
        a:visited {
            color: #ffffff
        }

        /* 鼠标移动到链接上 */
        a:hover {
            color: #ffffff
        }

        /* 选定的链接 */
        a:active {
            color: #ffffff
        }


    </style>

    <script type="text/javascript">

        var menuList =${resourcesByUserListStr!0};
        var menuMap;


        jQuery(document).ready(function () {
            menuMap = menuTreeToMap(menuList);
            assemblyMenu();
            jQuery("#jquery-accordion-menu").jqueryAccordionMenu();

        });

        $(function () {
            $("#menu-list li").click(function () {
                $("#menu-list li.active").removeClass("active")
                $(this).addClass("active");
            });
        })


        //整理菜单
        function assemblyMenu() {
            for (var i = 0; i < menuList.length; i++) {
                var temp = menuList[i];
                var grandfather = temp.grandfather;
                var parent = temp.parent;
                addGrandfather(grandfather);
                addparent(parent);
                addmenu(temp);
            }
        }

        //添加一级目录
        function addGrandfather(menu) {
            if (menu) {
                var rootMenuid = $("#menu-list");
                var nodes = $("#" + menu.menuid);
                if (nodes.length == 0) {
                    rootMenuid.append(
                            '<li id=' + menu.menuid + '><a href="#"><i class="' + menu.menuicon + '"></i>' + menu.menuname + '</a>' +
                            '<ul class="submenu" id=' + menu.menuid + 'ul' + '>' + '</ul>' +
                            '</li>'
                    )
                }
            }
        }

        //添加二级目录
        function addparent(menu) {
            if (menu) {
                var parentMenuid = $("#" + menu.parentid + "ul");
                var nodes = $("#" + menu.menuid);
                if (nodes.length == 0) {
                    parentMenuid.append(
                            '<li id=' + menu.menuid + '><a href="#"><i class="' + menu.menuicon + '"></i>' + menu.menuname + '</a>' +
                            '<ul class="submenu" id=' + menu.menuid + 'ul' + '>' + '</ul>' +
                            '</li>'
                    )
                }
            }
        }

        //添加三级目录
        function addmenu(menu) {
            var parentMenuid = $("#" + menu.parentid + "ul");
            var nodes = $("#" + menu.menuid);
            if (nodes.length == 0) {
                parentMenuid.append(
                        '<li id=' + menu.menuid + '><a href="javascript:void(0)" onclick="clilkMenu(' + menu.menuid + ')"><i class="' + menu.menuicon + '"></i>' + menu.menuname + '</a></li>'
                )
            }
        }

        //点击菜单事件
        function clilkMenu(menuid) {
            var menu = menuMap[menuid];
            var position = menu.grandfather.menuname + ' / ' + menu.parent.menuname + ' / ' + menu.menuname;
            $("#position").html(position);
            var pagebodyiframe = $("#pagebodyiframe");
            pagebodyiframe.attr('src', "${ctxPath}" + menu.action);
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


        function getPageWindow() {
            return getIframeWindow("pagebodyiframe");
        }
        function reloadPageWindow() {
            var win = getPageWindow();
            if (win) {
                win.location.reload();
            }
            win = null;
        }


    </script>

</head>

<body>
<#escape x as x?html>

<div id="page">
<#--信息栏-->
    <div id="top">
        <div id="logo" class="">
            <i class="fa fa-envira"></i>
            <span class="">DESSERT</span>
        </div>


        <div id="positionF">
            <span>当前位置 : </span><span id="position"></span>

        </div>


        <div id="userinfo" style="margin-top: 15px;margin-right: 20px">
            <a>
                <i class="fa fa-user"></i>&nbsp;<span id="username">${USER_KEY.username}</span>
            </a>
            &nbsp; &nbsp; &nbsp;

            <a href="${ctxPath}/home/loginOut.htm">
                <i class="fa fa-sign-out"></i>&nbsp;<span>退出</span>
            </a>


        <#--<div class="dropdown"  style="margin-top: 15px;margin-right: 30px">-->
        <#--<div data-toggle="dropdown">-->
        <#--<i class="fa fa-user"></i>-->
        <#--<span >管理员</span>-->
        <#--<i class="fa fa-angle-down"></i>-->
        <#--</div>-->
        <#--<ul class="dropdown-menu" role="menu" style="position: absolute;" >-->
        <#--<li><a href="#"><span class="fa fa-wrench" style="margin-right: 15px"></span>设置</a></li>-->
        <#--<li class="divider"></li>-->
        <#--<li><a href="#"><span class="fa fa-paper-plane-o" style="margin-right: 15px"></span>退出</a></li>-->
        <#--</ul>-->
        <#--</div>-->




        </div>

    </div>


    <div id="underColumn">
    <#--侧边栏-->
        <div id="sidebar">

        <#--菜单栏-->
            <div id="jquery-accordion-menu" class="jquery-accordion-menu red">
                <ul id="menu-list">

                </ul>

            </div>
        </div>


        <div id="pageBody">
        <#--菜单连接页面-->
            <div id="pageMain">
                <iframe id="pagebodyiframe" frameborder=0 scrolling="auto" height="100%" width="100%" src=""></iframe>
            </div>
        <#--底栏-->
            <div id="footer">
                <div class="pull-right" style="float: right;margin-top: 8px;margin-right: 20px">
                    © 2016 - 2016 <a href="#" target="_blank" style="color: #0a68b4">© DING</a>
                </div>
            </div>
        </div>


    </div>


</div>



</#escape>

<link rel="stylesheet" type="text/css" href="${resPath}/plugins/font-awesome/css/font-awesome.min.css">
</body>

</html>