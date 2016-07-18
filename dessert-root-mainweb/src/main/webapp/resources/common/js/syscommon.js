	//获取时间差
function getDiffDate(beginData,endDate){
	 var tmpBeginTime = new Date(beginData.replace(/-/g, "\/")); //时间转换      
     var tmpEndTime = new Date(endDate.replace(/-/g, "\/")); //时间转换         
     return (tmpEndTime - tmpBeginTime)/(1000 * 60 * 60 * 24);  
}
function validateText(text,inputDef,input){
	var result={isOk:1};
	if(text.length==0&&!inputDef.required){
       return result;
    }
    if(text.length==0&&inputDef.required){
    	result.isOk=0;
    	result.errorType="required";
        return result;
    }
    if(inputDef.length&&!validateLen(text,inputDef.length[0],inputDef.length[1])){
    	result.isOk=0;
    	result.errorType="lengthRange";
        return result;
     }
     if(inputDef.regex){
       if(!(new RegExp(inputDef.regex).test(text))){
    	   result.isOk=0;
       	   result.errorType="regex";
           return result;
       }     
     }
     if(inputDef.validate&&!inputDef.validate(input,text,inputDef)){
    	 result.isOk=0;
     	 result.errorType="validate";
         return result;
     }
     return result;
}
function errorValidate(text,input,result,inputDef,options){
	var error=result.errorType;
	if(error=="required"){
		error=" 不能为空";
	}
	else if(error=="lengthRange"){
		error=" 长度必须介于 "+inputDef.length[0]+" - "+inputDef.length[1]+" 之间";
	}
	else if(error=="regex"){
		error= inputDef.regMsg ||" 格式不符合要求";
	}
	else if(error=="validate"){
		return ;
	}
	var label=inputDef.label;
	if(!label){
		if(inputDef.getLabel)
		  label=inputDef.getLabel(input);
		else if(options.getLabel)
		  label=options.getLabel(input);
	}
	if(!label){
		label="";
	}
	label=label+error;
	if(options.showMessage){
		options.showMessage(input,label,inputDef);
	}else{
		showMessage({text:label});
	}
}
function validateInput(options,name){
   var len=options.inputArr.length;
   if(len==0){
	  alert('没有配置校验的字段'); 
	  return false;
   }
   var inputDef,input,text;
   var pass=-1,index;
   var contex;
   if(options.contex)
	   contex=$(options.contex);
   var result;
   var successCount=0;
   for(var i=0;i<len;i++){
     index=i;
     inputDef=options.inputArr[i];
     if(contex){
    	 input=contex.find(inputDef.findExp);}
     else
         input=$(inputDef.findExp);
     if(input.length==0){ 
    	 continue;
     }
     input=input.eq(0);
     if(name&&name!=input.attr("name")){
    	 continue;
     }
     //input.val($.trim(input.val()));
     text=$.trim(input.val());
     result=validateText(text,inputDef,input);
     if(!result.isOk){
    	 if(inputDef.error){
    		 inputDef.error(text,input,result,inputDef);
    	 }
    	 else if(options.error){
    		 options.error(text,input,result,inputDef,options);
    	 }else{
    		 errorValidate(text,input,result,inputDef,options);
    	 }
    	 if(options.breakLoop!==false){
    		 break;
    	 }
     }else{
    	 successCount++;
    	 var urlError=input.attr('error');
    	 if(urlError){
    		 successCount--;
    		 result.isOk=false;
    		 if(options.errorUrl){
    			 options.errorUrl(input,urlError);
    		 }else{
    			 showMessage({text:urlError,top:10});
    		 }
    		 if(options.breakLoop!==false){
    		    return false;
    	     }
    	 }
    	 else if(options.success){
    		 options.success(input,inputDef,options);
    	 }
     }
   }
   if(name){
	   return result&&result.isOk;
   }
   return result&&result.isOk&&len==successCount;
}

