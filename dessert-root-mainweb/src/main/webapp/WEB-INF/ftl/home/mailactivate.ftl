<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js"] />


    <title>邮箱验证结果</title>

    <style type="text/css">

        body {
            background-color: #23527c;
        }

        h3 {
            color: #ffffff;
            font-size: 30px;
        }

        a {
            color: #ffffff;
            text-decoration: none;
        }

        a:hover {
            color: #ffffff; /*鼠标经过的颜色变化*/
            text-decoration: none;
        }

        #box {

            width: 40%;
            padding-top: 60px;
            color: #000;
        }

        #boxhead {
            background-color: #F5F5F5;
            border-radius: 2px 2px 0px 0px;
            padding: 10px 15px;
            border-bottom: 1px solid transparent;
            font-weight: 700;
            font-size: 16px;
        }

        #boxmes {
            padding: 20px 30px 30px;
            background-color: #ffffff;

        }

        #box01 {
            display: none;
        }

        #about {

            width: 100px;
            height: 80px;
            margin-left: auto;
            margin-right: auto;
            margin-top: 10px;
            color: #ffffff;
            text-align: center;

        }


    </style>

    <script type="text/javascript">

        $(document).ready(function () {

            $("#box01").fadeIn(1500);


        });


        $(function () {
            var activateStatus = '${(messageparams.activateStatus)!''}';
            if (activateStatus == "100") {
                $("#directionPag").html("登录页面");
                delayURL("${ctxPath}/home/showLoginPage.htm");
            } else if (activateStatus == "101") {
                $("#directionPag").html("注册页面");
                delayURL("${ctxPath}/home/showSignUpPage.htm");
            } else if (activateStatus == "102") {
                $("#directionPag").html("登录页面");
                delayURL("${ctxPath}/home/showLoginPage.htm");
            } else {
                $("#ptime").children().remove();
                $("#ptime").html('<span  id="affsendmail" style="color: royalblue" data-toggle="modal"  data-target="#myModal">点击此处重新发送邮件...</span>');
            }

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


        $().ready(function () {
            // 在键盘按下并释放及提交后验证提交表单
            $("#signupForm").validate({
                submitHandler: function (form) {
                    $(".btn btn-primary").html("正在处理,请稍后...");
                    $(form).ajaxSubmit({
                        type: "post",
                        isText: true,
                        success: function (data) {
                            if (data == "1") {
                                window.location.replace("${ctxPath}/home/showLoginPage.htm");
                            } else {
                                $(".btnsign").html("系统异常,请稍后重试");
                            }
                        },

                    });
                },


                rules: {
                    email: {
                        required: true,
                        email: true,
                    },

                },
                messages: {
                    email: {
                        required: "请输入邮箱",
                        email: "请输入一个正确的邮箱",
                    },
                }

            });
        });


    </script>

</head>

<body>
<#escape x as x?html>




<div class="container" id="box">

    <div id="box01" class="animated fadeInUp">

        <h3 class="text-center">dessert</h3>

        <div>
            <div id="boxhead">
                <p class="text-center">${(message.message)!''}</p>
            </div>
            <div id="boxmes">
                <p>
                ${(message.message)!''}
                </p>
                <p id="ptime"><span id="time" style="color: royalblue">10000</span> <span>秒钟之后自动跳转到</span><span
                        id="directionPag"></span></p>
            </div>
        </div>
    </div>

    <div id="about">
        <p>
            <a href="">主页</a>
        </p>
        <p>
            <a>dessert</a> © 2016
        </p>
    </div>


</div>


<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">

                <h4 class="modal-title" id="myModalLabel">
                    重新发送邮件验证码
                </h4>
            </div>
            <form role="form" id="signupForm" action="${ctxPath}/home/resendmail.htm" method="post"
                  class="registration-form">
                <div class="form-group" style="margin : 0px 10px 10px 15px;">
                    <label class="sr-only" for="form-email">Email</label>
                    <input type="text" name="email" placeholder="Email..." class="form-email form-control" id="email" style="width: 80%;margin : 10px 10px 10px 0px;">
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>




</#escape>
</body>

</html>