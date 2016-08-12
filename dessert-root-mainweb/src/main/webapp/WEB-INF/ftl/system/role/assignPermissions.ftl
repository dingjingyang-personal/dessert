<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=["plugins/zTree/css/metroStyle/metroStyle.css"] />
<@includeRes resUrl=["plugins/zTree/js/jquery.ztree.core.js","plugins/zTree/js/jquery.ztree.excheck.js"] />

    <title>分配权限</title>

    <style type="text/css">


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

        .ztree {
            width: 320px;
            height: 380px;
            overflow-y: auto;
        }


    </style>

    <script type="text/javascript">


        const setting = {
            check: {
                enable: true
            },
            data: {
                key: {name: "menuname"},
                simpleData: {
                    enable: true,
                    idKey: "menuid",
                    pIdKey: "parentid",
                    rootPId: 0
                }
            }
        };

        var noNodes;
        var authoriedNodes;

        $(document).ready(function () {
            noNodes = ${noAssigListStr};
            authoriedNodes = ${resourcesByRoleListStr};
            $.fn.zTree.init($("#menutree"), setting, noNodes);
            $.fn.zTree.init($("#authoriedtree"), setting, authoriedNodes);
        });


        //授权或移除授权
        function addOrDeletePermissions(typeMode) {
            var data;
            if (typeMode == "add") {
                data = getNodes("menutree");
            } else {
                data = getNodes("authoriedtree");
            }

            if(data==null){
                return;
            }
            data.typeMode = typeMode;
            data.roleid = "${RequestParameters.roleid!''}";
            var url = "${ctxPath}/system/role/addOrDeletePermissions.htm";

            layer.load(1);
            ajaxEx({
                url: url, isText: true, data: data, success: function (data) {
                    layer.closeAll('loading');
                    if (data === "success") {
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


        function getNodes(id) {
            var data = {};
            var resourcesIds = [];
            var treeObj = $.fn.zTree.getZTreeObj(id);
            var assignNodes = treeObj.getCheckedNodes(true);
            var count = 0;
            var count1 = 0;
            for (var i = 0; i < assignNodes.length; i++) {
                var temp = assignNodes[i];
                if (temp.action != null && temp.action != "" && temp.menulevel == "3") {
                    resourcesIds.push(temp.menuid);
                    count++;
                } else {
                    count1++;
                }
            }
            if (count1 > 0 && count == 0) {
                layer.msg('此节点不能授权,请选择正确节点');
                return;
            }
            if (count == 0) {
                layer.msg('请选择权限');
                return;
            }
            data.checkNodes = resourcesIds.toString();
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
        <div class="">
            <ul id="menutree" class="ztree"></ul>
        </div>
    </div>

    <div class="orerate">
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-success btn-block" onclick="addOrDeletePermissions('add')">
                <i
                        class="fa fa-angle-double-right"></i>&nbsp;&nbsp;授权
            </button>

        </div>
        <div style="margin-top: 10px">
            <button id="btnAdd" type="button" class="btn btn-info btn-block" onclick="addOrDeletePermissions('delete')">
                <i
                        class="fa fa-angle-right"></i>&nbsp;&nbsp;移除
            </button>

        </div>


        <div style="margin-top: 270px">
            <button class="btn btn-danger btn-block" onclick="closeFrame()">关闭</button>
        </div>


    </div>


    <div class="isAssig">
        <div class="">
            <ul id="authoriedtree" class="ztree"></ul>
        </div>


    </div>


</div>





</#escape>
</body>

</html>