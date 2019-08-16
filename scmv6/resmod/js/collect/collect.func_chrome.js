var browser = client.browser;
var isChromePluginSearch=false;
var chromeSearchDbArr=[];

//插件那边定义了myVerEvent事件，这里只是触发
function GetVersion(){
    var SendVerEvent = document.createEvent('HTMLEvents');  
    SendVerEvent.initEvent('myVerEvent', true, true);  
    document.getElementById('CmdVerDiv').dispatchEvent(SendVerEvent);
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

//Chrome 更新插件专用事件
function DelExtension()
{
    var DelEvent = document.createEvent('HTMLEvents');  
    DelEvent.initEvent('DelExtEvent', true, true);
    document.getElementById('Chrome_DelExtDiv').dispatchEvent(DelEvent);
}

//校验chrome内核的浏览器中的插件是否是最新版本或是否需要安装插件
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
                                        showInstallOrUpdateTips("update");
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













//添加myCookieEvent事件监听，这个事件在插件里面的bridge.js中会触发，这里只是添加监听
function addChromeCookieEventListen(){
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
                      ocxSearch_chrome(db_code,'');
                  }
              }                   
        }else{
            var search_id=ins_url[db_code]["dbBitCode"];
            var inside_l_url=ins_url[db_code]["loginUrlInside"];
            var outside_l_url=ins_url[db_code]["loginUrl"];     
            //alert("addEventListener SetURL db_code="+db_code+"\ninside_l_url="+inside_l_url+"\noutside_l_url="+outside_l_url);
            getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
            setWorkingStatus(); 
        }  
  });
}



function resChromeSearchDbArr(db_code){
    var newArr=[];  
    for ( var i = 0; i < chromeSearchDbArr.length; i++) {
        if(chromeSearchDbArr[i]!=db_code)
            newArr.push(chromeSearchDbArr[i]);
    }
    return newArr;
}




//点击查找按钮
function searchData_chrome(type){   
    //获取选定的数据库
    search_DB_Code=getSearch_DB_Code();
    //获取单位名称
    tmpOrgName= $("#insName").val();
    //将查找条件两边的空格都去掉
    trimCriteria(type);
    //验证查询条件
    if (!validSearchInput(type)){
        setWorkingStatus();
        return;
    }
    setWorkingStatus();
    //加载人员别名数据
    ajaxInitPsnAliasJsonFRDB(type,tmpOrgName,goOnSearchData_chrome);
}


//继续 searchData_chrome的操作
function goOnSearchData_chrome(type,tmpOrgName){
    //清空
    orgNames="";
    affiliaton_insName="";
    if (tmpOrgName !=""){
        ajaxInsAliasName(tmpOrgName);
    }else{
        doAction_chrome(type,{'default':''});
    }
}

//获取单位别名并继续检索操作
function ajaxInsAliasName(tmpOrgName){
    $.ajax({
        url:'/pub/orgname/ajaxalias',
        type:'post',
        data:{'orgName':tmpOrgName, 'dbCode': search_DB_Code.join(":")},
        dataType:'json',  
        timeout: 10000,
        success: function(json){
            doAction_chrome(type,json);
        },
        error : function(){
            doAction_chrome(type,{'default':''});
        }
    });
}





//开始查找
function doAction_chrome(type,input_orgNames){
    criteria.field_count="0";
    criteria.operators_count="0";
    orgNames=input_orgNames;
    //判断当前查询的机构所使用的语言是否符合指定查询数据库的语言要求
    if (!checkOrgNames(orgNames)){
        setWorkingStatus();
        return;         
    }
    //设置检索条件到criteria中
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
    //选中的是按人员姓名检索还是按论文中作者名检索
    if($(".selected-oneself_confirm").attr("value")==1){
        criteria.matchMode=2;
    }else{
        criteria.matchMode=0;
    }
    //设置语言 
    criteria.language=$.trim($("#lang").val());
    //将criteria中的参数值拼接成字符串赋值给logCriteriaStr变量
    getLogSerachCriteria();
    //将查询条件填充在查询结果上方
    showSearchCriteria(type);
    chromeSearchDbArr=search_DB_Code;
    for(var i=0;i<search_DB_Code.length;i++){
        var db_code=search_DB_Code[i];
//        if(db_code=='WanFangYX'){
//            isWanFangNext=true;
//            searchWanFangYX('1'); 
//        }else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=null && ins_url[db_code]["loginUrl"]!=""){
//            //判断是否已经登录
//            isChromePluginSearch=false;
//            if(booleanPsnAlias){
//                bulidCurrentRetrieval (db_code);
//            }
//            getLoginSiteId_chrome(db_code);//判断是否已经登录
//        }else{
//        }
        //开始查找
        setTimeout("ocxSearch_chrome('" + db_code +"')",100 * (i+1));       //应hh要求，每次查找前先停100ms，以避免各组件相互冲突
    }
    //显示进度条
    setWorkingStatus();
    
    
    
    
    
    
    
    
    
    
    
    
    
}



