<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc_t.ftl">
    <title>用户登录</title>

    <style type="text/css">

        body {
            background: #ebebeb;
            font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei", "\9ED1\4F53", Arial, sans-serif;
            color: #222;
            font-size: 12px;
        }

        * {
            padding: 0px;
            margin: 0px;
        }

        .top_div {
            background: #008ead;
            width: 100%;
            height: 300px;
        }

        .ipt {
            border: 1px solid #d3d3d3;
            padding: 10px 10px;
            width: 290px;
            border-radius: 4px;
            padding-left: 35px;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
            -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
            -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
        }

        .ipt:focus {
            border-color: #66afe9;
            outline: 0;
            -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6)
        }

        .u_logo {
            background: url("/dessert-root-mainweb/resources/page/login3/images/username.png") no-repeat;
            padding: 10px 10px;
            position: absolute;
            top: 43px;
            left: 40px;

        }

        .p_logo {
            background: url("/dessert-root-mainweb/resources/page/login3/images/password.png") no-repeat;
            padding: 10px 10px;
            position: absolute;
            top: 12px;
            left: 40px;
        }

        a {
            text-decoration: none;
        }

        .tou {
            background: url("/dessert-root-mainweb/resources/page/login3/images/tou.png") no-repeat;
            width: 97px;
            height: 92px;
            position: absolute;
            top: -87px;
            left: 140px;
        }

        .left_hand {
            background: url("/dessert-root-mainweb/resources/page/login3/images/left_hand.png") no-repeat;
            width: 32px;
            height: 37px;
            position: absolute;
            top: -38px;
            left: 150px;
        }

        .right_hand {
            background: url("/dessert-root-mainweb/resources/page/login3/images/right_hand.png") no-repeat;
            width: 32px;
            height: 37px;
            position: absolute;
            top: -38px;
            right: -64px;
        }

        .initial_left_hand {
            background: url("/dessert-root-mainweb/resources/page/login3/images/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            left: 100px;
        }

        .initial_right_hand {
            background: url("/dessert-root-mainweb/resources/page/login3/images/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            right: -112px;
        }

        .left_handing {
            background: url("/dessert-root-mainweb/resources/page/login3/images/left-handing.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -24px;
            left: 139px;
        }

        .right_handinging {
            background: url("/dessert-root-mainweb/resources/page/login3/images/right_handing.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -21px;
            left: 210px;
        }


    </style>

    <script type="text/javascript">


        if (window != top){
            top.location.href = location.href;
        }

        $(function () {

            checkCookieSupport();

            //得到焦点
            $("#userpwd").focus(function () {
                $("#left_hand").animate({
                    left: "150",
                    top: " -38"
                }, {
                    step: function () {
                        if (parseInt($("#left_hand").css("left")) > 140) {
                            $("#left_hand").attr("class", "left_hand");
                        }
                    }
                }, 2000);
                $("#right_hand").animate({
                    right: "-64",
                    top: "-38px"
                }, {
                    step: function () {
                        if (parseInt($("#right_hand").css("right")) > -70) {
                            $("#right_hand").attr("class", "right_hand");
                        }
                    }
                }, 2000);
            });
            //失去焦点
            $("#userpwd").blur(function () {
                $("#left_hand").attr("class", "initial_left_hand");
                $("#left_hand").attr("style", "left:100px;top:-12px;");
                $("#right_hand").attr("class", "initial_right_hand");
                $("#right_hand").attr("style", "right:-112px;top:-12px");
            });
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
<div class="top_div"></div>
<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 230px; text-align: center;">
    <div style="width: 165px; height: 96px; position: absolute;">
        <div class="tou"></div>
        <div class="initial_left_hand" id="left_hand"></div>
        <div class="initial_right_hand" id="right_hand"></div>
    </div>

    <div id="msg" style="margin-bottom:0px;line-height:40px;font-size:1.2em;color:red;height:35px;margin-left: -20px"></div>
    <P style="padding: 30px 0px 10px; position: relative;"><span
            class="u_logo"></span> <input class="ipt" type="text" id="loginnameoremail" placeholder="请输入用户名或邮箱" value="">
    </P>
    <P style="position: relative;"><span class="p_logo"></span>
        <input class="ipt" type="password"  id="userpwd" placeholder="请输入密码" >
    </P>
    <div style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
        <P style="margin: 0px 35px 20px 45px;"><span style="float: left;"><a style="color: rgb(204, 204, 204);"
                                                                             href="#">忘记密码?</a></span>
           <span style="float: right;"><a style="color: rgb(204, 204, 204); margin-right: 10px;"
                                          href="#">注册</a>
              <a id="logina" style="background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;"
                  onclick="login()">登录</a>
           </span></P></div>
</div>
<div style="text-align:center;">

</div>

</#escape>
</body>

</html>