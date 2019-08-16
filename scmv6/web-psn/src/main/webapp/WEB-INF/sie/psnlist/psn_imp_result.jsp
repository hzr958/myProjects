<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员列表</title>
<link type="text/css" rel="stylesheet" href="${ressie}/css/plugin/jquery.thickbox.css" />
<link type="text/css" rel="stylesheet" href="${ressie}/css/achievement_lt.css" />
<script type="text/javascript" src="${ressie}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.json.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.alerts_${locale}.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${respsn}/person/ins.psn.import.js"></script>
<script type="text/javascript" src="${respsn}/person/ins.person.import.js"></script>
<script type="text/javascript" src="${respsn}/person/jquery.fileupload.js"></script>
<script type="text/javascript" src="${respsn}/person/jquery.filestyle.js"></script>
<script type="text/javascript" src="${respsn}/person/psn.fileupload.handler.js"></script>
<script type="text/javascript">


 </script>
</head>
<body>
  <div class="conter">
    <div id="con_five_1" style="display: block">
      <input type="hidden" name="isGoBack" id="isGoBack" value /> <input type="hidden" name="addSuccessListLength"
        id="addSuccessListLength" value="${addSuccessListSize}" />
      <div class="achievement_conter_right channel_achievement_conter">
        <div class="channel_achievement sie_database_headline mt20">
          <div>
            <a href="#" onclick="goNextStep();" class="martter-demo-step">保存</a> <a href="#" onclick="goBack();"
              class="martter-demo-browse">返回</a>
          </div>
          <c:if test="${addSuccessListSize > 0}">
            <p>
              <s:text name="rolPersonAdd.save.succed" />
            </p>
          </c:if>
        </div>
        <!-- 导入成功 -->
        <c:set var="needNextStep" value="0"></c:set>
        <c:if test="${addSuccessListSize > 0}">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="index-table01 ">
            <tr>
              <th width="12%" align="left"><s:text name="rolPersonAdd.person.name" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.firstname" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.lastname" /></th>
              <th width="22%"><s:text name="rolPersonAdd.person.email" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.title" /></th>
              <th width="30%"><s:text name="rolPersonAdd.person.unit" /></th>
            </tr>
            <c:forEach items="${addSuccessList}" var="successAdd">
              <tr>
                <td><c:out value="${successAdd.name }" /></td>
                <c:if test="${lang eq 'zh'}">
                  <td><c:out value="${successAdd.lastName }" /></td>
                  <td><c:out value="${successAdd.firstName }" /></td>
                </c:if>
                <c:if test="${lang eq 'en'}">
                  <td><c:out value="${successAdd.firstName }" /></td>
                  <td><c:out value="${successAdd.lastName }" /></td>
                </c:if>
                <td>${successAdd.email }</td>
                <td>${successAdd.position }</td>
                <td>${successAdd.unitName }</td>
              </tr>
            </c:forEach>
          </table>
        </c:if>
        <!-- 个人信息不完整 -->
        <c:if test="${impDateInfoErrorSize > 0}">
          <p class="database_reminder mt40 ">
            <b class="icon_hint_bt"></b>
            <s:text name="rolPersonAdd.save.dateerror" />
          </p>
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="index-table01 ">
            <tr>
              <th width="12%" align="left"><s:text name="rolPersonAdd.person.name" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.firstname" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.lastname" /></th>
              <th width="22%"><s:text name="rolPersonAdd.person.email" /></th>
              <th width="12%"><s:text name="rolPersonAdd.person.title" /></th>
              <th width="30%"><s:text name="rolPersonAdd.person.unit" /></th>
            </tr>
            <c:forEach items="${impDateInfoError}" var="errorAdd">
              <tr>
                <td align="center"><c:out value="${errorAdd.name }" /></td>
                <c:if test="${lang eq 'zh'}">
                  <td align="center"><c:out value="${errorAdd.lastName }" /></td>
                  <td align="center"><c:out value="${errorAdd.firstName }" /></td>
                </c:if>
                <c:if test="${lang eq 'en'}">
                  <td align="center"><c:out value="${errorAdd.firstName }" /></td>
                  <td align="center"><c:out value="${errorAdd.lastName }" /></td>
                </c:if>
                <td align="center">${errorAdd.email }</td>
                <td align="center">${errorAdd.position }</td>
                <td align="center">${errorAdd.unitName }</td>
                </td>
              </tr>
            </c:forEach>
          </table>
        </c:if>
        <!-- 人员信息重复(允许显示下一步) -->
        <c:if test="${nameRepeatListSize > 0}">
          <c:set var="needNextStep" value="1"></c:set>
          <p class="database_reminder mt40 ">
            <b class="icon_hint_bt"></b>
            <s:text name="rolPersonAdd.save.exist" />
          </p>
          <c:forEach items="${nameRepeatList}" var="nameRepeats" varStatus="repeatsStatus">
            <div class="nameRepeat_block">
              <c:forEach items="${nameRepeats}" var="nameRepeat1" varStatus="repeat1Status">
                <c:if test="${repeat1Status.index == 0}">
                  <div class="headline database_headeline">
                    <span class="message_database_headline"> <c:if test="${lang eq 'en' }">
                        <b>First&nbsp;Name:&nbsp;${nameRepeat1.firstName}&nbsp;&nbsp;
                          Last&nbsp;Name:&nbsp;${nameRepeat1.lastName }</b>
                      </c:if> <c:if test="${lang eq 'zh' }">
                        <b>姓名:&nbsp;${nameRepeat1.name }</b>
                      </c:if>
                    </span> <span class="mr60"><s:text name="rolPersonAdd.person.email" />: ${nameRepeat1.email }</span> <span
                      class="mr60"><s:text name="rolPersonAdd.person.title" />: ${nameRepeat1.position }</span> <span
                      class="mr60"><s:text name="rolPersonAdd.person.unit" />：${nameRepeat1.unitName }</span> <input
                      type="hidden" class="input_unitName" value="${nameRepeat1.unitName }" /> <input type="hidden"
                      class="input_unitId" value="${nameRepeat1.unitId }" /> <input type="hidden"
                      class="input_position" value="${nameRepeat1.position }" /> <input type="hidden"
                      class="input_posId" value="${nameRepeat1.posId }" /> <input type="hidden" class="input_firstName"
                      value="${nameRepeat1.firstName }" /> <input type="hidden" class="input_lastName"
                      value="${nameRepeat1.lastName }" /> <input type="hidden" class="input_enName"
                      value="${nameRepeat1.enName }" /> <input type="hidden" class="input_name"
                      value="${nameRepeat1.name }" /> <input type="hidden" class="input_email"
                      value="${nameRepeat1.email }" /> <input type="hidden" class="input_isNeedMail"
                      value="${nameRepeat1.isNeedMail }" />
                  </div>
                  <!-- 新增人员 -->
                  <div class="message_ask message_list">
                    <div class="message_database_newly">
                      <s:text name="user.import.adduser" />
                    </div>
                    <input type="radio" name="nameRepeat_${repeatsStatus.index }"
                      id="nameRepeat_add_${repeatsStatus.index }_${repeat1Status.index }" value="0"
                      class="nameRepeat regular-radio_1" checked="checked" onclick="replaceTo($(this));"> <label
                      for="nameRepeat_add_${repeatsStatus.index }_${repeat1Status.index }"></label>
                    <div class="message_list_check ">
                      <div class="message_list_check w425">
                        <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                          src="${ressie}/images/head_nan_photo.jpg" alt=""></a>
                        <div>
                          <p class=" fg">
                            <a href="#" class="message_data"> <c:if test="${lang eq 'en' }">
                                                       ${nameRepeat1.enName }
                                                    <c:if test="${empty nameRepeat1.enName}">
                                                        ${nameRepeat1.firstName}&nbsp;${nameRepeat1.lastName }
                                                    </c:if>
                              </c:if> <c:if test="${lang eq 'zh' }">${nameRepeat1.name }</c:if>
                            </a> <span>${nameRepeat1.position }</span>
                          </p>
                          <p class="monicker">
                            <b></b>${nameRepeat1.unitName }</p>
                          <p class="evaluate database_evaluate">
                            <i class="icon-new"></i>${nameRepeat1.email }</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:if>
                <!-- 替换原有信息 -->
                <c:if test="${repeat1Status.index > 0}">
                  <div class="message_ask message_list">
                    <div class="message_database_newly">
                      <s:text name="user.import.replaceuser" />
                    </div>
                    <input type="radio" name="nameRepeat_${repeatsStatus.index }"
                      id="nameRepeat_replace_${repeat1Status.index }_${repeatsStatus.index }"
                      value="${nameRepeat1.psnId }" class="regular-radio_1 nameRepeat" onclick="replaceTo($(this));" />
                    <label for="nameRepeat_replace_${repeat1Status.index }_${repeatsStatus.index }"></label>
                    <div class="message_list_check ">
                      <div class="message_list_check w425">
                        <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                          src="${nameRepeat1.avatars }" alt=""></a>
                        <div>
                          <p class=" fg">
                            <a href="#" class="message_data"> <c:if test="${lang eq 'en' }">${nameRepeat1.enName }</c:if>
                              <c:if test="${lang eq 'zh' }">${nameRepeat1.name }</c:if>
                            </a> <span>${nameRepeat1.position }</span>
                          </p>
                          <p class="monicker">
                            <b></b>${nameRepeat1.unitName }</p>
                          <p class="evaluate database_evaluate">
                            <i class="icon-new"></i>${nameRepeat1.email }</p>
                        </div>
                      </div>
                      <!--  替换为 -->
                      <div class="sie_message_list_check " style="display: none;">
                        <div class="message_database_newly orange">
                          <s:text name="user.adduser.replaceuser" />
                        </div>
                        <div class="message_list_check ">
                          <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                            src="${nameRepeat1.avatars }" alt=""></a>
                          <div>
                            <p class=" fg">
                              <a href="#" class="message_data newName"></a> <span class="newPos"></span>
                            </p>
                            <p class="monicker newUnit">
                              <b></b>
                            </p>
                            <p class="evaluate database_evaluate">
                              <i class="icon-new"></i> ${nameRepeat1.email }
                            </p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:if>
              </c:forEach>
            </div>
          </c:forEach>
        </c:if>
        <!-- 邮件地址重复（本单元） -->
        <c:if test="${emailRepeatListSize > 0}">
          <c:set var="needNextStep" value="1"></c:set>
          <p class="database_reminder mt40 ">
            <b class="icon_hint_bt"></b>以下人员添加失败(邮件/登录帐号已经被本单位人员使用)，请选择替换单位中存在的人员还是忽略该人员：
          </p>
          <c:forEach items="${emailRepeatList}" var="emailRepeats" varStatus="repeatsStatus">
            <div class="emailRepeat_block">
              <c:forEach items="${emailRepeats}" var="emailRepeat1" varStatus="repeat1Status">
                <c:if test="${repeat1Status.index == 0}">
                  <div class="headline database_headeline">
                    <span class="message_database_headline"> <c:if test="${lang eq 'en' }">
                                          First&nbsp;Name:&nbsp;${emailRepeat1.firstName}&nbsp;&nbsp; Last&nbsp;Name:&nbsp;${emailRepeat1.lastName }
                                    </c:if> <c:if test="${lang eq 'zh' }">姓名:&nbsp;${emailRepeat1.name }</c:if>
                    </span> <span class="mr60"><s:text name="rolPersonAdd.person.email" />: ${emailRepeat1.email }</span> <span
                      class="mr60"><s:text name="rolPersonAdd.person.title" />: ${emailRepeat1.position }</span> <span
                      class="mr60"><s:text name="rolPersonAdd.person.unit" />：${emailRepeat1.unitName }</span>
                  </div>
                  <input type="hidden" class="input_unitName" value="${emailRepeat1.unitName }" />
                  <input type="hidden" class="input_unitId" value="${emailRepeat1.unitId }" />
                  <input type="hidden" class="input_position" value="${emailRepeat1.position }" />
                  <input type="hidden" class="input_posId" value="${emailRepeat1.posId }" />
                  <input type="hidden" class="input_firstName" value="${emailRepeat1.firstName }" />
                  <input type="hidden" class="input_lastName" value="${emailRepeat1.lastName }" />
                  <input type="hidden" class="input_enName" value="${emailRepeat1.enName }" />
                  <input type="hidden" class="input_name" value="${emailRepeat1.name }" />
                  <input type="hidden" class="input_isNeedMail" value="${emailRepeat1.isNeedMail }" />
                </c:if>
                <c:if test="${repeat1Status.index > 0}">
                  <!--    保留原信息 -->
                  <div class="message_ask message_list">
                    <div class="message_database_newly">
                      <s:text name="rolpersonConfig.tip.ignore.addUser" />
                      </p>
                    </div>
                    <input type="hidden" id="repeatMail" name="repeatMail" value="${emailRepeat1.email }" /> <input
                      type="radio" name="emailRepeat_${repeatsStatus.index }"
                      id="emailRepeat_add_${repeatsStatus.index }_${repeat1Status.index }" value="0"
                      class="regular-radio emailRepeat" checked="checked" onclick="replaceTo($(this));"> <label
                      for="emailRepeat_add_${repeatsStatus.index }_${repeat1Status.index }"></label>
                    <div class="message_list_check ">
                      <div class="message_list_check w425">
                        <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                          src="${nameRepeat1.avatars }" alt=""></a>
                        <div>
                          <p class=" fg">
                            <a href="#" class="message_data"> <c:if test="${lang eq 'en' }">
                                                ${emailRepeat1.enName }
                                                <c:if test="${empty emailRepeat1.enName}">
                                                    ${emailRepeat1.firstName}&nbsp;${emailRepeat1.lastName }
                                                </c:if>
                              </c:if> <c:if test="${lang eq 'zh' }">${emailRepeat1.name }</c:if>
                            </a> <span>${emailRepeat1.position }</span>
                          </p>
                          <p class="monicker">
                            <b></b>${emailRepeat1.unitName }</p>
                          <p class="evaluate database_evaluate">
                            <i class="icon-new"></i> ${emailRepeat1.email }
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <!--    替换原信息 -->
                  <div class="message_ask message_list">
                    <div class="message_database_newly">
                      <s:text name="user.import.replaceuser" />
                    </div>
                    <input type="radio" name="emailRepeat_${repeatsStatus.index }"
                      id="emailRepeat_replace_${repeat1Status.index }_${repeatsStatus.index }"
                      value="${emailRepeat1.psnId }" class="regular-radio emailRepeat" onclick="replaceTo($(this));">
                    <label for="emailRepeat_replace_${repeat1Status.index }_${repeatsStatus.index }"></label>
                    <div class="message_list_check ">
                      <div class="message_list_check w425">
                        <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                          src="${nameRepeat1.avatars }" alt=""></a>
                        <div>
                          <p class=" fg">
                            <a href="#" class="message_data"> <c:if test="${lang eq 'en' }">${emailRepeat1.enName }</c:if>
                              <c:if test="${lang eq 'zh' }">${emailRepeat1.name }</c:if>
                            </a> <span>${emailRepeat1.position }</span>
                          </p>
                          <p class="monicker">
                            <b></b>${emailRepeat1.unitName }</p>
                          <p class="evaluate database_evaluate">
                            <i class="icon-new"></i> ${emailRepeat1.email }
                          </p>
                        </div>
                      </div>
                      <!--  替换为 -->
                      <div class="sie_message_list_check " style="display: none;">
                        <div class="message_database_newly orange">
                          <s:text name="user.adduser.replaceuser" />
                        </div>
                        <div class="message_list_check ">
                          <a class="message_ask_portrait" href="#"><img class="sie_list_potytait"
                            src="${nameRepeat1.avatars }" alt=""></a>
                          <div>
                            <p class=" fg">
                              <a href="#" class="message_data newName"> </a> <span class="newPos"></span>
                            </p>
                            <p class="monicker newUnit">
                              <b></b>
                            </p>
                            <p class="evaluate database_evaluate">
                              <i class="icon-new"></i> ${emailRepeat1.email }
                            </p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:if>
              </c:forEach>
            </div>
          </c:forEach>
        </c:if>
        <!-- 其他邮件地址重复 -->
        <c:if test="${otherEmailRepeatListSize > 0}">
          <c:set var="needNextStep" value="1"></c:set>
          <p class="database_reminder mt40 ">
            <b class="icon_hint_bt"></b>
            <s:text name="rolPersonConfig.tip.wrong.msg2" />
          </p>
          <c:forEach items="${otherEmailRepeatList}" var="otherEmailRepeats" varStatus="repeatsStatus">
            <div class="otherEmailRepeat_block">
              <div class="message_ask message_list btop">
                <div class="message_database_newly">
                  <s:text name="rolPersonConfig.tip.msg3" />
                </div>
                <input type="hidden" class="email" value="${otherEmailRepeats.email }" /> <input type="hidden"
                  class="input_isNeedMail" value="${otherEmailRepeats.isNeedMail }" /> <input type="hidden"
                  class="input_unitName" value="${otherEmailRepeats.unitName }" /> <input type="hidden"
                  class="input_unitId" value="${otherEmailRepeats.unitId }" /> <input type="hidden"
                  class="input_position" value="${otherEmailRepeats.position }" /> <input type="hidden"
                  class="input_posId" value="${otherEmailRepeats.posId }" /> <input type="hidden"
                  class="input_firstName" value="${otherEmailRepeats.firstName }" /> <input type="hidden"
                  class="input_lastName" value="${otherEmailRepeats.lastName }" /> <input type="hidden"
                  class="input_enName" value="${otherEmailRepeats.enName }" /> <input type="hidden" class="input_name"
                  value="${otherEmailRepeats.name }" /> <input type="hidden" class="input_email"
                  value="${otherEmailRepeats.email }" />
                <div class="message_list_check ">
                  <input type="radio" name="otherEmailRepeat_${repeatsStatus.index }"
                    id="otherEmailRepeat_yes_${repeatsStatus.index }" value="${otherEmailRepeats.psnId }"
                    class="regular-radio_3 otherEmailRepeat" checked="checked"> <label
                    for="otherEmailRepeat_yes_${repeatsStatus.index }"></label> <span class="mr20"><s:text
                      name="rolPersonAdd.form.label.yes" /></span> <input type="radio"
                    name="otherEmailRepeat_${repeatsStatus.index }" id="otherEmailRepeat_no_${repeatsStatus.index }"
                    value="0" class="regular-radio_3 ml20 otherEmailRepeat"> <label
                    for="otherEmailRepeat_no_${repeatsStatus.index }"></label> <span class="mr32"><s:text
                      name="rolPersonAdd.form.label.no" /></span> <a class="message_ask_portrait" href="#"> <c:if
                      test="${!empty otherEmailRepeats.avatars  }">
                      <img class="sie_list_potytait" src="${otherEmailRepeats.avatars }" alt="">
                    </c:if> <c:if test="${empty otherEmailRepeats.avatars  }">
                      <img class="sie_list_potytait" src="${ressie}/images/head_nan_photo.jpg" alt="">
                    </c:if>
                  </a>
                </div>
                <div>
                  <p class="fg">
                    <a href="#" class="message_data">${otherEmailRepeats.name }</a> <span>${otherEmailRepeats.position }</span>
                  </p>
                  <p class="monicker">
                    <b></b> ${otherEmailRepeats.unitName }
                  </p>
                  <p class="evaluate database_evaluate">
                    <i class="icon-new"></i> ${otherEmailRepeats.email }
                  </p>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:if>
      </div>
    </div>
  </div>
  <script type="text/javascript">
