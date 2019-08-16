<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.kw__box {
  display: flex;
  flex-wrap: wrap;
  overflow: hidden;
  color: rgba(0, 0, 0, 0.54);
}

.kw-chip_small {
  background-color: #ebf5ff;
  text-align: center;
  border-radius: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 22px;
  padding: 0 6px;
  font-size: 14px;
  line-height: 22px;
  margin: 4px 8px 4px 0;
}

.kw-chip_small:hover .normal-global_del-icon_show {
  visibility: visible;
}

.setup-keyword__box {
  width: 390px;
  padding: 16px;
}

.setup-keyword__box-input {
  border-bottom: 1px solid #ccc;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  min-width: 160px;
}

.setup-keyword__box-input_icon {
  width: 24px;
  height: 24px;
  font-size: 20px;
  color: #ccc;
  margin-top: 8px;
}

.setup-keyword__box-input_import {
  width: 100%;
  border: none;
  height: 32px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//新增标签初始化事件
	var $inputobj = $(".dev_add_grp_label_name");
	Grp.loadLabelInput($inputobj,function(ev,$this){
		$this.closest(".setup-keyword__box-input").find("i").eq(0).click();
		$this.closest(".setup-keyword__box-input")[0].querySelector(".setup-keyword__input-tip1").style.display="block";
		$this.closest(".setup-keyword__box-input")[0].querySelector(".setup-keyword__input-tip2").style.display="none";
	});
	//加载群组文件列表
	Grp.showGrpFileList();
	Grp.showGrpMemberFileInfoList();
	Grp.showFileLabelList(); 
	document.getElementsByClassName("dialogs__childbox_footer-tip")[0].onclick = function(){
		if(this.innerHTML=="check_box_outline_blank"){
			this.innerHTML="check_box";
		}else{
			this.innerHTML="check_box_outline_blank";
		}
	}
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
    	targetlist[i].querySelector("input").onfocus = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
    	}
    	targetlist[i].querySelector("input").onblur = function(){
    		this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";	
    	}
    }
    
    //document点击事件
    $(document).on('click',function(e){
        var _target = $(e.target);
        //隐藏设置标签选项
        $(".main-list__item .idx-social__item-show").css("display" , "none");
        $("#grp_file_upload_file_id").find(".set__result-label_item").css("display" , "none");
    });
    //
    var inputele = document.getElementsByClassName("setup-keyword__box-input_import")[0];
    inputele.onfocus = function(){
        document.getElementsByClassName("setup-keyword__input-tip2")[0].style.display="block";
        this.closest(".setup-keyword__box-input").querySelector(".setup-keyword__input-tip1").style.display="none";
    }
    inputele.onblur = function(){
        if((this.value.trim().length=="")||(this.value.trim()=="新增标签")||(this.value.trim()=="Add labels")){
            console.log(this.value);
            this.value = "";
            $(".setup-keyword__input-tip2").css("display" , "none");
            console.log(document.getElementsByClassName("setup-keyword__input-tip2").length);
            document.getElementsByClassName("setup-keyword__input-tip2")[0].style.display="none";
            this.closest(".setup-keyword__box-input").querySelector(".setup-keyword__input-tip2").style.display="none";
            this.closest(".setup-keyword__box-input").querySelector(".setup-keyword__input-tip1").style.display="block";
        }
    }
    document.onkeydown = function(event){
        if(event.keyCode == 27){
            event.stopPropagation();
            event.preventDefault();
            Grp.closeSelectGrpFileMethod();
        }
    }
});
//初始化 分享 插件
function fileInitSharePlugin(obj,event){
	$("#share_to_scm_box").find(".nav__underline").hide();
	var dyntype = $("#share_to_scm_box").attr("dyntype") ;
	var styleVersion ;
	if("GRP_ADDFILE"==dyntype||"GRP_SHAREFILE"==dyntype||"GRP_ADDCOURSE"==dyntype||"GRP_ADDWORK"==dyntype){
		styleVersion = "10" ;
	}
	$(obj).dynSharePullMode({
		'groupDynAddShareCount':"",
		'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
		'styleVersion' : styleVersion
	});
	$("#share_site_div_id").find(".inside").click()
	var $li = $("#share_to_scm_box").find("li");
	$li.eq(0).hide();
	$li.eq(1).click();
	document.getElementsByClassName("nav__item-selected")[0].classList.remove("nav__item-selected");
    document.getElementsByClassName("nav__item-container")[0].querySelector(".item_selected").classList
      .add("nav__item-selected");
	//$("#share_to_scm_box").find(".nav__underline").show();
}
</script>
<div class="container__horiz_left width-8" id="grp_file_main_id" des3GrpId="${des3GrpId }">
  <div class="container__card">
    <div class="main-list">
      <!-- 文件列表 头部 -->
      <div class="main-list__header" id="main-list__header_id" grpCategory="${grpCategory }" fileType="">
        <s:if test="grpCategory==10">
          <div class="main-list-header__title">
            <div class="filter-list checkbox-style" list-filter="grpfilelist">
              <s:if test=" module=='curware'  ">
                <div class="filter-list__section" filter-section="courseFileType" filter-method="master"
                  style="display: none;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="2">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.file.curware' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:if>
              <s:else>
                <div class="filter-list__section" filter-section="workFileType" filter-method="master"
                  style="display: none;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="1">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.file.work' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:else>
            </div>
          </div>
          <div class="main-list-header__searchbox" style="margin-right: 64px;">
            <div class="searchbox__container main-list__searchbox" list-search="grpfilelist">
              <div class="searchbox__main">
                <input placeholder=" <s:text name='groups.file.search' />">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <s:if test=" module =='curware' && ( grpRole==1 ||  grpRole ==2 )">
            <button class="button_main button_primary-reverse" onclick="Grp.addGrpFile(2)">
              <s:text name='groups.file.addCurware' />
            </button>
          </s:if>
          <s:if test=" module =='work' && ( grpRole==1 ||  grpRole ==2 || grpRole ==3 )">
            <button class="button_main button_primary-reverse" onclick="Grp.addGrpFile(1)">
              <s:text name='groups.file.addAssign' />
            </button>
          </s:if>
        </s:if>
        <s:else>
          <div class="main-list-header__title"></div>
          <div class="main-list-header__searchbox" style="margin-right: 64px;">
            <div class="searchbox__container main-list__searchbox" list-search="grpfilelist">
              <div class="searchbox__main">
                <input placeholder=" <s:text name='groups.file.search' />">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <s:if test="grpRole==1 ||  grpRole ==2 || grpRole ==3">
            <button class="button_main button_primary-reverse" onclick="Grp.addGrpFile(0)">
              <s:text name='groups.file.addFile' />
            </button>
          </s:if>
        </s:else>
      </div>
      <div class="main-list__list" id="grp_file_list" list-main="grpfilelist" workFile="1" courseFile="2" search-key="">
        <!-- 文件列表 -->
      </div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grpfilelist">
          <!-- 翻页 -->
        </div>
      </div>
    </div>
  </div>
