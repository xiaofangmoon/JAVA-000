package com.xiaofang.rpc.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xiaofang
 */
@Slf4j
public class RpcDiscover {
    /**
     * zoServer address
     */
    private String registryAddress;
    /**
     * 获取所有提供服务的服务器列表
     */
    private volatile List<String> dataList = new ArrayList<>();

    private ZooKeeper zooKeeper = null;

    public RpcDiscover(String registryAddress) throws IOException {
        this.registryAddress = registryAddress;

        zooKeeper = new ZooKeeper(registryAddress, Constant.SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    //监听zkServer 服务器列表变化
                    watchNode();
                }
            }
        });

        //获取节点数据
        watchNode();


    }

    public String discover() {
        int size = dataList.size();
        if ((size > 0)) {
            int index = new Random().nextInt(size);
            return dataList.get(index);
        }
        throw new RuntimeException("no server privide");
    }

    public void watchNode() {
        try {
            List<String> nodeList = zooKeeper.getChildren(Constant.REGISTRY_PATH, true);
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                log.info("node : " + node);
                byte[] bytes = zooKeeper.getData(Constant.REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            this.dataList = dataList;
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) throws IOException {
        log.info(new RpcDiscover("127.0.0.1:2181").discover());
        System.in.read();
    }

}
