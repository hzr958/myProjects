<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>成果编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
<script type="text/javascript">
  $(function() {
    //设置radio
    var type = $(".json_book_type").val();
    if (type == "") {
      type = "MONOGRAPH";
      $(".json_book_type").val(type);
    }
    $(".dev_book_type[value='" + type + "']").removeClass("selected-oneself").addClass("selected-oneself_confirm");

    //设置radio
    var inputlist = document.getElementsByClassName("dev-detailinput_container");
    for (var i = 0; i < inputlist.length; i++) {
      inputlist[i].onfocus = function() {
        if (this.closest(".handin_import-content_container-right_input")) {
          this.closest(".handin_import-content_container-right_input").style.border = "1px solid #2882d8";
        }
        if (this.closest(".handin_import-content_rightbox-border")) {
          this.closest(".handin_import-content_rightbox-border").style.border = "1px solid #2882d8";
        }
        if (this.closest(".handin_import-content_container-right_Citation-item")) {
          this.closest(".handin_import-content_container-right_Citation-item").style.border = "1px solid #2882d8";
        }
      }
      inputlist[i].onblur = function() {
        if (this.closest(".handin_import-content_container-right_input")) {
          this.closest(".handin_import-content_container-right_input").style.border = "1px solid #ddd";
        }
        if (this.closest(".handin_import-content_rightbox-border")) {
          this.closest(".handin_import-content_rightbox-border").style.border = "1px solid #ddd";
        }
        if (this.closest(".handin_import-content_container-right_Citation-item")) {
          this.closest(".handin_import-content_container-right_Citation-item").style.border = "1px solid #ddd";
        }
      }
    }
    
 //绑定页数页码oninput事件，设置页数页码的书写规范
    var pageNumInput=$("input[name='showTotalPages']");	
    pageNumInput.bind("input propertychange",function() {
    	  var valueStr=$(pageNumInput).val();
          if( !/^\d+-\d+$/.test(valueStr)){//验证不符合
          	pageNumInput.closest(".handin_import-content_rightbox-border").addClass("error_import-tip_border-warn");
          }else{
    		 pageNumInput.closest(".handin_import-content_rightbox-border").removeClass("error_import-tip_border-warn");
    		}
    	}
    );
  });
