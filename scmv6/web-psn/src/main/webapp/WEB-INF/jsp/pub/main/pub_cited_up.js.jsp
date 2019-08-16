<!-- ajax返回该页面的时候IE会有些问题 -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script language="javascript" type="text/javascript" src="${resscmsns}/scripts/downloads/iris_Octopus.html"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/collect/log_search.js"></script>
<script type="text/javascript" src="${resmod}/js/collect/collect.func_firefox.js"></script>
<div style='display: none; visibility: hidden;'>
  <object classid='clsid:4FA0F169-4CF7-4CE7-A2A1-FF9FC5C7356C' id='IrisOctopus'></object>
</div>
<script for="IrisOctopus" event="OnJobCompleted(param1,param2,param3,param4)">
if(Sys.ie&&Sys.ie.indexOf("11.0")!=-1){
    if(typeof(param1) != "undefined" && typeof(param2) != "undefined" && typeof(param3) != "undefined" && typeof(param4) != "undefined"){
        OnUpdate(param1,param2,param3,param4);  
    }
}
</script>
<script type="text/javascript">
var referencesearch_msg_alert='<s:text name="referencesearch.msg.alert" />';
var referencesearch_not_support_browser='<s:text name="referencesearch.not.support.browser"/>';

if(Sys.firefox)
//document.write('<embed id="IrisEmbedOctopus" type="application/Octopus-plugin" width="0" height="0"></embed>');
    $(document.body).append('<embed id="IrisEmbedOctopus" type="application/Octopus-plugin" width="0" height="0"></embed>');
if(Sys.chrome)
//document.write('<embed id="IrisChromeOctopus" type="application/Chrome-plugin" width="0" height="0"></embed>');
    $(document.body).append('<embed id="IrisChromeOctopus" type="application/Chrome-plugin" width="0" height="0"></embed>');
if (Sys.safari)
//document.write('<embed id="IrisSafariOctopus" type="application/safari-Splugin" width="0" height="0"></embed>');
    $(document.body).append('<embed id="IrisSafariOctopus" type="application/safari-Splugin" width="0" height="0"></embed>');
</script>
<div id="CmdEventDiv" style="display: none"></div>
<div id="CookieDiv" style="display: none">
  <span id="cookie_name"></span> <span id="cookie_value"></span> <span id="cookie_domain"></span>
</div>
<div id="SetcookieDiv" style="display: none">
  <span id="cookie_url"></span> <span id="cookie_str"></span>
</div>
<div id="CmdVerDiv" style="display: none">
  <span id="chrome_ver"></span>
</div>
<script type="text/javascript">
var IrisOctopus=null;//全局插件变量
var ins_url=${dbUrl}; //跳转到更新页面时必须加载此参数
var dataObj;
var needLogin=true;
var dataArr;
var currPubId='';
var content = '${lang}'=='en'?"Loading, please wait a moment...":"正在更新中，请稍后...";
var errorMsg= '${lang}'=='en'?"You don’t have access to the selected database(s). It may be caused by the following reasons:<br/><br/>1.     Your institution hasn’t subscribed to the database;<br/>2. Your institution has subscribed to the database, but your IP address doesn’t support the access;<br/>3. The database is under maintenance. Please try again later.<br/><br/>Should you have any query, please contact us at support@scholarmate.com":"您没有权限使用文献数据库，可能是由于以下原因：<br/>1）您所在的机构没有订购该数据库；<br/>2）您的机构已订购，但您的IP网段无法使用该数据库；<br/>3）所选数据库正在维护中，请稍候再试。<br/><br/>如果您还有疑问和建议，您可以：<br/>1）联系本系统的“在线咨询”，或者<br/>2）发电子邮件到本系统技术支持：support@scholarmate.com";
var reminder = '${lang}'=='en'?"Reminder":"提示";
var optmsgsuc = '${lang}'=='en'?"Update citation successful":"成功更新成果引用";
var optmsgerror = '${lang}'=='en'?"Update error":"更新时发生错误";
var installmsg = '${lang}'=='en'?"Click<a href='javascript:downloadOctopus()'>here</a>Install latest search tool":"点击<a href='javascript:downloadOctopus()'>此处</a>安装最新插件";
var dowmsg1 = '${lang}'=='en'?"Please click [Confirm] to update":"跨文献库检索工具版本已更新，请点击[确定]进行升级";
var dowmsg2 = '${lang}'=='en'?"Please download and install the Federated Search Tool":"请下载并安装跨文献库检索工具";
var datapubId =new Array();
var number=0;
var pubCount;
var totalTime;//总时间
var reducedTime=1;//减去的时间

