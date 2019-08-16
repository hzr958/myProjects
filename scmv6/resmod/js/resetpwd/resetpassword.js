//兼容IE，IE不支持closest()方法
if (window.Element && !Element.prototype.closest) {
	Element.prototype.closest = function (s) {
		var matches = (this.document || this.ownerDocument).querySelectorAll(s),
			i, el = this;
		do {
			i = matches.length;
			while (--i >= 0 && matches.item(i) !== el) {};
		} while ((i < 0) && (el = el.parentElement));
		return el;
	};
}

//input标签校验
Element.prototype.inputCheck = function () {
	var $inputPattern = new RegExp(this.getAttribute("pattern"));
	var $inputValue = this.value;
	var $inputSection = this.closest(".input-container")
	if ($inputSection.classList.contains("invalid-data")) {
		return false;
	} else {
		if ((this.hasAttribute("required") && !this.value) || (this.hasAttribute("pattern") && (!$inputPattern.test($inputValue)))) {
			this.closest(".input-container").classList.add("invalid-data");
			this.closest(".input-section").classList.add("invalid-data");
			return false;
		} else {
			return true;
		}
	}
}

//form校验，返回bool值
Element.prototype.formCheck = function () {
	if (this.tagName.toLowerCase() != "form") {
		try { //如果提交按钮绑定不是form，抛出异常
			throw "Only form can be submitted.";

		} catch (e) {
			console.error(e);
		}
		return false;
	} else {
		var $inputField = this.querySelectorAll('input');
		var $formValidate = true;
		for (var i = 0; i < $inputField.length; i++) {
			if ($inputField[i].readOnly || $inputField[i].disabled) {
				continue;
			} else {
				$formValidate = $inputField[i].inputCheck() ? $formValidate : $inputField[i].inputCheck();
			}
		}
		if ($formValidate) {
			return true;
		} else {
			return false;
		}
	}
};

