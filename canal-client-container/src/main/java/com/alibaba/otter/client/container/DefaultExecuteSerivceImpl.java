package com.alibaba.otter.client.container;

import com.alibaba.fastjson.JSON;

/**
 * Created by 大舒 on 2017/8/30.
 */
public class DefaultExecuteSerivceImpl implements ExecuteService {
    @Override
    public void execute(Result result) {
        {
            System.out.println("时间："+result.getExecuteTime());
            System.out.println("事件类型："+result.getEventType());
            System.out.println("列大小："+result.getBeforeColumns().size());
            System.out.println("更改前："+JSON.toJSONString(result.getBfColumns()));
            System.out.println("更改后："+ JSON.toJSONString(result.getAfColumns()));
            System.out.println(result.toString());
        }
    }
}
