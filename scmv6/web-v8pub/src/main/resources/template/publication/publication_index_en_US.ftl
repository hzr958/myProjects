<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="ScholarMate, Research Social Network, Research and Innovation Eco-system, Academic Marketing, Science Marketing, Scholar Marketing, Research Dissemination, Research Projects, Research Publications, Research Information System, Research Proposals, ISIS."/>
<meta name="description" content="ScholarMate connects people to research and innovate smarter" />
<title>Publications | ScholarMate</title>
<link href="/resscmwebsns/css_v5/public.css" rel="stylesheet" type="text/css" />
<link href="/resscmwebsns/css_v5/index.css" rel="stylesheet" type="text/css" />
<link href="/resscmwebsns/css_v5/header.css" rel="stylesheet" type="text/css" />
<link href="/resscmwebsns/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link href="/resscmwebsns/css_v5/login.css" rel="stylesheet" type="text/css" />
<link href="/resscmwebsns/css_v5/agency.css" rel="stylesheet" type="text/css" />
<script src="/resscmwebsns/js_v5/jquery.js"></script>
<script type="text/javascript">
var ctxpath =  "/scmwebsns";
	function searchPub(num){
		$.ajax({
			        url: ctxpath + '/indexSearch/ajaxSearchPub',
			        type: 'post',
			        dataType: 'json',
					data: {
			            "keyword": document.getElementById("keyword").value,
			            "pageNum":num
			        },
			        success: function(data){
			        	if(data&&data.result=='success'){
			        		$("[class='person_list div_psn']").html(data.object);
			        	}else{
			        		$("[class='person_list div_psn']").html("");
			        	}
			        }
			        ,
			        error:function(e){
			        	$("[class='person_list div_psn']").html("");
			        }
			    });
	}
</script>
</head>
<body>
	<div id="scholarmate_hearder">
		<div class="scholarmate_top">
			<div class="sm_logo_en_US">
				<a href="/" title="Home" alt="Home"></a>
			</div>
			<div class="sm_top_nav">
				<span id="back_span">
					<a href="/" rel="nofollow">Home</a> 
					|
				</span> 
				<a href="/help" target="_blank" rel="nofollow">Learning Center</a> | 
					<a href="pub_A_zh_CN.html">&nbsp;中文版&nbsp;</a>
			</div>
		</div>
	</div>
<div class="agency_box">
	<div class="menu">
		<div class="menu-bjleft"></div>
		<div class="menu-bjcenter">
			<div class="m-text">
				<span class="fcu14">Publications</span>
			</div>
		</div>
		<div class="menu-bjright"></div>
	</div>
	<div class="agency_bg01"></div>
	<div class="agency_bg02">
		<div class="agency_search" style="margin:0px 20px;display:none;">
            	<div class="ag_search_box">
                	<span class="cuti Fleft">Search Publications: </span>
                    <input name="" type="text" class="inp_text" style="width:270px; height:22px; line-height:22px;"/>
                    <a href="javascript:searchPub(1);" class="uiButton uiButtonConfirm mleft5" title="search" style="padding:3px 25px;">search</a>
                </div>
                <div class="clear" style="height:0px; overflow:hidden;"></div>
            </div>
		<div class="abc_list">
			<input type="hidden" id="codeNum" name="codeNum" />
			<div id="searchResult">
			<ul>
				<#list letterList as letter>
				<#if (letter == 'other')>
					<#if (currentLetter == letter)>
						<li style="border-right: 1px solid #cbd9e1;">
							<a id="other" class="abc_hover">other</a>
						</li>
					<#else>
						<li style="border-right: 1px solid #cbd9e1;">
							<a id="other" href="pub_other_en_US.html">other</a>
						</li>
					</#if>
				<#else>
					<#if (currentLetter == letter)>
						<li><a id="${letter}" class="abc_hover">${letter}</a></li>
					<#else>
						<li><a id="${letter}" href="pub_${letter}_en_US.html">${letter}</a></li>
					</#if>
				</#if>
				</#list>
			</ul>
			</div>
			<div class="clear" style="height: 1px; overflow: hidden;"></div>
		</div>
		<!-- 此处为具体成果链接数据 -->
		<div class="person_list div_psn" id="${currentLetter}">${content}</div>
		<div class="clear" style="height: 1px; overflow: hidden;"></div>
	</div>
	<div class="agency_bg03"></div>
</div>
<div id="footer">
	<div class="box_footer">
				<div class="footer-left">
					&copy;2019
					<a href="http://www.irissz.com" class="Blue" target="_blank">ScholarMate</a> 
					<a class="Blue" style="color:#333;" href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备16046710号-1</a>
					<img src="/resmod/images_v5/beian/beian.png" style="width: 12px;"/>
					<a class="Blue" style="color:#333;"  href="http://www.miitbeian.gov.cn" target="_blank">粤公网安备 4403052000213</a>
				</div>
				<div class="footer-right">
					<a href="/resscmwebsns/html/policy_en_US.html" class="Blue" target="_blank">
						Privacy
					</a> | 
					<a href="/resscmwebsns/html/condition_en_US.html" class="Blue" target="_blank">
						Terms
					</a> | 
					<a href="/resscmwebsns/html/contact_en_US.html" class="Blue" target="_blank">
						Contact Us
					</a> | 
		     		<a href="/resscmwebsns/html/res_download_zh_CN.html" class="Blue" target="_blank">
		     			Download 
		     		</a>
				</div>
	</div>
</div>
</body>
</html>
