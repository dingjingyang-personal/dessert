<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["common/css/signup/form-elements.css","common/css/signup/style.css"] />
<@includeRes resUrl=["common/js/signup/retina-1.1.0.js","common/js/signup/scripts.js","common/js/signup/jquery.backstretch.js"] />

    <title>用户注册</title>

    <style type="text/css">

    </style>



    <script type="text/javascript">


    </script>

</head>

<body>
<#escape x as x?html>

<div class="top-content">

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

                                <div class="btn-group">
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                        性别 <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">男</a></li>
                                        <li><a href="#">女</a></li>
                                    </ul>
                                </div>
                                </br>



                                <button type="button" class="btn btn-next">下一步</button>
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
                                <button type="button" class="btn btn-previous">上一步</button>
                                <button type="button" class="btn btn-next">下一步</button>
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
                                    <label class="sr-only" for="form-facebook">Facebook</label>
                                    <input type="text" name="form-facebook" placeholder="Facebook..." class="form-facebook form-control"
                                           id="form-facebook">
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
                                <button type="button" class="btn btn-previous">上一步</button>
                                <button type="submit" class="btn">现在加入!</button>
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