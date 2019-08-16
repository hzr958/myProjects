<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	//加载群组文件列表
	Grp.showGrpFileList("/groupweb/grpinfo/outside/ajaxgrpfilelist");
	$("#grp_params").attr("module","${module}");
	Grp.showFileLabelList();
	});


</script>
<div class="container__horiz_left width-8" id="grp_file_main_id" des3GrpId="${des3GrpId }">
  <div class="container__card">
    <div class="main-list">
      <!-- 文件列表 头部 -->
      <div class="main-list__header" id="main-list__header_id" grpCategory="${grpCategory }" fileType=""
        style="display: none;">
        <s:if test="grpCategory==10">
          <div class="main-list__header_title">
            <div class="filter-list checkbox-style" list-filter="grpfilelist">
              <s:if test=" module=='curware'  ">
                <div class="filter-list__section" filter-section="courseFileType" filter-method="master">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="2">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.outside.file.curware' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:if>
              <s:else>
                <div class="filter-list__section" filter-section="workFileType" filter-method="master">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="1">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.outside.file.work' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:else>
            </div>
          </div>
        </s:if>
        <s:else>
        </s:else>
      </div>
      <div class="main-list__list" id="grp_file_list" list-main="grpfilelist" workFile="1" courseFile="2" search-key="">
        <!-- 文件列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grpfilelist">
          <!-- 文件列表 尾部-->
        </div>
      </div>
    </div>
  </div>
</div>
<div class="container__horiz_right width-4">
  <div class="container__card"></div>
  <!-- 文件标签 -->
  <div class="module-card__box" id="grp_file_label_box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name="groups.outside.file.label" />
      </div>
    </div>
    <div class="setup-keyword__box">
      <div class="kw__box">
        <!--  标签列表 -->
      </div>
    </div>
  </div>
</div>
