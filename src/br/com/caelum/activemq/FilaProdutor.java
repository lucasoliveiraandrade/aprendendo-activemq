package br.com.caelum.activemq;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FilaProdutor {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = ((ConnectionFactory) context.lookup("ConnectionFactory")); // Convenção do JNDI. Ver jndi.properties.

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); // informa automaticamente a fila que a leitura da msg foi feita

        Destination filaFinanceiro = (Destination) context.lookup("financeiro"); // Convenção do JNDI. Ver jndi.properties.

        MessageProducer producer = session.createProducer(filaFinanceiro);

        for (int i = 0 ; i < 99 ; i++) {
            System.out.println("Inserindo msg " + i);
            Message message = session.createTextMessage("teste message " + i);
            producer.send(message);
        }

        session.close();
        connection.close();
        context.close();
    }
}
