
//调整表格大小随浏览器窗口大小改变
$(function () {
    $(window).resize(function () {
        $("#pagelist").setGridWidth($(window).width() * 0.97);
    });

});


function getIframeWindow(id){
    var win=null;
    if(document.frames)
        win = document.frames[id];
    if(!win&&document.getElementById){
        var frm= document.getElementById(id);
        return frm.contentWindow?frm.contentWindow:frm.contentDocument;
    }
    return win;
}