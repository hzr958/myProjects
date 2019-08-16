package com.smate.core.base.utils.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component("mongoAutoidUtil")
public class MongoAutoidUtil {
  @Autowired
  private MongoTemplate mongoTemplate;

  public int getNextSequence(String collectionName) {
    Query query = new Query(Criteria.where("collName").is(collectionName));
    Update update = new Update();
    update.inc("seq", 1);
    FindAndModifyOptions options = new FindAndModifyOptions();
    options.upsert(true);
    options.returnNew(true);
    MongoSequence seq = mongoTemplate.findAndModify(query, update, options, MongoSequence.class);
    return seq.getSeq();
  }
}

