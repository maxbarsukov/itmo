import time
import random
import json
import paho.mqtt.client as mqtt
import os

BROKER = os.getenv("MQTT_BROKER", "edge-broker")
PORT = int(os.getenv("MQTT_PORT", 1883))
TOPIC = os.getenv("MQTT_TOPIC", "factory/temp")

client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)

def connect():
    while True:
        try:
            client.connect(BROKER, PORT, 60)
            print(f"Connected to {BROKER}:{PORT}")
            return
        except Exception as e:
            print(f"Connection failed: {e}. Retrying in 5s...")
            time.sleep(5)

connect()

while True:
    temp = random.randint(20, 100)
    payload = json.dumps({"temp": temp, "ts": time.time()})
    client.publish(TOPIC, payload)
    print(f"Sent: {payload} to {TOPIC}")
    time.sleep(1)
