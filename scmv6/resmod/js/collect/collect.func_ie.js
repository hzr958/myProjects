//引用插件 /js/plugin/judge-browser/judge-browser.js
var browser = client.browser;
//============================================查找功能=============================================================
function detectOctopus(type){
    try{        
        if(browser.name == "IE"){
            if(browser.version == "11.0"){
                var activeX = document.createElement("object");
                activeX.id = "IrisOctopus";
                activeX.classid = "CLSID:4FA0F169-4CF7-4CE7-A2A1-FF9FC5C7356C";
                document.body.appendChild(activeX);
            }else{
                window.onload=function(){       
                    obj = getIrisOctopus().attachEvent("OnJobCompleted",OnUpdate_ie);
                }; 
            }
        }
        if(ctopusDownloadResPath!=""){
            getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath+"scripts/iris_Octopus.html?a="+100*Math.random());     
        }else{
            getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());    
        }
        getIrisOctopus().SetOutTime("-1","180000"); 
        var strVersion =getIrisOctopus().Version;   
       if (!checkLastest(strVersion,CNT_Version)){ 
            downloadOctopus(type,"update"); 
            return false;
        }
       return true;
    }catch(e){ 
        downloadOctopus(type,"install");
        return false;
    }
}
 
//点击查找按钮
function searchData_ie(type){   
    //将查找条件两边的空格都去掉
    trimCriteria(type); 
    //将经历全角转成半角
    tmpOrgName= getTmpOrgName(type); 
    //获取选定的数据库
    search_DB_Code=getSearch_DB_Code();   
    //检索文献，清空缓存
    if (!multDB)
        getMultCondition(criteria); 
    //ljj
    if("me"==type){
        //验证查询条件
        var isOk = validSearchInput(type);  
        if (!isOk){
            setWorkingStatus();
            return;
        }
        //显示进度条
        showPrepWorking();
        ajaxInitPsnAliasJsonFRDB(type,tmpOrgName,goOnSearchData_ie);
    }else{
        var isOk = validSearchInput(type);  
        if (!isOk){
            setWorkingStatus();
            return;
        }
        goOnSearchData_ie(type,tmpOrgName);
    }   
}
//ljj 继续searchData_ie的操作
function goOnSearchData_ie(type,tmpOrgName){

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
            success: function(json){doAction_ie(type,json);},
            error : function(){doAction_ie(type,{'default':''});}
        });
    }else{
        doAction_ie(type,{'default':''});
    }
}

//开始查找
function doAction_ie(type,input_orgNames){
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
        if($("input:radio[name='radiorefx']:checked").val())==1){
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
        criteria.matchMode=0;
    }
    //设置语言
    criteria.language=$.trim($("#lang").val());
    
    //显示查找条件
    showSearchCriteria(type);   
    //log
    getLogSerachCriteria();
    
    for(var i=0;i<search_DB_Code.length;i++){
        var db_code=search_DB_Code[i];
        if(db_code=='WanFangYX'){
            isWanFangNext=true;
            searchWanFangYX('1'); 
        }else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=null && ins_url[db_code]["loginUrl"]!=""){
            if(booleanPsnAlias){
                bulidCurrentRetrieval (db_code);
            }
            getLoginSiteId_ie(db_code);//判断是否已经登录
        }else{
            //开始查找
            setTimeout("ocxSearch_ie('" + db_code +"')",100 * (i+1));       //应hh要求，每次查找前先停100ms，以避免各组件相互冲突
        }
    }

}

