import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TopicoConsumidorComercial {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = ((ConnectionFactory) context.lookup("ConnectionFactory"));

        Connection connection = connectionFactory.createConnection();
        connection.setClientID("comercial");  //identificador da conexão para o topico
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic loja = (Topic) context.lookup("loja");

        MessageConsumer consumer = session.createDurableSubscriber(loja, "consumidor01", "setor='Comercial'",false); // nome do consumidor do topico e condição para leitura da msg

        consumer.setMessageListener(message ->
        {
            TextMessage textMessage = (TextMessage) message;

            try {
                System.out.println("Consumindo mensagem " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).nextLine(); // para deixar a aplicação de pé

        session.close();
        connection.stop();
        context.close();
    }
}
