#include "arm.h"
#include <Servo.h>

typedef struct {
  int Xup;
  int Zup;
  int Yup;
  int Xpress;
  int Zpress;
  int Ypress;
} keyStat;

int basePin = 11;
int shoulderPin = 10;
int elbowPin = 9;
int gripperPin = 6;
arm arm;
const byte N = 5;
keyStat keyStats[N];
int ch = 0;
int counter = 0;

void setup()
{
  keyStats[113] = { -10, 180, 50, -10, 180, 50}; //default
  keyStats[82] = { -70, 90, 110, -70, 110, 110};  //R
  keyStats[71] = { -10, 150, 97, -10, 110, 110}; //G
  keyStats[66] = { 50, 150, 130, 40, 120, 120}; //B
  
  keyStats[70] = { -10, 180, 50, -10, 180, 50}; //F
  

  Serial.begin(9600);
  arm.begin(basePin, shoulderPin, elbowPin, gripperPin);
  delay(2000);
}

void move_arm(keyStat key, int counter) {
  for(int i=0; i<counter; i++) {
    arm.gotoPoint(key.Xup, key.Zup, key.Yup);
    delay(200);
    arm.gotoPoint(key.Xpress, key.Zpress, key.Ypress);
    arm.gotoPoint(key.Xup, key.Zup, key.Yup);
    delay(200);
  }
}

void loop()
{
  if (Serial.available()>0)
  {
    delay(200);
    ch = Serial.read();

    counter = Serial.read() - 48;
    
    if(ch == 'F') 
    {
      for(int i = 0; i < counter; i++) 
      {
      	arm.gotoPoint(-100, 180, 50);
      	delay(100);	
      	arm.gotoPoint(100, 180, 50);
      	delay(100);
      }
    } 
    else 
    {
      move_arm( keyStats[ch], counter );
    }
    //delay(1000);
  }
  else {
    move_arm(keyStats[113], 1 );
    delay(1000);
  }

}




