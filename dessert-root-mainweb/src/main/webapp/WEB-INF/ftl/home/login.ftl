<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
    <title>用户登录</title>

    <style type="text/css">

        body {
            font-family: 'microsoft yahei', Arial, sans-serif;
            margin: 0;
            padding: 0;
        }


    </style>

    <script type="text/javascript">


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
            var data = {loginnameoremail: loginnameoremail, userpwd: userpwd, mac: (window.mac || '')};
            ajaxEx({
                url: "${ctxPath}/home/login.htm", isText: true, data: data, success: function (data) {
                    if (data == "1") {
                        window.location.replace("${ctxPath}/home/showIndex.htm");
                    } else if (data == "2") {
                        showError("登陆失败");
                    } else {
                        showError(data);
                    }
                }, error: function () {
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

    </script>

</head>

<body>
<#escape x as x?html>

<div id="loginModal" class="modal show">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="text-center text-primary">登录</h1>
            </div>
            <div id="msg" style="margin-bottom:0px;text-align:center;text-align:center;line-height:40px;font-size:1.2em;color:red;height:35px;"></div>
            <div class="modal-body">
                <form class="form col-md-12 center-block">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" id="loginnameoremail" placeholder="登录名或邮箱">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" id="userpwd" placeholder="登录密码">
                    </div>
                    <div class="form-group">
                        <a id="logina" class="btn btn-primary btn-lg btn-block" onclick="login()">
                            登录
                        </a>
                        <span><a href="#">找回密码</a></span>
                        <span><a href="${ctxPath}/home/showSignUpPage.htm" class="pull-right">注册</a></span>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>


</#escape>
</body>

</html>