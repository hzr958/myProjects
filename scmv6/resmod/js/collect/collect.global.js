
var operationStr="";//获取成果后的操作
var current_url_set={};
var taskCount=0;  //标记当前的任务数,用于进度条的显示
var collect_TaskCount=0;   //标记当前正在收集数据的任务数
var xmlId="";                       
var orgNames={'default':''};        //各数据库的单位别名信息
var search_DB_Code;         //即将要查询的数据库db_code
var entireXML="";           //获取到的xml
var fail_dbname="";
var intell_match=0;             //是否使用了智能匹配
var isShowAccurate=false; //是否显示精确检索
var tmpOrgName;
var entireXML="";
var initDBResutlCount=0;//用于判断查找到第一个db_code
var initHtmlResutlCount=0;//用于判断查找到第一个结果集
var criteria = new SearchCriteria();
var exSetCkShIds = [1024];//排除setCookie的searchId
var browserLanguage;//浏览器语言
//支持浏览器列表 对插件支持的操作系统 和浏览器可自行添加修改
var AvailableBrowsers = {'windows':{'ie':{'min':6,'max':Number.MAX_VALUE,'bit':[32,64]},'chrome':{'min':19,'max':Number.MAX_VALUE,'bit':[32]},'firefox':{'min':4,'max':Number.MAX_VALUE,'bit':[32]},'safari':null,'opera':null},
        'mac':{'chrome':null,'firefox':{'min':6,'max':Number.MAX_VALUE,'bit':[32]},'safari':{'min':5,'max':Number.MAX_VALUE,'bit':[32]}},
        'linux':{'ie':null,'chrome':null,'firefox':null,'safari':null}};
var orgNames="";
var affiliaton_insName = "";
var wanFangYX_Status="0";//标志当前单位成果数据库查询状态，0未操作， 1正在查找，5正在获取
var isWanFangNext=false;// 万方检索可取消参数
var logCriteriaStr="";
var appContextPath = "/resscmwebsns";
var dowOctopusPath=appContextPath+"/scripts/downloads/SCM_SearchTool_zh_CN.exe";
var isIeDow=true;
var tmp_JobInfo= null;//任务状态
var octopus_Version = null;
var isFirefoxDownErrorAlert=false;

var db_code_to_search_id={'WanFangYX':-1,'ROLDB':0,'Scopus':1,'ScienceDirect':2,'ISIWebOfScience':4,'IEEEXplore':135171,'ChinaJournal':16,'CqVip':32,'WanFang':64,'CNIPR':1024,'ITF':512,'Nsfc':4096,'CnkiFund':8192,'EI':16384,'SCI':32768,'SSCI':65536,'ISTP':131072,"PubMed":135168,"Baidu":135169,"GoogleScholar":128,"Cnkipat":135170};
var search_id_to_db_code={'-1':'WanFangYX',0:'ROLDB',1:'Scopus',2:'ScienceDirect',4:'ISIWebOfScience',135171:'IEEEXplore',16:'ChinaJournal',32:'CqVip',64:'WanFang',1024:'CNIPR',512:'ITF',4096:'Nsfc',8192:'CnkiFund',16384:'EI',131072:'ISTP',65536:'SSCI',32768:'SCI',135168:'PubMed',135169:"Baidu",128:"GoogleScholar",135170:"Cnkipat"};
var default_iframe_hegiht={"WanFangYX":1200,"Scopus":2400,"ChinaJournal":1600,'CnkiFund':1000,"EI":2700,"SCI":2300,"SSCI":2200,"ISTP":2900,'ISIWebOfScience':2250,'CNIPR':3085,'ITF':1300,'ScienceDirect':2200,'IEEEXplore':2200,'CqVip':1000,'Nsfc':2000,'WanFang':1500};
var default_batch_page_count={'SCI':20,'SSCI':20,'ISTP':20,'EI':25,'Scopus':20,'WanFang':10,'ChinaJournal':20,'CNIPR':10};
var isSearchButton=false;
var isBatchSearch=false;
var isTailSearch = false;
var tailThorwsCodes='';
//批量导入
var batchDataBaseCount=0;
var batchDataBaseStrs = "";
var batchPageSize=50;
var batchTotalCount=0;
var batchCurrPage=1;
var batchTotalPage=0;
var batchPageCount = 0;
var batchImpResult = 0;
var batchCurrBitId;
var cacheBatchQuery="";
var batchCacheLastPage=1;
var batchCacheTotalCount=0;
var batchStartYear="";
var batchEndYear="";
var batchDbId="";
var isNextIstp=false;
var leftDBshowFlag=false;
var ctopusDownloadResPath="";//设置的插件及脚本下载地址，如为空则下载本站的文件
//人员检索
var booleanPsnAlias = true;//人名检索开关
var isFirstToSearch=0;//表示检索SCI TSTP SSCI文献库的是否为第一次  0为第一次 1 不是
var isButtonExclude=false;
var psnAliasJson=[];//来自检索SCI的数据Json数组格式
var psnAliasJsonStr="";//Json字符串格式
var psnName="";//当前人的名字
var globalType="";//保存当前是否被本人检索
var maxPsnAlias = 100;//排除的别名最大个数
var SCIPsnAliasJsonFRDB=[];//来源项目数据库中的数据SCI ISTP SSCI
var ISTPPsnAliasJsonFRDB=[];
var SSCIPsnAliasJsonFRDB=[];
var allowDbcode = ['SCI','SSCI','ISTP'];
var removePsnAlias = [];//暂存移除数据
var psnAliasFirstName="";
var initNotIsiAlias=[];//检索时发送过去的别名
//发送isi别名时，该数组保存姓名组合不会被插件排除的。如发送别名："au= (ma j*) not (ma jiehua ) or (ma jian)"//[ma j*,ma jian]
var intoIsNotSearchAlias=[];
var psnNumber=0;//排除的别名数

var en_searchzh_notfind="<div style='color:#434049;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;can only be searched with Simplified Chinese system lanugaue， click <a href='"+appContextPath+"/html/system_language_en.html' target='_blank'>here</a> to learn how to set your system lanugaue.</div>";
var tw_searchzh_notfind="<div style='color:#434049;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;只支持系统语言为简体中文的系统，点击<a href='"+appContextPath+"/html/system_language_tw.html' target='_blank'>此处</a>查看如何修改系统语言。</div>";

//浏览器类型
var isIEBrowser = true;
//if(Sys.ie){
if(client.browser.name == "IE" || client.browser.name == "Trident"){
    browserLanguage = navigator.browserLanguage.toLowerCase();
    //根据客户端系统语言下载相应插件
    var systemLanguage = navigator.systemLanguage.toLowerCase();
    if(/x64/gi.test(window.navigator.userAgent.toLowerCase())){
        if("zh-cn"==systemLanguage){
            dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/SCM_SearchToolX64_zh_CN.exe";
        }else{
            dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/SCM_SearchToolX64_en_US.exe";
        }
    }else{
        if("zh-cn"==systemLanguage){
            dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/SCM_SearchTool_zh_CN.exe";
        }else{
            dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/SCM_SearchTool_en_US.exe";
        }
    }
}else{
    browserLanguage = navigator.language;
}
//if(Sys.firefox){
if(client.browser.name == "Firefox"){
    isIEBrowser = false;
    //if(Sys.win){
    if(client.system.name == "Windows"){
        dowOctopusPath="https://addons.mozilla.org/addon/npoctopus/";
    }else if(client.system.name == "Mac"){
        dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/MacxFFOctopus.xpi";
    }
}
//if(Sys.chrome){
if(client.browser.name == "Chrome"){
    isIEBrowser = false;
    dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/Goctopus.crx";
}
//if(Sys.safari){
if(client.browser.name == "Safari"){
    isIEBrowser = false;
    dowOctopusPath=(ctopusDownloadResPath==""?appContextPath:ctopusDownloadResPath)+"/scripts/downloads/MacxSOctopus.pkg";
}

function Profile(){
    this.name = "";
    this.dept ="";
    this.coAuthor="";
    this.email="";
    this.coemail="";
    this.kwd="";
    this.discipline="";
}

function SearchCriteria(){
    this.database   = "";
    this.topic      = "";
    this.firstName  = "";
    this.lastName   = "";
    this.cname      = "";
    this.pname      = "";
    this.otherName  = "";
    //单位名
    this.affiliaton = "";
    this.language   = "zh_CN";
    this.category   = "";
    this.pubYear    = "";
    //0—模糊匹配    1—精确匹配   2  自由检索（不会处理查找条件）
    this.matchMode  = 0;
    this.deptName   ="";//之前废弃的字段，现用于doi
    this.currentLimit="";
    this.mult_condition=null;
    this.field_name=new Array;
    this.field_value=new Array;
    this.operators=new Array;
    this.field_count=0;
    this.operators_count=0;
    this.profile= new Profile;
}
//mac firefox获取版本
function getVersionMacFF (){
    var newVersion = octopus_Version;
    for(var i = 0;i<navigator.plugins.length;i++){
        if(navigator.plugins[i].name=="firefox plugin for iris"){
            newVersion = navigator.plugins[i].description;
            break;
        }
    }
    return newVersion;
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
// safari获取插件的版本
function getVersionSafari (){
    navigator.plugins.refresh(false);
    var newVersion = octopus_Version;
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

//获取控件
function getIrisOctopus(){
    //if(Sys.ie){
    if(client.browser.name == "IE" || client.browser.name == "Trident"){
        return $("#IrisOctopus")[0];
    }
    //if(Sys.firefox){
    if(client.browser.name == "Firefox"){
        return document.embeds["IrisEmbedOctopus"];
    }
    //if(Sys.safari){
    if(client.browser.name == "Safari"){
        return document.getElementById("IrisSafariOctopus");
    }
    //if(Sys.chrome){
    if(client.browser.name == "Chrome"){
        GetVersion();
        return document.embeds["IrisChromeOctopus"];
    }
}

//检查是否按照了检索插件
function isSearchPlusInstalled(){   
    //if(Sys.ie){
    if(client.browser.name == "IE" || client.browser.name == "Trident"){
        try{ 
               var obj = new ActiveXObject("Octopus.IrisOctopus"); 
               var currVersion =obj.Version; 
               if(checkLastest(currVersion,CNT_Version)){
                   return true;
               }         
        }catch(e){    
               return false;
        }   
        return false;          
    }
    //if(Sys.firefox){
    if(client.browser.name == "Firefox"){
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
                getIrisOctopus().SetDownLoadFileUrl(pageContext_request_serverName+appContextPath+"/scripts/iris_Octopus.html?a="+100*Math.random());
                if (!checkLastest(octopus_Version,FF_Version))
                {
                    return false;
                }
                return true;
            } else{
                return false;
            }
        }catch(e){
            return false;
        } 
    }
    //if(Sys.safari){
    if(client.browser.name == "Safari"){
        try {
            if(checkSafariPlugin()){
                return checkLastest(getVersionSafari(),MacX_Version);
            }else{
                return false;
            }
        } catch (e) {
            return false;
        }
        return false;
    }
    //if(Sys.chrome){
    if(client.browser.name == "Chrome"){
        try {
            var mimes = navigator.mimeTypes;
            for (var i=0; i < mimes.length; i++) {
                 if(mimes[i].type == "application/chrome-plugin"){
                        var plugin = mimes[i].enabledPlugin;
                            if(plugin){                             
                                    return true;           
                             }
                  }
              }
        } catch (e) {
            return false;
        }
        return false;               
    }
    
}
 
//根据浏览器版本下载检索插件
function dowOctopus(systemType,type){
    if(systemType!='' && systemType.indexOf('linux')!=-1){
        //linux版本
        location.href=appContextPath+"/scripts/downloads/ffinstall.tar.gz";
    }else if(type=="360"){
        if(client.browser.is360()){
            window.open("https://ext.chrome.360.cn/webstore/search/IrisSearch");
        }
        else if(client.browser.match.indexOf("msie") > -1){
            location.href=dowOctopusPath;
        }
        else{
          scmpublictoast(globalTips.chooseVersion, 2000);
        }
    }else if(type == "IE"){
        if(client.browser.match.indexOf("msie")>-1){
            location.href=dowOctopusPath;
        }
        else{
          scmpublictoast(globalTips.chooseVersion, 2000);
        }
    }else if(type == "FireFox"){
        if(client.browser.name == "Firefox" && client.browser.platform == "Win32"){
            window.open(dowOctopusPath);
        }
        else{
          scmpublictoast(globalTips.chooseVersion, 2000);
        }
    }
    //log
    writeLog(3,"",(isIeDow==true?"ie for ":"firefox for ")+"download");
}


function isIE6(){
    var version = navigator.appVersion;//获取浏览器版本
    return version.indexOf("MSIE 6.0")<0?false:true;
}

//页面点击检索按钮
function searchData(){
    var articleType = "publication";
    if(publicationArticleType=='4'){
        articleType = "project";
    }else if(publicationArticleType=='2'){
        articleType = "reference";
    }else{
        articleType = "publication";
    }
    //检查页面是否超时
     $.ajax( {
            url :  "/pubweb/publication/timeout",
            type : "post",
            data : {},
            dataType : "json",
            timeout:2000,
            success : function(data) {
                if(data.ajaxSessionTimeOut=="yes"){
                    jConfirm(sns_tip_timeout, referencesearch_msg_alert, function(r){
                        if (r) {
                            document.location.href="/pubweb/"+articleType+"/collect";
                        }
                   });
                }else{
                    isSearchButton=true;
                    isBatchSearch = false;
                    isNextIstp=false;
                    leftDBshowFlag=false;
                    var version = navigator.appVersion;//获取浏览器版本
                    if(version.indexOf("MSIE 6.0")<0){//判断是否为IE 6.0（之所以要用indexOf,是因为各个不同浏览器，返回的version字符串内容不一样，不好处理，所有用indexOf）
                        setTimeout(function(){scroll(0,1000000);},100);//解决检索的时候，进度条被遮住
                    }
                    hiddenDBs();//隐藏已经查找过的左侧数据库
                    var type = getSearchType();
                    globalType=type;//在全局变量中记录一个type值,后面方法可以调用  
                    if("/scmwebsns"==ctxpath && "friend"==type && $.autoword["div_plugin_shoose_friends"].vals()==""){
                        return false;
                    }
                    //ie
                    if(isIEBrowser){
                        searchData_ie(type);
                    }else{
                        searchData_ff(type);
                    }
                }
            },error:function(){
                jConfirm(sns_tip_timeout, referencesearch_msg_alert, function(r){
                    if (r) {
                        document.location.href="/pubweb/"+articleType+"/collect";
                    }
               });
            }
    });
}

//获取当前选择的数据库，如果选择的是所有，则获取当前所有可用的数据库
function getSearch_DB_Code_tail(){
    var result=new Array;
    $.each(batchArr,function(n,o) { 
        result.push(getDbCodeByBitCode(o.dbBitId));
    }); 
    return result;
}

function splitNamePY(input) { 
     var pattern = /[^aoeiuv]?h?[iuv]?(ai|ei|ao|ou|er|ang?|eng?|ong|a|o|e|i|u|ng|n)?/gi; 
         var result; 
         var output=""; 
         while((result = pattern.exec(input))!=null && result.index != pattern.lastIndex){ 
             //alert("Match:" + result[0] + "， " + "Postion:" + result.index +", next start postion:" + pattern.lastIndex); 
             output=output+ " " +result[0]; 
          } 
     return $.trim(output); 
    } 

