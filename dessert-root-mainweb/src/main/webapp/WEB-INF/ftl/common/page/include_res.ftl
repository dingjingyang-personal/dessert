<#macro includeRes  resUrl=[] resType="js">
<#if resType=="js">
	<#list resUrl as js>
<script type="text/javascript" src="${resPath}/${js}?v=${resVersion}" ></script>
	</#list>
<#else>
    <#list resUrl as css>
<link rel="stylesheet" type="text/css"  href="${resPath}/${css}?v=${resVersion}" ></link>
    </#list>
</#if>
</#macro>