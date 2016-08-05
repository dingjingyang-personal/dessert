
//调整表格大小随浏览器窗口大小改变
$(function () {
    $(window).resize(function () {
        $("#pagelist").setGridWidth($(window).width() * 0.97);
    });

});