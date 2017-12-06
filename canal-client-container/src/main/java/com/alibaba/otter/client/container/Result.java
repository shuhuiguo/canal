package com.alibaba.otter.client.container;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

import java.util.*;

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
     * 变更前
     */
    private Map<String,Object> bfColumns;

    /**
     * 变更后
     */
    private Map<String,Object> afColumns;
    /**
     * 实际变更时间
     */
    private Date executeTime;

    private String tableName;

    /**
     * 变更的列
     */
    private String updateColumnNams;

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

    public Map<String, Object> getBfColumns() {
        return bfColumns;
    }

    public void setBfColumns(Map<String, Object> bfColumns) {
        this.bfColumns = bfColumns;
    }

    public Map<String, Object> getAfColumns() {
        return afColumns;
    }

    public void setAfColumns(Map<String, Object> afColumns) {
        this.afColumns = afColumns;
    }

    public static Map<String,Object> toTransformM(List<Column> columns){
        Map<String,Object> map = new HashMap<String, Object>();
        for(Column column : columns){
            map.put(column.getName(),column.getValue());
        }
        return map;
    }
    public static Map<String,Object> toTransformM(List<Column> columns,TableFilter tableFilter){
        Map<String,Object> map = new HashMap<String, Object>();
        boolean isUpdate = true;
        List<String> updateColumns = new ArrayList<String>();
        if(tableFilter.getUpdateColumns() != null && tableFilter.getUpdateColumns().length >0){
            updateColumns = Arrays.asList(tableFilter.getUpdateColumns());
            isUpdate = false;
        }
        for(Column column : columns){
            //TODO 该列是否已更新
            if(!isUpdate && updateColumns.contains(column.getName()) && column.getUpdated()){
                isUpdate = true;
            }
            map.put(column.getName(),column.getValue());
        }
        if(isUpdate){
            return map;
        }else{
            return null;
        }
    }
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "Result{" +
                "eventType=" + eventType +
                ", bfColumns=" + bfColumns +
                ", afColumns=" + afColumns +
                ", executeTime=" + executeTime +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    public String getUpdateColumnNams() {
        return updateColumnNams;
    }

    public void setUpdateColumnNams(String updateColumnNams) {
        this.updateColumnNams = updateColumnNams;
    }
}
