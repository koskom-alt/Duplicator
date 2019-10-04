package com.koskom.camel;



import com.sun.messaging.ConnectionConfiguration;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;


public class MessageDuplicator {
    private String url;
    private String from;
    private String to;

    MessageDuplicator(String url, String fromQueue, String toQueue) {

        this.url = url;
        this.from = fromQueue;
        this.to = toQueue;

    }


    public void run() throws Exception {
        JmsReadRoute routeBuilder = new JmsReadRoute(from , to);
        CamelContext ctx = new DefaultCamelContext();

        com.sun.messaging.ConnectionFactory connectionFactory1 = new com.sun.messaging.ConnectionFactory();
        connectionFactory1.setProperty(ConnectionConfiguration.imqAddressList, url);
        connectionFactory1.setProperty(ConnectionConfiguration.imqReconnectEnabled, "true");

        ctx.addComponent("mq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory1));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    ctx.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            ctx.addRoutes(routeBuilder);
            ctx.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        MessageDuplicator messageDuplicator = new MessageDuplicator(args[0], args[1], args[2]);

        try {
            messageDuplicator.run();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
