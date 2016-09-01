<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc_t.ftl">
<@includeRes resType="css" resUrl=["page/singup/css/style.css"] />
<@includeRes resUrl=["page/singup/js/Particleground.js"] />
    <title>用户登录</title>

    <style type="text/css">
        body{height:100%;background:#16a085;overflow:hidden;}
        canvas{z-index:-1;position:absolute;}

    </style>

    <script type="text/javascript">

        $(document).ready(function() {
            //粒子背景特效
            $('body').particleground({
                dotColor: '#5cbdaa',
                lineColor: '#5cbdaa'
            });

            checkCookieSupport();
        });




        function login() {

            $("#logina").html("正在登录,请稍后...");

            var loginnameoremail = $.trim($("#loginnameoremail").val());
            var userpwd = $.trim($("#userpwd").val());
            if (loginnameoremail.length == 0 && userpwd.length == 0) {
                showError("请输入登录名密码");
                return;
            }
            if (loginnameoremail.length == 0) {
                showError("请输入登录名或邮箱");
                return;
            }
            if (userpwd.length == 0) {
                showError("请输入密码");
                return;
            }
            var data = {loginnameoremail: loginnameoremail, userpwd: userpwd};

            ajaxEx({
                url: "${ctxPath}/home/login.htm",
                isText: true,
                data: data,
                success: function (data) {
                    if (data == "1") {
                        $("#logina").html("登录成功,正在跳转到首页");
                        window.location.replace("${ctxPath}/home/showIndex.htm");
                    } else if (data == "2") {
                        showError("登陆失败");
                    } else {
                        showError(data);
                    }
                },
                error: function () {
                    showError("系统异常，请稍后重试");
                }
            });
        }

        function showError(msg) {
            $("#msg").html(msg);
            $("#logina").html("登录");

        }
        //在页面回车输入事件
        document.onkeydown = function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if (e && e.keyCode == 13) {
                login();
            }
        };


        function checkCookieSupport(){
            var isSupport = false;
            if(typeof(navigator.cookieEnabled) != 'undefined')
                isSupport = navigator.cookieEnabled;
            else{
                document.cookie = 'test';
                isSupport = document.cookie == 'test';
                document.cookie = '';
            }
            if(!isSupport){
                showError("浏览器禁用了Cookie,此系统需要浏览器支持Cookie。");
            }
        }


    </script>

</head>

<body>
<#escape x as x?html>

<dl class="admin_login">
    <dt>
        <strong>DESSERT</strong>
        <em></em>
    <div id="msg" style="margin-bottom:10px;text-align:center;color:#ffffff;"></div>
    </dt>
    <dd class="user_icon">
        <input style="" placeholder="登录名或邮箱" class="login_txtbx" type="text" id="loginnameoremail">
    </dd>
    <dd class="pwd_icon">
        <input placeholder="登录密码" class="login_txtbx" type="password" id="userpwd">
    </dd>

    <dd>
        <input value="立即登陆" class="submit_btn" type="button" id="logina" onclick="login()">
    </dd>
    <dd>
        <p>© 2016-2016 ding </p>
        <p>B2-8998988-1</p>
    </dd>
</dl>

</#escape>
</body>

</html>