</div>
<div class="container__horiz_right width-4">
  <div class="container__card">
    <div class="module-card__box">
      <div class="module-card__header" id="grp_member_upload_file_id">
        <div class="module-card-header__title">
          <s:text name='groups.file.uploadFile' />
        </div>
        <button class="button_main button_icon" onclick="GrpFile.showSearchGrpMemberFile();">
          <i class="material-icons">search</i>
        </button>
      </div>
      <div class="module-card__header" id="grp_member_upload_file_search_id" style="display: none">
        <button class="button_main button_icon" onclick="GrpFile.hiddenSearchGrpMemberFile();">
          <i class="material-icons">arrow_back</i>
        </button>
        <div class="module-card-header__title">
          <div class="input__box" style="width: 250px;">
            <div class="input__area">
              <input placeholder="<s:text name='groups.file.search.member' />" value="${searchGrpFileMemberName}"
                id="searchGrpFileMemberNameId" onkeyup="Grp.showGrpMemberFileInfoList();">
            </div>
          </div>
        </div>
        <button class="button_main button_icon" onclick="GrpFile.emptySearchGrpMemberFile();">
          <i class="material-icons">close</i>
        </button>
      </div>
      <div des3MemberId="" id="memberSelectId">
        <div class="friend-selection__box" id="grp_member_file_info_list">
          <!--  群组成员 信息 -->
        </div>
      </div>
    </div>
  </div>
  <!-- 文件标签 -->
  <div class="module-card__box" id="grp_file_label_box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name="groups.file.label" />
      </div>
    </div>
    <div class="setup-keyword__box">
      <div class="kw__box">
        <!--  标签列表 -->
      </div>
      <div class="setup-keyword__box-input">
        <i class="material-icons setup-keyword__box-input_icon setup-keyword__input-tip1"
          onclick="Grp.addGrpLabel(this);">add</i> <input class="setup-keyword__box-input_import dev_add_grp_label_name"
          type="text" id="add_grp_label_name" placeholder="<s:text name="groups.file.label.add"/>"> <i
          class="material-icons setup-keyword__box-input_icon setup-keyword__input-tip2"
          onclick="Grp.addGrpLabel(this);">add</i>
      </div>
    </div>
  </div>
