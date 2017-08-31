package com.alibaba.otter.client.container;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

import java.util.Date;
import java.util.List;

/**
 * 变更结果
 * Created by 大舒 on 2017/8/29.
 */
public class Result {
    /**
     * 事件类型，insert,delete，update
     */
    private EventType eventType;
    /**
     * 变更后结果: insert 只有变更后数据
     */
    private List<Column> afterColumns;
    /**
     * 变更前数据：delete 只有变更前数据
     */
    private List<Column> beforeColumns;
    /**
     * 实际变更时间
     */
    private Date executeTime;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<Column> getAfterColumns() {
        return afterColumns;
    }

    public void setAfterColumns(List<Column> afterColumns) {
        this.afterColumns = afterColumns;
    }

    public List<Column> getBeforeColumns() {
        return beforeColumns;
    }

    public void setBeforeColumns(List<Column> beforeColumns) {
        this.beforeColumns = beforeColumns;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }
}
