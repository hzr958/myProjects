<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ include file="/common_v5/meta.jsp"%> --%>
<c:set var="title">${pubDetailVO.title }</c:set>
<c:set var="publish_date">${pubDetailVO.publishDate }</c:set>
<c:set var="keywords">${pubDetailVO.keywords }</c:set>
<c:set var="author_names">${pubDetailVO.authorNames }</c:set>
<c:set var="doi">${pubDetailVO.doi }</c:set>
<c:set var="summary">${pubDetailVO.summary }</c:set>
<c:set var="pub_type_id">${pubDetailVO.pubType }</c:set>
<c:set var="surl">${pubDetailVO.srcFulltextUrl }</c:set>
<c:set var="fulltextImgUrl">${pubDetailVO.fulltextImageUrl }</c:set>
<c:set var="pubUrl">${pubDetailVO.pubIndexUrl }</c:set>
<link rel="canonical" href="${not empty pubDetailVO.seoIndexUrl ? pubDetailVO.seoIndexUrl : pubDetailVO.pubIndexUrl}" />
<%-- 作者邮箱的DC标签 --%>
<c:forEach items="${members }" var="member">
  <c:if test="${! empty member and ! empty member.email }">
    <meta name="DC.identifier" content="${member.email}" xml:lang="en_US" />
  </c:if>
</c:forEach>
<%-- 作者邮箱的DC标签 --%>
<c:if test="${! empty title }">
  <meta name="citation_title" content="${title }" />
</c:if>
<c:if test="${! empty publish_date }">
  <meta name="citation_publication_date" content="${fn:trim(publish_date) }" />
  <c:set var="publish_year" value="${fn:substring(publish_date,0,4) }" />
  <meta name="citation_date" content="${publish_year }" />
</c:if>
<%-- 关键词的citation标签 --%>
<c:if test="${! empty keywords }">
  <meta name="citation_keywords" content="${fn:trim(keywords) }" />
</c:if>
<%-- 成果URL的citation标签 --%>
<meta name="citation_abstract_html_url" content="${pubUrl }" />
<%-- author的citation标签 --%>
<c:if test="${! empty author_names }">
  <meta name="citation_authors" content="${author_names}" />
  <c:set var="authorsArray" value="${fn:split(author_names,';|；') }" />
  <c:if test="${!empty authorsArray}">
    <c:forEach items="${authorsArray }" var="authorName" varStatus="st">
      <meta name="citation_author" content="${fn:trim(authorName) }" />
    </c:forEach>
  </c:if>
</c:if>
<%-- 卷的citation标签 --%>
<c:if test="${! empty volume }">
  <meta name="citation_volume" content="${fn:trim(volume) }" />
</c:if>
<%-- 专题号的citation标签 --%>
<c:if test="${! empty issue }">
  <meta name="citation_issue" content="${fn:trim(issue) }" />
</c:if>
<%-- doi的citation标签 --%>
<c:if test="${! empty doi }">
  <meta name="citation_doi" content="${fn:trim(doi) }" />
</c:if>
<%-- DC标签： --%>
<%-- 语言的DC标签 --%>
<c:if test="${locale eq 'en_US'}">
  <meta name="DC.language" content="eng" xml:lang="en_US" />
</c:if>
<c:if test="${locale eq 'zh_CN' }">
  <meta name="DC.language" content="chi" xml:lang="zh_CN" />
