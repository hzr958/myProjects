<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>群组编辑</title>
<link type="text/css" rel="stylesheet" href="${resmod}/css/smate.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/plugin/jquery.complete.css" />
<script type="text/javascript" src="${resmod}/js/jquery-browser.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.complete.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.textarea.autoresize.js"></script>
<!-- <style>
* {
	font-size: 14px;
	line-height: 20px;
}
</style> -->
<%-- <script type="text/javascript" src="${resmod}/js/jquery-browser.js"></script> --%>
<!--圆角控制-->
<!--[if lt IE 10]>
<script type="text/javascript" src="../js_v5/js2016/PIE.js"></script>
<![endif]-->
<script type="text/javascript">
var keyword_auto = $.autoword; 
var Group = Group ? Group : {};
Group.create = Group.create ? Group.create : {};
	$(document).ready(function() {  
		if($("input[name='groupName']").val()!=''){
			$("input[name='groupName']").addClass("active");
			var groupName=$("input[name='groupName']").val();
			if(groupName.length>200){
				groupName=groupName.substring(0,200);
			}
			$("input[name='groupName']").val(groupName);
		}
		
		if($("input[name='groupDescription']").val()!=''){
			$("#introduce").addClass("active");
			var groupDescription=$("input[name='groupDescription']").val();
			if(groupDescription.length>4000){
				groupDescription=groupDescription.substring(0,4000);
			}
			
			$("#introduce").val(groupDescription);
		}
		//判断是开放权限，选中对应的
		
		if($("input[name='openType']").val()=='O'){
			$("#test1").attr("checked","checked");
		}
		if($("input[name='openType']").val()=='H'){
			$("#test2").attr("checked","checked");
		}
		if($("input[name='openType']").val()=='P'){
			$("#test3").attr("checked","checked");
		}
		
		//关键字自动完成加载插件.
	   $("#auto_keywords_outer_div_1").autoword({
	    	"words_max":5,
	    	"select": $.smate.auto["keyword"],
		    "watermark_flag":true,
		    "watermark_tip":"关键词"
		    //"global" : false
	    });
	    var keyDiv = $.autoword["auto_keywords_outer_div_1"];	
	    
	    if($("input[name='keyWords1']").val()!=''){
	    	var chArr=new Array();
		    chArr=$("input[name='keyWords1']").val().split(";");
	    	 if(chArr.length>0){
         		for (var i=0; i<chArr.length;i++){
        			keyword_auto["auto_keywords_outer_div_1"].put("",chArr[i]);//往zh_auto_disckey_outer_div_1添加单词
         		}
     	   	}
		};
	    $("input[name='groupName']").on('input onpropertychange',function(){
	    	if($("input[name='groupName']").val().length<200||$("input[name='groupName']").val().length==200){
				$("#groupNameCounter").css("display","none");
				}
			if($("input[name='groupName']").val().length>200){
				$("#groupNameCounter").css("display","block");
				$("#groupNameCounter").attr("invalid_msg","群组名最多输入200字符");
				$("#groupNameCounter").attr("valid_msg","群组名最多输入200字符");
				}
			
			} );
		
		$("#introduce").on('keyup keydown',function(){
			if($("#introduce").val().length<4000 ||$("#introduce").val().length==4000){
				$("#groupDescriptionCounter").css("display","none");
				}
			
			if($("#introduce").val().length>4000){
				$("#groupDescriptionCounter").css("display","block");
				$("#groupDescriptionCounter").attr("invalid_msg","群组介绍最多输入4000字符");
				$("#groupDescriptionCounter").attr("valid_msg","群组介绍最多输入4000字符");
				}
			} );
			
			
			//选择学科领域
		$(".selector_dropdown_hint").click(function(){
			
			$(this).nextAll(".selector_dropdown_collections").addClass("shown");
			$(this).nextAll(".selector_dropdown_collections").css("display","block");
	    	
	    });
		$(".selector_dropdown_icon").click(function(){
			$(this).prev().nextAll(".selector_dropdown_collections").addClass("shown");
			$(this).prev().nextAll(".selector_dropdown_collections").css("display","block");
	    	
	    });
		
		$(".selector_dropdown_option").mouseover(function(){
			$(this).addClass("hover");
		});
		
		$(".selector_dropdown_collections .selector_dropdown_option").mouseout(function(){
			$(this).removeClass("hover");
		});
		
		if($("input[name='topCategoryId']").val()!=''){
            var topCategoryId=$("input[name='topCategoryId']").val();
			$.ajax({
				url :'/groupweb/creategroup/ajaxGetSecondDiscipline',
				type : 'post',
				dataType:'html',
				data : {"topCategoryId":topCategoryId},
				success : function(data) {
					
					var toConfirm=false;
					if(data!=null){
						toConfirm=data=="{\"ajaxSessionTimeOut\":\"yes\"}"?true:toConfirm;
					}
					if(toConfirm){
						jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
							if (r) {
								document.location.href=window.location.href;
								return;
							}
						});
					}else{
						if(typeof data!='undefined'&&data){
							$("#secondCategory").after(data);
							$(".selector_dropdown_single").css("display","block");
							var secondCategory=$("input[name='secondCategoryName']").val();
							var topCategoryName=$("input[name='topCategoryName']").val();
							$("#secondCategoryShown").nextAll(".selector_dropdown_collections").removeClass("shown");
							$("#secondCategoryShown").text(secondCategory);
							$("#firstCategoryShown").text(topCategoryName);
							}
							$("#secondCategory").click(function(){
								$(this).nextAll(".selector_dropdown_collections").addClass("shown");
								$(this).nextAll(".selector_dropdown_collections").css("display","block");
						    	
						    });
					}
				},
				error: function(){
					$.scmtips.show('error',"失败");
				}
			});
			
		}
		
		$(".selector_dropdown_option").click(function(){
			var topCategoryId=$(this).attr("value");
			var categoryValue=$(this).text();
			$("#firstCategoryShown").text(categoryValue);
			$("#secondCategoryShown").text("选择二级研究领域");
			$(this).parent().removeClass("shown");
			$(this).parent().css("display","none");
			$.ajax({
				url :'/groupweb/creategroup/ajaxGetSecondDiscipline',
				type : 'post',
				dataType:'html',
				data : {"topCategoryId":topCategoryId},
				success : function(data) {
					var toConfirm=false;
					if(data!=null){
						toConfirm=data=="{\"ajaxSessionTimeOut\":\"yes\"}"?true:toConfirm;
					}
					if(toConfirm){
						jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
							if (r) {
								document.location.href=window.location.href;
								return;
							}
						});
					}else{
						if(typeof data!='undefined'&&data){
							$(".selector_dropdown_single").css("display","block");
						    $("#secondCategory").parent().find(".selector_dropdown_collections").remove();
							$("#secondCategory").after(data);
							$("#secondCategory").nextAll(".selector_dropdown_collections").css("display","block");
							}
					}
				
				},
				error: function(){
					$.scmtips.show('error',"失败");
				}
			});
		});
	});
