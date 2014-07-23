
package org.futucode.ha.zk.master;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * ZooKeeper Client.
 */
public abstract class ZkClient implements Watcher {
    
    public static final int SESSION_TIMEOUT = 15000;
    
    private ZooKeeper zk;
    
    private final String hostPort;
    
    public ZkClient(String hostPort) {
        this.hostPort = hostPort;
    }
    
    @Override
    public void process(WatchedEvent we) {
        System.out.println(we);
    }
    
    public void startSession() throws IOException {
        this.zk = new ZooKeeper(hostPort, SESSION_TIMEOUT, this);
    }
    
    public void stopSession() throws InterruptedException {
        zk.close();
    }
    
    public ZooKeeper getZk() {
        return this.zk;
    }
    
}
