<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "/common/page/head_inc.ftl">
     <@includeRes resType="css" resUrl=["portal/login/css/login.css","portal/index/css/header_foot.css"] />
    <@includeRes resUrl=["portal/login/css/login.css"] resType="css" />
    <title>市场交易系统</title>
     <style>
 *{margin:0; padding:0;}
input,body{font-size:12px; font-family:"微软雅黑"; color:#666;}
input,img{border:0px;}
.clear{clear:both;}
a{ text-decoration:none;}
ul li{ list-style-type:none;}
.error{margin:0 auto; height:282px; border:0px #e5e5e5 solid;}
.err_page{width:780px; margin:0 auto; padding-top:35px;}
.err_page img{ float:left; margin-right:25px; display:block;}
.err_page span{font-size:25px; color:#ff0000; padding-top:70px; padding-bottom:10px ;display:block;}
.err_page p{ line-height:30px; color:#666;}
.err_page p a{color:#6699cc;}
.err_page p a:hover{ text-decoration:underline;}
 </style>

</head>
<body > 
 <div class="error">
		<div class="err_page">
        		<img src="${resPath}/common/images/404.jpg" />
                <span>抱歉，您访问的地址地址不存在</span>
                <P>请检查输入的地址是否正确 或联系系统管理员</P>
                <!--<p>15秒后页面跳转到<a href="">上一页</a></p>-->
        </div>
</div>
</body>
</html>