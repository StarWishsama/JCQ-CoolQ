package com.sobte.cqp.jcq.entity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Sobte on 2018/4/11.<br>
 * Time: 2018/4/11 13:37<br>
 * Email: i@sobte.me<br>
 * CQ码功能类 (线程安全)
 *
 * @author Sobte
 */
public class ActionCode {

    /**
     * 功能名称
     */
    private String action;

    /**
     * 功能键值队
     */
    private ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<String, String>();

    /**
     * 第一次访问
     */
    private boolean first;

    public ActionCode() {

    }

    public ActionCode(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ActionCode) {
            ActionCode ac = (ActionCode) obj;
            return ac.getAction() != null && ac.getAction().equals(this.action);
        }
        return false;
    }

    /**
     * 设置功能名称
     *
     * @param action 功能名称
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 获取功能名称
     *
     * @return 功能名称
     */
    public String getAction() {
        return action;
    }

    /**
     * 放置功能键值
     *
     * @param key   键
     * @param value 值
     * @return 与键关联的前一个值，如果没有映射则返回 {@code null}
     */
    public String put(String key, String value) {
        return map.put(key, value);
    }

    /**
     * 批量添加
     *
     * @param m 要添加的map
     */
    public void putAll(Map<? extends String, ? extends String> m) {
        map.putAll(m);
    }

    /**
     * 根据下标，放置功能键值，没有对应的下标，则放置失败，并返回 {@code null}
     *
     * @param index 下标
     * @param value 值
     * @return 与键关联的前一个值，如果没有映射则 或 没有对应的下标 返回 {@code null}
     */
    public String put(int index, String value) {
        Iterator<String> keys = map.keySet().iterator();
        for (int i = 0; keys.hasNext(); i++)
            if (i == index)
                return map.put(keys.next(), value);
        return null;
    }

    /**
     * 获取指定键的值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return getValue(key);
    }

    /**
     * 根据下标，获取指定位置的值
     *
     * @param index 下标
     * @return 值
     */
    public String get(int index) {
        Iterator<String> keys = map.keySet().iterator();
        for (int i = 0; keys.hasNext(); i++)
            if (i == index)
                return getValue(keys.next());
        return null;
    }

    /**
     * 获取值 第一次加载时解码酷Q
     *
     * @return 值
     */
    private String getValue(String key) {
        if (!first) {
            for (String k : map.keySet()) {
                map.put(k, CQCode.decode(map.get(k)));
            }
            first = true;
        }
        return map.get(key);
    }

    /**
     * 获取键集合，用于遍历
     *
     * @return 键集合
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     * 删除指定坐标的 键值
     *
     * @param index 坐标
     * @return 被删键的值
     */
    public String remove(int index) {
        Iterator<String> keys = map.keySet().iterator();
        for (int i = 0; keys.hasNext(); i++)
            if (i == index)
                return map.remove(keys.next());
        return null;
    }

    /**
     * 删除指定坐标的 键值
     *
     * @param key 键
     * @return 被删键的值
     */
    public String remove(String key) {
        return map.remove(key);
    }

    /**
     * 获取大小
     *
     * @return 大小
     */
    public int size() {
        return map.size();
    }

    /**
     * 清空
     */
    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[CQ:");
        sb.append(CQCode.encode(action, true));
        for (String key : map.keySet()) {
            sb.append(',');
            sb.append(CQCode.encode(key, true));
            sb.append('=');
            sb.append(CQCode.encode(map.get(key), true));
        }
        sb.append(']');
        return sb.toString();
    }

}
