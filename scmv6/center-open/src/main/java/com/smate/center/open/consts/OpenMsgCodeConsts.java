package com.smate.center.open.consts;

import com.smate.center.open.service.pubinfo.PubInfoVerifyUtil;

/**
 * 信息代码 常量类
 * 
 * @author tsz
 */
public class OpenMsgCodeConsts {

  /*
   * 信息代码第一位数字说名 0：传参错误 1：运行异常 2：webservice resful 入口处理 4：webservice resful 业务处理 8：webservice
   * resfulDAO处理
   * 
   * 0+2 0+4 0+8 1+2 1+4 1+8
   * 
   * 6种情况 上面是第一位 如 scm-901 属于 dao处理运行异常 后两位没有特别含义 需要保证错误编码的 ====唯一====
   */

  public static final String SCM_000 = "scm-000 数据处理成功";

  public static final String SCM_111 = "scm-111 系统异常";
  // 参数 错误
  public static final String SCM_201 = "scm-201 openid不能为空";
  public static final String SCM_202 = "scm-202 token不能为空";
  public static final String SCM_203 = "scm-203 token格式不正确";
  public static final String SCM_204 = "scm-204 token未开放权限";
  public static final String SCM_205 = "scm-205   请求服务类型参数不能为空";
  public static final String SCM_206 = "scm-206 openid或guid格式不正确";
  public static final String SCM_207 = "scm-207 openid或guid不能匹配到token对应的授权系统";
  public static final String SCM_208 = "scm-208   请求服务类型类型参数错误";
  public static final String SCM_209 = "scm-209  项目数据参数data不能为空";
  // 微信参数想关
  public static final String SCM_210 = "scm-210  微信群发消息数据参数不能为空";
  public static final String SCM_211 = "scm-211  微信个人消息数据参数不能为空";
  public static final String SCM_212 = "scm-212   微信群发消息openId不正确";
  public static final String SCM_213 = "scm-213   微信消息数据 参数 格式不正确 不能转换成map";
  public static final String SCM_214 = "scm-214   微信消息  消息内容不能为空";
  public static final String SCM_215 = "scm-215   微信消息  消息类型不能为空";
  public static final String SCM_216 = "scm-216   微信消息 消息发送机构 不能为空";
  public static final String SCM_217 = "scm-217   微信消息 消息时间描述不能为空";
  public static final String SCM_218 = "scm-218   微信消息 显示内容长度不正确";
  public static final String SCM_219 = "scm-219   微信消息  消息类型格式不正确";
  public static final String SCM_220 = "scm-220   微信消息  没有找到 对应的消息类型";
  public static final String SCM_221 = "scm-221   微信消息  用户尊称提示不能为空";
  public static final String SCM_222 = "scm-222   微信消息  补充说明不能为空";
  public static final String SCM_223 = "scm-223   微信消息  openId没有绑定微信用户";

  // isis成果在线 交互参数错误
  public static final String SCM_224 = "scm-224   服务扩展参数不能为空";
  public static final String SCM_225 = "scm-225   服务参数格式不正确,必须是json格式";
  public static final String SCM_226 = "scm-226   服务参数格式不正确,服务参数不能正确的转换成Map";
  public static final String SCM_227 = "scm-227   具体成果在线服务类型参数不能为空";
  public static final String SCM_228 = "scm-228   具体成果在线服务类型参数不正确";

  // 人员注册（人员同步） 参数错误
  public static final String SCM_229 = "scm-229   人员注册 来源系统不能为空";
  public static final String SCM_230 = "scm-230   人员注册 人员基本数据不能为空";
  public static final String SCM_231 = "scm-231   人员注册 人员基本数据 构造对象失败";
  public static final String SCM_232 = "scm-232   人员注册 人员基本数据 人员姓名 name不能为空";
  public static final String SCM_233 = "scm-233   人员注册 人员基本数据 人员邮件 email不能为空";
  public static final String SCM_234 = "scm-234   人员注册 人员基本数据 人员guid 不能为空";
  public static final String SCM_235 = "scm-235   人员注册  guid已经同步过了";

