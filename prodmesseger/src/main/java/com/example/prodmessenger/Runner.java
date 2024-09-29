package com.example.prodmessenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    public Runner(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Perguntar o nome do influenciador uma vez no início
        System.out.print("Digite o nome do influenciador: ");
        String nomeDoInfluenciador = scanner.nextLine();

        while (true) {
            System.out.println("Digite a mensagem ou 'sair' para encerrar:");
            String corpoDaMensagem = scanner.nextLine();

            if (corpoDaMensagem.equalsIgnoreCase("sair")) {
                break;
            }

            // Formatar a mensagem com a data e hora atuais, o nome do influenciador e o corpo da mensagem
            String mensagemFormatada = formatarMensagem(nomeDoInfluenciador, corpoDaMensagem);

            // Envia a mensagem para o exchange e a rota especificada
            rabbitTemplate.convertAndSend(ProdmessegerApplication.topicExchangeName, ProdmessegerApplication.routingKey, mensagemFormatada);

            System.out.println("Mensagem enviada: " + mensagemFormatada);
        }

        // Fecha o contexto do Spring quando o loop terminar
        context.close();
    }

    // Metodo para formatar a mensagem no padrão desejado
    private String formatarMensagem(String nomeDoInfluenciador, String corpoDaMensagem) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dataHora = agora.format(formatter);
        return String.format("[%s] %s : %s", dataHora, nomeDoInfluenciador, corpoDaMensagem);
    }
}
