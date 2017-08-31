<div class="blog_content">
    <div class="iteye-blog-content-contain">
<h3>主要是用于消费canal，提供简单容器，来获取数据变更记录，根据数据表拦截器，来过滤数据</h3>
<p>
    一.  canal服务端的部署，请参考阿里canal WIKI
    二.  消费容器
       步骤：
        1.根据canal服务，初始化ConsumerContainer
        2.新建TableFilter对象，设置需要拦截的数据表名，以及实现ExecuteService 类型，实现execute方法来处理拦截的数据，如更新redis数据，发送mq通知等业务
        3.启动ConsumerContainer，调用run()方法
</p>
<h3>阿里项目：https://github.com/alibaba/canal</h3>
</div>
</div>

