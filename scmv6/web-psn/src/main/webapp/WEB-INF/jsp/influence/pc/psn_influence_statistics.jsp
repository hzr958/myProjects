<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function centershowModel(obj){
    if($(obj).attr("data-model")){
        var checknum = $(obj).attr("data-model");
         if(checknum == 4){
             $(document).scrollTop(650);
        }else if(checknum == 5){
            $(document).scrollTop(950);
        }       
    }
}
</script>
<div class="container__effect-title">
  <s:text name="psnInfluence.visit.pub_impact_analytis" />
</div>
<div class="container__effect-fuctool">
  <div class="container__effect-fuctool_item" data-model="1">
    <span class="container__effect-fuctool_item-title"><s:text name="psnInfluence.visit.like" />/<s:text
        name="psnInfluence.visit.share" /></span> <span class="container__effect-fuctool_item-cnt"
      id="influence_award_share_sum">${awardAndShareSum }</span>
  </div>
  <div class="container__effect-fuctool_item" data-model="2">
    <span class="container__effect-fuctool_item-title"><s:text name="psnInfluence.visit.read" /></span> <span
      class="container__effect-fuctool_item-cnt" id="influence_visit_sum">${visitSum }</span>
  </div>
  <div class="container__effect-fuctool_item" data-model="3">
    <span class="container__effect-fuctool_item-title"><s:text name="psnInfluence.visit.download" /></span> <span
      class="container__effect-fuctool_item-cnt" id="influence_download_sum">${downLoadSum }</span>
  </div>
  <div class="container__effect-fuctool_item" data-model="4"  onclick="centershowModel(this)">
    <span class="container__effect-fuctool_item-title"><s:text name="psnInfluence.visit.cites" /></span> <span
      class="container__effect-fuctool_item-cnt" id="influence_cited_sum">${citedSum }</span>
  </div>
  <div class="container__effect-fuctool_item" data-model="5"  onclick="centershowModel(this)"> 
    <span class="container__effect-fuctool_item-title">H-index</span> <span class="container__effect-fuctool_item-cnt"
      id="influence_hindex">${hindex }</span>
  </div>
</div>