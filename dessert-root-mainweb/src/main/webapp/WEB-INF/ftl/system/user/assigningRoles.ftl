<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />

    <title>
        分配角色
    </title>

    <style type="text/css">


        .col-sm-1 {
            width: 22%;
            float: left;
        }

        .col-sm-2 {
            width: 60%;
            float: left;
        }

        .noAssig {
            border: 1px solid #b0b0b0;
            float: left;
            width: 330px;
            height: 400px;

            margin-top: 10px;
            margin-left: 30px;
        }

        .orerate {
            float: left;
            width: 100px;
            height: 400px;
            margin-top: 10px;
            margin-left: 20px;
        }

        .isAssig {
            border: 1px solid #b0b0b0;
            float: left;
            width: 330px;
            height: 400px;
            margin-top: 10px;
            margin-left: 20px;
        }

        span {
            font-size: 18px;
            margin-left: 20px;
        }

    </style>

    <script type="text/javascript">

        $(function () {
            var allRolesList = ${(allRolesListStr)!''};

            if (allRolesList != null && allRolesList.length > 0) {
                for (var i = 0; i < allRolesList.length; i++) {
                    var temp = allRolesList[i];

                    if (temp.isAssigning != null && temp.isAssigning != '' && temp.isAssigning == true) {
                        addTr("isAssigTabletbody", temp);
                    } else {
                        addTr("noAssigTabletbody", temp);
                    }
                }
            }

        });


        //授权或移除
        function authorOrdelete(addordelete) {
            var findid;
            var addid;
            if (addordelete != null && addordelete == "ADD") {
                findid = "noAssigTabletbody";
                addid = "isAssigTabletbody";
            } else {
                findid = "isAssigTabletbody";
                addid = "noAssigTabletbody";
            }
            var inputs = $("#" + findid).find("input");
            if (inputs != null || inputs.length > 0) {
                for (var i = 0; i < inputs.length; i++) {
                    var temp = inputs[i];
                    if (temp.checked == true) {
                        var partr = temp.parentNode.parentNode;
                        $("#" + addid).append(partr.outerHTML);
                        partr.remove();
                    }
                }
            }
        }

        //授权全部或移除全部
        function authorOrdeleteAll(addordelete) {
            var findid;
            var addid;
            if (addordelete != null && addordelete == "ADD") {
                findid = "noAssigTabletbody";
                addid = "isAssigTabletbody";
            } else {
                findid = "isAssigTabletbody";
                addid = "noAssigTabletbody";
            }

            var inputs = $("#" + findid).find("input");
            if (inputs != null || inputs.length > 0) {
                for (var i = 0; i < inputs.length; i++) {
                    var temp = inputs[i];
                    var partr = temp.parentNode.parentNode;
                    $("#" + addid).append(partr.outerHTML);
                    partr.remove();
                }
            }
        }


        function addTr(isAddId, temp) {
            var status;
            if (temp.status == "1") {
                status = "有效";
            } else {
                status = "无效";
            }
            $('#' + isAddId).append(
                    '<tr>' +
                    '<td>' + '<input type="checkbox" id=' + temp.roleid + '>' + '</td>' +
                    '<td>' + temp.rolename + '</td>' +
                    '<td>' + status + '</td>' +
                    '</tr>'
            )
        }


        //保存
        function assigningRoles() {

            var data = getAllRoleValue();
            data.userid = "${RequestParameters.userid!''}";
            var url = "${ctxPath}/system/user/assigningRoles.htm";

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
                            closeFrame();//关闭窗口
                        });
                    }
                    else {
                        parent.layer.alert('系统异常,请稍后重试!', {icon: 5});
                    }
                }
            });


        }

        function getAllRoleValue() {
            var data={};
            var roles = [];
            var inputs = $("#isAssigTabletbody").find("input");
            if (inputs != null || inputs.length > 0) {
                for (var i = 0; i < inputs.length; i++) {
                    var temp = inputs[i];
                    roles.push(temp.id);
                }
            }
            data.roleids=roles.toString();
            return data;
        }


    </script>
</head>

<body>
<#escape x as x?html>

<div class="">


    <div style="margin-left: 30px;margin-top: 20px;font-size: 18px">
        <div style="float: left; width: 470px;">未授权</div>
        <div style="float: left; width: 330px;">已授权</div>
    </div>


    <div class="noAssig">

        <table class="table table-hover">
            <thead>
            <tr>
                <th style="width: 15%">选中</th>
                <th style="width: 55%">角色名称</th>
                <th style="width: 30%">状态</th>
            </tr>
            </thead>
            <tbody id="noAssigTabletbody">

            </tbody>
        </table>
    </div>

    <div class="orerate">
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-success btn-block" onclick="authorOrdeleteAll('ADD')"><i
                    class="fa fa-angle-double-right"></i>&nbsp;&nbsp;授权全部
            </button>

        </div>
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-info btn-block" onclick="authorOrdelete('ADD')"><i
                    class="fa fa-angle-right"></i>&nbsp;&nbsp;授权
            </button>

        </div>
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-warning btn-block" onclick="authorOrdelete('DELETE')"><i
                    class="fa fa-angle-left"></i>&nbsp;&nbsp;移除
            </button>

        </div>
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-danger btn-block" onclick="authorOrdeleteAll('DELETE')"><i
                    class="fa fa-angle-double-left"></i>&nbsp;&nbsp;移除全部
            </button>

        </div>


        <div style="margin-top: 140px">
            <button class="btn btn-success btn-block" onclick="assigningRoles()">提交</button>
        </div>
        <div style="margin-top: 10px">
            <button class="btn btn-danger btn-block" onclick="closeFrame()">关闭</button>
        </div>


    </div>


    <div class="isAssig">

        <table class="table table-hover">
            <thead>
            <tr>
                <th style="width: 15%">选中</th>
                <th style="width: 55%">角色名称</th>
                <th style="width: 30%">状态</th>
            </tr>
            </thead>
            <tbody id="isAssigTabletbody">

            </tbody>
        </table>

    </div>


</div>



</#escape>
</body>

</html>