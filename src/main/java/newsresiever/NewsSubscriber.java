package newsresiever;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class NewsSubscriber {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] argv) throws Exception {

        System.out.println("Set your topic. Example  : 'set_topic php'");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String  routingKey ="broadcast";


        if ("set_topic".equalsIgnoreCase(input.substring(0, input.indexOf(" ")))) {
              routingKey = input.substring(input.indexOf(" ") + 1); //эксепшены не ловлю.

        }
        System.out.println(routingKey);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();


        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println(" You subscribed to news about " + routingKey );

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println( "One post about "+delivery.getEnvelope().getRoutingKey() + ":" + message+"'");

        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
