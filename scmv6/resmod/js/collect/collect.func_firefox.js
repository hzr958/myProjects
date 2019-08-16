//引用插件 /js/plugin/judge-browser/judge-browser.js
var browser = client.browser;
var isChromePluginSearch=false;
var chromeSearchDbArr=[];

function detectFFPlugin(type){
    var flag=false;
    var mimetype = navigator.mimeTypes["application/Octopus-plugin"];
    try{
        if(mimetype && mimetype.enabledPlugin){
                flag=true;
        }
        var mimeTypes = navigator.mimeTypes;
        for ( var i = 0; i < mimeTypes.length; i++) {
            if(mimeTypes[i].type=="application/octopus-plugin"){
                flag=true;
                break;
            }
        }
        if(flag){
            getOctopusVersion();
            if(ctopusDownloadResPath!=""){
                getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
            }else{
                getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
            }
            if (!checkLastest(octopus_Version,FF_Version))
            {
                downloadOctopus(type,"update");
                return false;
            }
            return true;
        } else{
            downloadOctopus(type,"install");
            return false;
        }
    }catch(e){
        downloadOctopus(type,"install");
        return false;
    }
    
}

function detectMacFFPlugin(type){   
    var flag=false;
    var mimetype = navigator.mimeTypes["application/Octopus-plugin"];
    try{
        if(mimetype && mimetype.enabledPlugin){
                flag=true;
        }
        var mimeTypes = navigator.mimeTypes;
        for ( var i = 0; i < mimeTypes.length; i++) {
            if(mimeTypes[i].type=="application/octopus-plugin"){
                flag=true;
                break;
            }
        }
        if(flag){
            if(ctopusDownloadResPath!=""){
                getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
            }else{
                getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
            }
            if (!checkLastest(getVersionMacFF(),MAC_FFVersion))
            {
                downloadOctopus(type,"update");
                return false;
            }
            return true;
        } else{
            downloadOctopus(type,"install");
            return false;
        }
    }catch(e){
        downloadOctopus(type,"install");
        return false;
    }
}

function delectSafariPlugin (type){
    try{
        if(checkSafariPlugin()){
                reloadSafariPlugin("IrisSafariOctopus","embed","application/safari-Splugin");
                if(ctopusDownloadResPath!=""){
                    getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
                }else{
                    getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
                }
                if (!checkLastest(getVersionSafari(),MacX_Version))
                {
                    downloadOctopus(type,"update");
                    return false;
                }
                return true;
            }else{
                downloadOctopus(type,"install");
                return false;
        } 
    }catch(e){
        return false;
    }
}

function detectChromePlugin(type){
    try {
        if (navigator.mimeTypes && navigator.mimeTypes.length > 0) {
              var mimes = navigator.mimeTypes;
              var isDownload=true;
              for (var i=0; i < mimes.length; i++) {
                  if(mimes[i].type == "application/chrome-plugin"){
                      isDownload=false;
                        var plugin = mimes[i].enabledPlugin;
                            if(plugin){
                                getIrisOctopus();
                                setTimeout(function(){
                                    if(ctopusDownloadResPath!=""){
                                        getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
                                    }else{
                                        getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
                                    }
                                    var chrome_octopus_Version=document.getElementById("chrome_ver").innerText;
                                    //alert("chrome_octopus_Version="+chrome_octopus_Version+"\nChrome_Version="+Chrome_Version+",qa:"+(checkLastest(chrome_octopus_Version,Chrome_Version)));
                                    if (!checkLastest(chrome_octopus_Version,Chrome_Version)){
                                        jConfirm("检测到插件版本过低,请更新","确定",function(sure){
                                            if(sure){
                                                /*DelExtension();
                                                setTimeout(function(){
                                                    if(detectChromeAgain()){
                                                        alert("系统无法自动更新插件,请关闭本页手动卸载后重新安装新版本！");
                                                        chromeLoginOut();
                                                        return false;
                                                    }else{
                                                        window.open("https://chrome.google.com/webstore/detail/irissearch/amjpbhldijcjfigaplifnbjmocacleim?hl=zh-CN");
                                                    }
                                                },1000);*/
                                                //WSN_SCM-18783
                                                window.open("https://ext.chrome.360.cn/webstore/search/IrisSearch");
                                            }else{
                                                chromeLoginOut();
                                                return false;
                                            }});
                                        return false;
                                    }
                                }, 1000);
                             } else{ 
                                downloadOctopus(type,"install");
                                return false;
                             }       
                  }
              }
              if(isDownload){
                  downloadOctopus(type,"install");
                    return false; 
              }
              return true;
          } else{
                downloadOctopus(type,"install");
                return false;
           }
    } catch (e) {
        downloadOctopus(type,"install");
        return false;
    }
}   
//Chrome 更新插件专用事件
function DelExtension()
{
    var DelEvent = document.createEvent('HTMLEvents');  
    DelEvent.initEvent('DelExtEvent', true, true);
    document.getElementById('Chrome_DelExtDiv').dispatchEvent(DelEvent);
}
//重新检测chrome的插件
function detectChromeAgain(){
    var mimes = navigator.mimeTypes;
     for (var i=0; i < mimes.length; i++) {
         if(mimes[i].type==="application/chrome-plugin"){
             return true;
             }
     }
     return false;
}
function GetVersion(){
    var SendVerEvent = document.createEvent('HTMLEvents');  
    SendVerEvent.initEvent('myVerEvent', true, true);  
    document.getElementById('CmdVerDiv').dispatchEvent(SendVerEvent);
}