Loadding_cited = {};
Loadding_cited.show_over = function(name,loginurl){
//  var over = $(".right-wrap");
    var over = $(".pub_list_content");
    var paddingLeft = over.css("padding-left").substring(0,over.css("padding-left").length-2);
    var left = $(over).offset().left;
    var top = $(over).offset().top;
    var width = $(over).width() + parseInt(paddingLeft);
    var height = $(over).height();

    var box_id = name + "_box";
    var main_id = name + "_main";
    
    var box = $("<div class='loadding_div_box' id='"+box_id+"'></div>");
    
    var main="";
    if(loginurl=="error"){
        jAlert(errorMsg,reminder);
        return;
    }else{
         main =  $('<div class="loadding_div_main"  id='+main_id+' align=center STYLE="border-style:solid;border-width:1pt;margin:auto;background:#fff;"><table width="98%"><tr height=15px><td valign="middle" width="20%" align="middle"><img src="${resmod}/images/loading.gif" alt="loading"  width="15px" height="15px" /></td><td style="margin-bottom:10px" align="left" id="td_proginfo"><font color=red> ' + content + '</font></td></tr></table></div>');
    }
                
    $(document.body).append(box);
    $(document.body).append(main);

    if(loginurl==null){
        var size = content.length;
        var main_width = size * 4 + 180;
        $('.loadding_div_main').css('width',main_width);
    }
    
    box.css("width",width);
    box.css("height",height);
    box.css("left",left);
    box.css("top",top);
    
    var main_width = $(main).width();
    var main_height = $(main).height();
    var mx = left + (width - main_width)/2;
    var my = top + (height - main_height)/2;
    main.css("left",mx);
    main.css("top",400);
    box.show();
    main.show();
};

//关闭进度条
Loadding_cited.close = function(name){
    var box_id = name + "_box";
    var main_id = name + "_main";
    $("#"+box_id).remove();
    $("#"+main_id).remove();
};
//查询成果总数
function getPubNumber(){
    $.ajax({
        url : '/pubweb/publication/pubNumber',
        type :'post',
        dataType :'json',
        success : function(data){
            if(data.ajaxSessionTimeOut=='yes'){//rol-1060 更新引用次数操作的超时
                jConfirm(scholar_view.sessionTip2,scholar_view.prompt,function(r){
                    if(r){
                        location.href = ctxpath + "?service="+ encodeURIComponent(location.href);
                    }else{
                        return;
                    }
                });
            }else{
                pubCount=data;
            }
        }
        
    });
}

function doUpdateCited(){
    pubCount=$("#pubTotalCount").text();
    $('div[dialog-id ="updateCite"] .update-citation__counter span:last').text(pubCount);
    totalTime=Math.ceil(pubCount/6);
    $("div[dialog-id='updateCite'] .update-citation__hint span:first span").text(totalTime);
    $.ajax( {
        url : '/pub/cite/ajaxparams',
        type : 'post',
        dataType:"json",
        success : function(data){
            if(data.ajaxSessionTimeOut=='yes'){//rol-1060 更新引用次数操作的超时
                jConfirm(scholar_view.sessionTip2,scholar_view.prompt,function(r){
                    if(r){
                        location.href = ctxpath + "?service="+ encodeURIComponent(location.href);
                    }else{
                        return;
                    }
                });
            }else{
                if(data.result == "success"){
                    dataArr = JSON.parse(data.params);
                    totalTime=Math.ceil(dataArr.length/6);
                    $("div[dialog-id='updateCite'] .update-citation__hint span:first span").text(totalTime);
                    number=pubCount-dataArr.length;
                    getCitedTimes();
                }else{
                    $.smate.scmtips.show('error',optmsgerror);
                    hideDialog("updateCite");
                    Loadding_cited.close("cited");
                }
            }
        },
        error : function(){
            $.smate.scmtips.show('error',optmsgerror);
            hideDialog("updateCite");
            Loadding_cited.close("cited");
        }
    });
}

