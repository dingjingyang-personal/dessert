<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["plugins/bootstrap-select/css/bootstrap-select.css"] />
<@includeRes resUrl=["plugins/bootstrap-select/js/bootstrap-select.js","common/js/retina-1.3.0.js","common/js/jquery.backstretch.js","common/js/jquery.form.js","plugins/jquery-validation/jquery.validate.js","plugins/jquery-validation/messages_zh.js","plugins/jquery-validation/validate-methods.js"] />

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
                    seqtypevalue: {
                        required: true,
                        minlength: 1,
                        maxlength: 10
                    },
                    seqorder: {
                        required: true,
                        minlength: 1,
                        maxlength: 2
                    }
                    
                },
                messages: {
                    seqtypevalue: {
                        required: "请输入类型值",
                        minlength: "类型值最少 1 个字符!",
                        maxlength: "类型值最多 10 个字符!",
                    },
                    seqorder: {
                        required: "请输入顺序",
                        minlength: "顺序长度不能小于 1 个字母",
                        maxlength: "顺序长度不能大于 2 个字母"
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

                <#if settingSequenceMap??&&settingSequenceMap.seqsettingid??>
                    data.seqsettingid='${(settingSequenceMap.seqsettingid)!''}';
                    url="${ctxPath}/system/sequence/updateSettingSequence.htm";
                <#else>
                    url="${ctxPath}/system/sequence/addSettingSequence.htm";
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
                            parent.$('#pagelist').trigger('reloadGrid');//刷新数据
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
                        <label for="seqtype" class="col-sm-1 control-label">类型值</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="seqtype" name="seqtype">
                                <option value="1" selected="selected">静态文本</option>
                                <option value="2" >日期格式</option>
                                <option value="3" >流水号位数</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="seqtypevalue" class="col-sm-1 control-label">类型值</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="seqtypevalue" name="seqtypevalue" value="${(settingSequenceMap.seqtypevalue)!''}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="seqorder" class="col-sm-1 control-label">顺序</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control ignore" id="seqorder" name="seqorder" value="${(settingSequenceMap.seqorder)!''}" >
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