//开始查找
function doAction_tail(type,input_orgNames){
    orgNames=input_orgNames;
    if (!checkOrgNames(orgNames)){
        return;         
    }
    //设置语言
    criteria.language=$.trim($("#lang").val());
    for(var i=0;i<search_DB_Code.length;i++){
        var db_code=search_DB_Code[i];  
        if(db_code=='WanFangYX'){
            isWanFangNext=true;
            searchWanFangYX('1'); 
        }else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=""){
            //判断是否已经登录
            var search_id=ins_url[db_code]["dbBitCode"];
            var inside_l_url=ins_url[db_code]["loginUrlInside"];
            var outside_l_url=ins_url[db_code]["loginUrl"];
            getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
        }else{
            //开始查找
            setTimeout("ocxSearch_tail('" + db_code +"')",100 * (i+1));     //应hh要求，每次查找前先停100ms，以避免各组件相互冲突
        }
    }
}

function ocxSearch_tail(db_code){
    if (db_code=="SCI"){
        criteria.currentLimit="SCI";
    }else if (db_code=="SSCI"){
        criteria.currentLimit="SSCI";
    }else if (db_code=="ISTP"){
        criteria.currentLimit="ISTP,ISSHP";
    }else{
        criteria.currentLimit="";
    }
    //设置单位检索式
    if (orgNames[db_code]){
        criteria.affiliaton=orgNames[db_code]; 
    }else{
        criteria.affiliaton=affiliaton_insName;
    }
    //调用插件Search方法
    getIrisOctopus().Search(ins_url[db_code]["dbBitCode"],criteria);
}

function pubSearchFormKeyDown(event){
     event= event ? event : (window.event ? window.event : null);
    if(event.keyCode==13)
    { 
        if($("#box_main_result").is(":hidden"))
        {
            $(".btnSearch").click();                                
        }
    }
};
$(document).ready(function(){
    try {
        document.getElementById("mainForm").onkeydown = pubSearchFormKeyDown;
    } catch (e) {}
});

//=====================================================调用插件===================================================

//直接调用组件开始查找
function ocxSearch_ie(db_code){
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
    //设置单位检索式
    if (orgNames[db_code]){   
        criteria.affiliaton=orgNames[db_code]; 
    }else{
        criteria.affiliaton=affiliaton_insName;
    }
    //调用插件Search方法
    getIrisOctopus().Search(ins_url[db_code]["dbBitCode"],criteria);
    //显示工作进度条
    setWorkingStatus();
}

function getTotalPages(totalCount,pageSize) {
    if (totalCount == null || totalCount < 0)
        return -1;
    var count = parseInt(totalCount/pageSize);
    if (totalCount % pageSize > 0) {
        count++;
    }
    return count;
}

