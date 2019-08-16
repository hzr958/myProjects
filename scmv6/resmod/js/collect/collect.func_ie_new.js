//校验插件是否正常安装或是否需要更新
function detectOctopus(type) {
  try {
    if (browser.name == "IE" || client.browser.name == "Trident") {
      if (browser.version == "11.0") {
        var activeX = document.createElement("object");
        activeX.id = "IrisOctopus";
        activeX.classid = "CLSID:4FA0F169-4CF7-4CE7-A2A1-FF9FC5C7356C";
        document.body.appendChild(activeX);
      } else {
        window.onload = function() {
          obj = getIrisOctopus().addEventListener("OnJobCompleted", OnUpdate_ie);
        };
      }
    }
    if (ctopusDownloadResPath != "") {
      getIrisOctopus().SetDownLoadFileUrl(ctopusDownloadResPath + "scripts/iris_Octopus.html?a=" + 100 * Math.random());
    } else {
      getIrisOctopus().SetDownLoadFileUrl(
          pageContext_request_serverName + appContextPath + "/scripts/iris_Octopus.html?a=" + 100 * Math.random());
    }
    getIrisOctopus().SetOutTime("-1", "180000");
    var strVersion = getIrisOctopus().Version;
    if (!checkLastest(strVersion, CNT_Version)) {
      downloadOctopus(type, "update");
      return false;
    }
    return true;
  } catch (e) {
    downloadOctopus(type, "install");
    return false;
  }
}

// 点击查找按钮
function searchData_ie(type) {
  // 将查找条件两边的空格都去掉
  trimCriteria(type);
  tmpOrgName = $("#insName").val();
  // 获取选定的数据库
  search_DB_Code = getSearch_DB_Code();
  // 验证查询条件
  if (!validSearchInput(type)) {
    setWorkingStatus();
    return;
  }
  // 显示进度条
  // showPrepWorking();
  setWorkingStatus();
  ajaxInitPsnAliasJsonFRDB(type, tmpOrgName, goOnSearchData_ie);
}

// 继续searchData_ie的操作
function goOnSearchData_ie(type, tmpOrgName) {
  // 清空
  orgNames = "";
  affiliaton_insName = "";
  if (tmpOrgName != "") {
    $.ajax({
      url : '/pub/orgname/ajaxalias',
      type : 'post',
      data : {
        'orgName' : tmpOrgName,
        'dbCode' : search_DB_Code.join(":")
      },
      dataType : 'json',
      timeout : 10000,
      success : function(json) {
        doAction_ie(type, json);
      },
      error : function() {
        doAction_ie(type, {
          'default' : ''
        });
      }
    });
  } else {
    doAction_ie(type, {
      'default' : ''
    });
  }
}

// 开始查找
function doAction_ie(type, input_orgNames) {
  orgNames = input_orgNames;
  // 判断当前查询的机构所使用的语言是否符合指定查询数据库的语言要求
  if (!checkOrgNames(orgNames)) {
    setWorkingStatus();
    return;
  }
  // 设置检索条件到criteria中
  criteria.topic = CtoH($.trim($("#title").val()));
  criteria.deptName = CtoH($.trim($("#doiSearch").val()));
  psnAliasFirstName = replaceStr(CtoH($.trim($("#fname").val())), "-", " ");
  psnAliasFirstName = splitNamePY(psnAliasFirstName);
  criteria.firstName = psnAliasFirstName;
  criteria.lastName = CtoH($.trim($("#lname").val()));
  criteria.cname = CtoH($.trim($("#cname").val()));
  criteria.otherName = CtoH($.trim($("#oname_else").val()));
  if (publicationArticleType == '1') {
    if (($("#publicyear").val().indexOf("-") > -1)
        && (parseInt($.trim($("#publicyear").val()).split("-")[0]) > parseInt($.trim($("#publicyear").val()).split("-")[1]))) {
      $("#publicyear").val(
          $.trim($("#publicyear").val()).split("-")[1] + "-" + $.trim($("#publicyear").val()).split("-")[0]);
    }
    criteria.pubYear = CtoH($.trim($("#publicyear").val()));
  }
  affiliaton_insName = CtoH($.trim($("#insName").val()));
  // 选中的是按人员姓名检索还是按论文中作者名检索
  if ($(".selected-oneself_confirm").attr("value") == 1) {
    criteria.matchMode = 2;
  } else {
    criteria.matchMode = 0;
  }
  // 设置语言
  criteria.language = $.trim($("#lang").val());
  // 显示查找条件
  showSearchCriteria(type);
  // log
  getLogSerachCriteria();
  for (var i = 0; i < search_DB_Code.length; i++) {
    var db_code = search_DB_Code[i];
    // 万方医学数据库应该也没有了
    // if(db_code=='WanFangYX'){
    // isWanFangNext=true;
    // searchWanFangYX('1');
    // }
    // 按LZH那边说的是不用调用SetURL了，直接取search
    // else if (ins_url[db_code] && !current_url_set[db_code] && ins_url[db_code]["loginUrl"]!=null
    // &&
    // ins_url[db_code]["loginUrl"]!=""){
    // if(booleanPsnAlias){
    // bulidCurrentRetrieval (db_code);
    // }
    // getLoginSiteId_ie(db_code);//判断是否已经登录
    // }
    // else{
    // }
    // 开始查找
    setTimeout("ocxSearch_ie('" + db_code + "')", 100 * (i + 1)); // 应hh要求，每次查找前先停100ms，以避免各组件相互冲突
  }
}

