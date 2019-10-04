package com.koskom.camel;

import org.apache.camel.builder.RouteBuilder;


public class JmsReadRoute extends RouteBuilder {
    private String fromQueue;
    private String[] toQueues;

    JmsReadRoute(String fromQueue, String toQueue) {
        this.fromQueue = "mq:queue:" + fromQueue;
        this.toQueues = toQueue.split(",");
        for (int i = 0; i < toQueues.length; i++) {
            toQueues[i] = "mq:queue:" + toQueues[i];
        }
    }

    public void configure() {
        from(fromQueue)
                .log(String.format("Sending from %s JMSMessageID = ${header.JMSMessageID} to %s", fromQueue, String.join(", ", toQueues)))
                .to(toQueues);
    }
}
