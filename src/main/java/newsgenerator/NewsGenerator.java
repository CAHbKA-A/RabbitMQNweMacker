package newsgenerator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class NewsGenerator {

    private final static String[] NEWS_TOPIC = {"java", "python", "basic", "C++"};
    private final static String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Random ran = new Random();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            for (int i = 0; i < 1000; i++) {
                String routingKey = NEWS_TOPIC[ran.nextInt(NEWS_TOPIC.length)];

                String message = "Blahblah in " + routingKey + " Post №" + i;
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                Thread.sleep(500 * ran.nextInt(10));
                System.out.println(" Published news about " + routingKey + ":'" + message + "'");
            }
        }


    }

}
