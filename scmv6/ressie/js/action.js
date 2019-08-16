//导航条效果
function scrollul(s){
	var $ul = $(s+" ul");
 	var liHeight = $ul.find("li:last").height();
 	$ul.animate({marginTop : liHeight+20 +"px"},1000,function(){
		$ul.find("li:last").prependTo($ul)
		$ul.find("li:first").hide();
		$ul.css({marginTop:0});
		$ul.find("li:first").fadeIn(1000);
	});
};
function setTab(name,cursel,n){
	for(i=1;i<=n;i++){
		var menu=document.getElementById(name+i);
		var con=document.getElementById("con_"+name+"_"+i);
		if(menu !=null){
			menu.className=i==cursel?"hover":"";
		}
		if(con != null){
			con.style.display=i==cursel?"block":"none";
		}
	}
};

<!--导航条效果-->
$(function(){
	var $a=$('#join li');
	$a.click(function(){
		var $this=$(this);
		var $t=$this.index();
		$a.removeClass();
		$this.addClass('hover');
	});
});
// 二级菜单效果
$(function(){
	var $a=$('#college a');
	$a.click(function(){
		var $this=$(this);
		var $t=$this.index();
		$a.removeClass();
		$this.addClass('hover');
	});
});
// 模拟复选框
/*
 * $(function () { $('.checkbox').on('click',function(){
 * if($(this).siblings("input[type='checkbox']").attr('checked')){ $(this).removeClass('cur');
 * $(this).siblings("a").css("color","#333");
 * 
 * $(this).siblings("input[type='checkbox']").removeAttr('checked') } else{ $(this).addClass('cur');
 * $(this).siblings("a").css("color","#1265cf");
 * $(this).siblings("input[type='checkbox']").attr('checked','checked') } }); });
 */
// 登录框1
$(function(){
	$(".text_login").focus(function(){
		if($(this).attr('id')=='register_password1'){
			$(this).hide();
			$('#register_password2').show();
			$('#register_password2').focus();
		}
	});
	$(".text_login").blur(function() {
		if ($(this).attr('id') == 'register_password2' && $(this).val() == '') {
			$(this).hide();
			$('#register_password1').show();
			$('#register_password1').val('密码');
			$('#register_password1').css("color","#999");
		}

	});
});
// 登录框2
$(function(){
	$(".text_login").focus(function(){
		if($(this).val()=='用户名' || $(this).val()=='密码'){
			$(this).val('');
			$('#password,#userName').css("color","#333");
		}
		if($(this).attr('id')=='register_password1'){
			$(this).hide();
			$('#password').show();
			$('#password').focus();
		}
	});
	$(".text_login").blur(function(){
//	
		if($(this).attr('id')=='userName' && $(this).val()=='' ){
			$(this).val('用户名');
			$('#userName').css("color","#c1c1c1");
		}
	});
	$(".password_bg").focus(function(){
		if($('#userName').val()=='用户名'){
			$('#userName').css("color","#c1c1c1");
		}
	});
});

// 登录框2
$(function(){
	$(".sie_text_login").focus(function(){
        if($(this).val()=='用户名' || $(this).val()=='密码'){
            $(this).val('');
            $('#register_password2,#register_uname1').css("color","#333");
        }
        if($(this).attr('id')=='register_password1'){
            $(this).hide();
            $('#register_password2').show();
            $('#register_password2').focus();
        }
    });
    $(".sie_text_login").blur(function(){
// if($(this).attr('id')=='register_password21' && $(this).val()==''){
// $(this).hide();
// $('#register_password1').show();
// $('#register_password1').val('密码');
// $('#register_uname1').css("color","#c1c1c1");
// }
        if($(this).attr('id')=='register_uname1' && $(this).val()=='' ){
            $(this).val('用户名');
            $('#register_uname1').css("color","#c1c1c1");
        }
    });
    $(".password_bg").focus(function(){
        if($('#register_uname1').val()=='用户名'){
            $('#register_uname1').css("color","#c1c1c1");
        }
    });
});
// 注册框
$(function(){
	$(".text_log").focus(function(){
		if($(this).attr('id')=='register_password5'){
			$(this).hide();
			$('#register_password6').show();
			$('#register_password6').focus();
		}
		if($(this).attr('id')=='register_password7'){
			$(this).hide();
			$('#register_password8').show();
			$('#register_password8').focus();
		}
	});
	$(".text_log").blur(function() {
		if ($(this).attr('id') == 'register_password6' && $(this).val() == '') {
			$(this).hide();
			$('#register_password5').show();
			$('#register_password5').val('密码');
			$('#register_password5').css("color","#999");
		}
		if ($(this).attr('id') == 'register_password8' && $(this).val() == '') {
			$(this).hide();
			$('#register_password7').show();
			$('#register_password7').val('重复密码');
			$('#register_password7').css("color","#999");
		}
	});
});

$(function(){
	if($(".left-wrap").offset()==null){
		return;
	}
	var navH = $(".left-wrap").offset().top;
	$(window).scroll(function(){
		var scroH = $(this).scrollTop();
		if(scroH>=navH){
			$(".left-wrap").css({"position":"fixed","top":0});
			return;
		}else if(scroH<navH){
			$(".left-wrap").css({"position":"static"});
			return;
		}
	});
});

$(function(){
	if(document.getElementsByClassName("conter_finished").length>0){
		 document.getElementsByClassName("conter_finished")[0].style.height = document.documentElement.clientHeight - 250 + "px";
	}
})

// 二级菜单
$(function(){
	var $a=$('#sie_join_nav1 a');
	$a.click(function(){
		var $this=$(this);
		var $t=$this.index();
		$a.removeClass();
		$this.addClass('hover');
	})
});
$(function(){
	var menuLi = $(".sie_top_nav").parent("li");
	for(i=0; i<menuLi.length;i++){
		if($(menuLi[i]).hasClass("hover")){
			$(".sie_top_nav")[i].style.display = "block";
		}else{
			$(".sie_top_nav")[i].style.display = "none";
		}
	}
});
$(function(){
	var $a=$('#sie_join_nav2 a');
	$a.click(function(){
		var $this=$(this);
		var $t=$this.index();
		$a.removeClass();
		$this.addClass('hover');
	})
});


$(function(){
	{
		var $a=$('#sie-hot li');
		$a.click(function(){
			var $this=$(this);
			// var $t=$this.index();
			// $a.removeClass();
			$this.addClass('hover');
		})
	}
});

