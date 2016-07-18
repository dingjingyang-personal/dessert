$(function(){
    bindPagesEvent({});
});

function bindPagesEvent(options){
    var temp;
    if(options&&options.container)
      temp=$(container);
    else
      temp=$(".snPages");
    if(options&&options.url){
       temp.parents('form').attr('action',options.url);
    }
    var pageHidden=temp.find("[name=currentPage]").eq(0);
    temp.find('input[type=text]:first').keydown(function(event){
    	    var mEvent=event|| window.event;
            var keycode=mEvent.keyCode||mEvent.which;
            if(keycode==13)
            {
               if(event && event.preventDefault)
                 event.preventDefault();
               else
                 window.event.returnValue = false;
               var thisObj=$(this);
               var pageIndex=parseInt(thisObj.attr('pageIndex'));
               if(!(/^\d+$/.test(thisObj.val())))
               {
                 thisObj.val(pageIndex);
                 return false;
               } 
               var page = parseInt(thisObj.val());
               if(page<1)
                 page=1;
               var pageCOunt=parseInt(thisObj.attr('pageCount'));
               if(page>pageCOunt)
                 page=pageCOunt;
               thisObj.val(page);
               pageHidden.val(page);
               if(page===pageIndex)
                 return false;
               if(options&&options.handler){
            	   options.handler(page);
               }
               temp.parents("form").submit();
               return false;
            }
         });
    window.clickPage=function(pageIndex){
        pageHidden.val(pageIndex);
        if(options&&options.handler){
     	   options.handler(page);
        }
        pageHidden.parents('form').submit();
    };
}
