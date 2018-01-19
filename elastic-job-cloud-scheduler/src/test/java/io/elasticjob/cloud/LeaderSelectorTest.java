package io.elasticjob.cloud;

import io.elasticjob.cloud.scheduler.ha.HANode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * Created by bdiao on 18/1/19.
 */
public class LeaderSelectorTest {

    @Test
    public void test() throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183")
                .retryPolicy(new ExponentialBackoffRetry(100, 1000, 3))
                .namespace("curator-test").build();
        client.start();

        LeaderSelector selector = new LeaderSelector(client, HANode.ELECTION_NODE, new LeaderSelectorListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

            }

            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("client take leadership, state:" + client.getState() + " zookeeper master node:"+client.getZookeeperClient().getZooKeeper().toString());
            }
        });
        selector.start();

        Thread.sleep(3000);
    }
}
