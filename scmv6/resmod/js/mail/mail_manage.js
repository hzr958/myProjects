/**
 * 邮件管理js zzx
 */
var MailManage={
		loadStateIco:function($obj,addWay){
			if(addWay==null||addWay==""){
				addWay="html";
			}
			$obj.doLoadStateIco({
				"style" : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
				"status" : 1,
				"addWay":addWay
			});
		},
		switchItem:function(obj){
			$(".content").hide();
			$(".dev_item").removeClass("item_checked");
			obj.addClass("item_checked");
			var from_id = obj.attr("from_id");
			if("content1"==from_id){
				var data = MailManage.getParam();
				MailManage.findMailManageList(data);
			}else if ("content2"==from_id) {
				
			}else if ("content3"==from_id) {
				
			}else if ("content4"==from_id) {
				var data = MailManage.getReturnParam();
				MailManage.findMailReturnList(data);
			}else if("content5"==from_id){
				var data = MailManage.getTemplateParam();
				MailManage.findTemplateList(data);
			}else if("content6"==from_id){
				var data = MailManage.getSenderParam();
				MailManage.findSenderList(data);
			}else if("content7"==from_id){
        var data = MailManage.getClientParam();
        MailManage.findClientList(data);
      }else if("content8"==from_id){
        var data = MailManage.getBlackParam();
        MailManage.findBlackList(data);
      }else if("content9"==from_id){
        var data = MailManage.getWhiteParam();
        MailManage.findWhiteList(data);
      }else if("content10"==from_id){
        var data = MailManage.getStatisticsParam();
        MailManage.findStatisticsList(data);
      }
			$("#"+obj.attr("from_id")).show();
		},
		findLinkList:function(data){
			MailManage.loadStateIco($("#mail_link_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/findlinklist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#mail_link_table").html(data);
				},
				error: function(){
				}
			});
		},
		findLinkSum:function(data){
			MailManage.loadStateIco($("#link_sum_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/findlinksumlist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#link_sum_table").html(data);
				},
				error: function(){
				}
			});
		},
		openMailDetail:function(obj){
			var $obj=$(obj);
			var des3MailId = $obj.attr("des3MailId");
			if(des3MailId!=null&&des3MailId!=""){
				window.open("/scmmanagement/mail/details?des3MailId="+des3MailId);
			}
		},
		closeDiv:function(){
			$("#my_div").hide();
			$(".borain-timeChoiceMask").hide();
		},
		closeDiv5:function(){
			$("#my_div5").hide();
			$(".borain-timeChoiceMask").hide();
		},
		showDiv:function(obj){
			$("#my_div").show();
			$(".borain-timeChoiceMask").show();
		},
		detailsItem:function(obj){
			MailManage.showDiv();
			var $obj=$(obj);
			var des3MailId = $obj.attr("des3MailId");
			var data ={"des3MailId":des3MailId};
			MailManage.findLinkList(data);
		},
		detailsLink:function(obj){
			$("#my_div5").show();
			$(".borain-timeChoiceMask").show();
			var $obj=$(obj);
			var data = {};
            data["templateCode"]=$obj.attr("templateCode");
            data["startCreateDateStr"]=$("#startCreateDate").val();
            data["endCreateDateStr"]=$("#endCreateDate").val();
			MailManage.findLinkSum(data);
		},
		detailsLinkSearch:function(){
			var data = {};
			data["templateCode"] = $("#divCode").val();
			data["startCreateDateStr"]=$("#startCreateDate").val();
            data["endCreateDateStr"]=$("#endCreateDate").val();
			MailManage.findLinkSum(data);
		},
		firstPageEvent:function(){
			var box1 = $("#content1");
			var currentCount = box1.find(".hidden_data").attr("currentCount");
			if(currentCount==1){
				return;
			}
			var data = MailManage.getParam();
			data["page.pageNo"]=Number(currentCount)-1;
			MailManage.findMailManageList(data);
		},
		nextPageEvent:function(){
			var box1 = $("#content1");
			var currentCount = box1.find(".hidden_data").attr("currentCount");
			var totalPage = box1.find(".hidden_data").attr("totalpages");
			if(currentCount==totalPage){
				return;
			}
			var data = MailManage.getParam();
			data["page.pageNo"]=Number(currentCount)+1;
			MailManage.findMailManageList(data);
		},
		pageSizeCheckInput5:function(){
			var box5 = $("#content5");
			var skipCount = $.trim(box5.find("input[name='pageSize']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=10;
			}
			var totalCount = box5.find(".hidden_data").attr("count");
			if(count>totalCount){
				count=totalCount;
			}
			if(count<=0){
				count=1;
			}
			if(count>100){
				count=100;
			}
			box5.find("input[name='pageSize']").val(count);
		},
		pageSizeCheckInput6:function(){
      var box6 = $("#content6");
      var skipCount = $.trim(box6.find("input[name='pageSize']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=10;
      }
      var totalCount = box6.find(".hidden_data").attr("count");
      if(count>totalCount){
        count=totalCount;
      }
      if(count<=0){
        count=1;
      }
      if(count>100){
        count=100;
      }
      box6.find("input[name='pageSize']").val(count);
    },
    pageSizeCheckInput7:function(){
      var box7 = $("#content7");
      var skipCount = $.trim(box7.find("input[name='pageSize']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=10;
      }
      var totalCount = box7.find(".hidden_data").attr("count");
      if(count>totalCount){
        count=totalCount;
      }
      if(count<=0){
        count=1;
      }
      if(count>100){
        count=100;
      }
      box7.find("input[name='pageSize']").val(count);
    },
    pageSizeCheckInput8:function(){
      var box8 = $("#content8");
      var skipCount = $.trim(box8.find("input[name='pageSize']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=10;
      }
      var totalCount = box8.find(".hidden_data").attr("count");
      if(count>totalCount){
        count=totalCount;
      }
      if(count<=0){
        count=1;
      }
      if(count>100){
        count=100;
      }
      box8.find("input[name='pageSize']").val(count);
    },
    pageSizeCheckInput9:function(){
      var box9 = $("#content9");
      var skipCount = $.trim(box9.find("input[name='pageSize']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=10;
      }
      var totalCount = box9.find(".hidden_data").attr("count");
      if(count>totalCount){
        count=totalCount;
      }
      if(count<=0){
        count=1;
      }
      if(count>100){
        count=100;
      }
      box9.find("input[name='pageSize']").val(count);
    },
    pageSizeCheckInput10:function(){
      var box10 = $("#content10");
      var skipCount = $.trim(box10.find("input[name='pageSize']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=10;
      }
      var totalCount = box10.find(".hidden_data").attr("count");
      if(count>totalCount){
        count=totalCount;
      }
      if(count<=0){
        count=1;
      }
      if(count>100){
        count=100;
      }
      box10.find("input[name='pageSize']").val(count);
    },
		pageSizeCheckInput:function(){
			var box1 = $("#content1");
			var skipCount = $.trim(box1.find("input[name='pageSize']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=10;
			}
			var totalCount = box1.find(".hidden_data").attr("count");
			if(count>totalCount){
				count=totalCount;
			}
			if(count<=0){
				count=1;
			}
			if(count>100){
				count=100;
			}
			box1.find("input[name='pageSize']").val(count);
		},
		pageSizeCheckInput4:function(){
			var box4 = $("#content4");
			var skipCount = $.trim(box4.find("input[name='pageSize']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=10;
			}
			var totalCount = box4.find(".hidden_data").attr("count");
			if(count>totalCount){
				count=totalCount;
			}
			if(count<=0){
				count=1;
			}
			if(count>100){
				count=100;
			}
			box4.find("input[name='pageSize']").val(count);
		},
		skipPageCheckInput9:function(){
      var box9 = $("#content9");
      var skipCount = $.trim(box9.find("input[name='skipCount']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=1;
      }
      var totalPage = box9.find(".hidden_data").attr("totalpages");
      if(count<=0){
        count=1;
      }
      if(parseInt(count)>parseInt(totalPage)){
        count=totalPage;
      }
      box9.find("input[name='skipCount']").val(count);
    },
    skipPageCheckInput10:function(){
      var box10 = $("#content10");
      var skipCount = $.trim(box10.find("input[name='skipCount']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=1;
      }
      var totalPage = box10.find(".hidden_data").attr("totalpages");
      if(count<=0){
        count=1;
      }
      if(parseInt(count)>parseInt(totalPage)){
        count=totalPage;
      }
      box10.find("input[name='skipCount']").val(count);
    },
    skipPageCheckInput8:function(){
      var box8 = $("#content8");
      var skipCount = $.trim(box8.find("input[name='skipCount']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=1;
      }
      var totalPage = box8.find(".hidden_data").attr("totalpages");
      if(count<=0){
        count=1;
      }
      if(parseInt(count)>parseInt(totalPage)){
        count=totalPage;
      }
      box8.find("input[name='skipCount']").val(count);
    },
		skipPageCheckInput7:function(){
			var box7 = $("#content7");
			var skipCount = $.trim(box7.find("input[name='skipCount']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=1;
			}
			var totalPage = box7.find(".hidden_data").attr("totalpages");
			if(count<=0){
				count=1;
			}
			if(parseInt(count)>parseInt(totalPage)){
				count=totalPage;
			}
			box7.find("input[name='skipCount']").val(count);
		},
		skipPageCheckInput6:function(){
      var box6 = $("#content6");
      var skipCount = $.trim(box6.find("input[name='skipCount']").val());
      var count =skipCount.replace(/[^\d]/g,'');
      if(count==null||count==""){
        count=1;
      }
      var totalPage = box6.find(".hidden_data").attr("totalpages");
      if(count<=0){
        count=1;
      }
      if(parseInt(count)>parseInt(totalPage)){
        count=totalPage;
      }
      box6.find("input[name='skipCount']").val(count);
    },
		skipPageCheckInput5:function(){
			var box5 = $("#content5");
			var skipCount = $.trim(box5.find("input[name='skipCount']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=1;
			}
			var totalPage = box5.find(".hidden_data").attr("totalpages");
			if(count<=0){
				count=1;
			}
			if(parseInt(count)>parseInt(totalPage)){
				count=totalPage;
			}
			box5.find("input[name='skipCount']").val(count);
		},
		skipPageCheckInput:function(){
			var box1 = $("#content1");
			var skipCount = $.trim(box1.find("input[name='skipCount']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=1;
			}
			var totalPage = box1.find(".hidden_data").attr("totalpages");
			if(count>totalPage){
				count=totalPage;
			}
			if(count<=0){
				count=1;
			}
			box1.find("input[name='skipCount']").val(count);
		},
		skipPageCheckInput4:function(){
			var box4 = $("#content4");
			var skipCount = $.trim(box4.find("input[name='skipCount']").val());
			var count =skipCount.replace(/[^\d]/g,'');
			if(count==null||count==""){
				count=1;
			}
			var totalPage = box4.find(".hidden_data").attr("totalpages");
			if(count>totalPage){
				count=totalPage;
			}
			if(count<=0){
				count=1;
			}
			box4.find("input[name='skipCount']").val(count);
		},
		skipPage:function(){
			var box1 = $("#content1");
			var data = MailManage.getParam();
			data["page.pageNo"]=$.trim(box1.find("input[name='skipCount']").val());
			MailManage.findMailManageList(data);
		},
		skipPage4:function(){
      var box4 = $("#content4");
      var data = MailManage.getReturnParam();
      data["page.pageNo"]=$.trim(box4.find("input[name='skipCount']").val());
      MailManage.findMailReturnList(data);
    },
		skipPage5:function(){
			var box5 = $("#content5");
			var data = MailManage.getTemplateParam();
			data["page.pageNo"]=$.trim(box5.find("input[name='skipCount']").val());
			MailManage.findTemplateList(data);
		},
		skipPage6:function(){
			var box6 = $("#content6");
			var data = MailManage.getSenderParam();
			data["page.pageNo"]=$.trim(box6.find("input[name='skipCount']").val());
			MailManage.findSenderList(data);
		},
		skipPage7:function(){
      var box7 = $("#content7");
      var data = MailManage.getClientParam();
      data["page.pageNo"]=$.trim(box7.find("input[name='skipCount']").val());
      MailManage.findClientList(data);
    },
    skipPage8:function(){
      var box8 = $("#content8");
      var data = MailManage.getBlackParam();
      data["page.pageNo"]=$.trim(box8.find("input[name='skipCount']").val());
      MailManage.findBlackList(data);
    },
    skipPage9:function(){
      var box9 = $("#content9");
      var data = MailManage.getWhiteParam();
      data["page.pageNo"]=$.trim(box9.find("input[name='skipCount']").val());
      MailManage.findWhiteList(data);
    },
    skipPage10:function(){
      var box10 = $("#content10");
      var data = MailManage.getStatisticsParam();
      data["page.pageNo"]=$.trim(box10.find("input[name='skipCount']").val());
      MailManage.findStatisticsList(data);
    },
		searchList:function(){
			var data = MailManage.getParam();
			MailManage.findMailManageList(data);
		},
		getTime:function(){
			var myDate=new Date();
			var year = myDate.getFullYear();
			var month1 = myDate.getMonth()+1;
			var day = myDate.getDate();
			myDate.setMonth(myDate.getMonth()-1);
			var month2 = myDate.getMonth()+1;
			var hh = myDate.getHours();
	        var mm = myDate.getMinutes();
	        var endDate = year + "-";
	        if(month1==1){
              year=year-1;
          }
	        var startDate = year + "-"
	        if(month1 < 10){
	        	endDate += "0";
	        }
	        endDate += month1 + "-";
	        if(month2 < 10){
	        	startDate += "0"
	        }
	        startDate += month2 + "-"
	        if(day < 10){
	        	startDate += "0";
	        	endDate += "0";
	        }
	        startDate += day + " ";
	        endDate += day + " ";
	        var data = {};
			data["startDate"]= startDate += " 00:00";
			data["endDate"]= endDate += " 23:59";
			return data;
	        
		},
		emptyParam:function(){
			$("#status_filter").val("");
			var box1 = $("#content1");
			box1.find("input[name='mailTemplateName']").val("");
			box1.find("input[name='receiver']").val("");
			box1.find("input[name='startDate']").val("");
			box1.find("input[name='endDate']").val("");
			$("#orderBy").val("mailId");
			box1.find("input[name='sender']").val("");
      box1.find("input[name='receiverPsnId']").val("");
      box1.find("input[name='senderPsnId']").val("");
			box1.find("input[name='skipCount']").val("");
			box1.find("input[name='pageSize']").val("10");
			box1.find("input[name='type']").val("");
		},
		emptyReturnParam:function(){
			var data = getTime();
			var box4 = $("#content4");
			box4.find("input[name='mailTemplateName']").val("");
			box4.find("input[name='receiver']").val("");
			box4.find("input[name='startSendDate']").val(data["startDate"]);
			box4.find("input[name='endSendDate']").val(data["endDate"]);
			box4.find("input[name='skipCount']").val("");
			box4.find("input[name='pageSize']").val("10");
		},
		emptydev5Param:function(){
			var data = getTime();
			$("#startCreateDate").val(data["startDate"]);
			$("#endCreateDate").val(data["endDate"]);
		},
		emptyTemplateParam:function(){
			var box5 = $("#content5");
			box5.find("input[name='templateName']").val("");
			box5.find("input[name='templateCode']").val("");
			box5.find("input[name='subject']").val("");
			$("#status").val("0");
			$("#limitStatus").val("0");
			box5.find("input[name='skipCount']").val("");
			box5.find("input[name='pageSize']").val("10");
		},
		emptySenderParam:function(){
			var box6 = $("#content6");
			box6.find("input[name='account']").val("");
			$("#sendStatus").val("");
			box6.find("input[name='skipCount']").val("");
			box6.find("input[name='pageSize']").val("10");
		},
		emptyClientParam:function(){
      var box7 = $("#content7");
      box7.find("input[name='clientName']").val("");
      $("#clientStatus").val("");
      box7.find("input[name='skipCount']").val("");
      box7.find("input[name='pageSize']").val("10");
    },
    emptyBlackParam:function(){
      var box8 = $("#content8");
      box8.find("input[name='blackEmail']").val("");
      $("#blackStatus").val("");
      $("#blackType").val("");
      box8.find("input[name='skipCount']").val("");
      box8.find("input[name='pageSize']").val("10");
    },
    emptyWhiteParam:function(){
      var box9 = $("#content9");
      box9.find("input[name='whiteEmail']").val("");
      $("#whiteStatus").val("");
      box9.find("input[name='skipCount']").val("");
      box9.find("input[name='pageSize']").val("10");
    },
    emptyStatisticsParam:function(){
      var box10 = $("#content10");
      box10.find("input[name='startStatisticsDate']").val("");
      box10.find("input[name='endStatisticsDate']").val("");
      box10.find("input[name='skipCount']").val("");
      box10.find("input[name='pageSize']").val("10");
    },
		getParam:function(){ 
			var box1 = $("#content1");
			var data = {};
			data["mailTemplateName"]=$.trim(box1.find("input[name='mailTemplateName']").val());
			data["receiver"]=$.trim(box1.find("input[name='receiver']").val());
			data["senderDateStartStr"]=$.trim(box1.find("input[name='startDate']").val());
			data["senderDateEndStr"]=$.trim(box1.find("input[name='endDate']").val());
			data["page.pageSize"]=$.trim(box1.find("input[name='pageSize']").val());
			data["page.ignoreMin"]=true;
			data["statusFilter"]=$("#status_filter").val();
			data["orderBy"]=$("#orderBy").val();
			data["sender"]=$.trim(box1.find("input[name='sender']").val());
			data["receiverPsnId"]=$.trim(box1.find("input[name='receiverPsnId']").val());
			data["senderPsnId"]=$.trim(box1.find("input[name='senderPsnId']").val());
			return data;
		},
		getReturnParam:function(){ 
			var data = {};
			var box4 = $("#content4");
			data["address"]=$.trim(box4.find("input[name='mailTemplateName']").val());
			data["account"]=$.trim(box4.find("input[name='receiver']").val());
			data["startSendDateStr"]=$.trim(box4.find("input[name='startSendDate']").val());
			data["endSendDateStr"]=$.trim(box4.find("input[name='endSendDate']").val());
			data["page.pageSize"] = $.trim(box4.find("input[name='pageSize']").val());
			data["page.ignoreMin"]=true;
			return data;
		},
		getTemplateParam:function(){ 
			var box5 = $("#content5");
			var data = {};
			data["templateName"]=$.trim(box5.find("input[name='templateName']").val());
			data["templateCode"]=$.trim(box5.find("input[name='templateCode']").val());
			data["subject"]=$.trim(box5.find("input[name='subject']").val());
			data["status"]=$("#status").val();
			data["limitStatus"]=$("#limitStatus").val();
			data["page.pageSize"]=$.trim(box5.find("input[name='pageSize']").val());
			data["page.ignoreMin"]=true;
			return data;
		},
		getSenderParam:function(){ 
			var box6 = $("#content6");
			var data = {};
			data["account"]=$.trim(box6.find("input[name='account']").val());
			data["status"]=$("#sendStatus").val();
			data["page.pageSize"]=$.trim(box6.find("input[name='pageSize']").val());
			data["page.ignoreMin"]=true;
			return data;
		},
		getClientParam:function(){ 
      var box7 = $("#content7");
      var data = {};
      data["clientName"]=$.trim(box7.find("input[name='clientName']").val());
      data["status"]=$("#clientStatus").val();
      data["page.pageSize"]=$.trim(box7.find("input[name='pageSize']").val());
      data["page.ignoreMin"]=true;
      return data;
    },
    getBlackParam:function(){ 
      var box8 = $("#content8");
      var data = {};
      data["email"]=$.trim(box8.find("input[name='blackEmail']").val());
      data["status"]=$("#blackStatus").val();
      data["type"]=$("#blackType").val();
      data["page.pageSize"]=$.trim(box8.find("input[name='pageSize']").val());
      data["page.ignoreMin"]=true;
      return data;
    },
    getWhiteParam:function(){ 
      var box9 = $("#content9");
      var data = {};
      data["email"]=$.trim(box9.find("input[name='whiteEmail']").val());
      data["status"]=$("#whiteStatus").val();
      data["page.pageSize"]=$.trim(box9.find("input[name='pageSize']").val());
      data["page.ignoreMin"]=true;
      return data;
    },
    getStatisticsParam:function(){ 
      var box10 = $("#content10");
      var data = {};
      data["startStatisticsDateStr"]=$.trim(box10.find("input[name='startStatisticsDate']").val());
      data["endStatisticsDateStr"]=$.trim(box10.find("input[name='endStatisticsDate']").val());
      data["page.pageSize"]=$.trim(box10.find("input[name='pageSize']").val());
      data["page.ignoreMin"]=true;
      return data;
    },
		findMailManageListCallBack:function(){
			var box1 = $("#content1");
			var data = box1.find(".hidden_data");
			var text ="记录总数:"+data.attr("count")+"条; "+data.val();
			$("#show_total").text(text);
			box1.find("input[name='totalPages']").val(data.attr("totalpages"));
			box1.find("input[name='currentCount']").val(data.attr("page"));
		},
		findReturnListCallBack:function(){
			var box4 = $("#content4");
			var data = box4.find(".hidden_data");
			box4.find("input[name='totalPages']").val(data.attr("totalpages"));
			box4.find("input[name='currentCount']").val(data.attr("page"));
			box4.find(".show_title").html(data.val());
		},
		findTemplateListCallBack:function(){
			var box5 = $("#content5");
			var data = box5.find(".hidden_data");
			var text ="记录总数:"+data.attr("count")+"条; "+data.val();
			box5.find("input[name='totalPages']").val(data.attr("totalpages"));
			box5.find("input[name='currentCount']").val(data.attr("page"));
			box5.find("#show_tem_total").html(text);
		},
		findSenderListCallBack:function(){
			var box6 = $("#content6");
			var data = box6.find(".hidden_data");
			var text ="记录总数:"+data.attr("count")+"条; "+data.val();
			box6.find("input[name='totalPages']").val(data.attr("totalpages"));
			box6.find("input[name='currentCount']").val(data.attr("page"));
			box6.find("#show_sender_total").html(text);
		},
		findClientListCallBack:function(){
      var box7 = $("#content7");
      var data = box7.find(".hidden_data");
      var text ="记录总数:"+data.attr("count")+"条; "+data.val();
      box7.find("input[name='totalPages']").val(data.attr("totalpages"));
      box7.find("input[name='currentCount']").val(data.attr("page"));
      box7.find("#show_client_total").html(text);
    },
    findBlackListCallBack:function(){
      var box8 = $("#content8");
      var data = box8.find(".hidden_data");
      var text ="记录总数:"+data.attr("count")+"条; "+data.val();
      box8.find("input[name='totalPages']").val(data.attr("totalpages"));
      box8.find("input[name='currentCount']").val(data.attr("page"));
      box8.find("#show_black_total").html(text);
    },
    findWhiteListCallBack:function(){
      var box9 = $("#content9");
      var data = box9.find(".hidden_data");
      var text ="记录总数:"+data.attr("count")+"条; "+data.val();
      box9.find("input[name='totalPages']").val(data.attr("totalpages"));
      box9.find("input[name='currentCount']").val(data.attr("page"));
      box9.find("#show_white_total").html(text);
    },
    findStatisticsListCallBack:function(){
      var box10 = $("#content10");
      var data = box10.find(".hidden_data");
      var text ="记录总数:"+data.attr("count")+"条; "+data.val();
      box10.find("input[name='totalPages']").val(data.attr("totalpages"));
      box10.find("input[name='currentCount']").val(data.attr("page"));
      box10.find("#show_statistics_total").html(text);
    },
		findMailManageList:function(data){
			MailManage.loadStateIco($("#mail_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/findlist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#mail_table").html(data);
					MailManage.findMailManageListCallBack();
				},
				error: function(){
				}
			});
		},
		findMailReturnList:function(data){
			MailManage.loadStateIco($("#mail_teturn_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/returnlist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#mail_teturn_table").html(data);
					MailManage.findReturnListCallBack();
				},
				error: function(){
				}
			});
		},
		findTemplateList:function(data){
			MailManage.loadStateIco($("#mail_template_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/findtemplatelist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#mail_template_table").html(data);
					MailManage.findTemplateListCallBack();
				},
				error: function(){
				}
			});
		},
		findSenderList:function(data){
			MailManage.loadStateIco($("#mail_sender_table"));
			$.ajax({
				url : '/scmmanagement/mailmanage/findsenderlist',
				type : 'post',
				dataType:'html',
				data:data,
				success : function(data) {
					$("#mail_sender_table").html(data);
					MailManage.findSenderListCallBack();
				},
				error: function(){
				}
			});
		},
		findClientList:function(data){
      MailManage.loadStateIco($("#mail_client_table"));
      $.ajax({
        url : '/scmmanagement/mailmanage/findclientlist',
        type : 'post',
        dataType:'html',
        data:data,
        success : function(data) {
          $("#mail_client_table").html(data);
          MailManage.findClientListCallBack();
        },
        error: function(){
        }
      });
    },
    findBlackList:function(data){
      MailManage.loadStateIco($("#mail_black_table"));
      $.ajax({
        url : '/scmmanagement/mailmanage/findblacklist',
        type : 'post',
        dataType:'html',
        data:data,
        success : function(data) {
          $("#mail_black_table").html(data);
          MailManage.findBlackListCallBack();
        },
        error: function(){
        }
      });
    },
    findWhiteList:function(data){
      MailManage.loadStateIco($("#mail_white_table"));
      $.ajax({
        url : '/scmmanagement/mailmanage/findwhitelist',
        type : 'post',
        dataType:'html',
        data:data,
        success : function(data) {
          $("#mail_white_table").html(data);
          MailManage.findWhiteListCallBack();
        },
        error: function(){
        }
      });
    },
    findStatisticsList:function(data){
      MailManage.loadStateIco($("#mail_statistics_table"));
      $.ajax({
        url : '/scmmanagement/mailmanage/findstatisticslist',
        type : 'post',
        dataType:'html',
        data:data,
        success : function(data) {
          $("#mail_statistics_table").html(data);
          MailManage.findStatisticsListCallBack();
        },
        error: function(){
        }
      });
    },
		content1_init:function(){
			var box1 = $("#content1");
			//检索事件
			box1.find(".dev_mail_search").unbind("click").click(function(){
				MailManage.searchList();
			});
			//输入回车事件
			box1.find("input[name='mailTemplateName'],input[name='receiver'],input[name='pageSize']").unbind("keyup").keyup(function(e){
				if(e.keyCode==13){
					MailManage.searchList();
				}
			});
			//清空事件
			box1.find(".dev_mail_cancle").unbind("click").click(function(){
				MailManage.emptyParam();
			});
			//每页显示数量输入效验
			box1.find("input[name='pageSize']").keyup(function(){
				MailManage.pageSizeCheckInput();
			});
			//跳转输入效验
			box1.find("input[name='skipCount']").keyup(function(){
				MailManage.skipPageCheckInput();
			});
			//跳转按钮事件
			box1.find(".btn_skipCount").click(function(){
				MailManage.skipPage();
			});
			//跳转回车
			box1.find("input[name='skipCount']").keyup(function(e){
				if(e.keyCode==13){
					MailManage.skipPage();
				}
			});
			//上一页
			box1.find(".first_page").unbind("click").click(function(){
				MailManage.firstPageEvent();
			});
			//下一页
			box1.find(".next_page").unbind("click").click(function(){
				MailManage.nextPageEvent();
			});
			//操作事件
			$("#mail_table").on("click",".open_link",function(){
				MailManage.detailsItem(this);
			});
			//打开邮件详情事件
			$("#mail_table").on("click",".open_details",function(){
				MailManage.openMailDetail(this);
			});
			//打开模板操作记录
			$("#mail_table").on("click",".open_template",function(){
				MailManage.detailsLink(this);
			});
			//关闭窗口事件
			$("#close_div").unbind("click").click(function(){
				MailManage.closeDiv();
			});
		},
		content4_init:function(){
			var box4 = $("#content4");
			//检索事件
			box4.find(".dev_mail_search").unbind("click").click(function(){
				var data = MailManage.getReturnParam();
				MailManage.findMailReturnList(data);
			});
			//清空事件
			box4.find(".dev_mail_cancle").unbind("click").click(function(){
				MailManage.emptyReturnParam();
			});
			//跳转按钮事件
			box4.find(".btn_skipCount").click(function(){
			  MailManage.skipPage4();
			});
			//跳转回车
			box4.find("input[name='skipCount']").keyup(function(e){
				if(e.keyCode==13){
					var data = MailManage.getReturnParam();
					MailManage.findMailReturnList(data);
				}
			});
			//上一页
			box4.find(".first_page").unbind("click").click(function(){
			  var currentCount = box4.find(".hidden_data").attr("currentCount");
        if(currentCount==1){
          return;
        }
				var data = MailManage.getReturnParam();
				data["page.pageNo"]=Number(currentCount)-1;
				MailManage.findMailReturnList(data);
			});
			//下一页
			box4.find(".next_page").unbind("click").click(function(){
				var data = MailManage.getReturnParam();
				var currentCount = box4.find(".hidden_data").attr("currentCount");
        var totalPage = box4.find(".hidden_data").attr("totalpages");
        if(currentCount==totalPage){
          return;
        }
        data["page.pageNo"]=Number(currentCount)+1;
				MailManage.findMailReturnList(data);
			});
			//每页显示数量输入效验
			box4.find("input[name='pageSize']").keyup(function(){
				MailManage.pageSizeCheckInput4();
			});
			//跳转输入效验
			box4.find("input[name='skipCount']").keyup(function(){
				MailManage.skipPageCheckInput4();
			});
			//打开窗口事件
			box4.on("click",".show_content",function(){
				var $this = $(this);
				$("#my_div4").find(".supernatant_box").html($this.find(".mail_data").html());
				$("#my_div4").show();
				$(".borain-timeChoiceMask").show();
			});
			//关闭窗口事件
			$("#close_div4").unbind("click").click(function(){
				$("#my_div4").hide();
				$(".borain-timeChoiceMask").hide();
			});
			
		},
		content5_init:function(){
			var box5 = $("#content5");
			//检索事件
			box5.find(".dev_mail_search").unbind("click").click(function(){
				var data = MailManage.getTemplateParam();
				MailManage.findTemplateList(data);
			});
			//清空事件
			box5.find(".dev_mail_cancle").unbind("click").click(function(){
				MailManage.emptyTemplateParam();
			});
			//跳转按钮事件
			box5.find(".btn_skipCount").click(function(){
				MailManage.skipPage5();
			});
			//跳转回车
			box5.find("input[name='skipCount']").keyup(function(e){
				if(e.keyCode==13){
					MailManage.skipPage5();
				}
			});
			//上一页
			box5.find(".first_page").unbind("click").click(function(){
				var currentCount = box5.find(".hidden_data").attr("currentCount");
				if(currentCount==1){
					return;
				}
				var data = MailManage.getTemplateParam();
				data["page.pageNo"]=Number(currentCount)-1;
				MailManage.findTemplateList(data);
			});
			//下一页
			box5.find(".next_page").unbind("click").click(function(){
				var data = MailManage.getTemplateParam();
				var currentCount = box5.find(".hidden_data").attr("currentCount");
				var totalPage = box5.find(".hidden_data").attr("totalpages");
				if(currentCount==totalPage){
					return;
				}
				data["page.pageNo"]=Number(currentCount)+1;
				MailManage.findTemplateList(data);
			});
			//每页显示数量输入效验
			box5.find("input[name='pageSize']").keyup(function(){
				MailManage.pageSizeCheckInput5();
			});
			//跳转输入效验
			box5.find("input[name='skipCount']").keyup(function(){
				MailManage.skipPageCheckInput5();
			});
			//操作事件
			$("#mail_template_table").on("click",".open_link",function(){
				MailManage.detailsLink(this);
			});
			//关闭窗口事件
			$("#close_div5").unbind("click").click(function(){
				MailManage.closeDiv5();
			});
			//检索事件
			$("#dev5_search").unbind("click").click(function(){
				MailManage.detailsLinkSearch(this);
			});
			//清空事件
			$("#dev5_cancle").unbind("click").click(function(){
				MailManage.emptydev5Param();
			});
		},
		content6_init:function(){
			var box6 = $("#content6");
			//检索事件
			box6.find(".dev_mail_search").unbind("click").click(function(){
				var data = MailManage.getSenderParam();
				MailManage.findSenderList(data);
			});
			//清空事件
			box6.find(".dev_mail_cancle").unbind("click").click(function(){
				MailManage.emptySenderParam();
			});
			//跳转按钮事件
			box6.find(".btn_skipCount").click(function(){
				MailManage.skipPage6();
			});
			//跳转回车
			box6.find("input[name='skipCount']").keyup(function(e){
				if(e.keyCode==13){
					MailManage.skipPage6();
				}
			});
			//上一页
			box6.find(".first_page").unbind("click").click(function(){
				var currentCount = box6.find(".hidden_data").attr("currentCount");
				if(currentCount==1){
					return;
				}
				var data = MailManage.getSenderParam();
				data["page.pageNo"]=Number(currentCount)-1;
				MailManage.findSenderList(data);
			});
			//下一页
			box6.find(".next_page").unbind("click").click(function(){
				var data = MailManage.getSenderParam();
				var currentCount = box6.find(".hidden_data").attr("currentCount");
				var totalPage = box6.find(".hidden_data").attr("totalpages");
				if(currentCount==totalPage){
					return;
				}
				data["page.pageNo"]=Number(currentCount)+1;
				MailManage.findSenderList(data);
			});
			//每页显示数量输入效验
			box6.find("input[name='pageSize']").keyup(function(){
				MailManage.pageSizeCheckInput6();
			});
			//跳转输入效验
			box6.find("input[name='skipCount']").keyup(function(){
				MailManage.skipPageCheckInput6();
			});
			
		},
		content7_init:function(){
      var box7 = $("#content7");
      //检索事件
      box7.find(".dev_mail_search").unbind("click").click(function(){
        var data = MailManage.getClientParam();
        MailManage.findClientList(data);
      });
      //清空事件
      box7.find(".dev_mail_cancle").unbind("click").click(function(){
        MailManage.emptyClientParam();
      });
      //跳转按钮事件
      box7.find(".btn_skipCount").click(function(){
        MailManage.skipPage7();
      });
      //跳转回车
      box7.find("input[name='skipCount']").keyup(function(e){
        if(e.keyCode==13){
          MailManage.skipPage7();
        }
      });
      //上一页
      box7.find(".first_page").unbind("click").click(function(){
        var currentCount = box7.find(".hidden_data").attr("currentCount");
        if(currentCount==1){
          return;
        }
        var data = MailManage.getClientParam();
        data["page.pageNo"]=Number(currentCount)-1;
        MailManage.findClientList(data);
      });
      //下一页
      box7.find(".next_page").unbind("click").click(function(){
        var data = MailManage.getClientParam();
        var currentCount = box7.find(".hidden_data").attr("currentCount");
        var totalPage = box7.find(".hidden_data").attr("totalpages");
        if(currentCount==totalPage){
          return;
        }
        data["page.pageNo"]=Number(currentCount)+1;
        MailManage.findClientList(data);
      });
      //每页显示数量输入效验
      box7.find("input[name='pageSize']").keyup(function(){
        MailManage.pageSizeCheckInput7();
      });
      //跳转输入效验
      box7.find("input[name='skipCount']").keyup(function(){
        MailManage.skipPageCheckInput7();
      });
      
    },
    content8_init:function(){
      var box8 = $("#content8");
      //检索事件
      box8.find(".dev_mail_search").unbind("click").click(function(){
        var data = MailManage.getBlackParam();
        MailManage.findBlackList(data);
      });
      //清空事件
      box8.find(".dev_mail_cancle").unbind("click").click(function(){
        MailManage.emptyBlackParam();
      });
      //跳转按钮事件
      box8.find(".btn_skipCount").click(function(){
        MailManage.skipPage8();
      });
      //跳转回车
      box8.find("input[name='skipCount']").keyup(function(e){
        if(e.keyCode==13){
          MailManage.skipPage8();
        }
      });
      //上一页
      box8.find(".first_page").unbind("click").click(function(){
        var currentCount = box8.find(".hidden_data").attr("currentCount");
        if(currentCount==1){
          return;
        }
        var data = MailManage.getBlackParam();
        data["page.pageNo"]=Number(currentCount)-1;
        MailManage.findBlackList(data);
      });
      //下一页
      box8.find(".next_page").unbind("click").click(function(){
        var data = MailManage.getBlackParam();
        var currentCount = box8.find(".hidden_data").attr("currentCount");
        var totalPage = box8.find(".hidden_data").attr("totalpages");
        if(currentCount==totalPage){
          return;
        }
        data["page.pageNo"]=Number(currentCount)+1;
        MailManage.findBlackList(data);
      });
      //每页显示数量输入效验
      box8.find("input[name='pageSize']").keyup(function(){
        MailManage.pageSizeCheckInput8();
      });
      //跳转输入效验
      box8.find("input[name='skipCount']").keyup(function(){
        MailManage.skipPageCheckInput8();
      });
      
    },
    content9_init:function(){
      var box9 = $("#content9");
      //检索事件
      box9.find(".dev_mail_search").unbind("click").click(function(){
        var data = MailManage.getWhiteParam();
        MailManage.findWhiteList(data);
      });
      //清空事件
      box9.find(".dev_mail_cancle").unbind("click").click(function(){
        MailManage.emptyWhiteParam();
      });
      //跳转按钮事件
      box9.find(".btn_skipCount").click(function(){
        MailManage.skipPage9();
      });
      //跳转回车
      box9.find("input[name='skipCount']").keyup(function(e){
        if(e.keyCode==13){
          MailManage.skipPage9();
        }
      });
      //上一页
      box9.find(".first_page").unbind("click").click(function(){
        var currentCount = box9.find(".hidden_data").attr("currentCount");
        if(currentCount==1){
          return;
        }
        var data = MailManage.getWhiteParam();
        data["page.pageNo"]=Number(currentCount)-1;
        MailManage.findWhiteList(data);
      });
      //下一页
      box9.find(".next_page").unbind("click").click(function(){
        var data = MailManage.getWhiteParam();
        var currentCount = box9.find(".hidden_data").attr("currentCount");
        var totalPage = box9.find(".hidden_data").attr("totalpages");
        if(currentCount==totalPage){
          return;
        }
        data["page.pageNo"]=Number(currentCount)+1;
        MailManage.findWhiteList(data);
      });
      //每页显示数量输入效验
      box9.find("input[name='pageSize']").keyup(function(){
        MailManage.pageSizeCheckInput9();
      });
      //跳转输入效验
      box9.find("input[name='skipCount']").keyup(function(){
        MailManage.skipPageCheckInput9();
      });
      
    },
    content10_init:function(){
      var box10 = $("#content10");
      //检索事件
      box10.find(".dev_mail_search").unbind("click").click(function(){
        var data = MailManage.getStatisticsParam();
        MailManage.findStatisticsList(data);
      });
      //清空事件
      box10.find(".dev_mail_cancle").unbind("click").click(function(){
        MailManage.emptyStatisticsParam();
      });
      //跳转按钮事件
      box10.find(".btn_skipCount").click(function(){
        MailManage.skipPage10();
      });
      //跳转回车
      box10.find("input[name='skipCount']").keyup(function(e){
        if(e.keyCode==13){
          MailManage.skipPage10();
        }
      });
      //上一页
      box10.find(".first_page").unbind("click").click(function(){
        var currentCount = box10.find(".hidden_data").attr("currentCount");
        if(currentCount==1){
          return;
        }
        var data = MailManage.getStatisticsParam();
        data["page.pageNo"]=Number(currentCount)-1;
        MailManage.findStatisticsList(data);
      });
      //下一页
      box10.find(".next_page").unbind("click").click(function(){
        var data = MailManage.getStatisticsParam();
        var currentCount = box10.find(".hidden_data").attr("currentCount");
        var totalPage = box10.find(".hidden_data").attr("totalpages");
        if(currentCount==totalPage){
          return;
        }
        data["page.pageNo"]=Number(currentCount)+1;
        MailManage.findStatisticsList(data);
      });
      //每页显示数量输入效验
      box10.find("input[name='pageSize']").keyup(function(){
        MailManage.pageSizeCheckInput10();
      });
      //跳转输入效验
      box10.find("input[name='skipCount']").keyup(function(){
        MailManage.skipPageCheckInput10();
      });
      
    },
		init:function(){
			//时间选择初始化
			onLoadTimeChoiceDemo();
			borainTimeChoice({
			        start:".startDate",
			        level:"HM",
			        less:false
			});
			borainTimeChoice({
		        start:".endDate",
		        level:"HM",
		        less:false
			});
			borainTimeChoice({
		        start:".startSendDate",
		        level:"HM",
		        less:false
			});
			borainTimeChoice({
		        start:".endSendDate",
		        level:"HM",
		        less:false
			});
			borainTimeChoice({
        start:".startStatisticsDate",
        level:"YMD",
        less:false
      });
      borainTimeChoice({
        start:".endStatisticsDate",
        level:"YMD",
        less:false
      });
			//菜单选项切换事件
			$(".dev_item").unbind("click").click(function(){
				var $this = $(this);
				MailManage.switchItem($this);
			});
			//初次加载邮件列表列表
			var data = MailManage.getParam();
			MailManage.findMailManageList(data);
			//邮件列表初始化
			MailManage.content1_init();
			//退信列表初始化
			MailManage.content4_init();
			//模板列表初始化
			MailManage.content5_init();
			
			MailManage.content6_init();
			
			MailManage.content7_init();
			
			MailManage.content8_init();
			
			MailManage.content9_init();
			
			MailManage.content10_init();
		}
};