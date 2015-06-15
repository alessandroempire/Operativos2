//aca va la implementacion del trabajador (Worker)

import java.util.Queue;

public class WorkerInfo {
    
    private String ip; 
    private String port;
    private String name;
    private String service;
    private Queue queue; 

    public WorkerInfo() {
    }
    
    public WorkerInfo(String ip, String port, String name, 
                      String service, Queue queue) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.service = service;
        this.queue = queue;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}