window.onload = function () {
	var locale = "${locale}"  ;
	var url = window.location.href
	//从邮件跳转的链接，提示不同
	var fromEmail = url.indexOf("&from=email") > 0 ?true : false ;
	if(fromEmail){
		resetpasswordTitle = resetpasswordTitleForEmail ;
	}
	if (url.indexOf("needresetpwd=true") > 0) { //判断触发条件
		var resetPassword = document.createElement("div");
		resetPassword.className = "background-cover";
		resetPassword.innerHTML = '<div class="dialogs-container" style="width: 320px;">' +
			'<div class="dialogs-content-container padding-24">' +
			'<div class="reset-password_title">'+resetpasswordTitle+'</div>' +
			'<form id="dev_dialogs_reset_password">' +
			'<div class="form-row">' +
			'<div class="input-container width-12">' +
			'<div class="input-title">'+resetpasswordEmail+'</div>' +
			'<div class="input-section-cluster">' +
			'<div class="input-section width-12">' +
			'<div class="input-area">' +
			'<input readonly value="" id="dev_reset_password_account">' + //传账号邮箱
			'</div>' +
			'<div class="input-underline_primary"></div>' +
			'<div class="input-underline_focused"></div>' +
			'</div>' +
			'</div>' +
			'<div class="input-helper-text input-character-counter"></div>' +
			'</div>' +
			'</div>' +
			'<div class="form-row">' +
			'<div class="input-container width-12">' +
			'<div class="input-title">'+resetpasswordNewPwd+'</div>' +
			'<div class="input-section-cluster">' +
			'<div class="input-section width-12">' +
			'<div class="input-area">' +
			'<input type="password" pattern="^[a-zA-Z0-9]{6,20}$" min-length=6 max-length=20 id="dev_reset_password_new" tabindex="1">' +
			'<i class="material-icons password-visibility"></i>' +
			'</div>' +
			'<div class="input-underline_primary"></div>' +
			'<div class="input-underline_focused"></div>' +
			'</div>' +
			'</div>' +
			'<div class="input-helper-text input-character-counter" helper-text="'+resetpasswordTip+'" invalid-message="'+resetpasswordTip+'"></div>' +
			'</div>' +
			'</div>' +
			'<div class="form-row">' +
			'<div class="input-container width-12">' +
			'<div class="input-title">'+resetpasswordNewPwdSure+'</div>' +
			'<div class="input-section-cluster">' +
			'<div class="input-section width-12">' +
			'<div class="input-area">' +
			'<input type="password" id="dev_reset_password_confirm" tabindex="2">' +
			'<i class="material-icons password-visibility"></i>' +
			'</div>' +
			'<div class="input-underline_primary"></div>' +
			'<div class="input-underline_focused"></div>' +
			'</div>' +
			'</div>' +
			'<div class="input-helper-text input-character-counter" helper-text="" invalid-message=""></div>' +
			'</div>' +
			'</div>' +
			'</form>' +
			'</div>' +
			'<div class="dialogs-footer no-border">' +
			'<button class="button button-flat_primary" submit-form="dev_dialogs_reset_password" id="dev_reset_password_confirm">'+resetpasswordSure+'</button>' +
			'<button class="button button-flat_transparent-secondary" id="dev_reset_password_close">'+resetpasswordCancle+'</button>' +
			'</div>' +
			'</div>';
		document.getElementsByTagName("body")[0].appendChild(resetPassword); //添加浮层显示
		//传账号邮箱
		var username = url.split("username=")[1];
		username = username.split("&")[0];
		$("#dev_reset_password_account").val(username);
		if(fromEmail){
			$("#dev_reset_password_close").remove();
		}
		
		var inputList = document.getElementsByTagName("input"); //input动画事件
		for (var i = 0; i < inputList.length; i++) {
			var $this = inputList[i];
			if ($this.closest(".input-container")){
				if ($this.value) {
					$this.closest(".input-container").classList.add("hasContent");
				}
				if ($this.hasAttribute("readonly")) {
					$this.closest(".input-container").classList.add("readonly");
				}
				$this.addEventListener("focus", function () {
					this.closest(".input-container").classList.add("isFocused");
					this.closest(".input-section").classList.add("isFocused");
				});
				$this.addEventListener("blur", function () {
					this.closest(".input-container").classList.remove("isFocused");
					this.closest(".input-section").classList.remove("isFocused");
					if (this.value) {
						this.closest(".input-container").classList.add("hasContent");
					} else {
						this.closest(".input-container").classList.remove("hasContent");
					}
				});
			} else {
				continue;
			}
		}

		var pswVisibility = document.getElementsByClassName("password-visibility"); //密码显示开关
		for (var i = 0; i < pswVisibility.length; i++) {
			var $this = pswVisibility[i];
			$this.addEventListener("click", function () {
				var $visbility = this.parentElement.querySelector("input");
				if ($visbility.getAttribute("type") == "password") {
					$visbility.setAttribute("type", "text");
				} else {
					$visbility.setAttribute("type", "password");
				}
			});
		}

		var newPassword = document.getElementById("dev_reset_password_new"), 
			confirmPassword = document.getElementById("dev_reset_password_confirm");
		var newValue, confirmValue;
		newPassword.addEventListener(("input" || "blur" || "change"), function () {
			newValue = this.value;
		});
		confirmPassword.addEventListener(("input" || "blur" || "change"), function () {
			confirmValue = this.value;
		});
		newPassword.addEventListener(("blur" || "change"), function () { //新密码校验判断
			var confirmCheck = confirmPassword.closest(".input-container");
			var pswPattern = new RegExp(this.getAttribute("pattern"));
			if (!pswPattern.test(newValue)) {
				this.closest(".input-container").classList.add("invalid-data");
				this.closest(".input-section").classList.add("invalid-data");
			} else {
				this.closest(".input-container").classList.remove("invalid-data");
				this.closest(".input-section").classList.remove("invalid-data");
			}
			if (confirmValue && (newValue != confirmValue)) {
				confirmCheck.querySelector(".input-helper-text").setAttribute("invalid-message", resetpasswordNotSame);
				confirmCheck.classList.add("invalid-data");
				confirmCheck.querySelector(".input-section").classList.add("invalid-data");
			} else {
				confirmCheck.classList.remove("invalid-data");
				confirmCheck.querySelector(".input-section").classList.remove("invalid-data");
			}
		});
		confirmPassword.addEventListener(("blur" || "change"), function () { //确认密码校验判断
			if (newValue != confirmValue) {
				this.closest(".input-container").querySelector(".input-helper-text").setAttribute("invalid-message", resetpasswordNotSame);
				this.closest(".input-container").classList.add("invalid-data");
				this.closest(".input-section").classList.add("invalid-data");
			} else {
				this.closest(".input-container").classList.remove("invalid-data");
				this.closest(".input-section").classList.remove("invalid-data");
			}
		});

		var submitFormButtom = document.querySelectorAll("button[submit-form]"); //提交form判断
		for (var i = 0; i < submitFormButtom.length; i++) {
			var $this = submitFormButtom[i]
			var $formId = $this.getAttribute("submit-form");
			var $compatibleForm = document.getElementById($formId);
			$this.addEventListener("click", function () {
				var $submitCheck = $compatibleForm.formCheck();
				if (!$submitCheck) {
					return false;
				} else {
					//提交表单所需要执行代码;
		    		var new_pwd = strEnc(newValue,username);
		    		var confirm_pwd = strEnc(confirmValue,username);
					var post_data = {'newPwd':new_pwd,'confirmPwd':confirm_pwd};
					$.ajax( {
						url : '/psnweb/personal/ajaxresetpwd',
						type : 'post',
						dataType:'json',
						data : post_data,
						success : function(data) {
							if(data.result == 'error'){
								$("#dev_reset_password_new").val("");
								$("#dev_reset_password_confirm").val("");
							} else {
								scmpublictoast("重置密码成功",1000);
								var $currentDialog = $this.closest(".background-cover");
								$currentDialog.parentNode.removeChild($currentDialog);
							}
						},
						error:function(){
							$("#dev_reset_password_new").val("");
							$("#dev_reset_password_confirm").val("");
						}
					});
				}
			});
		}

		var closeReset = document.getElementById("dev_reset_password_close"); //关闭窗口
		closeReset.addEventListener("click", function () {
			var $currentDialog = this.closest(".background-cover");
			$currentDialog.parentNode.removeChild($currentDialog);
			//更新sys_user_login表的last_login_time字段
			$.ajax({
				url : "/psnweb/personal/ajaxupdatelogintime",
				type : "post",
				dataType : "json",
				data : {},
				success : function(data) {
					if (data.result == "success") {
						//alert("后台已自动更新登录时间");
					}
				}
			});
		});

	} else {
		return false;
	}
};