function cancelJob_ff(){
    $(".btnSearch").removeAttr("disabled");
    $("#imp_btn").removeAttr("disabled");
    getIrisOctopus().Cancel("-1","-1");
    //取消任务时，如果判断已经取到了xml，则仅将此部分xml显示
    if (entireXML!=""){
        collect_TaskCount=0;
        importData_ff("","");
    }
    setWorkingStatus();
}

//点击查找按钮
function searchData_ff(type){   
    //获取选定的数据库
    search_DB_Code=getSearch_DB_Code();
    tmpOrgName= getTmpOrgName(type); 
    //ljj
    if('me'==type){     
        ajaxInitPsnAliasJsonFRDB(type,tmpOrgName,goOnSearchData_ff);
    }else{
        goOnSearchData_ff(type,tmpOrgName);
    }
    
}


//ljj 继续 searchData_ff的操作
function goOnSearchData_ff(type,tmpOrgName){
    //将查找条件两边的空格都去掉
    trimCriteria(type);
    //将经历全角转成半角

    //检索文献，清空缓存
    if (!multDB)
        getMultCondition(criteria);
        
    //验证查询条件
    if (!validSearchInput(type)){
        setWorkingStatus();
        return;
    }
    //显示进度条
    showPrepWorking();
    //清空
    orgNames="";
    affiliaton_insName="";
    if (tmpOrgName !=""){
        $.ajax({
            url:'/pubweb/publication/ajaxGetOrgAlias',
            type:'post',
            data:{'orgName':tmpOrgName, 'dbCode': search_DB_Code.join(":")},
            dataType:'json',  
            timeout: 10000,
            success: function(json){
                doAction_ff(type,json);
            },
            error : function(){
                doAction_ff(type,{'default':''});
            }
        });
    }else{
        doAction_ff(type,{'default':''});
    }
}

//=======================================查找，获取功能函数====================================================


