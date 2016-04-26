<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "/common/page/head_inc.ftl">
    <title>错误页</title>
     <style>
 .result-box {
	PADDING-BOTTOM: 110px; PADDING-LEFT: 20px; PADDING-RIGHT: 20px; BACKGROUND: #fff; BORDER-TOP: #f90 3px solid; PADDING-TOP: 100px
}
.result-box-bg {
	BACKGROUND: url(images/face1.gif)
}
.center-table {
	MARGIN: 0px auto
}
.pl40 {
	PADDING-LEFT: 40px
}
.vt {
	VERTICAL-ALIGN: top
}
.f14 {
	FONT-SIZE: 14px
}
.db {
	DISPLAY: block
}
.mb20 {
	MARGIN-BOTTOM: 20px
}
.c-6 {
	COLOR: #666
}
.page_500{width:auto; padding:30px 0;}
.service_error{width:430px; margin:0 auto; padding:20px 0;}
.ser-icon,.icon_right{ float:left;}
.icon_right{width:325px; margin-left:15px; padding:5px;}
.icon_right span{color:#ff6600; font-size:16px; line-height:30px;}
.icon_right p{ line-height:22px; margin:7px 0;}
.icon_right  img{ float:left;padding-top:3px; margin-right:10px;}
.icon_right  a{color:#666;}
 </style>
    <script type="text/javascript">
   
    </script>
</head>
<body >
<div class="page_500">
	<div class="service_error">
		<div class="ser-icon">
				<img src="${resPath}/common/images/500.jpg" />
        </div>
        <div class="icon_right">
        		<span>服务器内部错误</span>
                <p>网站可能遇到问题，错误码: 500</p>
                <p>请稍后重试</p>
        </div>
        <div class="clear"></div>
    </div>
</div>
</body>
</html>