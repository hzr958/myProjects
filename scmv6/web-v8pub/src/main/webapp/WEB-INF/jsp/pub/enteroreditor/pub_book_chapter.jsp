<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>成果编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="pub_head_res.jsp"%>
</head>
<body>
  <form id="enterPubForm" method="post">
    <%--书籍章节--%>
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
        <!-- 标题begin -->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.title" />:</span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container json_title" maxlenth="2000" id="title" autocomplete="off"
                title_msg="<spring:message code="pub.enter.title"/>" name="title" value="${pubVo.title}">
            </div>
            <div class="json_title_msg" style="display: none"></div>
          </div>
        </div>
        <!-- 标题end -->
        <!-- 书名begin -->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
          <div class="handin_import-content_container-left" style="margin-top: 6px;">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.bookName" /></span>
          </div>
          <div class="handin_import-content_container-right">
            <div class="handin_import-content_container-right_input error_import-tip_border">
              <input type="text" class="dev-detailinput_container  json_bookChapter_name" maxlength="200" id="name" autocomplete="off"
                name="name" value="${pubVo.pubTypeInfo.name}">
            </div>
            <div class="json_bookChapter_name_msg"></div>
          </div>
        </div>
        <!-- 书名end -->
        <div class="handin_import-content_container-center">
          <!-- ISBNbegin -->
          <div class="handin_import-content_container-left">
            <span style="color: red;"></span><span>ISBN:</span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center;">
            <div class="handin_import-content_container-right_area"
              style="height: auto; border: none; flex-direction: column; align-items: flex-start;">
              <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border">
                <input class="dev-detailinput_container full_width json_bookChapter_isbn" maxlength="20" type="text"
                  name="isbn" id="isbn" value="${pubVo.pubTypeInfo.ISBN }" />
              </div>
              <div class="json_bookChapter_isbn_msg" style="display: none"></div>
            </div>
            <!-- ISBNend -->
            <!-- 开始结束页码begin -->
            <div class="handin_import-content_container-right_sub-area">
              <span class="handin_import-content_container-tip"></span> <span><spring:message
                  code="pub.enter.startPageAndEndPage" /></span>
            </div>
            <div class="handin_import-content_container-right_area"
              style="height: auto; border: none; flex-direction: column; align-items: flex-start;">
              <div
                class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                <input type="text" class="dev-detailinput_container full_width json_bookChapter_pageNumber"
                  maxlength="17" name="" placeholder='<spring:message code="pub.enter.pageNumberMsg"/>'
                  title='<spring:message code="pub.enter.pageNumberMsg"/>' value="${pubVo.pubTypeInfo.pageNumber}">
              </div>
            </div>
            <!-- 开始结束页码 end-->
          </div>
        </div>
        <div class="handin_import-content_container-around">
          <!-- 出版日期  begin-->
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.bookPublishDate" /></span>
          </div>
          <div class="handin_import-content_container-right_state">
            <div class="handin_import-content_container-right_area"
              style="width: 48%; margin-left: 11px; height: auto; border: none; flex-direction: column; align-items: flex-start;">
              <div
                class="handin_import-content_container-right_area-content input__box  dev-detailinput_container-input handin_import-content_rightbox-border error_import-tip_border">
                <input class="json_publishDate dev-detailinput_container" itemevent="callbackDate" type="text"
                  name="publishDate" id="publishDate" onfocus="this.blur()" unselectable="on" readonly datepicker
                  date-format="yyyy-mm-dd" value='<iris:dateFormat dateStr="${pubVo.publishDate }" splitChar="-"/>' />
              </div>
              <div class="json_publishDate_msg" style="display: none"></div>
            </div>
            <!-- 出版日期  end-->
            <div class="handin_import-content_container-right_state-sub" style="align-items: flex-start;">
              <!-- 编辑  begin-->
              <div class="handin_import-content_container-right_state-sub_title"
                style="width: 47%; margin-right: 8px; margin-top: 6px;">
                <span class="handin_import-content_container-tip">*</span> <span><spring:message
                    code="pub.enter.bookEditors" /></span>
              </div>
              <div class="handin_import-content_container-right_area"
                style="width: 85%; height: auto; margin-right: 2px; border: none; flex-direction: column; align-items: flex-start;">
                <div
                  class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border">
                  <input type="text" class="dev-detailinput_container full_width json_bookChapter_editors"
                    maxlength="50" name="editors" value="${pubVo.pubTypeInfo.editors}" />
                </div>
                <div class="json_bookChapter_editors_msg" style="display: none"></div>
              </div>
              <!-- 编辑end-->
            </div>
          </div>
        </div>
        <div class="handin_import-content_container-center">
          <div class="handin_import-content_container-left">
            <span class="handin_import-content_container-tip">*</span> <span><spring:message
                code="pub.enter.countryArea" /></span>
          </div>
          <div class="handin_import-content_container-right" style="display: flex; align-items: center; height: 32px;">
            <!-- 国家或地区  begin-->
            <%@ include file="pub_country_or_area.jsp"%>
            <!-- 国家或地区  end-->
        
          </div>
        </div>
        
        
        <!-- 出版社 begin-->
        <div class="handin_import-content_container-center" style="align-items: flex-start;">
             <div class="handin_import-content_container-left" style="margin-top: 6px;">
                <span class="handin_import-content_container-tip">*</span> 
                <span><spring:message code="pub.enter.bookPublisher" /></span>
             </div>
             <div class="handin_import-content_container-right" >
                <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border error_import-tip_border" style="width: 80%!important;">
                  <input type="text"
                    class="dev-detailinput_container full_width json_bookChapter_publisher js_autocompletebox"
                    request-data="PubEdit.getAutoCompleteJson('publisher');" request-url="/psnweb/ac/ajaxgetComplete"
                    maxlength="50" name="publisher" value="${pubVo.pubTypeInfo.publisher}" />
                </div>
                <div class="json_bookChapter_publisher_msg" style="display: none"></div>
              </div>
        </div>
        <!-- 出版社end-->
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