<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="${ressie}/css/reset.css" />
<link rel="stylesheet" href="${ressie}/css/common.css" />
<link rel="stylesheet" href="${ressie}/css/administrator.css" />
<link rel="stylesheet" href="${ressie}/css/plugin/toast.css" />
<link rel="stylesheet" href="${ressie}/css/researcher.effect.css" />
<script type="text/javascript" src="${ressie}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ressie}/js/jquery.js"></script>
<script type="text/javascript" src="${ressie}/js/ion.checkRadio.min.js"></script>
<script type="text/javascript" src="${ressie}/js/action.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/smate.toast.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/echarts.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/proplem.analysis.extract_zh_CN.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/proplem.analysis.extract.js"></script>
<script type="text/javascript" src="${resapp}/problem-analysis/researcher_effect.js"></script>
<title>开题分析</title>
<script type="text/javascript">
$(document).ready(function(){
	var obj = $("#kw1");
	$(obj).addClass("analysis-importantkey_container-item-border");
    $("#trend_upload").css("display","block");
	Analysis.selectKw(obj);
	//登录框登录不刷新
    $("#login_box_refresh_currentPage").val("false");
});
</script>
</head>
<body>
  <form action="" method="POST" id="regForm">
    <input type="hidden" id="insNames" name="insNames" value="" />
    <input type="hidden" id="selectKw" name="selectKw" value="" /> 
    <input type="hidden" id="trend_sign" name="trend_sign" value="false"/> 
    <input type="hidden" id="dis_sign" name="dis_sign" value="false" /> 
    <input type="hidden" id="psn_sign" name="psn_sign" value="false" />
    <input type="hidden" id="ins_sign" name="ins_sign" value="false" />
    <input type="hidden" name="title" value="${title }" id="title">
    <input type="hidden" name="summary" value="${summary }" id="summary">
    <div class="message">
      <div class="message_conter">
        <div class="sie_kingmaster_conter ">
          <div class="seek sie_management_seek">
            <p class="headline_1">
              科研查新<span class="ml8">通过大数据技术，了解未来趋势，有针对性投入</span>
            </p>
            <div class="clear"></div>
            <div class="handin_import-content_container-cancle" onclick="Analysis.back();">返回</div>
          </div>
          <div class="bfa">
            <div class="inspection_data pt14">
              <div class="SIE_psninfor-item">
                <div class="SIE_psninfor-item_left" style="width: 70px; margin-right: 14px; text-align: right;">
                  <span style="padding: 0px; margin: 0px;">标题：</span>
                </div>
                <div class="SIE_psninfor-item_right" style="margin-left: 9px;">
                  <div class="SIE_psninfor-item_right-content sie_service_top">
                    <input type="text" name="title" id="title" class="SIE_psninfor-item_right-content_input"
                      style="background-color: #ffffff; color: #333333;" value="${title }" disabled="disabled">
                  </div>
                </div>
              </div>
              <div class="SIE_psninfor-item">
                <div class="SIE_psninfor-item_left" style="width: 70px; margin-right: 14px; text-align: right;">
                  <span style="padding: 0px; margin: 0px;">摘要：</span>
                </div>
                <div class="SIE_psninfor-item_right" style="margin-left: 9px;">
                  <div class="SIE_psninfor-item_right-content">
                    <textarea name="summary" id="summary" cols="30" rows="10" class="textarea_original w715"
                      style="background: #ffffff; color: #333333; margin: 0px;" disabled="disabled">${summary }</textarea>
                  </div>
                </div>
              </div>
              <div class="SIE_psninfor-item" style="padding-bottom: 32px;">
                <div class="SIE_psninfor-item_left" style="margin-left: 12px;">
                  <span>选择关键词：</span>
                </div>
                <div class="SIE_psninfor-item_right">
                  <div class="w1042 dj_s">
                    <div class="key_word-member ds_c analysis-importantkey_container">
                      <input type="hidden" name="keywords_list" />
                      <c:forEach items="${keyWordsList}" var="result">
                        <c:set value="${index+1}" var="index" scope="page" />
                        <a class="analysis-importantkey_container-item" id="kw${index }"
                          onmouseup="Analysis.selectKw(this);" onmousedown="Analysis.signSelectKw(this);">${result['kw'] }
                          <!--                                             <b onclick='Analysis.deleteElement(this);'></b> -->
                        </a>
                      </c:forEach>
                    </div>
                    <!--                                     <a href="#" class="btn_refresh ml10" onclick="Analysis.extractKeywords();"><i class="icon_common icon-refresh mr4"></i>抽取</a> -->
                    <div class="key_word-member ml20">
                      <input type="text" placeholder="请输入关键词" id="keyword">
                      <!--                                         <a href="#" class="increased" id="key_increased" onclick="str();"><span></span>添加关键词</a> -->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <ul id="sie_join" class="sie_join sie_join_novelty bt mt12">
            <li id="four1" onmousedown="Analysis.setTab('four',1,4)" onmouseup="Analysis.researchTrend(this);"
              class="hover"><a>科研趋势</a>
              <p></p></li>
            <!--                     <li id="four2" onclick="setTab('four',2,5)" class=""><a>关联研究</a><p></p></li> -->
            <li id="four2" onmousedown="Analysis.setTab('four',2,4)" onmouseup="Analysis.relatedDis(this);" class=""><a>相关学科</a>
              <p></p></li>
            <li id="four3" onmousedown="Analysis.setTab('four',3,4)" onmouseup="Analysis.researchers(this);" class=""><a>科研人员</a>
              <p></p></li>
            <li id="four4" onmousedown="Analysis.setTab('four',4,4)" onmouseup="Analysis.institution(this);" class=""><a>科研单位</a>
              <p></p></li>
          </ul>
          <div>
            <div class="version-tip1" style="display: none;" id="trend_upload">
              <img class="sie_upload2" src="${ressie }/images/upload.gif" alt="">
            </div>
            <div id="con_four_1" style="width: 1200px; height: 500px; display: block;" class="mt30"></div>
            <!--                     <div class="big" style="display:none;" id="trend"> -->
            <!--                         <div class="tagbox"> -->
            <!--                             <div class="noRecord" style='margin-top: -40px;'> -->
            <!--                                 <div class="content"> -->
            <!--                                     <div class="no_effort"> -->
            <!--                                         <h2 class=>很抱歉，未找到与检索条件相关结果</h2> -->
            <!--                                         <div class="no_effort_tip pl27"> -->
            <!--                                             <span>温馨提示： </span> -->
            <!--                                             <p>检查选择关键词是否正确</p> -->
            <!--                                             <p>更换关键词</p> -->
            <!--                                         </div> -->
            <!--                                     </div> -->
            <!--                                 </div> -->
            <!--                             </div> -->
            <!--                         </div> -->
            <!--                     </div> -->
            <!--                     <div id="con_four_2" style="display: none;"></div> -->
            <div class="version-tip1" style="display: none;" id="dis_upload">
              <img class="sie_upload2" src="${ressie }/images/upload.gif" alt="">
            </div>
            <div id="con_four_2" style="display: none; width: 1200px; height: 500px;" class="mt30">
              <div class="big" style="display: none;" id="related_dis">
                <div class="tagbox">
                  <div id="searchers" class="noRecord" style='margin-top: -40px;'>
                    <div class="content">
                      <div class="no_effort">
                        <h2 class=>很抱歉，未找到与检索条件相关结果</h2>
                        <div class="no_effort_tip pl27">
                          <span>温馨提示： </span>
                          <p>检查选择关键词是否正确</p>
                          <p>更换关键词</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="version-tip1" style="display: none;" id="researchers_upload">
              <img class="sie_upload2" src="${ressie }/images/upload.gif" alt="">
            </div>
            <div id="con_four_3" style="display: none; width: 1200px; height: 500px;" class="mt30">
              <!--                         <div class="big"><div class="tagbox" id="tagbox"> -->
              <!--                                 <a title="精准医疗" class="c1" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:1;">精准医疗</a> -->
              <!--                                 <a title="精准医疗" class="c1" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:1;"> 小明</a> -->
              <!--                                 <a title="无人智能技术" class="c2"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:2;">无人智能技术</a> -->
              <!--                                 <a title="智能机器人" class="c3"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:3;">智能机器人</a> -->
              <!--                                 <a title="精准医疗" class="c5" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:5;"> aaa</a> -->
              <!--                                 <a title="无人智能技术" class="c6"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:6;">bbbbb</a> -->
              <!--                                 <a title="智能机器人" class="c7"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:7;">ccccc</a> -->
              <!--                                 <a title="智能机器人" class="c7"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:7;">小智</a> -->
              <!--                                 <a title="3D打印技术" class="c4" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:4;">小杰</a>  -->
              <!--                                 <a title="3D打印技术" class="c4" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:4;">3D打印技术</a> -->
              <!--                                 <a title="3D打印技术" class="c8" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:8;">ddddd</a>  -->
              <!--                                 <a title="3D打印技术" class="c8" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:8;">小星</a> -->
              <!--                                 <a title="无人智能技术" class="c2"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:2;">小玲</a> -->
              <!--                                 <a title="无人智能技术" class="c6"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:6;">小芳</a> -->
              <!--                                 <a title="智能机器人" class="c3"  style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:3;">小丽</a> -->
              <!--                                 <a title="精准医疗" class="c5" style="left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:5;"> 大熊</a> -->
              <!--                                 <div id="researchers" class="noRecord" style='margin-top: -40px; display:block;'> -->
              <!--                                     <div class="content"> -->
              <!--                                         <div class="no_effort"> -->
              <!--                                             <h2 class=>很抱歉，未找到与检索条件相关结果</h2> -->
              <!--                                             <div class="no_effort_tip pl27"> -->
              <!--                                                 <span>温馨提示： </span> -->
              <!--                                                 <p>检查选择关键词是否正确</p> -->
              <!--                                                 <p>更换关键词</p> -->
              <!--                                             </div> -->
              <!--                                         </div> -->
              <!--                                     </div> -->
              <!--                                 </div> -->
              <!--                             </div> -->
              <!--                         </div> -->
            </div>
            <div class="version-tip1" style="display: none;" id="ins_upload">
              <img class="sie_upload2" src="${ressie }/images/upload.gif" alt="">
            </div>
            <div id="con_four_4" style="display: none; width: 1200px; height: 500px;" class="mt30"></div>
            <div class="noresult-div">
              <div class="big noresult" style="display: none;">
                <div class="tagbox" id="tagbox">
                  <div class="noRecord" style='margin-top: -40px;'>
                    <div class="content">
                      <div class="no_effort">
                        <h2 class=>很抱歉，未找到与检索条件相关结果</h2>
                        <div class="no_effort_tip pl27">
                          <span>温馨提示： </span>
                          <p>检查选择关键词是否正确</p>
                          <p>更换关键词</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</body>
</html>
