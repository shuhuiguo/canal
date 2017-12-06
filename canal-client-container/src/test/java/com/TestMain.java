package com;

import com.alibaba.otter.client.container.ConsumerContainer;
import com.alibaba.otter.client.container.TableFilter;

/**
 * Created by 大舒 on 2017/8/29.
 */
public class TestMain {
    public static void main(String[] args){
        ConsumerContainer consumerContainer= new ConsumerContainer("shuhg","192.168.43.42",11111,"","");
        //ConsumerContainer consumerContainer= new ConsumerContainer("example","192.168.43.41",11111,"","");
        //TableFilter filter = new TableFilter();
        //filter.setTables("plt_image");

        //consumerContainer.addTableFilter(filter);
        consumerContainer.addTableFilter(new TableFilter("*"));
        consumerContainer.run();
    }
}
