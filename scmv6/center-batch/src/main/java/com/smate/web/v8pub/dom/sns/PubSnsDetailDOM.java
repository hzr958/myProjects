package com.smate.web.v8pub.dom.sns;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import com.smate.web.v8pub.dom.PubDetailDOM;

/**
 * 个人库成果详情
 * 
 * @author houchuanjie
 * @date 2018/05/31 14:40
 */
@Document(collection = "V_PUB_SNS_DETAIL")
public class PubSnsDetailDOM extends PubDetailDOM {

  @Override
  public int hashCode() {
    return 31 + Objects.hashCode(this.pubId);
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
