
package org.futucode.ha.zk.master;

import java.util.Random;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;

/**
 *
 * Asynchronous Master Watcher.
 */
public class AsyncMaster extends ZkClient implements Watcher {
    
    private static final String MASTER_PATH = "/master";
    
    private static boolean isLeader = false;
    
    private static final Random random = new Random();
    
    private static final String serverId = Integer.toString(random.nextInt());
    
    public AsyncMaster(String hostPort) {
        super(hostPort);
    }
    
    private static final AsyncCallback.StringCallback masterCreateCallback =
            new AsyncCallback.StringCallback() {
                
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    switch( Code.get(rc) ) {
                        case CONNECTIONLOSS:
                            checkMaster();
                            return;
                        case OK:
                            isLeader = true;
                            break;
                        default:
                            isLeader = false;
                    }
                    System.out.println("I'm " + (isLeader ? "" : "not") +
                            "the leader");
                }
                
            };
            
    private static void checkMaster() {};
    
    public void runForMaster() {
        this.getZk().create(MASTER_PATH, serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, masterCreateCallback, null);
    }
    
    public static void main(String[] args) {
        
        AsyncMaster asyncMaster = new AsyncMaster(args[0]);
        
    }
    
}
