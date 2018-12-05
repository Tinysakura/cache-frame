package com.cfh.cacheframe.adapter.client;

import com.cfh.cacheframe.adapter.CacheClient;
import com.cfh.cacheframe.adapter.RejectHandler;
import com.cfh.cacheframe.common.PriorityBlockingMapQueue;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2018/12/4
 * @TODO 性能提升
 */

public class LFUCacheClient<K, V> extends ConcurrentHashMap<K, V> implements CacheClient {
    // 在内部维护一个与ConcurrentHashMap自身大小相同的小顶堆
    private PriorityBlockingMapQueue<Entry> hitFrequencySet;
    private Integer maxCapacity;
    private volatile long memSize;
    private RejectHandler<K, V> rejectHandler;

    public LFUCacheClient(Integer maxCapacity) {
        super(maxCapacity);
        this.maxCapacity = maxCapacity;

        hitFrequencySet = new PriorityBlockingMapQueue<>(maxCapacity, (e1, e2) -> e1.getHitFrequency() > e2.getHitFrequency() ? 1 : e1.getHitFrequency() == e2.getHitFrequency() ? 0 : -1);
    }

    public LFUCacheClient(Integer maxCapacity, Long memSize) {
        super(maxCapacity);
        this.maxCapacity = maxCapacity;
        this.memSize = memSize;

        hitFrequencySet = new PriorityBlockingMapQueue<>(maxCapacity, (e1, e2) -> e1.getHitFrequency() > e2.getHitFrequency() ? 1 : e1.getHitFrequency() == e2.getHitFrequency() ? 0 : -1);
    }

    public RejectHandler<K, V> getRejectHandler() {
        return rejectHandler;
    }

    public void setRejectHandler(RejectHandler<K, V> rejectHandler) {
        this.rejectHandler = rejectHandler;
    }

    /**
     * 注意重写后的put方法已经不再线程安全需要加锁
     * @param key
     * @param value
     * @return
     */
    @Override
    public synchronized V put(K key, V value) {
        // 放入新值，访问频次置为0
        hitFrequencySet.add(new Entry(key, 0L));

        // 淘汰最少被访问的缓存即小顶堆堆顶的元素
        K weedKey = hitFrequencySet.poll().getKey();
        super.remove(weedKey);

        return super.put(key, value);
    }

    @Override
    public synchronized V get(Object key) {
        // 更新对应缓存的访问频次
        hitFrequencySet.increaseHitFrequency(key);

        return super.get(key);
    }

    public class Entry{
        private K key;
        /**
         * 对于指定key的缓存的访问频次
         */
        private Long hitFrequency;

        public Entry(K key, Long hitFrequency) {
            this.key = key;
            this.hitFrequency = hitFrequency;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public Long getHitFrequency() {
            return hitFrequency;
        }

        public void setHitFrequency(Long hitFrequency) {
            this.hitFrequency = hitFrequency;
        }
    }
}