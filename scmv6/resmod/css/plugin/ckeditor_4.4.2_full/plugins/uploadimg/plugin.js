/**
 * @author zk
 */
CKEDITOR.plugins.add('uploadimg', {
    lang: 'en-us,en,zh-cn,zh', 
	icons: 'uploadimg',
    init: function(editor){	
	/*
		CKEDITOR.dialog.add('uploadimgdialog', this.path + 'dialogs/uploadimg.js');
        editor.addCommand('uploadimgdialog', new CKEDITOR.dialogCommand('uploadimgdialog', {
            exec: function(editor){
				var des3GroupId = $("#des3GroupId").val();
				$.ajax({//获取唯一的dataDes3Id
			        url: ctxpath + '/group/ajaxGetDataDes3Id',
			        type: 'post',
			        dataType: 'json',
					data: {
			            "groupId": des3GroupId
			        },
			        success: function(data){
						dataDes3Id = data.msg;
			        }
			    });
			   editor.openDialog( this.dialogName );
            }
        }));
        */
		editor.addCommand('uploadimage', {
	        exec: function( editor ) {
				var des3InspgId = $("#des3InspgId").val();
	        	//区分浏览器，不同浏览器 生成动态id不一样，原因不明。 tsz 
				//改成class 绑定 tsz
	        		$(".cke_button__uploadimg").thickbox({
	    				type: "inspgProfileImg"
	    				}); 
	        	
	        	
	        }
	    } );
        editor.ui.addButton('uploadimg', {
            label: editor.lang.common.image,
            command: 'uploadimage',
            toolbar: 'insert'
        });
    }
});




