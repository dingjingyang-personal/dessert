<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["common/index/css/font-awesome.css","common/index/css/jquery-accordion-menu.css"] />
<@includeRes resUrl=["common/index/js/jquery-accordion-menu.js"] />
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

            height: 50px;
            width: 100%;
            background-color: #1b586f;
        }

        #underColumn {
            width: 100%;
            height: 620px
        }

        #sidebar {
            float: left;
            overflow-y: auto;
            overflow-x: hidden;
            height: 620px;
            background-color: #2F4050;
        }

        /*#pageBody {*/
            /*float: left;*/
            /*height: 620px;*/
            /*width: 1150px;*/
            /*background-color: #f0f0f0;*/
        /*}*/

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
            if(menu){
                var rootMenuid = $("#menu-list");
                var nodes = $("#"+menu.menuid);
                if(nodes.length==0){
                    rootMenuid.append(
                            '<li id='+ menu.menuid+'><a href="#">' + menu.menuname + '</a>' +
                            '<ul class="submenu" id='+menu.menuid+'ul'+'>'+'</ul>'+
                            '</li>'
                    )
                }
            }
        }

        //添加二级目录
        function addparent(menu) {
            if(menu){
                var parentMenuid = $("#"+menu.parentid+"ul");
                var nodes = $("#"+menu.menuid);
                if(nodes.length==0){
                    parentMenuid.append(
                            '<li id='+ menu.menuid+'><a href="#">' + menu.menuname + '</a>' +
                            '<ul class="submenu" id='+menu.menuid+'ul'+'>'+'</ul>'+
                            '</li>'
                    )
                }
            }
        }

        //添加三级目录
        function addmenu(menu) {
            var parentMenuid = $("#"+menu.parentid+"ul");
            var nodes = $("#"+menu.menuid);
            if(nodes.length==0){
                parentMenuid.append(
                        '<li id='+ menu.menuid+'><a href="javascript:void(0)" onclick="clilkMenu('+menu.menuid+')">' + menu.menuname + '</a></li>'
                )
            }
        }

        //点击菜单事件
        function clilkMenu(menuid) {
            var menu = menuMap[menuid];
            var position =menu.grandfather.menuname+' > '+menu.parent.menuname+'> '+menu.menuname ;
            $("#position").html(position);
            var pagebodyiframe = $("#pagebodyiframe");
            pagebodyiframe.attr('src',"${ctxPath}"+menu.action);
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




    </script>

</head>

<body>
<#escape x as x?html>

<div id="pagebody">
    <#--信息栏-->
    <div id="top">


    </div>

    <div id="underColumn">
        <#--侧边栏-->
        <div id="sidebar" class="content">

            <#--菜单栏-->
            <div id="jquery-accordion-menu" class="jquery-accordion-menu red">
                <ul id="menu-list">

                </ul>
                <#--<div class="jquery-accordion-menu-footer">-->
                    <#--Footer-->
                <#--</div>-->
            </div>
        </div>


        <#--菜单连接页面-->
        <div id="pageBody" style="height: 100%;padding: 0 15px;position: inherit;margin: 0 0 0 220px;min-height: auto;background-color: #f3f3f4;">
            <div style="height: 30px;width: calc(100% -200px); margin-right: -15px;margin-left: -15px;">
                <div style="font-size:12px;padding-top: 8px">
                    <span>当前位置 : </span><span id="position"></span>
                </div>
            </div>
            <div style="height: calc(100% - 75px);overflow: hidden;margin-right: -5px;margin-left: -15px;">
                <iframe id="pagebodyiframe" frameborder=0 scrolling="auto" width="100%" height="98%" src=""  "></iframe>
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


</body>

</html>