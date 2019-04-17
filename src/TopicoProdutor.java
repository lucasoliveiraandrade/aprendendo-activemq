import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TopicoProdutor {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = ((ConnectionFactory) context.lookup("ConnectionFactory")); // Convenção do JNDI. Ver jndi.properties.

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // informa automaticamente o topico que a leitura da msg foi feita

        Topic topico = (Topic) context.lookup("loja"); // Convenção do JNDI. Ver jndi.properties.

        MessageProducer producer = session.createProducer(topico);

        for (int i = 0 ; i < 20 ; i++) {
            System.out.println("Inserindo msg " + i);
            Message message = session.createTextMessage("teste message " + i);
            producer.send(message);
        }

        session.close();
        connection.close();
        context.close();
    }
}