//插件监控
function tailSearchData(){
    isTailSearch=true;
    isSearchButton=false;
    isBatchSearch = false;

    cacheBatchQuery="";
     if(batchArr.length<=0)
        return;
    batchDataBaseStrs = "";
    $.each(batchArr,function(n,o) { 
        if(n>0)
        batchDataBaseStrs+=ins_url[getDbCodeByBitCode(o.dbBitId)]["dbName"]+",";
    }); 
    batchDataBaseCount = batchArr.length;
    batchCurrBitId = batchArr[0].dbBitId;
    cacheBatchQuery = batchArr[0].dbQuery;
    var code = getDbCodeByBitCode(batchCurrBitId);
    var txt = "";
    if(batchArr.length<=1){
        txt = batchLoadTxt11.replace("@currDataBase@",ins_url[code]["dbName"]);
    }else{
        txt = batchLoadTxt12.replace("@currDataBase@",ins_url[code]["dbName"]);
        txt = txt.replace("@dataBase@",batchDataBaseStrs);
    }
    Loadding_batch.close("batch");
    Loadding_batch.show_over("batch","rol_main",batchLoadTitle,batchLoadCancel,txt,true);
    
    if (code=="SCI"){
        criteria.currentLimit="SCI";
    }else if (code=="SSCI"){
        criteria.currentLimit="SSCI";
    }else if (code=="ISTP"){
        criteria.currentLimit="ISTP,ISSHP";
    }else{
        criteria.currentLimit="";
    }
    criteria.affiliaton=cacheBatchQuery;
    criteria.language=$("#lang").val();
    criteria.matchMode=2;
    getIrisOctopus().Search(parseInt(batchCurrBitId),criteria);
}


//ljj scopus分月检索批量导入
var sepTotalPage=0;      //当前检索的月份总页数
var sepCurrentPage=0;    //当前检索的月份已经导入的页数
var sepTotalCount=0;     //当前检索的月份总条数
var sepCurrentCount=0;   //当前检索的月份已经导入的条数
var sepCurrentYear=2000; //当前检索年份
var sepCurrentMonth=1;   //当前检索月份
var isSeparate=false;    //标记是否是分月检索
var negativeOne=-1; //数值常量
var historyId=negativeOne;       //主日志记录的主键id
var sepHistoryId=negativeOne; //分月检索记录的主键id
var twoThousand =2000; //scopus库限制返回结果条数
var scopusDBid=8; //scopus库dbid，数值产量
var scopusSitid=1; //scopus库sitid，数值产量
var scopusPageSize=20; //页大小
var delayTime=5000; //重新激活插件延迟时间(毫秒)
var finishSepBatch=false; //是否完成了分月检索
var finalCacheBatchQuery='';//备份初始检索式
//不归类到任意一个月份的其他成果
var notInAnyMonthQuery=' and not PUBDATETXT({january} OR {february} OR {march} OR {april} OR {may} OR {june} OR {july} OR {august} OR {september} OR {october} OR {november} OR {december})';//
var openSepSearch=true;//分月检索功能块“开关”


//ljj 调整scopus库在列表中的位置 
function orderScopus(){
            
    var hasScopus=false;
    var dbId='';
    var dbBitId='';
    var startYear='';
    var endYear='';
    var dbQuery='';
    var newArr=[];

    $.each(batchArr,function(n,o) { 
        if(o.dbId!='8'){ 
            newArr.push(o);
        }else{
            hasScopus = true;
            dbId = o.dbId;
            dbBitId = o.dbBitId;
            startYear = o.startYear;
            endYear = o.endYear;
            dbQuery = o.dbQuery;
        }
    }); 
    
    if(hasScopus){ 
        var query = {'dbId':dbId,'dbBitId':dbBitId,'startYear':startYear,'endYear':endYear,'dbQuery':dbQuery};
        newArr.push(query);
        batchArr = newArr;
    }   
}


//ljj 数字转换成英文缩写 
function getMonth(mon){
    
    var month = new Array();
    month['1']='Jan';
    month['2']='Feb';
    month['3']='Mar';
    month['4']='Apr';
    month['5']='May';
    month['6']='Jun';
    month['7']='Jul';
    month['8']='Aug';
    month['9']='Sep';
    month['10']='Oct';
    month['11']='Nov';
    month['12']='Dec';
    
    return month[mon];
}


///ljj 更换检索式
function changeQueryStr(varSitId,hasSepResult){
                            
    //1、点击检索按钮开始的情况
    //2、存在主日志记录，并且数据总条数超过2K
    if(hasSepResult){ 
        
        $.ajax({  
            url:ctxpath+'/refserver/ajaxGetSeparateLastCurrPage',
            type:'post',
            data:{'sepHistory.historyId':historyId},
            dataType:'json',
            //timeout:30000,
            success:function(sep){
                
                //“主日志记录有，分月检索日志记录无
                if(negativeOne==sep.id){ //初始化分月检索开始的年和月 
                    sepCurrentYear=batchArr[0].startYear;
                    sepCurrentMonth=1; 
                    sepCurrentPage=1; //从第一页开始
                    
                //“主日志记录有，分月检索日志记录有
                }else{
                    sepHistoryId = sep.id;
                    sepTotalCount=sep.totalCount;
                    sepTotalPage=sep.totalPage;
                    sepCurrentCount=sep.currentCount;
                    sepCurrentPage=sep.currentPage;
                    sepCurrentYear=sep.currentYear;
                    sepCurrentMonth=sep.currentMonth;
                    sepCurrentPage = sepCurrentPage>1?(parseInt(sepCurrentPage)+1):sepCurrentPage; 
                }
                
                
                if(13==sepCurrentMonth){  //处理在13月中断的情况
                    cacheBatchQuery = finalCacheBatchQuery + sepCurrentYear + notInAnyMonthQuery;//第13条检索式
                    show_msg_tips_batch_imp("yes","检索式=1："+cacheBatchQuery);
                }else{
                    var index ;
                    if(negativeOne!=cacheBatchQuery.indexOf('PUBYEAR', 0)){
                        index = cacheBatchQuery.indexOf('PUBYEAR', 0);
                        cacheBatchQuery = cacheBatchQuery.substring(0, index)+' PUBDATETXT('+getMonth(sepCurrentMonth)+' '+sepCurrentYear+')';//更换检索式
                    }else{
                        index = cacheBatchQuery.indexOf('PUBDATETXT', 0);
                    }
                    show_msg_tips_batch_imp("yes","检索式=2："+cacheBatchQuery);
                    cacheBatchQuery = cacheBatchQuery.substring(0, index)+' PUBDATETXT('+getMonth(sepCurrentMonth)+' '+sepCurrentYear+')';//更换检索式
                }
                
                getIrisOctopus().BatchFetch(parseInt(varSitId),cacheBatchQuery,parseInt(sepCurrentPage)); // 开始/重新检索
                
            },
            error:function( xmlhttp,error,desc){
                refreshAlert('Scopus',batchLoadTimetOut);
            }
        });
        
    //"检索插件"回调的情况
    }else{  
        
        if(13==sepCurrentMonth){ //最后一条检索式，检索没有归类到月份的成果记录
            cacheBatchQuery = finalCacheBatchQuery + sepCurrentYear + notInAnyMonthQuery;//第13条检索式
            show_msg_tips_batch_imp("yes","切换检索式=3："+cacheBatchQuery);
        }else{
            var index;
            if(negativeOne!=cacheBatchQuery.indexOf('PUBYEAR', 0)){
                index = cacheBatchQuery.indexOf('PUBYEAR', 0);
            }else{
                index = cacheBatchQuery.indexOf('PUBDATETXT', 0);
            }
            cacheBatchQuery = cacheBatchQuery.substring(0, index)+' PUBDATETXT('+getMonth(sepCurrentMonth)+' '+sepCurrentYear+')';//更换检索式
            show_msg_tips_batch_imp("yes","检索式=4："+cacheBatchQuery);
        }
        
        getIrisOctopus().BatchFetch(parseInt(varSitId),cacheBatchQuery,parseInt(sepCurrentPage)); //重新检索
    }
}

// varRetInt ： 插件检索返回的数据条数
// varSitId ： 检索的站点ID
// hasRetStr : =true 插件有数据的时候记录主日志，=false 插件没有数据的时候记录主日志
// ljj 没有主日志记录，生成一条新的主日志记录 
function createNewHistory(varRetInt,varSitId,hasRetStr){
    
    //ljj
    getBatchYear(varSitId,false);
    
    $.ajax({
        url:ctxpath+'/refserver/createNewHistory',
        type:'post',
        data:{'history.dbId':batchDbId,
              'history.startYear':batchStartYear,
              'history.endYear':batchEndYear,
              'history.totalCount':batchTotalCount,
              'history.totalPage':batchTotalPage,
              'history.state':0,
              'history.currCount':0,
              'history.currPage':0},
        dataType:'json',  
        success: function(data){
            historyId=data.id;
            if(hasRetStr){
                if(negativeOne!=data.id && varRetInt>twoThousand){ //插件返回数据总条数>2K，切换成分月检索
                    sepCurrentMonth=1;
                    sepCurrentYear=batchArr[0].startYear;
                    getIrisOctopus().Cancel(batchCurrBitId,9);
                    changeQueryStr(varSitId,false);
                }
            }else{
                emptySepCont();     
                updateSepHistoryState(1); 
                createNewSepHistory(0,null,null,null,false); //没有xml数据返回，依然记录这个月的检索日志
                sepCurrentMonth++; //切换到下一个月
                moveNextMonth(varSitId);
            }
        },
        error:function(){
            // 新的主日志记录表保存失败
            refreshAlert('Scopus','数据导入时出现服务器错误，请稍后再试~');
        }
    });
}


//ljj 生成一条新的分月检索日志记录
function createNewSepHistory(varRetInt,varRetStr,db_code,varSitId,hasRetStr){
        
    sepTotalPage = getTotalPages(varRetInt,batchPageSize);
    sepTotalCount = varRetInt;
        
    $.ajax({
        url:ctxpath+'/refserver/createNewSepHistory',
        type:'post',
        data:{'sepHistory.currentYear':sepCurrentYear,
              'sepHistory.currentMonth':sepCurrentMonth,
              'sepHistory.totalCount':sepTotalCount,
              'sepHistory.totalPage':sepTotalPage,
              'sepHistory.currentCount':sepCurrentCount,
              'sepHistory.currentPage':sepCurrentPage,
              'sepHistory.state':0,
              'sepHistory.historyId':historyId},
        dataType:'json',  
        success: function(data){
            
            if(0!=data.id){
                sepHistoryId=data.id;//可能是-1或者返回的当前保存的分月检索的id
            }
            
            if(negativeOne!=data.id && hasRetStr){
                
                updateSepHistoryState(1);
                sepCurrentPage++; 
                importSepBatchData(varRetStr,db_code,varSitId,sepCurrentPage,sepTotalPage); //保存数据
                sepCurrentCount=scopusPageSize*sepCurrentPage; 
                
                if(sepCurrentCount>sepTotalCount){
                    sepCurrentCount=sepTotalCount;
                }
                
                //单个月的数据条数大于2K，或者导入了所有页，更换检索式,修改当前月的导入状态state=1
                if(sepCurrentCount>twoThousand || sepCurrentPage>=sepTotalPage){
                    emptySepCont();
                    sepCurrentMonth++; //切换到下一个月
                    moveNextMonth(varSitId);                    
                }
            }
        },
        error:function(){
            refreshAlert('Scopus','数据导入时出现服务器错误，请稍后再试~');
        }
    });
    
}

//ljj 修改分月检索的导入状态
//后一个月的开始，修改前一个月的导入状态，如果是最后一个月，自己修改自己的导入状态
function updateSepHistoryState(state){
    if(negativeOne!=sepHistoryId){
        $.ajax({
            url:ctxpath+'/refserver/updateSepHistoryState',
            type:'post',
            data:{'sepHistory.id':sepHistoryId,'sepHistory.state':state},
            success:function(){},
            error:function(){}
        }); 
    }
}


//ljj 初始化分月检索常量
function emptySepCont(){    
    sepCurrentPage=0;
    sepTotalPage=0;
    sepCurrentCount=0;
    sepTotalCount=0;
}


//ljj 导入分月检索的每一页数据
function importSepBatchData(dataXml,db_code,varSitId,currPage,totalPage){
    
    updateSepHistoryState(1); //修改上一个月导入状态
    
    dataXml=dataXml.substring(dataXml.indexOf('<data '),dataXml.lastIndexOf('</scholarWorks>')) ;
    dataXml=encodeURIComponent('<scholarWorks><error />'+dataXml+'</scholarWorks>');        
    
    $.ajax({
        url:ctxpath+'/publication/import/ajaxSepBatchSave',     
        type:'post',
        dataType:'json', 
        data:{'inputXml':dataXml,'publicationArticleType':1,
            'sepThrows.historyId':historyId,
            'sepThrows.sepHistoryId':sepHistoryId, 
            'sepThrows.currPage':sepCurrentPage, //当月导入了多少页，到了service中需要检索原来已经导入了多少条，然后相累加
            'sepThrows.pageCount':sepTotalPage,
            'pageSize':scopusPageSize}, //当月的总页数，到了service中需要检索原来的页数，然后相累加
        success:function(data){
            //保存数据成功，更改分月检索的state状态
        },
        error:function(){}
    });                  
    
    
    var msg='当前正在抓取Scopus库'+sepCurrentYear+'年的成果（当前第'+sepCurrentPage+'页/总'+sepTotalPage+'页），请稍后....';
    refreshAlert('Scopus',msg);
    
}

//ljj 获取检索开始的年和结束的年
// init 处理检索式边界值
function getBatchYear(varSitId,init){
    
    $.each(batchArr,function(n,o) { 
        if(o.dbBitId==varSitId){
            batchDbId = o.dbId;
            batchStartYear = o.startYear;
            batchEndYear = o.endYear;
            return;
        }
    }); 

}



//ljj 分月检索，插件返回数据 
function sepHasResult(varRetStr,varRetInt,db_code,varSitId){
    
    if(varRetInt>twoThousand && negativeOne!=cacheBatchQuery.indexOf('PUBYEAR', 0)){
        getIrisOctopus().Cancel(varSitId,9); //停止scopus默认检索
        changeQueryStr(varSitId,true);//将检索式更换成分月检索 
        
    }else{
        
        //主日志记录不存在
        if(historyId==negativeOne){ 
            createNewHistory(varRetInt,varSitId,true); //新生成一条主日志记录
        
        //存在主日志记录
        }else{
            
            //先生成一条分月检索日志记录，然后翻页导入数据
            createNewSepHistory(varRetInt,varRetStr,db_code,varSitId,true); 
        }   
    }//end of else
    
}

//ljj 分月检索，其中某个月插件并没有返回数据，或者单个月返回的数据条数超过2K 
function sepHasNoResult(varSitId){
    
    emptySepCont(); //初始化检索常量

    if(negativeOne==historyId){ //没有主日志记录
        
        createNewHistory(0,varSitId,false); //新生成一条主日志记录
        
    }else{  //有主日志记录
        emptySepCont();
        if(negativeOne!=sepHistoryId){ //如果是没有分月检索日志记录，就不用执行此修改操作
            updateSepHistoryState(1); //先修改上一个月的导入状态
        }
        createNewSepHistory(0,null,null,null,false); //保存新的
        sepCurrentMonth++; 
        moveNextMonth(varSitId);    
        
        if(finishSepBatch){
            
            updateSepHistoryState(1);//导入完成，修改本月的导入状态state=1
            
            //修改主日志记录导入状态
            $.ajax({
                url:ctxpath+'/refserver/updateHistoryState',
                type:'post',
                data:{'history.id':historyId,'history.state':1},
                //dataType:'json',
                success:function(){},
                error:function(){}
            });
            
            cancelBatch();
        }
    }
}


