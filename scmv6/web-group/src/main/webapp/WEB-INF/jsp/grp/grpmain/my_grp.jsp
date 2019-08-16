<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="format-detection" content="telephone=no">
<title>科研之友</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.scmtips.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_mainlist.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_chipbox.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script src="${resmod}/smate-pc/js/browsercompatible.js" type="text/javascript"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpManage/grp.manage.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpManage/grp.manage_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base.js"></script>
<script type="text/javascript" src="${resmod}/js/home/random.module.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base_${locale }.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpMember/grp.member.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpMember/grp.member_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript">
        $(document).ready(function () {
            var locale = '${locale}';
            $createGrpChipBox = window.ChipBox({
                name: "chipcodecreate", maxItem: 10, isSeparate: true, callbacks: {
                    compose: function () {
                        var newAddItem = $("#create_keywords").find(".chip__box").last();
                        var newAddItemText = $.trim(newAddItem.find(".chip__text").text());
                        var code =newAddItem.attr("code");
                        var newItemCodeIsNotNull = code != null && code != "" && typeof(code) != "undefined";
                        var notLastItems = $("#create_keywords").find(".chip__box:not(:last)");
                        if(notLastItems.length >= 1){
                            for(var i=0; i<notLastItems.length; i++){
                                var currentItem = $(notLastItems[i]);
                                var currentItemCode = currentItem.attr("code");
                                var currentItemCodeNotNull = currentItemCode != "" && currentItemCode != null && typeof(currentItemCode) != "undefined"; 
                                //text相同，
                                if(currentItem.find(".chip__text").text().trim() == newAddItemText){
                                    newAddItem.remove();
                                    scmpublictoast("关键词重复",2000);
                                    break;
                                }
                            }
                        }
                        var str = "";
                        $("#create_keywords").find(".chip__text").each(function (i, o) {
                            str += $(o).text() + ";";
                        });
                        console.log(str);
                        if (str.length > 640) {
                            if ("${locale}" == "en_US") {
                                scmpublictoast("Keywords is limited to 650 characters.", 2000);
                            } else {
                                scmpublictoast("关键词总长度不能超过650个字符", 2000);
                            }
                        }
                    }
                }
            });
            addFormElementsEvents();
            //加载群组列表
            var model = "${model}";
            var jumpto="${jumpto}";
            if ("rcmdGrp" == model) {
                $("#rcmd_grp_nav_item_id").click();
            } else {
                $("#grp_list").doLoadStateIco({
                    style: "height:28px; width:28px; margin:auto;margin-top:24px;",
                    status: 1
                });
                GrpBase.loadMyGrpSub(jumpto);
            }
            GrpBase.bindKeyWordsRcmd();
            var headerlist = document.getElementsByClassName("nav_horiz-container");
            var total =  document.getElementsByClassName("header__box")[0].offsetWidth; 
            var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
            var box = document.getElementsByClassName("header__nav")[0];
            var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
       /*      for(var i = 0 ; i < headerlist.length; i++){
                if(!!window.ActiveXObject || "ActiveXObject" in window){
                    if(locale == "en_US"){
                        headerlist[i].style.right = total - 230 -30 - parentleft - subleft + "px";
                    }else{
                        headerlist[i].style.right = total - 230 -20 - parentleft - subleft + "px";
                    }
                }else{
                    if(locale == "en_US"){
                        headerlist[i].style.right = total - 220 -30 - parentleft - subleft + "px";
                    }else{
                        headerlist[i].style.right = total - 220 -20 - parentleft - subleft + "px";
                    }
                } 
            } */
            document.onkeydown = function(event){
                if(event.keyCode == 27){
                     event.stopPropagation();
                     event.preventDefault();
                     GrpBase.hideGrpMemberApply();
                 }
             }
        });
        function showCreateGrp() {
            BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function(){
                showDialog("create_grp_ui");
            }, 1);
        }
        var defaultDisciplineName = '<s:text name="groups.base.priCtg" />';
        var sencondDisciplineName = '<s:text name="groups.base.secCtg" />';
        function hideCreateGrp() {
            document.getElementById("grpform").reset();
            var $selectBox1 = document.querySelector('.dev_sel__box[selector-id="1st_discipline"]');
            $selectBox1.setAttribute("sel-value", "");
            $(".dev_sel__box .sel__value_selected").text(defaultDisciplineName);
            $("div[selector-id='2nd_discipline']").find(".sel__value_selected").text(sencondDisciplineName);
            $("#autokeywords").empty();
            var $selectBox2 = document.querySelector('.dev_sel__box[selector-id="2nd_discipline"]');
            $selectBox2.setAttribute("sel-value", "");
            $selectBox2.style.visibility = "hidden";
            hideDialog("create_grp_ui");
        }
        function clickMemberOpt(obj, e) {
            $("#grp_list").find('.action-dropdown__list').removeClass("list_shown");
            $(obj).closest(".main-list__item_actions").find('.action-dropdown__list').addClass("list_shown");
            //点击其他地方关闭
            clickOtherHide(e, function () {
                $(obj).parent().find('.action-dropdown__list').removeClass("list_shown");
            });
        }
        //点击其他地方事件
        function clickOtherHide(e, myFunction) {
            if (e && e.stopPropagation) {//非IE
                e.stopPropagation();
            }
            else {//IE
                window.event.cancelBubble = true;
            }
            $(document).click(function () {
                myFunction();
            });
        }
    </script>
