<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#assign ctxPath=request.contextPath>

<#--<#include "/common/page/head_inc_t.ftl">-->
<#--<@includeRes resType="css" resUrl=[-->
<#--"plugins/bootstrap/css/bootstrap.css",-->
<#--"page/index1/css/skycons.css",-->
<#--"plugins/font-awesome-4.6.3/css/font-awesome.css",-->
<#--"page/index1/css/pace.css",-->
<#--"page/index1/css/bootkit.css",-->
<#--"page/index1/css/jquery-ui-1.css",-->
<#--"page/index1/css/jquery.css",-->
<#--"page/index1/css/style.css",-->
<#--"page/index1/css/add-ons.css"-->


<#--] />-->

    <link href="${ctxPath}/resources/plugins/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/skycons.css" rel="stylesheet">
    <link href="${ctxPath}/resources/plugins/font-awesome-4.6.3/css/font-awesome.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/pace.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/bootkit.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/jquery-ui-1.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/jquery.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/style.css" rel="stylesheet">
    <link href="${ctxPath}/resources/page/index1/css/add-ons.css" rel="stylesheet">


    <script src="${ctxPath}/resources/common/js/jquery-1.12.3.js"></script>
    <script src="${ctxPath}/resources/page/index1/js/modernizr.js"></script>

    <title>主页</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">



        var menuList =${resourcesByUserListStr!0};
        var menuMap;


        jQuery(document).ready(function () {
            menuMap = menuTreeToMap(menuList);
            assemblyMenu();

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
                    '<li id=' + menu.menuid + '>'+
                    '<a href="#">'+
                    '<i class="' + menu.menuicon + '">'+
                    '<span>' + menu.menuname + '</span>'+
                    '</a>'+
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

                            '<li id=' + menu.menuid + '>'+
                            '<a href="#">'+
                            '<i class="' + menu.menuicon + '">'+
                            '<span>' + menu.menuname + '</span>'+
                            '</a>'+
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
                        '<li id=' + menu.menuid + '><a href="javascript:void(0)" onclick="clilkMenu(' + menu.menuid + ')"><i class="' + menu.menuicon + '"></i><span>' + menu.menuname + '</span></a></li>'
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


<body class="  pace-done">

<#escape x as x?html>
<div class="sidebar mm-menu mm-horizontal mm-offcanvas">
    <div id="mm-0" class="sidebar-collapse mm-panel mm-opened mm-current">
        <!-- Sidebar Header Logo-->
        <div class="sidebar-header">
            <img src="/dessert-root-mainweb/resources/page/index1/images/logo.png" class="img-responsive" alt=""></div>
        <!-- Sidebar Menu-->
        <div style="height: 410px;" class="sidebar-menu">
            <nav id="menu" class="nav-main opened" role="navigation">
                <ul class="nav nav-sidebar" id="menu-list">
                    <div class="panel-body text-center">
                        <div class="flag">
                            <img src="/dessert-root-mainweb/resources/page/index1/images/USA.png" class="img-flags"
                                 alt=""></div>
                    </div>


                </ul>
            </nav>
        </div>
        <!-- End Sidebar Menu--></div>
    <!-- Sidebar Footer-->
    <div id="mm-1" class="sidebar-footer drop-shadow mm-panel mm-hidden">
        <div class="small-chart-visits">
            <div class="small-chart" id="sparklineLineVisits">
                <canvas height="30" width="59"
                        style="display: inline-block; width: 59px; height: 30px; vertical-align: top;"></canvas>
            </div>
            <div class="small-chart-info">
                <label>New Visits</label>
                <strong>70,79%</strong></div>
            <script type="text/javascript">var sparklineLineVisitsData = [15, 16, 17, 19, 15, 25, 23, 35, 29, 15, 30, 45];</script>
        </div>
        <ul class="sidebar-terms bk-margin-top-10">
            <li>
                <a href="#">Terms</a></li>
            <li>
                <a href="#">Privacy</a></li>
            <li>
                <a href="#">Help</a></li>
            <li>
                <a href="#">About</a></li>
        </ul>
    </div>
    <!-- End Sidebar Footer--></div>
<div class="mm-page">
    <div class="pace  pace-inactive">
        <div data-progress="99" data-progress-text="100%" style="width: 100%;" class="pace-progress">
            <div class="pace-progress-inner"></div>
        </div>
        <div class="pace-activity"></div>
    </div>
    <div class="navbar" role="navigation">
        <div class="container-fluid container-nav">
            <!-- Navbar Action -->
            <ul class="nav navbar-nav navbar-actions navbar-left">
                <li class="visible-md visible-lg">
                    <a href="#" id="main-menu-toggle">
                        <i class="fa fa-th-large"></i>
                    </a>
                </li>
                <li class="visible-xs visible-sm">
                    <a href="#" id="sidebar-menu">
                        <i class="fa fa-navicon"></i>
                    </a>
                </li>
            </ul>
            <!-- Navbar Left -->
            <div class="navbar-left">
                <!-- Search Form -->

            </div>
            <!-- Navbar Right -->
            <div class="navbar-right">
                <!-- Notifications -->
                <ul class="notifications hidden-xs">
                    <li>
                        <a href="#" class="dropdown-toggle notification-icon" data-toggle="dropdown">
                            <i class="fa fa-tasks"></i>
                            <span class="badge">10</span></a>
                        <ul class="dropdown-menu update-menu" role="menu">
                            <li>
                                <a href="#">
                                    <i class="fa fa-database bk-fg-primary"></i>Database</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-bar-chart-o bk-fg-primary"></i>Connection</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-bell bk-fg-primary"></i>Notification</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-envelope bk-fg-primary"></i>Message</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-flash bk-fg-primary"></i>Traffic</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-credit-card bk-fg-primary"></i>Invoices</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-dollar bk-fg-primary"></i>Finances</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-thumbs-o-up bk-fg-primary"></i>Orders</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-folder bk-fg-primary"></i>Directories</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-users bk-fg-primary"></i>Users</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#" class="dropdown-toggle notification-icon" data-toggle="dropdown">
                            <i class="fa fa-envelope"></i>
                            <span class="badge">5</span></a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-menu-header">
                                <strong>Messages</strong>
                                <div class="progress progress-xs  progress-striped active">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 60%;">60%
                                    </div>
                                </div>
                            </li>
                            <li class="avatar">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <img class="avatar"
                                         src="/dessert-root-mainweb/resources/page/index1/images/avatar1.jpg" alt="">
                                    <div>
                                        <div class="point point-primary point-lg"></div>
                                        New message
                                    </div>
                      <span>
                        <small>1 minute ago</small></span>
                                </a>
                            </li>
                            <li class="avatar">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <img class="avatar"
                                         src="/dessert-root-mainweb/resources/page/index1/images/avatar2.jpg" alt="">
                                    <div>
                                        <div class="point point-primary point-lg"></div>
                                        New message
                                    </div>
                      <span>
                        <small>3 minute ago</small></span>
                                </a>
                            </li>
                            <li class="avatar">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <img class="avatar"
                                         src="/dessert-root-mainweb/resources/page/index1/images/avatar3.jpg" alt="">
                                    <div>
                                        <div class="point point-primary point-lg"></div>
                                        New message
                                    </div>
                      <span>
                        <small>4 minute ago</small></span>
                                </a>
                            </li>
                            <li class="avatar">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <img class="avatar"
                                         src="/dessert-root-mainweb/resources/page/index1/images/avatar4.jpg" alt="">
                                    <div>
                                        <div class="point point-primary point-lg"></div>
                                        New message
                                    </div>
                      <span>
                        <small>30 minute ago</small></span>
                                </a>
                            </li>
                            <li class="avatar">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <img class="avatar"
                                         src="/dessert-root-mainweb/resources/page/index1/images/avatar5.jpg" alt="">
                                    <div>
                                        <div class="point point-primary point-lg"></div>
                                        New message
                                    </div>
                      <span>
                        <small>1 hours ago</small></span>
                                </a>
                            </li>
                            <li class="dropdown-menu-footer text-center">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">View
                                    all messages</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#" class="dropdown-toggle notification-icon" data-toggle="dropdown">
                            <i class="fa fa-bell"></i>
                            <span class="badge">3</span></a>
                        <ul class="dropdown-menu list-group">
                            <li class="dropdown-menu-header">
                                <strong>Notifications</strong>
                                <div class="progress progress-xs  progress-striped active">
                                    <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="60"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 60%;">60%
                                    </div>
                                </div>
                            </li>
                            <li class="list-item">
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-inbox.html">
                                    <div class="pull-left">
                                        <i class="fa fa-envelope-o bk-fg-primary"></i>
                                    </div>
                                    <div class="media-body clearfix">
                                        <div>Unread Message</div>
                                        <h6>You have 10 unread message</h6></div>
                                </a>
                            </li>
                            <li class="list-item">
                                <a href="#">
                                    <div class="pull-left">
                                        <i class="fa fa-cogs bk-fg-primary"></i>
                                    </div>
                                    <div class="media-body clearfix">
                                        <div>New Settings</div>
                                        <h6>There are new settings available</h6></div>
                                </a>
                            </li>
                            <li class="list-item">
                                <a href="#">
                                    <div class="pull-left">
                                        <i class="fa fa-fire bk-fg-primary"></i>
                                    </div>
                                    <div class="media-body clearfix">
                                        <div>Update</div>
                                        <h6>There are new updates available</h6></div>
                                </a>
                            </li>
                            <li class="list-item-last">
                                <a href="#">
                                    <h6>Unread notifications</h6>
                                    <span class="badge">15</span></a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- End Notifications -->
                <!-- Userbox -->
                <div class="userbox">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <figure class="profile-picture hidden-xs">
                            <img src="/dessert-root-mainweb/resources/page/index1/images/avatar.jpg" class="img-circle"
                                 alt=""></figure>
                        <div class="profile-info">
                            <span class="name">John Smith Doe</span>
                  <span class="role">
                    <i class="fa fa-circle bk-fg-success"></i>Developer</span>
                        </div>
                        <i class="fa custom-caret"></i>
                    </a>
                    <div class="dropdown-menu">
                        <ul class="list-unstyled">
                            <li class="dropdown-menu-header bk-bg-white bk-margin-top-15">
                                <div class="progress progress-xs  progress-striped active">
                                    <div class="progress-bar progress-bar-primary" role="progressbar" aria-valuenow="60"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 60%;">60%
                                    </div>
                                </div>
                            </li>
                            <li>
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-profile.html">
                                    <i class="fa fa-user"></i>Profile</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-wrench"></i>Settings</a>
                            </li>
                            <li>
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-invoice">
                                    <i class="fa fa-usd"></i>Payments</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-file"></i>File</a>
                            </li>
                            <li>
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/page-login.html">
                                    <i class="fa fa-power-off"></i>Logout</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- End Userbox --></div>
            <!-- End Navbar Right --></div>
    </div>
    <div class="container-fluid content">
        <div class="row">
            <!-- Sidebar -->
            <!-- End Sidebar -->
            <!-- Main Page -->
            <div style="min-height: 610px;" class="main ">
                <!-- Page Header -->
                <div class="page-header">
                    <div class="pull-left">
                        <ol class="breadcrumb visible-sm visible-md visible-lg">
                            <li>
                                <a href="http://www.17sucai.com/preview/2/2015-07-29/npts_10_cvl/index.html">
                                    <i class="icon fa fa-home"></i>Home</a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-file-text"></i>Pages</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-credit-card"></i>Invoice
                            </li>
                        </ol>
                    </div>
                    <div class="pull-right">
                        <h2>Invoice</h2></div>
                </div>
                <!-- End Page Header -->
                <div class="invoice">
                    <div id="pageBody"
                         style="width: 100%;height: 100%">
                    <#--<div style="height: 30px;width: calc(100% -200px); margin-right: -15px;margin-left: -15px;">-->
                    <#--<div style="font-size:12px;padding-top: 8px">-->
                    <#--<span>当前位置 : </span><span id="position"></span>-->
                    <#--</div>-->
                    <#--</div>-->
                        <div style="width: 100%;height: 100%">
                            <iframe id="pagebodyiframe" frameborder=0 scrolling="auto"  src="" height="100%" width="100%"></iframe>
                        </div>
                    </div>


                </div>
            </div>
            <!-- End Main Page -->
            <!-- Footer -->
            <div id="footer">
                <ul>
                    <li>
                        <div class="title">Memory</div>
                        <div class="bar">
                            <div class="progress light progress-sm  progress-striped active">
                                <div class="progress-bar progress-squared progress-bar-success" role="progressbar"
                                     aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">60%
                                </div>
                            </div>
                        </div>
                        <div class="desc">4GB of 8GB</div>
                    </li>
                    <li>
                        <div class="title">HDD</div>
                        <div class="bar">
                            <div class="progress light progress-sm  progress-striped active">
                                <div class="progress-bar progress-squared progress-bar-primary" role="progressbar"
                                     aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%;">40%
                                </div>
                            </div>
                        </div>
                        <div class="desc">250GB of 1TB</div>
                    </li>
                    <li>
                        <div class="title">SSD</div>
                        <div class="bar">
                            <div class="progress light progress-sm  progress-striped active">
                                <div class="progress-bar progress-squared progress-bar-warning" role="progressbar"
                                     aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width: 70%;">70%
                                </div>
                            </div>
                        </div>
                        <div class="desc">700GB of 1TB</div>
                    </li>

                </ul>
            </div>
            <!-- End Footer --></div>
    </div>
    <div class="clearfix"></div>
</div>
<!-- Start: Header -->
<!-- End: Header -->
<!-- Start: Content -->
<!--/container-->
<!-- start: JavaScript-->

    <#--<@includeRes resUrl=[-->
    <#--"page/index1/js/jquery_003.js",-->
    <#--"page/index1/js/jquery-2.js",-->
    <#--"page/index1/js/jquery-migrate-1.js",-->
    <#--"plugins/bootstrap/js/bootstrap.js",-->
    <#--"page/index1/js/skycons.js",-->
    <#--"page/index1/js/pace.js",-->
    <#--"page/index1/js/jquery-ui-1.js",-->
    <#--"page/index1/js/moment.js",-->
    <#--"page/index1/js/jquery.js",-->
    <#--"page/index1/js/jquery_002.js",-->
    <#--"page/index1/js/core.js",-->
    <#--"page/index1/js/invoice.js"-->

    <#--] />-->




<script src="${ctxPath}/resources/page/index1/js/jquery_003.js"></script>
<script src="${ctxPath}/resources/page/index1/js/jquery-2.js"></script>
<script src="${ctxPath}/resources/page/index1/js/jquery-migrate-1.js"></script>
<script src="${ctxPath}/resources/plugins/bootstrap/js/bootstrap.js"></script>
<script src="${ctxPath}/resources/page/index1/js/skycons.js"></script>
<script src="${ctxPath}/resources/page/index1/js/pace.js"></script>
<script src="${ctxPath}/resources/page/index1/js/jquery-ui-1.js"></script>
<script src="${ctxPath}/resources/page/index1/js/moment.js"></script>
<script src="${ctxPath}/resources/page/index1/js/jquery.js"></script>
<script src="${ctxPath}/resources/page/index1/js/jquery_002.js"></script>
<script src="${ctxPath}/resources/page/index1/js/core.js"></script>
<script src="${ctxPath}/resources/page/index1/js/invoice.js"></script>

</#escape>
</body>


</html>