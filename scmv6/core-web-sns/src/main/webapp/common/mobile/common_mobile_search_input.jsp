<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="paper__func-tool">
  <div class="paper__func-box" style="height: 30px; line-height: 30px;width: 82%;" id="paper_search_func-box_div">
    <!-- <a onclick="" id="showIcon1" class="paper__func-search" style="margin-top: -6px;"></a> -->
    <a class="paper__func-search" id="showIcon1" style="height: 32px; display: none; width: 8%; min-width: 28px;" onclick="doSearch();"></a>    
    <form action="" style="padding: 0px; margin: 0px;width: 93%; padding-left:5px;" id="common_search_form">
      <input onkeydown="" onchange="" type="search" autocomplete="off"
        id="searchStringInput" class="paper__func-search__flag"
        style="text-indent:0px;line-height: 25px; font-size: 16px; width: 100%;" placeholder="检索论文"
        value="">
    </form>
    <a class="paper__func-search" id="showIcon2" style="height: 32px; width: 10%;" onclick="doSearch();"></a>
    
<!--     <a onclick="" id="showIcon2" class="paper__func-search" style="margin-top: -8px;"></a> -->
  </div>
  
  <div class="message-page__fuctool-right_tip" style="margin-right: 8px; margin-left: 16px;display:none;" id="common_search_filter_div">
    <i class="material-icons" id="common_search_filter_icon" onclick="">filter_list</i>
  </div>
</div>

<script type="text/javascript">
var commonMobileSearch = commonMobileSearch ? commonMobileSearch : {};
commonMobileSearch.initSearchInput = function(options){
  //定义输入框各种事件需要执行的函数
  var defaultOptions = {
      "searchFunc": "", //点击检索图标执行函数
      "formAction": "", //form表单action属性
      "inputOnkeydown": "", //检索框onkeydown事件
      "inputOnchange": "", //检索框onchange事件
      "oninput":"",//检索框oninput事件
      "searchFilter": "", //过滤条件图标点击事件
      "searchInputVal": "",//检索的字符串
      "onsubmit": "return false;",//form提交事件
      "needFilter": false, //是否需要显示过滤条件图标
      "placeHolder": "",
      "inputOnfocus": "",
      //下面是与样式相关的
      "searchBoxDivStyle": "",
      "searchIcon2Style": "",
      "searchFilterStyle": ""
  }
  var opts = $.extend(defaultOptions, options);
  //改变样式
  if(opts.searchBoxDivStyle){
      $("#paper_search_func-box_div").attr("style", opts.searchBoxDivStyle);
  }
  /* if(opts.searchIcon2Style){
    $("#showIcon2").attr("style", opts.searchIcon2Style);
  } */
  if(opts.searchFilterStyle){
    $("#common_search_filter_div").attr("style", opts.searchFilterStyle);
  }
  //设置检索框检索字符串
  $("#searchStringInput").val(opts.searchInputVal);
  //设置输入框默认提示语
  if(opts.placeHolder){
    $("#searchStringInput").attr("placeholder", opts.placeHolder);
  }
  //是否显示过滤条件图标
  if(opts.needFilter){
    $("#common_search_filter_div").show();
    $("#common_search_filter_icon").attr("onclick", opts.searchFilter);
  }else{
    $("#common_search_filter_div").hide();
    $("#paper_search_func-box_div").width("96%");
  }
  //设置检索图标点击事件
  $("#showIcon1").attr("onclick", opts.searchFunc);
  $("#showIcon2").attr("onclick", opts.searchFunc);
  //设置输入框输入事件
  $("#searchStringInput").attr("onkeydown", opts.inputOnkeydown);
  $("#searchStringInput").attr("onchange", opts.inputOnchange);
  $("#searchStringInput").attr("oninput", opts.oninput);
  $("#searchStringInput").attr("onfocus", opts.inputOnfocus);
  //设置form的action属性
  $("#common_search_form").attr("action", opts.formAction);
  $("#common_search_form").attr("onsubmit", opts.onsubmit);
  //设置输入框获取失去焦点时检索图标的显示位置
  if($("#searchStringInput").val()=="" && !$("#searchStringInput").is(":focus")){
    $("#showIcon1").show();
    $("#showIcon2").hide();
  }else{
      $("#showIcon1").hide();
      $("#showIcon2").show();         
  }
  $("#searchStringInput").focus(function(){             
    if($("#searchStringInput").val()!=""){
      $("#showIcon1").hide();
      $("#showIcon2").show();
    }
  });
  $('#searchStringInput').bind('input propertychange', function() {  
    if($("#searchStringInput").val()!=""){
      $("#showIcon1").hide();
      $("#showIcon2").show();
    }else{
      $("#showIcon1").show();
      $("#showIcon2").hide();
    } 
  });  
  $("#searchStringInput").blur(function(){
    setTimeout(function(){
        if($("#searchStringInput").val()==""){
            $("#showIcon1").show();
            $("#showIcon2").hide();
        }else{
            $("#showIcon1").hide();
            $("#showIcon2").show();         
        }           
    },100);
  });  
  $("#searchStringInput").bind('search', function () {
    eval(opts.searchFunc);
});
}
</script>