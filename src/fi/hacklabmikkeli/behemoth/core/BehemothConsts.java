package fi.hacklabmikkeli.behemoth.core;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class BehemothConsts {
  
  public static final NXTRegulatedMotor LEFT_MOTOR = Motor.B;
  public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.C;
  public static final NXTRegulatedMotor SNATCHER_MOTOR = Motor.A;
  public static final SensorPort SNATCHER_ENDSTOP_SENSOR = SensorPort.S1;
  public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S3;
  public static final int LIGHT_SENSOR_HIGH_VALUE = 340;
  public static final int LIGHT_SENSOR_LOW_VALUE = 224;
  
}
