#include <DHT11.h>
#include "CRC8.h"
#include "CRC.h"
#define SetBit(reg, bita) reg |= (1<<bita)

volatile char b = 0;
typedef unsigned char uint8_t;
DHT11 dht11(2);
CRC8 crc;

void USART_Transmit(uint8_t data)
{
  /* Wait for empty transmit buffer */
  while (!(UCSR0A & (1<<UDRE0)));
  /* Put data into buffer, sends the data */
  UDR0 = data;
}

void USART_Transmit(uint8_t data[], int len) {
  for (int i=0; i<len; i++) {
    USART_Transmit(data[i]);
  }
}
// Отправка пакета UART клиенту. Содержит синхробайт, длину данных, сами данные и контрольная сумма
void USART_SendPacket(uint8_t data[], int len) {
  USART_Transmit(0x5A);
  USART_Transmit((uint8_t)len);
  USART_Transmit(data, len);
  uint8_t crc = calcCRC8(data, len);
  USART_Transmit(crc);
}
void setup() {
    // Initialize serial communication to allow debugging and data readout.
    // Using a baud rate of 9600 bps.
    dht11.setDelay(2000); // Set this to the desired delay. Default is 500ms.
    uint32_t baudRate = 115200;
    uint32_t ubrr = 16000000 / 16 / baudRate;
    UBRR0H = (unsigned char) (ubrr >> 8);
    UBRR0L = (unsigned char) ubrr;
    SetBit(UCSR0B, TXEN0);
    SetBit(UCSR0B, RXEN0);
    SetBit(UCSR0B, RXCIE0);
    // Настройка по условиям варианта
    SetBit(UCSR0C, 1);
    SetBit(UCSR0C, 2);
    SetBit(UCSR0C, 5);
    SetBit(UCSR0C, 4);
    SetBit(UCSR0C, 3);
    pinMode(13, OUTPUT);
}

uint8_t packet[256];
int state = 0, len = 0, pos = 0;
/*
ISR --- стандартный встроенный метод в C для вызова векторов прерывания в Atmega.
Здесь происходит обработка данных, принятые от клиента:
- 0: Ожидание начала передачи.
- 1: Ожидание получения длины пакета.
- 2: Получение данных пакета.
- 3: Проверка контрольной суммы, здесь сбрасывается
pos для принятия нового пакета.
*/
ISR(USART_RX_vect) {
  b = UDR0;
  if(b == 'A') digitalWrite(13, HIGH);
  if(b == 'B') digitalWrite(13, LOW);
  switch(state) {
    case 0:
      if (b == 0x5A) state = 1;
      break;
    case 1:
      len = b;
      state = 2;
      break;
    case 2:
      packet[pos++] = b;
      if (pos >= len) state = 3;
      break;
    case 3:
      uint8_t crc = calcCRC8(packet, len);
      if (b == crc) USART_SendPacket(packet, len);
      state = 0;
      pos = 0;
  }
}
void loop() {
    int temperature = 0;
    int humidity = 0;
    // Attempt to read the temperature and humidity values from the DHT11 sensor.
    int result = dht11.readTemperatureHumidity(temperature, humidity);
    // Check the results of the readings.
    // If the reading is successful, print the temperature and humidity values.
    // If there are errors, print the appropriate error messages.
    if (result == 0) {
      int res[2];
      res[0] = temperature;
      res[1] = humidity;
      // Отправка пакета клиенту вместо вывода в Serial Monitor.
      USART_SendPacket((uint8_t*)res, 4);
    }
}
