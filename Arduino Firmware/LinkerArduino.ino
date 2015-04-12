/* Project6 - DC Motor
 */

int motorpin1=2;  //right motor direction pin  
int motorpin2=3;  //right motor direction pin 

int motorpin3=7;  //left motor direction pin
int motorpin4=8;  //left motor direction pin

int RightM=6;     //pwm pin for right motor
int LeftM=5;     //pwm pin for left motor

int stb=4;        //standby pin (high)
int power=0;

boolean ecoMode=false;



void setup()
{

  pinMode( motorpin1, OUTPUT );
  pinMode( motorpin2, OUTPUT);
  pinMode( motorpin3, OUTPUT );
  pinMode( motorpin4, OUTPUT);
  pinMode( RightM, OUTPUT);
  pinMode( LeftM, OUTPUT);
  pinMode( stb, OUTPUT);
  digitalWrite(stb, HIGH);  //sets standby pin to HIGH
  Serial.begin(9600);
}
void loop()
{


  if(Serial.available()){
    int command= Serial.read();

    switch (command){

    case 0:
      //stop
      power=0;
      break;

    case 1:
    power=255;
      //right turn
      digitalWrite(motorpin1, HIGH);   //sets direction of motor
      digitalWrite(motorpin2, LOW);  //sets direction of motor

      digitalWrite(motorpin3, HIGH);   //sets direction of motor
      digitalWrite(motorpin4, LOW);  //sets direction of motor
      break;

    case 10:
    power=255;
      //backward motor
      digitalWrite(motorpin1, LOW);   //sets direction of motor
      digitalWrite(motorpin2, HIGH);  //sets direction of motor

      digitalWrite(motorpin3, LOW);   //sets direction of motor
      digitalWrite(motorpin4, HIGH);  //sets direction of motor
      break;

    case 4:
      power=255;
      //forward
      digitalWrite(motorpin1, HIGH);   //sets direction of motor
      digitalWrite(motorpin2, LOW);  //sets direction of motor

      digitalWrite(motorpin3, LOW);   //sets direction of motor
      digitalWrite(motorpin4, HIGH);  //sets direction of motor
      break;

    case 5:
      power=255;
      //left motor
      digitalWrite(motorpin1, LOW);   //sets direction of motor
      digitalWrite(motorpin2, HIGH);  //sets direction of motor

      digitalWrite(motorpin3, HIGH);   //sets direction of motor
      digitalWrite(motorpin4, LOW);  //sets direction of motor
      break;

    default:
      //zero out motor
      power=0;
      break;


    }
  }

  if(ecoMode && power!=0){
    power = 0; //set eco motor speed.
  }


  analogWrite(RightM,power);  //send power to right motor
  analogWrite(LeftM, power);  //send power to left motor

}