/* 
varSitId    站点ID 1 scopus 2 sd 4 isi 8 ieee 16 cjn 32 wp 64 wf ;1024 cnipr,
varCmdId    命令ID  0 login;  1 Search;  2 GoPage;  3 SetRows;  4 Sort;  5 Analyse; 6 GetRetRows; 7 PageDown; 8 PageUp; 9 SearchByString 11 SetUrl; 13 SearchMulti;  14 refresh; 1025 CiteUpdate 
varRetInt  返回整型值  0： 没有找到记录；-1：没有登录；-2：查找条件出错；-4：无法访问站点；-9：要输入验证码
varRetStr  返回内容：如果varRetInt是-1，-4，就不用第二次查找了。
varRetInt 批量检索，此字段返回总条数，如果失败则返回负数.
*/
function OnUpdate_ie(varSitId,varCmdId,varRetInt,varRetStr){
    //alert("varSitId="+varSitId+"\nvarCmdId="+varCmdId+"\nvarRetInt="+varRetInt+"\n"+varRetStr);
    if(isBatchSearch){  //批量检索
        var db_code =getDbCodeByBitCode(varSitId);
        if(varRetInt>0){
                    
            //ljj 这里确保从主日志记录中找不到记录的时候，且插件返回的scopus记录条数大于2000，切换成分月检索
            if(varRetInt>twoThousand && scopusSitid==varSitId){
                isSeparate=true;
            }
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
                    batchTotalPage = getTotalPages(varRetInt,batchPageSize);
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
            
        }else if(varRetInt==0){ //查不到数据
                                    
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
    }else if(isTailSearch){
        refreshTailArrBySearch(varSitId,varRetInt,varRetStr);//调下一个库,最后一个库时记录日志
    }else{
        var db_code =getDbCodeByBitCode(varSitId);  
        //log
        onUpdateLog(db_code,varCmdId,varRetInt,varRetStr);
        
        if('ISTP'==db_code && !isNextIstp && varRetStr.indexOf("Invalid query. Please check that the timespan is within the selected database")>=0){
            criteria.currentLimit="ISTP";
            getIrisOctopus().Search(ins_url[db_code]["dbBitCode"],criteria);
            isNextIstp=true;
            return;
        }
        
        if (varCmdId==14){
            return ;            //针对refresh，不进行任何操作
        }
        else if(varCmdId==5)            
        {   
            if(varRetInt=="-9"){//返回-9，需要输入验证码 中国知识产权网
                var loginurl = "http://search.cnipr.com/search!doOverviewSearch.action";
                varRetStr=replaceStr(referencesearch_msg_needverify,"@loginurl@",loginurl);
                writeHtml(db_code,varRetStr,'0',0);
                setDbSerarchResCount(db_code,0);
            }else{//如果是取详情返回，则直接导入
                importData_ie(varRetStr,db_code);
                return;
            }       
        }
        else if (varRetInt=="-1")       //如果返回-1，则提示需要登录
        {
            if (!ins_url[db_code] || ins_url[db_code]["loginUrl"]=="")
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
            //scm-6608 varRetStr=replaceStr('<br/>'+referencesearch_msg_search_error,'@database@',getDBName(db_code)) + varRetStr;
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
        else if(varRetInt=="-9"){//返回-9，需要输入验证码  中国知识产权网
            var loginurl = "http://search.cnipr.com/search!doOverviewSearch.action";
            varRetStr=replaceStr(referencesearch_msg_needverify,"@loginurl@",loginurl);
            writeHtml(db_code,varRetStr,'0',0);
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
                    varRetStr=replaceStr(varRetStr,'setInterval("refreshDB(','setInterval("parent.refreshDB(');
                    writeHtml(db_code,varRetStr,"1",varRetInt);
                    /*if(db_code=="EI"){
                        getJsonToHtml();
                    }*/
                }
            }
        }
        switchDIV(true);
        setWorkingStatus(); 
        checkedDBListShow(db_code,varRetInt);
    }
}
//如果是json数据类型的则需要到后台处理下
function getJsonToHtml(db_code,varRetStr,varRetInt){
    /*$.ajax({
        url:"/pubweb/publication/search",
        type:"post",
        data:{'db_code':db_code,'jsonHtml':varRetStr},
        dataType:"html",
        timeout: 10000,
        success: function(data){
            $("#list_lable_info").remove();
            $("#list_lable_info").html(data);
        },
        error : function(){
            
        }
    });*/
    $.ajax({
        url: "/pubweb/publication/search",
        dataType:"html",
        type: "post",
        data:{'dbCode':db_code,'jsonHtml':varRetStr},
        success: function(data){
            /*$("#list_lable_info").remove();*/
            /*$("#list_json_info").html(data);*/
            writeHtml(db_code,data,"1",varRetInt);
        },
        error: function(){
            
        }
    });
}

//判断当前使用的是校内或校外2 校内 1
function getLoginSiteId_ie(db_code){
    var search_id=ins_url[db_code]["dbBitCode"];
    var inside_l_url=ins_url[db_code]["loginUrlInside"];
    var outside_l_url=ins_url[db_code]["loginUrl"];
    

    getIrisOctopus().SetURL(search_id,inside_l_url,outside_l_url);
    setWorkingStatus();
}

function getDataXML_All_ie(type){
    
    entireXML="";
    var isSelected=false;
    collect_TaskCount=search_DB_Code.length;
    for(var i=0;i<search_DB_Code.length;i++){
        isSelected=getDataXML_ie(type,search_DB_Code[i]) || isSelected;
    }
    
    if (!isSelected){
        jAlert(referencesearch_msg_noselected,referencesearch_msg_alert);
    }else{
        $("#imp_btn").attr("disabled",true);
    }
}