  // 人员同步 参数错误
  public static final String SCM_236 = "scm-236   人员同步 来源系统不能为空";
  public static final String SCM_237 = "scm-237   人员同步 人员基本数据不能为空";
  public static final String SCM_238 = "scm-238   人员同步 人员基本数据 构造对象失败";
  public static final String SCM_239 = "scm-239  人员同步 人员基本数据 人员姓名 psnId不能为空";
  public static final String SCM_240 = "scm-240  人员同步 来源系统不正确";

  // 群组创建 参数错误
  public static final String SCM_241 = "scm-241   群组创建 来源系统不能为空";
  public static final String SCM_242 = "scm-242   群组创建 群组基本数据不能为空";
  public static final String SCM_243 = "scm-243   群组创建 创建人psnId不能为空";
  public static final String SCM_244 = "scm-244   群组创建 群组基本数据 构造对象失败";
  public static final String SCM_245 = "scm-245   群组创建 群组基本数据 群组名称 groupName不能为空";
  public static final String SCM_246 = "scm-246   群组创建 群组基本数据 群组分类 groupCategory不能为空";
  public static final String SCM_247 = "scm-247   群组创建 群组基本数据 群组分类groupCategory要为数字类型:10表示兴趣群组,11表示项目群组";
  public static final String SCM_248 = "scm-248   群组创建 群组基本数据 群组公开类型 openType不能为空";

  // 获取自动登陆链接 参数错误标记
  public static final String SCM_249 = "scm-249  服务参数 自动登陆类型不能为空";
  public static final String SCM_250 = "scm-250  服务参数 自动登陆类型不正确";

  // 成果记录日志
  public static final String SCM_251 = "scm-251  服务参数 业务系统导入的成果id为空";

  // 获取动态token
  public static final String SCM_252 = "scm-252  服务参数 动态token类型不能为空";
  public static final String SCM_253 = "scm-253  服务参数 动态token类型不正确";

  // 互联互通，获取群组信息
  public static final String SCM_254 = "scm-254  服务参数  群组的groupCode不能为空";
  public static final String SCM_255 = "scm-255  服务参数 quicklyCreateGroup格式错误 , 值必须为  true！";
  public static final String SCM_256 = "scm-256  服务参数 具体服务类型参数prjStatus不能为空";
  public static final String SCM_257 = "scm-257  服务参数 groupData不能为空 ";
  public static final String SCM_258 = "scm-258  服务参数 fromSys不能为空 ";
  public static final String SCM_259 = "scm-259  服务参数 groupData不是json格式的字符串 ";
  public static final String SCM_260 = "scm-260  服务参数 groupData解析成对象出错！ ";
  // 互联互通，第三方项目
  public static final String SCM_261 = "scm-261  群组名称//项目名称groupName ,不能为空！ ";
  public static final String SCM_262 = "scm-262  项目状态prjStatus长度只能等于1,且为数字！ ";
  public static final String SCM_263 = "scm-263  项目金额amount整数长度不能大于12位  ,且为数字！ ";
  public static final String SCM_264 = "scm-264  参与人员信息格式必须为 json ,且长度小于3000 ！ ";
  public static final String SCM_265 = "scm-265  中文关键词格式必须为 json ,且长度小于1000  ！ ";
  public static final String SCM_266 = "scm-266  英文关键词格式必须为 json ,且长度小于1000 ！ ";
  public static final String SCM_267 = "scm-267  服务参数 groupCode无效 或者失效";

  public static final String SCM_268 = "scm-268 服务参数  pubIdList列表不能为空，或pubIdList列表不符合要求";
  public static final String SCM_269 = "scm-269 服务参数  成果Id错误 ，查询不到该成果";
  public static final String SCM_270 = "scm-270服务参数  成果Id，该成果被删除";
  public static final String SCM_271 = "scm-271服务参数  成果Id，没有权限查询改成果";

  public static final String SCM_272 = "scm-272具体服务类型参数pageNo不能为空";
  public static final String SCM_273 = "scm-273具体服务类型参数pageSize不能为空";
  public static final String SCM_274 = "scm-274具体服务类型参数pageNo格式不正确";
  public static final String SCM_275 = "scm-275具体服务类型参数pageSize格式不正确";

