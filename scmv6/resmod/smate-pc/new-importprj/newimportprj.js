
var scmpcimport = function(optins){
    var defaults = {
		data: ""
	};
    const languageSettings={};
    const multilanguageSettings = {
    		"title": ["Add Publications", "添加新成果"],
    		"importlabel": ["Search and import from ScholarMate or ", "从科研之友检索导入或使用"],
    		"searchtype": ["Search from Database", "联邦检索"],
    		"manualtype": ["Manual Entry", "手工录入"],
    		"filetype": ["Import from File", "文件导入"],
    		"searchboxholder": ["Enter search criteria", "输入成果信息检索"],
    		"closebtn": ["Close", "关闭"],
    		"importbtn": ["Import", "导入"],
    		"searchPaper": ["Search Paper", "检索论文"],
    		"searchPatent": ["Search Patent", "检索专利"]
    	};
    	Object.keys(multilanguageSettings).forEach(function (x) {
    		if (window.locale === "en_US") {
    			languageSettings[x] = multilanguageSettings[x][0];
    		} else {
    			languageSettings[x] = multilanguageSettings[x][1];
    		}
    	});
    var opts = Object.assign(defaults,optins); 
    var newcontent = opts.data;
    if(document.getElementsByClassName("new_import-container").length==0){
        var coverbox = document.createElement("div");
        coverbox.className = "background-cover";
    	document.body.appendChild(coverbox);
    	var element = '<div class="new_import-container_header">'
                    +'<span>'+languageSettings.title+'</span>'
                    +'<i class="new_import-container_close  list-results_close"></i>'
                    +'</div>'
                    +'<div class="new_import-container_body">'
                    +'<div class="new_import-container_body-func">'
	            	+'<span>'+languageSettings.importlabel+'</span>'
	            	+'<div class="new_import-container_body-func_list" onclick="Pub.searchPubImport();">'+languageSettings.searchtype+'</div>'
	            	+'<div class="new_import-container_body-func_list" onclick="Pub.manualImportPub();">'+languageSettings.manualtype+'</div>'
	            	+'<div class="new_import-container_body-func_list" onclick="Pub.fileImport();">'+languageSettings.filetype+'</div>'
	            	/*+'<div class="new_import-container_body-func_list">导入</div>'*/
	                +'</div>'
	                +'<div class="new_import-container_head_box">'
                    +'<div class="new_import-container_body-search" style="position: relative;">'
            	    +'<input type="text" id="pubSearchInput" class="searchString" placeholder="'+languageSettings.searchboxholder+'">'
            	    +'<i class="searchbox__icon" onclick="Pub.searchPub($(\'.searchString\').val())"></i>'
                    +'</div>'
                    +'<div class="new_import-container_body-select">'
                    +'<div class="new_import-container_body-select_item">'
                    +'<i class="check_language check_paper check_language-selected"></i>'
                    +'<span class="new_import-container_body-select_detaile">'+languageSettings.searchPaper+'</span>'
                    +'</div>'
                    +'<div  class="new_import-container_body-select_item">'
                    +'<i class="check_language check_patent"></i>'
                    +'<span class="new_import-container_body-select_detaile">'+languageSettings.searchPatent+'</span>'
                    +'</div>'
                    +'</div>'
                    +'</div>'
                    +'<div class="new_import-container_body-content">'
                    +'<div class="main-list__list" list-main="addPubList">'
                    +'</div>'
                    +'</div>'
                    +'</div>'
                    +'<div class="new_import-container_footer">'
        	        +'<div class="new_import-container_footer-cancle">'+languageSettings.closebtn+'</div>'
        	        +'<div class="new_import-container_footer-confirm" onclick="Pub.importPubs()">'+languageSettings.importbtn+'</div>'
                    +'</div>';
                    var parntbox = document.createElement("div");
	                parntbox.className = "new_import-container";
	                parntbox.innerHTML = element;
	                coverbox.appendChild(parntbox);
	                var allwidth = coverbox.offsetWidth;
	                var allheight = coverbox.offsetHeight;
	                const selfwidth = parntbox.clientWidth;
	                const selfheight = parntbox.clientHeight;
	                var setwidth = (allwidth - selfwidth)/2 + "px"; 
				    var setheight =  (allheight - selfheight)/2 + "px";
				    parntbox.style.left = setwidth;
				    parntbox.style.bottom = setheight;
				    window.onresize = resize;
				    function resize(){
				    	var newwidth = coverbox.offsetWidth;
	                    var newheight = coverbox.offsetHeight;
				        var setwidth = (newwidth - selfwidth)/2 + "px"; 
				        var setheight =  (newheight - selfheight)/2 + "px";
				        parntbox.style.left = setwidth;
				        parntbox.style.bottom = setheight;
				    }
				    document.getElementsByClassName("new_import-container_close")[0].onclick = function(){
				    	parntbox.style.bottom= - selfheight + "px";
				        setTimeout(function(){
				        	coverbox.removeChild(parntbox);
				        	coverbox.style.display = "none";
				        	document.body.removeChild(coverbox);
				        	Pub.reloadCurrentPubList();
				        } ,500);
				    }
				    document.onkeydown = function(event){
		                if(event.keyCode == 27){
		                    event.stopPropagation();
		                    event.preventDefault();
		                    parntbox.style.bottom= - selfheight + "px";
	                        setTimeout(function(){
	                            document.getElementsByClassName("background-cover")[0].removeChild(document.getElementsByClassName("new_import-container")[0]);
	                            coverbox.style.display = "none";
	                            document.body.removeChild(coverbox);
	                            Pub.reloadCurrentPubList();
	                        } ,500);
		                }
		            }
				    document.getElementsByClassName("new_import-container_footer-cancle")[0].onclick = function(){
				    	parntbox.style.bottom= - selfheight + "px";
				        setTimeout(function(){
				        	coverbox.removeChild(parntbox);
				        	coverbox.style.display = "none";
				        	document.body.removeChild(coverbox);
				        	Pub.reloadCurrentPubList();
				        } ,500);
				    }
				    document.getElementsByClassName("new_import-container_footer-cancle")[0].onmouseover = function(){
				    	this.style.border='1px solid #288aed';
				    	this.style.color='#288aed';
				    }
				    document.getElementsByClassName("new_import-container_footer-cancle")[0].onmouseout = function(){
				    	this.style.border='1px solid #fff';
				    	this.style.color='#333';
				    }
		            var singlleist = document.getElementsByClassName("check_language");
		            for(var i = 0; i < singlleist.length; i++){
		                 singlleist[i].onclick = function(){
		                	if(!this.classList.contains("check_language-selected")){
		                		document.getElementsByClassName("check_language-selected")[0].classList.remove("check_language-selected");
		                		this.classList.add("check_language-selected");
		                		Pub.searchPub(document.getElementById("pubSearchInput").value);
		                	}
		                 }
		            }
    }
    var placeholder="";
    document.getElementById("pubSearchInput").onfocus = function(){
    	placeholder = this.placeholder;
    	this.placeholder="";
    }
    document.getElementById("pubSearchInput").onblur = function(){
    	this.value=this.value.trim();
    	this.placeholder=placeholder;
    }
    document.getElementById("pubSearchInput").onkeydown = function(e){
    	var event = e.which || e.keyCode;
		if(event == 13){
			this.value=this.value.trim();
			var content = this.value;
			Pub.searchPub(content);
		}
    }
}
function closeNewImportBox(){
   if(document.getElementsByClassName("background-cover")&&document.getElementsByClassName(".new_import-container")){
       
       var coverbox = document.getElementsByClassName("background-cover")[0];
       var parntbox = coverbox.querySelector(".new_import-container");
       coverbox.removeChild(parntbox);
       coverbox.style.display = "none";
       document.body.removeChild(coverbox);
       
    }
}