//引用沈幸嘉的
var input_selector = 'input, textarea';

$(document).on('focus', input_selector, function () {
	      $(this).addClass('active');
	});

$(document).on('change', input_selector, function () {
    if($(this).val().length !== 0 || $(this).attr('placeholder') !== undefined) {
      $(this).addClass('active');
    }
  });

$(document).on('blur', input_selector, function () {
    if ($(this).val().length === 0 && $(this).attr('placeholder') === undefined) {
      $(this).removeClass('active');
    }
  });
  
//Text Counter
(function($){ 
	  $.fn.characterCounter = function(){
		  return this.each(function(){	  
			  var $input=$(this);
			  var $inputCounterDiv = $input.parent().find('div[class="input_character_counter persistent"]');
			  var $hasLengthLimitation = $input.attr('length') !== undefined;
			  if($hasLengthLimitation){
				  $input.on('input', updateCounter);
				  $input.on('focus', updateCounter);
				  $input.on('change', updateCounter);
				} 
		  });
	  };

	  function updateCounter(){
		  var maxLength = $(this).attr('length');
		  var presentLength = $(this).val().length;
		  var isValidLength = presentLength <= maxLength;
		  $(this).parent().find('div[class="input_character_counter persistent"]').html( presentLength + '/' + maxLength);
	      validateInputLength(isValidLength, $(this));
	  };
	  
	  function validateInputLength(isValidLength, $input){
		  var $hasLengthLimitation = $input.attr('length') !== undefined;
		  if($hasLengthLimitation && !isValidLength){
			  $input.addClass('invalid');
			  $input.removeClass('valid');
		  }
		  else if($hasLengthLimitation && isValidLength){
			  $input.removeClass('invalid');
		  }
	  }; 
	  
	  $(document).ready(function(){
		  $('input,textarea').characterCounter();
	 });	
})(jQuery);  
  
