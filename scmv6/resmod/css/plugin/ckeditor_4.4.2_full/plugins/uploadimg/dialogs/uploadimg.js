CKEDITOR.dialog.add( 'uploadimgdialog', function( editor ) {	
    return {
        title: editor.lang.image.upload,
        minWidth: 470,
        minHeight: 200,
		button: [ CKEDITOR.dialog.cancelButton ],
        contents: [
            {
                id: 'tab-uploadimg',
                elements: [
                    {
                        id: 'ele-uploadimg',
						type: 'html',
                        //html:'<iframe id="groupUploadImgFrame" src="/scmwebsns/group/ajaxUploadImgPage" frameborder="0" scrolling="no"></iframe>'						
						html: '<div id="group_image_box_upload_panel"></div>',
						onShow: function(){
							/**
							$("#group_image_box_upload").remove();
							$("#group_image_box_upload_panel").append( '<div id="group_image_box_upload"></div>' );
							$("#group_image_box_upload").imgcutupload({
								"ctxpath": ctxpath,
								"resmod": resmod,
								"imgType": 7,
								"locale": locale,
								"dataDes3Id": dataDes3Id
							});
							**/
						}
					}   
                ]
            }
        ],
		onShow:function(){
			$(".cke_dialog_ui_hbox_first").hide();
			imgUrlPath = "";
		},     
        onOk: function() {
			if(domainPath==imgUrlPath||""==imgUrlPath){
				return;
			}
			var imageElement = editor.document.createElement( 'img' );
			imageElement.setAttribute('src',imgUrlPath);
			editor.insertElement( imageElement );
			//this.destroy();
          /*
 
		    var dialog = this;

            var abbr = editor.document.createElement( 'abbr' );
            abbr.setAttribute( 'title', dialog.getValueOf( 'tab-basic', 'title' ) );
            abbr.setText( dialog.getValueOf( 'tab-basic', 'abbr' ) );

            var id = dialog.getValueOf( 'tab-adv', 'id' );
            if ( id )
                abbr.setAttribute( 'id', id );

            editor.insertElement( abbr );
*/
            
        }
    };
});