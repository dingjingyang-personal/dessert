<!DOCTYPE html>
<html lang="zh-CN">

<head>

<#include "/common/page/head_inc.ftl">
<@includeRes resType="css" resUrl=[] />
<@includeRes resUrl=[] />
    <title></title>

    <style type="text/css">

    </style>

    <script type="text/javascript">


    </script>
</head>

<body>
<#escape x as x?html>




<div class="addOrUpdate">
    <div class="">

        <form role="form" id="" class="form-horizontal" action="${ctxPath}/home/signup.htm" method="post">

            <fieldset>
                <div class="form-bottom">
                    <div class="form-group">
                        <label for="firstname" class="col-sm-1 control-label">角色名</label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" id="firstname">
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">角色类型</label>
                        <div class="col-sm-2">
                            <select class="form-control">
                                <option value="1">管理员</option>
                                <option value="2" selected="selected">普通</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">有效状态</label>
                        <div class="col-sm-2">
                            <select class="form-control">
                                <option value="1" selected="selected">有效</option>
                                <option value="2">无效</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="name" class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-2">
                            <textarea class="form-control" rows="3"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <button class="btnsign">提交</button>
                    </div>
                </div>
            </fieldset>

        </form>

    </div>
</div>



</#escape>
</body>

</html>