  public static final String SCM_276 = "scm-276没有成果被更新";
  public static final String SCM_277 = "scm-277没有查询到成果";
  public static final String SCM_278 = "scm-278 业务系统未开通此服务";
  // grp 创建群组信息
  public static final String SCM_279 = "scm-279 具体服务类型参数grpName为空";
  public static final String SCM_280 = "scm-280 具体服务类型参数grpCategory为空 或格式不正确";
  public static final String SCM_281 = "scm-281 具体服务类型参数firstCategoryId为空 或格式不正确";
  public static final String SCM_282 = "scm-282 具体服务类型参数[ 项目群组］的projectNo为空 或格式不正确";
  public static final String SCM_283 = "scm-283 具体服务类型参数  grpData为空 或格式不正确";
  public static final String SCM_284 = "scm-284 具体服务类型参数  grpDescription为空 ";

  public static final String SCM_285 = "scm-285 具体服务类型参数  邮件email不能为空 ";

  // 短地址
  public static final String SCM_286 = "scm-286 具体服务类型参数  创建人createPsnId不能为空 ";
  public static final String SCM_287 = "scm-287 具体服务类型参数  shortUrlParamet格式必须是json格式 ";
  public static final String SCM_288 = "scm-288 具体服务类型参数  type不能为空或者类型出错 ";
  public static final String SCM_289 = "scm-289 具体服务类型参数  shortUrlParamet不能为空 ";
  public static final String SCM_290 = "scm-290 具体服务类型参数  shortUrlParamet参数中des3GrpId不能为空 ";
  public static final String SCM_291 = "scm-291 具体服务类型参数  shortUrlParamet参数中des3PsnId不能为空 ";
  // 2017-08-- demo接口改造
  public static final String SCM_292 = "scm-292 具体服务类型参数  data参数中psnId格式不正确 ";
  public static final String SCM_293 = "scm-293 具体服务类型参数searchKey不能为空 ";
  public static final String SCM_294 = "scm-294 具体服务类型参数dataType数据不正确 ";

  // solr 人员信息更新
  public static final String SCM_295 = "scm-295 具体服务类型参数  psn_id 不能为空";
  // solr 基金信息更新
  public static final String SCM_2955 = "SCM_2955 具体服务类型参数  fund_id 不能为空";

  // 检查openid
  public static final String SCM_296 = "scm-296 具体服务类型参数  open_id 不能为空";

  // 检查用户名密码
  public static final String SCM_297 = "scm-297 具体服务类型参数  username 不能为空";
  public static final String SCM_298 = "scm-298 具体服务类型参数  password 不能为空";



  // 获取期刊信息
  public static final String SCM_2001 = "scm-2001 具体服务类型参数  jname 不能为空";
  public static final String SCM_2002 = "scm-2002 具体服务类型参数  psnId 不能为空，必须为数字";
  public static final String SCM_2003 = "scm-2003 具体服务类型参数  缺少issn参数";
  public static final String SCM_2004 = "scm-2004 具体服务类型参数  缺少jnameFrom参数";

  // 单位id 没有权限
  public static final String SCM_2005 = "scm-2005 insId不正确，没有权限调用该接口";
  public static final String SCM_2006 = "scm-2006 pubConfirmId不属于当前调用者，没有权限调用该接口";
  public static final String SCM_2007 = "scm-2007 pubConfirmId不能为空，没有权限调用该接口";
  public static final String SCM_2008 = "scm-2008 该pubConfirmId成果已认领或者拒绝";
  public static final String SCM_2009 = "scm-2009 成果认领,出现异常";

