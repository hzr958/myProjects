<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="set-email__main-title">
  <span class="set-email__main-heading"><s:text name="psnset.emailset" /></span>
</div>
<div class="set-email__container">
  <div class="set-email__container-subheading">
    <s:text name="email.notification" />
    <s:text name="psnset.colon" />
  </div>
  <div class="set-email__slector-container">
    <div class="set-email__slector-container__header">
      <div class="set-email__slector-container__list">
        <s:text name="email.header.part1" />
      </div>
      <div class="set-email__slector-container__list">
        <s:text name="email.header.part2" />
      </div>
      <div class="set-email__slector-container__list">
        <s:text name="email.header.part3" />
      </div>
      <div class="set-email__slector-container__list">
        <s:text name="email.header.part4" />
      </div>
      <div class="set-email__slector-container__list">
        <s:text name="email.header.part5" />
      </div>
    </div>
    <div class="set-email__slector-container__content">
      <div class="set-email__selector-list" style="border-style: none;">
        <c:forEach items="${ constMailTypeList}" var="mailType">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${mailType.remark =='1' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeZhName }</span>
              </div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${mailType.remark =='1' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeEnName }</span>
              </div>
            </c:if>
          </s:else>
        </c:forEach>
      </div>
      <div class="set-email__selector-list">
        <c:forEach items="${ constMailTypeList}" var="mailType">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${mailType.remark =='2' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeZhName }</span>
              </div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${mailType.remark =='2' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeEnName }</span>
              </div>
            </c:if>
          </s:else>
        </c:forEach>
      </div>
      <div class="set-email__selector-list">
        <c:forEach items="${ constMailTypeList}" var="mailType">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${mailType.remark =='3' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeZhName }</span>
              </div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${mailType.remark =='3' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeEnName }</span>
              </div>
            </c:if>
          </s:else>
        </c:forEach>
      </div>
      <div class="set-email__selector-list">
        <c:forEach items="${ constMailTypeList}" var="mailType">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${mailType.remark =='4' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeZhName }</span>
              </div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${mailType.remark =='4' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeEnName }</span>
              </div>
            </c:if>
          </s:else>
        </c:forEach>
      </div>
      <div class="set-email__selector-list">
        <c:forEach items="${ constMailTypeList}" var="mailType">
          <s:if test="#attr.lang eq  'zh'">
            <c:if test="${mailType.remark =='5' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeZhName }</span>
              </div>
            </c:if>
          </s:if>
          <s:else>
            <c:if test="${mailType.remark =='5' }">
              <div class="set-email__selector-list__box" code="${mailType.mailTypeId }">
                <i class="set-email__selector-tip set-email__select-btn"></i> <span class="set-email__tip-content">${mailType.typeEnName }</span>
              </div>
            </c:if>
          </s:else>
        </c:forEach>
      </div>
    </div>
    <div class="check-language" id="check_language_id">
      <span><s:text name="email.language.save" /> <s:text name="psnset.colon" /></span>
      <div class="check-language__list">
        <i class="selector-language__btn check_language" code="zh_CN"></i><span><s:text name="email.language.zh" />
        </span>
      </div>
      <div class="check-language__list">
        <i class="selector-language__btn check_language" code="en_US"></i><span><s:text name="email.language.en" />
        </span>
      </div>
    </div>
    <div class="set-email__forsave">
      <div class="set-email__savebtn savebtn_spcset" onclick="PsnsettingEmail.ajaxSavePsnMailSet(this);">
        <s:text name="email.language.save" />
      </div>
    </div>
  </div>
</div>
