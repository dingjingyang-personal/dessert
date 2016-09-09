<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["plugins/bootstrap/css/bootstrap-select.css"] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/bootstrap/js/bootstrap-select.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js","plugins/jquery-validation/validate-methods.js"] />

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

            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({
                

                debug: true,
                rules: {
                    seqkey: {
                        required: true,
                        minlength: 2,
                        maxlength: 20
                    },
                    seqdesc: {
                        required: true,
                        minlength: 1,
                        maxlength: 50
                    }
                    
                },
                messages: {
                    seqkey: {
                        required: "请输入流水号KEY",
                        minlength: "流水号KEY最少 2 个字符!",
                        maxlength: "流水号KEY最多 20 个字符!",
                    },
                    seqdesc: {
                        required: "请输入描述",
                        minlength: "描述长度不能小于 1 个字母",
                        maxlength: "描述长度不能大于 50 个字母"
                    }
                }

            });


        });


        //保存
        function saveSequence() {
            var isValidate = $("#addOrUpdateForm").valid();
            if (isValidate) {

                var data = getAllInputValue("#addOrUpdateForm");
                var url;

                <#if sequenceMap??&&sequenceMap.seqid??>
                    data.seqid='${(sequenceMap.seqid)!''}';
                    url="${ctxPath}/system/sequence/updateSequence.htm";
                <#else>
                    url="${ctxPath}/system/sequence/addSequence.htm";
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
                        <label for="seqkey" class="col-sm-1 control-label">流水号KEY</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="seqkey" name="seqkey"
                                <#if sequenceMap??&&sequenceMap.seqid??>
                                   disabled
                                </#if>
                                   value="${(sequenceMap.seqkey)!''}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="seqdesc" class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control ignore" id="seqdesc" name="seqdesc" value="${(sequenceMap.seqdesc)!''}" >
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;top:20px;left: 55%;">
                        <button class="btn btn-success" onclick="saveSequence()">提交</button>
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