//ljj 切换到下一个月 
function moveNextMonth(varSitId){
    
    if(13==sepCurrentMonth){ //最后一条检索式
        getIrisOctopus().Cancel(varSitId,9); //停止上一个月的检索
        changeQueryStr(varSitId,false);//更新检索式
    }else{
        
        if(sepCurrentMonth>12){
            
            sepCurrentYear++; //切换到下一个年
            sepCurrentMonth=1;//重新从第1个月开始
            
            if(sepCurrentYear<=batchArr[0].endYear){ //保证检索的年是用户输入的最后一个年
                getIrisOctopus().Cancel(varSitId,9); //停止上一年的检索 
                changeQueryStr(varSitId,false);//更新检索式
            }else{
                $.smate.scmtips.show("success",batchImportYes,null,1000000);
                finishSepBatch=true;
                return;
            }

        }else{
            getIrisOctopus().Cancel(varSitId,9); //停止上一个月的检索                    
            changeQueryStr(varSitId,false);//更新检索式      
        } 
    }
    
    $.smate.scmtips.show("success","moveNextMonth："+cacheBatchQuery);
    refreshAlert('Scopus','当前正在抓取Scopus库，请稍后....');
    
}


//批量导入
function searchBatchData(){
    
    if(openSepSearch){
        isSeparate=false; 
        finishSepBatch=false;
        orderScopus(); 
    }
    
    cacheBatchQuery="";
    if(batchArr.length<=0){
        return;
    }
    batchDataBaseStrs = "";
    $.each(batchArr,function(n,o) { 
        if(n>0)
        batchDataBaseStrs+=getDbCodeByBitCode(o.dbBitId)+",";
    }); 
    batchDataBaseCount = batchArr.length;
    batchCurrBitId = batchArr[0].dbBitId;
    cacheBatchQuery = batchArr[0].dbQuery;//初始化检索式
    
    //ljj
    var index = cacheBatchQuery.indexOf('AFT', 0);  
    finalCacheBatchQuery = cacheBatchQuery.substring(0, index)+' = ';
    
    
    isBatchSearch = true;
    var code = getDbCodeByBitCode(batchCurrBitId);
    //重置batchPageSize
    batchPageSize =  default_batch_page_count[code];
    
    var txt = "";
    if(batchArr.length<=1){
        txt = batchLoadTxt11.replace("@currDataBase@",ins_url[code]["dbName"]);
    }else{
        txt = batchLoadTxt12.replace("@currDataBase@",ins_url[code]["dbName"]);
        txt = txt.replace("@dataBase@",batchDataBaseStrs);
    }
    Loadding_batch.close("batch");
    Loadding_batch.show_over("batch","rol_main",batchLoadTitle,batchLoadCancel,txt);
    
    $.ajax({
        url:ctxpath+'/refserver/ajaxGetLastCurrPage',
        type:'post',
        data:{'history.dbId':batchArr[0].dbId,'history.startYear':batchArr[0].startYear,'history.endYear':batchArr[0].endYear},
        dataType:'json',  
        //timeout:10000,
        success:function(data){ 
            
            batchCacheLastPage = data.lastPage==0?1:data.lastPage;
            batchCurrPage=batchCacheLastPage;
            batchCacheTotalCount=data.totalCount;
            batchCurrPage = batchCurrPage>1?parseInt(batchCurrPage)+1:batchCurrPage; 
            batchTotalCount = batchCacheTotalCount;
            batchTotalPage=getTotalPages(batchTotalCount,batchPageSize);    
            
            if(openSepSearch){
                //主日志记录表中没有相同条件的检索记录
                if(negativeOne==data.id){
                    historyId = negativeOne;
                    getIrisOctopus().BatchFetch(parseInt(batchCurrBitId),cacheBatchQuery,parseInt(batchCurrPage));//开始检索
                    
                //主日志记录中存在相同条件的检索记录
                }else{
                    
                    historyId = data.id;
                    if(batchCacheTotalCount>twoThousand && scopusDBid==batchArr[0].dbId){  //检查返回的曾经记录的总条数和dbId
                        isSeparate = true;//分月检索
                        changeQueryStr(batchCurrBitId,true); //更换检索式 
                    }else{ 
                        isSeparate = false; //非分月检索
                        sepHistoryId=negativeOne;  
                        getIrisOctopus().BatchFetch(parseInt(batchCurrBitId),cacheBatchQuery,parseInt(batchCurrPage));//开始检索
                    }
                }
            }else{
                getIrisOctopus().BatchFetch(parseInt(batchCurrBitId),cacheBatchQuery,parseInt(batchCurrPage));
            }
            
        },error:function(){ 
            if(openSepSearch){
                // 主日志记录表查询失败
                refreshAlert('Scopus',batchLoadTimetOut);
            }else{
                getIrisOctopus().BatchFetch(parseInt(batchCurrBitId),cacheBatchQuery,parseInt(batchCurrPage));
            }
        }
    });
}


//批量导入取消
function cancelBatch(){
    try {
        if(batchArr.length<=0)
            return;
        getIrisOctopus().Cancel(batchArr[0].dbBitId,9); 
        Loadding_batch.close("batch");
    } catch (e) {
        Loadding_batch.close("batch");
    }
    if(false==finishSepBatch && true==isSeparate){
        updateSepHistoryState(0); //导入状态
        show_msg_tips_batch_imp("warning","导入被中断");
    }
}

//刷新批量检索提示
function refreshAlert(db_code,txt){
    var content = "";
    if(!txt){
        if(batchDataBaseCount<=1){
            content =  batchLoadTxt21;
            content = content.replace("@currDataBase@",db_code);
            content = content.replace("@totalPage@",batchTotalPage);
            content = content.replace("@currPage@",batchCurrPage==0?1:batchCurrPage);
        }else{
            content = batchLoadTxt22;
            content = content.replace("@currDataBase@",db_code);
            content = content.replace("@totalPage@",batchTotalPage);
            content = content.replace("@currPage@",batchCurrPage==0?1:batchCurrPage);
            content = content.replace("@dataBase@",batchDataBaseStrs);
        }
    }else{
        content = txt;
    }
    Loadding_batch.close("batch");
    Loadding_batch.show_over("batch","rol_main",batchLoadTitle,batchLoadCancel,content);
}

var isSaveLog = false;


function refreshBatchArrBySearch(varSitId){
    //alert('refresh');
    emptyBatchConst();
    var newArr=[];
    $.each(batchArr,function(n,o) { 
        if(o.dbBitId!=varSitId){
            newArr.push(o);
        }
    }); 
    batchArr = newArr;
    Loadding_batch.close("batch");
    if(batchArr.length<1){
        $.smate.scmtips.show("success",batchImportYes,null,1000000);
        document.onclick=function(){
             $.scmtips._hide();
         }
        return;
    }
    searchBatchData();
}


function refreshTailArrBySearch(varSitId,varRetInt,varRetStr){
    emptyBatchConst();
    var newArr=[];
    $.each(batchArr,function(n,o) { 
        var code = getDbCodeByBitCode(varSitId);
        if(o.dbBitId!=varSitId){
            newArr.push(o);
        }else{
            if(varRetInt<=0)
            tailThorwsCodes+=code+':'+varRetInt+"<br/>";
        }
    }); 
    batchArr = newArr;
    Loadding_batch.close("batch");
    if(batchArr.length<1){  
        if(tailThorwsCodes.length>0){
            $.ajax({
                url:ctxpath+'/tailMail',
                type:'post',
                data:{'dbRetInts':tailThorwsCodes},
                dataType:'json',  
                timeout: 3000,
                success: function(json){
                }
            });
        }
        $.smate.scmtips.show("success",batchImportYes,null,1000000);
        setTimeout(function(){
            window.open('','_self','');  
            window.close();  
        },3000);
        return;
    }
    tailSearchData();
}

function emptyBatchConst(){
    batchCurrPage = 1;
    batchTotalCount = 0;
    batchTotalPage= 0;
    batchPageCount = 0;
}



function importBatchData(dataXml,db_code,varSitId,currPage,totalPage){
    dataXml=dataXml.substring(dataXml.indexOf("<data "),dataXml.lastIndexOf("</scholarWorks>")) ;
    dataXml=encodeURIComponent("<scholarWorks><error />"+dataXml+"</scholarWorks>");
    //ljj
    getBatchYear(varSitId,false);
    
    $.ajax({
        url:ctxpath+'/publication/import/ajaxBatchSave',        
        type:'post',
        dataType:'json', 
        data:{'inputXml':dataXml, 'publicationArticleType':1,
            'history.dbId':batchDbId,
            'history.dbBitId':varSitId,
            'history.startYear':batchStartYear,
            'history.endYear':batchEndYear,
            'history.totalCount':batchTotalCount,
            'history.currPage':currPage,
            'history.totalPage':totalPage },
        success:function(data){ },
        error:function(){ }
    });
}




function saveBatchNetThrowsLog(varRetInt,varSitId,currPage,totalPage){
    //ljj
    getBatchYear(varSitId,false); 
    $.ajax({
        url:ctxpath+'/publication/import/ajaxBatchSave',
        type:'post',
        data:{'inputXml':varRetInt, 'history.dbId':batchDbId,'history.dbBitId':varSitId,'history.startYear':batchStartYear,'history.endYear':batchEndYear,'history.totalCount':batchTotalCount,'history.currPage':currPage,'history.totalPage':totalPage},
        success: function(){}
    });
    
}

function saveBatchHistoryBy0(varSitId){
    //ljj
    getBatchYear(varSitId,false);
    $.ajax({
        url:ctxpath+'/publication/saveHistory',
        type:'post',
        data:{'history.dbId':batchDbId,'history.startYear':batchStartYear,'history.endYear':batchEndYear,'history.totalCount':0,'history.totalPage':0,'history.currCount':0,'history.currPage':0},
        dataType:'json',  
        timeout: 3000,
        success: function(data){
            historyId=data.id;
        }
    });
}




function dojob(db_code){    
    if(isIEBrowser){
        ocxSearch_ie(db_code);
    }else{
        ocxSearch_ff(db_code);
    }   
}


function getSearchType(){
    if(typeof context_search_type == "undefined"){
        type="search_ref";//检索文献
    }else{
        type = context_search_type;
    }
    return type; 
}



//隐藏所有的左侧数据库
function hiddenDBs(){
    var ul = document.getElementById("db_list_ul");
    var liList = ul.getElementsByTagName("li");
    for(var i=0;i<liList.length;i++){
         $("#"+liList[i].id).css('display','none');
    }
}

function finish(publicationArticleType){
    
    var groupId = $("#groupId").val();
    if(groupId!=null&&groupId!=""){//返回群组的
        var backType  = "&model=pub";
        location.href="/groupweb/grpinfo/main?des3GrpId="+encodeURIComponent(groupId)+backType;
        return;
    }
    if(publicationArticleType==1)
        location.href="/psnweb/homepage/show?module=pub&onlyImports=1&importSource=true";
    if(publicationArticleType==2)
        location.href=ctxpath+"/reference/maint";
    if(publicationArticleType==4)
        location.href=ctxpath+"/project/maint";
}


//获取任务运行状态
function getJobInfo(){
    tmp_JobInfo = "";
    if(isIEBrowser){
        tmp_JobInfo = getIrisOctopus().GetJobInfo();
    }else{
        getIrisOctopus().GetJobInfo();
    }
    return tmp_JobInfo;
}

function setJobInfo(job){
    tmp_JobInfo = job; 
}

//获取插件版本
function getOctopusVersion(){
    octopus_Version = "";
    if(isIEBrowser){
        octopus_Version = getIrisOctopus().get_Version();
    }else{
        getIrisOctopus().get_Version();
    }
    return octopus_Version;
}

function setOctopusVersion(version){
    octopus_Version = version;
}


//根据db_code 获取该数据库当前显示的名字
function getDBName(db_code){
    if(publicationArticleType==4 && (lang=="zh" || lang=="zh_CN")){
        var db_name='';
        var param = "";
        if(param=="en" || param=="en_US"){
            param="enUsName";
        }else{
            param="zhCnName";
        }
        var dbLen=search_DB_Code.length;
        for(var i=0;i<dbLen;i++){
            var code=ins_url[search_DB_Code[i]]["code"];
            if(db_code==code){
                db_name = ins_url[search_DB_Code[i]][param];
            }   
        }
        return db_name; 
    }else{
        return getShowDBname(db_code);
    }
}

//尝试重新查找
function retrySearch(db_code)
{ 
    if(isIEBrowser){
        ajaxInitPsnAliasJsonFRDB(type,tmpOrgName,ocxSearch_ie,db_code);
    }else{
        retrySearch_ff(db_code);
    }
}


//显示/隐藏工作进度条
function setWorkingStatus(flag){
    var progressBar = "&nbsp;&nbsp;<img src='"+respath+"/images/prog_bar.gif' /  style=''>";
    var taskInfo=getTaskInfo();
    if ($("#divMessage").length>0){
        var searchingInfo="";
        if (taskInfo["getxml"]["db_id"] !=""){
            searchingInfo=referencesearch_msg_getting;
            
            var tmpProgInfo=referencesearch_msg_collecting;//"<font color='red'>正在从@DB_NAME@获取数据...</font>";
            var tmpDBName="";
            showProgress("imp_res",tmpProgInfo.replace("@DB_NAME@",""));
            if(multDB){     
                var job_db_id=taskInfo["getxml"]["db_id"].split(",");           
                for (var i=1;i<job_db_id.length;i++){   
                    tmpDBName=tmpDBName + ", " + getDBName(search_id_to_db_code[job_db_id[i]]);                                     
                }
            }else{
                tmpDBName=tmpDBName + ", " + $("#dblist_sel>option:selected").get(0).text; 
            }
            $("#td_proginfo").html(tmpProgInfo.replace("@DB_NAME@",tmpDBName.substring(2)));        
            searchingInfo=referencesearch_msg_getting;


        }else{
            Loadding_div.close('imp_res');
        }
        if (taskInfo["search"]["db_id"]!=""){
            searchingInfo="";
            var job_db_id=taskInfo["search"]["db_id"].split(",");
            for (var i=1;i<job_db_id.length;i++){               
                    searchingInfo=searchingInfo+", " +getDBName(getDbCodeByBitCode(job_db_id[i]));          
            }
            searchingInfo=referencesearch_msg_searching.replace('@dbname@',searchingInfo.substring(1));
        }
        
        if (taskInfo["count"]=="0"){
            $("#divMessage").hide();
            $("#divMessage")[0].innerHTML = "";
        }else{
            $("#divMessage").show();
            if($('.loadding_div_box').is(':visible'))
                $("#divMessage")[0].innerHTML = "";
            else
            $("#divMessage")[0].innerHTML = "<table border=0 ><tr><td align=left valign=middle><B>" + searchingInfo + "</B></td><td align=left style='display: flex; margin: 8px 0px;'>" + progressBar + "&nbsp;</td><td style='color: #1265cf; margin:0 8px;'>&nbsp;</td></tr></table>";   
        }
    }
}

