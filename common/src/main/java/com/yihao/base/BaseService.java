package com.yihao.base;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2014/12/6.
 */
@Service
public class BaseService extends SqlSessionTemplate implements Base, ApplicationContextAware, InitializingBean {
        protected javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //    public String className = "";
    protected ApplicationContext applicationContext;

    @Autowired
    protected RedisCacheManager cacheManager;
    @Autowired
    protected JedisPool jedisPool;

    @Autowired
    public BaseService(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
//        Class clazz = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()
/*        Type type = getClass().getGenericSuperclass();
        Class clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            className = clazz.getSimpleName();
        } else {
            String namee = ((Class) type).getSimpleName();
            className = namee.substring(0, namee.indexOf("Service")==-1?namee.length():namee.indexOf("Service"));
        }*/
//           System.out.println("***********\t"+className);
    }


    //    @Resource
//    protected Validator validator;


    public <E> BaseReturn validAndReturn(E t) {
        Set<ConstraintViolation<E>> errs = validator.validate(t);
        if (errs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<E> cons : errs)
                sb.append(cons.getPropertyPath() + ":" + cons.getInvalidValue() + "===>" + cons.getMessage() + "\r\n");
            return new BaseReturn(1, sb.toString());
        } else return new BaseReturn(0, null);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO:将系统数据初始化到缓存
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public Object getCacheOfValue(String cacheName, Object key) {
        return cacheManager.getCache(cacheName).get(key).get();
    }
    public Object getCacheOfValue(String cacheName, Object key,Class clazz) {
        return cacheManager.getCache(cacheName).get(key,clazz);
    }

    public void setCacheOfValue(String cacheName, Object key, Object value) {
        cacheManager.getCache(cacheName).put(key,value);
    }

    @Override
    public <T> T selectOne(String statement) {
        return super.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return super.selectOne(statement, parameter);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return super.selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return super.selectMap(statement, parameter, mapKey);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return super.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return super.selectList(statement, parameter);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        super.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        super.select(statement, parameter, handler);
    }

    @Override
    public int update(String statement) {
        return super.update(statement);
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }
}
