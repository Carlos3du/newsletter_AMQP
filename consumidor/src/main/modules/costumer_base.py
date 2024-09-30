import pika as pk
import src.main.modules.interface_functions as interface


def consumidor():
    url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
    params = pk.URLParameters(url)

    connection = pk.BlockingConnection(params)
    channel = connection.channel()

    channel.exchange_declare(exchange='channel_exchange', exchange_type='topic')

    result = channel.queue_declare('', exclusive=True)
    queue_name = result.method.queue

    # Agora ela vai escolher:
    while True:
        escolha = interface.menu()

        # Validação:
        if escolha != 0:
            routing_key = escolha
            break
        else:
            print("Escolha inválida. Tente novamente.")



    channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)


    print(f'[STATUS_CONSUMIDOR] Bem vindo ao canal {routing_key}, aguardando mensagens...')
    channel.basic_consume(queue=queue_name,
                          on_message_callback=interface.callback,
                          auto_ack=True)
    channel.start_consuming()
