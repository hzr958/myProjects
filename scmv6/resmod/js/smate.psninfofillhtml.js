var smate = smate ? smate : {};

smate.psninfofillhtml = smate.psninfofillhtml ? smate.psninfofillhtml : {};

var cacheObj = {};

smate.psninfofillhtml.showHtml =function(_htmlData){

	 if (typeof(_htmlData.curStatus) == "undefined") { 
		 $("#psnInfoFill").hide("slow");
		   return;
		} 
	 var targetMethod="psnInfoFill_"+_htmlData.curStatus;
	 smate.psninfofillhtml.dealHtml[targetMethod](_htmlData);
	 
 }	 
smate.psninfofillhtml.dealHtml ={
	
		
		//工作过的地方（学校）
		psnInfoFill_1:	function(_htmlData){
	     //填充数据到html中
		
			var insName = _htmlData.oName;
			if(locale=="en_US"){
				if( _htmlData.oEnName==null){
					insName=_htmlData.oName;
				}
				else{
					insName=_htmlData.oEnName;
				} 
			}else if(insName==null){
				insName=_htmlData.oEnName;
			}
		    ajaxLoadHtml("fillpsninfo_workPlace.html", null, null, function(_data){
		         var _val = {
		             "insName": insName,
		             	"insId":_htmlData.oId
		         };
		     _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	
		     $("#psnInfoFill").html(_text_format);
		     $("#psnInfoFill").slideDown("slow");
		     });
	    
		},
		//工作过的地方(部门)
		psnInfoFill_2 : function(_htmlData){
			
			var insName = _htmlData.oName;
			if(locale=="en_US"){
				if( _htmlData.oEnName==null){
					insName=_htmlData.oName;
				}
				else{
					insName=_htmlData.oEnName;
				} 
			}else if(insName==null){
				insName=_htmlData.oEnName;
			}
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_workDept.html", null, null, function(_data){
	         var _val = {
	        		  "insName": insName,
	               	"insId":_htmlData.oId
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#psnInfoFill").slideDown("slow");
	         $('#workDept_input1').watermark({
	 			tipCon : Personal_FillPsnInfo.locale["workDept_content_tip3"]
	 		});
	         $('#workDept_input2').watermark({
		 			tipCon : Personal_FillPsnInfo.locale["workDept_content_tip4"]
		 		});
				
			 $("#workDept_input1").complete({
                "ctx": snsctx,
                "key": "insUnit",
				    "formatItem": $.ins_unit_formatItem,
					 "width": 200,
					 "extraParams":{insName:function(){return $.trim($("#workDept_input1").attr("insName"));}}
              });//单位院系自动提示下拉框
             
	         });
	     },
			
		//工作过的地方(时间)
	     psnInfoFill_3 : function(_htmlData){
	    	 
	    	 var insName = _htmlData.oName;
				if(locale=="en_US"){
					if( _htmlData.oEnName==null){
						insName=_htmlData.oName;
					}
					else{
						insName=_htmlData.oEnName;
					} 
				}
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_workTime.html", null, null, function(_data){
	         var _val = {
	             "insName": insName,
	             "insId":_htmlData.oId
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         //初始化年份下拉框
	         var startYear =1900;
	         var currentYear = new Date().getFullYear();
	         yearOptions(parseInt(startYear), parseInt(currentYear), "psnInfo_work_fromYear");
	         yearOptions(parseInt(startYear), parseInt(currentYear), "psnInfo_work_toYear");
	         $("#psnInfoFill").slideDown("slow");
	         $("#psnInfoFill_isPresent").click(function(){
	      	   clickIsPresent();
	         });
	     });
	  
		},

		//推荐职称
		psnInfoFill_4:	function(_htmlData){
	     //填充数据到html中
			
			var rePos = _htmlData.oName;
			if("en_US"==locale)
				rePos = _htmlData.oEnName;
	    ajaxLoadHtml("fillpsninfo_workRecommendPosition.html", null, null, function(_data){
	         var _val = {
	        		 "posId":_htmlData.oId,
	        		 "recommend_position":rePos
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#psnInfoFill").slideDown("slow");
	     });
		},
		
		//自填职称
		psnInfoFill_5 :function(_htmlData){
			   //填充数据到html中
		    ajaxLoadHtml("fillpsninfo_workPosition.html", null, null, function(_data){
		         var _val = {
		         };
		     var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
		     $("#psnInfoFill").html(_text_format);
		     $("#selfPositionInput").complete({
		            "ctx": snsctx,
		            "key": "position",
		            "bind": {
		                "code": "base_posId"
		            }
		        });//职称自动提示下拉框
		     $("#psnInfoFill").slideDown("slow");
		     });
		},
		
		//熟悉研究领域推荐
		psnInfoFill_6:function(_htmlData){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_ResearchArea_know.html", null, null, function(_data){
	         var _val = {
	          //   "insName": _htmlDate.insName
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	        
	         $("#psnInfoFillKey").autoword({
	             "words_max": 50,
	             "select": $.auto["key_word"],
	             "delClick": function(val,label){
	            	 FillPsnInfo.addExcludeWords(label);
	             }
	         });//单位自动提示下拉框
	         $("#psnInfoFill").slideDown("slow");
	         //填充推荐的关键词
	         autoFillResArea(_htmlData.resAreaList);
	     });
		},
		
		
		//从事研究领域
		psnInfoFill_7 :function(_htmlData){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_ResearchArea_on.html", null, null, function(_data){
	         var _val = {
	         //    "insName": _htmlDate.insName
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	          $("#psnInfoFillKey").autoword({
	             "words_max": 50,
	             "select": $.auto["key_word"],
	             "watermark_flag":true,
	             "watermark_tip":smate.resumepsninfo["watermark_tips"]
	             });//单位自动提示下拉框
	         $("#psnInfoFill").slideDown("slow");
	     });
		},
		//哪所学校学习过
		psnInfoFill_8:function(_htmlDate){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_schoolStudy.html", null, null, function(_data){
	         var _val = {
	          //   "insName": _htmlDate.insName
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#studyDegree").complete({
		            "ctx": snsctx,
		            "key": "degree"
		        });//学位自动提示下拉框
	           
	         $('#studyDegree').watermark({
		 			tipCon : Personal_FillPsnInfo.locale["schoolStudy_degree"]
		 		});
	         $("#studySchoolName").complete({
		            "ctx": snsctx,
		            "key": "ins_name"
		        });//学校自动提示下拉框
	         $('#studySchoolName').watermark({
		 			tipCon : Personal_FillPsnInfo.locale["schoolStudy_name"]
		 		});
	         
	         $('#studyDeptName').watermark({
		 			tipCon : Personal_FillPsnInfo.locale["schoolStudy_dept"]
		 		});
	         $("#psnInfoFill").slideDown("slow");
	     });
		},
		
		//教育经历 (时间)
		psnInfoFill_9:function(_htmlData){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_eduTime.html", null, null, function(_data){
	         var _val = {
	             "insName": _htmlData.oName,
	             "insId":_htmlData.oId
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         //初始化年份下拉框
	         var startYear =1900;
	         var currentYear = new Date().getFullYear();;
	         yearOptions(parseInt(startYear), parseInt(currentYear), "psnInfo_work_fromYear");
	         yearOptions(parseInt(startYear), parseInt(currentYear) + 7, "psnInfo_work_toYear");
	         $("#psnInfoFill").slideDown("slow");
	         $("#psnInfoFill_isPresent").click(function(){
	      	   clickIsPresent();
	         });
	     });
	  
		},
		//展现头衔
		psnInfoFill_10:function(_htmlData){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_titloShow.html", null, null, function(_data){
	         var _val = {
	             "insName": _htmlData.insName
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#psnInfoFill").slideDown("slow");
	     });
		},
		
		
		//工作内容
		psnInfoFill_11:function(_htmlData){
	     //填充数据到html中
			var toYear =_htmlData.toYear;
			if(toYear==0)
				toYear=  new Date().getFullYear();
			var insDate = _htmlData.fromYear+"-"+toYear;
	    ajaxLoadHtml("fillpsninfo_JobContent.html", null, null, function(_data){
	         var _val = {
	             "insName": _htmlData.insName,
	             "insDate":insDate
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#psnInfoFill").slideDown("slow");
	     });
		},
		//所教课程
		psnInfoFill_12:function(_htmlDate){
	     //填充数据到html中
	    ajaxLoadHtml("fillpsninfo_teachCourse.html", null, null, function(_data){
	         var _val = {
	            // "insName": _htmlDate.insName
	         };
	         var _text_format = $.scmformat(_data["text_format"], _val);//格式化数据
	         $("#psnInfoFill").html(_text_format);
	         $("#psnInfoFillTeachCourse").autoword({
	             "words_max": 50,
	         	"filter": ["<", ">", "&", "_", "%"],//过滤特殊字符
	             "select": $.auto["taught"]
	         });
	         $("#psnInfoFill").slideDown("slow");
	         
	     });
		}
		
		
};
 
