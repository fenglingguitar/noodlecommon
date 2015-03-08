package org.fl.noodle.common.dynamicsql;


import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.fl.noodle.common.mvc.vo.PageVo;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.BeansDtdResolver;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;
import org.xml.sax.XMLReader;

public class DynamicSqlTemplateImpl implements DynamicSqlTemplate, InitializingBean, ResourceLoaderAware {
	
	private final static Log logger = LogFactory.getLog(DynamicSqlTemplateImpl.class);
	
	private ResourceLoader				resourceLoader;
	private List<String>                fileNameList;
	private final Map<String, String> 	nameSqlMap = new HashMap<String, String>();
	private boolean 					isRefresh = false;
	private final Map<String, Long> 	fileNameTimeMap = new HashMap<String, Long>();
	private HibernateTemplate			hibernateTemplate;
	private int 						pageSize;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	private DefaultConfigurationBuilder getBuilder() throws Exception {
		
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(false);
        
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader parser = saxParser.getXMLReader();
        parser.setEntityResolver(new BeansDtdResolver());
        
        return new DefaultConfigurationBuilder(parser);
    }
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		buildNameSqlMap();
        if (isRefresh) {
        	buildFileNameTimeMap();
        }
	}
	
	private void buildNameSqlMap() throws Exception {
		
		DefaultConfigurationBuilder builder = getBuilder();

        for (int i = 0; i < fileNameList.size(); i++) {
            String fileName = ((String) fileNameList.get(i)).trim();
            if (resourceLoader instanceof ResourcePatternResolver) {
                try {
                    Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(fileName);
                    buildNameSqlMap(builder, resources);
                } catch (IOException ex) {
                    throw new ConfigurationException("Could not resolve sql definition resource pattern [" + fileName + "]", ex);
                }
            } else {
            	Resource resource = resourceLoader.getResource(fileName);
            	buildNameSqlMap(builder, new Resource[]{resource});
            }
        }
	}
    
    private void buildNameSqlMap(DefaultConfigurationBuilder builder, Resource[] resources) throws Exception {
        
    	for (Resource resource : resources) {
            Configuration config = builder.build(resource.getInputStream());
            Configuration[] querys = config.getChildren("query");
            for (Configuration query : querys) {
                String queryName = query.getAttribute("name");
                if (queryName.equals("")) {
                    throw new ConfigurationException("Sql name is essential attribute in a <query>.");
                }
                nameSqlMap.put(queryName, query.getValue());
            }
    	}
    }
    
	public void setRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}
	
	private void buildFileNameTimeMap() throws Exception {
		
		for (int i = 0; i < fileNameList.size(); i++) {
            String fileName = ((String) fileNameList.get(i)).trim();
            if (resourceLoader instanceof ResourcePatternResolver) {
                try {
                    Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(fileName);
                    buildFileNameTimeMap(resources); 
                } catch (IOException ex) {
                    throw new ConfigurationException("Could not resolve sql definition resource pattern [" + fileName + "]", ex);
                }
            } else {
            	Resource resource = resourceLoader.getResource(fileName);
            	buildFileNameTimeMap(new Resource[]{resource});
            }
        }
	}
	
	private void buildFileNameTimeMap(Resource[] resources) throws Exception {
		
		for (Resource resource : resources) {
			String fileName = resource.getFilename();						
			long fileTime 	= resource.lastModified();			
			fileNameTimeMap.put(fileName, fileTime);
		}
		
	}
	
	private String getSqlByName(String key) throws Exception {
		
		if (isRefresh) {
			refreshNameSqlMap();
		}
		return nameSqlMap.get(key);
	}
	
	private void refreshNameSqlMap() throws Exception {
		
		DefaultConfigurationBuilder builder = getBuilder();
		
		for (int i = 0; i < fileNameList.size(); i++) {
            String fileName = ((String) fileNameList.get(i)).trim();
            if (resourceLoader instanceof ResourcePatternResolver) {
                try {
                    Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(fileName);
                    refreshNameSqlMap(builder, resources);
                } catch (IOException ex) {
                    throw new ConfigurationException("Could not resolve sql definition resource pattern [" + fileName + "]", ex);
                }
            } else {
            	Resource resource = resourceLoader.getResource(fileName);
            	refreshNameSqlMap(builder, new Resource[]{resource});
            }
        }
	}
	
	private void refreshNameSqlMap(DefaultConfigurationBuilder builder, Resource[] resources) throws Exception {
		
		for (Resource resource : resources) {
			String filename = resource.getFilename();						
			long fileTimeNow = resource.lastModified();
			synchronized (fileNameTimeMap) {
				long fileTime = fileNameTimeMap.get(filename) != null ? fileNameTimeMap.get(filename) : 0 ;
				if (fileTime != fileTimeNow) {
					buildNameSqlMap(builder, resource);
					fileNameTimeMap.put(filename, fileTimeNow);
				}
			}
		}
	}
	
	private void buildNameSqlMap(DefaultConfigurationBuilder builder, Resource resource) throws Exception {
        
        Configuration config = builder.build(resource.getInputStream());
        Configuration[] querys = config.getChildren("query");
        
        for (Configuration query : querys) {
            String queryName = query.getAttribute("name");
            if (queryName.equals("")) {
                throw new ConfigurationException("Sql name is essential attribute in a <query>.");
            }
            nameSqlMap.put(queryName, query.getValue());
        }
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}

	public List<String> getFileNameList() {
		return fileNameList;
	}	
	
	public void setIsRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public boolean getIsRefresh() {
		return isRefresh;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	private static String removeOrders(String sql) {
		
		Assert.hasText(sql);
		
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		
		StringBuffer sb = new StringBuffer();
		
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		
		return sb.toString();
    };
    
    private static String removeSelect(String sql) {
    	
        Assert.hasText(sql);
        
        int beginPos = sql.toLowerCase().indexOf("from ");
        
        Assert.isTrue(beginPos != -1, " hql : " + sql + " must has a keyword 'from'");
        
        return sql.substring(beginPos);
    };
    
    private Context generateVelocityContext(Map<String, Object> params) {
    	
        VelocityContext context = new VelocityContext();
        if (null == params) {
        	
            return context;
        }
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
        	
            String key = iterator.next();
            Object value = params.get(key);
            
            context.put(key, value);
        }
        
        return context;
    }
    
    private void setProperties(Query query, Context context, String[] namedParams) throws HibernateException {
    	
        for (int i=0; i<namedParams.length; i++) {
        	
            String namedParam = namedParams[i];
            
            final Object object = context.get(namedParam);
            if (object == null) {
                continue;
            }
            
            query.setParameter(namedParam, object);
        }
    }
	
	@Override
	public <T> PageVo<T> queryPage(String sqlName, Map<String, Object> params, int pageIndex, int pageSize, Class<T> clazz) throws Exception {
		return queryPageBySql(sqlName, params, pageIndex, pageSize, clazz);
	}
	
	@Override
	public <T> List<T> queryList(String sqlName, Map<String, Object> params, Class<T> clazz) throws Exception {
		return queryPageBySql(sqlName, params, 0, 0, clazz).getData();
	}

	public <T> PageVo<T> queryPageBySql(String sqlName, Map<String, Object> params, final int pageIndex, int pageSize, final Class<T> clazz) throws Exception {
		
		final Context context = generateVelocityContext(params);
		StringWriter writer = new StringWriter();
		
		String sqlOriginal = getSqlByName(sqlName);
		if (sqlOriginal == null) {
			throw new Exception("QueryPageBySql -> The sql is not find, Sql: " + sqlName);
		}
		
        Velocity.evaluate(context, writer, "Hibernate", sqlOriginal);
        final String sql = writer.toString();
        
        Long total = 0L;
        if (pageIndex > 0 && pageSize > 0) {
        	final String sqlCount = "select sum(num) from (select count(*) as num " + removeSelect(removeOrders(sql)) + ") as total";
        	total = hibernateTemplate.execute(new HibernateCallback<Long>() {
    			public Long doInHibernate(Session session) throws HibernateException, SQLException {
    				Query query = session.createSQLQuery(sqlCount);
    				setProperties(query, context, query.getNamedParameters());
    				Number count = (Number)query.uniqueResult();
    				return count != null ? count.longValue() : 0;
    			}
    		});
        }
        
		if (pageSize <= 0) {
			if (this.pageSize > 0 ) {
				pageSize = this.pageSize;
			} else {
				pageSize = 10;
			}
		}
		
		final int pageSizeFinal = pageSize;
		
		List<T> list = hibernateTemplate.execute(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(new MyResultTransformer(clazz));
				setProperties(query, context, query.getNamedParameters());
				if (pageIndex > 0 && pageSizeFinal > 0) {
		            query.setFirstResult((pageIndex - 1) * pageSizeFinal);
		            query.setMaxResults(pageSizeFinal);
		        }
				return query.list();
			}
		});
		
		long start = pageIndex > 0 && pageSize > 0 ? (pageIndex - 1) * pageSize : 0;
		return new PageVo<T>(start, total, pageSize, list);
	}
	
	@Override
	public <T> PageVo<T> queryPageSql(String strSql, Map<String, Object> params, int pageIndex, int pageSize, Class<T> clazz) throws Exception {
		return queryPageByStrSql(strSql, params, pageIndex, pageSize, clazz);
	}

	@Override
	public <T> List<T> queryListSql(String strSql, Map<String, Object> params, Class<T> clazz) throws Exception {
		return queryPageByStrSql(strSql, params, 0, 0, clazz).getData();
	}
	
	public <T> PageVo<T> queryPageByStrSql(final String strSql, final Map<String, Object> params, final int pageIndex, int pageSize, final Class<T> clazz) throws Exception {
		
		final Context context = generateVelocityContext(params);
		StringWriter writer = new StringWriter();
		
		String sqlOriginal = strSql;
		
        Velocity.evaluate(context, writer, "Hibernate", sqlOriginal);
        final String sql = writer.toString();
        
        Long total = 0L;
        if (pageIndex > 0 && pageSize > 0) {
        	final String sqlCount = "select sum(num) from (select count(*) as num " + removeSelect(removeOrders(sql)) + ") as total";
        	total = hibernateTemplate.execute(new HibernateCallback<Long>() {
    			public Long doInHibernate(Session session) throws HibernateException, SQLException {
    				Query query = session.createSQLQuery(sqlCount);
    				setProperties(query, context, query.getNamedParameters());
    				Number count = (Number)query.uniqueResult();
    				return count != null ? count.longValue() : 0;
    			}
    		});
        }
		
		if (pageSize <= 0) {
			if (this.pageSize > 0 ) {
				pageSize = this.pageSize;
			} else {
				pageSize = 10;
			}
		}
		
		final int pageSizeFinal = pageSize;
		List<T> list = hibernateTemplate.execute(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(new MyResultTransformer(clazz));
				setProperties(query, context, query.getNamedParameters());
				if (pageIndex > 0 && pageSizeFinal > 0) {	
		            query.setFirstResult((pageIndex - 1) * pageSizeFinal);
		            query.setMaxResults(pageSizeFinal);
		        }
				return query.list();
			}
		});
		
		long start = pageIndex > 0 && pageSizeFinal > 0 ? (pageIndex - 1) * pageSizeFinal : 0;
		return new PageVo<T>(start, total, pageSizeFinal, list);
	}
	
	@Override
	public <T> T insert(Object vo, Class<T> clazz) throws Exception {
		T md = clazz.newInstance();
		PropertyUtils.copyProperties(md, vo);
		setForeignMd(vo, md, clazz);
		hibernateTemplate.save(md);
		return md;
	}
	
	@Override
	public <T> void update(Object vo, Class<T> clazz) throws Exception {
		Object md = clazz.newInstance();
		PropertyUtils.copyProperties(md, vo);
		setForeignMd(vo, md, clazz);
		hibernateTemplate.update(md);
	}
	
	@Override
	public <T> int updateExcept(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception {
		return updateByCreateSql(vo, clazz, false, fieldNameArray, null, null);
	}
	
	@Override
	public <T> int updateInclude(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception {
		return updateByCreateSql(vo, clazz, false, null, null, fieldNameArray);
	}
	
	@Override
	public <T> int updateNonull(Object vo, Class<T> clazz) throws Exception {
		return updateByCreateSql(vo, clazz, true, null, null, null);
	}
	
	@Override
	public <T> int updateNonullNoById(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception {
		return updateByCreateSql(vo, clazz, true, null, fieldNameArray, null);
	}
	
	public <T> int updateByCreateSql(Object vo, Class<T> clazz, boolean isNonull, String[] exceptFieldNameArray, String[] selectFieldNameArray, String[] includeFieldNameArray) throws Exception {
		
		HashSet<String> exceptFieldNameSet = new HashSet<String>();
		if (exceptFieldNameArray != null) {
			for (String fieldName : exceptFieldNameArray) {
				exceptFieldNameSet.add(fieldName);
			}
		}
		
		HashSet<String> includeFieldNameSet = new HashSet<String>();
		if (includeFieldNameArray != null) {
			for (String fieldName : includeFieldNameArray) {
				includeFieldNameSet.add(fieldName);
			}
		}
		
		Table table = (Table) clazz.getAnnotation(Table.class);
		String tableName = table.name();
		
		StringBuilder sqlBuilder = new StringBuilder("UPDATE " + tableName);
		StringBuilder sqlSetBuilder = new StringBuilder(" SET ");
		StringBuilder sqlWhereBuilder = new StringBuilder(" WHERE 1=1");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Set<String> fieldSet = new HashSet<String>();
		Set<String> fieldPrimarySet = new HashSet<String>();
		
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			
			Set<String> tempSet = null;
			Id id = method.getAnnotation(Id.class);
			if (id != null) {
				tempSet = fieldPrimarySet;
			} else {
				tempSet = fieldSet;
			}
			
			ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
			if (manyToOne != null) {
				JoinColumns joinColumns = method.getAnnotation(JoinColumns.class);
				if (joinColumns != null) {
					JoinColumn joinColumnArray[] = joinColumns.value();
					for (JoinColumn joinColumn : joinColumnArray) {
						tempSet.add(joinColumn.name().toUpperCase());
					}
				} else {
					JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
					tempSet.add(joinColumn.name().toUpperCase());
				}
			} else {
				Column column = method.getAnnotation(Column.class);
				if (column != null) {					
					tempSet.add(column.name().toUpperCase());
				}
			}
		}
		
		Class<?> clazzVo = vo.getClass();
		Field[] fields = clazzVo.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);   
		
		if (selectFieldNameArray != null) {		
			for (String fieldPrimaryName : selectFieldNameArray) {
				boolean bool = false;
				for (Field field : fields) {
					if (field.getName().equals(fieldPrimaryName)) {
						bool = true;
						Object fieldValue = field.get(vo);
						if (fieldValue != null) {
							sqlWhereBuilder.append(" AND ");
							sqlWhereBuilder.append(field.getName());
							sqlWhereBuilder.append(" = :");
							sqlWhereBuilder.append(field.getName());
							paramMap.put(field.getName(), fieldValue);
						} else {
							throw new Exception("UpdateByCreateSql -> Select field is null in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
						}
						break;
					}
				}
				if (!bool) {						
					throw new Exception("UpdateByCreateSql -> Select field is not exist in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
				}
			}
		} else {
			for (String fieldPrimaryName : fieldPrimarySet) {
				boolean bool = false;
				for (Field field : fields) {
					if (field.getName().toUpperCase().equals(fieldPrimaryName)) {
						bool = true;
						Object fieldValue = field.get(vo);
						if (fieldValue != null) {
							sqlWhereBuilder.append(" AND ");
							sqlWhereBuilder.append(field.getName());
							sqlWhereBuilder.append(" = :");
							sqlWhereBuilder.append(field.getName());
							paramMap.put(field.getName(), fieldValue);
						} else {
							throw new Exception("UpdateByCreateSql -> Id field is null in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
						}
						break;
					}
				}
				if (!bool) {						
					throw new Exception("UpdateByCreateSql -> Id field is not exist in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
				}
			}
		}
		
		for (Field field : fields) {
			String fieldName = field.getName();
			if (fieldSet.contains(fieldName.toUpperCase())) {
				if (exceptFieldNameArray == null || (exceptFieldNameArray != null && !exceptFieldNameSet.contains(fieldName))) {
					if (includeFieldNameArray == null || (includeFieldNameArray != null && includeFieldNameSet.contains(fieldName))) {
						Object fieldValue = field.get(vo);
						if (fieldValue != null) {
							sqlSetBuilder.append(fieldName);
							sqlSetBuilder.append(" = :");
							sqlSetBuilder.append(fieldName);
							sqlSetBuilder.append(", ");
							paramMap.put(fieldName, fieldValue);
						} else {
							if (!isNonull) {
								sqlSetBuilder.append(fieldName);
								sqlSetBuilder.append(" = NULL");
								sqlSetBuilder.append(", ");							
							}
						}
					}
				}
			}
		}
		
		if (sqlSetBuilder.length() <= 5) {
			return 0;
		}
		
		sqlBuilder.append(sqlSetBuilder.substring(0, sqlSetBuilder.length() - 2).toString());
		sqlBuilder.append(sqlWhereBuilder.toString());
		
		final Context context = generateVelocityContext(paramMap);
        final String sql = sqlBuilder.toString();
        
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				String[] namedParams = query.getNamedParameters();
				setProperties(query, context, namedParams);
				return query.executeUpdate();
			}
		}); 
	}
	
	@Override
	public int updateSql(String sqlName, Map<String, Object> params) throws Exception {
		
		final Context context = generateVelocityContext(params);
		StringWriter writer = new StringWriter();
		
		String sqlOriginal = getSqlByName(sqlName);
		if (sqlOriginal == null) {
			throw new Exception("UpdateSql -> The sql is not find: " + sqlName);
		}
		
        Velocity.evaluate(context, writer, "Hibernate", sqlOriginal);
        final String sql = writer.toString();
        
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				String[] namedParams = query.getNamedParameters();
				setProperties(query, context, namedParams);
				return query.executeUpdate();
			}
		}); 
	}
	
	@Override
	public <T> void delete(Object vo, Class<T> clazz) throws Exception {
		Object md = clazz.newInstance();
		PropertyUtils.copyProperties(md, vo);
		setForeignMd(vo, md, clazz);
		hibernateTemplate.delete(md);
	}
	
	@Override
	public <T> void deleteNoById(Object vo, Class<T> clazz, String[] selectFieldNameArray) throws Exception {
		
		HashSet<String> exceptFieldNameSet = null;
		if (selectFieldNameArray != null) {
			exceptFieldNameSet = new HashSet<String>();
			for (String fieldName : selectFieldNameArray) {
				exceptFieldNameSet.add(fieldName);
			}
		}
		
		Table table = (Table) clazz.getAnnotation(Table.class);
		String tableName = table.name();
		
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + tableName);
		StringBuilder sqlWhereBuilder = new StringBuilder(" WHERE 1=1");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		Class<?> clazzVo = vo.getClass();
		Field[] fields = clazzVo.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);   
		if (selectFieldNameArray != null) {
			for (String fieldPrimaryName : selectFieldNameArray) {
				boolean bool = false;
				for (Field field : fields) {
					if (field.getName().equals(fieldPrimaryName)) {
						bool = true;
						Object fieldValue = field.get(vo);
						if (fieldValue != null) {
							sqlWhereBuilder.append(" AND ");
							sqlWhereBuilder.append(fieldPrimaryName);
							sqlWhereBuilder.append(" = :");
							sqlWhereBuilder.append(fieldPrimaryName);
							paramMap.put(fieldPrimaryName, fieldValue);
						} else {
							throw new Exception("DeleteNoById -> Select field is null in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
						}
						break;
					}
				}
				if (!bool) {						
					throw new Exception("DeleteNoById -> Select field is not exist in vo, Class: " + clazzVo.getName() + ", Field: " + fieldPrimaryName);
				}
			}
		}
		
		sqlBuilder.append(sqlWhereBuilder.toString());
		
		final Context context = generateVelocityContext(paramMap);
        final String sql = sqlBuilder.toString();
        
        hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				String[] namedParams = query.getNamedParameters();
				setProperties(query, context, namedParams);
				return query.executeUpdate();
			}
		}); 
	}
	
	@Override
	public <T> T load(Object vo, final Class<T> clazz) throws Exception {
		return loadBySql(vo, clazz);
	}
	
	public <T> T loadBySql(Object vo, Class<T> clazz) throws Exception {
		
		Map<String, Object> primaryFieldMap = new HashMap<String, Object>();
		
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Id id = method.getAnnotation(Id.class);
			if (id != null) {
				ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
				if (manyToOne != null) {
					JoinColumns joinColumns = method.getAnnotation(JoinColumns.class);
					if (joinColumns != null) {
						JoinColumn[] joinColumnArray = joinColumns.value();
						for (JoinColumn joinColumn : joinColumnArray) {
							primaryFieldMap.put(joinColumn.name().toUpperCase(), joinColumn.name().toUpperCase());
						}
					} else {
						JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
						primaryFieldMap.put(joinColumn.name().toUpperCase(), joinColumn.name().toUpperCase());
					}
				} else {
					Column column = method.getAnnotation(Column.class);
					if (column != null) {					
						primaryFieldMap.put(column.name().toUpperCase(), column.name().toUpperCase());
					}
				}
			}
		}
		
		Class<?> clazzVo = vo.getClass();
		Field[] fields = clazzVo.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);   
		Set<String> primaryFieldSet = primaryFieldMap.keySet();
		for (String primaryFieldName : primaryFieldSet) {
			boolean bool = false;
			for (Field field : fields) {
				if (field.getName().toUpperCase().equals(primaryFieldName)) {
					bool = true;
					Object fieldValue = field.get(vo);
					if (fieldValue != null) {
						primaryFieldMap.put(primaryFieldName, fieldValue);
					} else {
						throw new Exception("LoadBySql -> Id field is null in vo, Class: " + clazzVo.getName() + ", Field: " + primaryFieldName);
					}
					break;
				}
			}
			if (!bool) {						
				throw new Exception("LoadBySql -> Id field is not exist in vo, Class: " + clazzVo.getName() + ", Field: " + primaryFieldName);
			}
		}
		
		return loadBySql(primaryFieldMap, clazz);
	}
	
	public <T> T loadBySql(Map<String, Object> map, final Class<T> clazz) throws Exception {
		
		Table table = (Table) clazz.getAnnotation(Table.class);
		String tableName = table.name();
		
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + tableName + " WHERE 1=1 "); 
		
		Set<String> fieldNameSet = map.keySet();
		for (String fieldName : fieldNameSet) {
			sqlBuilder.append("AND ");
			sqlBuilder.append(fieldName.toUpperCase());
			sqlBuilder.append(" = :");
			sqlBuilder.append(fieldName.toUpperCase());
			sqlBuilder.append(" ");
		}
		
		final Context context = generateVelocityContext(map);
        final String sql = sqlBuilder.toString();
        
        T object = hibernateTemplate.execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).addEntity(clazz);
				String[] namedParams = query.getNamedParameters();
				setProperties(query, context, namedParams);
				@SuppressWarnings("unchecked")
				List<T> list = query.list();
				if (list.size() != 0) {
					return list.get(0);
				} else {
					return null;
				}
			}
		});
		
		return object;
	}
	
	private <T> void setForeignMd(Object vo, Object md, Class<T> clazz) throws Exception {
		
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
			if (manyToOne != null) {
				
				Map<String, Object> foreignNameMap = new HashMap<String, Object>();
				JoinColumns joinColumns = method.getAnnotation(JoinColumns.class);
				if (joinColumns != null) {
					JoinColumn[] joinColumnArray = joinColumns.value();
					for (JoinColumn joinColumn : joinColumnArray) {
						if (joinColumn.referencedColumnName() != null && !joinColumn.referencedColumnName().equals("")) {
							foreignNameMap.put(joinColumn.referencedColumnName().toUpperCase(), joinColumn.name().toUpperCase());
						} else {
							foreignNameMap.put(joinColumn.name().toUpperCase(), joinColumn.name().toUpperCase());
						}
					}
				} else {
					JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
					if (joinColumn.referencedColumnName() != null && !joinColumn.referencedColumnName().equals("")) {
						foreignNameMap.put(joinColumn.referencedColumnName().toUpperCase(), joinColumn.name().toUpperCase());
					} else {
						foreignNameMap.put(joinColumn.name().toUpperCase(), joinColumn.name().toUpperCase());
					}
				}
				
				Set<String> foreignNameSet = foreignNameMap.keySet();
				for (String referencedFieldName : foreignNameSet) {
					String fieldName = (String) foreignNameMap.get(referencedFieldName);
					Class<?> clazzVo = vo.getClass();
					Field[] fields = clazzVo.getDeclaredFields();
					AccessibleObject.setAccessible(fields, true);					
					for (Field field : fields) {
						if (field.getName().toUpperCase().equals(fieldName)) {
							Object fieldValue = field.get(vo);
							if (fieldValue != null && !fieldValue.equals("")) {
								foreignNameMap.put(referencedFieldName, fieldValue);
							} else {
								foreignNameMap.remove(referencedFieldName);
							}
							break;
						}
					}
				}
				
				if (foreignNameMap.keySet().size() > 0) {
					String methodName = method.getName();
					Class<?> returnType = method.getReturnType();
					Object mdForeign = loadBySql(foreignNameMap, returnType);
					if (mdForeign == null) {
						throw new Exception("SetForeignMd -> The foreign record not exit");
					}
					Method methodSet = clazz.getMethod("set" + methodName.substring(3), returnType);
					methodSet.invoke(md, mdForeign);
				}
			}
		}
	}
	
	
	@Override
	public void flush() {
		hibernateTemplate.flush();
	}

	@Override
	public <T> T insertOrUpdate(Object vo, Class<T> clazz) throws Exception {
		T md = clazz.newInstance();
		PropertyUtils.copyProperties(md, vo);
		setForeignMd(vo, md, clazz);
		hibernateTemplate.saveOrUpdate(md);
		return md;
	}
	
	class MyResultTransformer implements ResultTransformer {
		
		private static final long serialVersionUID = 4216528307922649659L;

		Class<?> clazz;
		
		MyResultTransformer(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] arg0, String[] arg1) {
			
			Object object = null;
			
			try {
				object = clazz.newInstance();
				for (int i=0; i<arg1.length; i++) {
					Field field = clazz.getDeclaredField(arg1[i]);
					field.setAccessible(true);
					Object backObject = getByType(field, arg0[i]);
					field.set(object, backObject);
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("MyResultTransformer -> transformTuple, Class: " + clazz.getName() + " -> " + e);
				}
			}
			
			return object;
		}
		
		private Object getByType(Field field, Object paramObject) {
			
			if (paramObject instanceof BigInteger) {
            	
        		BigInteger bigInteger = (BigInteger) paramObject;

        		if (field.getType().isAssignableFrom(int.class)
	            		|| field.getType().isAssignableFrom(Integer.class)) {
	            	
    	            paramObject = bigInteger.intValue();
	            	
	            } else if (field.getType().isAssignableFrom(long.class)
	            			|| field.getType().isAssignableFrom(Long.class)) {
	            	
    	            paramObject = bigInteger.longValue();
    	            
	            } else if (field.getType().isAssignableFrom(Date.class)) {
	            	
	            	paramObject = new Date(bigInteger.longValue());
	            }
        		
            } else if (paramObject instanceof BigDecimal) {
            	
            	BigDecimal bigDecimal = (BigDecimal) paramObject;
            	
            	if (field.getType().isAssignableFrom(int.class)
	            		|| field.getType().isAssignableFrom(Integer.class)) {
	            	
    	            paramObject = bigDecimal.intValue();
	            	
	            } else if (field.getType().isAssignableFrom(long.class)
	            			|| field.getType().isAssignableFrom(Long.class)) {
	            	
    	            paramObject = bigDecimal.longValue();
    	            
	            } else if (field.getType().isAssignableFrom(Date.class)) {
	            	
	            	paramObject = new Date(bigDecimal.longValue());
	            	
	            } else if (field.getType().isAssignableFrom(float.class)
            			|| field.getType().isAssignableFrom(Float.class)) {
	            	
	            	paramObject = bigDecimal.floatValue();
	            	
	            } else if (field.getType().isAssignableFrom(double.class)
            			|| field.getType().isAssignableFrom(Double.class)) {
	            	
	            	paramObject = bigDecimal.doubleValue();
	            }
            }
			
			return paramObject;
		}
		
	}
}