  public static final String SCM_2010 = "scm-2010 具体服务类型参数loginEmail不能为空";
  public static final String SCM_2011 = "scm-2011 具体服务类型参数userName不能为空";
  public static final String SCM_2012 = "scm-2012 具体服务类型参数dataType不能为空";
  public static final String SCM_2013 = "scm-2013 具体服务类型参数loginEmail格式不正确";
  public static final String SCM_2014 = "scm-2014 超过该接口的调用次数，稍后再试";
  public static final String SCM_2015 = "scm-2015 具体服务类型参数insId不能为空，必须为数字";
  public static final String SCM_2016 = "scm-2016 具体服务类型参数targetToken不能为空,必须为8位";
  public static final String SCM_2017 = "scm-2017 具体服务类型参数psnId不能为空，必须为数字";
  public static final String SCM_2018 = "scm-2018 具体服务类型参数relation不能为空，必须为true or false";
  public static final String SCM_2019 = "scm-2019 具体服务类型参数targetToken不存在";
  public static final String SCM_2020 = "scm-2020 具体服务类型参数psnId不存在";
  public static final String SCM_2021 = "scm-2021 当前群组不属于当前用户";
  public static final String SCM_2031 = "scm-2031 具体服务类型参数participantNames不能为空，且长度不能超过500";
  public static final String SCM_2032 = "scm-2032 具体服务类型参数pubInfoList不能为空";
  public static final String SCM_2033 = "scm-2033 具体服务类型参数psnInfo不能为空";
  public static final String SCM_2034 = "scm-2034 具体服务类型参数psnInfo对象的name和email不能为空";
  public static final String SCM_2037 = "scm-2037 具体服务类型参数psnInfo对象的name长度不能超过" + PubInfoVerifyUtil.name_max_len;
  public static final String SCM_2038 = "scm-2038 具体服务类型参数psnInfo对象的email长度不能超过" + PubInfoVerifyUtil.email_max_len;
  public static final String SCM_2039 = "scm-2039 具体服务类型参数psnInfo对象的的phone长度不能超过" + PubInfoVerifyUtil.tel_max_len;

  public static final String SCM_2043 = "scm-2043 验证通过";
  public static final String SCM_2044 = "scm-2044 成果信息缺失";
  public static final String SCM_2045 = "scm-2045 authorNames不匹配";
  public static final String SCM_2046 = "scm-2046 title不匹配";
  public static final String SCM_2047 = "scm-2047 doi不匹配";
  public static final String SCM_2048 = "scm-2048 journalName不匹配";
  public static final String SCM_2049 = "scm-2049 publishYear不匹配";
  public static final String SCM_2050 = "scm-2050 fundingInfo不匹配";
  public static final String SCM_2051 = "scm-2051 没有匹配到基准库成果";
  public static final String SCM_2052 = "scm-2052 title不能为空";
  public static final String SCM_2053 = "scm-2053 title不能超过2000个字符";
  public static final String SCM_2054 = "scm-2054 authorNames不能超过1000个字符";
  public static final String SCM_2055 = "scm-2055 keyCode不能为空";
  public static final String SCM_2056 = "scm-2056 keyCode不能超过20个字符";
  public static final String SCM_2057 = "scm-2057 authorNames不能为空";
  public static final String SCM_2058 = "scm-2058 authorNames不包含参与人";

  public static final String SCM_2059 = "scm-2059 doi为空";
  public static final String SCM_2060 = "scm-2060 journalName为空";
  public static final String SCM_2061 = "scm-2061 publishYear为空";
  public static final String SCM_2062 = "scm-2062 fundingInfo为空";
  public static final String SCM_2063 = "scm-2063 title为空";
  public static final String SCM_2064 = "scm-2064 authorNames为空";
  public static final String SCM_2065 = "scm-2065 作者名少了";
  public static final String SCM_2066 = "scm-2066 作者名多了";
  public static final String SCM_2067 = "scm-2067 作者名序不对";
  public static final String SCM_2068 = "scm-2068 参与人名序正确，非参与人名序错误";
  public static final String SCM_2069 = "scm-2069 参与人名序错误";
  public static final String SCM_2070 = "scm-2070 基准库成果不包含参与人";
  public static final String SCM_2071 = "scm-2071 专利论文不验证";
  public static final String SCM_2072 = "scm-2072 作者名过长";
  public static final String SCM_2073 = "scm-2073 参与人名序提前";
  public static final String SCM_2074 = "scm-2074 参与人名序提后";
  public static final String SCM_2075 = "scm-2075 crossref的数据不验证";
  public static final String SCM_2035 = "scm_2035 参数data解密失败";
  public static final String SCM_2036 = "scm_2036 返回结果加密失败";
  public static final String SCM_20361 = "SCM_20361 注册失败,帐号/手机号已经存在";

  // 第三方系统获取自动登录链接错误
  public static final String SCM_401 = "scm-401 获取自动链接时，不存在链接编码(autoLoginUrlCode)或传递的链接编码匹配不到链接";

