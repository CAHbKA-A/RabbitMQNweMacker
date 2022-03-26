package newsgenerator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class NewsConsole {

    private final static String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        System.out.println("Enter you news. Example  : 'php some message'");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             Scanner scanner = new Scanner(System.in)) {
            while (true) {

                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    break;
                } else {
                    String routingKey = input.substring(0, input.indexOf(" "));
                    input = input.substring(input.indexOf(" ") + 1); //эксепшены не ловлю.
                    System.out.println(input + "____" + routingKey);
                    channel.basicPublish(EXCHANGE_NAME, routingKey, null, input.getBytes("UTF-8"));
                    System.out.println(" Published news about " + routingKey + ": '" + input + "'");
                }


            }
        }

    }

}
