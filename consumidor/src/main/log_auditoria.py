import pika as pk
import src.main.modules.general_functions as gf

# URL de conexão com CloudAMQP
url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
params = pk.URLParameters(url)

# Conexão com o servidor AMQP via CloudAMQP
connection = pk.BlockingConnection(params)
channel = connection.channel()

# Declarar o exchange do tipo 'topic' sem durabilidade (já existente no RabbitMQ)
channel.exchange_declare(exchange='channel_exchange', exchange_type='topic', durable=False)

# Declarar uma fila durável (persistente) para o log de auditoria
queue_name = 'auditoria_log'
channel.queue_declare(queue=queue_name, durable=True)

# Vincular a fila de auditoria ao exchange com a chave de roteamento '#' (captura todas as mensagens)
routing_key = '#'
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)

print(f"[STATUS_AUDITORIA] Log de auditoria aguardando todas as mensagens...")

channel.basic_consume(queue=queue_name,
                      on_message_callback=gf.callback_log,
                      auto_ack=False)

channel.start_consuming()
