package com.example.prodmessenger;

import org.springframework.boot.SpringApplication;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1 - Produtor");
            System.out.println("2 - Auditoria");
            System.out.println("3 - Sair");
            System.out.print("Digite a sua escolha: ");

            try {
                int escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha após o número

                switch (escolha) {
                    case 1:
                        System.out.println("Iniciando o Produtor...");
                        SpringApplication.run(ProdmessegerApplication.class, args); // Inicia o produtor
                        break;
                    case 2:
                        System.out.println("Iniciando Auditoria (opcional, se houver implementação futura)...");
                        // Aqui você pode implementar a lógica de auditoria, caso seja necessário
                        break;
                    case 3:
                        System.out.println("Saindo...");
                        System.exit(0); // Terminar a execução
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro de entrada. Por favor, digite um número válido.");
                scanner.nextLine(); // Limpar o scanner para evitar loop infinito
            }
        }
    }
}
