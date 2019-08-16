<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8">
<title>科研验证</title>
<link rel="stylesheet" href="${ressie }/css/appy.css" />
<link rel="stylesheet" href="${ressie}/css/plugin/toast.css" />
<script type="text/javascript" src="${ressie}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ressie}/js/jquery.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/smate.toast.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/scm-pc_filedragbox.js"></script>
<script type="text/javascript" src="${resapp }/validate/validate.form.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/jquery-qrcode.min.js"></script>
<script type="text/javascript" src="${ressie }/js/plugin/sie-base-utils.js"></script>
<script type="text/javascript" src="${resapp }/validate/validate.pay.js"></script>
<script type="text/javascript">
var intervalId;
window.onload = function(){
  var checklist = document.getElementsByClassName("new-paymentpage_Accountitem-item_check");
  var showele = document.getElementsByClassName("new-paymentpage_Accountappy-wechatpay")[0];
  var payele = document.getElementsByClassName("new-paymentpage_container")[0];
  var closeele = document.getElementsByClassName("new-paymentpage_header-close")[0];
  showele.onclick = function(){
    ValidatePay.changeTradeType("wechat", this);
  }
  window.onresize = function(){
    payele.style.left = (window.innerWidth - payele.offsetWidth)/2 + "px";
    payele.style.top = (window.innerHeight - payele.offsetHeight)/2 + "px";
  }
  for(var i = 0; i < checklist.length; i++){
  checklist[i].onclick = function(){
      if(document.getElementsByClassName("new-paymentpage_Accountitem-item_checked").length > 0){
          document.getElementsByClassName("new-paymentpage_Accountitem-item_checked")[0].classList.remove("new-paymentpage_Accountitem-item_checked");
      }
      if(this.classList.contains("new-paymentpage_Accountitem-item_checked")){
          this.classList.remove("new-paymentpage_Accountitem-item_checked")
      }else{
          this.classList.add("new-paymentpage_Accountitem-item_checked")
      }
      var payGrade = $.trim($(this).attr("pay_grade"));
      $("#pay_grade").val(payGrade);
      ValidatePay.changeSelectedGradeInfo(payGrade);
    }
  }
  
  $("#smate_pay_grade_C").click();
  if ($("#need_Invoices").is(':checked')) {
    ValidatePay.changeNeedInvoices();
  }
}


