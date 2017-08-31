package com.alibaba.otter.client.container;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * binlog 消费容器
 * Created by 大舒 on 2017/8/28.
 */
public class ConsumerContainer {
    private static Logger logger = LoggerFactory.getLogger(ConsumerContainer.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);
    //canal目标集合，不能重复
    private static List<String> destinations = new ArrayList<String>();
    private CanalConnector connector;
    private String destination;
    private String hostname;
    private int port;
    private String userName;
    private String pwd;
    //获取数据大小
    private int batchSize = 1024;
    private volatile boolean isRuning = true;
    private volatile boolean isInit = false;
    //表拦截器
    private List<TableFilter> tableFilters = new ArrayList<TableFilter>();
    public ConsumerContainer(String destination,String hostname,int port,String userName,String pwd){
        this.destination = destination;
        this.hostname = hostname;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
        init();
    }
    public ConsumerContainer(){
    }

    /**
     * 初始化
     */
    public void init(){
        logger.info("canal consumerContainer init");
        if(destinations.contains(this.destination)){
            logger.error("this destination :{} is exist",this.destination);
            return;
        }
        this.destinations.add(this.destination);
        connector  = CanalConnectors.newSingleConnector(new InetSocketAddress(hostname, port),
                destination,
                userName,
                pwd);
        this.isInit = true;
    }

    public void run(){
        if(!this.isInit){
            init();
        }
        try {
            MDC.put("destination", destination);
            connector.connect();
            connector.subscribe();
            List<Entry>  entries= null;
            logger.info("canal consumerContainer is runing!");
            while (isRuning) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {

                } else {
                    entries = message.getEntries();
                    executorService.execute(new ConsumerThread(entries,this.tableFilters));
                }
                connector.ack(batchId); // 提交确认

            }
        } catch (Exception e) {
             //connector.rollback(batchId); // 处理失败, 回滚数据
        } finally {
            connector.disconnect();
            MDC.remove("destination");
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public List<TableFilter> getTableFilters() {
        return tableFilters;
    }

    public void setTableFilters(List<TableFilter> tableFilters) {
        this.tableFilters = tableFilters;
    }
    public void addTableFilter(TableFilter tableFilters) {
        this.tableFilters.add(tableFilters);
    }
}