//开始查找
function doAction_ff(type,input_orgNames){
    criteria.field_count="0";
    criteria.operators_count="0";
    orgNames=input_orgNames;
    //判断当前查询的机构所使用的语言是否符合指定查询数据库的语言要求
    if (!checkOrgNames(orgNames)){
        setWorkingStatus();
        return;         
    }
    if(type=="me"){
      criteria.topic= CtoH($.trim($("#title").val()));
      criteria.deptName= CtoH($.trim($("#doiSearch").val()));
      psnAliasFirstName=replaceStr(CtoH($.trim($("#fname").val())),"-"," ");  
      psnAliasFirstName=splitNamePY(psnAliasFirstName);
      criteria.firstName  = psnAliasFirstName;
      criteria.lastName   = CtoH($.trim($("#lname").val()));
      criteria.cname  = CtoH($.trim($("#cname").val()));
      criteria.otherName  = CtoH($.trim($("#oname_else").val()));
      if(publicationArticleType=='1'){
        if(($("#publicyear").val().indexOf("-") > -1) 
                && (parseInt($.trim($("#publicyear").val()).split("-")[0]) > parseInt($.trim($("#publicyear").val()).split("-")[1]))){
            $("#publicyear").val($.trim($("#publicyear").val()).split("-")[1] + "-" + $.trim($("#publicyear").val()).split("-")[0]);
        }
        criteria.pubYear    = CtoH($.trim($("#publicyear").val()));
      }
      affiliaton_insName =  CtoH($.trim($("#insName").val()));
      if($("input:radio[name='radiorefx']:checked").val()==1){
          criteria.matchMode=2;
      }else{
          criteria.matchMode=0;
      }
    }
    if(type=="friend"){
      criteria.topic= CtoH($.trim($("#title_friend").val()));
      var fname=replaceStr(CtoH($.trim($("#fname_friend").val())),"-"," ");   
      criteria.firstName  = fname;
      criteria.lastName   = CtoH($.trim($("#lname_friend").val()));
      criteria.cname  = CtoH($.trim($("#cname_friend").val()));
      if(publicationArticleType=='1'){
        if(($("#publicyear_friend").val().indexOf("-") > -1) 
                && (parseInt($.trim($("#publicyear_friend").val()).split("-")[0]) > parseInt($.trim($("#publicyear_friend").val()).split("-")[1]))){
            $("#publicyear_friend").val($.trim($("#publicyear_friend").val()).split("-")[1] + "-" + $.trim($("#publicyear_friend").val()).split("-")[0]);
        }
        criteria.pubYear    = CtoH($.trim($("#publicyear_friend").val()));
      }
      affiliaton_insName =  CtoH($.trim($("#insName_friend").val()));
      criteria.otherName="";
      criteria.matchMode=0;
    }
    if(type=="else"){
        criteria.cname="";
        criteria.firstName="";
        criteria.lastName="";
        criteria.topic= CtoH($.trim($("#title_else").val()));
        criteria.otherName  = CtoH($.trim($("#oname_else").val()));
        if(publicationArticleType=='1'){
          if(($("#publicyear_else").val().indexOf("-") > -1) 
                  && (parseInt($.trim($("#publicyear_else").val()).split("-")[0]) > parseInt($.trim($("#publicyear_else").val()).split("-")[1]))){
              $("#publicyear_else").val($.trim($("#publicyear_else").val()).split("-")[1] + "-" + $.trim($("#publicyear_else").val()).split("-")[0]);
          }
          criteria.pubYear    = CtoH($.trim($("#publicyear_else").val()));
        }
        affiliaton_insName =  CtoH($.trim($("#insName_else").val()));
        criteria.matchMode = 2;
        isShowAccurate = true;
    }
    if(!multDB){
      var fname=replaceStr(CtoH($.trim($("#fname").val())),"-"," ");  
      criteria.firstName  = fname;
      criteria.lastName   = CtoH($.trim($("#lname").val()));
      criteria.cname  = CtoH($.trim($("#cname").val()));
        
      if(($("#publicyear").val().indexOf("-") > -1) 
          && (parseInt($.trim($("#publicyear").val()).split("-")[0]) > parseInt($.trim($("#publicyear").val()).split("-")[1]))){
        $("#publicyear").val($.trim($("#publicyear").val()).split("-")[1] + "-" + $.trim($("#publicyear").val()).split("-")[0]);
      }
      criteria.pubYear    = CtoH($.trim($("#publicyear").val()));
  
      affiliaton_insName =  CtoH($.trim($("#insName").val()));
        criteria.otherName="";
        var repeats = $(".repeat"); 
        var operators = repeats.find(".operators"); 
        var field_value = repeats.find(".field_value");
        var temp_select_field_name = repeats.find(".field_name"); 
        var availCount = 0;
        var jsonItems ="";//存放“其他”条件的值，以json格式存放
        var jsonItem = "";//jsonItems的每一个项
        var splitChart  = getSplitChart();//获取ascii为192的字符作为分隔符
        var operatorVals = "";
        field_value.each(function(i){
            jsonItem = ""; 
            if($.trim($(this).val()) != ""){
                var operatorVal = $(operators.get(i)).val();
                var tempSelectVal = $(temp_select_field_name.get(i)).val();
                if(operatorVal=="null" || operatorVal == null){
                    operatorVal="";
                }
                if(tempSelectVal=="null" || tempSelectVal == null){
                    tempSelectVal="";
                }
                jsonItem = tempSelectVal+splitChart+$(this).val()+splitChart;
                operatorVals += operatorVal+splitChart;
                availCount++;  
                jsonItems += jsonItem;
            }   
        }); 
        if(jsonItems != ""){//有选项，则去掉最后一个逗号
            jsonItems = jsonItems.substring(0, jsonItems.lastIndexOf(splitChart));
            operatorVals = operatorVals.substring(0, operatorVals.lastIndexOf(splitChart));
            criteria.field_count=availCount+"";
            criteria.operators_count=availCount+"";
            criteria.dynamicValue =jsonItems+splitChart+operatorVals;  
        }  
    }
    //设置语言 
    criteria.language=$.trim($("#lang").val());
    //log
    getLogSerachCriteria();
    
    //显示查找条件z
    showSearchCriteria(type);
    //if(Sys.firefox || Sys.safari){
    if(browser.name == "Firefox" || browser.name == "Safari"){
        for(var i=0;i<search_DB_Code.length;i++){
            var db_code=search_DB_Code[i];
            if(db_code=='WanFangYX'){
                isWanFangNext=true;
                searchWanFangYX('1'); 
            }else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=null && ins_url[db_code]["loginUrl"]!=""){
                if(booleanPsnAlias){
                    bulidCurrentRetrieval (db_code);
                }
                getLoginSiteId_ff(db_code);//判断是否已经登录
            }else{
                //开始查找
                setTimeout("ocxSearch_ff('" + db_code +"')",100 * (i+1));       //应hh要求，每次查找前先停100ms，以避免各组件相互冲突
            }
        } 
    }
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        chromeSearchDbArr=search_DB_Code;
        var db_code=search_DB_Code[0];  
        if(db_code=='WanFangYX'){
            isWanFangNext=true;
            searchWanFangYX('1'); 
        }else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=""){
            //判断是否已经登录
            getLoginSiteId_ff(db_code);
        }else{
            //开始查找
            setTimeout("ocxSearch_ff('" + db_code +"')",100 * (i+1));       //应hh要求，每次查找前先停100ms，以避免各组件相互冲突
        }
    }
        
    //显示进度条
    setWorkingStatus();

}

function resChromeSearchDbArr(db_code){
    var newArr=[];  
    for ( var i = 0; i < chromeSearchDbArr.length; i++) {
        if(chromeSearchDbArr[i]!=db_code)
            newArr.push(chromeSearchDbArr[i]);
    }
    return newArr;
}

//获取ascii为x的字符，作为分隔符
function getSplitChart(){
    return String.fromCharCode(1);
}
//尝试重新查找
function retrySearch_ff(db_code)
{ 
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        if(chromeSearchDbArr.length<1){
            chromeSearchDbArr.push(db_code);
        }else{
            var flag = false;
            for ( var i = 0; i < chromeSearchDbArr.length; i++) {
                if(chromeSearchDbArr[i]!=db_code)
                    flag=true;
            }
            if(flag)
            chromeSearchDbArr.push(db_code);
        }
    }
    //如果配置了校外尝试登录url，则认为需要尝试登录
    if (ins_url[db_code]["loginUrl"]!="" && !current_url_set[db_code])
    {
        getLoginSiteId_ff(db_code);
    }else{
        ocxSearch_ff(db_code,'');
    }
    
    setWorkingStatus();
}

