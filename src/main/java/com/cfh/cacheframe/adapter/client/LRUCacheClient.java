package com.cfh.cacheframe.adapter.client;

import com.cfh.cacheframe.adapter.CacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * 基于LinkedHashMap扩展的线程安全的LRU缓存容器
 * @TODO 性能提升
 */
public class LRUCacheClient<K, V> extends LinkedHashMap<K, V> implements CacheClient {
    private static final Logger log = LoggerFactory.getLogger(LRUCacheClient.class);

    private final int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    // 使用读写锁保证线程安全的同时提高缓存的读性能
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public LRUCacheClient(int maxCapacity)
    {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    /**
     * 这个方法会在执行put或putAll方法时被调用，移除容器中最近最久未使用的entry
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() >= this.maxCapacity;
    }

    @Override
    public V put(K key, V value) {
        readWriteLock.writeLock().lock();

        try {
            return super.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("缓存添加失败");
        } finally {
            // 在finally块中释放写锁
            readWriteLock.writeLock().unlock();
        }

        return null;
    }

    @Override
    public V get(Object key) {
        readWriteLock.readLock().tryLock();

        try {
            return super.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("缓存获取失败");
        } finally {
            // 在finally块中释放读锁
            readWriteLock.readLock().unlock();
        }

        return null;
    }
}