function validateLen(text,minLen,maxLen){
   var len=text.length;
   return len>=minLen&&len<=maxLen;
}
function ajaxEx(options)
{
	if(options.showWait!==false){
		showMask(options);
	}
	jQuery.ajax({  
        url: options.url||"",  
        contentType: "application/x-www-form-urlencoded",  
        type: "post",  cache:false,
        dataType:"text",  
        data:options.data||"",  
        success: function(text){
          if(options.showWait!==false){
          	  hideMask(options);
          }
          if(options.success)
          {
             var data=null;
             if(options.isText!==false)
               data=text;
             else
             {
               try{
            	 eval("data="+text);
               }
               catch(ex)
               {
                  data={success:0,error:"客户端解析错误 "+ex.description};
               }
             }
             options.success(data);
          }
        },  
        error: function(e,status,errorThrown){
          if(options.showWait!==false){
        	  hideMask(options);
          }
          if(options.error)
          {options.error(e);}
          else
          {
            alertMsg("操作失败 "+errorThrown);
          }
        }
        ,beforeSend:function(){
        	
        },
        complete: function(){         
        	if(options.complete){options.complete();}}
    }); 
	
}
function ajaxJson(options)
{
	if(options.showWait!==false){
		showMask(options);
	}
	jQuery.ajax({  
        url: options.url||"",  
        contentType: "application/x-www-form-urlencoded",  
        type: "post",  cache:false,
        dataType:"json",  
        data:options.data||"",  
        success: function(rtData){
          if(options.showWait!==false){
          	  hideMask(options);
          }
          if(!rtData){
        	  alert("无效数据");
        	  return ;
          }
          if(rtData.src==1){
        	  if(options.exception){
        		  options.exception(rtData.data);
        	  }else{
        		  alert(rtData.data);
        	  }
          }
          if(options.success)
          {
             options.success(data);
          }
        },  
        error: function(e,status,errorThrown){
          if(options.showWait!==false){
        	  hideMask(options);
          }
          if(options.error)
          {
        	  options.error(e);
          }
          else
          {
            alertMsg("操作失败 "+errorThrown);
          }
        }
        ,beforeSend:function(){
        	
        },
        complete: function(){         
        	if(options.complete){options.complete();}}
    }); 
	
}
function selectDataInPages(options){
    var selector={};
    selector.selectedMap=decodeFromJson($.parseJSON(decodeURIComponent(options.selectedMapJson)||'{}'));
    selector.selectedCount = selector.selectedMap.selectedCount||0;
    selector.selectedMap.selectedCount=0;
    var tempKey,tempChk;
     selector.getSelectedJson=function(){
       var json=[];
       var tempJsonVar;
       for(var i in selector.selectedMap){
          tempJsonVar = selector.selectedMap[i];
          if(tempJsonVar===0){
             continue;
          }
          json.push("\""+i+"\":\""+encodeURIComponent(tempJsonVar)+"\"");
       } 
       return "{"+json.join(",")+"}";
    };
    selector.limitMaxSelect=function(){
        var isAllow=options.maxSelect?options.maxSelect>selector.selectedCount:true;
        if(!isAllow&&options.maxAlert){
          options.maxAlert(selector.selectedCount);
        }
        return isAllow;
    };
    selector.selectHandler=function(chk){
        tempKey = options.getChkUniqueId(chk);
        tempChk = $(chk);
        if(tempChk.is(':checked')&&options.isValid(chk,tempKey,selector.selectedMap)
            &&selector.limitMaxSelect()){
           if(!selector.selectedMap[tempKey]){
              selector.selectedCount+=1;
           }
           selector.selectedMap[tempKey] = options.getSelectedValue?options.getSelectedValue(chk):1;
           tempChk.attr("checked","true");	            
        }else{
           if(selector.selectedMap[tempKey]){
              selector.selectedCount-=1;             
           }    
           selector.selectedMap[tempKey] = 0;
           tempChk.removeAttr("checked");    
        }
        if(options.formsId){
           $.each(options.formsId,function(){
              var json=encodeURIComponent(selector.getSelectedJson());
              setFormValue(this,options.hiddenName||'SelectedParam',json);
           });
        }	         
    };
    var allChks=$('#'+options.TableId).find('tbody tr td:first-child').find('[type=checkbox]');
    allChks.click(function(){
        selector.selectHandler(this);
    });
    allChks.each(function(i,chk){
       tempKey = options.getChkUniqueId(chk);
       if(selector.selectedMap[tempKey]){
           $(chk).attr('checked','true');
       }
    });
    if(options.selectAllBtnId){
      $('#'+options.selectAllBtnId).click(function(){
    	  if($(this).is(':checked')){
    		  allChks.attr('checked','true'); 
    	  }else{
    		  allChks.removeAttr('checked');
    	  }
    	  allChks.each(function(){
    		  selector.selectHandler(this);
    	  });    	  
      });
    }
    selector.getSelectedKey=function(){
       var temp=[];
       for(var i in selector.selectedMap){
          if(!selector.selectedMap[i]) continue;
          temp.push(i);
       }
       return temp;
    };
    selector.getOneValue=function(){
    	var temp;
    	for(var i in selector.selectedMap){
    		if((temp=selector.selectedMap[i]))
    		  return temp;
    	}
    	return 0;
    };
    selector.getAllValue=function(){
    	var temp,tempVal=[];
    	for(var i in selector.selectedMap){
    		if((temp=selector.selectedMap[i]))
    			tempVal.push(temp);
    	}
    	return tempVal;  	
    };
   return selector;
}
function decodeFromJson(map){
   var temp={},count=0;
   for(var i in map){
        temp[i]=decodeURIComponent(map[i]);  
        count+=1;
   }
   temp.selectedCount=count;
   map=null;
   return temp;
}
function setFormValue(frm,name,value){
   var frmObj=$('#'+frm);
   var input;
   if((input=frmObj.find('[name='+name+']')).length==0){
      input=$("<input type='hidden' name='"+name+"' />");
      frmObj.append(input);
   }
   input.val(value);
}
function trimForm(frm){
	var thisObj;
	frm.find("input[type=text]").each(function(){
		thisObj=$(this);
		thisObj.val($.trim(thisObj.val()));
	});
	return frm;
}
function alertMsg(msg,fn){
    alert(msg);
}
function showMask(options)
{	
	if(!options){
		options={};
	}
	var wrap=(options&&options.container)||$('body');
	var msg=(options&&options.loadingMsg)||"正在处理,请稍后。。。";	
	var win=(options&&options.container)||$(window);
	var width=win.outerWidth();
	var height=win.outerHeight();
	if(width==0){
		width=$(document.body).width();
	}
	if(height==0){
		height=$(document.body).height();
	}
	$("<div class=\"wait-mask\"></div>").css({display:"block",width:width,height:height}).appendTo(wrap);
    $("<div class=\"wait-mask-msg\"></div>").html(msg).appendTo(wrap).css({display:"block",left:(width-$("div.wait-mask-msg",wrap).outerWidth())/2,top:(height-$("div.wait-mask-msg",wrap).outerHeight())/2-5});
}
function hideMask(options)
{
	if(!options){
		options={};
	}
	var wrap=(options&&options.container)||$('body');
	wrap.children("div.wait-mask-msg").remove();
    wrap.children("div.wait-mask").remove();
}
function getInputValue(jqObj,allRequired){
	var data={count:0};
	var obj;
	var count=0;
	var isMust=allRequired||false;
	var text;
	var txtObj=jqObj.filter('[name]');
	txtObj.each(function(){
		obj=$(this);
		text=obj.val();
		if(isMust&&text.length==0) return ;
		data[obj.attr('name')]=text;
		count++
	});
	if(count===txtObj.length){
		data.count=count;
	}
	return data;
}
function getAllInputValue(contex){
	var con;
	if(contex)
		con=$(contex);
	else
		con=$('body');
	return getInputValue(con.find('input[type=text],input[type=hidden],input[type=password],select,textarea'));
}
function bindBlurInputEvent(options){
	 var obj;
	 $(options.valuesInputExp).each(function(){
		 obj=$(this);
		 obj.data("oldData",obj.val());
		 obj.blur(function(){
			 blurInput(this,options);
		 });
	 });
}
function blurInput(obj,options){
    var ipt=$(obj);
    var data;
    var oldData=ipt.data('oldData');
    if(oldData&&oldData==ipt.val()){
       return ;
    }
    if(options&&options.valuesInputExp){
       data=getInputValue($(options.valuesInputExp),true);
    }else{
       data=getInputValue(ipt,true);
    }
    if(data.count==0){
      return ;
    }
    if(options&&options.blurData){
       $.extend(data,options.blurData);
    }
    if(options&&options.beginValidate){
    	options.beginValidate(ipt);
    }
    ajaxEx({url:options.url,data:data,success:function(data){
       if(options&&options.afterValidate){
  		  options.afterValidate(ipt,data);
  		  return ;
  	   }
       if(data==="1"){
    	   var temp;
    	   if(options&&options.valuesInputExp)
    		   temp=$(options.valuesInputExp);
    	   else
    		   temp=ipt;
    	   temp.each(function(){
              $(this).removeAttr('error');
           });
       }else{
          ipt.attr('error',data);
       }
    }});
    ipt.data('oldData',ipt.val());
}
function getWebRoot(){
	var pathName = window.document.location.pathname;
	return pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
}
function reLogin(){
	var win=top;
	if(!win){
		win=window;
	}
	win.location.replace(getWebRoot()+'/home/showLoginPage.htm');
}





    /*获取jqgrid一行数据*/
    function getGridData(id) {
        var n = $("#"+id).jqGrid("getGridParam", "selrow");
        return n ? $("#"+id).jqGrid("getRowData", n) : null;
    }