function showProgress(name,content){
    if($('#imp_res_main').is(':visible'))
        return;
    var over = $("#box_main_result");
    var left = $(over).offset().left;
    var top = $(over).offset().top+25;
    var width = $(over).width();
    var height = $(over).height();
    var box_id = name + "_box";
    var main_id = name + "_main";
    var box = $("<div class='loadding_div_box' id='"+box_id+"'></div>");
    var main =  $('<div class="loadding_div_main"  id='+main_id+' align=center STYLE="border-style:solid;border-width:1pt;margin:auto;background:#fff;"><table width="98%"><tr height=15px><td valign="middle" width="8%" align="middle"><img src="'+respath+'/images/loading.gif" alt="loading"  style="width: 15px !important; height: 15px !important;"/></td><td style="margin-bottom:10px" align="left" id="td_proginfo"><font color=red> ' + content + '</font></td></tr></table></div>');
    $(document.body).append(box);
    $(document.body).append(main);

    var size = content.length;
    var width1 = size * 4 + 380;
    main.css('width',width1);
    
    //box.css("width",width);
    box.css("width",document.body.clientWidth);
    box.css("height",$(over).height()+300);
    //box.css("left",left);
    box.css("left",0);
    box.css("top",0);
    
    var main_width = $(main).width();
    var main_height = $(main).height();
    var mx = left + (width - main_width)/2;
    //var my = top + (height - main_height)/2;
    var my = top + 300;
    main.css("left",mx);
    main.css("top",my); 
    box.show();
    main.show();
}

//显示进度条
function showPrepWorking(){ 
    var progressBar = "<img src='"+appContextPath+"/images/prog_bar.gif' />";
    if ($("#divMessage").length>0){
        $("#divMessage").show();
        $("#divMessage")[0].innerHTML = "<table border=0 ><tr><td align=left valign=middle><B>"+referencesearch_msg_prepsearch+"</B></td><td align=left style='height: 20px;'>" + 
                                        progressBar + "&nbsp;</td></tr></table>";
    }
}

function getTaskInfo(){
    //组件会返回如“JobCount@SiteID;CmdID@SiteID;CmdID...."的信息
    tmp_JobInfo = getJobInfo();
    
    var jobInfo= tmp_JobInfo.split("@");
    var jobCount=jobInfo[0];
    var taskInfo={"count":0,"search":{"db_id":""},"getxml":{"db_id":""}};
    
    if (wanFangYX_Status !="0"){
        jobCount=1+parseInt(jobInfo[0]);
        jobInfo.push("-1;"+wanFangYX_Status); 
    }   

    if (jobCount==0)
        return taskInfo;
    else
        taskInfo["count"]=jobCount;
    
    for(var i=1;i<=jobCount;i++){
        var jobInfo_db=jobInfo[i].split(";");
        //判断返回任务类型，如是5则认为是在取xml，否则认为是查找
        if ("5"==jobInfo_db[1]){
            taskInfo["getxml"]["db_id"]=taskInfo["getxml"]["db_id"] + "," +jobInfo_db[0];
        }else if ("14"==jobInfo_db[1])      {
            //如果cmdid=14则是刷新页面，则此任务不作数
            taskInfo["count"]=taskInfo["count"]-1; 
        }else{
            taskInfo["search"]["db_id"]=taskInfo["search"]["db_id"] + "," +jobInfo_db[0];
        }
    }
    return taskInfo;
}