function getDataXML_ie(type,db_code){//获取xml
    
    //检索所有的checkbox,对选定的添加到参数串,发起搜索
    var objdoc=getFrameDoc("if_" + db_code);
    var cbxs = null;
    if(db_code == "CNIPR"){
        cbxs = $(objdoc).find(":radio[name='iris_id']");
    }else{
        cbxs = $(objdoc).find(":checkbox[name='iris_id']");
    }
    var url="";
    if(cbxs != null && cbxs.size() > 0){
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
            //if(Sys.safari){
            if(browser.name == "Safari"){
                //备注：由于safari 浏览器中的confirm 的按钮和其他浏览器不一致，故只能对提示语进行修改
                referencesearch_msg_turnpage = referencesearch_msg_turnpage1;
            }
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



function GoPage(doc){
    isSearchButton=false;
    var pages=doc.getElementById("GoPageID").value;
    //jAlert(pages);
    var db_code=doc.getElementById("db_code").value;
    if (! isDigit(pages)){
        jAlert(referenceSearch_msg_integer,referencesearch_msg_alert);
        return false;
    }
    operationStr=" getIrisOctopus().GoPage(" + ins_url[db_code]["dbBitCode"] +"," + pages + ");";
    if (getDataXML_ie("turnpage",db_code)==false){
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}

function sortitem(doc){ 
    isSearchButton=false;
    var sort_value=doc.getElementById("sortby").value;
    var db_code=doc.getElementById("db_code").value;
    getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"],sort_value);
    setWorkingStatus();
}

function sortclassify(doc){ 
    isSearchButton=false;
    var sort_value=doc.getElementById("classify").value;
    var db_code=doc.getElementById("db_code").value;
    getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"],sort_value);
    setWorkingStatus();
}

function showitem(doc){
    isSearchButton=false;
    var count=parseInt(doc.getElementById("showpage").value);
    var db_code=doc.getElementById("db_code").value;
    getIrisOctopus().SetRows(ins_url[db_code]["dbBitCode"],count);
    setWorkingStatus(); 
}

function PageDown(doc){ 
    isSearchButton=false;
    var db_code=doc.getElementById("db_code").value;
    operationStr = "getIrisOctopus().PageDown(" + ins_url[db_code]["dbBitCode"] + ");";
    if (getDataXML_ie("turnpage",db_code)==false){
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}

function PageUp(doc){
    isSearchButton=false;
    var db_code=doc.getElementById("db_code").value;
    operationStr="getIrisOctopus().PageUp(" + ins_url[db_code]["dbBitCode"] + ");";
    if (getDataXML_ie("turnpage",db_code)==false)
    {
        eval(operationStr);
        operationStr="";
    }
    setWorkingStatus();
}
//尝试重新查找
function retrySearch_ie(db_code){ 
    isSearchButton=false;
    //如果配置了校外尝试登录url，则认为需要尝试登录
    if (ins_url[db_code]["loginUrl"]!="" && !current_url_set[db_code])
    {
        getLoginSiteId_ie(db_code);
    }else{
        ocxSearch_ie(db_code,'');
    }
}
//精确查找
function preciseSearch(db_code){
    isSearchButton=false;
    criteria.matchMode=1;
    //如果配置了校外尝试登录url，则认为需要尝试登录
    if (ins_url[db_code]["loginUrl"]!="" && !current_url_set[db_code])
    {
        getLoginSiteId_ie(db_code);
    }else{
        ocxSearch_ie(db_code,'');
    }
}
//刷新数据库
function refreshDB(dbid){
    getIrisOctopus().ExecuteCommand(dbid,14,0,0);       
}

function cancelJob(){
    isSearchButton=false;
    $(".btnSearch").removeAttr("disabled");
    $("#imp_btn").removeAttr("disabled");
    if(isWanFangNext==true){
        $("#divMessage").hide();
        $("#divMessage")[0].innerHTML = "";             
        return;
    }
    if(isButtonExclude){
        for(var i=0;i<search_DB_Code.length;i++){
        if(search_DB_Code[i]=="SCI"||search_DB_Code[i]=="SSCI"||search_DB_Code[i]=="ISTP"){
        $(getFrameDoc("if_"+search_DB_Code[i])).find("#btnExclude")[0].disabled=false;     
        }
        }
        isButtonExclude = false;
    }
    getIrisOctopus().Cancel("-1","-1");
    setWorkingStatus();
    //取消任务时，如果判断已经取到了xml，则仅将此部分xml显示
    if (entireXML!=""){
        collect_TaskCount=0;
        importData_ie("","");
    }
}

function importData_ie(dataXml,db_code)     //针对frame 导入
{   
    collect_TaskCount--;
    dataXml=dataXml.substring(dataXml.indexOf("<data "),dataXml.lastIndexOf("</scholarWorks>")) ;       //将返回的xml数据中前后<scholarmWorks>结点都去掉
    entireXML=entireXML + dataXml;
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
                success: function(xmlIdObj){
                    var xmlId=xmlIdObj["xmlId"];
                    if(xmlId==null || xmlId=='null'){
                        jAlert(db_code+" "+referencelist_import_fail_dbname,referencesearch_msg_alert);
                        $("#imp_btn").attr("disabled",false);
                        return;
                    }
                    unCheckedAll(search_DB_Code);
                    entireXML="";
                    switchDIV(true);
                    showImportFrm(xmlIdObj);
                    doAfterImport();
                },
                error : function(xmlhttp,error,desc){
                    unCheckedAll(search_DB_Code);   
                    entireXML="";
                    switchDIV(true);
                    //showImportFrm(xmlIdObj);
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
            try {
               writeLog(0,"",referenceSearch_msg_importfailed+"\n成果导入出错时,打印此错误情况entireXML.length>20：db_code="+db_code);
             } catch (e) {}
            fail_dbname="";
            switchDIV(true);
            return ;
        }
    }   
}

function doAfterImport(){
    setWorkingStatus();
    if (operationStr.length>0){
        eval(operationStr);
        operationStr="";
    }   
}

function showImportFrm(xmlIdObj){   
    artType=artType==null?'':artType;
    var xmlId=xmlIdObj["xmlId"];    
    var groupId = encodeURIComponent($('#groupId').val());
    var folderId = encodeURIComponent($('#folderId').val());
    var friendPsnId = encodeURIComponent($("#friendPsnId").val());
    var queryString = encodeURIComponent($("#queryString").val());
    var savePubType = encodeURIComponent($("#savePubType").val());
    //"&savePubType="+savePubType+
    if(publicationArticleType=='4'){
        $("#showList").attr("alt",ctxpath+"/project/import/showListFrm?xmlId="+xmlId +"&groupId="+groupId+"&savePubType="+savePubType+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&queryString="+queryString+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click();
    }
    if(publicationArticleType=='1'){
        $("#showList").attr("alt","/pubweb/publication/import/showPubListFrm?publicationArticleType="+publicationArticleType+"&xmlId="+xmlId +"&groupId="+groupId+"&savePubType="+savePubType+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&queryString="+queryString+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click(); 
    }
    if(publicationArticleType=='2'){
        $("#showList").attr("alt",ctxpath+"/reference/import/showRefListFrm?publicationArticleType="+publicationArticleType+"&xmlId="+xmlId +"&groupId="+groupId+"&savePubType="+savePubType+"&folderId="+folderId+"&friendPsnId="+friendPsnId+"&queryString="+queryString+"&artType="+artType+"&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=500&width=915");
        $("#showList").click();
    }
    $("#imp_btn").attr("disabled",false);
}