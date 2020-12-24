package com.xiaofang.rpc.register;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @author xiaofang
 */
@Getter
@Setter
@Slf4j
public class RpcRegistry {

    private String registryAddress;
    private ZooKeeper zooKeeper;

    public void createNode(String data) throws Exception {
        zooKeeper = new ZooKeeper(registryAddress, Constant.SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        if (zooKeeper != null) {
            try {
                Stat stat = zooKeeper.exists(Constant.REGISTRY_PATH, false);

                if (stat == null) {
                    //不存在，创建一个持久的节点目录
                    zooKeeper.create(Constant.REGISTRY_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                //创建一个临时节点
                log.info(Constant.DATA_PATH);
                zooKeeper.create(Constant.DATA_PATH, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            } catch (Exception e) {
                log.error("error={}", e);
            }
        } else {
            log.debug("zookeeper connect is null");
        }
    }

    public static void main(String[] args) throws Exception {
        String address = "127.0.0.1:2181";
        RpcRegistry rpcRegistry = new RpcRegistry();
        rpcRegistry.setRegistryAddress(address);
        rpcRegistry.createNode("testData");
        System.in.read();
    }
}