  // 互联互通
  public static final String SCM_402 = "scm-402   获取群组信息，groupCode无效 或者失效";
  public static final String SCM_403 = "scm-403  没有查询到群组信息！";
  public static final String SCM_404 = "scm-404  没有查询到没有项目群组！";
  public static final String SCM_405 = "scm-405  groupCode正确    但不能 不能配皮 对应的 openId！！";
  public static final String SCM_406 = "scm-406  groupCode已经被关联！！";
  public static final String SCM_407 = "scm-407  oldGroupCode查询不到关联信息！";
  public static final String SCM_408 = "scm-408  groupCode无效，或不属于当前人员的groupCode";
  public static final String SCM_409 = "scm-409  des3GrpId无效";

  // dao 运行异常
  public static final String SCM_901 = "scm-901   从数据库获取第三方系统注册名称异常";
  public static final String SCM_902 = "scm-902   根据openId,token从数据库获取open人员关联对象异常";
  public static final String SCM_903 = "scm-903   保存错误日志异常";
  // 业务处理 运行异常
  public static final String SCM_501 = "scm-501   校验授权参数 openid token时异常";
  public static final String SCM_502 = "scm-502    获取人员基本信息服务异常";
  public static final String SCM_503 = "scm-503    保存个人微信消息出错";
  public static final String SCM_504 = "scm-504    保存群发微信消息出错";
  public static final String SCM_521 = "scm-521    第三方接收项目JSON数据解析出错";
  public static final String SCM_522 = "scm-522    第三方接收项目数据解析出错";
  public static final String SCM_523 = "scm-523    第三方接收项目数据type数据错误,取值限定0或1";
  public static final String SCM_524 = "scm-524    第三方接收项目数据state数据错误,取值限定01或02或03";
  public static final String SCM_525 = "scm-525    第三方接收项目数据解析错误.";
  public static final String SCM_505 = "scm-505    获取微信授权码异常";
  public static final String SCM_526 = "scm-526    解除绑定异常";
  public static final String SCM_527 = "scm-527 人名 ，单位组合或者邮件地址为空！";
  public static final String SCM_528 = "scm-528   按照人名+单位组合或者邮件地址检索系统中的所有人员， 查询员列表的记录失败";
  public static final String SCM_529 = "scm-529   人员同步 异常";

  public static final String SCM_530 = "scm-530   获取自动登陆加密串异常";
  // 互联互通
  public static final String SCM_531 = "scm-531 构建群组信息xml异常";

  public static final String SCM_301 = "scm-301   数据交互异常 ";
  public static final String SCM_302 = "scm-302    返回json格式数据构造异常";
  public static final String SCM_303 = "scm-303    构建返回xml数据异常";
  // MSG
  public static final String SCM_700 = "SCM-700    产生消息服务，消息类型msgType为空！";
  public static final String SCM_701 = "SCM_701  产生消息服务，receiverId为空或不是数字类型！";
  public static final String SCM_702 = "SCM_702  产生消息服务，receiverIds为空 ，必须要有一个接收者Id且为数字且为数字！";
  public static final String SCM_703 = "SCM_703  产生消息服务，构建具体消息出错！";
  public static final String SCM_704 = "SCM_704 产生消息服务，paramet或data为空！";
  public static final String SCM_705 = "SCM_705  产生消息服务，msgType类型不正确！";
  public static final String SCM_706 = "SCM_706  产生消息服务，pubId不能为空，且必须为数字！";
  public static final String SCM_707 = "SCM_707  产生消息服务，pubIdList不能为空！";
  public static final String SCM_708 = "SCM_708  产生消息服务，rcmdFriendIdList不能为空！";
  public static final String SCM_709 = "SCM_709  产生消息服务，smateInsideLetterType不能为空 , 或者类型错误！";
  public static final String SCM_710 = "SCM_710  产生消息服务，content不能为空！";
  public static final String SCM_711 = "SCM_711  产生消息服务，fileId  or  grpFileId不能为空,且为数字 ,belongPerson必须为布尔值！";
  public static final String SCM_712 = "SCM_712  产生消息服务，pubId or grpPubId不能为空,且为数字 ,belongPerson必须为布尔值！";
  public static final String SCM_713 = "SCM_713  产生消息服务，grpId不能为空,且为数字 ！";
  public static final String SCM_714 = "SCM_714  产生消息服务，grpMsgType不能为空 ！";
  public static final String SCM_715 = "SCM_715  产生消息服务，grpMsgType，类型错误 ！";
  public static final String SCM_716 = "SCM_716  产生消息服务，grpFileId，不能为空，且为数字 ！";
  public static final String SCM_717 = "SCM_717  产生消息服务，grpPubId，不能为空，且为数字 ！";
  public static final String SCM_718 = "SCM_718  产生消息服务，requestFriendId，不能为空，且为数字 ！";
  public static final String SCM_719 = "SCM_719  产生消息服务，requestGrpId，不能为空，且为数字 ！";
  public static final String SCM_720 = "SCM_720  产生消息服务，rcmdGrpId，不能为空，且为数字 ！";
  public static final String SCM_721 = "SCM_721  产生消息服务，rolpubId不能为空，且必须为数字！";
  public static final String SCM_722 = "SCM_722  产生消息服务，fulltextId不能为空，且必须为数字！";
  public static final String SCM_724 = "SCM_723  产生消息服务，des3NewsId不能为空，且必须为数字！";
  public static final String SCM_727 = "SCM_727  产生消息服务，des3InsId不能为空，且必须为数字！";

