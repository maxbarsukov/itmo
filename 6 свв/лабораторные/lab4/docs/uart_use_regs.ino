#define SetBit(reg, bita) reg |= (1<<bita)

volatile char b = 0;

void setup() {
  uint16_t baudRate = 9600;
  uint16_t ubrr = 16000000 / 16 / baudRate - 1;

  UBRR0H = (unsigned char) (ubrr >> 8);
  UBRR0L = (unsigned char) ubrr;

  SetBit(UCSR0B, TXEN0);
  SetBit(UCSR0B, RXEN0);
  SetBit(UCSR0B, RXCIE0);

  SetBit(UCSR0C, 1);
  SetBit(UCSR0C, 2);

  pinMode(13, OUTPUT);
}

ISR(USART_RX_vect) {
  b = UDR0;
  
  if(b == 'A') digitalWrite(13, HIGH);
  if(b == 'B') digitalWrite(13, LOW);

  while(!(UCSR0A & (1<<UDRE0)));

  UDR0 = b;
}

void loop() {

}
