
package org.futucode.ha.zk.master;

import java.util.Random;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 *
 * Synchronous Master Watcher.
 */
public class SyncMaster extends ZkClient implements Watcher {
    
    private static final long MAIN_THREAD_TTL = 30000L;
    
    private static final String MASTER_PATH = "/master";
    
    private static final Random random = new Random();
    
    private final String serverId = Integer.toString(random.nextInt());
    
    private boolean isLeader = false;
    
    public SyncMaster(String hostPort) {
        super(hostPort);
    }
    
    public boolean checkMaster() throws InterruptedException {
        while (true) {
            try {
                Stat stat = new Stat();
                byte data[] = getZk().getData(MASTER_PATH, this, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (KeeperException.NoNodeException nn_ex) {
                // No master, so try create it again
                return false;
            } catch (KeeperException.ConnectionLossException cl_ex) {
                // Swallow this type of exception
            } catch (KeeperException kex) {
                // Swallow general exception
            }
        }
    }
    
    public void runForMaster() throws InterruptedException {
        while (true) {
            try {
                this.getZk().create(MASTER_PATH, serverId.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException.NodeExistsException ne_ex) {
                isLeader = false;
            } catch (KeeperException.ConnectionLossException cl_ex) {
                // Swallow this type of exception
            } catch (KeeperException kex) {
                // Swallow general exception
            }
            
            if (checkMaster()) {
                break;
            }
        }
    }
    
    public boolean isLeader() {
        return isLeader;
    }
    
    public static void main(String[] args) throws Exception {
        
        SyncMaster syncMaster = new SyncMaster(args[0]);
        syncMaster.startSession();
        
        syncMaster.runForMaster();
        
        if (syncMaster.isLeader()) {
            System.out.println("I'm the leader.");
            // Wait for a bit
            Thread.sleep(MAIN_THREAD_TTL);
        } else {
            System.out.println("Someone else is the leader.");
        }
        
        syncMaster.stopSession();
    }
    
}