</head>
<body>
  <header>
    <div class="header__2nd-nav">
      <div class="header__2nd-nav_box" style="justify-content: flex-end; position: relative;">
        <c:if test="${locale=='en_US'}">
          <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px; right: 229px;">
            <ul class="nav__list">
              <li class="nav__item item_selected" style="min-width: 78px;" onclick="GrpBase.loadMyGrpSub();"><s:text
                  name='groups.base.mygroups' /></li>
              <li class="nav__item" style="min-width: 78px;" onclick="GrpManage.showRcmdGrp(this);"
                id="rcmd_grp_nav_item_id"><s:text name='groups.base.suggroups' /></li>
            </ul>
            <div class="nav__underline"></div>
          </nav>
        </c:if>
        <c:if test="${locale=='zh_CN'}">
          <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px; right: 229px;">
            <ul class="nav__list">
              <li class="nav__item item_selected" onclick="GrpBase.loadMyGrpSub();"><s:text
                  name='groups.base.mygroups' /></li>
              <li class="nav__item" onclick="GrpManage.showRcmdGrp(this);" id="rcmd_grp_nav_item_id"><s:text
                  name='groups.base.suggroups' /></li>
            </ul>
            <div class="nav__underline"></div>
          </nav>
        </c:if>
        <button class="button_main button_primary-reverse" onclick='showCreateGrp(event)'
          style="margin-left: 0px; box-sizing: content-box;">
          <s:text name='groups.base.creategroup' />
        </button>
      </div>
    </div>
  </header>
  <div class="module-home__box" id="grp_lists" style="margin: 24x auto 0; margin-top: 48px;"></div>
  <div class="dialogs__box" style="width: 800px;" dialog-id="create_grp_ui" id="create_grp_ui" flyin-direction="top">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="create-grp__select-type_container">
          <div class="create-grp__select-type_item item_selected class_create_grpCategory" smate_grpCategory='11'
            onclick='GrpBase.changeGrpType(this,11)'>
            <div class="create-grp__type_project"></div>
            <s:text name='groups.base.create.proGroup' />
          </div>
          <div class="create-grp__select-type_item" smate_grpCategory='10' onclick='GrpBase.changeGrpType(this,10)'>
            <div class="create-grp__type_course"></div>
            <s:text name='groups.base.create.curGroup' />
          </div>
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 840px;">
      <div class="dialogs__content global__padding_24">
        <form form-id="creategrp" id="grpform">
          <div class="form__sxn_row">
            <div class="input__box">
              <label class="input__title" id="id_title_grp_name"><s:text name='groups.base.create.proTitle' /></label>
              <div class="input__area">
                <input class='input_grpname_val' required maxlength="199">
              </div>
              <div class="input__helper" helper-text="" invalid-message=""></div>
            </div>
          </div>
          <div class="form__sxn_row" id="id_dev_projectno_val">
            <div class="input__box">
              <label class="input__title"><s:text name='groups.base.create.proNo' /></label>
              <div class="input__area">
                <input class='input_projectno_val' maxlength="99">
              </div>
              <div class="input__helper" helper-text="" invalid-message=""></div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box">
              <label class="input__title" id="id_title_grp_grpdescription"><s:text
                  name='groups.base.create.proabs' /></label>
              <div class="input__area">
                <textarea class='input_grpdescription_val' maxlength="1999"  style="padding: 0px;" required></textarea>
                <div class="textarea-autoresize-div"></div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box no-flex input_not-null">
              <label class="input__title"><s:text name='groups.base.proArea' /></label>
              <div class="dev_sel__box" selector-id="1st_discipline" onclick="GrpBase.showScienceAreaBox(1)">
                <div class="sel__value" style="display: flex; align-items: center;">
                  <span class="sel__value_selected sel__value_placeholder sel__value_selected-content"> <s:text
                      name='groups.base.priCtg' />
                  </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                </div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
            <div class="input__box no-flex input_not-null">
              <label class="input__title"></label>
              <div class="dev_sel__box" style="visibility: hidden" selector-id="2nd_discipline"
                onclick="GrpBase.showScienceAreaBox(1)">
                <div class="sel__value" style="display: flex; align-items: center;">
                  <span class="sel__value_selected sel__value_placeholder sel__value_selected-content"> <s:text
                      name='groups.base.secCtg' />
                  </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                </div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box no-input-area">
              <label class="input__title"><s:text name='groups.base.keywords' /></label>
              <div class="chip-panel__box" id="create_keywords" chipbox-id="chipcodecreate">
                <!-- 关键词 -->
                <div class="chip-panel__manual-input js_autocompletebox" id='autokeywords'
                  request-url="/groupweb/mygrp/ajaxautoconstkeydiscs" contenteditable="true"></div>
              </div>
              <div class="global__para_caption" style="white-space: normal;">
                <s:text name='groups.keywords.describe' />
              </div>
            </div>
          </div>
          <div class="form__sxn_row">
            <!-- 推荐关键词 -->
            <div class="input__box no-input-area grp_rcmd_keywords"></div>
          </div>
          <div class="form__sxn_row" style="flex-direction: column;">
            <div class="input__box no-input-area" style="margin-bottom: 0px; border-bottom: 1px solid #ddd;">
              <label class="input__title"><s:text name='groups.set.group.permissions' /></label>
              <div class="input-radio__box_vert" id="choose-authority-list"
                style="display: flex; flex-direction: inherit; padding: 0px;">
                <div class="input-radio__sxn" onclick="grpChangePublic('myGrp')"
                  title="<s:text name='groups.public.describe'/>">
                  <div class="input-custom-style">
                    <input type="radio" name="choose-authority" value="O"> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.public' />
                  </div>
                  <%--  <div class="input-radio_grp-auth"> <s:text name='groups.public.describe' /> </div> --%>
                </div>
                <div class="input-radio__sxn" style="margin: 0px 48px;" onclick="grpChangeSemiPublic('myGrp')"
                  title="<s:text name='groups.semi.public.describe'/>">
                  <div class="input-custom-style">
                    <input type="radio" value="H" name="choose-authority" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.semi.public' />
                  </div>
                  <%-- <div class="input-radio_grp-auth"> <s:text name='groups.semi.public.describe' /> </div> --%>
                </div>
                <div class="input-radio__sxn" onclick="grpChangeSemiPrivacy('myGrp')"
                  title="<s:text name='groups.privacy.describe'/>">
                  <div class="input-custom-style">
                    <input type="radio" value="P" name="choose-authority"> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.privacy' />
                  </div>
                  <%-- <div class="input-radio_grp-auth"> <s:text name='groups.privacy.describe' /> </div> --%>
                </div>
              </div>
            </div>
            <div>
              <div id="grpEditPublicDescribe" style="display: none;">
                <div class="input-radio_grp-auth">
                  <div class="input-radio__box_horiz" id="openType">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                        <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        首页</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexMemberOpen1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成员</div>
                    </div>
                    <!--  课程群组 成员，文献，课件，作业  -->
                    <span style="display: none;" name="corse_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isCurwareFileShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        课件</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isWorkFileShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        作业</div>
                    </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="checked" name="isIndexPubOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          文献</div>
                      </div>
                    </span>
                    <!--  项目群组 成员，成果，文献，文件   -->
                    <span style="display: flex;" name="project_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isPrjPubShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成果</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"  checked="checked" name="isPrjRefShow1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文献</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexFileOpen1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文件</div>
                    </div></span>
                  </div>
                </div>
              </div>
              <div id="grpEditSemiPublicDescribe" style="display: block;">
                <div class="input-radio_grp-auth">
                  <div class="input-radio__box_horiz" id="halfType">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                        <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        首页</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexMemberOpen2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成员</div>
                    </div>
                    <!--  课程群组 成员，文献，课件，作业  -->
                    <span style="display: none;" name="corse_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="" name="isCurwareFileShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        课件</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="" name="isWorkFileShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        作业</div>
                    </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox" checked="" name="isIndexPubOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          文献</div>
                      </div>
                    </span>
                    <!--  项目群组 成员，成果，文献，文件   -->
                    <span style="display: flex;" name="project_module">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isPrjPubShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        成果</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"  checked="checked" name="isPrjRefShow2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文献</div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" name="isIndexFileOpen2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        文件</div>
                    </div>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" onclick="GrpBase.doGrpBaseCreate(this)"
          submitform-id="creategrp">
          <s:text name='groups.create.btn.ensure' />
        </button>
        <button class="button_main button_primary-cancle" onclick="hideCreateGrp()">
          <s:text name='groups.create.btn.cancel' />
        </button>
      </div>
    </div>
  </div>
  <div class="dialogs__box" style="width: 480px" id='copy_grp_ui' dialog-id="copy_grp_ui" flyin-direction="top"
    cover-event="hide">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name='groups.list.action.copy' />
        </div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted">
      <div class="dialogs__content global__padding_24">
        <div class="global__para_body">
          <s:text name='groups.list.action.copyInfo' />
        </div>
        <form>
          <div class="form__sxn_row">
            <div class="input__box no-input-area">
              <label class="input__title"><s:text name='groups.list.action.copyModelInfo' /></label>
              <!--  10  课程群组 ，文献，课件，作业  -->
              <div id="copy_grp_ui_course" class="input-radio__box_horiz" style="display: none;">
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyBaseinfo" checked disabled="disabled"> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.list.action.copyPart1' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyGrpPubs" checked> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.list.action.copyPart2' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyCourseGrpCourseware" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label" id="label_grpCategory">
                    <s:text name='groups.file.curware' />
                  </div>
                </div>
              </div>
              <!--   11 项目群组   成果，文献，文件   -->
              <div id="copy_grp_ui_project" class="input-radio__box_horiz" style="display: none;">
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyBaseinfo" checked disabled="disabled"> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.list.action.copyPart1' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyProjectGrpPubs" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.project.pub' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyProjectGrpRefs" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.project.ref' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyGrpCourseware" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label" id="label_grpCategory">
                    <s:text name='groups.list.file' />
                  </div>
                </div>
              </div>
              <div id="copy_grp_ui_other" class="input-radio__box_horiz" style="display: none;">
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyBaseinfo" checked disabled="disabled"> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.list.action.copyPart1' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyGrpPubs" checked> <i class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.list.action.copyPart2' />
                  </div>
                </div>
                <div class="input-radio__sxn">
                  <div class="input-custom-style">
                    <input type="checkbox" name="isCopyGrpCourseware" checked> <i
                      class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label" id="label_grpCategory">
                    <s:text name='groups.list.file' />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="form__sxn_row">
            <div class="input__box">
              <label class="input__title"><s:text name='groups.list.action.copyNewName' /></label>
              <div class="input__area">
                <input style="display: none;"> <input type="text" value="default" id="copy_grp_name">
              </div>
              <div class="input__helper" helper-text="" invalid-message=""></div>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__footer">
        <button class="button_main button_primary-reverse" id="btn_copy_grp" onclick='GrpBase.doCopyGrp(this)'>
          <s:text name='groups.list.action.copyConfirm' />
        </button>
        <button class="button_main" onclick='GrpBase.hideCopyGrpUI()'>
          <s:text name='groups.list.action.copyCancel' />
        </button>
      </div>
    </div>
  </div>
  <div class="dialogs__box dialogs__box-limit_size" id="dev_jconfirm" style="width: auto;" dialog-id="dev_jconfirm_ui" flyin-direction="top">
    <div class="dialogs__modal_text"  style="width: 100%; padding: 8px 0px;">
      <s:text name='groups.list.action.quitTips' />
    </div>
    <div class="dialogs__modal_actions">
      <button class="button_main button_dense button_primary" onclick="hideDialog('dev_jconfirm_ui')">
        <s:text name='groups.manage.btn.confirm' />
      </button>
      <button class="button_main button_dense" onclick="hideDialog('dev_jconfirm_ui')">
        <s:text name='groups.manage.btn.cancel' />
      </button>
    </div>
  </div>
  <div class="sel-dropdown__box" selector-data="1st_discipline">
    <div class="sel-dropdown__list">
      <div class="sel-dropdown__item" sel-itemvalue="1" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.agri' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="2" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.sci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="3" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.humanSci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="4" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.econManage' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="5" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.engi' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="6" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.infoSci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="7" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.list.area.pharSci' />
      </div>
    </div>
  </div>
  <div class="sel-dropdown__box" selector-data="2nd_discipline" data-src="request"
    request-url="/groupweb/mygrp/ajaxgetseconddiscipline" request-data="GrpBase.getJSON()" style="min-width: 96px;">
  </div>
  <div class="dialogs__box" style="width: 720px;" dialog-id="join_grp_invite_box" flyin-direction="top">
    <div class="dialogs__childbox_fixed" style="display: flex; justify-content: space-between; align-items: center;">
      <nav class="nav_horiz">
        <ul class="nav__list">
          <li class="nav__item item_selected dev_grp_module_reg" onclick="GrpBase.changeGrpModule(this,'regGrp');"><s:text
              name='groups.list.reqGrpTitle' /></li>
          <li class="nav__item dev_grp_module_invite" onclick="GrpBase.changeGrpModule(this,'inviteGrp');"><s:text
              name='groups.list.inviteTitle' /></li>
        </ul>
        <div class="nav__underline" style="width: 112px; left: 0px;"></div>
      </nav>
      <i class="list-results_close google__icon-close" onclick="GrpBase.hideGrpMemberApply();"></i>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list" id="has_ivite_grp_list">
        <!-- 被邀请的群组列表 -->
      </div>
    </div>
  </div>
  <!-- 科技领域弹出框 -->
  <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
    process-time="0"></div>
  <!-- 科技领域弹出框 -->
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
