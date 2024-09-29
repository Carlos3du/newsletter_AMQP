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

# Função callback para processar as mensagens recebidas
def callback(ch, method, properties, body):
    print(f" [AUDITORIA] Mensagem recebida com chave {method.routing_key}: {body.decode()}")
    ch.basic_ack(delivery_tag=method.delivery_tag)  # Confirmação manual de que a mensagem foi processada

# Consumidor se inscreve na fila e escuta todas as mensagens
channel.basic_consume(queue=queue_name,
                      on_message_callback=callback,
                      auto_ack=False)  # auto_ack=False para confirmar manualmente o processamento

print('[STATUS_AUDITORIA] Aguardando todas as mensagens para auditoria...')
channel.start_consuming()
