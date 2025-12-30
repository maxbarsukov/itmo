import paho.mqtt.client as mqtt
import os
import json

BROKER = os.getenv("MQTT_BROKER", "cloud-broker")
PORT = int(os.getenv("MQTT_PORT", 1883))
TOPIC = os.getenv("MQTT_TOPIC", "cloud/alerts")

def on_connect(client, userdata, flags, reason_code, properties):
    print(f"Connected to Cloud Broker with result code {reason_code}")
    client.subscribe(TOPIC)

def on_message(client, userdata, msg):
    try:
        data = json.loads(msg.payload.decode())
        print(f"ALERT RECEIVED: Temp={data.get('temp')}°C at {data.get('ts')}")
    except:
        print(f"Received raw message: {msg.payload.decode()}")

client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
client.on_connect = on_connect
client.on_message = on_message

print(f"Connecting to {BROKER}...")
client.connect(BROKER, PORT, 60)
client.loop_forever()