</div>
</div>
<div class="dialogs__box dialogs__childbox_limited-normal" dialog-id="select_import_grp_file_method_dialog" flyin-direction="bottom" style="width: auto;"
  id="select_import_grp_file_method">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.file.import.type' />
      </div>
      <i class="list-results_close" onclick="Grp.closeSelectGrpFileMethod()"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted " style="height: 215px;">
    <div class="import-methods__box">
      <div class="import-methods__sxn" onclick="GrpFile.SelectFileUpload();">
        <div class="import-methods__sxn_logo file-import"></div>
        <div class="import-methods__sxn_name">
          <s:text name='groups.file.localImport' />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name='groups.file.localImport.desc' />
        </div>
      </div>
      <div class="import-methods__sxn" onclick="Grp.SelectMyFileImport();">
        <div class="import-methods__sxn_logo library-import"></div>
        <div class="import-methods__sxn_name">
          <s:text name='groups.file.filesImport' />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name='groups.file.filesImport.desc' />
        </div>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_footer" style="">
    <i class="material-icons dialogs__childbox_footer-tip" style="">check_box</i> <span
      class="dialogs__childbox_footer-content" style=""><s:text name='groups.file.importBtn.notify' /></span>
  </div>
</div>
<div class="dialogs__box dialogs__childbox_limited-bigger"  id="select_my_file_import" dialog-id="select_my_file_import_dialog"
  flyin-direction="bottom">
  <div class="dialogs__childbox_fixed" id="id_myfile_header">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.file.filesImport.myfiles' />
      </div>
      <div class="dialogs__header_searchbox" style="margin-right: 64px;">
        <div class="searchbox__container main-list__searchbox" list-search="myfilelist">
          <div class="searchbox__main">
            <input placeholder=" <s:text name='groups.file.filesImport.search' />" id="grp_file_search_my_file_key">
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
      </div>
      <button class="button_main button_icon" onclick="GrpFile.closeMyFileImport();GrpFile.refreshDrawerSelected();">
        <i class="list-results_close"></i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="grpFileMyFileListId" list-main="myfilelist">
      <!-- 我的文件列表 -->
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="GrpFile.importMyFileListToGrp (this)">
        <s:text name='groups.file.importBtn.import' />
      </button>
    </div>
  </div>
</div>
<!-- cover-event="hide" -->
<div class="dialogs__box" dialog-id="grp_file_eidt_file_dialog" flyin-direction="bottom" style="width: 480px"
  id="grp_file_edit_file_id" des3_grp_file_id="" des3_grp_id="">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">文件描述</div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24">
      <form>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title">文件描述</label>
            <div class="input__area">
              <textarea id="grp_file_edit_file_content" maxlength="200"></textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="Grp.saveEditGrpFile();">保存</button>
      <button class="button_main button_primary-cancle" onclick="Grp.hiddenEditGrpFile();">取消</button>
    </div>
  </div>
</div>
<div class="dialogs__box  dialogs__childbox_limited-big" dialog-id="grp_file_upload_file" flyin-direction="bottom"
  id="grp_file_upload_file_id">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.file.localImport.select' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="overflow-y: auto;">
    <div class="dialogs__content global__padding_24" style="padding-bottom: 0px !important" id="fileupload">
      <div class="set__result-label">
        <span class="set__result-label_title" style="cursor: pointer;"
          onclick="Grp.showFileLabelListForUploadFile(this);"><s:text name="groups.file.label.set" /></span> <i
          class="material-icons set__result-label-flag" onclick="Grp.showFileLabelListForUploadFile(this);">arrow_drop_down</i>
        <div class="set__result-label_item">
          <div class="set__result-label_item-list"></div>
        </div>
      </div>
      <div class="kw__box">
        <!-- 上传文件，添加标签 -->
      </div>
      <form enctype="multipart/form-data" method="post">
        <div style="height: 160px; margin-top: 24px;">
          <div class="fileupload__box" maxlength="10"
        maxclass="grp_file_upload"></div>
        </div>
        
        <div class="form__sxn_row-list" style="min-height: 48px; width: 100%; margin: 8px 0px"></div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title"><s:text name='groups.file.localImport.fileDesc' /></label>
            <div class="input__area">
              <textarea id="grp_file_upload_file_content" class="dev_input-edit-area" maxlength="200"></textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" id="uploadfilebuttonId" onclick="GrpFile.uploadFileButton();">
        <s:text name='groups.file.importBtn.upload' />
      </button>
      <button class="dev_input-footer_delete  button_main " onclick="GrpFile.cancleUploadFile();">
        <s:text name='groups.file.importBtn.cancel' />
      </button>
    </div>
  </div>
</div>
