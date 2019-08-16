package com.smate.center.searcher.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.QueryParsers;
import org.springframework.data.solr.core.SolrCallback;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.center.searcher.repository.SolrRepository;
import com.smate.center.searcher.repository.SolrRepository.LinkType;
import com.smate.center.searcher.util.SolrPage;
import com.smate.core.base.utils.common.ReflectionUtils;

//import com.iris.utils.ReflectionUtils;

/**
 * Solr 公共服务接口
 * 
 * @author 斌少
 * 
 */
@Service
public class SolrServiceImpl<T, ID extends Serializable> implements SolrService<T, ID> {
	@Autowired
	private SolrRepository<T, ID> solrDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrTemplate.class);

	@Override
	public void createOrUpdateIndex(T doc) {
		Object env = ReflectionUtils.getFieldValue(doc, "env");
		Assert.notNull(env, "Cannot save 'null' env property.");
		Object businessType = ReflectionUtils.getFieldValue(doc, "businessType");
		Assert.notNull(businessType, "Cannot save 'null' businessType property.");	 
		ReflectionUtils.setFieldValue(doc, "id", UUID.randomUUID().toString());
		solrDao.save(doc);
	}

	@Override
	public T findOne(ID id) {
		return solrDao.findOne(id);
	}

	@Override
	public Object findOneDetail(ID id) {
		Query query = new SimpleQuery(new Criteria(solrDao.getIdFieldName()).is(id));
		query.setPageRequest(new PageRequest(0, 1));

		QueryParsers queryParsers = new QueryParsers();
		SolrQuery solrQuery = queryParsers.getForClass(query.getClass()).constructSolrQuery(query);
		LOGGER.debug("Executing query '" + solrQuery + "' against solr.");

		QueryResponse response = executeSolrQuery(solrQuery);

		if (response.getResults().size() > 0) {
			if (response.getResults().size() > 1) {
				LOGGER.warn("More than 1 result found for singe result query ('{}'), returning first entry in list");
			}
			return response.getResults().get(0);
		}
		return null;

	}

	final QueryResponse executeSolrQuery(final SolrQuery solrQuery) {
		return solrDao.getSolrOperations().execute(new SolrCallback<QueryResponse>() {
			@Override
			public QueryResponse doInSolr(SolrServer solrServer) throws SolrServerException, IOException {
				return solrServer.query(solrQuery);
			}
		});
	}

	@Override
	public Sort getSortDesc(String field) {
		return solrDao.getSortDesc(field);
	}

	@Override
	public Sort getSortAsc(String field) {
		return solrDao.getSortAsc(field);
	}

	@Override
	public void deleteIndex(ID id) {
		solrDao.delete(id);
	}

	@Override
	public void createOrUpdateIndexs(List<T> docs) {
		if (CollectionUtils.isNotEmpty(docs)) {
			for (T t : docs) {
			  ReflectionUtils.setFieldValue(t, "id", UUID.randomUUID().toString());
			}
		}
		solrDao.save(docs);
	}

	@Override
	public void deleteIndexs(Collection<String> ids) {
		solrDao.deleteIndexs(ids);
	}

	@Override
	public void deleteIndexs(List<T> docs) {
		solrDao.delete(docs);
	}

	@Override
	public List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, LinkType type,
			boolean highlight) {
		return solrDao.find(businessType, conditions, entityClass, type, highlight);
	}

	@Override
	public List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			LinkType type, boolean highlight) {
		return solrDao.find(businessType, conditions, entityClass, sort, type, highlight);
	}

	@Override
	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass,
			SolrPage<T> page, LinkType type, boolean highlight) {
		return solrDao.find(businessType, conditions, entityClass, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			SolrPage<T> page, LinkType type, boolean highlight) {
		return solrDao.find(businessType, conditions, entityClass, sort, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Pageable p,
			LinkType type, boolean highlight) {
		SolrPage<T> page = new SolrPage<>(p.getPageNumber() + 1, p.getPageSize());
		return solrDao.find(businessType, conditions, entityClass, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			Pageable p, LinkType type, boolean highlight) {
		SolrPage<T> page = new SolrPage<>(p.getPageNumber() + 1, p.getPageSize());
		return solrDao.find(businessType, conditions, entityClass, sort, page, type, highlight);
	}

	@Override
	public List<T> find(Map<String, Object> conditions, Class<T> entityClass, LinkType type, boolean highlight) {
		return this.find(null, conditions, entityClass, type, highlight);
	}

	@Override
	public List<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, LinkType type,
			boolean highlight) {
		return this.find(null, conditions, entityClass, sort, type, highlight);
	}

	@Override
	public SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, SolrPage<T> page, LinkType type,
			boolean highlight) {
		return this.find(null, conditions, entityClass, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, SolrPage<T> page,
			LinkType type, boolean highlight) {
		return this.find(null, conditions, entityClass, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Pageable page, LinkType type,
			boolean highlight) {
		return this.find(null, conditions, entityClass, page, type, highlight);
	}

	@Override
	public SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, Pageable page,
			LinkType type, boolean highlight) {
		return this.find(null, conditions, entityClass, sort, page, type, highlight);
	}

	@Override
	public SolrPage<T> findPerson(Map<String, Object> conditions, SolrPage<T> page) {
		return solrDao.findPerson(conditions,  page);
	}
	
}
