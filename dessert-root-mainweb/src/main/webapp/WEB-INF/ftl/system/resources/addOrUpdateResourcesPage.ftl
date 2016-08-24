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


            //若菜单层级为3，则action必填，否则不可以填
            if(getMenuLevel("${(RequestParameters.flag)!''}")==3){
                $("#menuaction").attr("disabled",false);
            }else{
                $("#menuaction").attr("disabled",true);
            }
            <#if MenuMap??>
                $('#menutype').val(${(MenuMap.menutype)!''});
                $('#status').val(${(MenuMap.status)!''});
            </#if>



            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({

                debug: true,

                rules: {
                    menuname: {
                        required: true,
                        minlength: 2,
                        maxlength: 10,

                    }

                },
                messages: {
                    menuname: {
                        required: "请输入菜单名",
                        minlength: "菜单名最少2个字符",
                        maxlength: "菜单名最多10个字符",
                    }
                }

            });


        });




        function getMenuLevel(flag){
            var menuaction = $.trim($("#menuaction").val());
            var supmenulevel = "";
            var menulevel = "";
            if(flag=="A"){
                supmenulevel = "${(supMenuMap.menulevel)!''}";
                menulevel = parseInt(supmenulevel) + 1;
            }else{
                menulevel = "${(MenuMap.menulevel)!''}";
            }
            return menulevel;
        }



        //新建菜单
        function submitCreat(){
            var supmenuid = "${(supMenuMap.menuid)!''}";
            var newmenuid = "${(MenuMap.menuid)!''}";
            var menutype = $.trim($("#menutype").val());
            var status =  $("#status").val();
            var menuaction =  $.trim($("#menuaction").val());
            var menuname =  $.trim($("#menuname").val());
            var menulevel = getMenuLevel("${(RequestParameters.flag)!''}");


            if(menutype==''||status==''||menuname==''){
                layer.msg('请输入必填项');
                return;
            }

            //菜单层级为3，菜单链接必填
            if(menulevel==3){
                if(menuaction==""){
                    layer.msg('请输入菜单链接');
                    return;
                }

                var actionleth = menuaction.length;
                if(actionleth>300){
                    layer.msg('链接最多300个字符');
                    return;
                }
            }



            var flag = "${(RequestParameters.flag)!''}";
            var datas={"supmenuid":supmenuid,"menuid":newmenuid,"menutype":menutype,"status":status,
                "action":menuaction,"menuname":menuname,"flag":flag,"menucount":"${(RequestParameters.menucount)!''}"};
            var url ="${ctxPath}/system/resources/addOrUpdateResources.htm";
            layer.load(1);
            ajaxEx({
                type:"post",
                async: false,
                data: datas,
                cache: false,
                url: url,
                success: function (result){
                    layer.closeAll('loading');
                    if(result == "true"){
                        if(flag=="A"){
                            parent.layer.msg('创建成功!  2秒后窗口关闭', {
                                time: 2000,
                                icon: 1,
                                shade: 0.3,
                                shadeClose: false
                            }, function () {
//                                window.parent.$('#treegrid').trigger('reloadGrid');//列表页面刷新数据
                                parent.location.reload();
                                closeFrame();//关闭窗口
                            });
                        }
                        if(flag=="M"){
                            parent.layer.msg('修改成功!  2秒后窗口关闭', {
                                time: 2000,
                                icon: 1,
                                shade: 0.3,
                                shadeClose: false
                            }, function () {
//                                window.parent.$('#treegrid').trigger('reloadGrid');//列表页面刷新数据
                                location.reload();
                                closeFrame();//关闭窗口
                            });
                        }
                    }else{
                        parent.layer.alert('系统异常,请稍后重试!', {icon: 5});
                    }
                }
            });
        }





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
                            <span>${(supMenuMap.menuname)!''}</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="menuname" class="col-sm-1 control-label">菜单名称</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="menuname" name="menuname" value="${(MenuMap.menuname)!''}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="action" class="col-sm-1 control-label">页面链接</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="menuaction" name="menuaction" value="${(MenuMap.action)!''}">
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="menutype" class="col-sm-1 control-label">菜单类型</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="menutype" name="menutype">
                                <option value="1" selected="selected">菜单</option>
                                <option value="2" >按钮</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status" class="col-sm-1 control-label">菜单状态</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="status" name="status">
                                <option value="1" selected="selected">有效</option>
                                <option value="2">无效</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;top:20px;left: 61%;">
                        <button class="btn btn-success" onclick="submitCreat()">提交</button>
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