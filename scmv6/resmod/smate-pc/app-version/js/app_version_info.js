var App = App ? App : {};
var $inputField = document.querySelector('input[type="file"]'); //文件上传INPUT

//添加input值改变时候事件，会在添加进文件的时候触发
App.fileInputChangeEvent = function(){
  var $self = $(this);
  //仅在值不为空的时候做校验
  $inputField = document.querySelector('input[type="file"]'); //文件上传INPUT
  if ($inputField.value === null || $inputField.value === "") {
    $("#versionSize").val("");
    $("#downloadUrl").val("");
    return;
  }
  const $validation = App.fileValidation($inputField.files[0]);
  if ($validation) {
    $("#login_box_refresh_currentPage").val("false");//登录不跳转                  
    BaseUtils.checkTimeout(function(){//是否登录
      App.uploadFileToServer($self);
    }); 
    return;
  }
};



/**
 * 文件校验
 * @param {FileObject}    file    所需校验的文件
 * 
 * @return  {Boolean}                       是否同时符合大小和类型
 */
App.fileValidation = function(file) {
  function fileUploadReset(str) {
    $inputField.value = null;
    scmpublictoast(str, 1000);
    return false;
  }
  //文件不能为空
  if (!file) {
    return fileUploadReset("请选择APP文件");
  }
  //文件大小不能等于0MB
  if (file.size == 0){
    return fileUploadReset("不能上传空的文件");
  }
  //文件类型验证
  var $supportType = false;
  const $fileName = file.name;
  var supportType = [".apk", ".ipa"];//支持的文件类型
  //支持的文件类型 start
  for (var i = 0; i < supportType.length; i++) {
    if ($fileName.substr($fileName.lastIndexOf(".")).toLowerCase() === supportType[i].toLowerCase()) {
      $supportType = true;
      break;
    }
  }
  if (!$supportType) {
      return fileUploadReset("不支持上传该类型文件");
  }
  //支持的文件类型  end
  return true;
}


/**
 * 上传文件至伺服器
 * @param {HTMLElement} o     所需上传文件的对象，其class中包含‘fileupload__box’
 * @param {Object}    data    用于单独调用的时候赋予的额外参数，多用于非直接上传场景
 */
App.uploadFileToServer = function (o, data) {
  try {
    if (typeof o === "undefined") {
      throw "A fileupload box must be assigned in uploadFileToServer method";
    }
  } catch (e) {
    console.error(e);
  }
  const $inputField = document.querySelector('input[type="file"]'); //文件上传INPUT
  const $uploadFile = $inputField.files[0]; //文件上传的具体文件

  //构建一个FormData对象，把文件、初始化参数、额外参数添加到对象中
  const $formData = new FormData();
  $formData.append("filedata", $uploadFile);
  $formData.append("fileDealType", "appfile");
  //构建一个新XMLHttpRequest对象上传文件
  const xhr = new XMLHttpRequest();
  xhr.open("post", "/fileweb/upload/app");
  xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");

  //保存至总文件表之后的回调函数，保存后即为完成状态
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      App.fileUploadCallback(xhr);
    }
  };
  xhr.send($formData);
};

/**
 * 上传文件后的回调方法
 */
App.fileUploadCallback = function(data){
  var response = JSON.parse(data.response);
  if(response != null && response.result == "success"){
    var extendFileInfo = response.extendFileInfo[0];
    //文件大小
    var fileSize = parseInt(extendFileInfo.fileSize) / 1024;
    if(fileSize >= 1 && fileSize < 1024){
      //KB
      fileSize = Math.ceil(fileSize) + " KB";
    }else{
      //MB
      fileSize = Math.ceil(fileSize / 1024) + " MB";
    }
    $("#versionSize").val(fileSize);
    $("#downloadUrl").val(extendFileInfo.downloadUrl);
    $("#file_error").text("");
    scmpublictoast("上传成功", 1000);
  }else{
    scmpublictoast("上传失败", 1000);
    $("#versionSize").val("");
    $("#downloadUrl").val("");
  }
}


//提交版本信息
App.updateVersionInfo = function(){
  if(App.validateNeedInfo()){
    //文件上传完，获取文件信息，再更新版本信息
    var versionJson = {
        "optType":$.trim($("#optType").val()),
        "mustUpdate": $.trim($("input[name='mustUpdate']:checked").val()),
        "appType": $.trim($("input[name='appType']:checked").val()),
        "versionName": $.trim($("#versionName").val()),
        "versionCode": $.trim($("#versionCode").val()),
        "versionDesc": $.trim($("#versionDesc").val()),
        "newMd5": $.trim($("#newMd5").val()),
        "versionSize": $.trim($("#versionSize").val()),
        "downloadUrl": $.trim($("#downloadUrl").val()),
        "id": $.trim($("#versionId").val())
    }
    $.ajax({
      url: "/oauth/version/ajaxupdate",
      type: "post",
      data: versionJson,
      dataType: "json",
      success: function(data){
        if(data != null && data.result == "success"){
          window.location.href = location.href;
        }else{
          scmpublictoast("操作失败", 1000);
        }
      },
      error: function(){
        scmpublictoast("操作失败", 1000);
      }
    });
  }
}

