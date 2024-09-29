def menu():
    print("Opções de canais para se conectar:\n"
          "1. Geral Choquei\n"
          "2. Geral Musica\n"
          "3. Tudo Sobre Gustavo Lima\n"
          "4. Tudo Sobre Deolane\n"
          "5. Tudo Sobre Juliette\n"
          "6. Fofocas de Gustavo Lima")


    choice = input("Digite a opção desejada para começar: ")

    routes = {
        '1': 'rota.choquei.#',
        '2': 'rota.musica.#',
        '3': 'rota.*.gustavolima',
        '4': 'rota.*.deolane',
        '5': 'rota.*.juliette',
        '6': 'rota.choquei.gustavolima'
    }
    return routes.get(choice, 'choquei.#')  # Padrão 'choquei.#' caso escolha inválida

# Função callback para processar as mensagens recebidas
def callback(ch, method, properties, body):
    print(f"[RETORNO_PRODUTOR]  {method.routing_key}: {body.decode()}")