//判断当前使用的是校内或校外，1 校内 2
function getLoginSiteId_ff(db_code){
    //alert("getLoginSiteId_ff SetCookie SetURL");
    var search_id=ins_url[db_code]["dbBitCode"];
    var inside_l_url=ins_url[db_code]["loginUrlInside"];
    var outside_l_url=ins_url[db_code]["loginUrl"];
    //if(Sys.firefox){
    if(browser.name == "Firefox"){
        SetCookie(db_code);
        getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
        setWorkingStatus(); 
    }
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        //alert("getLoginSiteId_ff db_code="+db_code);
        isChromePluginSearch=false;
        SaveCookie(db_code);
    }
    //if(Sys.safari){
    if(browser.name == "Safari"){
        try{
        getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
        }catch(e){
            console.log(e+"我发生错误了");
        }
    }
         
}

//直接调用组件开始查找
function ocxSearch_ff(db_code){ 
    if(multDB && booleanPsnAlias){
        bulidCurrentRetrieval (db_code);
    }
    if (db_code=="SCI"){
        criteria.currentLimit="SCI";
    }else if (db_code=="SSCI"){
        criteria.currentLimit="SSCI";
    }else if (db_code=="ISTP"){
        criteria.currentLimit="ISTP,ISSHP";
    }else{
        criteria.currentLimit="";
    }
    var search_id=ins_url[db_code]["dbBitCode"];
    
    //if(Sys.firefox){
    if(browser.name == "Firefox"){
        SetCookie(db_code);
    }
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        //alert("ocxSearch_ff db_code="+db_code);
        isChromePluginSearch=true;
        SaveCookie(db_code);
    }
    
    //设置单位检索式
    if (orgNames[db_code]){   
        criteria.affiliaton=orgNames[db_code]; 
    }else{
        criteria.affiliaton=affiliaton_insName;
    }
      
    //调用插件Search方法 
    //if(Sys.firefox || Sys.safari){
    if(browser.name == "Firefox" || browser.name == "Safari"){
        var tc = new temClass();
        getIrisOctopus().Search(search_id, tc);
        //显示工作进度条
        setWorkingStatus();
    }
    
    //alert("ocxSearch_ff 直接调用组件开始查找 SetCookie getIrisOctopus().Search");
}

var firefoxCookie_dbCode="";
function SetCookie(db_code){
    firefoxCookie_dbCode = db_code;
    GetCookie(db_code);
}

function AnswerProc(EvtAnswer){
    var outside_l_url=ins_url[firefoxCookie_dbCode]["loginUrl"];
    var search_id= parseInt(ins_url[firefoxCookie_dbCode]["dbBitCode"]);    
    var url = "";
    var host = "";
    var name = "";
    var value = ""; 
    var ffhost = EvtAnswer.target.getAttribute("host");
    if (ffhost.indexOf(".cityu.edu.hk;")!=-1){
         url = outside_l_url;                 
         host = ffhost;
         name = EvtAnswer.target.getAttribute("name");
         value = EvtAnswer.target.getAttribute("value");
   } 
    //alert("AnswerProc host="+ffhost+"\nname="+name+"\nvalue="+value);     
    var criteria = new cookieCriteria(url,host,name,value);
    getIrisOctopus().SetCookie(search_id,criteria);
}

function cookieCriteria(url,host,name,value){
    this.url=url;
    this.host=host;
    this.name=name;
    this.value=value;
}

//监听Firefox的扩展返回的信息
//if(Sys.firefox){
if(browser.name == "Firefox"){
    document.addEventListener("BackCookieEvent",function(e) { AnswerProc(e); },false);
}


function GetCookie(db_code,cookiename, cookievalue, cookiedomain){
    //alert("GetCookie start");
    //if(Sys.firefox){  
    if(browser.name == "Firefox"){
        var element = document.createElement("Cmdgetcookie");   
        document.documentElement.appendChild(element);   
        var evt = document.createEvent("Events");   
        evt.initEvent("GetCookieEvent", true, false);   
        element.dispatchEvent(evt); 
    }
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
         this.url = ins_url[db_code]["loginUrl"];
         this.host = cookiedomain;
         this.name = cookiename;
         this.value = cookievalue;
    }
    //alert("GetCookie end");
}

function SaveCookie(db_code)
{
    var customEvent = document.createEvent('HTMLEvents');   
    customEvent.initEvent('myCustomEvent', true, true);
    document.getElementById('CmdEventDiv').dispatchEvent(customEvent);
    //alert("SaveCookie db_code="+db_code);
} 
/**
 * 这个函数是获取GetLinuxCookie的cookie的函数
 * @return
 */
