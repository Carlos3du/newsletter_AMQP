import pika as pk
import src.main.modules.general_functions as gf


# Conexão com o servidor AMQP (RabbitMQ, por exemplo)
connection = pk.BlockingConnection(pk.ConnectionParameters('localhost'))
channel = connection.channel()

# Declarar o exchange do tipo 'topic'
channel.exchange_declare(exchange='channel_exchange', exchange_type='topic')

# Criar uma fila exclusiva para o consumidor
result = channel.queue_declare('', exclusive=True)
queue_name = result.method.queue

# Exibir o menu para escolher a rota
routing_key = gf.menu

# Binding da fila com o exchange e a rota escolhida
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)

print(f"[STATUS_CONSUMIDOR] Consumidor aguardando mensagens da rota: {routing_key}")



# Consumidor se inscreve na fila e escuta as mensagens
channel.basic_consume(queue=queue_name,
                      on_message_callback=gf.callback,
                      auto_ack=True)

print('[STATUS-CONSUMIDOR] Aguardando mensagens...')
channel.start_consuming()
