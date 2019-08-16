/**
 * @license Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';

config.toolbar_Basic =
[
	['Bold', 'Italic', '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','TextColor','BGColor', 'Font', 'FontSize','-', 'uploadimg','-', 'Maximize' ]
];
config.toolbar_Full =
[
    { name: 'document', items : [ 'Source','-','DocProps','Preview','Print','-','Templates' ] },
    { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
    { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
    { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 
        'HiddenField' ] },
    '/',
    { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
    { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',
    '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
    { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
    { name: 'insert', items : [ 'Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe','uploadimg' ] },
    '/',
    { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
    { name: 'colors', items : [ 'TextColor','BGColor' ] },
    { name: 'tools', items : [ 'Maximize', 'ShowBlocks' ] }
];
	// Remove some buttons, provided by the standard plugins, which we don't
	// need to have in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';
	config.enterMode = 2;
	config.height = '500px';
	config.language = locale=='zh_CN'?'zh-cn':'en';
	config.toolbar ='Basic';
    config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;'+ config.font_names;
	config.fontSize_style={element:"span",styles:{'font-size':'#(size)','line-height':'100%','word-break': 'break-all', 'word-wrap':'break-word','max-width':'600px'},overrides:[{element:"font",attributes:{size:null}}]};
	config.pasteFromWordKeepsStructure = false; 
	config.pasteFromWordIgnoreFontFace = false;
	config.pasteFromWordRemoveStyle = false;
	config.forcePasteAsPlainText =false;  
	config.pasteFromWordRemoveFontStyles = false;
	config.shiftEnterMode = 1;
	config.extraPlugins = 'uploadimg';
	/*The allowedContent in the code above is set to true to disable content filtering. 
	Setting this option is not obligatory, but in full page mode there is a strong chance that one 
	may want be able to freely enter any HTML content in source mode without any limitations.  */
	config.allowedContent = true; 
	config.removePlugins = 'elementspath'; //去掉下方标签解释的显示
	
};
CKEDITOR.cleanWord=function(a,b){return a;};//从word复制到ckeditor，格式保留