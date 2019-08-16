var urlTypes={
		/** thinckbox_friend_start 联系人**/
		"showfriend":"/friend/ajaxSelectFriends?firstFlag=first&TB_iframe=true&height=440&width=560",//我的联系人
		"sendInviteMsg":"/friend/initMsg?TB_iframe=true&height=175&width=420",//发送联系人邀请
		"friendsfriend":"/friend/ofFriends?TB_iframe=true&height=323&width=640",//联系人的联系人
		"friendLabelManagement":"/#TB_inline?height=260&amp;width=440&amp;inlineId=friendLabelManagement",//联系人标签管理
		"addRequests":"/friend/ajaxInitAddRequests?TB_iframe=true&height=260&width=490",//添加联系人请求
		"friendsOfFriend":"/friend/ofFriends?TB_iframe=true&height=307&width=640",//联系人的联系人
		
		/** thinckbox_friend_end 联系人**/
		
		/** thinckbox_msgbox_start 动态、消息 **/
		"showMsgBox":"/message/inside/showMsgBox?&TB_iframe_msg=true&height=360&width=650",	
		"sendMessage":"/msgbox/ajaxSendMessageBox?TB_iframe=true&rank_menu=true&height=405&width=740",//发送消息弹出对话框(从联系人列表抽取为弹出框控件,群组成员页面已用,调用本控件的页面需设置变量msgboxConfig，msgType).
		/** thinckbox_msgbox_end 动态、消息 **/
		

		/** thinckbox_group_start 群组**/
		"groupSelfEdit":"/group/selfEdit?TB_iframe_msg=true&height=202&width=450",		//个人设置(群组动态页面的设置).
		"groupMemInvite":"/group/memberInvite?TB_iframe_msg=true&height=231&width=550",//邀请联系人(群组联系人页面的操作).
		"groupMemberInviteMsg":"/group/memberInviteMsg?TB_iframe_msg=true&height=265&width=560",//发送邀请信息(群组联系人页面的操作).
		"groupInviteSharePub":"/message/inside/showMsgBoxSharePub?TB_iframe_msg=true&height=240&width=550",//邀请成员共享成果(群组联系人页面的操作).
		"groupInviteShareFile":"/message/inside/showMsgBoxShareFile?TB_iframe_msg=true&height=240&width=550",//邀请成员共享文件(群组联系人页面的操作).
		"groupFolderManageDiv":"/#TB_inline?height=285&amp;width=450&amp;inlineId=groupFolderManageDiv",//群组文件夹管理面板
		"groupPopGroupMember":"/group/groupPopGroupMember?TB_iframe=true&height=402&width=560",//群组成员弹出框
		"groupBriefImg":"/group/ajaxBriefImg?TB_iframe=true&amp;modal=true&amp;height=365&amp;width=580",//群组简介图片
		"groupCopyCourse_en":"/group/copyCourse?TB_iframe_msg=true&amp;height=235&amp;width=590",//复制教学群组
		"groupCopyCourse_cn":"/group/copyCourse?TB_iframe_msg=true&amp;height=210&amp;width=520",//复制教学群组
		/** 查看导入表格详细信息 SCM-3117_2014-1-6_tsz  **/
		"groupMemInviteDetail":"/group/memberInviteDetail?TB_iframe_msg=true&height=350&width=450",//邀请联系人-查看导入表格详细信息(群组联系人页面的操作).
		/** thinckbox_group_end 群组**/
		
		/** thinckbox_cv_start 简历**/
		"cvPub":"/personalResume/ajaxResumePub?TB_iframe=true&height=500&width=900",//简历成果
		"cvPrj":"/personalResume/ajaxResumePrj?TB_iframe=true&height=500&width=900",//简历项目
		"cvPsnRecommend":"/cvPsnRecommend/ajaxPsnByCvType?TB_iframe=true&height=450&width=625",//简历智能推荐
		"cvCustomImg":"/personalResume/ajaxCvCustomImg?TB_iframe=true&amp;modal=true&amp;height=365&amp;width=580",//简历自定义图片
		"cvAddCustom":"/personalResume/ajaxPreAddCustom?TB_iframe=true&amp;height=370&amp;width=940",//新增简历自定义选项
		"cvAddOnlyName":"/personalResume/ajaxPreAddOnlyName?TB_iframe=true&amp;height=150&amp;width=940",//新增简历，只有名称
		"cvAddClassic":"/personalResume/ajaxPreAddClassic?TB_iframe=true&amp;height=330&amp;width=940",//新增简历(经典)
		/** thinckbox_cv_end 简历**/
		
		/** thinckbox_frofile_start 人员信息 **/
		"loadEmail":"/profile/loadEmail?TB_iframe=true&height=340&width=615",
		"loadAdjusting":"/profile/ajaxAdjusting?TB_iframe=true&height=230&width=362",
		/** thinckbox_frofile_end 人员信息 **/
		

		/** thinckbox_publication_start 成果 **/
		"pubLabelManagement":"/#TB_inline?height=274&amp;width=444&amp;inlineId=pubLabelManagement",
		"collectEditPerson":"/publication/ajaxEditPerson?TB_iframe=true&height=220&width=550",
		"collectEditExperience":"/publication/ajaxEditExperience?TB_iframe=true&height=380&width=670",
		"publicationComment":"/comment/publication/ajaxpubcommentlist?TB_iframe=true&height=400&width=700",
		"collectToFriendRecommendRes":"/friend/ajaxRecommendPub?TB_iframe=true&height=295&width=620",
		"pubLabelManagementSimple":"/#TB_inline?height=270&amp;width=444&amp;inlineId=pubLabelManagementSimple",//成果标签管理--成果列表
		/** thinckbox_publication_end 成果 **/
		
		
		/** thinckbox_project_start 项目 **/
		"projectLabelManagement":"/#TB_inline?height=274&amp;width=444&amp;inlineId=projectLabelManagement",
		"projectComment":"/comment/project/ajaxMaintprjcommentlist?TB_iframe=true&height=400&width=700",
		"enterPrjLabel":"/project/ajaxShowPrjAdd?TB_iframe=true&height=290&width=570",
		/** thinckbox_project_end 项目 **/
		
		
		/** thinckbox_reference_start 文献 **/
		"refLabelManagement":"/#TB_inline?height=270&amp;width=440&amp;inlineId=refLabelManagement",
		"referenceComment":"/comment/publication/ajaxrefcommentlist?TB_iframe=true&height=400&width=700",
		/** thinckbox_reference_end 文献 **/
		
		
		"uploadFulltext":"/folder/showfullltext?TB_iframe=true&height=340&width=570",
		
		
		/** thinckbox_file_start 文件**/
		"fileLabelManagement":"/#TB_inline?height=274&amp;width=444&amp;inlineId=fileLabelManagement",
		"fileComment":"/group/ajaxMyFileComment?TB_iframe=true&height=400&width=700",
		/** thinckbox_file_end 文件**/
			
		/** isi. */
		"keyword_recycleBin" : "/isis/dialog/deletedKeywordList?TB_iframe=true&height=480&width=655", // 关键词回收站
		"hotKeyword_recycleBin" : "/isis/dialog/deletedHotWordList?TB_iframe=true&height=500&width=800", // 热词回收站
		"synonym_recycleBin" : "/isis/dialog/deletedSynonymList?TB_iframe=true&height=500&width=800", // 同义词回收站
		"journal_recycleBin" : "/isis/dialog/deletedJournalList?TB_iframe=true&height=500&width=760", // 期刊回收站
		"goupFileImportList" : "/group/ajaxMyFileList?TB_iframe=true&height=480&width=810", //我的文件导入
		
		/**rol.*/
		"rolLoadEmail":"/profile/loadEmail?TB_iframe=true&height=340&width=615",//修改首要邮件
		"rolInsPsns":"/publication/autoInsPersons?TB_iframe=true&height=440&width=560", //单位人员
		"rolSelectUnit":"/#TB_inline?height=265&amp;width=600&amp;inlineId=unitRoles", //选择部门
		"rolAddJrptForUnit":"/bjournal/addJrptForUnit?TB_iframe=true&height=480&width=810",
		
		/** 成果推广-读者推荐-编辑收件人 */
		"pub_readerCmd_editReceiver":"/spread/readerCmd/getPubReaderPage?TB_iframe=true&height=400&width=650",
		"recommendPubToGroup":"/spread/ajaxRecommendGroup?TB_iframe=true&height=500&width=660",
		"recGroupPubs":"/spread/ajaxRecommendPubs?TB_iframe=true&height=500&width=815",
		
		/** thickbox_settings_start 设置*/
		"addMergeCount":"/user/setting/enterAddMerge?TB_iframe=true&height=180&width=470",
			
	   /**成果文献保存成功提示对话窗**/
		"prsaveSuccessTip":"/pubref/savesuccesstip?TB_iframe=true&height=115&width=440",
		
		/** 进度弹出层 */	
		"progressbar":"/profile/import/ajaxprogressbar?TB_iframe=true&amp;modal=true&amp;height=90&amp;width=230",
			
		/** 新系统的成果收藏 */
		"newPubSave" : "/pubweb/grouppub/ajaximportmypub/show?TB_iframe=true&height=160&width=700"


};