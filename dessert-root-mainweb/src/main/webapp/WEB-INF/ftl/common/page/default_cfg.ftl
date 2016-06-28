<#assign ctxPath=request.contextPath>
<#setting number_format="computer">
<#assign resPath="${ctxPath}/resources">
<#assign ftpPath="http://test/ihsdata">
<#-- 设置资源版本号 -->
<#assign resVersion="${(res.version)!'2016.4.22'}">
<#-- 使用页面引入宏 -->
<#include "./include_res.ftl" >