function GetLinuxCookie(searchId){
    //if(Sys.firefox){
    if(browser.name == "Firefox"){
        try {
            if (netscape.security.PrivilegeManager.enablePrivilege) {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            }
        } catch (ex) {
            //alert("!!浏览器设置！\n请在新窗口地址栏输入：about:config ；并回车\n在页面窗口上面的过滤器框中输入：signed.applets.codebase_principal_support\n将鼠标移至过滤后结果行上，通过双击或者右键显示的切换功能将其值设置为：true ，重启浏览器。");
            throw ex;
        }
    }
    
    var strCookie="";
    
    var obj = Components.classes["@mozilla.org/cookiemanager;1"].getService(Components.interfaces.nsICookieManager);
    var enumerator = obj.enumerator;
    while (enumerator.hasMoreElements()) {
         var nextCookie = enumerator.getNext();
         nextCookie = nextCookie.QueryInterface(Components.interfaces.nsICookie);
         if (nextCookie.host.indexOf(".cityu.edu.hk")!=-1){
              var ncName = nextCookie.name;
              var ncValue = nextCookie.value;
               //返回ISI的登录cookie
              if(ncName.indexOf("ezproxy")>=0 && (searchId == 4 || searchId == 32768 || searchId == 65536 || searchId == 131072)){
                strCookie +=ncName+"="+ncValue + ";";
              }
              // //返回scopus的登录cookie
              if(ncName.indexOf("IIIV")>=0 && searchId == 1){
                strCookie +=ncName+"="+ncValue + ";";
              }
         }    
    }
    //其他库不做处理,返回空值
    return strCookie;
} 


function SetLinuxCookies(varHost, varCookstr){  
    //if(Sys.firefox){
    if(browser.name == "Firefox"){
        var element = document.createElement("Cmdsetcookie");         
        element.setAttribute("CookieHost", varHost);   
        element.setAttribute("CookieKey", varCookstr);  
        document.documentElement.appendChild(element); 
        
        var evt = document.createEvent("Events");   
        evt.initEvent("SetCookieEvent", true, false);   
        element.dispatchEvent(evt);
    }
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        var cookieEvent = document.createEvent('HTMLEvents');   
        cookieEvent.initEvent('SetcookieEvent', true, true);
        document.getElementById("cookie_url").innerText = varHost;
        document.getElementById("cookie_str").innerText = varCookstr;
        document.getElementById('SetcookieDiv').dispatchEvent(cookieEvent);
    }
}


function temClass()
{
        var splitChart  = getSplitChart();//获取ascii为192的字符作为分隔符
        this.field_count = criteria.field_count;    
        this.operators_count = criteria.operators_count;
        this.dynamicValue = 
            criteria.database + splitChart +
            criteria.topic + splitChart +
            //criteria.deptName + splitChart +
            criteria.firstName + splitChart +
            criteria.lastName + splitChart +
            criteria.cname + splitChart +
            criteria.pname + splitChart +
            criteria.otherName + splitChart +
            criteria.affiliaton + splitChart +
            criteria.language + splitChart +
            criteria.category + splitChart +
            criteria.pubYear + splitChart +
            criteria.matchMode + splitChart+
            criteria.deptName + splitChart +
            criteria.currentLimit + splitChart +
            criteria.field_count + splitChart +
            criteria.operators_count; 
        if (!multDB){ 
            this.dynamicValue = this.dynamicValue + splitChart + criteria.dynamicValue;   
        }
}


function getDataXML_All_ff(type)
{
    entireXML="";
    var isSelected=false;
    collect_TaskCount=search_DB_Code.length;
    for(var i=0;i<search_DB_Code.length;i++){
        isSelected=getDataXML_ff(type,search_DB_Code[i]) || isSelected;
    }
    if (!isSelected){       
        jAlert(referencesearch_msg_noselected,referencesearch_msg_alert);
    }else{
        $("#imp_btn").attr("disabled",true);
    }
}

function getDataXML_ff(type,db_code)            //获取xml
{
    //检索所有的checkbox,对选定的添加到参数串,发起搜索
    var objdoc=getFrameDoc("if_" + db_code);
    var cbxs = null;
    if(db_code == "CNIPR"){//中国知识产权网的是单选框
        cbxs = $(objdoc).find(":radio[name='iris_id']");
    }else{
        cbxs = $(objdoc).find(":checkbox[name='iris_id']");
    }
    var url="";
    if(cbxs.size() > 0){
        var i;
        var rec_code="";
        var seq_no="";
        for( i=0; i<cbxs.length; i++ ){
            if( cbxs[i].checked ){
                rec_code += cbxs[i].value + ";";
                if (cbxs[i].seq_no)
                    seq_no += cbxs[i].seq_no + ";";
            }
        }
        // 不是全空
        if( "import"==type){
            if (rec_code.length == 0){
                collect_TaskCount--;
                return false;
            }else if(db_code=='WanFangYX'){
                getWanFangYxXml(rec_code);
                return true;
            }else{
                getIrisOctopus().Analyse(ins_url[db_code]["dbBitCode"],rec_code,seq_no);
                setWorkingStatus();
                return true;
            }
        }
        if ("turnpage"==type){
            if (rec_code.length > 0 && confirm(referencesearch_msg_turnpage)==true){                
                getIrisOctopus().Analyse(ins_url[db_code]["dbBitCode"],rec_code,seq_no);
                setWorkingStatus();
                return true;
            }else{
                collect_TaskCount--;
                return false;
            }
        } 
    }else{
        collect_TaskCount--;
    }
}

function setPageCount(obj){
    batchPageSize = obj;
}

function getTotalPagesFF(totalCount,pageSize) {
    if (totalCount == null || totalCount < 0)
        return 0;
    var count = parseInt(totalCount/pageSize);
    if (totalCount % pageSize > 0) {
        count++;
    }
    return count;
}

