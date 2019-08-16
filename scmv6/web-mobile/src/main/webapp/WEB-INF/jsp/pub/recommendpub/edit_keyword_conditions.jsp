<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript">
        window.onload=function(){
            document.getElementsByClassName("provision_container-body")[0].style.height = window.innerheight - 96 + "px"; 
            $(".dev_key_item>.material-icons, .dev_add_item>.material-icons").click(function(){
            	$(this).closest(".provision_container-body_keyword-item").remove();
            	 $("#keyWordsum").text(parseInt($("#keyWordsum").text()) -1);
            });
            addFormElementsEvents($(".dev_input_search")[0]);
            bindSelectStatus();
        }
        
          //绑定点击关键词事件
          function bindSelectStatus(){
            $(".provision_container-body_keyword-item_list.dev_word").bind("click",function(){
              $(".provision_container-body_keyword-item.dev_key_item").each(function(){
                $(this).css("background","");
              });
              $(this).parent().css("background","#D9D9D9");
            });
          }
          
        function addKeyWord(){
          var keyWordStr = $.trim($("#serchKeyInput").val());
          if(keyWordStr != ""){
              var repeat = false;
              $(".dev_word").each(function(){
                  var word = $.trim($(this).attr("value"));
                  if(word.toUpperCase( ) == keyWordStr.toUpperCase( )){//重复就不添加了
                      $("#serchKeyInput").val("");
                      $("#serchKeyInput").focus();
                      repeat = true;
                      scmpublictoast("关键词重复",1000,3);
                  }
              });
              if($(".dev_key_item").length < 10 && !repeat){
                  var node = $(".dev_add_item").clone(true);
                  $(node).removeClass("dev_add_item").addClass("dev_key_item")
                  $(node).find(".dev_word").attr("value",keyWordStr).text(keyWordStr);
                  if($(".dev_key_item").length>0){
                      $(".dev_key_item:first").before(node);
                  }else{
                      $(".dev_add_item").before(node);                        
                  }
                  $(node).show();
                  $("#serchKeyInput").val("");
                  $("#serchKeyInput").focus();
                  $("#keyWordsum").text(parseInt($("#keyWordsum").text()) + 1);
              }else if($(".dev_key_item").length >= 10 ){
                  scmpublictoast("最多输入10个关键词",2000,3);
              }
          }else{
               scmpublictoast("请输入关键词",2000,3);
          }
        } 
 
        //返回
        function goback(){
        	$("#defultKeyJson").val("");
            $("#pub_search").submit();
        };
        
        function saveKeyWord(){
        	var keyword = [];
        	$(".dev_word").each(function(){
        		var word = $.trim($(this).attr("value"));
        		if(word!="" && keyword.indexOf(word)<0){
	        		keyword.push(word);        			
        		}
        	});
        	keyword = keyword.reverse(); 
        	var jsonKeyword = JSON.stringify(keyword); 
        	$.ajax({
        		url: "/pub/mobile/ajaxsavepsnkeywords",
        		data: {"defultKeyJson":jsonKeyword},
        		type: "post",
        		dataType: "json",
        		success: function(data){
                    if(data.result == "success"){
                        goback();
                    }else{
                          scmpublictoast("添加关键词出错",2000,2);
                    }
        		},
        		error: function(){
        			
        		}
        	});
        }
        
        function callbackKey(){
        	addKeyWord();
        }
    </script>
</head>
<body>
  <form id="pub_search" method="post" action="/pub/mobile/ajaxconditions">
    <input type="hidden" id="defultArea" name="defultArea" value="${pubVO.defultArea}" /> <input type="hidden"
      id="defultKeyJson" name="defultKeyJson" value='<c:out value="${pubVO.defultKeyJson}"/>' /> <input type="hidden"
      id="searchArea" name="searchArea" value="${pubVO.searchArea}" /> <input type="hidden" id="searchPsnKey"
      name="searchPsnKey" value='<c:out value="${pubVO.searchPsnKey}"/>' /> <input type="hidden" id="searchPubYear"
      name="searchPubYear" value="${pubVO.searchPubYear}" /> <input type="hidden" id="searchPubType"
      name="searchPubType" value="${pubVO.searchPubType}" />
  </form>
  <div class="provision_container">
    <div class="provision_container-title">
      <a href="javascript:goback();"><i class="material-icons">keyboard_arrow_left</i></a> <span>设置关键词</span> <i></i>
    </div>
    <div class="provision_container-body" style="margin-top:31px;">
      <div class="provision_container-body_keyword-input dev_input_search">
        <input item-event="callbackKey()" contenteditable="true"
          request-url="/psnweb/recommend/ajaxautoconstkeydiscscodeid" id="serchKeyInput" type="text"
          class="provision_container-body_keyword-input_box js_autocompletebox" maxlength="25" placeholder="输入需要添加的关键词">
        <div style="width: 100%;font-size: 17px;color: #cbcbcb;"> 已输入<span id="keyWordsum"><c:out value="${fn:length(pubVO.keyList)}"></c:out></span>个关键词</div>
      </div>
      <div class="provision_container-body_keyword-add">
        <div class="provision_container-body_keyword-addbtn" onclick="addKeyWord();">添加</div>
      </div>
      <div style="width: 95%; height: auto;">
        <c:forEach items="${pubVO.keyList }" var="item">
          <div class="provision_container-body_keyword-item dev_key_item">
            <div class="provision_container-body_keyword-item_list dev_word" style="margin-left: 11px;" value='<c:out value=" ${item.keyWords }"/>'>
              <c:out value=" ${item.keyWords }" />
            </div>
            <i class="material-icons" style="margin-right: 11px;">clear</i>
          </div>
        </c:forEach>
        <div class="provision_container-body_keyword-item dev_add_item" style="display: none">
          <div class="provision_container-body_keyword-item_list dev_word"></div>
          <i class="material-icons">clear</i>
        </div> 
      </div>
    </div>
    <div class="provision_container-footer" onclick="saveKeyWord();">确定</div>
  </div>
</body>
</html>