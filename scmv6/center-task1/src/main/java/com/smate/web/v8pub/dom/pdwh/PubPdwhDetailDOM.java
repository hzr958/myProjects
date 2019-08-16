package com.smate.web.v8pub.dom.pdwh;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import com.smate.web.v8pub.dom.PubDetailDOM;

/**
 * 基准库成果详情
 * 
 * @author houchuanjie
 * @date 2018/05/31 14:46
 */
@Document(collection = "V_PUB_PDWH_DETAIL")
public class PubPdwhDetailDOM extends PubDetailDOM {

  @Override
  public int hashCode() {
    return 17 + Objects.hashCode(this.pubId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubDetailDOM that = (PubDetailDOM) o;
    return Objects.equals(pubId, that.getPubId());
  }
}
