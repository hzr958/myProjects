package com.smate.center.searcher.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.smate.center.searcher.repository.SolrRepository.LinkType;
import com.smate.center.searcher.util.SolrPage;

/**
 * Solr 公共服务接口
 * 
 * @author 斌少
 * 
 */
public interface SolrService<T, ID extends Serializable> {

	/** 创建索引（批量） **/
	void createOrUpdateIndexs(List<T> docs);

	/** 创建索引（单个） **/
	void createOrUpdateIndex(T doc);

	/** 获取一个排序（逆序） **/
	public Sort getSortDesc(String field);

	/** 获取一个排序（顺序） **/
	public Sort getSortAsc(String field);

	/** 删除一个索引 **/
	void deleteIndex(ID id);

	/** 根据ID获取一个文档对象 **/
	public T findOne(ID id);

	/** 根据ID获取一个文档对象(详细信息) **/
	public Object findOneDetail(ID id);

	/** 删除索引（批量） **/
	void deleteIndexs(List<T> docs);

	/** 根据ID删除索引（批量） **/
	void deleteIndexs(Collection<String> docs);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，type：与/或，highlight是否高亮 **/
	List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, LinkType type,
			boolean highlight);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，sort排序 ，type：与/或，highlight是否高亮 **/
	List<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort, LinkType type,
			boolean highlight);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，page 分页 ，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, SolrPage<T> page,
			LinkType type, boolean highlight);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，sort排序，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			SolrPage<T> page, LinkType type, boolean highlight);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Pageable page,
			LinkType type, boolean highlight);

	/** 查询，businessType为业务类型，entityClass为要转换的实体类型，sort排序，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(String businessType, Map<String, Object> conditions, Class<T> entityClass, Sort sort,
			Pageable page, LinkType type, boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，type：与/或，highlight是否高亮 **/
	List<T> find(Map<String, Object> conditions, Class<T> entityClass, LinkType type, boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，sort排序，type：与/或，highlight是否高亮 **/
	List<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, LinkType type, boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, SolrPage<T> page, LinkType type,
			boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，sort排序，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, SolrPage<T> page, LinkType type,
			boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Pageable page, LinkType type,
			boolean highlight);

	/** 查询，conditions为查询条件，entityClass为要转换的实体类型，sort排序，page 分页，type：与/或，highlight是否高亮 **/
	SolrPage<T> find(Map<String, Object> conditions, Class<T> entityClass, Sort sort, Pageable page, LinkType type,
			boolean highlight);

	/**** 查询 姓名检索 业务******/
	SolrPage<T> findPerson(Map<String, Object> c,
			SolrPage<T> p);


}
