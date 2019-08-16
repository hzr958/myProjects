<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function changeInvoicesType(invoicesType, obj){
  $(".invoices_type_item").removeClass("new-invoice_pay-title_item-selected");
  $(obj).addClass("new-invoice_pay-title_item-selected");
  if(invoicesType == "normal"){
    $(".increment_invoices").hide();
    $(".normal_invoices").show();
    $("#invoices_type").val("1");
    $("#invoices_title_error_msg_text").text("抬头不能为空");
  }else{
    $(".increment_invoices").show();
    $(".normal_invoices").hide();
    $("#invoices_type").val("2");
    $("#invoices_title_error_msg_text").text("单位名称不能为空");
  }
}

</script>
<div id="validate_pay_invoices_info_div" style="display:none;">
<input type="hidden" name="invoicesType" id="invoices_type" value=""/>
    <div class="new-invoice_pay-title">
        <div class="new-invoice_pay-title_item invoices_type_item" onclick="changeInvoicesType('normal', this);" id="normal_invoices_div_title">普通发票</div>
        <div class="new-invoice_pay-title_item new-invoice_pay-title_item-selected invoices_type_item" onclick="changeInvoicesType('increment', this);">增值税专用发票</div>
    </div>
    <div class="new-invoice_pay-body">
        <div class="new-invoice_pay-body_item" id="increment_invoices_info">
            <div class="new-invoice_payitem" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title increment_invoices">单位名称:</span>
                    <span class="new-invoice_payitem-left_title normal_invoices" style="display:none;">抬头:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="100" class="new-invoice_payitem-right_input invoices_input" name="title" id="invoices_title" oninput="ValidatePay.checkInvoicesInfo('title');">
                </div>
                <div class="error_message-prompt error_message-rightprompt invoices_input_msg" id="invoices_title_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail" id="invoices_title_error_msg_text">单位名称不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
            <div class="new-invoice_payitem" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title">税号:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="100" class="new-invoice_payitem-right_input invoices_input" name="uniformId" id="invoices_uniform_id" oninput="ValidatePay.checkInvoicesInfo('uniform_id');">
                </div>
                <div class="error_message-prompt error_message-rightprompt invoices_input_msg" id="invoices_uniform_id_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail">税号不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
            <div class="new-invoice_payitem increment_invoices" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title ">开户银行:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="100" class="new-invoice_payitem-right_input invoices_input" name="bank" id="invoices_bank" oninput="ValidatePay.checkInvoicesInfo('bank');">
                </div>
                <div class="error_message-prompt error_message-rightprompt invoices_input_msg" id="invoices_bank_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail">开户银行不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
            <div class="new-invoice_payitem increment_invoices" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title">银行账号:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="100" class="new-invoice_payitem-right_input invoices_input" name="bankNO" id="invoices_bankNO" oninput="ValidatePay.checkInvoicesInfo('bankNO');">
                </div>
                <div class="error_message-prompt error_message-rightprompt invoices_input_msg" id="invoices_bankNO_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail">银行账号不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
            <div class="new-invoice_payitem increment_invoices" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title">注册地址:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="100" class="new-invoice_payitem-right_input invoices_input" name="addr" id="invoices_addr" oninput="ValidatePay.checkInvoicesInfo('addr');">
                </div>
                <div class="error_message-prompt error_message-rightprompt invoices_input_msg" id="invoices_addr_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail">注册地址不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
            <div class="new-invoice_payitem increment_invoices" style="position: relative;">
                <div class="new-invoice_payitem-left">
                    <span class="new-invoice_payitem-left_icon red">*</span>
                    <span class="new-invoice_payitem-left_title">注册电话:</span>
                </div>
                <div class="new-invoice_payitem-right">
                    <input type="text" maxlength="50" class="new-invoice_payitem-right_input invoices_input" name="tel" id="invoices_tel" oninput="ValidatePay.checkInvoicesInfo('tel');">
                </div>
                <div class="error_message-prompt error_message-rightprompt  invoices_input_msg" id="invoices_tel_error_msg" style="display: none; right: -262px; top: 2px;">
                    <div class="error_message-prompt_side error_message-prompt_rightside">
                        <div class="error_message-prompt_detail">注册电话不能为空</div>
                        <div class="error_message-prompt_icon-right"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
