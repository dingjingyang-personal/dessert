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

        .addOrUpdate{
            margin-top: 20px;
            margin-left: 60px;
        }


    </style>

    <script type="text/javascript">


        $(function(){

            var url;
            if("${(resourcesMap.menuid)!''}"==null||"${(resourcesMap.menuid)!''}"==''){
                url = "${ctxPath}/system/resources/addResources.htm";
            }else {
                url = "${ctxPath}/system/resources/updateResources.htm?menuid="+"${(resourcesMap.menuid)!''}";
                $("#roletype").val("${(resourcesMap.menutype)!''}");
                $("#status").val("${(resourcesMap.status)!''}");
            }

            $("#addOrUpdateForm").attr("action", url);


            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({

                submitHandler: function(form)
                {
                    layer.load(1);
                    $(form).ajaxSubmit({
                        type:"post",
                        isText: true,
                        success: function (data) {
                            layer.closeAll('loading');
                            if (data == "1") {
                                parent.layer.msg('保存成功!  2秒后窗口关闭',{
                                    time:2000,
                                    icon: 1,
                                    shade:0.3,
                                    shadeClose:false
                                },function(){
                                    window.parent.$('#pagelist').trigger('reloadGrid');//列表页面刷新数据
                                    closeFrame();//关闭窗口
                                });

                            } else {
                                parent.layer.alert('系统异常,请稍后重试!', {icon: 5});
                            }
                        },

                    });
                },


                rules: {
                    menuname: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,

                    },

                    action: {
                        url:true,
                        maxlength: 100,
                    },
                },
                messages: {
                    menuname: {
                        required: "请输入菜单名",
                        minlength: "菜单名最少 2 个字符!",
                        maxlength: "菜单名最多 10 个字符!",
                    },
                    action: {
                        url:"请输入正确的URL",
                        maxlength: "最多输入100个字符!",
                    },
                }

            });


        });






    </script>
</head>

<body >
<#escape x as x?html>




<div class="">
    <div class="addOrUpdate" >

        <form  id="addOrUpdateForm" class="form-horizontal "  method="post">

            <fieldset>
                <div class="form-bottom">

                    <div class="form-group">
                        <label for="firstname" class="col-sm-1 control-label">上级菜单</label>
                        <div class="col-sm-2">
                            <span>${(resourcesMap.rolename)!''}</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="menuname" class="col-sm-1 control-label">菜单名称</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="menuname" name="menuname" value="${(resourcesMap.menuname)!''}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="action" class="col-sm-1 control-label">页面链接</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="action" name="action" value="${(resourcesMap.action)!''}">
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="menutype" class="col-sm-1 control-label">菜单类型</label>
                        <div class="col-sm-2">
                            <select class="form-control" id="menutype" name="menutype">
                                <option value="1" selected="selected">菜单</option>
                                <option value="2" >按钮</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status" class="col-sm-1 control-label">菜单状态</label>
                        <div class="col-sm-2">
                            <select class="form-control" id="status" name="status">
                                <option value="1" selected="selected">有效</option>
                                <option value="2">无效</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;top:20px;left: 61%;">
                        <button class="btn btn-success">提交</button>
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