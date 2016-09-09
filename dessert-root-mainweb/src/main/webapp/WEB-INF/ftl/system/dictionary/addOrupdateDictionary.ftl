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

            $("#diclang").val("${(dictionaryMap.diclang)!'ZH'}");
            $("#status").val("${(dictionaryMap.status)!'1'}");

            // 在键盘按下并释放及提交后验证提交表单
            $("#addOrUpdateForm").validate({
                

                debug: true,
                rules: {
                    diccode: {
                        required: true,
                        minlength: 2,
                        maxlength: 20
                    },
                    dicvalue: {
                        required: true,
                        minlength: 1,
                        maxlength: 30
                    },
                    dictype:{
                        required:true,
                        minlength: 2,
                        maxlength: 30
                    }
                    
                },
                messages: {
                    diccode: {
                        required: "请输入代码",
                        minlength: "代码最少 2 个字符!",
                        maxlength: "代码最多 20 个字符!",
                    },
                    dicvalue: {
                        required: "请输入值",
                        minlength: "值长度不能小于 1 个字母",
                        maxlength: "值长度不能大于 30 个字母"
                    },
                    dictype:{
                        required: "请输入代码类型",
                        minlength: "代码类型长度不能小于 2 个字母",
                        maxlength: "代码类型长度不能大于 30 个字母"
                    }
                }

            });


        });


        //保存
        function saveDictionary() {
            var isValidate = $("#addOrUpdateForm").valid();
            if (isValidate) {

                var data = getAllInputValue("#addOrUpdateForm");
                var url;

                <#if dictionaryMap??&&dictionaryMap.dicid??>
                    data.dicid='${(dictionaryMap.dicid)!''}';
                    url="${ctxPath}/system/dictionary/updateDictionary.htm";
                <#else>
                    url="${ctxPath}/system/dictionary/addDictionary.htm";
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
                        <label for="diccode" class="col-sm-1 control-label">代码</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="diccode" name="diccode"
                                   value="${(dictionaryMap.diccode)!''}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dicvalue" class="col-sm-1 control-label">值</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control ignore" id="dicvalue" name="dicvalue" value="${(dictionaryMap.dicvalue)!''}" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="diclang" class="col-sm-1 control-label">语言类型</label>
                        <div class="col-sm-2">
                            <select class="selectpicker form-control" id="diclang" name="diclang">
                                <option value="ZH" selected="selected">中文</option>
                                <option value="EN" >英文</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dictype" class="col-sm-1 control-label">代码类型</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="dictype" name="dictype"
                                   value="${(dictionaryMap.dictype)!''}">
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
                        <button class="btn btn-success" onclick="saveDictionary()">提交</button>
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