</c:if>
<%-- 成果类别的DC标签 --%>
<c:if test="${! empty pub_type_id }">
  <c:if test="${pub_type_id==1 }">
    <meta name="DC.type" content="奖励" xml:lang="en_US" />
    <meta name="DC.type" content="award" xml:lang="zh_CN" />
  </c:if>
  <c:if test="${pub_type_id==2 }">
    <c:set var="isbn">${pubDetailVO.pubTypeInfo.ISBN }</c:set>
    <c:set var="book_title">${pubDetailVO.pubTypeInfo.name }</c:set>
    <c:set var="publisher">${pubDetailVO.pubTypeInfo.publisher }</c:set>
    <c:set var="page">${pubDetailVO.pubTypeInfo.pageNumber }</c:set>
    <meta name="DC.type" content="书/著作" xml:lang="en_US" />
    <meta name="DC.type" content="book_monograph" xml:lang="zh_CN" />
    <%-- ISBN号的citation标签 --%>
    <c:if test="${! empty isbn }">
      <meta name="citation_issue" content="${fn:trim(isbn) }" />
    </c:if>
    <%-- publisher的DC标签 --%>
    <c:if test="${! empty publisher }">
      <meta name="DC.publisher" content="${fn:escapeXml(publisher) }" />
    </c:if>
    <%-- publisher的citation标签--%>
    <c:if test="${! empty publisher }">
      <meta name="citation_publisher" content="${fn:trim(publisher) }" />
    </c:if>
    <%-- 起始页码的DC标签 --%>
    <c:if test="${! empty page }">
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
    </c:if>
    <%-- 起始页码的citation标签 --%>
    <c:if test="${! empty page }">
      <meta name="citation_page" content="${page }">
    </c:if>
  </c:if>
  <c:if test="${pub_type_id==3 }">
    <c:set var="conf_name">${pubDetailVO.pubTypeInfo.name }</c:set>
    <c:set var="page">${pubDetailVO.pubTypeInfo.pageNumber }</c:set>
    <meta name="DC.type" content="会议论文" xml:lang="en_US" />
    <meta name="DC.type" content="conference_paper" xml:lang="zh_CN" />
    <c:if test="${! empty conf_name }">
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(conf_name) }" xml:lang="en_US" />
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(conf_name) }" xml:lang="zh_CN" />
    </c:if>
    <%-- 起始页码的DC标签 --%>
    <c:if test="${! empty page }">
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
    </c:if>
    <%-- 起始页码的citation标签 --%>
    <c:if test="${! empty page }">
      <meta name="citation_page" content="${page }">
    </c:if>
  </c:if>
  <c:if test="${pub_type_id==4 }">
    <c:set var="jname">${pubDetailVO.pubTypeInfo.name}</c:set>
    <c:set var="volume">${pubDetailVO.pubTypeInfo.volumeNo }</c:set>
    <c:set var="issue">${pubDetailVO.pubTypeInfo.issue }</c:set>
    <c:set var="issn">${pubDetailVO.pubTypeInfo.ISSN }</c:set>
    <c:set var="article_number">${pubDetailVO.pubTypeInfo.pageNumber }</c:set>
    <c:set var="page">${pubDetailVO.pubTypeInfo.pageNumber }</c:set>
    <meta name="DC.type" content="期刊论文" xml:lang="en_US" />
    <meta name="DC.type" content="journal_article" xml:lang="zh_CN" />
    <c:if test="${! empty jname }">
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(jname) }" xml:lang="en_US" />
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(jname) }" xml:lang="zh_CN" />
    </c:if>
    <%-- 期刊名称的citation标签 --%>
    <c:if test="${! empty jname }">
      <meta name="citation_journal_title" content="${fn:trim(jname) }" />
    </c:if>
    <%-- ISSN的DC标签 --%>
    <c:if test="${! empty issn }">
      <meta name="DC.identifier" content="${fn:trim(issn) }" xml:lang="en_US" />
    </c:if>
    <%-- ISSN号的citation标签 --%>
    <c:if test="${! empty issn }">
      <meta name="citation_issue" content="${fn:trim(issue) }" />
    </c:if>
    <%-- 文章号的DC标签 --%>
    <c:if test="${! empty article_number }">
      <meta name="DC.identifier" content="${article_number}" scheme="DCTERMS.URI" />
    </c:if>
    <%-- 起始页码的DC标签 --%>
    <c:if test="${! empty page }">
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
    </c:if>
    <%-- 起始页码的citation标签 --%>
    <c:if test="${! empty page }">
      <meta name="citation_page" content="${page }">
    </c:if>
    <%-- 卷号的DC标签 --%>
    <c:if test="${! empty volume }">
      <meta name="DC.identifier" content="${fn:trim(volume) }" xml:lang="en_US" />
    </c:if>
    <%-- 期号的DC标签 --%>
    <c:if test="${! empty issue }">
      <meta name="DC.identifier" content="${fn:trim(issue) }" xml:lang="en_US" />
    </c:if>
  </c:if>
  <c:if test="${pub_type_id==5 }">
    <c:set var="dissertationInstitution">${pubDetailVO.pubTypeInfo.issuingAuthority }</c:set>
    <meta name="DC.type" content="专利" xml:lang="en_US" />
    <meta name="DC.type" content="patent" xml:lang="zh_CN" />
    <%-- 毕业学校的citation标签 --%>
    <c:if test="${! empty dissertationInstitution }">
      <meta name="citation_dissertation_institution" content="${dissertationInstitution }">
    </c:if>
  </c:if>
  <c:if test="${pub_type_id==7 }">
    <meta name="DC.type" content="其他" xml:lang="en_US" />
    <meta name="DC.type" content="other" xml:lang="zh_CN" />
  </c:if>
  <c:if test="${pub_type_id==8 }">
    <c:set var="programme">${pubDetailVO.pubTypeInfo.degree }</c:set>
    <meta name="DC.type" content="学位论文" xml:lang="en_US" />
    <meta name="DC.type" content="thesis" xml:lang="zh_CN" />
    <%-- 学位级别的citation标签 --%>
    <c:if test="${! empty programme }">
      <meta name="citation_dissertation_name" content="${programme }">
    </c:if>
  </c:if>
  <c:if test="${pub_type_id==10 }">
    <c:set var="isbn">${pubDetailVO.pubTypeInfo.ISBN }</c:set>
    <c:set var="book_title">${pubDetailVO.pubTypeInfo.name }</c:set>
    <c:set var="publisher">${pubDetailVO.pubTypeInfo.publisher }</c:set>
    <c:set var="page">${pubDetailVO.pubTypeInfo.pageNumber }</c:set>
    <meta name="DC.type" content="书籍章节" xml:lang="en_US" />
    <meta name="DC.type" content="book_chapter" xml:lang="zh_CN" />
    <c:if test="${! empty book_title }">
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(book_title) }" xml:lang="en_US" />
      <meta name="DCTERMS.isPartOf" content="${fn:escapeXml(book_title) }" xml:lang="zh_CN" />
    </c:if>
    <%-- ISBN号的citation标签 --%>
    <c:if test="${! empty isbn }">
      <meta name="citation_issue" content="${fn:trim(isbn) }" />
    </c:if>
    <%-- publisher的DC标签 --%>
    <c:if test="${! empty publisher }">
      <meta name="DC.publisher" content="${fn:escapeXml(publisher) }" />
    </c:if>
    <%-- publisher的citation标签--%>
    <c:if test="${! empty publisher }">
      <meta name="citation_publisher" content="${fn:trim(publisher) }" />
    </c:if>
    <%-- 起始页码的DC标签 --%>
    <c:if test="${! empty page }">
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
      <meta name="DC.identifier" content="${fn:trim(page) }" xml:lang="en_US" />
    </c:if>
    <%-- 起始页码的citation标签 --%>
    <c:if test="${! empty page }">
      <meta name="citation_page" content="${page }">
    </c:if>
  </c:if>
