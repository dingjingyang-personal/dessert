<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["page/index/css/jquery-accordion-menu.css"] />
<@includeRes resUrl=["page/index/js/jquery-accordion-menu.js"] />
    <title>主页</title>

    <style type="text/css">

        * {
            box-sizing: border-box;
            -moz-box-sizing: border-box;
            -webkit-box-sizing: border-box;
        }

        body {
            background: #f0f0f0;
            overflow: hidden;
        }

        #top {
            width: 100%;
            padding:0;
            margin:0;
            border-width: 0;
            border-radius: 0;
            height: 50px;
            background-color: #1b586f;
            color: #ffffff;

        }

        #underColumn {
            width: 100%;
            height: 620px
        }

        #sidebar {
            float: left;
            overflow-y: auto;
            overflow-x: hidden;
            height: 100%;
            background-color: #2F4050;
        }

        .content {
            width: 200px;
            margin: auto, auto;
        }

        .footer {
            background: none repeat scroll 0 0 #f0f0f0;
            border-top: 1px solid #b0b0b0;
            overflow: hidden;
            padding: 10px 30px;
            margin: 0 -35px;
            height: 36px;
        }

        a {
            color: #95f9f6;
            text-decoration: none;
            cursor: pointer;
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
            var position = menu.grandfather.menuname + ' <i class="fa fa-angle-double-right"></i> ' + menu.parent.menuname + ' <i class="fa fa-angle-double-right"></i> ' + menu.menuname;
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
    <div class="app-header navbar" id="top">
        <div class="navbar-header bg-dark" style="float: left">

            <div class="navbar-brand text-lt" style="width: 200px">
                <i class="fa fa-envira"></i>
                <span class="hidden-folded m-l-xs">DESSERT</span>
            </div>
        </div>


        <div style="margin-top:15px;width:40%;float: left" >
            <span>当前位置 : </span><span id="position"></span>

        </div>


    <#--<div class="nav navbar-nav hidden-xs">-->
    <#--<div  class="btn no-shadow navbar-btn" data-toggle="class:app-aside-folded" data-target=".app">-->
    <#--<i class="fa fa-dedent fa-fw text" ></i>-->
    <#--<i class="fa fa-indent fa-fw text-active"></i>-->
    <#--</div>-->
    <#--<div  class="btn no-shadow navbar-btn" data-toggle="class:show" data-target="#aside-user">-->
    <#--<i class="icon-user fa-fw"></i>-->
    <#--</div>-->
    <#--</div>-->

        <div class="" style="float: right;width: 15%" >
            <a class="btn  navbar-btn">
                <i class="fa fa-user"></i> 管理员
            </a>
            <a class="btn ">
                <i class="fa fa-sign-out"></i> 退出
            </a>
        </div>

    </div>


</div>

<div id="underColumn">
<#--侧边栏-->
    <div id="sidebar" class="content">

    <#--菜单栏-->
        <div id="jquery-accordion-menu" class="jquery-accordion-menu red">
            <ul id="menu-list">

            </ul>

        </div>
    </div>


<#--菜单连接页面-->
    <div id="pageBody"
         style="height: 100%;padding: 0 0px;position: inherit;margin: 0 0 0 220px;min-height: auto;background-color: #f3f3f4;">
    <#--<div style="height: 30px;width: calc(100% -200px); margin-right: -15px;margin-left: -15px;">-->
    <#--<div style="font-size:12px;padding-top: 8px">-->
    <#--<span>当前位置 : </span><span id="position"></span>-->
    <#--</div>-->
    <#--</div>-->
        <div style="height: calc(100% - 75px);overflow: hidden;margin-right: -5px;margin-left: -15px;">
            <iframe id="pagebodyiframe" frameborder=0 scrolling="auto" width="100%" height="99%" src=""
            ></iframe>
        </div>
        <div class="footer">
            <div class="pull-right" style="float: right !important;">
                © 2016 - 2016 <a href="" target="_blank">©ding</a>
            </div>
        </div>
    </div>


</div>


</div>



</#escape>

<link rel="stylesheet" type="text/css" href="${resPath}/plugins/font-awesome-4.6.3/css/font-awesome.min.css">
</body>

</html>