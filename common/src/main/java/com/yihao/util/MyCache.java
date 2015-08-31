package com.yihao.util;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lenovo on 2014/12/27.
 */
public class MyCache implements Cache {
    private String name;
    private Jedis client;
    private int expire;
    public MyCache(){}

    public MyCache(String name, JedisPool jedisPool) {
        Assert.notNull(client, "Memcahce client must not be null");
        this.client=jedisPool.getResource();
        this.name=name;
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return client;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value=this.client.get(objectToString(key));
        return (value != null ? new SimpleValueWrapper(value) : null);
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        return ((T)o);
    }

    public static String objectToString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return (String) object;
        } else {
            return object.toString();
        }
    }
    @Override
    public void put(Object key, Object value) {
//        this.client.setex(objectToString(key),expire,value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
//        if (client.get(o)=null)
//            return client.set(o, o1);
//        else
//            return client.get(o);
        return null;
    }

    @Override
    public void evict(Object key) {
        this.client.del(objectToString(key));
    }

    @Override
    public void clear() {
        //TODO: delete all data;
    }

    public Jedis getClient() {
        return client;
    }

    public void setClient(Jedis client) {
        this.client = client;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