var deleteGroupCfm="<s:text name='page.group.confirm.deleteGroup'/>";
var deleteUnionGroupCfm="<s:text name='page.group.confirm.deleteUionGroup'/>";
var reminder = "<s:text name='group.tip.reminder' />";
	Group.del = function(des3GroupId , groupNodeId){
		if("1" == $("#groupHasUnion").val() ){//表示关联群组
			deleteGroupCfm =  deleteUnionGroupCfm ;
		}
		jConfirm(deleteGroupCfm, reminder, function(sure){
		    if (sure) {
		        $.proceeding.show();
		        $.ajax({
		            url: ctxpath + '/group/ajaxGroupDelete',
		            type: 'post',
		            dataType: 'json',
		            data: {
		                "groupPsn.des3GroupId": des3GroupId,
		                "groupPsn.groupNodeId": groupNodeId
		            },
		            success: function(data){
		                $.proceeding.hide();
		                
		                toConfirm=data.ajaxSessionTimeOut;
		    			if(toConfirm){
		    				jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
		    					if (r) {
		    						document.location.href = window.location.href ;
		    						return;
		    					}
		    				});
		    			}
		                if("success"==data.result){
		                	 $.scmtips.show(data.result, data.msg);
		                	 setTimeout(function(){
				                    window.location.href = ctxpath +"/group/main";
				                }, 2000);
		                }
		            },
		            error: function(){
		                $.proceeding.hide();
		                $.scmtips.show("error", Group.myGroup.opFaild);
		            }
		        });
		    }
		});
	}
	
	
  