</c:if>
<%-- 文章名的DC标签 --%>
<c:if test="${! empty title }">
  <meta name="DC.title" content="${title }" xml:lang="en_US" />
  <meta name="DC.title" content="${title }" xml:lang="zh_CN" />
</c:if>
<%-- 作者人名的DC标签 --%>
<c:if test="${! empty author_names }">
  <c:set var="authorsArray" value="${fn:split(author_names,';|；') }" />
  <c:if test="${!empty authorsArray}">
    <c:forEach items="${authorsArray }" var="authorName" varStatus="st">
      <meta name="DC.creator" content="${fn:trim(authorName) }" xml:lang="en_US" />
      <meta name="DC.creator" content="${fn:trim(authorName) }" xml:lang="zh_CN" />
    </c:forEach>
  </c:if>
</c:if>
<%-- 摘要的DC标签 --%>
<c:if test="${! empty summary }">
  <meta name="DCTERMS.abstract" content="${summary }" xml:lang="en_US" />
  <meta name="DCTERMS.abstract" content="${summary }" xml:lang="zh_CN" />
</c:if>
<%-- 关键词的DC标签 --%>
<c:if test="${! empty keywords }">
  <c:set var="keywordsArray" value="${fn:split(keywords,';|；') }" />
  <c:if test="${!empty keywordsArray}">
    <c:forEach items="${keywordsArray }" var="keyword" varStatus="st">
      <meta name="DC.subject" content="${fn:trim(keyword) }" />
    </c:forEach>
  </c:if>
