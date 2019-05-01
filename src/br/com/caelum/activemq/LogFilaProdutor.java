package br.com.caelum.activemq;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LogFilaProdutor {

    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext context = new InitialContext();
        ConnectionFactory connectionFactory = ((ConnectionFactory) context.lookup("ConnectionFactory"));

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination filaLog = (Destination) context.lookup("log");

        MessageProducer producer = session.createProducer(filaLog);

        System.out.println("Inserindo msg INFO");
        Message message = session.createTextMessage("INFO | LOG Message");
        producer.send(message, DeliveryMode.NON_PERSISTENT, 4, 5000);

        System.out.println("Inserindo msg WARNING");
        message = session.createTextMessage("WARNING | LOG Message");
        producer.send(message, DeliveryMode.NON_PERSISTENT, 7, 5000);

        System.out.println("Inserindo msg DEBUG");
        message = session.createTextMessage("DEBUG | LOG Message");
        producer.send(message, DeliveryMode.NON_PERSISTENT, 0, 5000);

        System.out.println("Inserindo msg ERROR");
        message = session.createTextMessage("ERROR | LOG Message");
        producer.send(message, DeliveryMode.NON_PERSISTENT, 9, 5000);

        // Lembrete: para o ActiveMQ trabalhar com as prioridades das mensagens é preciso fazer a seguinte configuração:
        // na pasta 'conf' do ActiveMQ, editar o arquivo activemq.xml adicionando a tag   em policyEntry
        // https://activemq.apache.org/how-can-i-support-priority-queues.html

        session.close();
        connection.close();
        context.close();
    }
}