//直接调用组件开始查找
function ocxSearch_chrome(db_code){ 
    if(multDB && booleanPsnAlias){
        bulidCurrentRetrieval (db_code);
    }
    if (db_code=="SCI" || db_code=="SCIE"){
        criteria.currentLimit="SCI";
    }else if (db_code=="SSCI"){
        criteria.currentLimit="SSCI";
    }else if (db_code=="ISTP"){
        criteria.currentLimit="ISTP,ISSHP";
    }else{
        criteria.currentLimit="";
    }
    var search_id=ins_url[db_code]["dbBitCode"];
    isChromePluginSearch=true;
    SaveCookie(db_code);
    //设置单位检索式
    if (orgNames[db_code]){   
        criteria.affiliaton=orgNames[db_code]; 
    }else{
        criteria.affiliaton=affiliaton_insName;
    }
}


//chrome导入成果
function getDataXML_All_chrome(type)
{
    entireXML="";
    var isSelected=false;
    collect_TaskCount=search_DB_Code.length;
    for(var i=0;i<search_DB_Code.length;i++){
        isSelected=getDataXML_chrome(type,search_DB_Code[i]) || isSelected;
    }
    if (!isSelected){       
        smate.showTips._showNewTips(referencesearch_msg_noselected,referencesearch_msg_alert,undefined,undefined,referencesearch_btn_sure,referencesearch_btn_cancel);
    }else{
        $("#imp_btn").attr("disabled",true);
    }
}


