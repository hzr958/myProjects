var history = history ? history : {};
/**
 * js引用 search.history.js"> search.history。controller.js css引用 ${resmod }/mobile/css/mobile.css 页面定义
 * <div id="showHistory"> </div> <div class="dev_history_comment">页面的内容</div>
 */
// 显示历史记录
history.showHistory = function() {
  desPsnId = $("#desPsnId").val();
  var his = new History(desPsnId);
  var data = his.getList();

  if (data) {
    var maxLength = data.length > 8 ? 8 : data.length;
    var startHtml = '<div class="search-history_harder">' + '<span class="search-history_harder-title">检索记录</span>'
        + '</div>' + '<div class="search-history_body" id="history">';
    var endHtml = '<div class="show_histroy-up_tip" onclick="history.hideHistory();"><i class="material-icons">keyboard_arrow_up</i></div></div>';
    var itemHtml = "";
    for (var i = 0; i < maxLength; i++) {
      itemHtml = itemHtml + '<div class="search-history_body-item" value='+this.htmlEncode(decodeURIComponent(data[i].title))+'>'
          + '<i class="material-icons search-history_body-tip">query_builder</i>';
      itemHtml = itemHtml + '<div class="search-history_body-item_detail" onclick="history.hisKeyhref(' + i + ');">'
          + this.htmlEncode(decodeURIComponent(data[i].title)) + '</div>'
          + '<div class="new-Represent_achieve-item_func" style="height: 30px; width: 30px; padding-left: 10px;" onclick="history.removeItem(this)">'
          + '<i class="material-icons" style="font-size: 20px;">close</i>'
          + '</div></div>';
    }
    itemHtml = itemHtml + '<div style="text-align: center;border-bottom: 0px solid #dddddd;padding-top: 10px;padding-bottom: 14px;">'
    +'<div onclick="history.cleanAll();" style="margin-left: 0px;font-size: 14px;width: 180px;height: 20px;">清除全部</div></div>'
    if (maxLength>0) {
       $(".dev_history_comment").hide();
       $("#showHistory").html(startHtml + itemHtml + endHtml);
       $("#showHistory").show();
    }
  }
}
// 把标签显示成字符串
history.htmlEncode = function(html) {
  // 1.首先动态创建一个容器标签元素，如DIV
  var temp = document.createElement("div");
  // 2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
  (temp.textContent != undefined) ? (temp.textContent = html) : (temp.innerText = html);
  // 3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
  var output = temp.innerHTML;
  temp = null;
  return output;
}
history.htmlDecode = function(text) {
  // 1.首先动态创建一个容器标签元素，如DIV
  var temp = document.createElement("div");
  // 2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
  temp.innerHTML = text;
  // 3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
  var output = temp.innerText || temp.textContent;
  temp = null;
  return output;
}
// 点击关键词跳转
history.hisKeyhref = function(i) {
  desPsnId = $("#desPsnId").val();
  var his = new History(desPsnId);
  var data = his.getList();
  if (data) {
    // 保存到历史记录
    var his = new History(desPsnId);
    his.add(data[i].title, data[i].link, "");
    history.UpdateUrlParam("searchString",data[i].title);//改变url中的参数
    try {
      fn = eval(hisKeyHref);
      if (typeof fn === 'function'){
        fn.call(this, data[i].title, data[i].link); 
      }else{
        window.location.href = data[i].link.substring(0, data[i].link.indexOf("fromPage=") + 9) + $("#fromPage").val();
      } 
    } catch(e) {
      window.location.href = data[i].link.substring(0, data[i].link.indexOf("fromPage=") + 9) + $("#fromPage").val();
    }
  }
}
// 隐藏清除历史记录
history.hideHistory = function() {
  $("#showHistory").html("");
  $("#showHistory").hide();
  $(".dev_history_comment").show();// 原来页面的内容显示
}
history.cleanAll = function(){
  history.alertBox();
}
history.cleanAllHis = function(){
  var his = new History(desPsnId);
  his.clearHistory();
  history.hideHistory();
}
history.removeItem = function(obj){//删除单个历史记录
  var his = new History(desPsnId);
  var title = $(obj).prev().text();
  his.deleteItem(title);
  $(".search-history_body-item[value='"+title+"']").remove();    
  if($(".search-history_body-item").length==0){
    history.hideHistory();
  }
}
history.cancelClean = function(){
  $("#historyBox").hide();
}
history.alertBox = function(){
  if($("#historyBox").length>0){
    $("#historyBox").show();
    return;
  }
  var boxhtml = '<div class="black_top" style="display: flex;" id="historyBox">'+
  '<div class="screening_box">'+
    '<div class="screening">'+

      '<div class="screening_tx" id="dynpubtitle" style="padding-top:20px;">是否清除全部历史记录</div>'+
      '<p>'+
        '<input type="button" value="确&nbsp;&nbsp;定" onclick="history.cleanAllHis()" class="determine_btn"><input type="button" onclick="history.cancelClean()" value="取&nbsp;&nbsp;消" class="cancel_btn">'+
      '</p>'+
    '</div>'+
  '</div>'+
 '</div>';
  $("#showHistory").append(boxhtml);
  $("#historyBox").show();
}

// 添加 或者 修改 url中参数的值
history.UpdateUrlParam = function(name, val) {
    var thisURL = document.location.href;
    // 如果 url中包含这个参数 则修改
    if (thisURL.indexOf(name+'=') > 0) {
        var v = history.getUrlParam(name);
        if (v != null) {
            // 是否包含参数
            thisURL = thisURL.replace(name + '=' + v, name + '=' + val);

        } else {
            thisURL = thisURL.replace(name + '=', name + '=' + val);
        }
        
    } else{
      if (thisURL.indexOf('?') > 0) {
        thisURL += "&"+name + '=' + val;
      }else{
        thisURL += "?"+name + '=' + val;
      }
    } 
    history.pushState('','',thisURL);
};
history.getUrlParam = function(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg);  //匹配目标参数
  if (r != null) return unescape(r[2]); return null; //返回参数值
}