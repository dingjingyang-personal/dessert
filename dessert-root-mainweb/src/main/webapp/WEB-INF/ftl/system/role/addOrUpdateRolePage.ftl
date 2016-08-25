<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["plugins/bootstrap/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/bootstrap/js/bootstrap-select.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js"] />

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
            margin-left: 30px;
        }


    </style>

    <script type="text/javascript">


        $(function () {

            $("#roletype").val("${(roleMap.roletype)!'2'}");
            $("#status").val("${(roleMap.status)!'1'}");

            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({

                debug: true,
                rules: {
                    rolename: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,

                    },

                    roledescription: {
                        maxlength: 100,
                    },
                },
                messages: {
                    loginname: {
                        required: "请输入角色名",
                        minlength: "角色名最少 2 个字符!",
                        maxlength: "角色名最多 10 个字符!",
                    },
                    roledescription: {
                        maxlength: "最多输入100个字符!",
                    },
                }

            });


        });


        //保存
        function saveRole() {
            var isValidate = $("#addOrUpdateForm").valid();
            if (isValidate) {

                var data = getAllInputValue("#addOrUpdateForm");
                var url;

                <#if roleMap??&&roleMap.roleid??>
                    data.roleid='${(roleMap.roleid)!''}';
                    url="${ctxPath}/system/role/updateRole.htm";
                <#else>
                    url="${ctxPath}/system/role/addRole.htm";
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
                        parent.layer.alert('系统异常,请稍后重试!', {icon: 5});
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
                        <label for="firstname" class="col-sm-1 control-label">角色名</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="rolename" name="rolename"
                                   value="${(roleMap.rolename)!''}">
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">角色类型</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="roletype" name="roletype">
                                <option value="1">管理员</option>
                                <option value="2" selected="selected">普通</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">有效状态</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="status" name="status">
                                <option value="1" selected="selected">有效</option>
                                <option value="2">无效</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-2">
                            <textarea class="form-control" rows="3" id="roledescription"
                                      name="roledescription">${(roleMap.roledescription)!''}</textarea>
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;top:20px;left: 55%;">
                        <button class="btn btn-success" onclick="saveRole()">提交</button>
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