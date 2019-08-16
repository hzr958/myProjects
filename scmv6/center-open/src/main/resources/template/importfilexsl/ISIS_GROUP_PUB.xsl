<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xs">
  <xsl:output method="xml" encoding="UTF-8" indent="yes" />
  <xsl:template match="publications">
    <xsl:element name="publications">
      <xsl:element name="open_id">
        <xsl:value-of select="open_id"></xsl:value-of>
      </xsl:element>
      <xsl:element name="total_pages">
        <xsl:value-of select="total_pages"></xsl:value-of>
      </xsl:element>
      <xsl:element name="count">
        <xsl:value-of select="count"></xsl:value-of>
      </xsl:element>
      <xsl:element name="getPubStatus">
        <xsl:value-of select="getPubStatus"></xsl:value-of>
      </xsl:element>
      <xsl:element name="group_code">
        <xsl:value-of select="group_code"></xsl:value-of>
      </xsl:element>
      <xsl:element name="current_get_pub_date">
        <xsl:value-of select="current_get_pub_date"></xsl:value-of>
      </xsl:element>
      <xsl:for-each select="publication">
        <xsl:element name="publication">
          <xsl:element name="pub_basic">
            <xsl:element name="pub_id">
              <xsl:value-of select="pub_id"></xsl:value-of>
            </xsl:element>
            <xsl:element name="status">
              <xsl:value-of select="status"></xsl:value-of>
            </xsl:element>
            <xsl:element name="pub_type_id">
              <xsl:value-of select="pub_type_id"></xsl:value-of>
            </xsl:element>
            <xsl:element name="zh_pub_type_name">
              <xsl:value-of select="zh_pub_type_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="en_pub_type_name">
              <xsl:value-of select="en_pub_type_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="zh_title">
              <xsl:value-of select="zh_title"></xsl:value-of>
            </xsl:element>
            <xsl:element name="en_title">
              <xsl:value-of select="en_title"></xsl:value-of>
            </xsl:element>
            <xsl:element name="zh_source">
              <xsl:value-of select="zh_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="en_source">
              <xsl:value-of select="en_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="authors_name">
              <xsl:value-of select="authors_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="publish_year">
              <xsl:value-of select="publish_year"></xsl:value-of>
            </xsl:element>
            <xsl:element name="publish_month">
              <xsl:value-of select="publish_month"></xsl:value-of>
            </xsl:element>
            <xsl:element name="publish_day">
              <xsl:value-of select="publish_day"></xsl:value-of>
            </xsl:element>
            <xsl:element name="create_date">
              <xsl:value-of select="create_date"></xsl:value-of>
            </xsl:element>
            <xsl:element name="has_full_text">
              <xsl:value-of select="has_full_text"></xsl:value-of>
            </xsl:element>
            <xsl:element name="full_text_img_url">
              <xsl:value-of select="full_text_img_url"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_ei_source">
              <xsl:value-of select="list_ei_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_sci_source">
              <xsl:value-of select="list_sci_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_ssci_source">
              <xsl:value-of select="list_ssci_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_istp_source">
              <xsl:value-of select="list_istp_source"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_ei">
              <xsl:value-of select="list_ei"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_sci">
              <xsl:value-of select="list_sci"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_ssci">
              <xsl:value-of select="list_ssci"></xsl:value-of>
            </xsl:element>
            <xsl:element name="list_istp">
              <xsl:value-of select="list_istp"></xsl:value-of>
            </xsl:element>
            <xsl:element name="owner">
              <xsl:value-of select="owner"></xsl:value-of>
            </xsl:element>
            <xsl:element name="authenticated">
              <xsl:value-of select="authenticated"></xsl:value-of>
            </xsl:element>
            <xsl:element name="cited_times">
              <xsl:value-of select="cited_times"></xsl:value-of>
            </xsl:element>
            <xsl:element name="pub_detail_param">
              <xsl:value-of select="pub_detail_param"></xsl:value-of>
            </xsl:element>
            <xsl:element name="full_link">
              <xsl:value-of select="full_link"></xsl:value-of>
            </xsl:element>
            <xsl:element name="product_mark">
              <xsl:value-of select="product_mark"></xsl:value-of>
            </xsl:element>
            <xsl:element name="relevance">
              <xsl:value-of select="relevance"></xsl:value-of>
            </xsl:element>
            <xsl:element name="labeled">
              <xsl:value-of select="labeled"></xsl:value-of>
            </xsl:element>
            <xsl:element name="remark">
              <xsl:value-of select="remark"></xsl:value-of>
            </xsl:element>
            <xsl:element name="authors">
              <xsl:for-each select="authors/author">
                <xsl:element name="author">
                  <xsl:element name="psn_name">
                    <xsl:value-of select="psn_name"></xsl:value-of>
                  </xsl:element>
                  <xsl:element name="org_name">
                    <xsl:value-of select="org_name"></xsl:value-of>
                  </xsl:element>
                  <xsl:element name="email">
                    <xsl:value-of select="email"></xsl:value-of>
                  </xsl:element>
                  <xsl:element name="is_message">
                    <xsl:value-of select="is_message"></xsl:value-of>
                  </xsl:element>
                  <xsl:element name="first_author">
                    <xsl:value-of select="first_author"></xsl:value-of>
                  </xsl:element>
                  <xsl:element name="is_mine">
                    <xsl:value-of select="is_mine"></xsl:value-of>
                  </xsl:element>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">1</xsl:attribute>
            <xsl:element name="award_type_name">
              <xsl:value-of select="award_type_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="award_grade_name">
              <xsl:value-of select="award_grade_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="prize_org">
              <xsl:value-of select="prize_org"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">2</xsl:attribute>
            <xsl:element name="language">
              <xsl:value-of select="language"></xsl:value-of>
            </xsl:element>
            <xsl:element name="publication_status">
              <xsl:value-of select="publication_status"></xsl:value-of>
            </xsl:element>
            <xsl:element name="country_name">
              <xsl:value-of select="country_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
              <xsl:value-of select="city"></xsl:value-of>
            </xsl:element>
            <xsl:element name="pub_house">
              <xsl:value-of select="pub_house"></xsl:value-of>
            </xsl:element>
            <xsl:element name="t_word">
              <xsl:value-of select="t_word"></xsl:value-of>
            </xsl:element>
            <xsl:element name="isbn">
              <xsl:value-of select="isbn"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">3</xsl:attribute>
            <xsl:element name="conf_name">
              <xsl:value-of select="conf_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_type">
              <xsl:value-of select="conf_type"></xsl:value-of>
            </xsl:element>
            <xsl:element name="doi">
              <xsl:value-of select="doi"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_org">
              <xsl:value-of select="conf_org"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_start_year">
              <xsl:value-of select="conf_start_year"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_start_month">
              <xsl:value-of select="conf_start_month"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_start_day">
              <xsl:value-of select="conf_start_day"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_end_year">
              <xsl:value-of select="conf_end_year"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_end_month">
              <xsl:value-of select="conf_end_month"></xsl:value-of>
            </xsl:element>
            <xsl:element name="conf_end_day">
              <xsl:value-of select="conf_end_day"></xsl:value-of>
            </xsl:element>
            <xsl:element name="begin_num">
              <xsl:value-of select="begin_num"></xsl:value-of>
            </xsl:element>
            <xsl:element name="end_num">
              <xsl:value-of select="end_num"></xsl:value-of>
            </xsl:element>
            <xsl:element name="paper_type">
              <xsl:value-of select="paper_type"></xsl:value-of>
            </xsl:element>
            <xsl:element name="country_name">
              <xsl:value-of select="country_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
              <xsl:value-of select="city"></xsl:value-of>
            </xsl:element>
            <xsl:element name="article_no">
              <xsl:value-of select="article_no"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">4</xsl:attribute>
            <xsl:element name="impact_factors">
              <xsl:value-of select="impact_factors"></xsl:value-of>
            </xsl:element>
            <xsl:element name="public_status">
              <xsl:value-of select="public_status"></xsl:value-of>
            </xsl:element>
            <xsl:element name="doi">
              <xsl:value-of select="doi"></xsl:value-of>
            </xsl:element>
            <xsl:element name="issue_no">
              <xsl:attribute name="code">01</xsl:attribute>
              <xsl:value-of select="volume"></xsl:value-of>
            </xsl:element>
            <xsl:element name="issue_no">
              <xsl:attribute name="code">02</xsl:attribute>
              <xsl:value-of select="issue"></xsl:value-of>
            </xsl:element>
            <xsl:element name="include_start">
              <xsl:value-of select="include_start"></xsl:value-of>
            </xsl:element>
            <xsl:element name="begin_num">
              <xsl:value-of select="begin_num"></xsl:value-of>
            </xsl:element>
            <xsl:element name="end_num">
              <xsl:value-of select="end_num"></xsl:value-of>
            </xsl:element>
            <xsl:element name="article_no">
              <xsl:value-of select="article_no"></xsl:value-of>
            </xsl:element>
            <xsl:element name="journal_name">
              <xsl:value-of select="journal_name"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">5</xsl:attribute>
            <xsl:element name="patent_status">
              <xsl:value-of select="patent_status"></xsl:value-of>
            </xsl:element>
            <xsl:element name="apply_man">
              <xsl:value-of select="apply_man"></xsl:value-of>
            </xsl:element>
            <xsl:element name="license_unit">
              <xsl:value-of select="license_unit"></xsl:value-of>
            </xsl:element>
            <xsl:element name="ch_patent_type">
              <xsl:value-of select="ch_patent_type"></xsl:value-of>
            </xsl:element>
            <xsl:element name="patent_num">
              <xsl:value-of select="patent_num"></xsl:value-of>
            </xsl:element>
            <xsl:element name="country_name">
              <xsl:value-of select="country_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
              <xsl:value-of select="city"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">7</xsl:attribute>
            <xsl:element name="country_name">
              <xsl:value-of select="country_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
              <xsl:value-of select="city"></xsl:value-of>
            </xsl:element>
          </xsl:element>
          <xsl:element name="pub_extend">
            <xsl:attribute name="pub_type_id">10</xsl:attribute>
            <xsl:element name="book_name">
              <xsl:value-of select="book_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="series_book">
              <xsl:value-of select="series_book"></xsl:value-of>
            </xsl:element>
            <xsl:element name="isbn">
              <xsl:value-of select="isbn"></xsl:value-of>
            </xsl:element>
            <xsl:element name="editors">
              <xsl:value-of select="editors"></xsl:value-of>
            </xsl:element>
            <xsl:element name="country_name">
              <xsl:value-of select="country_name"></xsl:value-of>
            </xsl:element>
            <xsl:element name="city">
              <xsl:value-of select="city"></xsl:value-of>
            </xsl:element>
            <xsl:element name="pub_house">
              <xsl:value-of select="pub_house"></xsl:value-of>
            </xsl:element>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>