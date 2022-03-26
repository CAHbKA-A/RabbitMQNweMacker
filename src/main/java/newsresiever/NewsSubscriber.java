package newsresiever;

import com.rabbitmq.client.*;

public class NewsSubscriber {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        String routingKey = "basic";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println(" You subscribed to news about " + routingKey );

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(delivery.getEnvelope().getRoutingKey() + ":" + message);

        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