//填充关键词
function autoFillResArea(value){
	
	 var _zhKey = $.autoword["psnInfoFillKey"];
	var areaList = value;
	for(var area in areaList){
		_zhKey.put("",areaList[area]);//添加关键词
	}
}

//选取当前时,将至的年月份为不可选
function clickIsPresent(){
	var obj = document.getElementsByName("psnInfoFill_isPresent"); 
	if(obj[0].checked){
		$("#psnInfo_work_toYear").attr("disabled","disabled");
		$("#psnInfo_work_toMonth").attr("disabled","disabled");
	}else{
		$("#psnInfo_work_toYear").removeAttr("disabled");
		$("#psnInfo_work_toMonth").removeAttr("disabled");
	}
}
/*
 * 工作经历时间规则
 */
 function editDateRule(){
    if ($("#psnInfoFill_isPresent").is(":checked")) 
        return true;
    var fromYear = parseInt($("#psnInfo_work_fromYear").val());
    var fromMonth = isNaN($("#psnInfo_work_fromMonth").val()) ? -1 : parseInt($("#psnInfo_work_fromMonth").val());
    var toYear = parseInt($("#psnInfo_work_toYear").val());
    var toMonth = isNaN($("#psnInfo_work_toMonth").val()) ? -1 : parseInt($("#psnInfo_work_toMonth").val());
    if (fromYear > toYear) 
        return false;
    if (fromMonth > -1 && toMonth > -1 && fromYear == toYear && fromMonth > toMonth) 
        return false;
    return true;
};


