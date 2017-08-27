package fi.hacklabmikkeli.behemoth.snatcher;

import lejos.nxt.NXTRegulatedMotor;

public class SnatcherController {

  public SnatcherController (NXTRegulatedMotor motor) {
    this.motor = motor;
  }
  
  public void moveUp(int speed, int amount) {
    if (currentValue + amount > maxValue) {
      amount = maxValue - currentValue;
      currentValue = maxValue;
    }
    
    this.motor.setSpeed(speed);
    this.motor.rotate(amount);
  }
  
  public void moveDown(int speed, int amount) {
    currentValue -= amount;
    if (currentValue < 0) {
      amount = -currentValue;
      currentValue = 0;
    }
    
    this.motor.setSpeed(speed);
    this.motor.rotate(-amount);
  }
  
  public NXTRegulatedMotor getMotor() {
    return motor;
  }

  public void setMotor(NXTRegulatedMotor motor) {
    this.motor = motor;
  }   
  
  private int currentValue = 0;
  private int maxValue = 450;
  private NXTRegulatedMotor motor;
  
}