// 判断当前使用的是校内或校外2 校内 1--------按LZH那边说的是不用调用SetURL了，直接取search
function getLoginSiteId_ie(db_code) {
  var search_id = ins_url[db_code]["dbBitCode"];
  var inside_l_url = ins_url[db_code]["loginUrlInside"];
  var outside_l_url = ins_url[db_code]["loginUrl"];
  getIrisOctopus().SetURL(search_id, inside_l_url, outside_l_url);
  setWorkingStatus();
}

// 直接调用组件开始查找
function ocxSearch_ie(db_code) {
  if (multDB && booleanPsnAlias) {
    bulidCurrentRetrieval(db_code);
  }
  if (db_code == "SCI" || db_code == "SCIE") {
    criteria.currentLimit = "SCI";
  } else if (db_code == "SSCI") {
    criteria.currentLimit = "SSCI";
  } else if (db_code == "ISTP") {
    criteria.currentLimit = "ISTP,ISSHP";
  } else {
    criteria.currentLimit = "";
  }
  // 设置单位检索式
  if (orgNames[db_code]) {
    criteria.affiliaton = orgNames[db_code];
  } else {
    criteria.affiliaton = affiliaton_insName;
  }
  // 调用插件Search方法
  getIrisOctopus().Search(ins_url[db_code]["dbBitCode"], criteria);
  // 显示工作进度条
  setWorkingStatus();
}

/*
 * varSitId 站点ID 1 scopus 2 sd 4 isi 8 ieee 16 cjn 32 wp 64 wf ;1024 cnipr, varCmdId 命令ID 0 login; 1
 * Search; 2 GoPage; 3 SetRows; 4 Sort; 5 Analyse; 6 GetRetRows; 7 PageDown; 8 PageUp; 9
 * SearchByString 11 SetUrl; 13 SearchMulti; 14 refresh; 1025 CiteUpdate varRetInt 返回整型值 0：
 * 没有找到记录；-1：没有登录；-2：查找条件出错；-4：无法访问站点；-9：要输入验证码 varRetStr 返回内容：如果varRetInt是-1，-4，就不用第二次查找了。
 * varRetInt 批量检索，此字段返回总条数，如果失败则返回负数.
 */