/**
 * 产生年
 * */
function yearOptions(start, end, select_id){
	if (start > end) {
        var tmp = start;
        start = end;
        end = tmp;
    }
    var select = $("#" + select_id);
    for (; end >= start; end--) {
        var option = $("<option value='" + end + "' >" + end + "</option>");
        select.append(option);
    }
}

	/**
	 * 填充个人补充信息弹出窗
	 */
	function ajaxLoadHtml(name, obj, parent, init){//ajax加载Html
	    $.ajax({
	        url: resmod + "/js_v5/home/personalCenter/html/" + name,
	        type: 'post',
	        dataType: 'html',
	        data:{"rand":Math.random()},
	        success: function(_text){
	            var _text_format = $.scmformat(_text, Personal_FillPsnInfo.locale);//格式化数据
	            	//缓存html
	         
	            var htmlName = name.substring(0,name.indexOf("."));
	            var cache =cacheObj[htmlName];
	            if(typeof(cache) =="undefined")
	            	cacheObj[htmlName]=_text_format;
	            if (obj != null) {
	                obj.html(_text_format);
	                }
	            if (parent != null) {
	                parent.before(obj);//插入格式化后的内容
	               }
	            if (typeof init != "undefined") {
	                init({
	                    "text_format": _text_format
	                 });
	                }
	        }
	    });
	};
