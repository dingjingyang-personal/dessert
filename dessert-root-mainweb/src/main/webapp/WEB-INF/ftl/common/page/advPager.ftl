<#macro AdvPageObj pageObj showPages=5 clickEvent="clickPage">
   <#if !(pageObj??)>
     <#return/>
   </#if>
   <@AdvPager pageIndex=pageObj.currentPage pageSize=pageObj.pageSize total=pageObj.recordCount showPages=showPages clickEvent="${clickEvent}" />
</#macro>
<#macro Pages start=1 end =1 selectIndex =1 clickEvent="clickPage">
   <#if end<=1 >
     <a href='javascript:void(0);' class="current" >${selectIndex}</a>
     <#return/>
   </#if>
   <#list start..end as pageIndex >
      <#if (pageIndex==selectIndex)>
         <a href='javascript:void(0);' class="current" >${pageIndex}</a>
      <#else>
         <a href='javascript:${clickEvent}(${pageIndex});' >${pageIndex}</a>
      </#if>
   </#list> 
</#macro>
<#macro AdvPager pageIndex=1 pageSize=5  total=0 showPages=10 clickEvent="clickPage">
   <#assign pageCount=(total/pageSize)?int>
   <#if ((total%pageSize)>0) >
      <#assign pageCount=pageCount+1>
   </#if>
   <#assign currentPage=pageIndex>
   <#if (pageCount<=0)>
      <#assign totalPageCount=1>
   <#else>
      <#assign totalPageCount=pageCount>
   </#if>
   <#if currentPage?int <=0>
     <#assign currentPage=1>
   </#if>
   <div class="snPages" >
     <#list RequestParameters?keys as key>
       <#if (key!="currentPage"&&(key!='pageSize') && RequestParameters[key]??)>
          <input type="hidden" name="${key}" value="${RequestParameters[key]?html}"/>
       </#if>
    </#list>
    <input type="hidden" name="currentPage" value="${currentPage}"/>
    <#--<input type="hidden" name="pageSize"  value="${pageSize}"/>-->
      <#if (pageIndex?int<=1)>
         <span class="prev"><b></b>上一页</span>
      <#else>
         <a class="prev" href="javascript:${clickEvent}(${currentPage-1});" hideFocus='true'><b></b>上一页</a>
      </#if>
      <#if (totalPageCount<=showPages) >
         <#assign startPage=1 endPage=totalPageCount>
      <#else >
         <#if ((pageIndex-showPages/2)<=0)>
            <#assign startPage=1 endPage=showPages>
         <#elseif (totalPageCount-pageIndex-showPages/2)<=0>
             <#assign startPage=totalPageCount-showPages+1  endPage=totalPageCount>
         <#else>
             <#assign startPage=currentPage-showPages/2+1  endPage=startPage+showPages-1>
         </#if>
      </#if>
      <@Pages start=startPage end=endPage selectIndex=currentPage clickEvent=clickEvent></@Pages>
      <#if (pageIndex?int==totalPageCount?int)||endPage<=1>
         <span class="next"  ><b></b>下一页</span>
      <#else>
         <a class="next"  href="javascript:${clickEvent}(${currentPage+1});" hideFocus='true'><b></b>下一页</a>
      </#if>
      <div style='padding-right:10px'><span class="to">当前第<input type="text" pageIndex="${currentPage}" pageCount="${totalPageCount}" value="${currentPage}" />页,共${totalPageCount}页,共 ${total} 记录</span></div>
   </div>
</#macro>