</script>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--书籍/专著--%>
    <div class="handin_import-container">
      <!-- 成果头部信息  begin-->
      <%@ include file="pub_head_info.jsp"%>
      <!-- 成果头部信息  end-->
      <div class="handin_import-content_container">
        <!-- 成果类型  begin-->
        <%@ include file="pub_type.jsp"%>
        <!-- 成果类型  end-->
        <!-- 成果全文  begin-->
        <%@ include file="pub_fulltext.jsp"%>
        <!-- 成果全文  end-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <!-- 标题begin -->
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.bookMonographName" />:</span>
          </div>
          <div class="handin_import-content_container-right" style="flex-direction: column; align-items: flex-start;">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title" autocomplete="off"
                title_msg="<spring:message code="pub.enter.bookMonographName"/>" maxlenth="2000" id="title" name="title"
                value="${pubVo.title}">
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
          <!-- 标题end -->
        </div>
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <!-- 丛书名称  begin-->
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span style="color: red;"></span><span><spring:message code="pub.enter.bookseriesName" /></span>
          </div>
          <div class="handin_import-content_container-right"
            style="display: flex; flex-direction: column; align-items: flex-start;">
            <div class="handin_import-content_container-right_input">
              <input type="text" class="dev-detailinput_container  json_book_seriesName" maxlength="200"
                name="seriesName" value="${pubVo.pubTypeInfo.seriesName}">
            </div>
            <div class="json_book_seriesName_msg" style="display: none"></div>
          </div>
          <!-- 丛书名称  end-->
        </div>
        <div class="handin_import-content_container-center">
          <!-- 专著类别  begin-->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.bookType" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 28px 0px 0px;">
              <i class="selected-oneself dev_book_type" value="MONOGRAPH"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.monograph" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
              <i class="selected-oneself dev_book_type" value="TEXTBOOK"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.textbook" /></span>
            </div>
            <div class="handin_import-content_container-right_collect-item" style="margin: 0px 12px;">
              <i class="selected-oneself dev_book_type" value="COMPILE"></i><span
                class="selected-author_confirm-detaile"><spring:message code="pub.enter.compile" /></span>
            </div>
            <input type="hidden" class="json_book_type" name="type" value="${pubVo.pubTypeInfo.type }">
          </div>
          <!-- 专著类别 end-->
        </div>
        <div class="handin_import-content_container-center">
          <!-- ISBN  begin-->
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span>ISBN:</span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input class="dev-detailinput_container full_width json_book_isbn" maxlength="20" type="text"
                  name="isbn" id="isbn" value="${pubVo.pubTypeInfo.ISBN }" />
              </div>
              <div class="json_book_isbn_msg" style="display: none"></div>
            </div>
            <!-- ISBN  begin-->
            <div class="handin_import-content_container-right_sub-area">
              <!-- 编辑  begin-->
              <span class="handin_import-content_container-tip">*</span><span> <spring:message
                  code="pub.enter.bookEditors" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="border: none; flex-direction: column; align-items: flex-start; height: auto; width: 312px;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container full_width json_book_editors" maxlength="50"
                  name="editors" value="${pubVo.pubTypeInfo.editors}" />
              </div>
              <div class="json_book_editors_msg" style="display: none"></div>
            </div>
            <!-- 编辑  end-->
          </div>
        </div>
        <div class="handin_import-content_container-around" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.bookPublishDate" /></span>
          </div>
          <div style="width: 73%; display: flex; flex-direction: column; align-items: flex-start;">
            <!-- 出版日期  begin-->
            <!-- 发表日期  begin-->
            <div class="handin_import-content_container-right_state" style="width: 100%;">
              <div class="handin_import-content_container-right_area" style="width: 43%; margin-left: 12px;">
                <div
                  class="handin_import-content_container-right_area-content input__box dev-detailinput_container-input error_import-tip_border">
                  <input class="json_publishDate dev-detailinput_container" itemevent="callbackDate" type="text"
                    name="publishDate" id="publishDate" readonly unselectable="on" datepicker date-format="yyyy-mm-dd"
                    onfocus="this.blur()" value='<iris:dateFormat dateStr="${pubVo.publishDate }" splitChar="-"/>' />
                </div>
              </div>
              <!-- 发表日期 end-->
              <div class="handin_import-content_container-right_state-sub" style="margin: 20px 0px;">
                <!-- 出版社 begin-->
                <div class="handin_import-content_container-right_state-sub_title" style="width: 36%;">
                  <span class="handin_import-content_container-tip">*</span> <span><spring:message
                      code="pub.enter.bookPublisher" /></span>
                </div>
                <div class="handin_import-content_container-right_area"
                  style="width: 312px; border: none; flex-direction: column; align-items: flex-start; margin-right: 30px;">
                  <div
                    class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                    <input type="text"
                      class="dev-detailinput_container full_width json_book_publisher js_autocompletebox"
                      request-data="PubEdit.getAutoCompleteJson('publisher');" request-url="/psnweb/ac/ajaxgetComplete"
                      maxlength="50" name="publisher" value="${pubVo.pubTypeInfo.publisher}" />
                  </div>
                  <div class="json_book_publisher_msg" style="display: none"></div>
                </div>
                <!-- 出版社 end-->
              </div>
            </div>
            <div class="json_publishDate_msg" style="margin-left: 12px; display: none"></div>
          </div>
        </div>
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span><span><spring:message
                code="pub.enter.countryArea" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center; height: 32px;">
            <!-- 国家或地区  begin-->
            <%@ include file="pub_country_or_area.jsp"%>
            <!-- 国家或地区  end-->
           
          </div>
        </div>
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <!-- 总页码 begin-->
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span style="color: red;"></span><span><spring:message code="pub.enter.bookPage" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="height: auto; border: none; flex-direction: column; align-items: flex-start;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input class="json_book_pageNumber" maxlength="20" type="hidden" name="pageNumber" id="pageNumber"
                  value="${pubVo.pubTypeInfo.pageNumber}" /> <input class="json_book_totalPages" maxlength="20"
                  type="hidden" name="totalPages" id="totalPages" value="${pubVo.pubTypeInfo.totalPages}" /> <input
                  type="text" maxlength="40" class="dev-detailinput_container full_width "
                  placeholder='<spring:message code="pub.enter.pageNumberMsg"/>'
                  title_msg='<spring:message code="pub.enter.pageNumberMsg"/>' name="showTotalPages"
                  value="${pubVo.pubTypeInfo.showTotalPageOrPage}" onBlur="verfyPageNum(this)" /> 
              </div>
            </div>
            <!-- 总页码 end-->
            <!-- 总字数begin-->
            <div class="handin_import-content_container-right_sub-area">
              <span style="color: red;"></span><span><spring:message code="pub.enter.totallWords" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="height: auto; border: none; flex-direction: column; align-items: flex-start;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input type="text" class="dev-detailinput_container full_width json_book_totalWords" maxlength="7"
                  name="totalWords" id="totalWords" value="${pubVo.pubTypeInfo.totalWords}" />
              </div>
              <div class="json_book_totalWords_msg" style="display: none"></div>
            </div>
            <!-- 总字数end-->
          </div>
        </div>
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <!-- 语言  begin-->
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
             <span style="color: red;"></span>
             <span><spring:message code="pub.enter.language" /></span>
          </div>
          <div class="handin_import-content_container-right" style="flex-direction: column; align-items: flex-start;">
            <div class="handin_import-content_container-right_area-content input__box dev-detailinput_container-input input_not-null handin_import-content_rightbox-border" style="width: 80%!important;">
                  <input class="json_book_language dev-detailinput_container" maxlength="20" type="text" name="language"
                    id="language" value="${pubVo.pubTypeInfo.language}" style="padding: 0px 10px;">
                </div>
                <div class="json_book_language_msg" style="display: none"></div>
          </div>
          <!-- 语言 end-->
        </div>
       </div>
        
        
        
        <!-- 成果资助标注  begin-->
        <%@ include file="pub_funding_annotation.jsp"%>
        <!-- 成果资助标注  end-->
        <!-- 成果引用数  begin-->
        <%@ include file="pub_cited_times.jsp"%>
        <!-- 成果引用数  end-->
        <!-- 剩下的摘要、作者等公共部分 begin -->
        <%@ include file="author_and_other_common.jsp"%>
        <!-- 剩下的摘要、作者等公共部分 end -->
      </div>
    </div>
  </form>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
</body>
</html>