function OnUpdate_ie(varSitId, varCmdId, varRetInt, varRetStr) {
  // if(isTailSearch){
  // refreshTailArrBySearch(varSitId,varRetInt,varRetStr);//调下一个库,最后一个库时记录日志
  // }else{
  // }
  // alert("varSitId="+varSitId+"\nvarCmdId="+varCmdId+"\nvarRetInt="+varRetInt+"\n"+varRetStr);
  var db_code = getDbCodeByBitCode(varSitId);
  // log
  onUpdateLog(db_code, varCmdId, varRetInt, varRetStr);
  if ('ISTP' == db_code && !isNextIstp
      && varRetStr.indexOf("Invalid query. Please check that the timespan is within the selected database") >= 0) {
    criteria.currentLimit = "ISTP";
    getIrisOctopus().Search(ins_url[db_code]["dbBitCode"], criteria);
    isNextIstp = true;
    return;
  }
  if (varCmdId == 14) {
    return; // 针对refresh，不进行任何操作
  } else if (varCmdId == 5) {
    if (varRetInt == "-9") {// 返回-9，需要输入验证码 中国知识产权网
      var loginurl = "http://search.cnipr.com/search!doOverviewSearch.action";
      varRetStr = replaceStr(referencesearch_msg_needverify, "@loginurl@", loginurl);
      writeHtml(db_code, varRetStr, '0', 0);
      setDbSerarchResCount(db_code, 0);
    } else {// 如果是取详情返回，则直接导入
      importData_chrome(varRetStr, db_code);
      return;
    }
  } else if (varRetInt == "-1") // 如果返回-1，则提示需要登录
  {
    if (!ins_url[db_code] || ins_url[db_code]["loginUrl"] == "") {
      varRetStr = referencesearch_msg_nopermission;
    } else {
      var loginurl = ins_url[db_code]["loginUrl"];
      loginurl = loginurl.replace("http://apps", "http://www");
      varRetStr = replaceStr(referencesearch_msg_needlogin, "@loginurl@", loginurl);
    }
    varRetStr = replaceStr(varRetStr, "@database@", getDBName(db_code));
    writeHtml(db_code, varRetStr, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-2") {
    // scm-6608
    varRetStr = "没有命中的记录";
    writeHtml(db_code, varRetStr, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-6" || varRetInt == "-7") {
    if (navigator.userAgent.indexOf("Windows NT 5") != -1) {
      writeHtml(db_code, '<br/>' + referencesearch_hlep_langxp, '0', 0);
    } else {
      writeHtml(db_code, '<br/>' + referencesearch_hlep_langwin7, '0', 0);
    }
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-3") {
    writeHtml(db_code, '<br/>' + referencesearch_msg_nopermission, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-4") {
    writeHtml(db_code, '<br/>' + referencesearch_msg_noready, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-5") {
    writeHtml(db_code, '<br/>' + referencesearch_msg_timeout, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else if (varRetInt == "-9") {// 返回-9，需要输入验证码 中国知识产权网
    var loginurl = "http://search.cnipr.com/search!doOverviewSearch.action";
    varRetStr = replaceStr(referencesearch_msg_needverify, "@loginurl@", loginurl);
    writeHtml(db_code, varRetStr, '0', 0);
    setDbSerarchResCount(db_code, 0);
  } else {
    if (varCmdId == 11) {
      if (varRetInt == "1")// 如果是校内登录
      {
        current_url_set[db_code] = "true";
        dojob(db_code, ins_url[db_code]["actionUrlInside"], ins_url[db_code]["fulltextUrlInside"], varRetStr);
        return;
      } else if (varRetInt == "2")// 如果是校外登录
      {
        current_url_set[db_code] = "true";
        dojob(db_code, ins_url[db_code]["actionUrl"], ins_url[db_code]["fulltextUrl"], varRetStr);
        return;
      } else if (varRetInt == '3') {// 不支持当前浏览器版本,重新下载插件
        if (db_code == 'Scopus') {
          varRetStr = replaceStr(referencesearch_msg_scopus_needreinstall, "@reinstall@",
              "/pub/import/plugin?downloadType=install&returnInt=" + varRetInt);
          writeHtml(db_code, varRetStr, '0', 0);
          setDbSerarchResCount(db_code, 0);
        }
      }
    } else if (varCmdId == 14) {
    } else // 如果是search ，则判断是否需要登录
    {
      if (varCmdId == "1") { // 设置结果条数
        setDbSerarchResCount(db_code, varRetInt);// 重设DB结果数
      }
      if (varRetInt == "0" && varCmdId == "1") // 如果返回值为0,且操作为查找时则认为当前没有查询到成果
      {
        if ("en_US" == browserLanguage && " ,16,256,512,32,64,1024,4096,8192".indexOf("," + varSitId) != -1) {
          writeHtml(db_code, referencesearch_msg_norecord + en_searchzh_notfind, '0', varRetInt);
        } else if (("zh_TW" == browserLanguage || "zh_HK" == browserLanguage)
            && " ,16,256,512,32,64,1024,4096,8192".indexOf("," + varSitId) != -1) {
          writeHtml(db_code, referencesearch_msg_norecord + tw_searchzh_notfind, '0', varRetInt);
        } else {
          writeHtml(db_code, referencesearch_msg_norecord, '0', varRetInt);
        }
      } else {
        varRetStr = replaceStr(varRetStr, 'setInterval("refreshDB(', 'setInterval("parent.refreshDB(');
        writeHtml(db_code, varRetStr, "1", varRetInt);
      }
    }
  }
  // 隐藏检索页面，显示检索结果页面
  switchDIV(true);
  setWorkingStatus();
  checkedDBListShow(db_code, varRetInt);
  resetSelectedPubSum();
  resetCheckBoxClickEvent(db_code);
}

// 检索结果中的上一页方法
function PageUp(doc) {
  var dbCode = doc.getElementById("db_code").value;
  if (hasSelected(dbCode)) {
    // 提示栏
    smate.showTips._showNewTips(referencesearch_msg_turnpage, referencesearch_msg_alert, "changePageBySure('" + dbCode
        + "','up')", undefined, referencesearch_btn_sure, referencesearch_btn_cancel);
    $("#alert_box_cancel_btn").unbind();// 解绑取消按钮原来的事件
    $("#alert_box_cancel_btn").bind('click', function() {// 重新绑定事件，直接写在上面无效才这样写的
      changePageByCancle(dbCode, 'up');
    });
  } else {
    changePageByCancle(dbCode, "up");// 没选择成果，则采用点击取消按钮的逻辑，不导入成果
  }
}

// 检索结果中的下一页方法
function PageDown(doc) {
  var dbCode = doc.getElementById("db_code").value;
  if (hasSelected(dbCode)) {
    // 提示栏
    smate.showTips._showNewTips(referencesearch_msg_turnpage, referencesearch_msg_alert, "changePageBySure('" + dbCode
        + "','down')", undefined, referencesearch_btn_sure, referencesearch_btn_cancel);
    $("#alert_box_cancel_btn").unbind();// 解绑取消按钮原来的事件
    $("#alert_box_cancel_btn").bind('click', function() {// 重新绑定事件，直接写在上面无效才这样写的
      changePageByCancle(dbCode, 'down');
    });
  } else {
    changePageByCancle(dbCode, "down");// 没选择成果，则采用点击取消按钮的逻辑，不导入成果
  }
}
/**
 * 点击弹出框确定按钮，加载下一页的数据,同时将选择的成果导入
 * 
 * @param db_code
 * @param type
 *          操作类型，向上翻页或者向下翻页
 * @returns
 */
function changePageBySure(dbCode, type) {
  isSearchButton = false;
  operationStr = "";
  if (type == 'down') {// 向下翻页
    operationStr = "getIrisOctopus().PageDown(" + ins_url[dbCode]["dbBitCode"] + ");";
  } else if ("up") {// 向上翻页
    operationStr = "getIrisOctopus().PageUp(" + ins_url[dbCode]["dbBitCode"] + ");";
  }
  getDataXML_chrome("turnpage", dbCode);// 显示导入成果的加载圈，同时导入成果
  eval(operationStr);// 调用插件，加载下一页数据
  operationStr = "";
  $("#smate_alert_tips_div").remove();// 关闭弹出窗口
  setWorkingStatus();
}

/**
 * 点击弹出框取消按钮，进行翻页但不导入成果
 * 
 * @param dbCode
 * @param type
 *          翻页类型
 * @returns
 */
function changePageByCancle(dbCode, type) {
  isSearchButton = false;
  operationStr = "";
  if (type == 'down') {// 向下翻页
    operationStr = "getIrisOctopus().PageDown(" + ins_url[dbCode]["dbBitCode"] + ");";
  } else if ("up") {// 向上翻页
    operationStr = "getIrisOctopus().PageUp(" + ins_url[dbCode]["dbBitCode"] + ");";
  }
  eval(operationStr);// 调用插件，加载下一页数据
  operationStr = "";
  $("#smate_alert_tips_div").remove();// 关闭弹出窗口
  setWorkingStatus();
}

/**
 * 判断有没有选中成果
 * 
 * @returns
 */
function hasSelected(db_code) {
  var objdoc = getFrameDoc("if_" + db_code);
  var cbxs = null;
  if (db_code == "CNIPR") {// 中国知识产权网的是单选框
    cbxs = $(objdoc).find(":radio[name='iris_id']");
  } else {
    cbxs = $(objdoc).find(":checkbox[name='iris_id']");
  }
  if (cbxs.size() > 0) {
    var i;
    var rec_code = "";
    for (i = 0; i < cbxs.length; i++) {
      if (cbxs[i].checked) {
        rec_code += cbxs[i].value + ";";
      }
    }
  }
  return rec_code.length > 0;
}

/**
 * 确认导入选中的成果，并跳至指定页码
 * @param dbCode
 * @returns
 */
function toPagesBySure(dbCode,pages,isSure) {
  operationStr = "parent.getIrisOctopus().GoPage(" + ins_url[dbCode]["dbBitCode"] + "," + pages + ");";
  if(isSure){
    getDataXML_chrome("turnpage", dbCode);// 显示导入成果的加载圈，同时导入成果
  }
  eval(operationStr);// 调用插件，加载下一页数据
  operationStr = "";
  $("#smate_alert_tips_div").remove();// 关闭弹出窗口
  setWorkingStatus();
}

// 检索结果中的跳转到某一页方法
function GoPage(doc) {
  isSearchButton = false;
  var pages = doc.getElementById("GoPageID").value;
  // jAlert(pages);
  var db_code = doc.getElementById("db_code").value;

  var pageIntVal = parseFloat(pages);

  if (pageIntVal.toString() == "NaN") {
    smate.showTips._showNewTips(referenceSearch_msg_integer, referencesearch_msg_alert);
    $("#alert_box_cancel_btn").hide();
    $("#alert_box_close_btn").hide();
  } else if (pageIntVal <= 0) {
    pages = "1";
  } else if (pageIntVal > 9999999999) {
    pages = 999999999;
  } else {
    pages = pageIntVal;
  }
  if (hasSelected(db_code)) {
    // 提示栏
    smate.showTips._showNewTips(referencesearch_msg_turnpage, referencesearch_msg_alert, "toPagesBySure('" + db_code
        + "'"+",'"+pages+"',"+true+")", undefined, referencesearch_btn_sure, referencesearch_btn_cancel);
    $("#alert_box_cancel_btn").unbind();// 解绑取消按钮原来的事件
    $("#alert_box_cancel_btn").bind('click', function() {// 重新绑定事件，直接写在上面无效才这样写的
      toPagesBySure(db_code,pages,false);
    });
  } else {
    toPagesBySure(db_code,pages,false);
  }
  doc.getElementById("GoPageID").value = pages;// 将当前跳转的页号进行修改
}

// 检索结果中的选择每页显示多少条记录的方法
function showitem(doc) {
  isSearchButton = false;
  var count = parseInt(doc.getElementById("showpage").value);
  var db_code = doc.getElementById("db_code").value;
  getIrisOctopus().SetRows(ins_url[db_code]["dbBitCode"], count);
  setWorkingStatus();
}

// 获取总页数
function getTotalPages(totalCount, pageSize) {
  if (totalCount == null || totalCount < 0) {
    return -1;
  }
  var count = parseInt(totalCount / pageSize);
  if (totalCount % pageSize > 0) {
    count++;
  }
  return count;
}

// 取消导入
function cancelJob() {
  isSearchButton = false;
  $(".btnSearch").removeAttr("disabled");
  $("#imp_btn").removeAttr("disabled");
  if (isWanFangNext == true) {
    $("#divMessage").hide();
    $("#divMessage")[0].innerHTML = "";
    return;
  }
  if (isButtonExclude) {
    for (var i = 0; i < search_DB_Code.length; i++) {
      if (search_DB_Code[i] == "SCIE" || search_DB_Code[i] == "SCI" || search_DB_Code[i] == "SSCI"
          || search_DB_Code[i] == "ISTP") {
        $(getFrameDoc("if_" + search_DB_Code[i])).find("#btnExclude")[0].disabled = false;
      }
    }
    isButtonExclude = false;
  }
  getIrisOctopus().Cancel("-1", "-1");
  setWorkingStatus();
  // 取消任务时，如果判断已经取到了xml，则仅将此部分xml显示
  if (entireXML != "") {
    collect_TaskCount = 0;
    importData_chrome("", "");
  }
  $("#ifClick").val(true);
}

function sortitem(doc) {
  isSearchButton = false;
  var sort_value = doc.getElementById("sortby").value;
  var db_code = doc.getElementById("db_code").value;
  getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"], sort_value);
  setWorkingStatus();
}

function sortclassify(doc) {
  isSearchButton = false;
  var sort_value = doc.getElementById("classify").value;
  var db_code = doc.getElementById("db_code").value;
  getIrisOctopus().Sort(ins_url[db_code]["dbBitCode"], sort_value);
  setWorkingStatus();
}

// 刷新数据库
function refreshDB(dbid) {
  getIrisOctopus().ExecuteCommand(dbid, 14, 0, 0);
}