/* 
varSitId    站点ID 1 scopus 2 sd 4 isi 8 ieee 16 cjn 32 wp 64 wf ;1024 cnipr,
varCmdId    命令ID  0 login;  1 Search;  2 Sort;  3 GoPage;  4 SetRows;  5 Analyse;  11 loginurl;  14 refresh
varRetInt  返回整型值  0： 没有找到记录；-1：没有登录；-2：查找条件出错；-4：无法访问站点。
varRetStr  返回内容
*/  
function OnUpdate(varSitId,varCmdId,varRetInt,varRetStr){
    //alert("update调用varSitId,varCmdId,varRetInt,varRetStr "+varSitId+" "+varCmdId+" "+varRetInt+" "+varRetStr);
    var db_code =getDbCodeByBitCode(varSitId);  
    //if(Sys.chrome)
    //alert("varSitId="+varSitId+"\nvarCmdId="+varCmdId+"\nvarRetInt="+varRetInt);
    if(isBatchSearch){  
        if(varRetInt>0){
            
            //ljj 
            //这里确保从主日志记录中找不到记录的时候，且插件返回的scopus记录条数大于2000，切换成分月检索 scopusSitid scopus库sitid，数值产量
            if(varRetInt>twoThousand && scopusSitid==varSitId){
                isSeparate=true;
            }
            //isSeparte 标记是否是分月检索 openSepSearch 分月检索开关
            if(isSeparate && openSepSearch){ 
                batchTotalCount = varRetInt;
                batchTotalPage=getTotalPages(varRetInt,batchPageSize);      
                sepHasResult(varRetStr,varRetInt,db_code,varSitId);
                
            }else{
                
                if(batchCacheLastPage>1 && batchCacheTotalCount>0 && batchCacheTotalCount!=varRetInt){
                    getIrisOctopus().Cancel(varSitId,9); 
                    emptyBatchConst();
                    getIrisOctopus().BatchFetch(parseInt(varSitId),cacheBatchQuery,parseInt(batchCurrPage));
                    batchCacheLastPage=1;
                    batchCacheTotalCount=0;
                }else{
                    batchTotalCount = varRetInt;
                    batchPageSize =  default_batch_page_count[db_code];
                    batchTotalPage = getTotalPagesFF(batchTotalCount,batchPageSize);
                    if($.trim(varRetStr).length<=0){
                        getIrisOctopus().BatchFetch(parseInt(varSitId),cacheBatchQuery,parseInt(batchCurrPage));
                    }else{
                        if(batchCurrPage<batchTotalPage){   
                            importBatchData(varRetStr,db_code,varSitId,batchCurrPage,batchTotalPage);   
                        }
                        refreshAlert(db_code);
                        if(batchCurrPage==batchTotalPage){
                            setTimeout(function(){
                                importBatchData(varRetStr,db_code,varSitId,batchTotalPage,batchTotalPage);  
                                refreshBatchArrBySearch(varSitId);
                            },10000);
                        }
                        
                        batchCurrPage++;
                    }
                }
            }
            
        }else if(varRetInt==0){
            
            //ljj
            if(isSeparate && openSepSearch){    
                sepHasNoResult(varSitId);
            }else{
                saveBatchHistoryBy0(varSitId);//记录日志
                refreshBatchArrBySearch(varSitId);//调下一个库
            } 
            
        }else if(varRetInt<0){
            if(varRetInt==-4){
                //-4：网络故障，重试
                getIrisOctopus().BatchFetch(parseInt(varSitId),cacheBatchQuery,parseInt(batchCurrPage));
                saveBatchNetThrowsLog(varRetInt,varSitId,batchCurrPage,batchTotalPage);//记录日志
            }else if(varRetInt==-5 || varRetInt==-3){
                //-3：脚本错误(脏数据错误以及脚本处理错误),-5：超时
                refreshAlert(db_code,batchSearchErrorMsgNet+varRetInt);
            }else if(varRetInt==-1){
                //-1：需要登录
                refreshAlert(db_code,batchSearchErrorMsg);
                //refreshAlert(db_code,batchSearchErrorMsgLogin); scopus校外测试    
                
            }else if(varRetInt==-2){
                //-2:检索式错误
                refreshAlert(db_code,batchSearchErrorMsgQuery);
            }
        }
    }else{  
        //log
        onUpdateLog(db_code,varCmdId,varRetInt,varRetStr);
        if (varCmdId==14){
            return ;            //针对refresh，不进行任何操作
        }
        else if(varCmdId==5)        //如果是取详情返回，则直接导入    
        {   
            if(varRetStr!=""){
                //alert(varRetStr);
                importData_ff(varRetStr,db_code);
                return;
            }else{
                varRetStr=replaceStr('<br/>'+referencesearch_msg_oper_err6,'@database@',getDBName(db_code)) + varRetStr;
                writeHtml(db_code,varRetStr,'0',0);
            }
        }
        else if (varRetInt=="-1")       //如果返回-1，则提示需要登录
        {
            if (!ins_url[db_code] || ins_url[db_code]["loginUrl"]=="" || ISIMacSafari(db_code))
            {
                varRetStr=referencesearch_msg_nopermission;
            }else{
                var loginurl = ins_url[db_code]["loginUrl"];
                loginurl = loginurl.replace("http://apps","http://www");
                varRetStr=replaceStr(referencesearch_msg_needlogin,"@loginurl@",loginurl);  
            }
            varRetStr=replaceStr(varRetStr,"@database@",getDBName(db_code));
            writeHtml(db_code,varRetStr,'0',0);
            setDbSerarchResCount(db_code,0);
        }
        else if (varRetInt=="-2")
        {   
            //scm-6608
            //varRetStr=replaceStr('<br/>'+referencesearch_msg_search_error,'@database@',getDBName(db_code)) + varRetStr;
            varRetStr= "没有命中的记录";
            writeHtml(db_code,varRetStr,'0',0);
            setDbSerarchResCount(db_code,0);
        }
        else if (varRetInt=="-6" || varRetInt=="-7")    
        {
            if(navigator.userAgent.indexOf("Windows NT 5")!=-1) { 
                writeHtml(db_code,'<br/>'+referencesearch_hlep_langxp,'0',0);
            } else { 
                writeHtml(db_code,'<br/>'+referencesearch_hlep_langwin7,'0',0);
            }
            setDbSerarchResCount(db_code,0);
        }
        else if (varRetInt=="-3")
        {
            writeHtml(db_code,'<br/>'+referencesearch_msg_nopermission,'0',0);
            setDbSerarchResCount(db_code,0);
        }
        else if (varRetInt=="-4")
        {   
                writeHtml(db_code,'<br/>'+referencesearch_msg_noready,'0',0);
                setDbSerarchResCount(db_code,0);
        }
        else if (varRetInt=="-5")
        {
            writeHtml(db_code,'<br/>'+referencesearch_msg_timeout,'0',0);
            setDbSerarchResCount(db_code,0);
        }
        else
        {
            if (varCmdId==11)
            {
                if(varRetInt=="1")//如果是校内登录
                {
                    current_url_set[db_code]="true";
                    dojob(db_code,ins_url[db_code]["actionUrlInside"],ins_url[db_code]["fulltextUrlInside"],varRetStr);
                    return;
                }
                else if (varRetInt=="2")//如果是校外登录
                {
                    current_url_set[db_code]="true";
                    dojob(db_code,ins_url[db_code]["actionUrl"],ins_url[db_code]["fulltextUrl"],varRetStr);
                    return;
                }else if(varRetInt == '3'){//不支持当前浏览器版本,重新下载插件
                    if(db_code == 'Scopus'){
                        varRetStr=replaceStr(referencesearch_msg_scopus_needreinstall, "@reinstall@", ctxpath+"/publication/downloadCAB?downloadType=install&returnInt="+varRetInt);
                        writeHtml(db_code,varRetStr,'0',0);
                        setDbSerarchResCount(db_code,0);
                    }
                }
            }
            else if (varCmdId==14){}
            else        //如果是search ，则判断是否需要登录
            { 
                if(varCmdId=="1"){ //设置结果条数
                    setDbSerarchResCount(db_code,varRetInt);//重设DB结果数
                }
                if (varRetInt=="0" && varCmdId=="1")  //如果返回值为0,且操作为查找时则认为当前没有查询到成果
                {
                    if("en_US"==browserLanguage && " ,16,256,512,32,64,1024,4096,8192".indexOf(","+varSitId)!=-1){
                        writeHtml(db_code,referencesearch_msg_norecord+en_searchzh_notfind,'0',varRetInt);
                    }else if(("zh_TW"==browserLanguage || "zh_HK"==browserLanguage) && " ,16,256,512,32,64,1024,4096,8192".indexOf(","+varSitId)!=-1){
                        writeHtml(db_code,referencesearch_msg_norecord+tw_searchzh_notfind,'0',varRetInt);
                    }else{
                        writeHtml(db_code,referencesearch_msg_norecord,'0',varRetInt);
                    }
                }else{  
                    varRetStr=replaceStr(varRetStr,'setInterval("refreshDB_ff(','setInterval("parent.refreshDB_ff(');
                    writeHtml(db_code,varRetStr,"1",varRetInt);
                }
            }
        }
        switchDIV(true);
        setWorkingStatus();
        checkedDBListShow(db_code,varRetInt);
    }
}

