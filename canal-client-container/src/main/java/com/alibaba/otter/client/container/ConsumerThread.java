package com.alibaba.otter.client.container;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 大舒 on 2017/8/30.
 */
public class ConsumerThread implements Runnable{
    public static final String          UPDATECOLUMNNAMES               = "updateColumnNames";
    private List<Entry> entries;
    private   List<TableFilter> tableFilters;
    public ConsumerThread(List<Entry> entries, List<TableFilter> tableFilters){
        this.entries = entries;
        this.tableFilters = tableFilters;
    }
    @Override
    public void run() {
        for (Entry entry : entries) {
            //获取数据
            if(entry.getEntryType() == EntryType.ROWDATA){
                RowChange rowChage = null;
                Result result = null;
                Map<String, Object> updatColumns = null;
                boolean isUpdate = false;
                for (TableFilter tableFilter : tableFilters) {
                    //判断拦截的表名
                    if (tableFilter.getTables().contains(entry.getHeader().getTableName()) || tableFilter.getTables().contains("*")){
                        try {
                            rowChage = RowChange.parseFrom(entry.getStoreValue());
                        } catch (Exception e) {
                            throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                        }
                        for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                            //TODO 扩展字段判断，是否有重复的列
                            if(rowData.getPropsCount()>0 && rowData.getProps(0).getKey().equals(UPDATECOLUMNNAMES)){
                                isUpdate = isHaveUpdate(rowData.getProps(0).getValue(),tableFilter.getUpdateColumns());
                                if(!isUpdate){
                                    continue;
                                }
                            }
                            result = new Result();
                            result.setEventType(rowChage.getEventType());
                            result.setBeforeColumns(rowData.getBeforeColumnsList());
                            result.setAfterColumns(rowData.getAfterColumnsList());
                            result.setExecuteTime(new Date(entry.getHeader().getExecuteTime()));
                            if(rowData.getPropsCount()>0 && rowData.getProps(0).getKey().equals(UPDATECOLUMNNAMES)){
                                result.setUpdateColumnNams(rowData.getProps(0).getValue());
                            }

                            result.setAfColumns(Result.toTransformM(rowData.getAfterColumnsList()));
                            result.setBfColumns(Result.toTransformM(rowData.getBeforeColumnsList()));
                            result.setTableName(entry.getHeader().getTableName());
                            tableFilter.run(result);
                        }

                    }
                }
            }else {
                //logger.info("entry type :{}",entry.getHeader().getEventType());
            }
        }
    }

    private boolean isHaveUpdate(String updateColumnNames,String[] tableFilterUpdateColumns){
        if(tableFilterUpdateColumns != null && tableFilterUpdateColumns.length>0) {
          for (String updateColumn : tableFilterUpdateColumns) {
              if (updateColumnNames.contains(updateColumn)) {
                 return true;
              }
          }
            return false;
        }else{
            return true;
        }
    }
}
