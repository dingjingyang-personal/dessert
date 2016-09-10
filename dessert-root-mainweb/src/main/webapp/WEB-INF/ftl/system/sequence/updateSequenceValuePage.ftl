<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=["common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js","plugins/jquery-validation/validate-methods.js"] />

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
                    seqvalue: {
                        required: true,
                        minlength: 1,
                        maxlength: 10
                    }

                },
                messages: {
                    seqvalue: {
                        required: "请输入类型值",
                        minlength: "类型值最少 1 个字符!",
                        maxlength: "类型值最多 10 个字符!",
                    }
                }

            });


        });


        //保存
        function saveModel() {
            var isValidate = $("#addOrUpdateForm").valid();
            if (isValidate) {

                var url;
                var data = getAllInputValue("#addOrUpdateForm");
                data.seqkey = "${RequestParameters.seqkey!''}";
                data.seqowner = "${RequestParameters.seqowner!''}";
                url = "${ctxPath}/system/sequence/updateSequenceValue.htm";

                layer.load(1);
                ajaxEx({
                    url: url, isText: true, data: data, success: function (data) {
                        layer.closeAll('loading');
                        if (data === "1") {
                            parent.layer.msg('保存成功!  2秒后窗口关闭', {
                                time: 2000,
                                icon: 1,
                                shade: 0.3,
                                shadeClose: false
                            }, function () {
                                parent.$('#pagelist').trigger('reloadGrid');//刷新数据
                                closeFrame();//关闭窗口
                            });
                        }
                        else {
                            parent.layer.alert(data, {icon: 5});
                        }
                    }
                });

            } else {
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
                        <label for="seqtypevalue" class="col-sm-1 control-label">值</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="seqvalue" name="seqvalue"
                                   value="${(sequenceValueMap.seqvalue)!''}">
                        </div>
                    </div>

                    <div class="form-group" style="position: relative;top:20px;left: 55%;">
                        <button class="btn btn-success" onclick="saveModel()">提交</button>
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