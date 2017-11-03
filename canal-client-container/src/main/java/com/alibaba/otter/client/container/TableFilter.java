package com.alibaba.otter.client.container;

import java.util.Arrays;
import java.util.List;

/**
 * 拦截器
 * Created by 大舒 on 2017/8/28.
 */
public class TableFilter {
    /**
     * 需要拦截的表明
     */
    private String tables;
    /**
     * 实际业务执行类
     */
    private ExecuteService executeService;

    public TableFilter(){};

    public TableFilter(String tables){
        this.tables = tables;
    }

    public TableFilter(String tables,ExecuteService executeService){
        this.tables = tables;
        this.executeService = executeService;
    }

    public void run(Result result){
        this.getExecuteService().execute(result);
    }

    public List<String> getTables(){
        String[] table = this.tables.split(",");
        List t = Arrays.asList(table);
        return t;
    }

    public ExecuteService getExecuteService() {
        if(this.executeService == null){
            executeService = new DefaultExecuteSerivceImpl();
        }
        return executeService;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public void setExecuteService(ExecuteService executeService) {
        this.executeService = executeService;
    }
}
