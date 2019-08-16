
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="obj" status="itStat" var="data">
    <div class="message_ask dev_pub_del_${data.uuId}">
      <input type="hidden" value="${data.uuId}" name="uuId" />
      <input type="hidden" value="${data.main.status}" name="status" />
      <div class="pub-idx__full-text_img" style="position: relative; margin-left: -17px; width:73px;"
        onclick="ValidateList.downFile('<iris:des3 code="${data.fileId}"/>')">
        <a class="message_ask_portrait"
          href="/application/validate/fileDownload?fdesId=<iris:des3 code="${data.fileId}"/>"><img
          src="${ressie}/images/achievement/icon_record_lt.png" alt=""></a> <a href="###"
          class="new-tip_container-content" title="下载文档"> <img src="${ressie}/images/file_ upload1.png"
          class="new-tip_container-content_tip1" style="display: block;"> <img
          src="${ressie}/images/file_ upload.png" class="new-tip_container-content_tip2">
        </a>
      </div>
      <div class="message_big"
        style="display: flex; justify-content: space-between; align-items: center; min-height: 90px; margin-left:40px;">
        <div
          style="display: flex; align-items: flex-start; justify-content: space-between; width: 665px; flex-direction: column; min-height: 90px;">
          <div style="display: flex; align-items: flex-start; flex-direction: column; min-height: 60px;">
            <div class="fg sie_lsit_p3" style="line-height: 24px; height: 52px; width: 600px" onclick="ValidateList.viewDetail('${data.main.status}','${data.main.des3UuId }',this,'true');">
              <c:if test="${!empty data.main.title}">
                  <a href="###" class="message_data data3 multipleline-ellipsis__content-box" title="${data.main.title}">${data.main.title}</a>
                </c:if>
            </div>
            <div class="monicker" style="line-height: 24px; width: 500px auto;">
              <b><span class="sie_lsit_span5" style="max-width: 600px;" title="${data.uuId}">${data.uuId}</span></b>
            </div>
            <div class="monicker_lt mw410 f999 ellipsis_divv" style="line-height: 24px; width: 600px;"
              title="${data.submitTimeStr}">${data.submitTimeStr}</div>
             <div class="evaluate">
                <a href="javascript:void(0)" class="assist_5" onclick="ValidateList.confirmDelMain ('${data.des3Id }')"><i style="margin-top: 2px;"></i>删除</a>
              </div>
          </div>
        </div>
      </div>
      <div class="w200">
        <p class="ds_jcc schedule_examine tc">
          <c:choose>
            <c:when test="${data.main.status eq 1}">
              <a href="###" onclick="ValidateList.viewDetail('${data.main.status}','${data.main.des3UuId }',this);"
                class="icon_examine1" title="文档已验证完成"><i
                style="display: inline-block; width: 18px; height: 18px; margin-right: 4px; background: url(/ressie/images/icon_via.png) no-repeat;"></i></a>
            </c:when>
            <c:otherwise>
              <a href="###" onclick="ValidateList.viewDetail('${data.main.status}','${data.main.des3UuId }',this);"
                class="icon_examine1" title="该文档后台验证中，请稍后查看"><i
                style="display: inline-block; width: 18px; height: 18px; margin-right: 4px; background: url(/ressie/images/icon-new.png) no-repeat -85px -55px;"></i></a>
            </c:otherwise>
          </c:choose>
        </p>
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>                        
  <!-- <div class="confirm_words_bt">科研验证，帮助科研人员及时发现诚信风险，提高申请成功率、项目完成率，目前无待验证文档。</div> -->
    <div style="display: flex; margin-top: 130px;align-content: flex-start;">
       <div class="new-paypage_tip-item_icon"></div>
       <div style="width: 720px; margin-left: 20px;">
           <div class="new-paypage_tip-item_title">
            科研验证是基于科研之友海量科研数据，为科研人员提供的科研活动自查工具，通过自查各项指标，了解申报书、进展报告、结题报告中可能存在的诚信风险，提高申请成功率和项目完成率。
           </div>
           <div class="new-paypage_tip-item_use">
                              使用步骤：
           </div>
           <div>
             <div class="new-paypage_tip-item_list">
               1.登录资助机构业务系统（如<a href="https://isisn.nsfc.gov.cn" target="_blank" style="color: #2882d8 !important;">国家自然科学基金委会</a>、<a href="http://ywgl.jxstc.gov.cn" target="_blank" style="color: #2882d8 !important;">江西省科技厅</a>）
             </div>
             <div class="new-paypage_tip-item_list">
                2.生成需要验证的PDF文档（已提交的申报项目、进展报告或结题报告）
             </div>
             <div class="new-paypage_tip-item_list">
                3.点击“+新的待验证文档”，进入提交页，上传该PDF文档
             </div>
             <div class="new-paypage_tip-item_list">
                4.等待验证结果，点击验证结果图标或标题，进入详情页查看验证结果
             </div>
             <div class="new-paypage_tip-item_list">
                5.如有疑问，可联系<a style="color: #2882d8 !important;" onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=${pageContext.request.requestURL}&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">在线客服</a>
             </div>
           </div>
       </div>
   </div>
</s:else>
<%@include file="/common/page-tags.jsp"%>
