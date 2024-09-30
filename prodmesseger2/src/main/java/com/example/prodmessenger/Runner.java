package com.example.prodmessenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    // Mapeamento de opções para as rotas
    private static final Map<String, String> rotas = new HashMap<>();

    static {
        // Rotas mapeadas
        rotas.put("1", "rota.choquei.#");
        rotas.put("2", "rota.musica.#");
        rotas.put("3", "rota.#.gustavolima");
        rotas.put("4", "rota.#.deolane");
        rotas.put("5", "rota.#.juliette");
        rotas.put("6", "rota.choquei.gustavolima");
    }

    public Runner(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Exibir as rotas disponíveis
        System.out.println("Selecione a rota:");
        System.out.println("1 - Choquei (Gustavo Lima, Deolane, Juliette)");
        System.out.println("2 - Musica (Gustavo Lima, Deolane, Juliette)");
        System.out.println("3 - Gustavo Lima");
        System.out.println("4 - Deolane");
        System.out.println("5 - Juliette");
        System.out.println("6 - Choquei Apenas de Gustavo Lima");
        System.out.print("Digite o número da rota: ");
        String escolhaRota = scanner.nextLine();

        // Pega a chave de roteamento da rota selecionada
        String routingKey = rotas.get(escolhaRota);
        if (routingKey == null) {
            System.out.println("Rota inválida. Encerrando...");
            context.close();
            return;
        }

        // Variável para armazenar o nome do influenciador, se necessário
        String nomeDoInfluenciador = null;

        // Para as rotas 3, 4 e 5, vamos capturar qualquer mensagem relacionada ao influenciador
        switch (escolhaRota) {
            case "3" -> {
                nomeDoInfluenciador = "gustavolima";
                routingKey = "rota.#." + nomeDoInfluenciador;  // Captura mensagens de Choquei ou Música para Gustavo Lima
            }
            case "4" -> {
                nomeDoInfluenciador = "deolane";
                routingKey = "rota.#." + nomeDoInfluenciador;  // Captura mensagens de Choquei ou Música para Deolane
            }
            case "5" -> {
                nomeDoInfluenciador = "juliette";
                routingKey = "rota.#." + nomeDoInfluenciador;  // Captura mensagens de Choquei ou Música para Juliette
            }
            case "1", "2" -> {
                System.out.print("Digite o nome do influenciador (Gustavo Lima, Deolane, Juliette): ");
                nomeDoInfluenciador = scanner.nextLine().toLowerCase();
                routingKey = routingKey.replace("#",  nomeDoInfluenciador);
            }
        }

        // Loop para enviar mensagens enquanto o usuário não digitar "sair"
        while (true) {
            System.out.println("Digite a mensagem ou 'sair' para encerrar:");
            String corpoDaMensagem = scanner.nextLine();

            if (corpoDaMensagem.equalsIgnoreCase("sair")) {
                break;
            }

            // Formatar a mensagem com data, hora e nome do influenciador
            String mensagemFormatada = formatarMensagem(nomeDoInfluenciador, corpoDaMensagem);

            // Enviar a mensagem com a configuração de persistência
            rabbitTemplate.convertAndSend(
                    ProdmessegerApplication.topicExchangeName,
                    routingKey,
                    mensagemFormatada,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        //messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT); // Persistente
                        return message;
                    }
            );

            System.out.println("Mensagem enviada: " + mensagemFormatada);
        }

        context.close();
    }

    // Metodo para formatar a mensagem com data/hora e influenciador
    private String formatarMensagem(String nomeDoInfluenciador, String corpoDaMensagem) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dataHora = agora.format(formatter);
        return String.format("[%s] %s : %s", dataHora, nomeDoInfluenciador, corpoDaMensagem);
    }
}