function OnUpdate(varSitId,varCmdId,varRetInt,varRetStr){
    //varRetInt=1;
    //varRetStr="<data><tc No=\"1\" ut=\"000280863000010\" count=\"212\" link=\"http://apps.webofknowledge.com\"/></data>";
    logSearch.citedOnUpdate(dataArr[0],varSitId,varCmdId,varRetInt,varRetStr);
    if (varRetInt!='0'){    
        hideDialog("updateCite");
        hideDialog("closeCite");
        /* Loadding_cited.close("cited");
        Loadding_cited.show_over("cited","error"); */
        /*  $("#updateError").show(); */
         showDialog("updateError");
    }/* else if (varRetInt=='-4' &&varRetStr==""){
        IrisOctopus.ExecuteCommandJS(32768,1025,1,dataArr[0].citeUrl);
    } */
    else if(isNaN(varRetStr)){
        $('div[dialog-id ="updateCite"] .update-citation__counter span:first').text(number);
        needLogin=false;
        ajaxUpdateScmCited(varRetStr);
         /* resetDataArr(); */ 
    }/* else{
        hideDialog("updateCite");
        Loadding_cited.close("cited");
        Loadding_cited.show_over("cited","error");
    } */
}

function resetDataArr(){
    var newArr=[];
    $.each(dataArr,function(n,o) { 
        if(currPubId!=o.pubIds){
            newArr.push(o);
        }
    }); 
    dataArr = newArr;
    if(dataArr.length>0){
        currPubId = dataArr[0].pubIds; 
        IrisOctopus.ExecuteCommandJS(32768,1025,1,dataArr[0].citeUrl);
    }else{
        $.smate.scmtips.show('success',optmsgsuc);
        Loadding_cited.close("cited");
    }
}

function ajaxUpdateScmCited(varRetStr){
    //alert(varRetStr + "\n pubId = " + dataArr[0].pubIds);
    /* varRetStr="<data><tc No=\"1\" ut=\"A1995RP75500001\" count=\"5955\" link=\"http://apps.webofknowledge.com/CitingArticles.do?product=UA&amp;SID=@SID@&amp;search_mode=CitingArticles&amp;parentProduct=UA&amp;UT=WOS:A1995RP75500001\"/></data>"; */
    var post_data = {'des3PubId':dataArr[0].pubIds,'psnId':dataArr[0].psnId,'citedXml':encodeURIComponent(varRetStr), 'srcDbId':dataArr[0].srcDbId};
    var pubIds=dataArr[0].pubIds;
    /* var post_data = {'des3PubId':'uicwOqvCOzSl3hURETzi7A%3D%3D','psnId':'1001000736709','citedXml':encodeURIComponent('<data><tc No="1" ut="000290240800027" count="111" link=""/></data>')};
    var pubIds='uicwOqvCOzSl3hURETzi7A%3D%3D'; */
    $.ajax({
        url : '/pub/cite/ajaxsave',
        type : 'post',
        dataType:"json",
        data : post_data,
        success : function(data){
            number=number+1;
            reducedTime=reducedTime+1;
            dataArr.splice(0,1);
            getCitedTimes();
        },
        error : function(){
            //scroll(0,1000000);
            $.smate.scmtips.show('error',optmsgerror);
            Loadding_cited.close("cited");
        }
    });
    
}

//更新引用次数
function getCitedTimes(){   
    if (IrisOctopus==null){
        //显示提示信息：安装插件。
        $.smate.scmtips.show('warn',installmsg);
    }
    /* $('div[dialog-id ="updateCite"] .update-citation__counter span:first').text(number); */
    if(reducedTime%6==0){
        totalTime=totalTime-1;
    }
    $("div[dialog-id='updateCite'] .update-citation__hint span:first span").text(totalTime);
    if(dataArr.length>0){
        //alert(dataArr[0].citeUrl);
        /* number=dataArr.length; */
        IrisOctopus.ExecuteCommandJS(32768,1025,1,dataArr[0].citeUrl);
    }else{
        /* hideDialog("updateCite"); */
        document.location.href=window.location.href;
    }
}
//判断当前使用的是校内或校外，1 校内 2
function getLoginSiteId(obj){
    var isiUrl="http://apps.webofknowledge.com";
    //只使用isi获取
    if(typeof ins_url["SCI"]== "undefined"){
        IrisOctopus.SetURL(32768,isiUrl,isiUrl);
        /* $(obj).closest(".dev_pub_list_div").find(".preloader").hide();
        $(obj).closest(".dev_pub_list_div").find(".dev_pub_cite_num").show();
        $(obj).closest(".dev_pub_list_div").find(".dev_pub_cite_name").show(); */
    }else{
        IrisOctopus.SetURL(32768,ins_url["SCI"]["loginUrlInside"],ins_url["SCI"]["loginUrl"]);
        /* $(obj).closest(".dev_pub_list_div").find(".preloader").hide();
        $(obj).closest(".dev_pub_list_div").find(".dev_pub_cite_num").show();
        $(obj).closest(".dev_pub_list_div").find(".dev_pub_cite_name").show(); */
    }
    if(!Sys.ie&&Sys.firefox){//ie11会被误认为火狐加个判断
        if(typeof ins_url["SCI"]!= "undefined"){
            SetCookie("SCI");
        }
    };  
}