</script>
</head>
<body class="grey_bg">
  <input type="hidden" id="groupHasUnion" value="${form.hasUnion}">
  <div class="content-1200">
    <div class="group_box">
      <div class="edit_l fl">
        <ul class="edit_lt">
          <li class="hover"><a style="cursor: default;" href="javascript:void(0)">基本信息</a></li>
          <c:if test="${form.currentPsnGroupRoleStatus == 4 }">
            <li><a href="javascript:;" onclick="Group.del('${form.des3GroupId}' ,'1')" style="color: red;">删除群组</a></li>
          </c:if>
          <!-- <li><a href="#">权限管理</a></li>
        <li><a href="#">消息设置</a></li>
        <li><a href="#">权限设置</a></li> -->
        </ul>
      </div>
      <div class="edit_r fr">
        <div class="group_infro">
          <form class="col s12" id="GroupPsnForm" method='post'>
            <input type="hidden" name="groupCategory" value="${form.groupCategory}"> <input type="hidden"
              name="keyWords1" value="${form.keyWords1}"> <input type="hidden" name="openType"
              value="${form.openType}"> <input type="hidden" name="des3GroupId" value="${form.des3GroupId}">
            <input type="hidden" name="groupImgUrl" value="${form.groupImgUrl}"> <input type="hidden"
              name="disciplines" value="${form.discipline1}"> <input type="hidden" name="topCategoryName"
              value="${topCategoryName}"> <input type="hidden" name="topCategoryId" value="${topCategoryId}">
            <input type="hidden" name="secondCategoryName" value="${secondCategoryName}"> <input type="hidden"
              name="groupDescription" value="${form.groupDescription}"> <input type="hidden"
              name="originGroupName" value="${form.groupName}">
            <div class="form_row" style="margin-bottom: 20px;">
              <div class="input_field_titled">
                <input id="title" name="groupName" type="text" class="validate input_field normal"
                  value="${form.groupName}" length="200">
                <div class="input_title">名称</div>
                <div class="input_field_underline"></div>
                <div class="input_field_active_underline"></div>
                <div class="input_helper persistent" normal_msg="" id="groupNameCounter" invalid_msg="" valid_msg=""
                  style="display: none"></div>
                <div class="input_character_counter persistent"></div>
              </div>
            </div>
            <div class="form_row" style="margin-bottom: 20px;">
              <div class="input_field_titled">
                <textarea id="introduce" name="groupDescription1"
                  class="validate input_field normal textarea_autoresize" length="4000"></textarea>
                <div class="input_title">群组简介（选填）</div>
                <div class="input_field_underline"></div>
                <div class="input_field_active_underline"></div>
                <div class="input_helper persistent" normal_msg="" id="groupDescriptionCounter" invalid_msg=""
                  valid_msg="" style="display: none"></div>
                <div class="input_character_counter persistent"></div>
              </div>
            </div>
            <div class="form_row">
              <div class="selectors">
                <div class="selectors_title">研究领域</div>
                <div class="selector_dropdown_content">
                  <div class="selector_dropdown_single underline" style="width: 200px;">
                    <div class="selector_dropdown_hint" id="firstCategoryShown" style="cursor: pointer;">选择一个研究领域</div>
                    <div class="selector_dropdown_icon" style="cursor: pointer;"></div>
                    <div class="selector_dropdown_value">
                      <c:forEach var="topDiscipline" items="${topDisciplineList}">
                        <div class="selector_dropdown_option" value="${topDiscipline.topCategoryId}">${topDiscipline.topCategoryZhName }</div>
                      </c:forEach>
                    </div>
                    <div class="selector_dropdown_collections" style="display: none;">
                      <c:forEach var="topDiscipline" items="${topDisciplineList}">
                        <div class="selector_dropdown_option" value="${topDiscipline.topCategoryId}">${topDiscipline.topCategoryZhName }</div>
                      </c:forEach>
                    </div>
                  </div>
                  <div class="selector_dropdown_single underline" style="width: 200px; margin-left: 50px; display: none">
                    <div class="selector_dropdown_hint" id="secondCategoryShown" style="cursor: pointer;">选择二级研究领域</div>
                    <div class="selector_dropdown_icon" id="secondCategory" style="cursor: pointer;"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="clear" style="height: 40px;"></div>
            <div class="row" style="margin-bottom: 20px;">
              <div class="auto_word_outer_div" id="auto_keywords_outer_div_1" style="width: 665px;"></div>
              <div class="prompt_wrap">使用回车将多个关键词分开</div>
            </div>
            <h3 class="set_permi">选择开放权限</h3>
            <ul class="radio-permi">
              <li>
                <p>
                  <input class="with-gap" name="type" value="O" type="radio" id="test1" /> <label for="test1">开放</label>
                  <span>任何人不需要申请即可加入及上传文件</span>
                </p>
              </li>
              <li>
                <p>
                  <input class="with-gap" name="type" value="H" type="radio" id="test2" /> <label for="test2">半开放</label>
                  <span>申请加入及上传文件需通过审核</span>
                </p>
              </li>
              <li>
                <p>
                  <input class="with-gap" name="type" value="P" type="radio" id="test3" /> <label for="test3">隐私</label>
                  <span>其他人员无法看到群组，需邀请才能加入</span>
                </p>
              </li>
            </ul>
            <div class="group-btn">
              <a class="waves-effect waves-light button01 w86" onclick="Group.saveEditGroup();">确认</a>
            </div>
          </form>
        </div>
      </div>
      <div class="clear_h40"></div>
    </div>
  </div>
</body>
</html>
