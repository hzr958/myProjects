<%@ page language="java" pageEncoding="UTF-8"%>
<div class="left-wrap ">
  <div class="mod-subnav">
    <div class="Retrieval">
      <!-- 搜索成果 -->
      <input type="text" class="Retrieval-input" id="searchKey" name="searchKey" value="<c:out value="${searchKey}"/>" />
      <input onclick="Group.publication.doSearch()" title='<s:text name="application.file.tip.inputTip.groupPubs"/>'
        type="button" class="btn-search" />
    </div>
    <div class="sidebar-nav mtop10">
      <ul>
        <li id="menu-mine">
          <!-- 所有成果 --> <a onclick="switchContents('menu-mine','/groupweb/grouppub/show')"> <span
            class="Shear-headnone"></span>
            <div class="Fleft text-overflow2 nav_img01">
              <s:text name="group.leftMenu.item.publication.all" />
            </div>
        </a>
        </li>
        <li style="position: relative;">
          <!-- 标签 --> <a onclick="scmLeftMenu.showMenu(this)"> <span class="Shear-head"></span>
            <div class="Fleft text-overflow2 nav_img02">
              <s:text name="action.label.tagname" />
            </div>
        </a>
          <dl class="menu-shrink" id="tag-dl">
            <!-- 文件夹列表 -->
            <s:iterator value="groupFolderList" var="item">
              <dd style="display: none;" class="groupTag" id="menu-dd-f${groupFolderId}">
                <a title="${fn:escapeXml(folderName)}(${folderSum})"
                  onclick="switchContents('menu-dd-f${groupFolderId}','/groupweb/grouppub/show','folder','<iris:des3 code="${groupFolderId}"/>')">
                  <div class="two_nav_name">${fn:escapeXml(folderName)}</div>
                  <div class="Fright">(${folderSum})</div>
                  <div class="clear"></div>
                </a>
              </dd>
            </s:iterator>
            <dd id="tagMove" class="date_bottom">
              <!-- 前后十个 -->
              <a id="forward" class=""><span><s:text name="tag.page.forward" /></span></a> <a id="back"
                class="canclick"><span><s:text name="tag.page.back" /></span></a>
            </dd>
            <dd id="menu-dd-folder-else">
              <!-- 未归类 -->
              <a title="<s:text name="group.leftMenu.item.untitled" />(${groupPsn.sumPubsNfolder})"
                onclick="switchContents('menu-dd-folder-else','/groupweb/grouppub/show','folder','<iris:des3 code="-1"/>')">
                <div class="two_nav_name">
                  <s:text name="group.leftMenu.item.untitled" />
                </div>
                <div class="Fright">(${groupPsn.sumPubsNfolder})</div>
                <div class="clear"></div>
              </a>
            </dd>
          </dl> <%-- 非群组成员不能提供管理操作 --%> <s:if test="groupInvitePsn != null">
            <%@ include file="../grouppub/group_pub_label_management.jsp"%>
            <%-- 标签管理 --%>
            <div id="management" title="<s:text name="dialog.manageTag.title"/>">
              <a href="#"><s:text name="action.label.manager.tagname" /> </a>
            </div>
          </s:if>
        </li>
        <li><a onclick="scmLeftMenu.showMenu(this)"><span class="Shear-head"></span>
            <div class="Fleft text-overflow2 nav_img03">
              <s:text name="group.leftMenu.item.publication.category" />
            </div> </a>
          <dl class="menu-shrink">
            <!-- 成果类别 -->
            <s:iterator var="item" value="pubTypes">
              <dd id="menu-dd${item.id}">
                <a
                  onclick="switchContents('menu-dd${item.id}','/groupweb/grouppub/show','category','<iris:des3 code="${item.id}"/>')"><div
                    class="two_nav_name">${item.name}</div>
                  <div class="Fright">(${item.count})</div>
                  <div class="clear"></div> </a>
              </dd>
              <input type="hidden" class="pub_type_id" id="pub_tid_${item.id}" value="<iris:des3 code="${item.id}"/>" />
            </s:iterator>
          </dl></li>
        <li>
          <!-- 收录类别 --> <a onclick="scmLeftMenu.showMenu(this)"><span class="Shear-head"></span>
            <div class="Fleft text-overflow2 nav_img04">
              <s:text name="group.leftMenu.item.includedBy" />
            </div> </a>
          <dl class="menu-shrink">
            <s:iterator value="recordList" var="item">
              <dd id="menu-dd${item.code}">
                <a
                  onclick="switchContents('menu-dd${item.code}','/groupweb/grouppub/show','list','<iris:des3 code="${item.code}"/>')">
                  <div class="two_nav_name">${fn:toUpperCase(item.code)}</div>
                  <div class="Fright">(${item.count})</div>
                  <div class="clear"></div>
                </a>
              </dd>
            </s:iterator>
          </dl>
        </li>
        <li><a onclick="scmLeftMenu.showMenu(this)"><span class="Shear-head"></span>
            <div class="Fleft text-overflow2 nav_img05">
              <s:text name="action.label.year" />
            </div> </a>
          <dl class="menu-shrink">
            <!-- 发表年份 -->
            <s:iterator value="yearsMap.list" var="item">
              <dd id="menu-dd${item.year}" class="infolink_fundingYear">
                <a
                  onclick="switchContents('menu-dd${item.year}','/groupweb/grouppub/show','publishYear','<iris:des3 code="${item.year}"/>')">
                  <div class="two_nav_name">${item.year}</div>
                  <div class="Fright">(${item.count})</div>
                  <div class="clear"></div>
                </a>
              </dd>
            </s:iterator>
            <input type="hidden" class="fundingYearIndex" name="fundingYearIndex" value="${fundingYearIndex }" />
            <s:if test="yearsMap.list.size>5">
              <dd id="menu-dd-year-move" class="date_bottom_${locale}">
                <a class="canclick" onclick="scmLeftMenu.fundingYearMove('left');" id="left"
                  title="<s:text name='group.leftMenu.item.year.pre' />"> <span> <s:text
                      name='group.leftMenu.item.year.pre' />
                </span>
                </a> <a onclick="scmLeftMenu.fundingYearMove('right');" id="right"
                  title="<s:text name="group.leftMenu.item.year.next" />"> <span> <s:text
                      name="group.leftMenu.item.year.next" />
                </span>
                </a>
              </dd>
            </s:if>
            <dd id="menu-dd-year-else">
              <a
                onclick="switchContents('menu-dd-year-else','/groupweb/grouppub/show','publishYear','<iris:des3 code="-1"/>')"><div
                  class="two_nav_name">
                  <s:text name="group.leftMenu.item.untitled" />
                </div>
                <div class="Fright">(${yearsMap.notClassifiedCount})</div>
                <div class="clear"></div> </a>
            </dd>
          </dl></li>
      </ul>
    </div>
  </div>
</div>