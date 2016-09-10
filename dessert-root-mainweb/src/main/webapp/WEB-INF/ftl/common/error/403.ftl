<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
    <title>禁止访问</title>


    <script type="text/javascript">

        $(function(){
            delayURL("${ctxPath}/home/showIndex.htm");
        });

        function delayURL(url) {
            var delay = document.getElementById("time").innerHTML;
            if (delay > 0) {
                delay--;
                document.getElementById("time").innerHTML = delay
            } else {
                window.top.location.href = url
            }
            setTimeout("delayURL('" + url + "')", 1000)
        }

    </script>

</head>

<body >

<div class="jumbotron"  >

    <h3 class="text-center">禁止访问!</h3>
    <p class="text-center" style="font-size: 16px"><span id="time">5</span>秒钟之后自动跳转首页</p>
</div>


</body>

</html>