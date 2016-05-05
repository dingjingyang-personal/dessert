<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["common/css/signup/signup-form-elements.css","common/css/signup/signup-style.css","plugins/bootstrap/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","plugins/bootstrap/js/bootstrap-select.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js"] />

    <title>用户注册</title>

    <style type="text/css">

    </style>


    <script type="text/javascript">


        jQuery(document).ready(function () {

            /*
                背景图片
            */
            $.backstretch("/dessert-root-mainweb/resources/common/images/signup/1.jpg");

            $('#top-navbar-1').on('shown.bs.collapse', function () {
                $.backstretch("resize");
            });
            $('#top-navbar-1').on('hidden.bs.collapse', function () {
                $.backstretch("resize");
            });

            /*
                Form
            */
            $('.registration-form fieldset:first-child').fadeIn('slow');

            $('.registration-form input[type="text"], .registration-form input[type="password"], .registration-form textarea').on('focus', function () {
                $(this).removeClass('input-error');
            });

            // 下一步
            $('.registration-form .btn-next').on('click', function () {

                var parent_fieldset = $(this).parents('fieldset');
                var next_step = true;

                parent_fieldset.find('input[type="text"], input[type="password"], textarea').each(function () {
                    if ($(this).val() == "") {
                        $(this).addClass('input-error');
                        next_step = false;
                    }
                    else {
                        $(this).removeClass('input-error');
                    }
                });


                var validator = $("#signupForm").validate();
                var nodes = $(this).parent().find('input[type=text],input[type=hidden],input[type=password],select');
                var iselement = true;
                for (var i = 0; i < nodes.length; i++) {
                    var boo = validator.element(nodes[i]);
                    iselement = iselement && boo;
                }
                if (!iselement) {
                    return;
                }


                if (next_step) {
                    parent_fieldset.fadeOut(400, function () {
                        $(this).next().fadeIn();
                    });
                }

            });

            // previous step
            $('.registration-form .btn-previous').on('click', function () {
                $(this).parents('fieldset').fadeOut(400, function () {
                    $(this).prev().fadeIn();
                });
            });

            // submit
            $('.registration-form').on('submit', function (e) {

                $(this).find('input[type="text"], input[type="password"], textarea').each(function () {
                    if ($(this).val() == "") {
                        e.preventDefault();
                        $(this).addClass('input-error');
                    }
                    else {
                        $(this).removeClass('input-error');
                    }
                });

            });


        });


        $().ready(function () {
            // 在键盘按下并释放及提交后验证提交表单
            $("#signupForm").validate({
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
                    username: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,
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
                    sex: {
                        required: true,
                    },
                },
                messages: {
                    loginname: {
                        required: "请输入登录名",
                        minlength: "登录名最少 2 个字符",
                        maxlength: "登录名最多 10 个字符",
                        remote:"此登录名已存在"
                    },
                    username: {
                        required: "请输入姓名",
                        minlength: "姓名最少 2 个字符",
                        maxlength: "姓名最多 10 个字符"
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
                    sex: "请选择性别",
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
                            <div class="form-top">
                                <div class="form-top-left">
                                    <h3>步骤 1 / 3</h3>
                                    <p>请填写信息:</p>
                                </div>
                                <div class="form-top-right">
                                    <i class="fa fa-user"></i>
                                </div>
                            </div>
                            <div class="form-bottom">
                                <div class="form-group">
                                    <label class="sr-only" for="form-first-name">登录名</label>
                                    <input type="text" name="loginname" placeholder="登录名..." class="form-first-name form-control"
                                           id="loginname">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-last-name">姓名</label>
                                    <input type="text" name="username" placeholder="姓名..." class="form-last-name form-control"
                                           id="username">
                                </div>


                                <button type="button" class="btnsign btn-next">下一步</button>
                            </div>
                        </fieldset>

                        <fieldset>
                            <div class="form-top">
                                <div class="form-top-left">
                                    <h3>步骤 2 / 3</h3>
                                    <p>请填写信息:</p>
                                </div>
                                <div class="form-top-right">
                                    <i class="fa fa-key"></i>
                                </div>
                            </div>
                            <div class="form-bottom">
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
                                <button type="button" class="btnsign btn-previous">上一步</button>
                                <button type="button" class="btnsign btn-next">下一步</button>
                            </div>
                        </fieldset>

                        <fieldset>
                            <div class="form-top">
                                <div class="form-top-left">
                                    <h3>步骤 3 / 3</h3>
                                    <p>请填写信息:</p>
                                </div>
                                <div class="form-top-right">
                                    <i class="fa fa-twitter"></i>
                                </div>
                            </div>
                            <div class="form-bottom">
                                <div class="form-group">
                                    <select id="sex" name="sex" class="selectpicker" data-live-search="false" data-live-search-style="begins"
                                            title="选择性别">
                                        <option value="1">男</option>
                                        <option value="2">女</option>
                                    </select>
                                </div>
                                <button type="button" class="btnsign btn-previous">上一步</button>
                                <button type="submit" class="btnsign">现在加入</button>
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