function SetCookie(db_code){    
     var search_id= parseInt(ins_url[db_code]["dbBitCode"]);
     var criteria = new GetCookie(db_code);
     IrisOctopus.SetCookie(search_id,criteria);
}

function GetCookie(db_code){
    var host = "";
    var name = "";
    var value = "";
    var obj = Components.classes["@mozilla.org/cookiemanager;1"].getService(Components.interfaces.nsICookieManager);
    var enumerator = obj.enumerator;
    var s = "";
    while (enumerator.hasMoreElements()) {
         var nextCookie = enumerator.getNext();
         nextCookie = nextCookie.QueryInterface(Components.interfaces.nsICookie);
         if (nextCookie.host == ".cityu.edu.hk"){
              host = host + nextCookie.host + ";";
              name = name + nextCookie.name + ";";
              value = value + nextCookie.value + ";";
         };   
    }
    var outside_l_url=ins_url[db_code]["loginUrl"];
    this.url = outside_l_url;                 
    this.host = host;
    this.name = name;
    this.value = value;
}
function initOcx(){
    if(Sys.ie){
        try{        
            if(Sys.ie.indexOf("11.0")!=-1){
                var activeX = document.createElement("object");
                activeX.id = "IrisOctopus";
                activeX.classid = "CLSID:4FA0F169-4CF7-4CE7-A2A1-FF9FC5C7356C";
                document.body.appendChild(activeX);
            }
            IrisOctopus= $("#IrisOctopus")[0];
            IrisOctopus.SetDownLoadFileUrl(pageContext_request_serverName+"${resscmsns}/scripts/iris_Octopus.html?a="+100*Math.random());       
            if(Sys.ie.indexOf("11.0")==-1){     
                obj = IrisOctopus.attachEvent("OnJobCompleted",OnUpdate);   
            }
            IrisOctopus.SetOutTime("-1","180000");
            var strVersion =IrisOctopus.Version; 
           if (!checkLastest(strVersion,CNT_Version)){
                downloadOctopus("cited","update");
                return false;
           }
           return true;
        }catch(e){
            if(!$.browser.mozilla)
            downloadOctopus("cited","install");
            return false;
        };  
    }else if(Sys.firefox){
        var flag=false;
        var mimetype = navigator.mimeTypes["application/Octopus-plugin"];
        try{
            if(mimetype && mimetype.enabledPlugin){
                    flag=true;
            }
            var mimeTypes = navigator.mimeTypes;
            var octopus_Version ="";
            for ( var i = 0; i < mimeTypes.length; i++) {
                if(mimeTypes[i].type=="application/octopus-plugin"){
                    flag=true;
                    octopus_Version = mimeTypes[i].enabledPlugin.description.split("_")[1];
                    break;
                }
            }
            if(flag){
                IrisOctopus= document.embeds["IrisEmbedOctopus"];
                IrisOctopus.SetDownLoadFileUrl(pageContext_request_serverName+"${resscmsns}/scripts/iris_Octopus.html?a="+100*Math.random());
                if (!checkLastest(octopus_Version,FF_Version))
                {
                    downloadOctopus("cited","update");
                    return false;
                }
                return true;
            } else{
                downloadOctopus("cited","install");
                return false;
            }
        }catch(e){
            downloadOctopus("cited","install");
            return false;
        }
    }else if(Sys.safari){
        try{
            if(checkSafariPlugin()){
                reloadSafariPlugin("IrisSafariOctopus","embed","application/safari-Splugin");
                IrisOctopus= document.embeds["IrisSafariOctopus"];
                IrisOctopus.SetDownLoadFileUrl(pageContext_request_serverName+"${resscmsns}/scripts/iris_Octopus.html?a="+100*Math.random());
                if (!checkLastest(getVersionSafari(),MacX_Version))
                {
                    downloadOctopus("cited","update");
                    return false;
                }
                return true;
        }else{
            downloadOctopus("cited","install");
            return false;
        }
            
        }catch(e){
            console.log("异常类型 :"+e);
        }
    }else if(Sys.chrome){
        var flag=false;
        /* var ua = navigator.userAgent.toLowerCase();
        var chromeIndex = ua.indexOf("chrome");
        var chromeVersion = ua.substring(chromeIndex+7, chromeIndex+9);//获取谷歌浏览器的版本号 */
        var mimetype = navigator.mimeTypes["application/chrome-plugin"];
         if(mimetype && mimetype.enabledPlugin){
            flag=true;
         }
        // nonsupport//nonSupportChrome   
           var mimeTypes = navigator.mimeTypes;//获取对象属性
            var octopus_Version ="";
            for ( var i = 0; i < mimeTypes.length; i++) {
                if(mimeTypes[i].type=="application/chrome-plugin"){
                    flag=true;
                    octopus_Version = mimeTypes[i].enabledPlugin.description.split("_")[1];
                    break;
                }
            }      
        if(flag){
            IrisOctopus= document.embeds["IrisChromeOctopus"];    
            IrisOctopus.SetDownLoadFileUrl("http://${pageContext.request.serverName}${res}/scripts/iris_Octopus.html?a="+100*Math.random()); 
            if (!checkLastest(octopus_Version,Chrome_Version))
            {
                downloadOctopus("cited","update");
                return false;
            }  
            return true;
        } else{
            downloadOctopus("cited","install");
            return false;
        }
        /* if((ua.substring((ua.lastIndexOf("/") - 6),ua.lastIndexOf("/"))) =="safari"){//若是谷歌内核浏览器，是谷歌浏览器PROJECTC-415 
            var chromeIndex = ua.indexOf("chrome");
            var chromeVersion = ua.substring(chromeIndex+7, chromeIndex+9);//获取谷歌浏览器的版本号
            if((Sys.win && ua.indexOf('windows nt 6')>-1 && !(ua.indexOf('windows nt 6.1')>-1) && !(ua.indexOf('windows nt 6.0')>-1) && !(ua.indexOf('windows nt 6.2')>-1)) || (chromeVersion >= 45)){//scm-8701
                /* jAlert(referencesearch_not_support_browser,referencesearch_msg_alert,function(){
                    location.href="${snsctx}/publication/msgTipCAB";
                }); */
                /* nonSupportChrome(); 
                return ;
            }else{
                downloadOctopus("cited","install");
                return false; 
            }
        }  */
        
        
        detectChromePlugin("search");
        
        document.getElementById('CookieDiv').addEventListener('myCookieEvent', function() {
              //alert("myCookieEvent has been listened!");  
              var cookiename = document.getElementById("cookie_name").innerText;
              var cookievalue = document.getElementById("cookie_value").innerText;
               var cookiedomain = document.getElementById("cookie_domain").innerText;

             //alert("addEventListener \ncookiename="+cookiename + "\ncookievalue=" + cookievalue + "\ncookiedomain=" + cookiedomain+"\nchromeSearchDbArr="+chromeSearchDbArr);
               
             var db_code = chromeSearchDbArr[0];
                
              var search_id = parseInt(ins_url[db_code]["dbBitCode"]);
              var criteria = new GetCookie(db_code,cookiename,cookievalue,cookiedomain);
              getIrisOctopus().SetCookie(search_id,criteria);
              

              if(isChromePluginSearch){
                  //alert("addEventListener 开始调用Search方法db_code="+db_code+",chromeSearchDbArr="+chromeSearchDbArr);
                  chromeSearchDbArr = resChromeSearchDbArr(db_code);

                  //调用插件Search方法 
                   var tc = new temClass(); 
                   getIrisOctopus().Search(search_id, tc);
                   setWorkingStatus(); 
                    
                    if(chromeSearchDbArr.length>0){
                        db_code = chromeSearchDbArr[0];
                        if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=""){
                            isChromePluginSearch=false;
                            SaveCookie(db_code);
                        }else{
                            ocxSearch_ff(db_code,'');
                        }
                    }                   
              }else{
                  var search_id=ins_url[db_code]["dbBitCode"];
                  var inside_l_url=ins_url[db_code]["loginUrlInside"];
                  var outside_l_url=ins_url[db_code]["loginUrl"];     
                 // alert("addEventListener SetURL db_code="+db_code+"\ninside_l_url="+inside_l_url+"\noutside_l_url="+outside_l_url);
                  getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
                  setWorkingStatus(); 
              }  
        });
    }
}

