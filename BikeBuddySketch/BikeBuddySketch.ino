/*
The sketch receives a Bluetooth Low Energy 4 connection,
then uses PWM on the RGB shield to approximate the color
selected.

This sketch is supposed to be used with the rfduinoColorWheel
iPhone application.
*/

/*
 Copyright (c) 2014 OpenSourceRF.com.  All right reserved.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

#include <RFduinoBLE.h>

// pin 2 on the RGB shield is the red led
int led1 = 2;
// pin 3 on the RGB shield is the green led
int led2 = 3;
// pin 4 on the RGB shield is the blue led
int led3 = 4;
// pin 6 z acceleration
int accZ = 6;

// debounce time (in ms)
int debounce_time = 10;

// maximum debounce timeout (in ms)
int debounce_timeout = 100;

void setup() {
  // setup the leds for output
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);  
  pinMode(led3, OUTPUT);

  // set the acceleration for input
  pinMode(accZ, INPUT);

  // this is the data we want to appear in the advertisement
  // (if the deviceName and advertisementData are too long to fix into the 31 byte
  // ble advertisement packet, then the advertisementData is truncated first down to
  // a single byte, then it will truncate the deviceName)
  RFduinoBLE.advertisementData = "BikeBuddy";
  
  // start the BLE stack
  RFduinoBLE.begin();
}

void loop() {
  // switch to lower power mode
  //RFduino_ULPDelay(INFINITE);

  int accBike = analogRead(accZ);

  //delay_until_button(HIGH);
  if (accBike > 180) {
    RFduinoBLE.send(1);
    delay (1000);
    //RFduinoBLE.send(0);
  }
  delay (200);
  //delay_until_button(LOW);
    
}

/*int debounce(int state)
{
  int start = millis();
  int debounce_start = start;
  
  while (millis() - start < debounce_timeout)
    if (digitalRead(accZ) == state)
    {
      if (millis() - debounce_start >= debounce_time)
        return 1;
    }
    else
      debounce_start = millis();

  return 0;
}

int delay_until_button(int accValue)
{
  // set button edge to wake up on
  if (accValue > 200)
    RFduino_pinWake(accZ, HIGH);
  else
    RFduino_pinWake(accZ, LOW);
    
  do
    // switch to lower power mode until a button edge wakes us up
    RFduino_ULPDelay(INFINITE);
  while (! debounce(accValue));
  
  // if multiple buttons were configured, this is how you would determine what woke you up
  if (RFduino_pinWoke(accZ))
  {
    // execute code here
    RFduino_resetPinWake(accZ);
  }
}*/

void RFduinoBLE_onConnect() {
  // the default starting color on the iPhone is white
  analogWrite(led1, 255);
  analogWrite(led2, 255);
  analogWrite(led3, 255);
  delay (500);
  analogWrite(led1, 255);
  analogWrite(led2, 0);
  analogWrite(led3, 0);
}

void RFduinoBLE_onDisconnect() {
  // turn all leds off on disconnect and stop pwm
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
}

void RFduinoBLE_onReceive(char *data, int len) {
  // each transmission should contain an RGB triple
  if (len >= 3)
  {
    // get the RGB values
    uint8_t r = data[0];
    uint8_t g = data[1];
    uint8_t b = data[2];

    // set PWM for each led
    analogWrite(led1, r);
    analogWrite(led2, g);
    analogWrite(led3, b);
  }
}

