package br.com.caelum.activemq;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class FilaConsumidor {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = ((ConnectionFactory) context.lookup("ConnectionFactory")); // Convenção do JNDI. Ver jndi.properties.

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // informa automaticamente a fila que a leitura da msg foi feita

        Destination filaFinanceiro = (Destination) context.lookup("financeiro"); // Convenção do JNDI. Ver jndi.properties.

        MessageConsumer consumer = session.createConsumer(filaFinanceiro);

        consumer.setMessageListener(message ->
        {
            TextMessage textMessage = (TextMessage) message;

            try {
                System.out.println("Consumindo mensagem " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).nextLine();  // para deixar a aplicação de pé

        session.close();
        connection.stop();
        context.close();
    }
}
