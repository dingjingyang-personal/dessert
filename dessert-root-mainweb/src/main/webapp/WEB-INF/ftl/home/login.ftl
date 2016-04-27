<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html"
      xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

<head>

<#include "/common/page/head_inc.ftl">
    <title>登陆</title>

    <style type="text/css">

        body{
            font-family: 'microsoft yahei',Arial,sans-serif;
            margin:0;
            padding:0;
        }


    </style>

    <script type="text/javascript">

        $(function(){
            $.readMac({success:function(data){
                window.mac=data;
            }});
        });
        function login(){
            var loginname=$.trim($("#loginname").val());
            var userpwd=$.trim($("#userpwd").val());
            if(loginname.length==0&&userpwd.length==0){
                showError("请输入用户名密码");
                return ;
            }
            if(loginname.length==0){
                showError("请输入用户名");
                return ;
            }
            if(userpwd.length==0){
                showError("请输入密码");
                return ;
            }
            showError("");
            var data={userno:loginname,userpwd:userpwd,mac:(window.mac||'')};
            ajaxEx({url:"${ctxPath}/home/login.htm",isText:true,data:data,success:function(data){
                if(data=="1"){
                    showMask({loadingMsg:"验证已完成，正在登陆......"});
                    window.location.replace("${ctxPath}/home/showIndex.htm");
                }else if(data=="2"){
                    showError("登陆失败");
                }else{
                    showError(data);
                }
            },error:function(){
                showError("系统异常，请稍后重试");
            }});
        }
        function showError(msg){
            $("#msg").html(msg);
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

<div id="loginModal" class="modal show">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="text-center text-primary">登录</h1>
            </div>
            <div class="modal-body">
                <form  class="form col-md-12 center-block" >
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" id="loginname" placeholder="登录名">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" id="userpwd" placeholder="登录密码">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick="login()">
                            立刻登录
                        </button>
                        <span><a href="#">找回密码</a></span>
                        <span><a href="#" class="pull-right">注册</a></span>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

</body>

</html>