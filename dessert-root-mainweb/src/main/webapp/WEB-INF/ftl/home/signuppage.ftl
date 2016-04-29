<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["common/css/signup/signup-form-elements.css","common/css/signup/signup-style.css","plugins/bootstrap/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","plugins/bootstrap/js/bootstrap-select.js"] />

    <title>用户注册</title>

    <style type="text/css">

    </style>


    <script type="text/javascript">


        jQuery(document).ready(function () {



            /*
                Fullscreen background
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

            // next step
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

                    <form role="form" action="" method="post" class="registration-form">

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
                                    <input type="text" name="form-first-name" placeholder="登录名..." class="form-first-name form-control"
                                           id="form-first-name">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-last-name">姓名</label>
                                    <input type="text" name="form-last-name" placeholder="姓名..." class="form-last-name form-control"
                                           id="form-last-name">
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
                                    <input type="text" name="form-email" placeholder="Email..." class="form-email form-control" id="form-email">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-password">密码</label>
                                    <input type="password" name="form-password" placeholder="密码..." class="form-password form-control"
                                           id="form-password">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-repeat-password">确认密码</label>
                                    <input type="password" name="form-repeat-password" placeholder="确认密码..."
                                           class="form-repeat-password form-control" id="form-repeat-password">
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
                                    <select id="" class="selectpicker" data-live-search="false" data-live-search-style="begins" title="选择性别">
                                        <option>男</option>
                                        <option>女</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-twitter">Twitter</label>
                                    <input type="text" name="form-twitter" placeholder="Twitter..." class="form-twitter form-control"
                                           id="form-twitter">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-google-plus">Google plus</label>
                                    <input type="text" name="form-google-plus" placeholder="Google plus..." class="form-google-plus form-control"
                                           id="form-google-plus">
                                </div>
                                <button type="button" class="btnsign btn-previous">上一步</button>
                                <button type="submit" class="btnsign">现在加入!</button>
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