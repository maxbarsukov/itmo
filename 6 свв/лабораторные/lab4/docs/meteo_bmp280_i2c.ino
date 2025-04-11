#include <GyverBME280.h>
GyverBME280 bme;
void setup() {
  Serial.begin(9600);
  Serial.println("Start");
  
  // запуск датчика и проверка на работоспособность
  if (!bme.begin(0x76)) Serial.println("Error!");
}
void loop() {
  // температура
  Serial.print("Temperature: ");
  Serial.println(bme.readTemperature());
  // // влажность
  // Serial.print("Humidity: ");
  // Serial.println(bme.readHumidity());
  // давление
  Serial.print("Pressure: ");
  Serial.println(bme.readPressure());
  Serial.println();
  delay(1000);
}