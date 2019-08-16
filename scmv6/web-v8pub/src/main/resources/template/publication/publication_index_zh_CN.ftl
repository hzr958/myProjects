<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="科研之友，科研社交网络，科研创新生态环境，学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研诚信，同行专家，科研系统，科研项目申请书，ISIS系统。"/>
<meta name="description" content="科研之友：科研社交网络，成就科研梦想。学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研系统。" /> 
<title>成果列表|科研之友</title>
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
			<div class="sm_logo_zh_CN">
				<a href="/" title="首页"
					alt="首页"></a>
			</div>
			<div class="sm_top_nav">
				<span id="back_span">
					<a href="/" rel="nofollow">
						返回首页
					</a> 
					|
				</span> 
					<a href="/help" target="_blank" rel="nofollow"> 帮助中心 </a> | 
					<a href="pub_A_en_US.html">&nbsp;English&nbsp;</a>
			</div>
		</div>
	</div>
<div class="agency_box">
	<div class="menu">
		<div class="menu-bjleft"></div>
		<div class="menu-bjcenter">
			<div class="m-text">
				<span class="fcu14">浏览站内成果</span>
			</div>
		</div>
		<div class="menu-bjright"></div>
	</div>
	<div class="agency_bg01"></div>
	<div class="agency_bg02">
		<div class="agency_search" style="margin:0px 20px;display:none;">
            	<div class="ag_search_box">
                	<span class="cuti Fleft">站内成果搜索：</span>
                    <input id="keyword" name="keyword" type="text" class="inp_text" style="width:270px; height:22px; line-height:22px;"/>
                    <a href="javascript:searchPub(1);" class="uiButton uiButtonConfirm mleft5" title="搜索" style="padding:3px 25px;">搜索</a>
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
							<a id="other" href="pub_other_zh_CN.html">other</a>
						</li>
					</#if>
				<#else>
					<#if (currentLetter == letter)>
						<li><a id="${letter}" class="abc_hover">${letter}</a></li>
					<#else>
						<li><a id="${letter}" href="pub_${letter}_zh_CN.html">${letter}</a></li>
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
					<a href="http://www.irissz.com" class="Blue" target="_blank">深圳市科研之友网络服务有限公司</a> 
					<a class="Blue" style="color:#333;" href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备16046710号-1</a>
					<img src="/resmod/images_v5/beian/beian.png" style="width: 12px;"/>
					<a class="Blue" style="color:#333;"  href="http://www.miitbeian.gov.cn" target="_blank">粤公网安备 4403052000213</a>
				</div>
				<div class="footer-right">
					<a href="/resscmwebsns/html/policy_zh_CN.html" class="Blue" target="_blank">
						隐私政策
					</a> | 
					<a href="/resscmwebsns/html/condition_zh_CN.html" class="Blue" target="_blank">
						服务条款
					</a> | 
					<a href="/resscmwebsns/html/contact_zh_CN.html" class="Blue" target="_blank">
						联系我们
					</a> | 
		     		<a href="/resscmwebsns/html/res_download_zh_CN.html" class="Blue" target="_blank">
		     			下载 
		     		</a>
				</div>
	</div>
</div>
</body>
</html>
