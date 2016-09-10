<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["page/singup/css/signup-form-elements.css","page/singup/css/signup-style.css","plugins/bootstrap-select/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/bootstrap-select/js/bootstrap-select.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js"] />

    <title>用户注册</title>

    <style type="text/css">

        .form-bottom{
            opacity:0.8;
        }


        input.error { border: 1px solid red; }
        label.error {
            background:url("") no-repeat 0px 0px;
            padding-left: 15px;
            padding-bottom: 2px;
            font-weight: normal;
            color: #ea301e;
        }

    </style>


    <script type="text/javascript">


        jQuery(document).ready(function () {

            /*
                背景图片
            */
            $.backstretch("/dessert-root-mainweb/resources/page/singup/images/background001.jpg");

            $('#top-navbar-1').on('shown.bs.collapse', function () {
                $.backstretch("resize");
            });
            $('#top-navbar-1').on('hidden.bs.collapse', function () {
                $.backstretch("resize");
            });




        });


        $().ready(function () {
            // 在键盘按下并释放及提交后验证提交表单
            $("#signupForm").validate({

                submitHandler: function(form)
                {
                    $(".btnsign").html("正在处理,请稍后...");
                    $(form).ajaxSubmit({
                        type:"post",
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
                    loginname: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,
                        remote: {
                            url: "${ctxPath}/home/validateLoginNameOrEmail.htm",     //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "html",           //接受数据格式
                            data: {                     //要传递的数据
                                loginname: function() {
                                    return $("#loginname").val();
                                }
                            },
                            dataFilter: function (data) {　　　　//判断控制器返回的内容
                                if (data == "true") {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            }
                        }
                    },
                    email: {
                        required: true,
                        email: true,
                        remote: {
                            url: "${ctxPath}/home/validateLoginNameOrEmail.htm",     //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "html",           //接受数据格式
                            data: {                     //要传递的数据
                                email: function() {
                                    return $("#email").val();
                                }
                            },
                            dataFilter: function (data) {　　　　//判断控制器返回的内容
                                if (data == "true") {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            }
                        }
                    },
                    userpwd: {
                        required: true,
                        minlength: 5,
                        maxlength: 30,
                    },
                    repeatuserpwd: {
                        required: true,
                        minlength: 5,
                        maxlength: 30,
                        equalTo: "#userpwd"
                    },
                },
                messages: {
                    loginname: {
                        required: "请输入登录名",
                        minlength: "登录名最少 2 个字符",
                        maxlength: "登录名最多 10 个字符",
                        remote:"此登录名已存在"
                    },
                    email: {
                        required:"请输入邮箱",
                        email:"请输入一个正确的邮箱",
                        remote:"此邮箱已存在"
                    },
                    userpwd: {
                        required: "请输入密码",
                        minlength: "密码长度不能小于 5 个字母",
                        maxlength: "密码长度不能大于 30 个字母"
                    },
                    repeatuserpwd: {
                        required: "请输入密码",
                        minlength: "密码长度不能小于 5 个字母",
                        maxlength: "密码长度不能大于 30 个字母",
                        equalTo: "两次密码输入不一致"
                    },
                }

            });
        });






    </script>

</head>

<body>
<#escape x as x?html>

<div class="top-content" id="container">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>注册</strong></h1>
                    <div class="description">
                        <p>

                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">

                    <form role="form" id="signupForm" action="${ctxPath}/home/signup.htm" method="post" class="registration-form">

                        <fieldset>
                            <div class="form-bottom">
                                <div class="form-group">
                                    <label class="sr-only" for="form-first-name">登录名</label>
                                    <input type="text" name="loginname" placeholder="登录名..." class="form-first-name form-control"
                                           id="loginname">
                                </div>

                                <div class="form-group">
                                    <label class="sr-only" for="form-email">Email</label>
                                    <input type="text" name="email" placeholder="Email..." class="form-email form-control" id="email">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-password">密码</label>
                                    <input type="password" name="userpwd" placeholder="密码..." class="form-password form-control"
                                           id="userpwd">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-repeat-password">确认密码</label>
                                    <input type="password" name="repeatuserpwd" placeholder="确认密码..."
                                           class="form-repeat-password form-control" id="repeatuserpwd">
                                </div>
                                <div class="form-group">
                                    <button  class="btnsign">现在加入</button>
                                </div>
                            </div>
                        </fieldset>

                    </form>

                </div>
            </div>
        </div>
    </div>

</div>


</#escape>
</body>

</html>