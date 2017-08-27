package fi.hacklabmikkeli.behemoth.movement;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class MovementController {

  public MovementController(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, SensorPort lightSensorPort) {
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
    this.mode = MovementMode.NORMAL;
    this.lightSensor = new LightSensor(lightSensorPort);
    this.lightSensorCalibrated = false;
  }
  
  public void startLineFollowing() {
    this.mode = MovementMode.LINE_FOLLOW;
    if (!lightSensorCalibrated) {
      this.calibrateLightSensor();
    }
  }
  
  public void stopLineFollowing() {
    this.mode = MovementMode.NORMAL;
    this.setSpeed(0);
    this.leftMotor.stop();
    this.rightMotor.stop();
  }
  
  public void lineFollowProgress() {
    if (!this.mode.equals(MovementMode.LINE_FOLLOW)) {
      return;
    }
    
    if (this.lightSensor.getLightValue() > 50) { // Found white
      this.whiteCount++;
      this.lineFollowLeft();
    } else {
      this.whiteCount = 0;
      this.lineFollowRight();
    }
    
    if (this.whiteCount > 50) {
      
      for (int i = 0; i < 15; i++) {
        if (this.isBlack(MovementDirection.LEFT)) {
          this.whiteCount = 0;
          return;
        }
      }
      
      for (int i = 0; i < 30; i++) {
        if (this.isBlack(MovementDirection.RIGHT)) {
          this.whiteCount = 0;
          return;
        }
      }
      
      for (int i = 0; i < 15; i++) {
        if (this.isBlack(MovementDirection.LEFT)) {
          this.whiteCount = 0;
          return;
        }
      }
      
      this.leftMotor.setSpeed(100);
      this.rightMotor.setSpeed(100);
      this.leftMotor.rotate(-180, true);
      this.rightMotor.rotate(-180);
      
    }
    Delay.msDelay(10);
  }
  
  public void setLeftMotorDirectionAndSpeed(MovementDirection direction, int speed) {
    if (speed < this.motorMinSpeed) {
      this.leftMotor.stop();
      return;
    }

    this.leftMotor.setSpeed(speed);
    
    if (direction.equals(MovementDirection.FORWARD)) {
      this.leftMotor.forward();
    } else {
      this.leftMotor.backward();
    }   
  }

  public void setRightMotorDirectionAndSpeed(MovementDirection direction, int speed) {
    if (speed < this.motorMinSpeed) {
      this.rightMotor.stop();
      return;
    }
    
    this.rightMotor.setSpeed(speed);
    
    if (direction.equals(MovementDirection.FORWARD)) {
      this.rightMotor.forward();
    } else {
      this.rightMotor.backward();
    }   
  }
  
  public void turnLeft() {
    if (!this.mode.equals(MovementMode.NORMAL)) {
      return;
    }
    
    this.rightMotor.forward();
    this.leftMotor.backward();
  }

  public void turnRight() {
    if (!this.mode.equals(MovementMode.NORMAL)) {
      return;
    }
    
    this.leftMotor.forward();
    this.rightMotor.backward();    
  }
  
  public void moveForward() {
    if (!this.mode.equals(MovementMode.NORMAL)) {
      return;
    }
    
    this.leftMotor.forward();
    this.rightMotor.forward();
  }
  
  public void stop() {
    if (!this.mode.equals(MovementMode.NORMAL)) {
      return;
    }
    
    this.rightMotor.stop();
    this.leftMotor.stop();
  }
  
  public void moveBackwards() {
    if (!this.mode.equals(MovementMode.NORMAL)) {
      return;
    }
    
    this.leftMotor.backward();
    this.rightMotor.backward();
  }
   
  public NXTRegulatedMotor getLeftMotor() {
    return leftMotor;
  }

  public void setLeftMotor(NXTRegulatedMotor leftMotor) {
    this.leftMotor = leftMotor;
  }

  public NXTRegulatedMotor getRightMotor() {
    return rightMotor;
  }

  public void setRightMotor(NXTRegulatedMotor rightMotor) {
    this.rightMotor = rightMotor;
  }
  
  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }
  
  public MovementMode getMode() {
    return mode;
  }

  public void calibrateLightSensor(int black, int white) {
    this.lightSensor.setHigh(white);
    this.lightSensor.setLow(black);
    this.lightSensorCalibrated = true;
  }
  
  public void calibrateBlack() {
    Delay.msDelay(1000);
    this.blackValue = this.lightSensor.readNormalizedValue();
    this.lightSensor.calibrateLow();
    System.out.println("Black: " + this.blackValue);
    this.lightSensorCalibrated = true;
    Sound.twoBeeps();
    Delay.msDelay(1000);
  }
  
  public void calibrateWhite() {
    Delay.msDelay(1000);
    this.whiteValue = this.lightSensor.readNormalizedValue();
    this.lightSensor.calibrateHigh();
    System.out.println("White: " + this.whiteValue);
    this.lightSensorCalibrated = true;
    Sound.beep();
    Delay.msDelay(1000);
  }
  
  public void calibrateLightSensor() {
    System.out.println("Calibrating light sensor");
    System.out.println("Place on white surface and press enter");
    
    Button.ENTER.waitForPressAndRelease();
    Delay.msDelay(1000);

    this.whiteValue = this.lightSensor.readNormalizedValue();
    this.lightSensor.calibrateHigh();
    Sound.beep();
    System.out.println("White: " + this.whiteValue);

    System.out.println("Place on black surface and press enter");
    Button.ENTER.waitForPressAndRelease();
    Delay.msDelay(1000);

    this.blackValue = this.lightSensor.readNormalizedValue();
    this.lightSensor.calibrateLow();
    Sound.twoBeeps();
    System.out.println("Black: " + this.blackValue);
    Button.ENTER.waitForPressAndRelease();
 }

  private boolean isBlack(MovementDirection direction) {

    if (direction.equals(MovementDirection.LEFT)) {
      this.rightMotor.rotate(20, true);
      this.leftMotor.rotate(-20);
    } else {
      this.rightMotor.rotate(-20, true);
      this.leftMotor.rotate(20); 
    }
    
    return this.lightSensor.getLightValue() < 50;
  }
  
  private void lineFollowLeft() {
    this.rightMotor.forward();
    this.leftMotor.forward();
    this.leftMotor.setSpeed(speed / 3);
    this.rightMotor.setSpeed(speed);
  }
  
  private void lineFollowRight() {
    this.rightMotor.forward();
    this.leftMotor.forward();
    this.leftMotor.setSpeed(speed);
    this.rightMotor.setSpeed(speed / 3);
  }
  
  private int motorMinSpeed = 100;
  private int whiteCount = 0;
  private int whiteValue;
  private int blackValue;
  private LightSensor lightSensor;
  private boolean lightSensorCalibrated;
  private MovementMode mode;
  private int speed;
  private NXTRegulatedMotor leftMotor;
  private NXTRegulatedMotor rightMotor;
} 

