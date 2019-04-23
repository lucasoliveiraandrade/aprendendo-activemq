package br.com.caelum.activemq;

import br.com.caelum.activemq.modelo.Pedido;
import br.com.caelum.activemq.modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;

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
        Message message1 = session.createTextMessage(GeraXML());    // seria possivel usar o createObjectMessage e passar o objeto Pedido direto
        message1.setStringProperty("setor",  "Estoque");
        producer.send(message1);

        System.out.println("Inserindo msg 02");
        Message message2 = session.createTextMessage(GeraXML());
        message2.setStringProperty("setor",  "Financeiro");
        producer.send(message2);

        System.out.println("Inserindo msg 03");
        Message message3 = session.createTextMessage(GeraXML());
        message3.setStringProperty("setor",  "Comercial");
        producer.send(message3);

        // lembrete: Quando o ActiveMQ não consegue entregar a mensagem ao consumidor por padrão ele faz 6 tentativas.
        //           Se mesmo assim ele não conseguir, ele manda a mensagem para uma fila (e não tópico) chamada DLQ - Dead Letter Queue

        session.close();
        connection.close();
        context.close();
    }

    private static String GeraXML() {   // o ideal seria json
        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        StringWriter writer = new StringWriter();
        JAXB.marshal(pedido, writer);
        return writer.toString();
    }
}