</c:if>
<%-- 来源名称的DC标签 --%>
<%-- 发表日期和专题发表年份的DC标签 --%>
<c:if test="${! empty publish_date }">
  <meta name="DCTERMS.available" content="${publish_date }" scheme="DCTERMS.W3CDTF" />
  <meta name="DCTERMS.issued" content="${publish_year }" xml:lang="en_US" scheme="DCTERMS.W3CDTF" />
</c:if>
<%-- bibliographicCitation的DC标签 --%>
<c:if test="${!empty publish_year && pub_type_id==4 }">
  <meta name="DCTERMS.bibliographicCitation" content="${showBriefDesc }" xml:lang="en_US" />
  <meta name="DCTERMS.bibliographicCitation" content="${showBriefDesc }" xml:lang="zh_CN" />
</c:if>
<%-- DOI的DC标签 --%>
<c:if test="${! empty doi }">
  <meta name="DC.identifier" content="${fn:trim(doi) }" xml:lang="en_US" />
</c:if>
<c:if test="${! empty author_names }">
  <meta property="description"
    content="${author_names }${locale=='zh_CN' ? '，' : ', ' }${!empty showBriefDesc ? showBriefDesc : ''}" />
</c:if>
<%-- 来源URL的DC标签 --%>
<c:if test="${! empty surl }">
  <meta name="DC.identifier" content="${fn:trim(surl) }" xml:lang="en_US" />
</c:if>
<c:if test="${! empty keywords }">
  <meta name="keywords" content="${keywords}" />
</c:if>
<meta property="og:type" content="website">
<c:if test="${! empty pubUrl }">
  <meta property="og:url" content="${pubUrl }" />
</c:if>
<c:if test="${! empty title }">
  <meta property="og:title" content="${title }" />
</c:if>
  <meta property="og:description"
    content="${pubSEO.description}" />
<c:if test="${! empty fulltextImgUrl }">
  <meta property="og:image" content="${fulltextImgUrl }" />
</c:if>
<meta property="og:site_name" content="ScholarMate" />
<%-- <input type="hidden" name="title" value="${title }">
<input type="hidden" name="publish_date" value="${publish_date }">
<input type="hidden" name="keywords" value="${keywords }">
<input type="hidden" name="author_names" value="${author_names }">
<input type="hidden" name="jname" value="${jname }">
<input type="hidden" name="volume" value="${volume }">
<input type="hidden" name="issue" value="${issue }">
<input type="hidden" name="issn" value="${issn }">
<input type="hidden" name="isbn" value="${isbn }">
<input type="hidden" name="doi" value="${doi }">
<input type="hidden" name="page" value="${page }">
<input type="hidden" name="programme" value="${programme }">
<input type="hidden" name="dissertationInstitution" value="${dissertationInstitution }">
<input type="hidden" name="summary" value="${summary }">
<input type="hidden" name="pub_type_id" value="${pub_type_id }">
<input type="hidden" name="article_number" value="${article_number }">
<input type="hidden" name="surl" value="${surl }">
<input type="hidden" name="publisher" value="${publisher }">
<input type="hidden" name="conf_name" value="${conf_name }">
<input type="hidden" name="book_title" value="${book_title }">
<input type="hidden" name="fulltextImgUrl" value="${fulltextImgUrl }">
<input type="hidden" name="pubUrl" value="${pubUrl }"> --%>