//在查询结果上方显示查询条件
function showSearchCriteria(type){
    var tmpStr= referencesearch_label_criteria_database;
    var tmpDBName="";
    var tmpStr_LT=contextLang =="en"?"<b>(<font color=blue  class=Federal-retrieval_searchtip-detail1>":"<b>（<font color=blue class=Federal-retrieval_searchtip-detail2>";
    var tmpStr_RT=contextLang =="en"?"</font>)</b>":"</font>）</b>";
    //根据语言选择分隔符.
    var splitCode=contextLang =="en"?", ":"，";
    for(var i=0;i<search_DB_Code.length;i++){
        tmpDBName=tmpDBName + splitCode + getDBName(search_DB_Code[i]);
    }
    if (multDB)
        tmpStr=tmpStr +tmpDBName.substring(1)  ;
    else
        tmpStr=tmpStr +$("#dblist_sel>option:selected").get(0).text;
    if(type=="me"){
        if ($("#title").val() !="")         { tmpStr=tmpStr  +referencesearch_label_criteria_title+ $("#title").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#doiSearch").val() !="")        { tmpStr=tmpStr  +referencesearch_label_criteria_doi+ $("#doiSearch").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#cname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_cname + $("#cname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#lname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_lname + $("#lname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#fname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_fname + $("#fname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#oname_else").val() !="")        { tmpStr=tmpStr  +referencesearch_label_criteria_cname + $("#oname_else").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#insName").val() !="")       { tmpStr=tmpStr  +referencesearch_label_criteria_affiliaton+ $("#insName").val().replace(/</g, "&lt;").replace(/>/g, "&gt;") ;}
        if(publicationArticleType=='1'){
            if ($("#publicyear").val() !="")    { tmpStr=tmpStr  +referencesearch_label_criteria_pubyear+ $("#publicyear").val() ;}
        }
        tmpStr=tmpStr+tmpStr_RT;
    }
    if(type=="friend"){
        if ($("#title_friend").val() !="")          { tmpStr=tmpStr  +referencesearch_label_criteria_title + $("#title_friend").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#cname_friend").val() !="")          { tmpStr=tmpStr  +referencesearch_label_criteria_cname + $("#cname_friend").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#lname_friend").val() !="")          { tmpStr=tmpStr  +referencesearch_label_criteria_lname + $("#lname_friend").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#fname_friend").val() !="")          { tmpStr=tmpStr  +referencesearch_label_criteria_fname+ $("#fname_friend").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#insName_friend").val() !="")        { tmpStr=tmpStr  +referencesearch_label_criteria_affiliaton+ $("#insName_friend").val().replace(/</g, "&lt;").replace(/>/g, "&gt;") ;}
        if(publicationArticleType=='1'){
          if ($("#publicyear_friend").val() !="")       { tmpStr=tmpStr  +referencesearch_label_criteria_pubyear+ $("#publicyear_friend").val() ;}
        }
        tmpStr=tmpStr+tmpStr_RT;
    }
    if(type=="else"){
        if ($("#title_else").val() !="")        { tmpStr=tmpStr  +referencesearch_label_criteria_title + $("#title_else").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#oname_else").val() !="")        { tmpStr=tmpStr  +referencesearch_label_criteria_cname + $("#oname_else").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#insName_else").val() !="")      { tmpStr=tmpStr  +referencesearch_label_criteria_affiliaton+ $("#insName_else").val().replace(/</g, "&lt;").replace(/>/g, "&gt;") ;}
        if(publicationArticleType=='1'){
            if ($("#publicyear_else").val() !="")   { tmpStr=tmpStr  +referencesearch_label_criteria_pubyear+ $("#publicyear_else").val() ;}
        }
        tmpStr=tmpStr+tmpStr_RT;
    }

    
    if(!multDB){
        if ($("#cname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_cname + $("#cname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#lname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_lname + $("#lname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#fname").val() !="")     { tmpStr=tmpStr  +referencesearch_label_criteria_fname + $("#fname").val().replace(/</g, "&lt;").replace(/>/g, "&gt;");}
        if ($("#insName").val() !="")       { tmpStr=tmpStr  +referencesearch_label_criteria_affiliaton+ $("#insName").val().replace(/</g, "&lt;").replace(/>/g, "&gt;") ;}
        if ($("#publicyear").val() !="")    { tmpStr=tmpStr  +referencesearch_label_criteria_pubyear+ $("#publicyear").val() ;}
        tmpStr=tmpStr+tmpStr_RT;
        var ref_tmpStr="";
        if (criteria.field_count>0){
            $("#op_table tr").each(function(){  
                var field_value = $.trim($(this).find(".field_value").val());//内容
                var field_name = $(this).find(".field_name>option:selected").get(0).text;//范围
                var operators = $(this).find(".operators>option:selected").get(0).text;//链接符    
                if(field_value.length>0){
                    ref_tmpStr += " " +operators+" "+field_name+"=" +tmpStr_LT +"<font color=blue  class=Federal-retrieval_searchtip-detail1>" +field_value +"</font>"+tmpStr_RT;
                }
          }); 
        }
    tmpStr+=ref_tmpStr;
    }   
    $("#showcriteria").html(tmpStr);
    $(".btnSearch").attr("disabled","disabled");
}
//==============================================查询条件验证=========================================

//判断当前查询的机构所使用的语言是否符合指定查询数据库的语言要求
function checkOrgNames(orgNames){
    var type = getSearchType(); 
    var insName="";
    if(type=="me")
        insName=$("#insName").val();
    if(type=="friend")
        insName=$("#insName_friend").val();
    if(type=="else")
        insName=$("#insName_else").val();
    
    var orgName="";
    var errorDb_Name="";
    if (multDB){
        var dbLen=search_DB_Code.length;
        for(var i=0;i<dbLen;i++){
            var db_code=search_DB_Code[i];
            var lang=ins_url[search_DB_Code[i]]["suportLang"];
            if (orgNames[db_code]){
                orgName=orgNames[db_code];
            }else{
                    orgName=insName;
            }
            if ((lang =="en_US" || lang =="en") && orgName!="" && isChineseStr(orgName)){
                errorDb_Name=errorDb_Name+", "+getDBName(db_code);  
            }
        }
        errorDb_Name=errorDb_Name.substr(2);
    }else{
        var db_code=$("#dblist_sel").val().split('|')[0];
        if (orgNames[db_code])
        {
            orgName=orgNames[db_code];
        }else{
            orgName=insName;
        }
        var lang=$("#dblist_sel").val().split('|')[1];
        var db_name=$("#dblist_sel>option:selected").get(0).text;
         if ((lang =="en" || lang =="en_US") && orgName!="" && isChineseStr(orgName)){
             errorDb_Name=db_name;  
        }
    }
    if (errorDb_Name.length>0){
        if(type=="me")
            jAlert(replaceStr(referencesearch_msg_aff_onlysupport_me,'@database@',errorDb_Name),referencesearch_msg_alert);
        else
            jAlert(replaceStr(referencesearch_msg_aff_onlysupport,'@database@',errorDb_Name),referencesearch_msg_alert);    
        return false;
    }
    return true;
}
//判断当前查询的机构所使用的语言是否符合指定查询数据库的语言要求
function checkLanguage(criteria_len){
    if (multDB==true){
        var dbLen=search_DB_Code.length;
        for(var i=0;i<dbLen;i++){
            var lang=ins_url[search_DB_Code[i]]["suportLang"];
            var db_name=getDBName(search_DB_Code[i]);
            
            if (checkLanguage_DB(lang,db_name,criteria_len)==false)
                return false;
        }
    }else{
        var lang=$("#dblist_sel").val().split('|')[1];
        var db_name=$("#dblist_sel>option:selected").get(0).text;
        return checkLanguage_DB(lang,db_name,criteria_len);
    }
    return true;
}

function checkLanguage_DB(lang,db_name,criteria_len){
    var type = getSearchType(); 
    var cname="";
    var oname="";
    var fname="";
    var lname="";
    var title="";
    if(type=="me"){
        fname = $.trim($("#fname").val());
        lname = $.trim($("#lname").val());
        title = $.trim($("#title").val());
        cname = $.trim($("#oname_else").val());
    }
    if(type=="friend"){
        fname = $.trim($("#fname_friend").val());
        lname = $.trim($("#lname_friend").val());
        title = $.trim($("#title_friend").val());
    }
    if(type=="else"){       
        title = $.trim($("#title_else").val());
        cname = $.trim($("#oname_else").val());
    }
    /*
     * 文献检索约束增加
     * */
    if(type=="search_ref"){
        cname=$.trim($('#cname').val());
        fname=$.trim($('#fname').val());
        lname=$.trim($('#lname').val());
    }
    if (lang =="en_US" || lang =="en"){
        if (criteria_len==0){
            jAlert(replaceStr(referencesearch_msg_term_onlysupport,'@database@',db_name),referencesearch_msg_alert);
            return false;
        }else if ((isChineseStr(oname) || isChineseStr(fname) || isChineseStr(lname)) ||(cname!='' && isChineseStr(cname))){
            jAlert(replaceStr(referencesearch_msg_name_onlysupport,'@database@',db_name),referencesearch_msg_alert);
            return false;   
        }else if ( title !="" && isChineseStr(title) ){
            jAlert(replaceStr(referencesearch_msg_topic_onlysupport,'@database@',db_name),referencesearch_msg_alert);
            return false;   
        }           
        if (criteria.field_count>0){
            var flag = true;
            $("#op_table tr").each(function(){  
                var field_name = $.trim($(this).find(".field_value").val());    
                if(field_name.length>0){
                    if (isChineseStr(field_name)){
                        jAlert(replaceStr(referencesearch_msg_english_onlysupport,'@database@',db_name),referencesearch_msg_alert);
                        flag = false;
                        return false;   
                    }
               }
           });
            return flag;
        }
    }
    return true;
}
//验证查询条件
function validSearchInput(type){
    //文献?
    if (!multDB && $("#dblist_sel").val().length<1){
        if(isIE6()){
            alert(referencesearch_msg_needselectdatabase,referencesearch_msg_alert);
        }else{
            jAlert(referencesearch_msg_needselectdatabase,referencesearch_msg_alert);
        }
        return false;
    }
    if (multDB && search_DB_Code.length==0){
        if(isIE6()){
            alert(referencesearch_msg_needselectdatabase,referencesearch_msg_alert);
        }else{
            jAlert(referencesearch_msg_needselectdatabase,referencesearch_msg_alert);
        }
        return false;
    }   
    var pattern=/^(([1-9][0-9][0-9][0-9])|([1-9][0-9][0-9][0-9]-[1-9][0-9][0-9][0-9]))$/;
    
    if(type=="me"){
        if(publicationArticleType=='1'){
            var pub_year=CtoH($("#publicyear").val());
            if ( pub_year!="" && !pattern.test(pub_year)){
                jAlert(referencesearch_label_pubyeardesc,referencesearch_msg_alert);
                return false;           
            }
        }
        var pname=$.trim($("#cname").val());
        pname = pname==""?$.trim($("#oname_else").val()):pname;
        var criteria_len=$("#title").val().length +$("#insName").val().length +pname.length+ criteria.field_count;
        for(var j=0;j<search_DB_Code.length;j++){
            if(search_DB_Code[j]=="SCI" || search_DB_Code[j]=="SSCI" || search_DB_Code[j]=="ISTP" ||
                search_DB_Code[j]=="Scopus" || search_DB_Code[j]=="ScienceDirect" || search_DB_Code[j]=="WanFang" ||
                search_DB_Code[j]=="PubMed" || search_DB_Code[j]=="IEEEXplore" || search_DB_Code[j]=="Proquest"){
                criteria_len = criteria_len + $("#doiSearch").val().length;
            }
        }
        var db_code_str=search_DB_Code.join(":")+":";
        if (db_code_str.indexOf("CnkiFund:")<0 && db_code_str.indexOf("Nsfc:")<0){
            criteria_len=criteria_len+$("#fname").val().length + $("#lname").val().length;
        }
        
        //获取页面中文本框的值
        var txtTitle = $("#title").val();
        var txtDoi = $("#doiSearch").val();//doi
        var txtCname = $("#cname").val();
        var txtFname = $("#fname").val();
        var txtLname = $("#lname").val();
        var txtInsName = $("#insName").val();
        var txtPublicyear = $("#publicyear").val();
        var name_else =$.trim($("#oname_else").val());
        //是否选择了中文数据库
        var isSelectedChineseDB =isSelectedChineseDatabase();
        //是否选择了英文数据库
        var isSelectedEnglishDB =isSelectedEnglishDatabase(); 
        if(isSelectedChineseDB){//如果选择了中文库
            if($.trim(txtFname) != "" && $.trim(txtTitle)=="" && $.trim(txtDoi)=="" && $.trim(txtInsName)=="" && $.trim(txtLname)==""){
                jAlert(globalValidate.needMoreCondition,referencesearch_msg_alert);  
                return false;
            }
        } 
        if(isSelectedEnglishDB){//如果选择了英文库 
            if(($.trim(txtCname) != "" || $.trim(txtFname) != "") && $.trim(txtTitle)=="" &&
                    $.trim(txtDoi)=="" && $.trim(txtInsName)=="" && $.trim(txtLname)=="" && $.trim(txtPublicyear)==""){
                jAlert(globalValidate.needMoreCondition,referencesearch_msg_alert);  
                return false;
            } 
        }
        if (txtTitle=="" && txtDoi=="" && txtInsName=="" && txtCname=="" && txtFname=="" && txtLname=="" && name_else==""){
            jAlert(referencesearch_msg_needenteritem2,referencesearch_msg_alert);
            return false;
        }
        if($.trim(txtDoi) != "" && search_DB_Code.length>0){//有些库不支持doi检索
            var notDoiSearchDB = "";
            var splitCode=contextLang =="en"?", ":"，";
            for(var k=0;k<search_DB_Code.length;k++){
                if(search_DB_Code[k]!="SCI" && search_DB_Code[k]!="Baidu"&&search_DB_Code[k]!="SSCI" &&
                        search_DB_Code[k]!="ISTP" && search_DB_Code[k]!="Scopus" && search_DB_Code[k]!="ScienceDirect" &&
                        search_DB_Code[k]!="WanFang" && search_DB_Code[k]!="PubMed" && search_DB_Code[k]!="IEEEXplore" &&
                        search_DB_Code[k]!="Proquest"){
                    notDoiSearchDB = notDoiSearchDB + splitCode + getDBName(search_DB_Code[k]);
                }
            }
            if($.trim(notDoiSearchDB)!="" && notDoiSearchDB.indexOf(splitCode)!=-1){
                notDoiSearchDB = notDoiSearchDB.substring(1);
            }
            if($.trim(notDoiSearchDB)!=""){
                jAlert(notDoiSearchDB + referenceSearch_msg_notdoisearch,referencesearch_msg_alert);
                return false;
            }
        }
    }
    if(type=="friend"){ 
        if(publicationArticleType=='1'){
            var pub_year=CtoH($("#publicyear_friend").val());
            if ( pub_year!="" && !pattern.test(pub_year)){
                jAlert(referencesearch_label_pubyeardesc,referencesearch_msg_alert);
                return false;           
            }
        }
        var criteria_len=$("#title_friend").val().length +$("#insName_friend").val().length + criteria.field_count;
        var db_code_str=search_DB_Code.join(":")+":";
        if (db_code_str.indexOf("CnkiFund:")<0 && db_code_str.indexOf("Nsfc:")<0)
        {
            criteria_len=criteria_len+$("#fname_friend").val().length + $("#lname_friend").val().length;
        }
        if (criteria_len+$("#cname_friend").val().length==0)
        {
            if (($("#fname_friend").val().length + $("#lname_friend").val().length)>0)
            {
                jAlert(referencesearch_msg_needenteritem2,referencesearch_msg_alert);
                return false;
            }else{
                jAlert(referencesearch_msg_needenteritem,referencesearch_msg_alert);
                return false;
            }
        } 
        //获取页面中文本框的值
        var txtTitle = $("#title_friend").val();
        var txtCname = $("#cname_friend").val();
        var txtFname = $("#fname_friend").val();
        var txtLname = $("#lname_friend").val();
        var txtInsName = $("#insName_friend").val();
        var txtPublicyear = $("#publicyear_friend").val();
        //是否选择了中文数据库
        var isSelectedChineseDB =isSelectedChineseDatabase();
        //是否选择了英文数据库
        var isSelectedEnglishDB =isSelectedEnglishDatabase(); 
        if(isSelectedChineseDB){//如果选择了中文库
            if($.trim(txtFname) != "" && $.trim(txtTitle)=="" && $.trim(txtInsName)=="" && $.trim(txtLname)==""){
                jAlert(globalValidate.needMoreCondition,referencesearch_msg_alert);  
                return false;
            }
        } 
        if(isSelectedEnglishDB){//如果选择了英文库 
            if(($.trim(txtCname) != "" || $.trim(txtFname) != "") && $.trim(txtTitle)=="" && $.trim(txtInsName)=="" && $.trim(txtLname)=="" && $.trim(txtPublicyear)==""){
                jAlert(globalValidate.needMoreCondition,referencesearch_msg_alert);  
                return false;
            } 
        }
    }
    if(type=="else"){ 
        if(publicationArticleType=='1'){
            var pub_year=CtoH($("#publicyear_else").val());
            if ( pub_year!="" && !pattern.test(pub_year)){
                jAlert(referencesearch_label_pubyeardesc,referencesearch_msg_alert);
                return false;           
            }
        }
        var title_else = $.trim($("#title_else").val());
        var name_else =$.trim($("#oname_else").val());
        var insName_else=$.trim($("#insName_else").val());
        if (title_else=="" && name_else=="" && insName_else==""){
            jAlert(referencesearch_msg_needenteritem2,referencesearch_msg_alert);
            return false;
        }
    }
    if(!multDB){
        var pub_year=CtoH($("#publicyear").val());
        if ( pub_year!="" && !pattern.test(pub_year))
        {
            jAlert(referencesearch_label_pubyeardesc,referencesearch_msg_alert);
            return false;           
        }
        var criteria_len=$("#insName").val().length + criteria.field_count;
        var db_code_str=search_DB_Code.join(":")+":";
        if (db_code_str.indexOf("CnkiFund:")<0 && db_code_str.indexOf("Nsfc:")<0)
        {
            criteria_len=criteria_len+$("#fname").val().length + $("#lname").val().length;
        }
        if (criteria_len+$("#cname").val().length==0)
        {
            if ($("#publicyear").val().length!=0 || ($("#fname").val().length + $("#lname").val().length)>0)
            {
                jAlert(referencesearch_msg_needenteritem2,referencesearch_msg_alert);
                return false;
            }else{
                jAlert(referencesearch_msg_needenteritem,referencesearch_msg_alert);
                return false;
            }
        }
    }
    return checkLanguage(criteria_len);
}

//判断是否选择了中文数据库
function isSelectedChineseDatabase(){
    var chineseDatabase = new Array("ChinaJournal","CNIPR");
    for(var i=0;i<search_DB_Code.length;i++){
        for(var j=0;j<chineseDatabase.length;j++){
            if(search_DB_Code[i].toUpperCase()==chineseDatabase[j].toUpperCase()){
                return true;
            } 
        }
    }
    return false;
}
//判断是否选择了英文数据库
function isSelectedEnglishDatabase(){
    var chineseDatabase = new Array("SCI","SSCI","ISTP","EI","Scopus","ScienceDirect","IEEEXplore","ISIWebOfScience","ITF","PubMed");
    for(var i=0;i<search_DB_Code.length;i++){
        for(var j=0;j<chineseDatabase.length;j++){
            if(search_DB_Code[i].toUpperCase()==chineseDatabase[j].toUpperCase()){
                return true;
            } 
        }
    }
    return false;
}
//================================================================================================================================================================  

//将查找条件两边的空格都去掉
function trimCriteria(type){
    if(type=="me"){
        removeDBUI();
        $("#title").val($.trim($("#title").val()));
        $("#cname").val($.trim($("#cname").val()));
        $("#lname").val($.trim($("#lname").val()));
        $("#fname").val($.trim($("#fname").val()));
        $("#insName").val($.trim($("#insName").val()));
        $("#oname_else").val($.trim($("#oname_else").val()));
        $("#publicyear").val(replaceStr($("#publicyear").val()," ",""));
    }
    if(type=="friend"){
        removeDBUI();
        $("#title_friend").val($.trim($("#title_friend").val()));
        $("#cname_friend").val($.trim($("#cname_friend").val()));
        $("#lname_friend").val($.trim($("#lname_friend").val()));
        $("#fname_friend").val($.trim($("#fname_friend").val()));
        $("#insName_friend").val($.trim($("#insName_friend").val()));
        $("#publicyear_friend").val(replaceStr($("#publicyear_friend").val()," ",""));
    }
    if(type=="else"){
        removeDBUI();
        $("#title_else").val($.trim($("#title_else").val()));
        $("#oname_else").val($.trim($("#oname_else").val()));
        $("#insName_else").val($.trim($("#insName_else").val()));
        $("#publicyear_else").val(replaceStr($("#publicyear_else").val()," ",""));
    }
    if(type=="search_ref"){
        removeDBUI();
        $("#cname").val($.trim($("#cname").val()));
        $("#lname").val($.trim($("#lname").val()));
        $("#fname").val($.trim($("#fname").val()));
        $("#insName").val($.trim($("#insName").val()));
        $("#publicyear").val(replaceStr($("#publicyear").val()," ",""));
    }
}

//获取单位名
function getTmpOrgName(type){
    var tmpOrgName="";
    if(type=="me"){
        tmpOrgName = $("#insName").val();
    }
    if(type=="friend"){
        tmpOrgName = $("#insName_friend").val();
    }
    if(type=="else"){
        tmpOrgName = $("#insName_else").val();
    }
    if(type=="search_ref"){
        tmpOrgName = $("#insName").val();
    }
    return tmpOrgName;
}
//点击导入显示选择的数据列表
function switchDIV(showResult){
    if (showResult && multDB){
        stepSwitchShow(2);
        $("#box_main_pub").css('display','none');
        $("#divMessage").css('display','none');
        $("#box_main_result").show();
    }
    if(!showResult && multDB){
        stepSwitchShow(1);
        $("#box_main_result").css('display','none');
        $("input").removeAttr("disabled");//针对ie9现象
        $(".btnSearch").removeAttr("disabled");
        $("#box_main_pub").show();
    }
    if (showResult && !multDB){
        stepSwitchShow(2);
        $("#box_main_ref").css('display','none');
        $("#divMessage").css('display','none');
        $("#box_main_result").show();
    }
    if(!showResult && !multDB){
        stepSwitchShow(1);
        $("#box_main_result").css('display','none');
        $("input").removeAttr("disabled");//针对ie9现象
        $(".btnSearch").removeAttr("disabled");
        $("#box_main_ref").show();
    }   
}

function stepSwitchShow(index){
    if(index==2){
        $("#step2-1").removeClass("pro03_bj02");
        $("#step2-1").addClass("pro02_bj02");
        $("#step2-2").removeClass("pro03_bj03");
        $("#step2-2").addClass("pro02_bj03");
        
        $("#step1-1").removeClass("pro02_bj01");
        $("#step1-1").addClass("pro03_bj01");
        $("#step1-2").removeClass("pro02_bj02");
        $("#step1-2").addClass("pro03_bj02");
        $("#step1-3").removeClass("pro02_bj03");
        $("#step1-3").addClass("pro02_bj001");
    }
    if(index==1){   
        $("#step1-1").removeClass("pro03_bj01");
        $("#step1-1").addClass("pro02_bj01");
        $("#step1-2").removeClass("pro03_bj02");
        $("#step1-2").addClass("pro02_bj02");
        $("#step1-3").removeClass("pro03_bj03");
        $("#step1-3").addClass("pro02_bj03");
        
        $("#step2-1").removeClass("pro02_bj02");
        $("#step2-1").addClass("pro03_bj02");
        $("#step2-2").removeClass("pro02_bj03");
        $("#step2-2").addClass("pro03_bj03");
    }
    
}

//清除查询条件
function clearInput(){ 
    var type=getSearchType();
    if(type=="me" || multDB==false){
        $("#title").val("");
        $("#doiSearch").val("");
        $("#fname").val("");
        $("#lname").val("");
        $("#cname").val("");
        $("#oname_else").val("");
        $("#insName").val("");
        $("#publicyear").val("");
    }
    if(type=="friend"){
        $("#title_friend").val("");
        $("#fname_friend").val("");
        $("#lname_friend").val("");
        $("#cname_friend").val("");
        $("#publicyear_friend").val("");
        $("#insName_friend").val("");
        $("#insId").val("");
        $("#friendPsnId").val("");
        $.autoword["div_plugin_shoose_friends"].clear();//增加联系人选择框条件清除 date:3-4 by:Lrl
        $("#uiList li").each(function(){ 
            $(this).remove(); 
        });
    }   
    if(type=="else"){
        $("#title_else").val("");
        $("#oname_else").val("");
        $("#publicyear_else").val("");
        $("#insName_else").val("");
        $("#insId").val("");
    }
    if (multDB==false){
        $("#op_table tr").each(function(){  
            $(this).find(".field_value").val('');
        });             
    }
}
//导入列表弹出后将所有选择框置为不选中
function unCheckedAll(){
    var db_code=uncheck_DB_Codes.substring(1).split(",");
    for(var i=0;i< db_code.length;i++)
    {
        try
        {
            var objdoc=getFrameDoc("if_" + db_code[i]);
            selectAll(objdoc,false);
        }catch(e){}
    }   
        uncheck_DB_Codes="";
}

//操作本页所有复选框
function selectAll(objdoc,selected){
    try{
        if(objdoc=='null'){
            $("#db_list_ul li a").each(function(){ 
                if($(this).hasClass("active")){
                    objdoc = getFrameDoc("if_" + $(this).attr("id"));
                    return;
                }
            });
        }
        var chkItems = $(objdoc).find(":checkbox[name='iris_id']");
        if (chkItems.size() <= 0) return false;
        $(objdoc).find("#cbxSelectAll").attr("checked",selected);
        $(chkItems).attr("checked",selected);
    }
    catch(e){}
    return true;
}

function select_pub(){
    var dbObjs=$("#tblPrjSummary").find("input[type='checkbox']");
    var dbLen=dbObjs.length;
    for(var i=0;i<dbLen;i++){
        //有一个未选中
        if(!dbObjs[i].checked){
            $("#cbxSelectAll").attr("checked",false);
            return false;
        }
    }
    $("#cbxSelectAll").attr("checked",true);

}

function downloadOctopus(type,showtype){
    isFirefoxDownErrorAlert=true;
    if (!type)
        type="";
    if ("update"==showtype){
        jAlert(referencesearch_msg_updatecab_alert,referencesearch_msg_alert,function(ev){
            //if(Sys.ie==6.0){
            if(client.browser.name == "IE" && client.browser.version == "6.0"){
                stopDefault(ev);
            }
            location.href = "/pubweb/snspub/downloadCAB?downloadType="+showtype;
            return false;
        });
    }else{
        jAlert(referencesearch_msg_downloadcab_alert,referencesearch_msg_alert,function(ev){
            //if(Sys.ie==6.0){
            if(client.browser.name == "IE" && client.browser.version == "6.0"){
                stopDefault(ev);
            }
            location.href = "/pubweb/snspub/downloadCAB?downloadType="+showtype;
            return false;
        });
    }
    //log
    writeLog(2,"",showtype);
}

//判断当前版本是否为最新,如oldVersion小于newVersion,则提示更新
 function checkLastest(oldVersion,newVersion){
    try{
        var oVers;
        var nVers;
        //if(isIEBrowser || Sys.chrome){
        if(client.browser.name == "IE" || client.browser.name == "Trident" || client.browser.name == "Chrome"){
          oVers=oldVersion.split(".");
          nVers=newVersion.split(".");
        }else{
          oVers=oldVersion.split(",");
          nVers=newVersion.split(",");
        }
        for (var i=0;i < nVers.length;i++){
            if (parseInt(oVers[i]) < parseInt(nVers[i]))
                return false;
        }
        return true;    
    }catch(e){
        return false;
    }
 }

 var pageBoxCount=0;
function writeHtml(db_code,html,type,varRetInt){
    db_count={};
    var iframeID="if_" + db_code;
    var tad_id="tab_" + db_code;
    var tad = $("#"+tad_id);
    if (tad.size() > 0){
        tad.trigger("click");
    }
    if (!type)
        type="0";
    if(html!=''){
        try {
            pageBoxCount = $(html).find(":checkbox[name='iris_id']").size();
        } catch (e) {
            pageBoxCount=0;
        }
    }
    WriteText (getFrameDoc(iframeID),html,iframeID,type,db_code,varRetInt); 
}

function resize(iframeID,varRetInt,timeout){
    varRetInt = pageBoxCount;
    setTimeout("resizeIframe ('" + iframeID +"','"+varRetInt+"')",timeout);
}

function resizeIframe(iframeID,varRetInt) {
    var nPageHeight=0;
    var db_coed=iframeID.substr(3,iframeID.length);
    try{
        nPageHeight=getFrameDoc(iframeID).getElementById(iframeID).scrollHeight;
        if(varRetInt>0 && nPageHeight==0){
            var rowhegith = 1;
            if(db_coed=='SCI' || db_coed=='SSCI' || db_coed=='ISTP' || db_coed=='SCIE'){
                rowhegith = default_iframe_hegiht[db_coed]/25;
            }else if(db_coed=='Scopus' || db_coed=='ChinaJournal'){
                rowhegith = default_iframe_hegiht[db_coed]/20;
            }else if(db_coed=='WanFang'  || db_coed=='WanFangYX'){
                rowhegith = default_iframe_hegiht[db_coed]/10;
            }else{
                rowhegith = default_iframe_hegiht[db_coed]/20;
            }
            nPageHeight = parseInt(nPageHeight)>0?parseInt(nPageHeight)+50:parseInt(rowhegith)*parseInt(varRetInt)+50;
        }   
    }catch(e){
        if(varRetInt>0){
            var rowhegith = 1;
            if(db_coed=='SCI' || db_coed=='SSCI' || db_coed=='ISTP' || db_coed=='SCIE'){
                rowhegith = default_iframe_hegiht[db_coed]/25;
            }else if(db_coed=='SCOPUS' || db_coed=='ChinaJournal'){
                rowhegith = default_iframe_hegiht[db_coed]/20;
            }else if(db_coed=='WanFang' || db_coed=='WanFangYX'){
                rowhegith = default_iframe_hegiht[db_coed]/10;
            }else{
                rowhegith = default_iframe_hegiht[db_coed]/20;
            }
            nPageHeight = parseInt(nPageHeight)>0?parseInt(nPageHeight)+50:parseInt(rowhegith)*parseInt(varRetInt)+50;
        }
    }
    var nMaxSize = 500;

    nPageHeight=parseInt(nPageHeight)<nMaxSize?nMaxSize:parseInt(nPageHeight);
    //ie11
    if(db_coed=='WanFang'){
        nPageHeight+=50;
    }
    if(db_coed=='CNIPR'){
        nPageHeight=default_iframe_hegiht[db_coed];
    }
    $("#"+iframeID).css("height",nPageHeight);

} 

function getFrameDoc(iframeID){
    return document.getElementById(iframeID).contentWindow.document;
}

function WriteText (doc,sText,id,type,db_code,varRetInt) {
    var frameHtml = "";

    if(db_code=="Scopus" ||db_code=="WanFang" || db_code=="SCI" || db_code=="SSCI" || db_code=="ISTP" || db_code=="CNIPR")
    frameHtml += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
    frameHtml += "<html>\n";
    frameHtml += "<head>\n";
    if(db_code=="ScienceDirect")
   /* frameHtml +="#if_ScienceDirect{height:auto!important;}";*/
    if(db_code=="CNIPR")
    frameHtml +="<link href='"+appContextPath+"/css/public.css' rel='stylesheet' type='text/css'/>";
    frameHtml += "<style>\n";
    frameHtml += "body {\n";
    frameHtml += "  background: #FFFFFF;\n";
    frameHtml += "  margin: 0px;\n";
    frameHtml += "  padding: 0px;\n";
    frameHtml += "font-family:verdana,arial,helvetica; font-size:12px;";
    frameHtml += "}\n";
    frameHtml += "ul, li{ margin:0px; padding:0px;}";
    frameHtml += "li{ list-style-type: none; }";
    
    frameHtml += ".find img{ margin-right:10px; cursor:pointer;}";
    frameHtml += "p {\n";
    frameHtml += "margin-bottom:0px;margin-top:0px;}\n";
    frameHtml += ".nologin { \n";
    frameHtml += "padding:5px;FONT-SIZE: 12px; LINE-HEIGHT: 150%; FONT-STYLE: normal;}\n";
    frameHtml +=".point_login { \n";
    frameHtml +="background: url("+respath +"/images/prompt_pic.gif) left center no-repeat;padding-left: 60px;margin-left: 170px;margin-top: 40px; }\n";
    frameHtml +=".point_login p{line-height: 22px;} \n";
    frameHtml +=".cuti {font-weight: bold;} \n";
    frameHtml+=".sousuoneirong th{text-align:left} \n";
    
    frameHtml+=".formwork__content{margin-top: -2px; margin-left: 8px;}";
    frameHtml+=".formwork__sub-content{display: flex; flex-direction: column;}";
    frameHtml+=".formwork__header{width: 100%; height: 36px; background: #f2f2f2; border-bottom: 1px solid #ddd; display: flex; align-items: center;padding-left: 8px;}"; 
    frameHtml+=".formwork__item{display: flex; justify-content:space-between; align-items: center; min-height: 96px; width: 100%;padding-bottom: 12px;border-bottom: 1px solid #ccc; margin-bottom: 5px;}";
    frameHtml+=".formwork__avator{height: 81px; width: 65px;}";
    frameHtml+=".formwork__item-container{display: flex; width: 98%; align-items: center; justify-content: space-between;}";
    /*frameHtml+=".formwork__infor{display: flex; flex-direction: column; margin-left: 28px;}" ;*/
    frameHtml+=".formwork__infor{display: flex; margin-left: 5px; margin-top: -46px;}" ;
    frameHtml+=".formwork__infor-title{ color: #333;  font-size: 16px; font-weight: 600;  cursor: pointer; overflow: hidden; text-overflow:ellipsis;white-space: nowrap;width: 600px;}";
    frameHtml+=".formwork__infor-title:hover{color: #288aed;}";
    frameHtml+=".formwork__infor-author{overflow: hidden; text-overflow:ellipsis; white-space: nowrap; width: 595px;}";
    frameHtml+=".formwork__infor-abstracts { font-size: 14px; line-height: 20px; letter-spacing: 0.2px; color: rgba(0, 0, 0, 0.87); line-height: 20px; max-height: 20px;  transition: height 100ms linear;  overflow: hidden; margin-left: -2px !important; margin-top: 8px; margin-bottom: 8px;}";
    frameHtml+=".formwork__infor-abstracts:before {content: ''; float: left; width: 2px;  height: 100%;}";
    frameHtml+=".formwork__infor-abstracts:after { content: '...';  color: #1265cf; box-sizing: content-box; float: right; position: relative; top: -20px; height: 20px; left: 100%; width: 80px;  margin-left: -80px;  padding-right: 2px; text-align: right; background: linear-gradient(to right, rgba(255, 255, 255, 0), white 50%, white); font-size: 32px; line-height: 2px; cursor: pointer;}";

    
    frameHtml+=".uiButton {cursor:pointer; display:inline-block; padding:2px 8px; color:#333; font-weight:bold; line-height:16px;";
    frameHtml+="text-align:center; text-decoration:none; vertical-align:top; white-space:nowrap; background:url(../images_v5/sm_icon.png) -352px -122px no-repeat; background-color:#eee;"; 
    frameHtml+="border:1px solid #999; border-bottom-color:#888; box-shadow:0 1px 0 rgba(0, 0, 0, 0.1); overflow: visible;}/*box-shadow:设置块阴影 */";
    frameHtml+=".f_normal{font-weight:normal;}/*文字恢复正常*/";
    frameHtml+="a.uiButton, a.uiButton:hover {color:#333333; text-decoration:none; }/* padding:4px 8px;针对使用A标签做按钮时的定义*/";
    frameHtml+=".clear{ clear:both;}";
    frameHtml+=".selection_box{ border-bottom:1px solid #e0e0e0; margin-top:5px; padding-bottom:15px; width:805px; margin-bottom:15px;}\n";
    frameHtml+=".selection_top{ margin-bottom:10px;}\n";
    frameHtml+=".selection_left{ float:left; font-size:12px; color:#333;}\n";
    frameHtml+=".selection_right{ float:right; position:relative;}\n";
    frameHtml+=".selection_right .exclude{ background:url("+respath+"/images/exclude_bg.gif) no-repeat; width:160px; height:23px; line-height:23px; padding-left:25px; position:absolute; top:0px; right:50px; color:#F00;}\n";
    frameHtml+=".selection_right .exclude a{ color:#F00; text-decoration:none;}\n";
    frameHtml+=".selection_right .exclude a:hover{ color:#F00; text-decoration:underline;}\n";
    frameHtml+=".exclude_box{ border:1px solid #dddddd; padding:0px 12px; height:90px; overflow-y:auto; overflow-x:hidden;}\n";
    frameHtml+=".exclude_box table tr td{ padding:5px 5px 5px 0px; font-size:12px;}\n";
    frameHtml+=".exclude_box table input{ vertical-align:middle; margin-right:3px; float:left;}\n";
    frameHtml+=".exclude_box span{ width:120px; word-wrap:break-word; overflow:hidden; float:left; line-height:14px; vertical-align:middle;}\n";
    
    frameHtml += "</style>\n";
    frameHtml += "<script>function checkUncheckByClass(a,b){};</script>\n";
    frameHtml += "</head>\n";
    frameHtml += "<body onload=\"parent.resize('" +id+ "','"+varRetInt+"','200');\">\n";
    frameHtml += "<input type=\"hidden\" id=\"db_code\" value=\"" + db_code +"\"/>";
    if(ctxpath.indexOf("scmwebsns")>0){
        //如果是ISI文献库这需要截出里面的人名字段
        if(globalType=="me"&&$.inArray(db_code,allowDbcode)!=-1){
            var psnData = sText.match(/<myFlag>([\s\S]*)<\/myFlag>/);
            psnAliasJsonStr = (psnData==null)?null:psnData[1];
            var hasFlag = (psnData==null&&sText.indexOf(referencesearch_msg_norecord)==-1)?false:true;
            sText=sText.replace(/<myFlag>([\s\S]*)<\/myFlag>/,"");              
            if(booleanPsnAlias){
                frameHtml +=writeCheckBox(db_code,hasFlag);
            }
        //}else if(!multDB||!Sys.ie){//如果是其他检索则去掉返回的别名值sys.ie是先去掉除ie以外浏览器的返回值
        }else if(!multDB|| client.browser.name != "IE"){
            sText=sText.replace(/<myFlag>([\s\S]*)<\/myFlag>/,"");
        }
    }else{
        sText=sText.replace(/<myFlag>([\s\S]*)<\/myFlag>/,"");
    }
    //如果是写入html，则加全选框   
    if (type && type=="1" && db_code != "CNIPR")//中国知识产权网的不要加全选功能
    {
        //scm-6689
        if(db_code == "BaiTen"){
            frameHtml +="<div style='padding-left:15px;border-top:1px solid #dddddd;padding-top:10px; margin-top:15px;'>";
        }else if(db_code == "PubMed"){
            frameHtml +="<div style='padding-left:8px;border-top:1px solid #dddddd;padding-top:10px; margin-top:15px;'>&nbsp;&nbsp;";
        }else{
            frameHtml +="<div style='padding-left:8px;border-top:1px solid #dddddd;padding-top:10px; margin-top:15px;'>";
        }
        frameHtml +="<input id=\"cbxSelectAll\" onclick=\"parent.selectAll(document,this.checked);\" type=\"checkbox\" />";
        frameHtml +="<span onclick=\"document.getElementById('cbxSelectAll').click();\" id=\"spanselectall\" >&nbsp;";
        frameHtml +=referencesearch_label_selectall;
        frameHtml +="</span></div>";
    }
    //百度专利样式
    sText = sText.replace("总记录数:","总记录数：");
    //scopus-ie8样式
    sText = sText.replace("http://www.scopus.com/1473091490/bundles/AllCSS.css",appContextPath+"/searchDb/scopus_AllCSS.css");
    sText = sText.replace("http://cdn.els-cdn.com/sd/css/css_gen_v01_141cR2.css",appContextPath+"/searchDb/scienceDirect.css");
    //scm-6689
    sText = sText.replace("http://so.baiten.cn/Content/Styles/Default/common.css",appContextPath+"/searchDb/baiten_common.css");
    //projectc-259
    sText = sText.replace("http://static.pubmed.gov/portal/portal3rc.fcgi/3715142/css/3357469/3475386/3603503/3398623/3395415/3318467/14534/3393491/3593022/3398246/3427736/12930/3528951/3317819/3486415/23471/36093/3317859/3577051/3579763/2751886/3579008/9685/3394274/21450/3327433/648426/24996/36029/35914/3357701/3395586.css",appContextPath+"/searchDb/pubMed.css");
    frameHtml += covertText(sText);
    frameHtml += "</body>\n";
    frameHtml += "</html>";
    doc.open();
    doc.write(frameHtml);
    doc.close();
    doc.body.id = id;
    frameshow(db_code);
    dbshow(db_code,varRetInt);
}

//显示检索结果
function frameshow(db_code){
    if(initHtmlResutlCount==0){
        var div = document.getElementById("list_lable_info");
        var iframelist = div.getElementsByTagName("iframe");
        for(var i=0;i<iframelist.length;i++){
            if(iframelist[i].id.substr(3,iframelist[i].id.length)==db_code){
                $("#"+iframelist[i].id).css("display","");
            }
            else{
                $("#"+iframelist[i].id).css("display","none");      
            }
        }
    }
    initHtmlResutlCount++;
}


//左侧显示检索结果数据库
function dbshow(db_code,varRetInt){
    var db_name = getShowDBname(db_code);
    if(!checkisDB(db_code)){
        if(initDBResutlCount==0){
            $('#db_list_ul').append("<li id='"+db_code+"'><a id='"+db_code+"' class='active' onclick='dbtocontent(this)' title='"+db_name+"("+varRetInt+")"+"'><div style='cursor: pointer;overflow: hidden; text-overflow:ellipsis; white-space: nowrap;width: 144px;' ><span>"+db_name+"</span>"+"(<label id='"+db_code+"_count'>"+varRetInt+"</label>)</div></a></li>");
        }else{
            $('#db_list_ul').append("<li id='"+db_code+"'><a id='"+db_code+"' onclick='dbtocontent(this)' title='"+db_name+"("+varRetInt+")"+"'><div style='cursor: pointer;overflow: hidden; text-overflow:ellipsis; white-space: nowrap;width: 144px;' ><span>"+db_name+"</span>"+"(<label id='"+db_code+"_count'>"+varRetInt+"</label>)</div></a></li>");
        }
    }
    initDBResutlCount++;
}

function checkedDBListShow(db_code,varRetInt){
    $('#db_list_ul li').each(function(){
        if(leftDBshowFlag) return;
        if($(this).attr('id')==db_code && varRetInt>0){
            leftDBshowFlag=true;
            dbtocontent(this);
        }
    }); 
}

function checkisDB(db_code){
    var flag = false;
    $("#db_list_ul").find("li").each(function(){ 
        if($(this).attr('id')==db_code){
            flag=true;
            return;
        } 
    });
    return flag;
}

function removeDBUI(){
    initDBResutlCount=0;
    initHtmlResutlCount=0;
    $("#db_list_ul").find("li").each(function(){ 
        $(this).remove(); 
    });
}

function leftDBshow(db_code){
    $("#db_list_ul li a").each(function(){ 
        if($(this).attr('id')==db_code){
            $(this).attr("class","active");
        }else{
            $(this).attr("class","");
        }
    });
}

function getShowDBname(db_code){
    var db_name='';
    var dbLen=search_DB_Code.length;
    for(var i=0;i<dbLen;i++){
        var code=ins_url[search_DB_Code[i]]["code"];
        if(db_code==code){
            db_name = ins_url[search_DB_Code[i]]["dbName"];
        }   
    }
    return db_name;
}


//登录后重新查找后设置左侧菜单db的查找结果数
function setDbSerarchResCount(db_code,varRetInt){
    $("#db_list_ul").find("li").each(function(){ 
        if($(this).attr('id')==db_code){
             $("#"+db_code+"_count").html(varRetInt);
        }
    });
}

//点击检索结果数据库，显示相应数据
function dbtocontent(thiss){
    leftDBshow(thiss.id);
    var div = document.getElementById("list_lable_info");
    var iframelist = div.getElementsByTagName("iframe");
    for(var i=0;i<iframelist.length;i++){
        if(iframelist[i].id.substr(3,iframelist[i].id.length)==thiss.id)
        {   $("#"+iframelist[i].id).css("display","");
            resizeIframe(iframelist[i].id,10);
        }
        else
        $("#"+iframelist[i].id).css("display","none");  
    }
}

function impGetDataXmlAll(obj,impType){
    BaseUtils.doHitMore(obj,2000);
    if(isIEBrowser){ 
        getDataXML_All_ie(impType);
    }else{ 
        getDataXML_All_ff(impType);
    }
}

function covertText(sText){
    sText = sText.replace(/&lt;/gi,"<");
    sText = sText.replace(/&gt;/gi,">");
    sText = sText.replace(/&amp;/gi,"&");
    sText = sText.replace(/&quot;/gi,"\"");
    sText = sText.replace(/&apos;/gi,"\'");
    sText = sText.replace(/&#034;/gi,"\"");
    sText = sText.replace(/&#038;/gi,"&");
    sText = sText.replace(/&#039;/gi,"\'");
    sText = sText.replace(/&#060;/gi,"<");
    sText = sText.replace(/&#062;/gi,">");
    sText = sText.replace(/\/scholar\/styles\/searchdb/gi,appContextPath +"/searchDb");
    sText = sText.replace (/(<p>\s{1,})/gi,"<p>&nbsp;") + "\n";
    return sText;
}

function select_ShDB_Code(status){
    if(!status){
        $("#box_main_ref").find("#dbCB_All").attr("checked",false);
    }else{
        var dbObjs=$(".data_tr").find("input[type='checkbox']");
        var dbLen=dbObjs.length;
        for(var i=0;i<dbLen;i++){
            //有一个未选中
            if(!dbObjs[i].checked){
                $("#box_main_ref").find("#dbCB_All").attr("checked",false);
                return false;
            }
        }
        $("#box_main_ref").find("#dbCB_All").attr("checked",true);
    }
}

//获取当前选择的数据库，如果选择的是所有，则获取当前所有可用的数据库
function getSearch_DB_Code(){
    var result=new Array;
    if (multDB){
        var dbObjs=$(".data_tr").find(":checkbox:checked");
        var dbLen=dbObjs.length;
        for(var i=0;i<dbLen;i++){
            result.push(dbObjs[i].value);
        }
    }else{
        result.push($("#dblist_sel").val().split("|")[0]);
    }
    return result;
}

//导入列表弹出后将所有选择框置为不选中
function unCheckedAll(db_code){
    for(var i=0;i<db_code.length;i++){
        try{
            var objdoc=getFrameDoc("if_" + db_code[i]);
            selectAll(objdoc,false);
        }catch(e){}
    }   
}

//==========================================函数=================================================
function replaceStr( str1, str2, str3 ){
  var startIndex=0;
  var strDest; 
  try{
      if (!str1)
        return "";
      if (str2.length==0)
        return str1;
      while (str1.indexOf(str2, startIndex)>=0){
            strDest = str1.substring(startIndex, str1.length).replace(str2, str3);
            if (startIndex>0)
            {
                strDest = str1.substring(0, startIndex) + strDest;
            }
            startIndex = str1.indexOf(str2, startIndex) + str3.length;
            str1 = strDest;
        }
    }
    catch(e){}
    
  return str1;
}
//判断是否是不是中文
function isChineseStr(s){
    var regu = "[\w\W]*[\u4e00-\u9fa5][\w\W]*";   
    var re = new RegExp(regu);
    if (s=="")
        return false;
    if (re.test(s)) {
        return true;
    }else{
        return false;
    }
}

//判断是否是数字
 function isDigit(s){ 
    var patrn=/^[0-9]{1,10}$/; 
    if (!patrn.exec(s)) return false ;
    return true ;
} 
//将全角转成半角
function CtoH(input){ 
    if(undefined==input)
        return;
    var result="";
    for (var i = 0; i < input.length; i++){
        if (input.charCodeAt(i)==12288){
            result+= String.fromCharCode(input.charCodeAt(i)-12256);
            continue;
        }   
        if (input.charCodeAt(i)>65280 && input.charCodeAt(i)<65375)
            result+= String.fromCharCode(input.charCodeAt(i)-65248);
        else 
            result+= String.fromCharCode(input.charCodeAt(i));
    } 
    return result;
} 

function getDbCodeByBitCode(bitCode){
    for (var obj in ins_url){
        if (null !=ins_url[obj]["dbBitCode"] && ins_url[obj]["dbBitCode"]==bitCode)
        {
            return obj;
        } 
    }   
}

//检索文献
function getMultCondition(criteria){
    criteria.field_count=0;
    criteria.operators_count=0;
    //清空数组
    if(criteria.field_value.length>0)
        criteria.field_value=[];
    if(criteria.field_name.length>0)
        criteria.field_name=[];
    if(criteria.operators.length>0)
        criteria.operators=[];
    //$.each(criteria.field_value,function(n,value) {    
           //jAlert(n+' '+value); 
    //}); 

    $("#op_table tr").each(function(){  
            var field_value = $.trim($(this).find(".field_value").val());
            var field_name = $.trim($(this).find(".field_name").val());
            var operators = $.trim($(this).find(".operators").val());   
            if(field_value.length>0){
                criteria.field_value.push(field_value);
                criteria.field_name.push(field_name);
                criteria.operators.push(operators);     
                criteria.operators_count++;
                criteria.field_count++;
            }
    });     
}

/***
 * 插件日志
 */

function writeLog(logType,retInt,logMsg){
    return ;//SCM-8669
    try {
        logMsg = logMsg.replace(/<\/?.+?>/g,"");//remove html tag
        $.ajax({
            url:'/pubweb/publication/search/writeLog',
            type:'post',
            data:{"logSearch.type":logType,"logSearch.retInt":retInt,"logSearch.logMsg":logMsg},  
            timeout: 10000,
            success: function(data){            
            }
        });
    } catch (e) {}
}

//为写入日志生成查询条件字符串
function getLogSerachCriteria(){
    try{
        logCriteriaStr="查找条件：";
        logCriteriaStr+=" [affiliation]:" +affiliaton_insName;
        logCriteriaStr+=" [title]:" + criteria.topic ;
        logCriteriaStr+=" [name]:" + criteria.cname ;
        logCriteriaStr+=" [firstName]:" + criteria.firstName ;
        logCriteriaStr+=" [lastName]:" + criteria.lastName ;
        logCriteriaStr+=" [otherName]:" + criteria.otherName;
        logCriteriaStr+=" [pubYear]:" + criteria.pubYear;
        if (criteria.field_count>0)
        {
            logCriteriaStr+=" [field_name]:" + criteria.field_name.join(";");
            logCriteriaStr+=" [field_value]:" + criteria.field_value.join(";");
            logCriteriaStr+=" [operators]:" + criteria.operators.join(";");
        }
    }
    catch(e)
    {
        return "";
    }
}

//插件返回写入日志
    function onUpdateLog(db_code,varCmdId,varRetInt,varRetStr){
        var logInsSearch = logCriteriaStr+"\n单位检索式:"+(orgNames[db_code]==undefined?"":orgNames[db_code]);
        if ( varCmdId!=14){
            if(varCmdId==5){    
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId +";varRetStrLength="+ varRetStr.length;
            writeLog(5,varRetInt,logContent);   
        }else if (varRetInt=="-1")  {//如果返回-1，则提示需要登录
            if (!ins_url[db_code] || ins_url[db_code]["loginUrl"]==""){
                varRetStr=referencesearch_msg_nopermission;
            }else{
                varRetStr=replaceStr(referencesearch_msg_needlogin,"@loginurl@",ins_url[db_code]["loginUrl"]);      
            }
            varRetStr=replaceStr(varRetStr,"@database@",getDBName(db_code));        
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ varRetStr;
            writeLog(4,varRetInt,logContent);
        }else if (varRetInt=="-2"){ //如果返回-2 则写入出错原因
            //scm-6608 varRetStr=replaceStr('<br/>'+referencesearch_msg_search_error,'@database@',getDBName(db_code)) + varRetStr;
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ varRetStr;
            writeLog(4,varRetInt,logContent);
        }else if (varRetInt=="-6" || varRetInt=="-7" || varRetInt=="-8"){
            if('Scopus'==db_code && varRetInt=='-6'){
                varRetStr=replaceStr('<br/>'+referencesearch_msg_oper_err6,'@database@',getDBName(db_code)) + varRetStr;
                var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ varRetStr;
                writeLog(4,varRetInt,logContent);
            }else{
                varRetStr=replaceStr('<br/>'+referencesearch_msg_oper_err,'@error@',varRetInt) + varRetStr;
                var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ varRetStr;
                writeLog(4,varRetInt,logContent);
            }
        }else if (varRetInt=="-3"){
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ referencesearch_msg_nopermission;
            writeLog(4,varRetInt,logContent);
        }else if (varRetInt=="-4"){
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ referencesearch_msg_noready;
            writeLog(4,varRetInt,logContent);
        }else if (varRetInt=="-5"){
            var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId + ";\nvarRetStr="+ referencesearch_msg_timeout;
            writeLog(4,varRetInt,logContent);
        }else{
            if (varCmdId==11){
                if(varRetInt=="1"){
                    var logLoginUrl = "\n校内登录:actionUrlInside="+ins_url[db_code]["actionUrlInside"]+";\nfulltextUrlInside="+ins_url[db_code]["fulltextUrlInside"];  
                    var logContent=logInsSearch+logLoginUrl+"\ndbCode=" + db_code + ";cmdID=" + varCmdId+";\nvarRetStr="+varRetStr;
                    writeLog(4,varRetInt,logContent);
                }else if (varRetInt=="2"){
                    var logLoginUrl = "\n校外登录:actionUrl="+ins_url[db_code]["actionUrl"]+";\nfulltextUrl="+ins_url[db_code]["fulltextUrl"];  
                    var logContent=logInsSearch+logLoginUrl+"\ndbCode=" + db_code + ";cmdID=" + varCmdId+";\nvarRetStr="+varRetStr;
                    writeLog(4,varRetInt,logContent);
                }
            }else{ 
                varRetStr=replaceStr(varRetStr,'setInterval("refreshDB(','setInterval("parent.refreshDB(');
                var logContent=logInsSearch+"\ndbCode=" + db_code + ";cmdID=" + varCmdId+"\nvarRetStr="+varRetStr;
                writeLog(4,varRetInt,logContent);
            }
        }
    }
}

//别名弹出框回调函数
function thickbox_close_callBack(body){
    var code = $(body).find("ul[class='label-list']").attr("alt");
    var count = $(body).find("ul[class='label-list']").attr("count");
    if(code!="undefined" && count>0){
        var document = getFrameDoc("if_"+code);
        setTimeout(function(){
            reSearchAction(code,true);
        },1000);    
    }
}

function isiAliasAll(db_code,flag){
    var frameId ="if_"+db_code;
    var document = getFrameDoc(frameId);
    if(flag){
        $(document).find(".exclude_box :checkbox").each(function(   ){
                if(this.disabled==false){
                    this.checked=true;
                }
        });
    }else{
        $(document).find(".exclude_box :checkbox").attr("checked",false);
    }   
}
    
//写checkbox html
function writeCheckBox(db_code,hasFlag){
    var html ="";
    if(!hasFlag)
    return "";  
     psnNumber = switchPsnAliasData(db_code).length;
    if(psnAliasJsonStr!=""&&psnAliasJsonStr!=undefined){
        psnAliasJson =eval('('+psnAliasJsonStr+')');
        if (typeof (psnAliasJson) == "string")
            psnAliasJson = eval('(' + psnAliasJson + ')');
    }else{
        psnAliasJson=[];
    }   
    if((psnAliasJson==null || psnAliasJson.length==0) && psnNumber==0){
        return "";
    }
    html += '<div class="selection_top">';
    if("zh_CN"==locale){
        html += '<div class="selection_left">从以下选项精确搜索范围：&nbsp;&nbsp;<input type="checkbox" onclick="parent.isiAliasAll(\''+db_code+'\',this.checked);" name="isinameall" class="excludeSelectAll_'+db_code+'">全选</div>';
        html += '<div class="selection_right"><div class="exclude"><a style="cursor:pointer;" onclick=\"parent.showPsnAliasBox(\''+db_code+'\')\">您已经排除了<span id="count_'+db_code+'">'+psnNumber+'</span>个人名！</a></div><a id="btnExclude" class="uiButton f_normal" title="排除" onclick="parent.reSearchAction(\''+db_code+'\')">排除</a></div>';
    }else{
        html += '<div class="selection_left">Narrow the range:&nbsp;&nbsp;<input type="checkbox" onclick="parent.isiAliasAll(\''+db_code+'\',this.checked);" name="isinameall" class="excludeSelectAll_'+db_code+'">Select all</div>';
        html += '<div class="selection_right"><div class="exclude" style="width:240px;background:url('+respath+'/images/exclude_bg_en_US.gif) no-repeat;"><a style="cursor:pointer;" onclick=\"parent.showPsnAliasBox(\''+db_code+'\')\"><span id="count_'+db_code+'">'+psnNumber+'</span> peoples have been exclusive.</a></div><a id="btnExclude" class="uiButton f_normal" title="Exclude" onclick="parent.reSearchAction(\''+db_code+'\')">Exclude</a></div>';
    }
    html += '<div class="clear"></div>';
    html += "</div>";
    if(psnAliasJson!=null && psnAliasJson.length>0){
        html += '<div class="exclude_box">';
      html = rebuidIsIAliasTable(html,psnAliasJson,db_code);
      html +="</div>";  
    }else{
         html +="<hr/>";    
    }
   return html;
}

function rebuidIsIAliasTable(html,psnAliasJson,db_code){
    if(psnAliasJson.length<1)
        return html;
        html += '<table width="100%" border="0" cellspacing="0" cellpadding="0">';
        if(intoIsNotSearchAlias.length==2){
            for ( var i = 0; i < psnAliasJson.length; i++) {
                var name = psnAliasJson[i].name.replace(",","").replace(" ","").replace("&apos;","'").toLowerCase();
                var aliasName1 = intoIsNotSearchAlias[0].replace(" ","").toLowerCase();
                var aliasName2 = intoIsNotSearchAlias[1].replace(" ","").toLowerCase();
                if(name==aliasName1 || name==aliasName2){
                    psnAliasJson[i].name+="#";
                }
            }
        }
      var jj = parseInt(psnAliasJson.length/5);
        var k=0;
        if(jj==0){
              html+='<tr>';
              for(var i=0;i<psnAliasJson.length;i++){   
                    var name=psnAliasJson[i].name;
                    var disabled = name.indexOf("#")!=-1;
                    if(disabled){
                         name = $.trim(name.replace("#",""));
                         html+='<td align="left" width="20%" valign="top"><input name="checkbox" disabled="disabled" type="checkbox"><span>'+name+'('+psnAliasJson[i].count+')'+'</span></td>';
                     }else{
                         html+='<td align="left" width="20%" valign="top"><input name="checkbox" type="checkbox" onclick="parent.cancelExcludeSelectAll(\''+db_code+'\')" value="'+name+'"><span>'+name+'('+psnAliasJson[i].count+')'+'</span></td>';
                     }
               }
              for(var i=0;i<5-psnAliasJson.length;i++){         
                    html+='<td align="left" width="20%" valign="top">&nbsp;</td>';
               } 
              html+='</tr>';
        }else{
              for(var j=1;j<=jj;j++){   
                  html+='<tr>';
                  for(var i=0;i<5;i++){
                      var name = psnAliasJson[k].name;
                      var disabled = name.indexOf("#")!=-1;
                       if(disabled){
                           name = $.trim(name.replace("#",""));
                           html+='<td align="left" width="20%" valign="top"><input name="checkbox" disabled="disabled" type="checkbox"><span>'+name+'('+psnAliasJson[k].count+')'+'</span></td>';
                       }else{
                           html+='<td align="left" width="20%" valign="top"><input name="checkbox" type="checkbox" onclick="parent.cancelExcludeSelectAll(\''+db_code+'\')" value="'+name+'"><span>'+name+'('+psnAliasJson[k].count+')'+'</span></td>';
                       }
                        k++;
                  }
                  html+='</tr>';
              }
       }    
      if(jj>0 && psnAliasJson.length%5>0){
          html+='<tr>';
          for(var i=k;i<psnAliasJson.length;i++){
             var name = psnAliasJson[i].name;
             var disabled = name.indexOf("#")!=-1;
             if(disabled){
                 name = $.trim(name.replace("#",""));
                 html+='<td align="left" width="20%" valign="top"><input name="checkbox" disabled="disabled" type="checkbox"><span>'+name+'('+psnAliasJson[i].count+')'+'</span></td>';
             }else{
                 html+='<td align="left" width="20%" valign="top"><input name="checkbox" type="checkbox" onclick="parent.cancelExcludeSelectAll(\''+db_code+'\')" value="'+name+'"><span>'+name+'('+psnAliasJson[i].count+')'+'</span></td>';
             }
           }
          for(var i=0;i<5-(psnAliasJson.length%5);i++){         
                html+='<td align="left" width="20%" valign="top">&nbsp;</td>';
           }
          html+='</tr>';
      }
      html +="</table>";
      return html;
}

function cancelExcludeSelectAll(db_code){
    var document=getFrameDoc("if_"+db_code);
    $(document).find(".excludeSelectAll_"+db_code).each(function(   ){
        if(this.checked==true){
            this.checked=false;
        }
    });

}

//排除别名
function reSearchAction(db_code,isCheckSelect){
    var frameId ="if_"+db_code;
    var document = getFrameDoc(frameId);
    var buttonId = "#btnExclude";
    var currentData = switchPsnAliasData(db_code);
    var jsonData = [];
    var currnetNum = currentData.length;
    var selectNum = $(document).find(".exclude_box :checkbox:checked").length;
    if(maxPsnAlias<currnetNum+selectNum){
        if("zh_CN"==locale){
            $.smate.scmtips.show("warn","排除人名数量不得超过"+maxPsnAlias+"个");
        }else{
            $.smate.scmtips.show("warn","The exclusive is up to "+maxPsnAlias);
        }       
        return;
    }
    if(selectNum==0 && !isCheckSelect){
        if("zh_CN"==locale){
            $.smate.scmtips.show("warn","请选择需要排除的人名");
        }else{
            $.smate.scmtips.show("warn","Please specify one to exclude at least");
        }
        return;
    }
    isButtonExclude = true;
    $(document).find(buttonId).attr({ "disabled": "disabled" });
    if(!isCheckSelect){
        $(document).find(".exclude_box :checkbox").each(function(i){
            if(this.checked==true&&this.value.replace(/(\w*)(,\s+)(\w*)(-?)(\w*)/i,"$1 $3 $5").replace(/(^\s*)|(\s*$)/g, "").toLowerCase()!=psnName.toLowerCase()){
                jsonData.push({'ID':null,'NAME':this.value,"ACTION":"0"});
                }
        });
    }
    $.each(removePsnAlias,function(i){
        jsonData.push(this);
    });
   var data={'dbcode':db_code,'psnName':psnName,'psnAliasNames':JSON.stringify(jsonData)};
    $.ajax({
        url:'/pubweb/publication/ajaxupdatepsnalias',
        type:'post',
        data:data,
        dataType:'json',
        success:function(json){ 
            if(json.ajaxSessionTimeOut=='yes'){
                top.location.reload(true);
            }
            $.each(json.data,function(i){
                var delId = this.ID;
                if(this.ACTION=="0"){
                    currentData.push({"ID":this.ID,"NAME":this.NAME});
                }else{
                    for ( var i = 0; i < removePsnAlias.length; i++) {
                        if(delId==removePsnAlias[i].ID){
                            removePsnAlias.myRemove(i);
                        }
                    }
                }
            });
            if(isCheckSelect){
                criteria.firstName = bulidRetrieval(removePsnAlias);
            }else{
                criteria.firstName = bulidRetrieval(currentData);
            }
            for ( var i = 0; i < search_DB_Code.length; i++) {
                if("SCI"==search_DB_Code[i] || "SSCI"==search_DB_Code[i] || "ISTP"==search_DB_Code[i]){
                    retrySearch(search_DB_Code[i]);
                    if(search_DB_Code[i]!=db_code)
                    $(getFrameDoc("if_"+search_DB_Code[i])).find(buttonId).attr({ "disabled": "disabled" });
                }
            }
        },
       error:function(){}});
}


//thickbox显示别名
function showPsnAliasBox(db_code){
    var boxDivId = "#boxDiv_"+db_code;
    var linkId = "#link_"+db_code;
    var html ="";
    var currentData = switchPsnAliasData(db_code);
    for(var i= 0 ;i<currentData.length;i++){
        var id =currentData[i].ID+"_"+db_code;
        html +="<li id='"+id+"' code='"+db_code+"' name ='"+currentData[i].NAME+"'><span class='lable-nr'>"+currentData[i].NAME+"</span><a class='pop-delete' href=\"javascript:void(0)\" onclick=\"reductionPsnAlias('"+id+"','"+db_code+"')\"></a></li>";
    }
    $ul = $(boxDivId).find("ul[class='label-list']").attr("alt",db_code).attr("count",0);
    $ul.empty();
    $ul.html(html);
    $(linkId).thickbox();
    $(linkId).click();
}

//取出已排除的别名
function reductionPsnAlias(id,db_code){
    var liId = "#"+id;
    var name = $(liId).attr("name");
    var currentID = id.substr(0,id.indexOf("_"));
    $(liId).parent().attr("count",1);
    $(liId).remove();
    var currentData = switchPsnAliasData(db_code);
    for(var i=0 ,len=currentData.length;i<len;i++){
        if(currentID==currentData[i].ID){
            removePsnAlias.push({"ID":currentData[i].ID,"NAME":currentData[i].NAME,"ACTION":"1"});
            currentData.myRemove(i);
            break;
        }
    }
    var frameId ="if_"+db_code;
    var document = getFrameDoc(frameId);
    $(document).find("#count_"+db_code).text(currentData.length);
}

//选择所有别名
function selectAllPsnAlias(src,db_code){
    var frameId ="if_"+db_code;
    var document = getFrameDoc(frameId);
    var checkboxAll = $(document).find(".exclude_box :checkbox");
    if(src.checked==true){
        checkboxAll.each(function (){
            if(this.disabled==false)
            this.checked=true;
        });
    }else{
        checkboxAll.each(function(){
            this.checked=false;
        });
    }
}

//根据db_code 取得对应的数据
function switchPsnAliasData (db_code){
    switch(db_code){
    case "SCI":
        return SCIPsnAliasJsonFRDB;
        break;
    case "SSCI":
        return SSCIPsnAliasJsonFRDB;
        break;
    case "ISTP":
        return ISTPPsnAliasJsonFRDB;
        break;
    default:
        return [];
        break;
    }
}

//建立检索式 格式 如右:au=(wang y*) not (wang ying or wang yi) or (wang yan or wang yingwing)
function bulidRetrieval(json){
    if(json.length<1){
        initNotIsiAlias=[];
        intoIsNotSearchAlias=[];
        var retrievalFirst = psnName;
        retrievalFirst = retrievalFirst.match(/^.*?\s\w/i)+"*";
        intoIsNotSearchAlias.push(retrievalFirst.replace("*", ""));
        intoIsNotSearchAlias.push(psnName);
        return $.trim(psnAliasFirstName);
    }
    var retrievalStr = "";
    var retrievalFirst = "";
    var tempArr = new Array();
    var tempStr = "";
    intoIsNotSearchAlias=[];
    if(json.length>0){
        retrievalFirst = psnName;
        retrievalFirst = retrievalFirst.match(/^.*?\s\w/i)+"*";
        for(var i=0;i<json.length;i++){
            tempArr.push(json[i].NAME.replace(/(\w*)(,\s+)(\w*)(-?)(\w*)/i,"$1 $3 $5"));
        }
        initNotIsiAlias = tempArr;
        if(tempArr.length>1){
            tempStr = tempArr.join(" or ");
        }else if(tempArr.length==1){
            tempStr = tempArr.join("");
        }   
        retrievalStr = "au= ("+retrievalFirst+") not ("+tempStr+") or ("+psnName+")";
        intoIsNotSearchAlias.push(retrievalFirst.replace("*", ""));
        intoIsNotSearchAlias.push(psnName);
        return retrievalStr;
    }
}

//根据选择的库建立检索式
function bulidCurrentRetrieval (db_code){
    var flag=false;
    if(criteria.firstName.length>0 || criteria.lastName.length>0 || criteria.cname.length>0){
        flag=true;
    }
    if("me"==globalType && ("SCI"==db_code || "SSCI"==db_code || "ISTP"==db_code) && flag){
        var currentData = switchPsnAliasData(db_code);
        criteria.firstName= bulidRetrieval(currentData);
    }else if(psnAliasFirstName!=""){
        criteria.firstName=psnAliasFirstName;
    }
}

//加载别名数据
function ajaxInitPsnAliasJsonFRDB(type,tmpOrgName,callback,db_code){
    if(!booleanPsnAlias){//如果不需要这个别名排除功能,直接callback就行
        if(db_code){
            callback(db_code);
        }else{
            callback(type,tmpOrgName);
        }
        return ;
    }
    SCIPsnAliasJsonFRDB=[];
    SSCIPsnAliasJsonFRDB=[];
    ISTPPsnAliasJsonFRDB=[];
   psnName =$.trim(CtoH($("#lname").val()))+" "+$.trim(replaceStr(CtoH($("#fname").val()),"-"," "));    
    data={'dbcodes':allowDbcode.join(','),'psnname':psnName};
    $.ajax({
        url:'/pubweb/publication/initPsnAliasJsonFRDB',
        type:'post',
        async:false,
        data:data,
        dataType:'json',
        timeout:'10000',
        success:function(json){
            if(json.result=='success'){
                for(var i = 0 ;i<allowDbcode.length;i++){
                    if(json[allowDbcode[i]]!=undefined||json[allowDbcode[i]]!=""){
                        var currentData = switchPsnAliasData(allowDbcode[i]);
                        var allowDbcodeJson=json[allowDbcode[i]];
                        if(allowDbcodeJson!=undefined||json[allowDbcode[i]]!=null){
                            $.each(allowDbcodeJson,function(){
                                currentData.push(this);
                            });
                        }
                    }
                }
                isFirstToSearch++;
                if(db_code){
                    callback(db_code);
                }else{
                    callback(type,tmpOrgName);
                }
                
            }else {
                if(db_code){
                    callback(db_code);
                }else{
                    callback(type,tmpOrgName);
                }
            }
        },
        error:function(){
            if(db_code){
                callback(db_code);
            }else{
                callback(type,tmpOrgName);
            }
        }
    });
}
//浏览器过滤器
function detectBrowsers (){
        var platform = window.navigator.platform.toString();
        var userAgent = window.navigator.userAgent.toLowerCase();
        var sys = platform.match(/win\d*/gi)?'windows':
        platform.match(/macintel/gi)?'mac':
        platform.match(/linux/gi)?'linux':'other';
        var browser,version,bit;
        switch(sys)
        {
            case 'windows':
            (/ms(ie)\s([\d.]*)/gi.test(userAgent))? '':
            (/(rv).?([\d.]*)/gi.test(userAgent))?'':
            (/(firefox).?([\d.]*)/gi.test(userAgent))? '':
            (/(chrome).?([\d.]*)/gi.test(userAgent))? '':
            (/(opera).?([\d.]*)/gi.test(userAgent))?'':
            (browser= 'unknow',version = '-1');
            break;
            case 'mac':
            (/version.?([\d.]+).*(safari)/gi.test(userAgent))? (browser=RegExp.$2,version=RegExp.$1):
            (/(firefox).?([\d.]*)/gi.test(userAgent))? '':
            (/(chrome).?([\d.]*)/gi.test(userAgent))? '':
            (/(opera).?([\d.]*)/gi.test(userAgent))?'':
            (browser= 'unknow',version = '-1');
            break;
            case 'linux':
            (/(firefox).?([\d.]*)/gi.test(userAgent))? '':
            (/(chrome).?([\d.]*)/gi.test(userAgent))? '':
            (/(opera).?([\d.]*)/gi.test(userAgent))?'':
            (browser= 'unknow',version = '-1');
            break;
            default:
            browser= 'unknow',version = '-1';
        }
        browser=browser || RegExp.$1=="rv"?"ie":RegExp.$1;
        version=version || RegExp.$2;
        version=parseFloat(version.match(/-?\d+\.?\d*/gi)[0]);
        switch(browser){
            case 'ie':
            bit = /x64/gi.test(userAgent) ? 64 :32;
            break;
            default:
            bit = 32;
        }
        var bitReg = new RegExp(bit,'gi');
        return AvailableBrowsers[sys]&&AvailableBrowsers[sys][browser]&&
                AvailableBrowsers[sys][browser]['min']<=version&&AvailableBrowsers[sys][browser]['max']>=version&&
                    (bitReg.test(AvailableBrowsers[sys][browser]['bit'].join(',')));
    }

//阻止浏览器的默认行为
function stopDefault(e) {
    if (e&&e.preventDefault)
        e.preventDefault();
    else
        window.event.returnValue = false;
    return false;
}
//增加一个remove数组元素方法,该方法改变调用方法的数组
Array.prototype.myRemove=function(dx)  
{  
    if(isNaN(dx)||dx>this.length){return false;}  
    for(var i=0,n=0;i<this.length;i++)  
    {  
        if(this[i]!=this[dx])  
        {  
            this[n++]=this[i];  
        }  
    }  
    this.length-=1;  
} ;
