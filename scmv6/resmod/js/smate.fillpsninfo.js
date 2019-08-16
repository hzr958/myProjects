/* 个人主页信息补充
 *  @author zk
 *  
 */
var smate = smate ? smate : {}; 

smate.fillpsninfo = smate.fillpsninfo ? smate.fillpsninfo:{};

	//入口
smate.fillpsninfo.entry = function(){
		$.ajax({
			url : snsctx + '/profile/infoFill',
		type : 'POST',
		dataType : 'json',
		data : {},
		success : function(data){ 
			if (data.result == 'success') {
				smate.psninfofillhtml.showHtml(data.htmlData);
			}  else {
				 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
			}
		},
		error : function(){
	          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
			}
		});
	}
	//dealResult 1:同意,2:否决,3:忽略
	//用户处理
smate.fillpsninfo.dealResult = function(type,dealResult){
		
	smate.fillpsninfo.ajaxDeal[type](dealResult);
	}
	
smate.fillpsninfo.ajaxDeal = {
	 
	   //关闭流程
	  closeFill : function(){
		  $.ajax({
			  url : snsctx + '/profile/closeFill',
				type : 'POST',
				dataType : 'json',
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					if (data.result == 'success') {
						$("#psnInfoFill").slideUp("slow");
					} else {
						 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
		  });
	  },
	 
		//工作地点
		workPlace : function(dealResult){
			
			  var insId = $("#workPlace").attr("insId");
			  $.ajax({
					url : snsctx + '/profile/workPlace',
					type : 'POST',
					dataType : 'json',
					data : {"oidStr":insId,"dealResult":dealResult},
					success : function(data){ 
						
						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						 
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
						} else {
							 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					});
		},
		//工作部门
		workDept : function(dealResult){
			
			var insId = $("#workDept_input1").attr("insId");
			var insDept = $("#workDept_input1").attr("value");
			var insPosition= $("#workDept_input2").attr("value");
			var insDeptCol = $("#workDept_input1").css('color');
			var insPositionCol = $("#workDept_input2").css('color');
			
			//清除水印；但当输入与水印同名时，如果颜色与水印不同，不清除
			if(insDept==Personal_FillPsnInfo.locale["workDept_content_tip3"] && insDeptCol!="#000" && insDeptCol!="rgb(0, 0, 0)")
				insDept="";
			if(insPosition==Personal_FillPsnInfo.locale["workDept_content_tip4"] && insPositionCol!="#000" && insPositionCol!="rgb(0, 0, 0)")
				insPosition="";
			if(dealResult==1){
				if(insDept.length==0){
					 $.smate.scmtips.show('warn', smate.resumepsninfo.locale["work_ins_dept_required"]);
					 return;
				}
				if(insDept.length>=100){
					 $.smate.scmtips.show('warn',$.format( smate.resumepsninfo.locale["work_degree_max"],100));
					 return;
				}
				if(insPosition.length==0){
					 $.smate.scmtips.show('warn', smate.resumepsninfo.locale["work_ins_position_required"]);
					 return;
				}
				if(insPosition.length>=100){
					 $.smate.scmtips.show('warn',$.format( smate.resumepsninfo.locale["work_position_max"],100));
					 return;
				}
			}
			$.ajax({
				url : snsctx + '/profile/workDept',
				type : 'POST',
				dataType : 'json',
				data : {"oidStr":insId,"dealResult":dealResult,"insDept":insDept,"insPosition":insPosition},
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					
					if (data.result == 'success') {
						smate.psninfofillhtml.showHtml(data.htmlData);
					} else {
						 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
			});
		},
		//工作时间
		workTime : function(dealResult){
			var insId = $("#insIdS").attr("insId");
			var fromYear = $("#psnInfo_work_fromYear option:selected").val();
			var fromMonth = $("#psnInfo_work_fromMonth option:selected") .val();
			var obj = document.getElementsByName("psnInfoFill_isPresent"); 
			if(obj[0].checked){
				var toYear =0;
				var toMonth =0;
			}else{
			var toYear = $('#psnInfo_work_toYear option:selected').val();
			var toMonth = $('#psnInfo_work_toMonth option:selected') .val();
			}
			if(dealResult==1){
			  var timeRe=FillPsnInfo.dataDeal_dataAvailable(fromYear,fromMonth,toYear,toMonth);
				if(timeRe==0){
					$.smate.scmtips.show('warn', smate.resumepsninfo.locale["date_required"]);
					return ;
				}
				if(timeRe==-1){
					$.smate.scmtips.show('warn', smate.resumepsninfo.locale["data_start_end_rule"]);
					return ;
				}
			}
			var insDate=fromYear+","+fromMonth+","+toYear+","+toMonth;
			$.ajax({
				url : snsctx + '/profile/workTime',
				type : 'POST',
				dataType : 'json',
				data : {"oidStr":insId,"dealResult":dealResult,"insDate":insDate},
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					
					if (data.result == 'success') {
						smate.psninfofillhtml.showHtml(data.htmlData);
					
						if(dealResult==1){
							
							 setTimeout(function(){Resume.personInfo.work.refresh();},200);
							
				          if (Resume.personInfo.refreshInformation != null) {//刷新信息完整度
				                    Resume.personInfo.refreshInformation();
				                }
					    }
					} else {
						 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
			});
		},
		//推荐职称
		recmdPosition : function (dealResult){
			var recmdPosId = $("#recmd_position_id").attr("posId");
			var posStr = $("#posStr").find("option:selected").text();
			$.ajax({
				url : snsctx + '/profile/recmdPosition',
				type : 'POST',
				dataType : 'json',
				data : {"oidStr":recmdPosId,"dealResult":dealResult},
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					
					if (data.result == 'success') {
						smate.psninfofillhtml.showHtml(data.htmlData);
						//更新职称 自动更新页面职称 tsz
						if(posStr!=null && posStr!=""){
							$("#base_list_text_position").text(posStr);
						}
					}else {
						 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
			});
		},
		//自填职称
		selfPosition : function(dealResult){
			var posId = $("#selfPositionInput").attr("code");
			var posStr =$("#selfPositionInput").val();
			
			if(dealResult==1){
				if(posStr.length==0){
					$.smate.scmtips.show('warn', smate.resumepsninfo.locale["base_position_required"]);
					return ;
				}
				if(posStr.length>50){
					$.smate.scmtips.show('warn',$.format( smate.resumepsninfo.locale["base_position_max"],50));
					return ;
				}
			}
			
			$.ajax({
				url : snsctx + '/profile/selfPosition',
				type : 'POST',
				dataType : 'json',
				data : {"oidStr":posId,"dealResult":dealResult,"ostr":posStr},
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					
					if (data.result == 'success') {
						smate.psninfofillhtml.showHtml(data.htmlData);
						//更新职称 自动更新页面职称 tsz
						if(posStr!=null && posStr!=""){
							$("#base_list_text_position").text(posStr);
						}
					} else {
						 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
			});
		},
		//熟悉的研究领域 
		KnowResearchArea : function(dealResult){

			var keysArr = [];
		    var keyStr = "";
		    var keys = $.autoword["psnInfoFillKey"].words();
		    $.each(keys, function(){
		        keysArr.push({
		            'keys': this["text"]
		        });
		        keyStr = keyStr.concat(this["text"]).concat("; ");
		    });
		    if (keyStr.lastIndexOf(";") != -1) 
		        keyStr = keyStr.substring(0, keyStr.lastIndexOf(";"));

			   var ostr = JSON.stringify(keysArr);
			    if (ostr == "[]") {//关键词为空
			    	ostr = '[{"keys":""}]';//区分学科领域与关键词
			    }
			$.ajax({
				url : snsctx + '/profile/knowResearchArea',
				type : 'POST',
				dataType : 'json',
				data : {"dealResult":dealResult,"ostr":ostr},
				success : function(data){ 
				
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return;
					}
					
					if (data.result == 'success') {
						//点保存,关键词出现在个人信息中
						if(dealResult==1){
						//FillPsnInfo.dealDiscKeyToPage(keyStr);
							location.reload(true);
						}
						 smate.psninfofillhtml.showHtml(data.htmlData);
					} else {
						  $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
					}
			});
		},
		//从事的研究领域 
		onResearchArea :function(dealResult){
			var keysArr = [];
		    var keyStr = "";
		    var keys = $.autoword["psnInfoFillKey"].words();
		    $.each(keys, function(){
		        keysArr.push({
		            'keys': this["text"]
		        });
		        keyStr = keyStr.concat(this["text"]).concat("; ");
		    });
		    if (keyStr.lastIndexOf(";") != -1) 
		        keyStr = keyStr.substring(0, keyStr.lastIndexOf(";"));

			   var ostr = JSON.stringify(keysArr);
			    if (ostr == "[]") {//关键词为空
			    	ostr = '[{"keys":""}]';//区分学科领域与关键词
			    }
			$.ajax({
				url : snsctx + '/profile/onResearchArea',
				type : 'POST',
				dataType : 'json',
				data : {"dealResult":dealResult,"ostr":ostr},
				success : function(data){ 
					
					//判断是否超时
					if(ajaxSessionTimeoutHandler(data)){
						return ;
					}
				
					if (data.result == 'success') {
						if(dealResult==1){
							//FillPsnInfo.dealDiscKeyToPage(keyStr);
							location.reload(true);
							}
						smate.psninfofillhtml.showHtml(data.htmlData);
					} else {
						 $.smate.scmtips.show('error', smate.resumepsninfo["fail"]);
					}
				},
				error : function(){
			          $.smate.scmtips.show('error', smate.resumepsninfo["fail"]);
					}
			});
		},
		//教育经历 
		schoolStudy :function(dealResult){
			
		      var degreeName = $("#studyDegree").val();
		      var insName=$("#studySchoolName").val();
		      var insId = $("#studySchoolName").attr("code");
		      var deptName=$("#studyDeptName").val();
		      var insNameCol=$("#studySchoolName").css('color');
		      var degreeNameCol = $("#studyDegree").css('color');
		      var deptNameCol=$("#studyDeptName").css('color');
		
		      //消除水印
		      if(degreeName==Personal_FillPsnInfo.locale["schoolStudy_degree"] && degreeNameCol!="#000" && degreeNameCol!="rgb(0, 0, 0)"){
		     	  degreeName="";
		         }
		    	 //通过样式判断，当输入与水印同名但为字体颜色为黑色时仍然能够提交
		      if(insName==Personal_FillPsnInfo.locale["schoolStudy_name"] && insNameCol!="#000" && insNameCol!="rgb(0, 0, 0)"){
		          	insName="";
		        }
		      if(deptName==Personal_FillPsnInfo.locale["schoolStudy_dept"] && deptNameCol!="#000" && deptNameCol!="rgb(0, 0, 0)"){
		    	  deptName="";
		        }
		     if(dealResult==1){	  
		    	 //单位长度控制 
		      if(insName.length==0){
		    	  $.smate.scmtips.show('warn', smate.resumepsninfo.locale["edu_ins_name_required"]);
		    	  return ;
		         }
		      if(insName.length>100){
		    	  $.smate.scmtips.show('warn', $.format(smate.resumepsninfo.locale["edu_ins_name_max"],100));
		    	  return ;
		         }
		       //专业长度控制
		         if(deptName.length>200){
			    	  $.smate.scmtips.show('warn', $.format(smate.resumepsninfo.locale["edu_major_max"],200));
			    	  return ;
			         }
		         //学历长度控制
		         if(degreeName.length>30){
		        	 $.smate.scmtips.show('warn', $.format(smate.resumepsninfo.locale["edu_degree_max"],30));
			    	  return ;
		         }
		       }
				$.ajax({
					url : snsctx + '/profile/schoolStudy',
					type : 'POST',
					dataType : 'json',
					data : {"insId":insId,"dealResult":dealResult,"insName":insName,"deptName":deptName,"degreeName":degreeName},
					success : function(data){ 
						
						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
						} else {
							 $.smate.scmtips.show('error', smate.resumepsninfo["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo["fail"]);
						}
				});
			},
			//教育经历 时间 
			eduTime : function(dealResult){
				
				var insId = $("#insIdS").attr("insId");
				if(dealResult==1){
					var fromYear = $("#psnInfo_work_fromYear option:selected").val();
					var fromMonth = $("#psnInfo_work_fromMonth option:selected") .val();
				
					var toYear = $('#psnInfo_work_toYear option:selected').val();
					var toMonth = $('#psnInfo_work_toMonth option:selected') .val();
					var timeRe =FillPsnInfo.dataDeal_dataAvailable(fromYear,fromMonth,toYear,toMonth)
					if(timeRe==0){
						$.smate.scmtips.show('warn', smate.resumepsninfo.locale["date_required"]);
						return ;
					}
					if(timeRe==-1){
						$.smate.scmtips.show('warn', smate.resumepsninfo.locale["data_start_end_rule"]);
						return ;
					}
					var insDate=fromYear+","+fromMonth+","+toYear+","+toMonth;
				}
				$.ajax({
					url : snsctx + '/profile/eduTime',
					type : 'POST',
					dataType : 'json',
					data : {"dealResult":dealResult,"dateStr":insDate},
					success : function(data){ 
						
						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
							if(dealResult==1){
								smate.resumepsninfo.edu.refresh();
				                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
				                	smate.resumepsninfo.refreshInformation();
				                }
						  }
						} else {
							 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
				});
			},
			//工作内容
			jobContent :function(dealResult){
				
				var ostr = $("#contentInput1").val();
				if(dealResult==1){
					if(ostr.length==0){
						$.smate.scmtips.show('warn',Personal_FillPsnInfo.locale["lengthLittle"]);
						return;
			    	}
					if(ostr.length>200){
						$.smate.scmtips.show('warn',$.format(Personal_FillPsnInfo.locale["jobContent_length_warn"],200));
						return;
					}
				}
				$.ajax({
					url : snsctx + '/profile/jobContent',
					type : 'POST',
					dataType : 'json',
					data : {"dealResult":dealResult,"ostr":ostr},
					success : function(data){ 
						
						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
						} else {
							 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
				});
			},
			//显示头衔 
			showTitlo :function(dealResult){
				var titloStr = $("#titloShowInput").val();
				if(dealResult==1){
					if(titloStr.length==0){
						$.smate.scmtips.show('warn', smate.resumepsninfo.locale["base_titolo_required"]);
						return;
					}
					if(titloStr.length>50){
						$.smate.scmtips.show('warn', $.format(smate.resumepsninfo.locale["base_position_max"],50));
						return;
					}
				}
				$.ajax({
					url : snsctx + '/profile/titloShow',
					type : 'POST',
					dataType : 'json',
					data : {"dealResult":dealResult,"ostr":titloStr},
					success : function(data){ 
						
						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
							if(titloStr!=null && titloStr!=""){
								//同步页面头衔的显示  tsz
								$("#base_titlo").text(titloStr);
							}
						}else {
							 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
				});
			},
			//所教课程
			teachCourse : function(dealResult){
				
				var str = new String();
			    var taughts = $.autoword["psnInfoFillTeachCourse"].words();	
			    $.each(taughts, function(index){
					var formatStr = this["text"];//部分英文课程含有特殊字符&
			        str = str.concat(formatStr);
					if(index != taughts.length-1){
						str = str.concat("; ")
					}
			    });
			    var post_data = {
			        'content': str,
			        "dealResult":dealResult
			    };
				$.ajax({
					url : snsctx + '/profile/teachCourse',
					type : 'POST',
					dataType : 'json',
					data : post_data,
					success : function(data){ 

						//判断是否超时
						if(ajaxSessionTimeoutHandler(data)){
							return;
						}
						
						if (data.result == 'success') {
							smate.psninfofillhtml.showHtml(data.htmlData);
							   $("#taught_list_text").html($.htmlformat(str));
				                if (smate.resumepsninfo.refreshInformation != null) {//刷新信息完整度
				                	smate.resumepsninfo.refreshInformation();
				                }
				                smate.resumepsninfo.taught.editClose();
						} else {
							 $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						}
					},
					error : function(){
				          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
						
						}
				});
			}
	}

	//页面处理关键词
smate.fillpsninfo.dealDiscKeyToPage =function(keyStr){
		 var _htmlArr = [];
         //data = {"text_format":_text_format}
         //关键词编辑后，填充数据到html中
         Resume.personInfo.ajaxLoadHtml("viewKey.html", null, null, function(_data){
             var _val = {
                 "keys": keyStr
             };
             var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
             _htmlArr.push(_text_format);
             $("#key_list_text div:eq(1)").html( _htmlArr.join("") + "<a  id=\"key_list_a\" ><i class=\"icon_edit\"></i></a>");
             $("#key_list_a").click( Resume.personInfo.key.editHtml);
             if (Resume.personInfo.refreshInformation != null) {//刷新信息完整度
                 Resume.personInfo.refreshInformation();
             }
         });
         Resume.personInfo.key.editClose();
	}
	
	

	//工作部门去掉灰色
smate.fillpsninfo.worDeptDelStyle = function(_this){
		_this.style="width:320px;";
		_this.value="";
	}
	
	//记住排除的关键词 
smate.fillpsninfo.addExcludeWords = function(text){
		
		$.ajax({
			url : snsctx + '/profile/excludeWords',
			type : 'POST',
			dataType : 'json',
			data : {"ostr":text},
			success : function(data){ 
			},
			error : function(){
		          $.smate.scmtips.show('error', smate.resumepsninfo.locale["fail"]);
				}
		});
	}
	
	//校验时间 
smate.fillpsninfo.dataDeal_dataAvailable =function(fromY,fromM,toY,toM){
		
		if(fromY==-1||toY==-1){
			return 0;
		}
		if(toY!=0&&toY<fromY){
			return -1;
		}
		if(toY!=0&&toY==fromY&&fromM>toM){
			return -1;
		}
		return 1;
	}
	
	/**
	 * 所教课程校验
	 */
smate.fillpsninfo.dataDeal_taughtAValidate = function(){
	    $("#psnInfoFillTeachCourse").validate({
	        rules: {
	            "taught": {
	                maxlength: 2500
	            }
	        },
	        messages: {
	            "taught": {
	                maxlength: $.format(smate.resumepsninfo.locale["taught"], 2500)
	            }
	        },
	        
	    });
	};
	
	function ajaxSessionTimeoutHandler(data){

		var  ajaxTimeOutFlag=data['ajaxSessionTimeOut'];
		if(ajaxTimeOutFlag!=null && typeof ajaxTimeOutFlag!="undefined" && ajaxTimeOutFlag=='yes'){
				jConfirm(Personal_FillPsnInfo.locale["loginTimeout"], Personal_FillPsnInfo.locale["prompt"], function(result) {
				if (result)
						parent.window.location.href=ctx;
				});
				return true;
	       }
		return false;
	}
	
	
	/**
	 * 鼠标点击-工作经历编辑
	 */
	smate.fillpsninfo.mouseClickWorkAction = function (des3Id){
		//点击映射到编辑按钮上
		Resume.personInfo.work.editData(des3Id, function(data){
            //填充数据
            Resume.personInfo.work.editHtml(data);
        });
	};

	/**
	 * 鼠标点击-教育经历编辑
	 */
	smate.fillpsninfo.mouseClickEduAction = function (des3Id){
		//点击映射到编辑按钮上
		 Resume.personInfo.edu.editData(des3Id, function(data){
	         //填充数据
	         Resume.personInfo.edu.editHtml(data);
	     });
	};
   
	/**
	 * 鼠标点击-有列表
	 */
	smate.fillpsninfo.mouseClick2Action = function (obj){
		smate.fillpsninfo.commonClickAction(obj);
		$(obj).parent().find(".icon_edit").click();
	};
	
	/**
	 * 鼠标点击-有列表---所教课程
	 */
	smate.fillpsninfo.mouseClick3Action = function (obj){
		smate.fillpsninfo.commonClickAction(obj);
		$(obj).parent().parent().find(".icon_edit").click();
	};
	
	/**
	 * //去掉水印
	 */
	smate.fillpsninfo.commonClickAction = function(obj){
		var objtext = $(obj).find('.myfnt');
		 if(objtext.html().replace(/(^\s*)|(\s*$)/g,"")==(noInformation+"&nbsp;&nbsp;&nbsp;")){
			 objtext.html("");
		 }else{
			 
		 }
	};
	
	/**
	 * 鼠标点击-项目
	 */
	smate.fillpsninfo.mouseClickPrjAction = function (des3Id){
		window.open(snsctx+'/project/edit?des3Id='+des3Id+'&backType=0&menuId=1400');
	};