</script>
</head>
<body>
  <form name="mainForm" id="ali_pay_form" action="/application/alipay/pre" method="post" enctype="multipart/form-data">
    <input type="hidden" id="orderNum" value=""/>
    <input type="hidden" id="pay_grade" name="grade" value=""/>
    <input type="hidden" id="selected_trade_type" name="selected_trade_type" value=""/>
    <header>
    <div class="header__2nd-nav">
      <div class="header__2nd-nav_box" style="justify-content: flex-end;">
        <nav class="nav_horiz nav_horiz-container" style="margin-left: 944px; top: 0px;">
        <ul class="nav__list" scm_file_id="menu__list">
          <li class="nav__item item_selected" onclick="Validate.backList();">科研验证</li>
        </ul>
        <div class="nav__underline" style="width: 75px; left: 9px;"></div>
        </nav>
        <div class="header__2nd-nav_action-list">
          <a href="###" style="margin-right: 0px;"></a>
        </div>
      </div>
    </div>
    </header>
    <div class="new-normalpage_container" style="margin-top: 100px; margin-bottom: 250px;">
        <!-- 套餐选择  -->
        <div class="new-paymentpage_Accountitem">
            <div class="new-paymentpage_Accountitem-title">立刻成为科研之友会员，使用科研验证服务</div>
            <div class="new-paymentpage_Accountitem-Category">
                <div class="new-paymentpage_Accountitem-item">
                    <i class="new-paymentpage_Accountitem-item_check" pay_grade="C" id="smate_pay_grade_C"></i>
                    <span class="new-paymentpage_Accountitem-item_detail">1个月 CNY 98元</span>
                </div>
            
                <div class="new-paymentpage_Accountitem-item"  style="margin: 0px 148px;">
                    <i class="new-paymentpage_Accountitem-item_check" pay_grade="A" id="smate_pay_grade_A"></i>
                    <span class="new-paymentpage_Accountitem-item_detail">12个月 CNY 998元</span>
                </div>

                <div class="new-paymentpage_Accountitem-item">
                    <i class="new-paymentpage_Accountitem-item_check" pay_grade="B" id="smate_pay_grade_B"></i>
                    <span class="new-paymentpage_Accountitem-item_detail">24个月 CNY 1800元</span>
                </div>

            </div>
            <div class="new-paymentpage_Accountitem-explain">
                <span class="new-paymentpage_Accountitem-explain_detail">VIP账号有效期至<span id="vip_end_date"></span></span>
            </div>
        </div>
        <!-- 支付方式选择 -->
        <div class="new-paymentpage_Accountitem">
            <div class="new-paymentpage_Accountitem-title">请选择快捷支付</div>
            <div class="new-paymentpage_Accountitem-Category">
                <div class="new-paymentpage_Accountappy-item new-paymentpage_Accountappy-alipay validate_trade_type" style="margin: 0px 80px 0px 0px;" onclick="ValidatePay.changeTradeType('alipay', this);"></div>
                <div class="new-paymentpage_Accountappy-item new-paymentpage_Accountappy-wechatpay validate_trade_type"></div>
            </div>
            <div class="new-paymentpage_Accountitem-item" style="justify-content: flex-start; margin-left: 30px;">
                    <input type="checkbox" name="needInvoices" id="need_Invoices" value="" onclick="ValidatePay.changeNeedInvoices();">
                    <span class="new-paymentpage_Accountitem-item_detail">需要开具发票</span>
            </div>
        </div>
        <!-- 发票信息 -->
        <%@ include file="pay_invoices_info.jsp" %>
        <div class="new-invoice_pay-func">
            <div class="new-invoice_pay-func_paynow" onclick="ValidatePay.payNow();" id="pay_submit_btn">立即支付</div>
            <div class="new-invoice_pay-func_return" onclick="ValidatePay.back();">返回</div>
        </div>
    </div>


  <div class="normal-background_cover" id="wechat_order_background_cover">
    <div class="new-paymentpage_container" id="wechat_order_container">
        <div class="new-paymentpage_header">
          <div class="new-paymentpage_header-title"> 
              <i class="new-paymentpage_header-icon"></i>
              <span class="new-paymentpage_header-paytitle"> 微信支付</span> 
              <span class="new-paymentpage_header-Cashier">收银台</span>
          </div>
          <i class="new-paymentpage_header-close list-results_close" onclick="ValidatePay.closeWeChatOrder();"></i>
        </div>

        <div class="new-paymentpage_pay-first" id="wechat_order_info"  style="display:flex;">
            <div class="new-paymentpage_pay-first_left">
              <div class="new-paymentpage_pay-first_left-header">订单会在2小时之后失效，请及时支付</div>
              <div class="new-paymentpage_pay-first_left-body" id="wechat_pay_qrcode"></div>
              <div class="new-paymentpage_pay-first_left-footer">打开手机微信扫一扫</div>
            </div>
            <div class="new-paymentpage_pay-first_line"></div>
            <div class="new-paymentpage_pay-first_right">
              <div class="new-paymentpage_pay-first_right-title" >科研之友订单</div>
              <div class="new-paymentpage_pay-first_right-num">
                <span class="new-paymentpage_pay-first_right-num_tip" >CNY</span>
                <span class="new-paymentpage_pay-first_right-num_detail" id="wechat_order_price"></span>
              </div>
              <div class="new-paymentpage_pay-first_right-item" >
                <span class="new-paymentpage_pay-first_right-item_title" >收款方：</span>
                <span class="new-paymentpage_pay-first_right-item_detail">深圳市科研之友网络服务有限公司</span>
              </div>
              <div class="new-paymentpage_pay-first_right-item" >
                <span class="new-paymentpage_pay-first_right-item_title">下单时间：</span>
                <span class="new-paymentpage_pay-first_right-item_detail" id="wechat_order_create_time"></span>
              </div>
              <div class="new-paymentpage_pay-first_right-item"> 
                <span class="new-paymentpage_pay-first_right-item_title">订单号：</span>
                <span class="new-paymentpage_pay-first_right-item_detail" id="wechat_order_NO"></span>
              </div>
              <div class="new-paymentpage_pay-first_right-item"> 
                <span class="new-paymentpage_pay-first_right-item_title">商品名称：</span>
                <span class="new-paymentpage_pay-first_right-item_detail" id="wechat_order_name"></span>
              </div>
              <div class="new-paymentpage_pay-first_right-item"  style=" margin-bottom: 100px;"> 
                <span class="new-paymentpage_pay-first_right-item_title">商品描述：</span>
                <span class="new-paymentpage_pay-first_right-item_detail" id="wechat_order_desc"></span>
              </div>
            </div>
        </div>

        
        <div class="new-paymentpage_pay-second" id="wechat_pay_success" style="display:none;">
            <i class="new-paymentpage_paysuccess—icon"></i>
            <div class="new-paymentpage_paysuccess—tip">
              支付成功
            </div>
            <div class="new-paymentpage_paysuccess—returnlart">
              <span id="back_to_validate_time">5</span>秒钟后进入提交页面
            </div>
            <div class="new-paymentpage_paysuccess—returnnow" onclick="ValidatePay.backToValidateList();">
              立即进入 
            </div>
        </div>

    </div>
  </div>
  </form>
</body>
</html>