//判断当前版本是否为最新,如oldVersion小于newVersion,则提示更新
function checkLastest(oldVersion,newVersion)
{
    try
    {
        var oVers;
        var nVers;
        if(Sys.ie || Sys.chrome){
          oVers=oldVersion.split(".");
          nVers=newVersion.split(".");
        }else{
          oVers=oldVersion.split(",");
          nVers=newVersion.split(",");
        }
        for (var i=0;i < nVers.length;i++)
        {
            if (parseInt(oVers[i]) < parseInt(nVers[i]))
                return false;
        }
        return true;    
    }catch(e){
        return false;
    }
}

function downloadOctopus(type,showtype){    
    var msg = "update"==showtype?dowmsg1:dowmsg2;
    /* jConfirm(msg,reminder, function(flag) {
        if(flag){
            location.href="/pub/import/plugin?downloadType=install";
        }else{
            Loadding_cited.close("cited");
        }
    }); */
    smate.showTips._showNewTips(msg, reminder, "updateOrInstallPluginPage('install');", "hideCitedTips();");
    //log
    logSearch.write(8,"","updateCited downloadOctopus "+showtype);
}

function hideCitedTips(){
    Loadding_cited.close("cited");
}

function updateOrInstallPluginPage(showType){
    if(showType == "update"){
        window.open("https://ext.chrome.360.cn/webstore/search/IrisSearch");
    }else{
        location.href = "/pub/import/plugin?downloadType="+showType;
    }
    return false;
}



