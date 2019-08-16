<!-- 成果、全文认领 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    var boxlist = document.getElementsByClassName("dev_lookall_pubconfirm");
    window.onresize = function(){
        for(var i = 0; i < boxlist.length; i++){
            var allheight = document.body.clientheight;
            var allwidth = document.body.offsetWidth;
            var selfheight = boxlist[i].offsetHeight;
            var selfwidth = boxlist[i].offsetWidth;
            boxlist[i].style.top = (allheight - selfheight)/2 + "px";
            boxlist[i].style.left = (allwidth - selfwidth)/2 + "px";
        }
    }
    document.onkeydown = function(event){
        if(event.keyCode == 27){
            event.stopPropagation();
            event.preventDefault();
            if(document.getElementsByClassName("cover_colored")){
                Pub.closePubftconfirmBack();
            }
        }
    }
})
</script>
<div class="dev_pub_similar-results" id="id_repeat_pub_title" style="display: none;">
  <span id="id_repeat_pub_msg" class="dev_pub_similar-results_tip"></span> <span id="id_repeat_pub_dealwith"
    class="dev_pub_similar-results_detail" onclick="RepeatPub.showrepeatpubUI(event)">立即处理</span>
</div>
<div class="dev_pub_confirm"></div>
<!-- 成果认领================ -->
<div class="dev_fulltext_rcmd"></div>
<!-- 全文认领============== -->
<!-- =================================成果认领查看全部start=================================== -->
<div class="dialogs__box dev_lookall_pubconfirm" style="width: 720px;" dialog-id="dev_lookall_pubconfirm_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="max-height: 56px; height: 56px;">
      <div class="nav_horiz">
        <div class="nav__list">
          <div class="dialogs__header_title nav__item item_selected dev_item_pubconfirm" onclick="Pub.toConfirmList();"
            style="font-size: 16px!important;">
            <spring:message code="pub.confrim" />
          </div>
          <div class="dialogs__header_title nav__item dev_item_pubfulltext" onclick="Pub.toFulltextList();"
            style="font-size: 16px!important; margin-left: 20px;">
            <spring:message code="pub.fulltext.claim" />
          </div>
        </div>
        <div class="nav__underline setting-list_page-item_hidden"></div>
      </div>
      <div class="pub-idx__main_add dev_lookall_pubconfirm_back_add_tip" style="position: absolute; right: 86px;">
        <i class="material-icons pub-idx__main_add-tip">check_box</i> <span class="pub-idx__main_add-detail"> <c:if
            test="${locale=='en_US' }">Send friend invitation to my co-authors</c:if> <c:if test="${locale=='zh_CN' }">邀请我的合作者成为联系人</c:if>
        </span>
      </div>
      <i class="list-results_close" onclick="Pub.closePubConfirmBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_confirmList" list-main="pubconfirm"></div>
    <div class="main-list__list item_no-border dev_fulltextList" list-main="pubftconfirm"></div>
  </div>
</div>
<!-- =================================成果认领查看全部end=================================== -->
<!-- =================================全文认领查看全部start=================================== -->
<div class="dialogs__box dev_lookall_pubconfirm" style="width: 720px;" dialog-id="dev_lookall_pubftconfirm_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="nav_horiz">
        <div class="nav__list">
          <div class="dialogs__header_title nav__item item_selected dev_item_pubfulltext1"
            onclick="Pub.toFulltextList1();" style="min-height: 18px; font-size: 16px;">
            <spring:message code="pub.fulltext.claim" />
          </div>
          <div class="dialogs__header_title nav__item dev_item_pubconfirm1" onclick="Pub.toConfirmList1();"
            style="min-height: 18px; font-size: 16px; margin-left: 20px;">
            <spring:message code="pub.confrim" />
          </div>
        </div>
        <div class="nav__underline  setting-list_page-item_hidden"></div>
      </div>
      <div class="pub-idx__main_add dev_lookall_pubftconfirm_back_add_tip" style="position: absolute; right: 86px;">
        <i class="material-icons pub-idx__main_add-tip">check_box</i> <span class="pub-idx__main_add-detail"> <c:if
            test="${locale=='en_US' }">Send friend invitation to my co-authors</c:if> <c:if test="${locale=='zh_CN' }">邀请我的合作者成为联系人</c:if>
        </span>
      </div>
      <i class="list-results_close" onclick="Pub.closePubftconfirmBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_confirmList1" list-main="pubconfirm1"></div>
    <div class="main-list__list item_no-border dev_fulltextList1" list-main="pubftconfirm1"></div>
  </div>
</div>
<!-- =================================全文认领查看全部end=================================== -->