//获取xml
function getDataXML_chrome(type,db_code){
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
            if (rec_code.length > 0 ){                
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
    //log
    onUpdateLog(db_code,varCmdId,varRetInt,varRetStr);
    if (varCmdId==14){
        return ;            //针对refresh，不进行任何操作
    }
    else if(varCmdId==5)        //如果是取详情返回，则直接导入    
    {   
        if(varRetStr!=""){
            //alert(varRetStr);
            importData_chrome(varRetStr,db_code);
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
                    varRetStr=replaceStr(referencesearch_msg_scopus_needreinstall, "@reinstall@", "/pub/import/plugin?downloadType=install&returnInt="+varRetInt);
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
    resetSelectedPubSum();
    resetCheckBoxClickEvent(db_code);
}



//成果导入------初始化xml并弹框显示
function importData_chrome(dataXml,db_code)     //针对frame 导入
{   
//    dataXml = '<scholarWorks><error /><data  seq_no=""><publication  tmpsource_url="http://elib.cnki.net/grid2008/DetailProject/DetailView.aspx?projectid=524511001 " source_url="http://elib.cnki.net/grid2008/DetailProject/DetailView.aspx?projectid=524511001 " fulltext_url="  " tmpcite_record_url="" cnkifund_cite_record_url="" etitle="" ctitle="n-3多不饱和脂肪酸对脑发育和功能相关基因DNA甲基化影响的研究  " pub_type="6 " organization="首都医科大学 " eabstract="" cabstract="" ekeywords="" ckeywords="n-3多不饱和脂肪酸;脑;DNA甲基化 " author_names="齐可民  " authors_names_spec="" original="" source="CnkiFund " description="" pubyear="" issue="" volume="" start_page="" end_page="" source_db_code="CnkiFund " cnkifund_cite_times="" issn="" prj_no="" prj_scheme_agency_name="教育部  " prj_scheme_name=" " source_id="" book_title="" series_name="" patent_no="" effective_start_date="" effective_end_date="" start_date="2013-02  " end_date="" amount="120000 " article_number="" doi="" publish_state="" publisher="" organizer="" conf_venue="" proceeding_title="" pub_date_desc="" thesis_ins_name="" thesis_dept_name="" thesis_programme="" keyword_plus="" accept_date=""/></data></scholarWorks>';
    collect_TaskCount--;
    dataXml=dataXml.substring(dataXml.indexOf("<data "),dataXml.lastIndexOf("</scholarWorks>")) ;       //将返回的xml数据中前后<scholarmWorks>结点都去掉
    entireXML=entireXML + dataXml;
    //alert(entireXML);
    if (dataXml=="" && db_code !="")
        fail_dbname=fail_dbname + ", "+ getDBName(db_code) ;

    if (collect_TaskCount<=0 ) 
    {
        if (entireXML.length>20)       //如果取到了记录信息，则导入。如没有取到则提示网络不通。
        {       
            if (fail_dbname=="")
                entireXML=encodeURIComponent("<scholarWorks><error />"+entireXML+"</scholarWorks>");
            else
                entireXML=encodeURIComponent("<scholarWorks><error fail_dbname='" + fail_dbname.substr(2) +"'/>"+entireXML+"</scholarWorks>");
            fail_dbname=""; 
            
            var url_type='';
            if(publicationArticleType=='4'){
                url_type='/prjweb/import/ajaxinit';
            }
            if(publicationArticleType=='1'){
                url_type='/pub/import/ajaxinit';
            }
            if(publicationArticleType=='2'){
                url_type=ctxpath+'/reference/import/initList';
            }
            var groupId = $('#des3GroupId').val();
            var folderId = $('#folderId').val();
            var friendPsnId = encodeURIComponent($("#friendPsnId").val());
            
            $.ajax({
                url:url_type,
                type:'post',
                data:{'inputXml':entireXML, 'publicationArticleType':publicationArticleType,'des3GroupId':groupId,'folderId':folderId,'friendPsnId':friendPsnId },
                dataType:'html',  
                timeout: 300000,
                success: function(data){
                    unCheckedAll(search_DB_Code);
                    entireXML="";
                    switchDIV(true);
                    showImportFrm_chrome(data);
                    doAfterImport_chrome();
                    after_checkValidPub();
                },
                error : function(xmlhttp,error,desc){
                    unCheckedAll(search_DB_Code);
                    entireXML="";
                    switchDIV(true);
                    doAfterImport_chrome();
                    smate.showTips._showNewTips(referenceSearch_msg_importfailed,referencesearch_msg_alert); 
                    Loadding_div.close('imp_res');
                    $("#imp_btn").attr("disabled",false);
                }
            });
            
        }else{
            smate.showTips._showNewTips(referenceSearch_msg_importfailed,referencesearch_msg_alert);
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
//出现成果异常弹框的-确认事件
function checkValidPub_confirmFun(){
  $("#showList").hide();
  $("#selected_to_import_num").html("0");
}
//出现成果异常弹框的-取消事件
function checkValidPub_cancelFun(){
  $("#showList").hide();
  $("#selected_to_import_num").html("0");
}
// 成果异常操作
function after_checkValidPub(){
  var vaildPub = $("#valid_pub").val();
  if(vaildPub != "" && vaildPub == "false"){
    // 需要弹框
    $("#import_pub_list_container").hide();
    smate.showTips._showNewTips(referenceSearch_msg_validpub,referencesearch_msg_alert,checkValidPub_confirmFun(),checkValidPub_cancelFun()); 
    Loadding_div.close('imp_res');
    $("#imp_btn").attr("disabled",false);
  }
}

//成果导入----初始化xml，显示了弹出框后的操作
function doAfterImport_chrome(){
    setWorkingStatus();
    if (operationStr.length>0)
    {
        eval(operationStr);
        operationStr="";
    }   
}

//显示要导入的成果弹出框
function showImportFrm_chrome(xmlIdObj){    
    $("#imp_btn").attr("disabled",false);
    var groupId = encodeURIComponent($('#groupId').val());
    $("#showList").html(xmlIdObj);
    if($.trim($("#import_error_msg").val()) != ""){
      smate.showTips._showNewTips(referenceSearch_msg_importfailed,referencesearch_msg_alert); 
      Loadding_div.close('imp_res');
      $("#import_error_msg").remove();
      return;
    }else{
      $("#showList").show();
    }
}


function SaveCookie(db_code)
{
    var customEvent = document.createEvent('HTMLEvents');   
    customEvent.initEvent('myCustomEvent', true, true);
    document.getElementById('CmdEventDiv').dispatchEvent(customEvent);
//  alert("SaveCookie db_code="+db_code);
} 

function GetCookie(db_code,cookiename, cookievalue, cookiedomain){
//  alert("GetCookie start");
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

//获取ascii为x的字符，作为分隔符
function getSplitChart(){
    return String.fromCharCode(1);
}

//尝试重新查找
function retrySearch_chrome(db_code)
{ 
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
        getLoginSiteId_chrome(db_code);
    }else{
        ocxSearch_chrome(db_code,'');
    }
    
    setWorkingStatus();
}


//判断当前使用的是校内或校外，1 校内 2
function getLoginSiteId_chrome(db_code){
    //alert("getLoginSiteId_ff SetCookie SetURL");
    var search_id=ins_url[db_code]["dbBitCode"];
    var inside_l_url=ins_url[db_code]["loginUrlInside"];
    var outside_l_url=ins_url[db_code]["loginUrl"];
    //if(Sys.chrome){
    if(browser.name == "Chrome"){
        //alert("getLoginSiteId_ff db_code="+db_code);
        isChromePluginSearch=false;
        SaveCookie(db_code);
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



//刷新数据库
function refreshDB_ff(dbid)
{
    getIrisOctopus().ExecuteCommand(dbid,14,0,0);       
}













