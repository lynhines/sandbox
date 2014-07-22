
package org.futucode.ha.zk.master;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * Master Watcher.
 */
public class Master implements Watcher {
    
    private static final int SESSION_TIMEOUT = 15000;
    
    private static final long MAIN_THREAD_TTL = 30000L;
    
    private ZooKeeper zk;
    
    private String hostPort;
    
    public Master(String hostPort) {
        this.hostPort = hostPort;
    }
    
    public void startZk() throws IOException {
        this.zk = new ZooKeeper(hostPort, SESSION_TIMEOUT, this);
    }
    
    public void stopZk() throws InterruptedException {
        zk.close();
    }
    
    @Override
    public void process(WatchedEvent we) {
        System.out.println(we);
    }
    
    public static void main(String[] args) throws Exception {
        
        Master m = new Master(args[0]);
        m.startZk();
        
        // Wait for a bit
        Thread.sleep(MAIN_THREAD_TTL);
        
        m.stopZk();
    }
    
}
