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

        System.out.println("Inserindo msg 01");
        Message message1 = session.createTextMessage("teste message 01");
        message1.setStringProperty("setor",  "Estoque");
        producer.send(message1);

        System.out.println("Inserindo msg 02");
        Message message2 = session.createTextMessage("teste message 02");
        message2.setStringProperty("setor",  "Financeiro");
        producer.send(message2);

        System.out.println("Inserindo msg 03");
        Message message3 = session.createTextMessage("teste message 03");
        message3.setStringProperty("setor",  "Comercial");
        producer.send(message3);

        session.close();
        connection.close();
        context.close();
    }
}
