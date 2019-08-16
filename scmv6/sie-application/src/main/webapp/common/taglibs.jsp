<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/iristaglib.tld" prefix="iris"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="snsctx" value="/scmwebsns" />
<c:set var="resmod" value="/resmod" />
<c:set var="ressns" value="/ressns" />
<c:set var="resscmsns" value="/resscmwebsns" />
<c:set var="siectx" value="/scmwebrol" />
<c:set var="pubctx" value="/pubweb" />
<c:set var="insctx" value="/insweb" />
<c:set var="respub" value="/ressns/js/pub" />
<c:set var="ressie" value="/ressie" />
<c:set var="resapp" value="/ressie/js/app" />
<s:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString() %></s:set>
<c:set var="lang" value="<%=org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage() %>" />
