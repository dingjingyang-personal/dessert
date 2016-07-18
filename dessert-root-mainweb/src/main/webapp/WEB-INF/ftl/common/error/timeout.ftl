<!DOCTYPE html>
<html lang="zh-CN">

    <head>

        <#include "/common/page/head_inc.ftl">
        <title>响应超时</title>


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

    <body>

        <div class="jumbotron" >
            <h3 class="text-center">页面请求超时!</h3>
            <p class="text-center">会话已过期或超时,请重新<a href="javascript:void(0)" onclick="reLogin()">登陆</a>!</p>
            <p class="text-center" style="font-size: 16px"><span id="time">10</span>秒钟之后自动跳转首页</p>
        </div>


    </body>

</html>