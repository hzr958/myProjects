<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${resmod }/css_v5/borain-timeChoice.css">
<link rel="stylesheet" type="text/css" href="${resmod }/css_v5/mail_manage.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/mail/borain-timeChoice.js"></script>
<script type="text/javascript" src="${resmod}/js/mail/mail_manage.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<%-- <link href="${resmod}/css/scmjscollection.css"	rel="stylesheet" type="text/css" /> --%>
<style type="text/css">
</style>
<script type="text/javascript">
  $(document).ready(function() {
    MailManage.init();
    var data = MailManage.getTime();
    $("#startSendDate").val(data["startDate"]);
    $("#endSendDate").val(data["endDate"]);
    $("#startCreateDate").val(data["startDate"]);
    $("#endCreateDate").val(data["endDate"]);
  });
</script>
<title>邮件管理</title>
</head>
<body>
  <div class="box_mail_manage">
    <div class="h1_mail_manage">
      <div class="item1_mail_manage">系统异常监控</div>
    </div>
    <div class="h1_mail_manage">
      <div class="item1_mail_manage item_checked dev_item" from_id="content1">邮件列表日记</div>
      <div class="item1_mail_manage dev_item" from_id="content2">异常行为</div>
      <div class="item1_mail_manage dev_item" from_id="content3">敏感词过滤</div>
      <div class="item1_mail_manage dev_item" from_id="content4">退信记录</div>
      <div class="item1_mail_manage dev_item" from_id="content5">模板记录</div>
      <div class="item1_mail_manage dev_item" from_id="content6">发送账号</div>
      <div class="item1_mail_manage dev_item" from_id="content7">发送客户端</div>
      <div class="item1_mail_manage dev_item" from_id="content8">黑名单</div>
      <div class="item1_mail_manage dev_item" from_id="content9">白名单</div>
      <div class="item1_mail_manage dev_item" from_id="content10">每天发送统计记录</div>
    </div>
    <div class="content" id="content10" style="display: none;">
      <!-- 每天发送统计记录-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
              统计日期：<input class="search_mail_manage_input startStatisticsDate" readonly="readonly" type="text" name="startStatisticsDate"
                id="startStatisticsDate">——<input class="search_mail_manage_input endStatisticsDate" type="text" readonly="readonly"
                name="endStatisticsDate" id="endStatisticsDate">
            </div>
          </div>
          <div class="item2">
          <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
        </div>
      </div>
      </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_statistics_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_statistics_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content9" style="display: none;">
      <!-- 白名单-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
             邮箱：<input type="text" name="whiteEmail">
            </div>
            <div class="item2_mail_manage">
              状态： <select id="whiteStatus">
                <option value="" selected="selected">全部</option>
                <option value="0">启用</option>
                <option value="1">关闭</option>
              </select>
            </div>
          </div>
          <div class="item2">
          <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
        </div>
      </div>
      </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_white_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_white_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content8" style="display: none;">
      <!-- 黑名单-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
             邮箱：<input type="text" name="blackEmail">
            </div>
            <div class="item2_mail_manage">
              状态： <select id="blackStatus">
                <option value="" selected="selected">全部</option>
                <option value="0">启用</option>
                <option value="1">关闭</option>
              </select>
            </div>
          </div>
          <div class="item2">
          <div class="item2_mail_manage">
              类别： <select id="blackType">
                <option value="" selected="selected">全部</option>
                <option value="0">邮箱</option>
                <option value="1">域名</option>
              </select>
            </div>
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_black_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_black_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content7" style="display: none;">
      <!-- 发送客户端-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
             客户端名字：<input type="text" name="clientName">
            </div>
            <div class="item2_mail_manage">
              状态： <select id="clientStatus">
                <option value="" selected="selected">全部</option>
                <option value="0">可用</option>
                <option value="1">不可用</option>
              </select>
            </div>
          </div>
          <div class="item2">
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_client_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_client_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content6" style="display: none;">
      <!-- 发送账号-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
              账号：<input type="text" name="account">
            </div>
            <div class="item2_mail_manage">
              状态： <select id="sendStatus">
                <option value="" selected="selected">全部</option>
                <option value="0">可用</option>
                <option value="1">不可用</option>
                <option value="88">管理员通知账号</option>
                <option value="9">超过发送限制</option>
              </select>
            </div>
          </div>
          <div class="item2">
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_sender_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_sender_table" width="100%" style="table-layout: fixed">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content5" style="display: none;">
      <!-- 模板记录-->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
              模板标识：<input type="text" name="templateCode">
            </div>
            <div class="item2_mail_manage">
              模板名字：<input type="text" name="templateName">
            </div>
          </div>
          <div class="item2">
            <div class="item2_mail_manage">
              主题：<input type="text" name="subject">
            </div>
            <div class="item2_mail_manage">
              可用状态： <select id="status">
                <option value="">全部</option>
                <option value="0" selected="selected">可用</option>
                <option value="1">不可用</option>
              </select>
            </div>
          </div>
          <div class="item3">
            <div class="item2_mail_manage">
              限制状态： <select id="limitStatus">
                <option value="">全部</option>
                <option value="0" selected="selected">无限制</option>
                <option value="1">每天对一个邮箱发一封</option>
                <option value="2">每周对一个邮箱发一封</option>
                <option value="3">每月对一个邮箱发一封</option>
              </select>
            </div>
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title" id="show_tem_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_template_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content4" style="display: none;">
      <!-- 退信记录 -->
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
              收件人邮箱：<input type="text" name="mailTemplateName">
            </div>
            <div class="item2_mail_manage">
              发件人邮箱：<input type="text" name="receiver">
            </div>
          </div>
          <div class="item2">
            <div class="item2_mail_manage">
              发送时间：<input class="search_mail_manage_input startSendDate" readonly="readonly" type="text"
                name="startSendDate" id="startSendDate">——<input class="search_mail_manage_input endSendDate"
                type="text" readonly="readonly" name="endSendDate" id="endSendDate">
            </div>
          </div>
          <div class="item3">
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage show_title"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_teturn_table">
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount"
            class="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
    <div class="content" id="content3" style="display: none;">
      <!-- 敏感词过滤 -->
    </div>
    <div class="content" id="content2" style="display: none;">
      <!-- 异常行为 -->
    </div>
    <div class="content" id="content1">
      <div class="search_mail_manage">
        <div class="center">
          <div class="item1">
            <div class="item2_mail_manage">
              邮件模版名：<input type="text" name="mailTemplateName" id="mailTemplateName">
            </div>
            <div class="item2_mail_manage">
              收件人邮箱：<input type="text" name="receiver" id="receiver">
            </div>
            <div class="item2_mail_manage">
              收件人ID：<input type="text" name="receiverPsnId" id="receiverPsnId">
            </div>
          </div>
          <div class="item2">
            <!-- <div class="item2_mail_manage">
						触发类型：<input type="text" name="type" id="type">
					</div> -->
            <div class="item2_mail_manage">
              发送时间：<input class="search_mail_manage_input startDate" readonly="readonly" type="text" name="startDate"
                id="startDate">——<input class="search_mail_manage_input endDate" type="text" readonly="readonly"
                name="endDate" id="endDate">
            </div>
            <div class="item2_mail_manage">
              邮件状态： <select id="status_filter">
                <option value="">不限</option>
                <option value="to_be_construct">待构造邮件</option>
                <option value="construct_error">构造失败</option>
                <option value="frequency_limit">模版频率限制</option>
                <option value="receive_refuse">用户不接收此类邮件</option>
                <option value="to_be_distributed">待分配</option>
                <option value="to_be_sent">待发送</option>
                <option value="send_successfully">发送成功</option>
                <option value="blacklist">黑名单</option>
                <option value="receiver_inexistence">收件箱不存在</option>
                <option value="information_lock">信息被锁定</option>
                <option value="send_error">发送出错</option>
                <option value="scheduling_error">调度出错</option>
                <option value="sending">邮件正在发送</option>
                <option value="build_send_error">构造邮件发送信息出错</option>
              </select>
            </div>
            <div class="item2_mail_manage">
              发件人ID：<input type="text" name="senderPsnId" id="senderPsnId">
            </div>
          </div>
          <div class="item3">
            <div class="item2_mail_manage">
              排序： <select id="orderBy">
                <option value="mailId" selected="selected">序号</option>
                <option value="updateDate">最新发送时间</option>
              </select>
            </div>
             <div class="item2_mail_manage">
              发件邮箱：<input type="text" name="sender" id="sender">
            </div>
            <div class="item2_mail_manage">
              <button class="search_btn dev_mail_cancle">清空</button>
              <button class="search_btn dev_mail_search">检索</button>
            </div>
          </div>
        </div>
      </div>
      <div class="h1_mail_manage">
        <div class="item2_mail_manage" id="show_total"></div>
      </div>
      <div class="listbox_mail_manage">
        <table id="mail_table">
          <tr>
            <th>序号</th>
            <th>邮件模版</th>
            <th>邮件标题</th>
            <th>收件人ID</th>
            <th>收件人邮箱</th>
            <th>发件邮箱</th>
            <th>发送时间</th>
            <th>状态</th>
            <th>触发操作类型</th>
            <th>操作</th>
          </tr>
        </table>
      </div>
      <div class="h1_mail_manage">
        <div class="mail_manage_page">
          <span>当前页数：<input type="text" value="1" name="currentCount" disabled="disabled"></span> <span>每页数量：<input
            type="text" value="10" name="pageSize"></span> <span>总页数：<input type="text" value="1"
            name="totalPages" disabled="disabled"></span> <span><input type="text" name="skipCount">
            <button class="btn_skipCount">跳转</button></span> <span><button class="first_page">上一页</button>
            <button class="next_page">下一页</button></span>
        </div>
      </div>
    </div>
  </div>
  <div id="fade" class="black_overlay"></div>
  <div id="my_div" class="white_content">
    <div style="text-align: right; cursor: default; height: 40px;">
      <span style="font-size: 16px;" id="close_div">关闭</span>
    </div>
    <div class="supernatant_box">
      <table id="mail_link_table">
        <tr>
          <th>序号</th>
          <th>url</th>
          <th>描述</th>
          <th>已访问数量</th>
        </tr>
      </table>
    </div>
  </div>
  <div id="my_div4" class="white_content">
    <div style="text-align: right; cursor: default; height: 40px;">
      <span style="font-size: 16px;" id="close_div4">关闭</span>
    </div>
    <div class="supernatant_box"></div>
  </div>
  <div id="my_div5" class="white_content">
    <div style="text-align: right; cursor: default; height: 40px;">
      <span style="font-size: 16px;" id="close_div5">关闭</span>
    </div>
    <div class="supernatant_box">
      <div>
        发送时间：<input type="text" name="startCreateDate" id="startCreateDate">——<input type="text"
          name="endCreateDate" id="endCreateDate">
        <button class="search_btn dev5_cancle" id="dev5_cancle">清空</button>
        <button class="search_btn dev5_search" id="dev5_search">检索</button>
      </div>
      <table id="link_sum_table">
      </table>
    </div>
  </div>
</body>
</html>