function GoPage_ff(doc){
    var pages=doc.getElementById("GoPageID").value;
    var db_code=doc.getElementById("db_code").value;
    if (! isDigit(pages)){
        jAlert(referenceSearch_msg_integer,referencesearch_msg_alert);
        return false;
    }
    operationStr=" getIrisOctopus().GoPage(" + ins_url[db_code]["dbBitCode"] +"," + pages + ");";
    if (getDataXML_ff("turnpage",db_code)==false){
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}

function sortitem_ff(doc)
{   
    var sort=doc.getElementById("sortby");
    var db_code=doc.getElementById("db_code").value;
    if(sort){
        getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"],sort.value);
        setWorkingStatus();
    }
}

function sortclassify_ff(doc)
{   
    var sort=doc.getElementById("classify");
    var db_code=doc.getElementById("db_code").value;
    if(sort){
        getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"],sort.value);
        setWorkingStatus();
    }
}

function showitem_ff(doc)
{
    var count=parseInt(doc.getElementById("showpage").value);
    var db_code=doc.getElementById("db_code").value;
    getIrisOctopus().SetRows(ins_url[db_code]["dbBitCode"],count);
    setWorkingStatus(); 
}

function PageDown_ff(doc)
{   
    var db_code=doc.getElementById("db_code").value;
    operationStr = "getIrisOctopus().PageDown(" + ins_url[db_code]["dbBitCode"] + ");";
    if (getDataXML_ff("turnpage",db_code)==false)
    {
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}

function PageUp_ff(doc)
{
    var db_code=doc.getElementById("db_code").value;
    operationStr="getIrisOctopus().PageUp(" + ins_url[db_code]["dbBitCode"] + ");";
    if (getDataXML_ff("turnpage",db_code)==false)
    {
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}

//刷新数据库
function refreshDB_ff(dbid)
{
    getIrisOctopus().ExecuteCommand(dbid,14,0,0);       
}


function importData_ff(dataXml,db_code)     //针对frame 导入
{   
    collect_TaskCount--;
    dataXml=dataXml.substring(dataXml.indexOf("<data "),dataXml.lastIndexOf("</scholarWorks>")) ;       //将返回的xml数据中前后<scholarmWorks>结点都去掉
    entireXML=entireXML + dataXml;
    //alert(entireXML);
    if (dataXml=="" && db_code !="")
        fail_dbname=fail_dbname + ", "+ getDBName(db_code) ;

    if (collect_TaskCount<=0 ) 
    {
        if (entireXML.length>20 )       //如果取到了记录信息，则导入。如没有取到则提示网络不通。
        {       
            if (fail_dbname=="")
                entireXML=encodeURIComponent("<scholarWorks><error />"+entireXML+"</scholarWorks>");
            else
                entireXML=encodeURIComponent("<scholarWorks><error fail_dbname='" + fail_dbname.substr(2) +"'/>"+entireXML+"</scholarWorks>");
            
            fail_dbname=""; 
            
            var url_type='';
            if(publicationArticleType=='4'){
                url_type=ctxpath+'/project/import/initList';
            }
            if(publicationArticleType=='1'){
                url_type='/pubweb/publication/import/initList';
            }
            if(publicationArticleType=='2'){
                url_type=ctxpath+'/reference/import/initList';
            }
            var groupId = $('#groupId').val();
            var folderId = $('#folderId').val();
            var friendPsnId = encodeURIComponent($("#friendPsnId").val());

            $.ajax({
                url:url_type,
                type:'post',
                data:{'inputXml':entireXML, 'publicationArticleType':publicationArticleType,'groupId':groupId,'folderId':folderId,'friendPsnId':friendPsnId },
                dataType:'json',  
                timeout: 300000,
                success: function(json){
                    unCheckedAll(search_DB_Code);
                    entireXML="";
                    switchDIV(true);
                    showImportFrm_ff(json);
                    doAfterImport_ff();
                },
                error : function(xmlhttp,error,desc){
                    unCheckedAll(search_DB_Code);
                    entireXML="";
                    switchDIV(true);
                    doAfterImport_ff();
                     jAlert(referenceSearch_msg_importfailed,referencesearch_msg_alert); 
                     Loadding_div.close('imp_res');
                     $("#imp_btn").attr("disabled",false);
                     try {
                         writeLog(0,"",referenceSearch_msg_importfailed+"\n成果导入出错时,打印此错误情况ajax error：db_code="+db_code+"\nerror="+error);
                    } catch (e) {}
                }
            });
            
        }else{
            jAlert(referenceSearch_msg_importfailed,referencesearch_msg_alert);
            Loadding_div.close('imp_res');
            $("#imp_btn").attr("disabled",false);
            fail_dbname="";
            switchDIV(true);
            try {
                 writeLog(0,"",referenceSearch_msg_importfailed+"\n成果导入出错时,打印此错误情况ajax error：db_code="+db_code+"\nerror="+error);
            } catch (e) {}
            return ;
        }
    }   
}

function doAfterImport_ff(){
    setWorkingStatus();
    if (operationStr.length>0)
    {
        eval(operationStr);
        operationStr="";
    }   
}

function showImportFrm_ff(xmlIdObj){    
    $("#imp_btn").attr("disabled",false);
    var xmlId=xmlIdObj["xmlId"];
    var groupId = encodeURIComponent($('#groupId').val());
    var folderId = encodeURIComponent($('#folderId').val());
    var friendPsnId = encodeURIComponent($("#friendPsnId").val());
    
    if(publicationArticleType=='4'){
        $("#showList").attr("alt",ctxpath+"/project/import/showListFrm?xmlId="+xmlId +"&groupId="+groupId+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click();
    }
    if(publicationArticleType=='1'){
        $("#showList").attr("alt","/pubweb/publication/import/showPubListFrm?publicationArticleType="+publicationArticleType+"&xmlId="+xmlId +"&groupId="+groupId+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click();
    }
    if(publicationArticleType=='2'){
        $("#showList").attr("alt",ctxpath+"/reference/import/showRefListFrm?publicationArticleType="+publicationArticleType+"&xmlId="+xmlId +"&groupId="+groupId+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click();
    }
}

//在苹果系统下safari浏览器ISI 不再支持校外检索 如下为过滤器 SCM-1950
function ISIMacSafari(dbcode){
    var DBcodeArray = new Array("scopus","ssci","sci","istp");
    //var flag = Sys.safari;
    var flag = browser.name == "Safari" ? browser.version : 0;
    var reg = RegExp("^"+dbcode+"$","gi");
    if(flag){
        for(var i = 0 ;i<DBcodeArray.length;i++){
            if(reg.test(DBcodeArray[i])){
                flag = true;
                break;
            }
            if(i==DBcodeArray.length-1){
                flag = false;
            }
        }
    }
    return flag;
}
