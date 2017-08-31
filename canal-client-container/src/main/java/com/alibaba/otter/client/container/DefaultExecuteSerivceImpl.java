package com.alibaba.otter.client.container;

/**
 * Created by 大舒 on 2017/8/30.
 */
public class DefaultExecuteSerivceImpl implements ExecuteService {
    @Override
    public void execute(Result result) {
        {
            System.out.println("时间："+result.getExecuteTime());
            System.out.println("事件类型："+result.getEventType());
            System.out.println("更改前："+result.getBeforeColumns().get(0).getValue());
            System.out.println("更改后："+result.getAfterColumns().get(0).getValue());
        }
    }
}
