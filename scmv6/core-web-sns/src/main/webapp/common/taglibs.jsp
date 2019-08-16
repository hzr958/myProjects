<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/irismenutaglib.tld" prefix="irismenu"%>
<%@ taglib uri="/WEB-INF/iristaglib.tld" prefix="iris"%>
<%@ taglib uri="/WEB-INF/irisfnlib.tld" prefix="irisfn"%>
<%@ taglib uri="/WEB-INF/irisnodetaglib.tld" prefix="irisnode"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="snsctx" value="/scmwebsns" />
<c:set var="ctx" value="/scmwebsns" />
<c:set var="resmod" value="/resmod" />
<c:set var="ressns" value="/ressns" />
<c:set var="resscmsns" value="/resscmwebsns" />
<c:set var="res" value="/resscmwebsns" />
<c:set var="siectx" value="/scmwebrol" />
<c:set var="respub" value="/ressns/js/v8pub" />
<c:set var="resdyn" value="/ressns/js/dyn" />
<c:set var="resprj" value="/ressns/js/prj" />
<c:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString() %></c:set>
<c:set var="lang" value="<%=org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage() %>" />