//校验提交信息
App.validateNeedInfo = function(){
  var check = true;
  if(BaseUtils.checkIsNull($.trim($("input[name='mustUpdate']:checked").val()))){
    $("#must_update_error").text("请选择是否要强制更新");
    check = false;
  }else{
    $("#must_update_error").text("");
  }
  if(BaseUtils.checkIsNull($.trim($("input[name='appType']:checked").val()))){
    $("#appType_error").text("请选择app类型");
    check = false;
  }else{
    $("#appType_error").text("");
  }
  if(BaseUtils.checkIsNull($.trim($("#versionName").val()))){
    $("#version_name_error").text("请输入正确的版本号");
    check = false;
  }else{
    $("#version_name_error").text("");
  }
  if(BaseUtils.checkIsNull($.trim($("#versionCode").val()))){
    $("#version_code_error").text("请输入正确的版本code");
    check = false;
  }else{
    $("#version_code_error").text("");
  }
  if(BaseUtils.checkIsNull($.trim($("#versionDesc").val()))){
    $("#version_desc_error").text("请输入正确的版本描述");
    check = false;
  }else{
    $("#version_desc_error").text("");
  }
  if(BaseUtils.checkIsNull($.trim($("#versionSize").val())) || BaseUtils.checkIsNull($.trim($("#downloadUrl").val()))){
    $("#file_error").text("请上传app安装文件");
    check = false;
  }else{
    $("#file_error").text("");
  }
  return check;
}

//编辑版本信息, optType:2(编辑), 1(新增)
App.editVersionInfo = function(appType, id, optType){
  App.cancelEditVersionInfo();
  $.ajax({
    url: "/oauth/version/ajaxget",
    type: "post",
    data: {
      "appType": appType,
      "id": id
    },
    dataType: "json",
    success: function(data){
      if(data != null && data.result == "success"){
        var versionInfo = data.versionInfo ? JSON.parse(data.versionInfo) : {};
        if("1" == versionInfo.mustUpdate){
          $("#must_update").click();
        }else{
          $("#not_must_update").click();
        }
        if("IOS" == appType){
          $("#appType_ios").click();
        }else{
          $("#appType_android").click();
        }
        $("#versionName").val(versionInfo.versionName);
        $("#version_name_error").text("");
        $("#versionCode").val(versionInfo.versionCode);
        $("#version_code_error").text("");
        $("#versionDesc").val(versionInfo.versionDesc);
        $("#version_desc_error").text("");
        $("#versionSize").val(versionInfo.versionSize);
        $("#downloadUrl").val(versionInfo.downloadUrl);
        $("#versionId").val(versionInfo.id)
        $("#optType").val(optType);
        $("#fileInput").hide();
        $("#file_error").text("");
        $("#show_file_input").val("Scholarmate.apk");
        $("#show_file_info_div").show();
        $("#edit_version_info").css("visibility", "visible");
      }
    },
    error: function(){
      
    }
  });
}

//删除版本信息
App.deleteVersionInfo = function(AppType, id){
  smate.showTips._showNewTips("真的真的真的确定要删除该版本信息么", "提示", "App.sendUpdateReq('"+AppType+"', '"+id+"')", "");
}



//发送更新（新增、编辑、删除）请求
App.sendUpdateReq = function(AppType, id){
  $.ajax({
    url: "/oauth/version/ajaxupdate",
    type: "post",
    data: {
      "appType": AppType,
      "optType": "0",
      "id": id
    },
    dataType: "json",
    success: function(data){
      if(data != null && data.result == "success"){
        window.location.href = location.href;
      }else{
        scmpublictoast("操作失败", 1000);
      }
    },
    error: function(){
      scmpublictoast("操作失败", 1000);
    }
  });
}

//移除文件
App.removeFile = function(){
  $("#fileInput").show();
  $("#file_error").text("");
  $("#show_file_input").val("Scholarmate.apk");
  $("#show_file_info_div").hide();
  $("#versionSize").val("");
  $("#downloadUrl").val("");
}

//取消修改版本信息
App.cancelEditVersionInfo = function(){
  $("#edit_version_info").css("visibility", "hidden");
}


//前一页
App.prePage = function(){
  var totalPages = parseInt($("#totalPages").val());
  var currentPage = parseInt($("#pageNo").val());
  var prePage = currentPage - 1;
  if(prePage >= 1){
    window.location.href = "/oauth/version/list?appType="+$.trim($("#list_app_type").val()) + "&page.pageNo=" + prePage;
  }
}

//后一页
App.nextPage = function(){
  var totalPages = parseInt($("#totalPages").val());
  var currentPage = parseInt($("#pageNo").val());
  var nextPage = currentPage + 1;
  if(nextPage <= totalPages){
    window.location.href = "/oauth/version/list?appType="+$.trim($("#list_app_type").val()) + "&page.pageNo=" + nextPage;
  }
}

//跳转到第几页
App.toPage = function(toPage){
  if(!$(this).hasClass("active")){
    window.location.href = "/oauth/version/list?appType="+$.trim($("#list_app_type").val()) + "&page.pageNo=" + toPage;
  }
}