function  nonSupportChrome(){
    jAlert(referencesearch_not_support_browser,referencesearch_msg_alert,function(){
        location.href="${snsctx}/publication/msgTipCAB";
    })
}
//safari判断start
function comparePlugins(a, b) {
    return a.name.localeCompare(b.name) || a.filename.localeCompare(b.filename) || a.description.localeCompare(b.description);
}

function compareMIMETypes(a, b) {
    return a.type.localeCompare(b.type) || a.description.localeCompare(b.description) || a.suffixes.localeCompare(b.suffixes);
}
function checkSafariPlugin (){
    //判断Safi插件是否存在
    navigator.plugins.refresh(false); // Supposedly helps if new plug-ins were added.
    var flag = false;
    
    Array.prototype.slice.call(navigator.plugins).sort(comparePlugins).forEach(function(plugin)
    {
       Array.prototype.slice.call(plugin).sort(compareMIMETypes).forEach(function(mimeType)
      {
            if(mimeType.type == 'application/safari-splugin')
             {                     
                     flag =  true;
            }
        });                                                                        
    });
    return flag;
}
//mac firefox获取版本
function getVersionMacFF (){
    var newVersion = "";
    for(var i = 0;i<navigator.plugins.length;i++){
        if(navigator.plugins[i].name=="firefox plugin for iris"){
            newVersion = navigator.plugins[i].description;
            break;
        }
    }
    return newVersion;
}
// safari获取插件的版本
function getVersionSafari (){
    navigator.plugins.refresh(false);
    var newVersion="";
    for(var i = 0;i<navigator.plugins.length;i++){
        if(navigator.plugins[i].name=="Safari plugin for iris"){
            newVersion = navigator.plugins[i].description;
            break;
        }
    }
    return newVersion;
}
//safari reload插件
function reloadSafariPlugin (id,kind,type){
    navigator.plugins.refresh(false);
    delElementx(id);
    createElementx(kind,id,type);
}
//safari判断end

//删除embed
function delElementx(id){
    document.getElementById(id).parentNode.removeChild(document.getElementById(id));
}
//添加embed
function createElementx (kind,id,type){
    var body = document.getElementsByTagName("body")[0];
    var tag = document.createElement(kind);
    tag.id = id;
    tag.type=type;
    body.appendChild(tag);
}
//initOcx();
//chrome设置版本号 通过插件设置这里只做触发
function GetChromeVersion(){
    var SendVerEvent = document.createEvent('HTMLEvents');  
    SendVerEvent.initEvent('myVerEvent', true, true);  
    document.getElementById('CmdVerDiv').dispatchEvent(SendVerEvent);
}
</script>