var locale='${locale}';
        
        function goNextStep() {
            $.ajax( {
                url :'/psnweb/person/ajaxtimeout',
                type : 'post',
                dataType : 'json',
                data : {    },
                success : function(data) {  
                    if (data.result != "success" && data.ajaxSessionTimeOut == "yes"){
                        /* 超时 刷新页面 */
                        window.location.reload();
                    }else{              
                        $.ajax({
                            url : '/psnweb/person/ajaxnextstep',
                            type : 'post',
                            dataType : 'json',
                            data : {},
                            success : function(data) {
                                //处理导入结果                            
                            },
                            error : function() {                            
                            }
                        });
                        //此函数中的内容要请求AjaxNextIptPsn后延迟2秒执行，否则在火狐中会报错
                        setTimeout(afterAjaxNextIptPsn,2000);
                    }
                }
            });
            
        }
        
        //此函数中的内容要请求AjaxNextIptPsn后延迟2秒执行，否则在火狐中会报错
        function afterAjaxNextIptPsn(){
            var nameRepeats = collectNameRepeat();
            var emailRepeats = collectEmailRepeat();
            var otherEmailRepeats = collectOtherEmailRepeat();
            //如果没有选中任何重复记录的信息或忽略重复记录，则提交后台执行页面的跳转(因导入失败结果不显示下一步，因此进入本判断但无success_add_block记录的均认为是无导入人员)_MaoJianGuo_2012-12-24_ROL-313.
            if (nameRepeats.length == 0 && emailRepeats.length == 0
                    && otherEmailRepeats.length == 0) {                
                //添加类型为导入添加.
                if ($("#success_add_block").size() > 0 || $("#addSuccessListLength").val() > 0) {
                    location.href = "/psnweb/person/importfinsh?message=importSuccess&addSuccessListLength="+$("#addSuccessListLength").val();
                }else {
                    location.href = "/psnweb/person/importfinsh?message=no_psn&addSuccessListLength=0";
                }
                return;
            }
            
            //如果当前选中的记录包含重复信息的人员记录，则对重复的人员记录进行相应操作.
            var repeat_data = {
                'nameRepeats' : nameRepeats,
                'emailRepeats' : emailRepeats,
                'otherEmailRepeats' : otherEmailRepeats,
            };
            var sucvalStr = $.trim( $("#addSuccessListLength").val());
            var sucval = 0;
            if( sucvalStr != ""){
            	sucval=parseInt($("#addSuccessListLength").val());
            }
            var sucSize=nameRepeats.length + otherEmailRepeats.length + emailRepeats.length + sucval;
            $("#addSuccessListLength").attr("value", sucSize);
            var repeatStr = $.toJSON(repeat_data);
            var post_data = {
                'repeatStr' : repeatStr                
            };
            $.ajax({
                        url : '/psnweb/person/ajaxsavenextpsn',
                        type : 'post',
                        dataType : 'json',
                        data : post_data,
                        success : function(data) {
                        	location.href = "/psnweb/person/importfinsh?message=importSuccess&addSuccessListLength="+$("#addSuccessListLength").val();
                        },
                        error : function() {
                            $.scmtips.show('error','<s:text name="rolPersonAdd.submit.error" />');
                        }
                    });
        }
        //检查是否有名称重复的人员记录被选中.
        function collectNameRepeat() {
            var nameRepeats = [];
            $(".nameRepeat_block").each(function() {
                var psnId = $(this).find(".nameRepeat:checked").val();
                
                if($("#isGoBack").val() == "1"){ // 点击“返回”按钮不导入人员
                    
                } else{
                    var position = $(this).find(".input_position").val();
                    var posId = $(this).find(".input_posId").val();
                    var unitId = $(this).find(".input_unitId").val();
                    var unitName = $(this).find(".input_unitName").val();  
                    var name = $(this).find(".input_name").val();
                    var firstName = $(this).find(".input_firstName").val();
                    var lastName = $(this).find(".input_lastName").val();
                    var enName = $(this).find(".input_enName").val();
                    var isNeedMail = $(this).find(".input_isNeedMail").val();
                    var email = $(this).find(".input_email").val();
                    if(enName == ""){
                        enName = firstName + " " + lastName;
                    }
                    nameRepeats.push({
                        'psnId' : psnId,
                        'unitId' : unitId,
                        'unitName': unitName,
                        'position' : position,
                        'posId' : posId,
                        'name' : name,
                        'firstName' : firstName,
                        'lastName' : lastName,
                        'enName' : enName,
                        'email' : email,
                        'isNeedMail' : isNeedMail
                    });
                }               
            });
            return nameRepeats;
        }
        //检查是否有Email重复的人员记录被选中.
        function collectEmailRepeat() {
            var emailRepeats = [];
            $(".emailRepeat_block").each(function() {
                var psnId = $(this).find(".emailRepeat:checked").val();
                if($("#isGoBack").val() == "1"){ // 点击“返回”按钮不导入人员
                    
                } else{
                	if (psnId != 0) {
                        var unitId = $(this).find(".input_unitId").val();
                        var unitName = $(this).find(".input_unitName").val();  
                        var position = $(this).find(".input_position").val();
                        var posId = $(this).find(".input_posId").val();
                        var isNeedMail=$(this).find(".input_isNeedMail").val();
                        var repeatMail=$("#repeatMail").val();
                        var name = $(this).find(".input_name").val();
                        var firstName = $(this).find(".input_firstName").val();
                        var lastName = $(this).find(".input_lastName").val();
                        var enName = $(this).find(".input_enName").val();
                        if(enName == ""){
                            enName = firstName + " " + lastName;
                        }
                        emailRepeats.push({
                            'psnId' : psnId,
                            'unitId' : unitId,
                            'unitName': unitName,
                            'position' : position,
                            'name' : name,
                            'firstName' : firstName,
                            'lastName' : lastName,
                            'enName' : enName,
                            'posId' : posId,
                            'isNeedMail' : isNeedMail,
                            'repeatMail' : repeatMail
                        });
                    }
                
                }
                
            });
            return emailRepeats;
        }
        //检查是否有其他Email重复的人员记录被选中.
        function collectOtherEmailRepeat() {
            var otherEmailRepeats = [];
            $(".otherEmailRepeat_block").each(function() {
                var radio = $(this).find(".otherEmailRepeat:checked");
                var psnId = radio.val();
                var position = $(this).find(".input_position").val();
                if (psnId != 0) {
                    var email = $(this).find(".email").val();                    
                    var isNeedMail=$(this).find(".input_isNeedMail").val();
                    var position = $(this).find(".input_position").val();
                    var posId = $(this).find(".input_posId").val();
                    var unitId = $(this).find(".input_unitId").val();
                    var name = $(this).find(".input_name").val();
                    var firstName = $(this).find(".input_firstName").val();
                    var lastName = $(this).find(".input_lastName").val();
                    var enName = $(this).find(".input_enName").val();
                    var unitName = $(this).find(".input_unitName").val();                    
                    otherEmailRepeats.push({
                        'psnId' : psnId,
                        'unitId' : unitId,
                        'position' : position,
                        'posId' : posId,
                        'name' : name,
                        'firstName' : firstName,
                        'lastName' : lastName,
                        'enName' : enName,
                        'email' : email,
                        'isNeedMail' : isNeedMail,
                        'unitName' : unitName
                        });
                }
            });
            return otherEmailRepeats;
        }
        //返回时提醒是否放弃所有操作
        function goBack() {
            $("#isGoBack").val("1");   //点击的是返回按钮
            if('${needNextStep}' > 0){
                if(locale == 'zh_CN'){
                    var tip = "查重数据还未被处理，确认放弃此次导入操作？"
                }else{
                    var tip = "Duplicated input data has not been handled yet, are you sure to return?"
                }   
                var option={
                        'screentxt':tip ,
                        'screencallback':goBackCallbackYes,
                        'screencancelcallback':goBackCallbackNo
               };
               popconfirmbox(option);
            }else{
                goNextStep();
            }
            
        }
        
        function goBackCallbackYes(){
        	 //导入人员，使用邮件已被注册，勾选“邀请加入本单位”后点返回，不添加该人员
            $("input[type=radio][name^='otherEmailRepeat_'][value=0]").attr("checked","checked");
            goNextStep();
        }
        
        function goBackCallbackNo(){
        	 return;
        }
        
        
        $(document).ready(function(){
            setNewPsnInfo();
        });
            
        function replaceTo(selectedRadio){
            var selectTr = selectedRadio.parent();
            selectTr.parent().find(".sie_message_list_check").hide();
            selectTr.find(".sie_message_list_check").show();
        }
        
        function setNewPsnInfo(){
            $(".emailRepeat_block").each(function(){
                var name = $(this).find(".input_name").val();
                var firstName = $(this).find(".input_firstName").val();
                var lastName = $(this).find(".input_lastName").val();
                var enName = $(this).find(".input_enName").val();
                var position = $(this).find(".input_position").val();
                var unitName = $(this).find(".input_unitName").val();
                if(locale == 'zh_CN'){
                    $(this).find(".newName").html(name);
                }else{
                    $(this).find(".newName").html(firstName + "&nbsp;" + lastName);
                }               
                
                $(this).find(".newPos").html(position);
                $(this).find(".newUnit").append("<b></b>");
                $(this).find(".newUnit").append(unitName);
                
            });
            
            $(".nameRepeat_block").each(function(){
                var name = $(this).find(".input_name").val();
                var firstName = $(this).find(".input_firstName").val();
                var lastName = $(this).find(".input_lastName").val();
                var enName = $(this).find(".input_enName").val();
                var position = $(this).find(".input_position").val();
                var unitName = $(this).find(".input_unitName").val();
                if(locale == 'zh_CN'){
                    $(this).find(".newName").html(name);
                }else{
                    $(this).find(".newName").html(firstName + "&nbsp;" + lastName);
                }
                $(this).find(".newPos").html(position);
                $(this).find(".newUnit").append("<b></b>");
                $(this).find(".newUnit").append(unitName);
            });
        }
        
 
        
    </script>
</body>
</html>
