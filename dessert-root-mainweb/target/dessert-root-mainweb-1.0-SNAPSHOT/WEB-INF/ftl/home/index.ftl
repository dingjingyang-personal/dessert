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

        #div1 {
            width: 100%;
            height: 100%
        }

        #div2 {

            height: 50px;
            width: 100%;
            background-color: #6b86b8;
        }

        #div3 {

            overflow-y: auto;
            overflow-x: hidden;
            height: 620px
        }

        #div4 {
            float:left;
            height: 620px;
            background-color: #1b6d85;
        }

        #div5{
            height: 620px;
            background-color: #2b542c;
        }

        .content {
            width: 220px;
            margin: auto, auto;
        }

        .filterinput {
            background-color: rgba(57, 146, 208, 0);
            border-radius: 15px;
            width: 90%;
            height: 30px;
            border: thin solid #FFF;
            text-indent: 0.5em;
            font-weight: bold;
            color: #FFF;
        }

        #demo-list a {
            overflow: hidden;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
            white-space: nowrap;
            width: 100%;
        }

    </style>

    <script type="text/javascript">

        jQuery(document).ready(function () {
            jQuery("#jquery-accordion-menu").jqueryAccordionMenu();

        });

        $(function () {
            //顶部导航切换
            $("#demo-list li").click(function () {
                $("#demo-list li.active").removeClass("active")
                $(this).addClass("active");
            })
        })


    </script>

</head>

<body>
<#escape x as x?html>

<div id="div1">
<#---->
    <div id="div2">



    </div>

    <div id="div3">
        <div id="div4" class="content">

            <div id="jquery-accordion-menu" class="jquery-accordion-menu red">
                <ul id="demo-list">

                    <li class="active"><a href="#">Home </a></li>
                    <li><a href="#">Events </a></li>
                    <li><a href="#">Gallery </a><span
                            class="jquery-accordion-menu-label">
				12 </span></li>
                    <li><a href="#">Services </a>
                        <ul class="submenu">
                            <li><a href="#">Web Design </a></li>
                            <li><a href="#">Hosting </a></li>
                            <li><a href="#">Design </a>
                                <ul class="submenu">
                                    <li><a href="#">Graphics </a></li>
                                    <li><a href="#">Vectors </a></li>
                                    <li><a href="#">Photoshop </a></li>
                                    <li><a href="#">Fonts </a></li>
                                </ul>
                            </li>
                            <li><a href="#">Consulting </a></li>
                        </ul>
                    </li>
                    <li><a href="#">系统管理 </a></li>
                    <li><a href="#">Portfolio </a>
                        <ul class="submenu">
                            <li><a href="#">Web Design </a></li>
                            <li><a href="#">Graphics </a><span class="jquery-accordion-menu-label">10 </span>
                            </li>
                            <li><a href="#">Photoshop </a></li>
                            <li><a href="#">Programming </a></li>
                        </ul>
                    </li>
                    <li><a href="#">About </a></li>
                    <li><a href="#">Contact </a></li>

                </ul>
                <div class="jquery-accordion-menu-footer">
                    Footer
                </div>
            </div>
        </div>


        <div id="div5">

        </div>

    </div>


</div>



</#escape>



</body>

</html>