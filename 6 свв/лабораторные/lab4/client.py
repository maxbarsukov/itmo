import serial
import crc8
import time
import threading

# Настройка по варианту
ser = serial.Serial("/dev/cu.usbserial-110", 115200, parity=serial.PARITY_ODD, stopbits=serial.STOPBITS_TWO)
hash = crc8.crc8()

# Функция для получения хеш-суммы
def tocrc(data):
    return hash.reset().update(data).digest()

# Функция для отправки пакета в контроллер
def sendPacket(data):
    ser.write(b'\x5A')
    ser.write(len(data).to_bytes(1, "little"))
    ser.write(data)
    ser.write(tocrc(data))

# Эта функция периодически отправляет пакет в контроллер.
# Строка после функции как раз ответственен за её активацию
def interval():
    while True:
        sendPacket(b'TEST')
        time.sleep(5)

threading.Thread(target=interval).start()

# Бесконечный цикл с валидацией и обработкой принятых данных от контроллера
while True:
    bs = ser.read()
    if (bs != b'\x5A'):
        continue
    n = ser.read()
    n = int.from_bytes(n, "little")
    data = ser.read(n)
    crc = ser.read()
    if tocrc(data) != crc:
        print("Error in crc8")
        continue
    
    temp = int.from_bytes(data[0:2], "little")
    hum = int.from_bytes(data[2:4], "little")
    print(f'Данные: {data}\nТемпература: {temp}°C; Влажность: {hum}%')