  // 获取成果关键词 信息
  public static final String SCM_723 = "scm-723 具体服务类型参数  pubData为空 或格式不正确";
  public static final String SCM_796 = " 关键词省份投入产出趋势分析，投入产出type 不能为空";
  public static final String SCM_797 = "分析周期开始年份 不能为空 ";
  public static final String SCM_798 = "分析周期结束年份 不能为空 ";
  public static final String SCM_800 = "分析周期开始年份格式不对";
  public static final String SCM_801 = "分析周期结束年份 格式不对 ";
  public static final String SCM_802 = "分析周期结束年份必须大于开始年份 ";
  public static final String SCM_803 = "分析周期开始年份不在规定范围内,年份大于1900";
  public static final String SCM_799 = "关键词列表 keywordList 不能为空 ";
  public static final String SCM_822 = "根据关键词检索专家，研究领域列表 PSN_SCIENCE_AREA 不能为空 ";
  public static final String SCM_833 = "SCM_833 根据地区检索论文，regionCode不能为空，且必须为数字";


  // openId获取
  public static final String SCM_410 = "scm-410 获取openId，参数 psnId不能为空";

  public static final String SCM_411 = "scm-411 获取openId，参数 psnId不存在";

  // --------以下均为SIE使用的状态码

  public static final String SCM_999 = "SCM-999 SIE，参数不符合规范，请查阅接口说明"; // STATUS:ERROR,用于内部接口,不满足入参要求的均返回此状态码

  public static final String SCM_9011 = "SCM_9011  SIE，通过其他系统注册新单位， 单位基本数据 ins_name不能为空";
  public static final String SCM_9012 = "SCM_9012  SIE，通过其他系统注册新单位， 单位基本数据 contact_person不能为空";
  public static final String SCM_9013 = "SCM_9013  SIE，通过其他系统注册新单位， 单位基本数据 contact_email不能为空且格式必须正确";
  public static final String SCM_9014 = "SCM_9014  SIE，通过其他系统注册新单位， 单位基本数据 pwd 不能为空";
  public static final String SCM_9015 = "SCM_9015  SIE，通过其他系统注册新单位， 单位基本数据 nature 不能为空";
  // 新增状态码
  public static final String SCM_9020 = "SCM_9020  SIE，通过其他系统注册新单位， 单位已存在，无需注册";
  public static final String SCM_9021 = "SCM_9021  SIE，通过其他系统注册新单位， 单位注册已受理，待审核";
  public static final String SCM_9016 = "SCM_9016  SIE，通过其他系统注册新单位， 单位基本数据 ins_name长度不能超过20";
  public static final String SCM_9017 = "SCM_9017  SIE，通过其他系统注册新单位， 单位基本数据 url长度不能超过20";
  public static final String SCM_9018 = "SCM_9018  SIE，通过其他系统注册新单位， 单位基本数据 url不能包含中文或特殊字符";

  // -----------以上均为SIE使用的状态码
}
