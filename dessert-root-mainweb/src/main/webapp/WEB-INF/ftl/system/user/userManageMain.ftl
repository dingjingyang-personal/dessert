<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["common/index/css/jquery-accordion-menu.css","common/css/sysui.css"] />
<@includeRes resUrl=["common/index/js/jquery-accordion-menu.js"] />
    <title>用户管理</title>

    <style type="text/css">


    </style>

    <script type="text/javascript">


    </script>

</head>

<body>
<#escape x as x?html>

<div class="m-b-md">
    <form class="form-inline" role="form" id="searchForm" name="searchForm" action="${ctxPath}/system/user/findUsers.htm">
        <div class="form-group">
            <label class="control-label">
                <span class="h4 font-thin v-middle">用户名:</span>
            </label>
            <input
                class="input-medium ui-autocomplete-input" id="name"
                name="roleFormMap.roleFormMap.name">
        </div>
        <a href="javascript:void(0)" class="btn btn-default" id="search">查询</a>
    </form>
</div>
<header class="panel-heading">
    <div class="doc-buttons">

    </div>
</header>
<div class="table-responsive">
    <div id="paging" class="pagclass" style="width: 600px;height: auto">
        <form action="${ctxPath}/system/user/findUsers.htm" method="post">
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>用户名称</th>
                    <th>登录名</th>
                    <th>邮箱</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                    <#if userPage?? && userPage.pageList??&&((userPage.pageList?size)>0)>
                        <#list userPage.pageList as user>
                        <tr>
                            <td>${user.username!''}</td>
                            <td>${user.loginname!''}</td>
                            <td>${user.email!''}</td>
                            <td>
                                <#if (user.status!'')=1>有效<#elseif (user.status!'')=2>无效</#if>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>

        <#--页码-->
            <#if userPage??>
                <div>
                    <#include "/common/page/advPager.ftl">
		     <@AdvPageObj pageObj=userPage/>
                </div>
            </#if>
        </form>
    </div>
</div>



</#escape>
</body>

</html>