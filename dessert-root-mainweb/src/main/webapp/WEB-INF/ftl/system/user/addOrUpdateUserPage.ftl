<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["plugins/bootstrap-select/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/bootstrap-select/js/bootstrap-select.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js","plugins/jquery-validation/validate-methods.js"] />

    <title></title>

    <style type="text/css">


        .col-sm-1 {
            width: 22%;
            float: left;
        }

        .col-sm-2 {
            width: 60%;
            float: left;
        }

        .addOrUpdate {
            margin-top: 20px;
            margin-left: 60px;
        }


    </style>

    <script type="text/javascript">



        $(function () {

            $("#sex").val("${(userMap.sex)!'1'}");
            $("#status").val("${(userMap.status)!'1'}");

            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({

                //在编辑状态下,忽略验证某些元素
                <#if userMap??&&userMap.userid??>
                    ignore:".ignore",
                </#if>

                debug: true,
                rules: {
                    username: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,
                        remote: {
                            url: "${ctxPath}/system/user/validateLoginNameOrEmail.htm",     //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "html",           //接受数据格式
                            data: {                     //要传递的数据
                                username: function() {
                                    return $("#username").val();
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
                    tel:{
                        isMobile:true
                    },

                    email: {
                        required: true,
                        email:true,
                        remote: {
                            url: "${ctxPath}/system/user/validateLoginNameOrEmail.htm",     //后台处理程序
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
                },
                messages: {
                    username: {
                        required: "请输入用户名",
                        minlength: "用户名最少 2 个字符!",
                        maxlength: "用户名最多 10 个字符!",
                        remote:"此用户名已存在"
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
                    tel:{
                        isMobile:"请输入正确的手机号码"
                    },
                    email:{
                        required: "请输入邮箱",
                        meail:"请输入正确的邮箱地址",
                        remote:"此邮箱已存在"
                    }

                }

            });


        });


        //保存
        function saveUser() {
            var isValidate = $("#addOrUpdateForm").valid();
            if (isValidate) {

                var data = getAllInputValue("#addOrUpdateForm");
                var url;

                <#if userMap??&&userMap.userid??>
                    data.userid='${(userMap.userid)!''}';
                    url="${ctxPath}/system/user/updateUser.htm";
                <#else>
                    url="${ctxPath}/system/user/addUser.htm";
                </#if>

                layer.load(1);
                ajaxEx({url:url,isText:true,data:data,success:function(data){
                    layer.closeAll('loading');
                    if(data==="1"){
                        parent.layer.msg('保存成功!  2秒后窗口关闭', {
                            time: 2000,
                            icon: 1,
                            shade: 0.3,
                            shadeClose: false
                        }, function () {
                            refreshJqGrid();//刷新数据
                            closeFrame();//关闭窗口
                        });
                    }
                    else{
                        parent.layer.alert(data, {icon: 5});
                    }
                }});

            }else {
                return;
            }

        }


    </script>
</head>

<body>
<#escape x as x?html>




<div class="">
    <div class="addOrUpdate">

        <form id="addOrUpdateForm" class="form-horizontal " method="post">

            <fieldset>
                <div class="form-bottom">
                    <div class="form-group">
                        <label for="username" class="col-sm-1 control-label">用户名</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="username" name="username"
                                   value="${(userMap.username)!''}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userpwd" class="col-sm-1 control-label">密码</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control ignore" id="userpwd" name="userpwd" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label  for="repeatuserpwd"class="col-sm-1 control-label">确认密码</label>
                        <div class="col-sm-2">
                            <input type="password" class="form-control ignore" id="repeatuserpwd" name="repeatuserpwd" >
                        </div>

                    </div>

                    <div class="form-group">
                        <label for="sex" class="col-sm-1 control-label">性别</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="sex" name="sex">
                                <option value="1" selected="selected">男</option>
                                <option value="2" >女</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="tel" class="col-sm-1 control-label">电话</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="tel" name="tel"
                                   value="${(userMap.tel)!''}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email" class="col-sm-1 control-label">邮箱</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="email" name="email"
                                   value="${(userMap.email)!''}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="status" class="col-sm-1 control-label">状态</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="status" name="status">
                                <option value="1" selected="selected">有效</option>
                                <option value="2">无效</option>
                            </select>
                        </div>
                    </div>


                    <div class="form-group" style="position: relative;top:20px;left: 55%;">
                        <button class="btn btn-success" onclick="saveUser()">提交</button>
                        <button class="btn btn-info" onclick="closeFrame()">关闭</button>
                    </div>
                </div>
            </fieldset>

        </form>

    </div>
</div>



</#escape>
</body>

</html>