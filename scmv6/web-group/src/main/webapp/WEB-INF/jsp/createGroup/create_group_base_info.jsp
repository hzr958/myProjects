<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/smate.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/plugin/jquery.complete.css" />
<script type="text/javascript" src="${resmod}/js/jquery-browser.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.complete.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${ressns}/js/group/group.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.textarea.autoresize.js"></script>
<!--[if lt IE 10]>
<script type="text/javascript" src="../js_v5/js2016/PIE.js"></script>
<![endif]-->
<script src="${resscmsns}/js_v5/js2016/scrollBar.js" type="text/javascript"></script>
<script type="text/javascript">
var keyword_auto = $.autoword;
var Group = Group ? Group : {};
Group.create = Group.create ? Group.create : {};
	$(document).ready(function() {  
		if($("input[name='step']").val()=='2'){
			Group.nextCreateStep('11');
		}
		
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
							var obj = $("#secondCategoryShown").parent();
							obj.empty();
							$(".selector_dropdown_single").css("display","block");
							obj.prepend("<div class='selector_dropdown_hint' id='secondCategoryShown'>选择二级研究领域</div>" +
							"<div class='selector_dropdown_icon' id='secondCategory'></div>");
							$("#secondCategory").after(data);
							$("#secondCategoryShown").click(function(){
								$(this).nextAll(".selector_dropdown_collections").addClass("shown");
								$(this).nextAll(".selector_dropdown_collections").css("display","block");
						    	
						    });
							$("#secondCategory").nextAll(".selector_dropdown_collections").css("display","block");
							$("#secondCategory").click(function(){
								$(this).nextAll(".selector_dropdown_collections").addClass("shown");
								$(this).nextAll(".selector_dropdown_collections").css("display","block");
						    	
						    });
						
						}
					}
					
				},
				error: function(){
					$.scmtips.show('error',"失败");
				}
			});
		});
		//关键字自动完成加载插件.
	    $("#auto_keywords_outer_div_1").autoword({
	    	"words_max":5,
	    	"select": $.smate.auto["keyword"],
		    "watermark_flag":true,
		    "watermark_tip":"关键词",
		    "global" : false
	    });
	    var keyDiv = $.autoword["auto_keywords_outer_div_1"];	
	    $("#groupNameCounter").css("display","none");
	    $("#groupDescriptionCounter").css("display","none");
	    
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
			if($("#introduce").text().length<4000||$("#introduce").val().length==4000){
				$("#groupDescriptionCounter").css("display","none");
				}
			
			if($("#introduce").val().length>4000){
				$("#groupDescriptionCounter").css("display","block");
				$("#groupDescriptionCounter").attr("invalid_msg","群组介绍最多输入4000字符");
				$("#groupDescriptionCounter").attr("valid_msg","群组介绍最多输入4000字符");

				}
			} );
		
		
		
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
	 
</script>
<div class="grey_bg">
  <div class="white_content">
    <div class="group_step">
      <div class="step_blue loca01">
        <i class="step_blue_icon" style="font-size: 14px;">1</i>
        <p>选择群组类型</p>
      </div>
      <div class="step_line_grey"></div>
      <div class="step_grey loca02" style="font-size: 14px;">
        <i class="step_grey_icon">2</i>
        <p>填写群组信息</p>
      </div>
    </div>
    <div class="group_type" style="display: block">
      <div class="group_one fl">
        <a href="#" onclick="Group.nextCreateStep('10')"> <i class="inst_icon"></i>
          <h2>兴趣群组</h2>
          <p>与同行交流，探索领域热点</p>
        </a>
      </div>
      <div class="group_one fr">
        <a href="#" onclick="Group.nextCreateStep('11')"> <i class="proj_group"></i>
          <h2>项目群组</h2>
          <p>辅助申请与结题，推广项目成果</p>
        </a>
      </div>
    </div>
    <div class="group_infro" style="display: none;">
      <form class="col s12" id="GroupPsnForm" method='post'>
        <input type="hidden" name="groupCategory" value="${groupCategory}"> <input type="hidden"
          name="des3PrjId" value="${des3Id}"> <input type="hidden" name="step" value="${step}"> <input
          type="hidden" name="keyWords1" value=""> <input type="hidden" name="disciplines" value="">
        <div class="form_row" style="margin-bottom: 20px;">
          <div class="input_field_titled">
            <input id="title" name="groupName" type="text" class="validate input_field normal" value="${groupName}"
              length="200">
            <div class="input_title">名称</div>
            <div class="input_field_underline"></div>
            <div class="input_field_active_underline"></div>
            <div class="input_helper persistent" normal_msg="" id="groupNameCounter" invalid_msg="" valid_msg=""></div>
            <div class="input_character_counter persistent"></div>
          </div>
        </div>
        <div class="form_row" style="margin-bottom: 20px;">
          <div class="input_field_titled">
            <textarea id="introduce" name="groupDescription" class="validate input_field normal textarea_autoresize"
              length="4000"></textarea>
            <div class="input_title">群组简介（选填）</div>
            <div class="input_field_underline"></div>
            <div class="input_field_active_underline"></div>
            <div class="input_helper persistent" normal_msg="" id="groupDescriptionCounter" invalid_msg="" valid_msg=""></div>
            <div class="input_character_counter persistent"></div>
          </div>
        </div>
        <div class="form_row">
          <div class="selectors">
            <div class="selectors_title">研究领域</div>
            <div class="selector_dropdown_content">
              <div class="selector_dropdown_single underline" style="width: 200px; cursor: pointer;">
                <div class="selector_dropdown_hint" id="firstCategoryShown">选择一个研究领域</div>
                <div class="selector_dropdown_icon"></div>
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
              <div class="selector_dropdown_single underline"
                style="width: 200px; margin-left: 50px; discipline: none; display: none; cursor: pointer;">
                <div class="selector_dropdown_hint" id="secondCategoryShown">选择二级研究领域</div>
                <div class="selector_dropdown_icon" id="secondCategory"></div>
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
              <input class="with-gap" name="openType" value="O" type="radio" id="test1" /> <label for="test1">开放</label>
              <span>任何人不需要申请即可加入及上传文件</span>
            </p>
          </li>
          <li>
            <p>
              <input class="with-gap" name="openType" value="H" type="radio" id="test2" /> <label for="test2">半开放</label>
              <span>申请加入及上传文件需通过审核</span>
            </p>
          </li>
          <li>
            <p>
              <input class="with-gap" name="openType" value="P" type="radio" id="test3" /> <label for="test3">隐私</label>
              <span>其他人员无法看到群组，需邀请才能加入</span>
            </p>
          </li>
        </ul>
        <div class="group-btn">
          <a href="javascript:;" class="waves-effect waves-light button01 w86" id="groupSaveBtn"
            onclick="Group.saveCreateGroup();">确认</a>
          <c:if test="${isGroupPrj!=1}">
            <a href="javascript:;" onclick="Group.goBack()" class="waves-effect waves-teal button02 w98">返回上一步</a>
          </c:if>
          <!-- <a href="/scmwebsns/project/maint?menuId=1400" class="waves-effect waves-teal button02 w98">返回上一步</a> -->
        </div>
      </form>
    </div>
  </div>
</div>
