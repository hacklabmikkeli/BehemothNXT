package fi.hacklabmikkeli.behemoth.io;

import java.io.DataInputStream;
import java.io.IOException;

import fi.hacklabmikkeli.behemoth.core.BehemothConsts;
import fi.hacklabmikkeli.behemoth.movement.MovementController;
import fi.hacklabmikkeli.behemoth.movement.MovementDirection;
import fi.hacklabmikkeli.behemoth.movement.MovementMode;
import fi.hacklabmikkeli.behemoth.snatcher.SnatcherController;
import fi.hacklabmikkeli.behemoth.utils.BehemothUtils;
import lejos.nxt.Button;

public class CommunicationsThread extends Thread {

  public CommunicationsThread(MovementController movementController, SnatcherController snatcherController, DataInputStream dataInputStream) {
    this.movementController = movementController;
    this.dataInputStream = dataInputStream;
    this.snatcherController = snatcherController;
  }

  @Override
  public void run() {
    while (!Button.ESCAPE.isPressed()) {
      try {
        if (dataInputStream.available() > 0) {
          int command = dataInputStream.readInt();
          if (command <= 1000 && movementController.getMode().equals(MovementMode.NORMAL)) {
            if (command < 500) {
              movementController.setRightMotorDirectionAndSpeed(MovementDirection.BACKWARD,
                  BehemothUtils.resolveMotorSpeed(0, command));
            } else {
              movementController.setRightMotorDirectionAndSpeed(MovementDirection.FORWARD,
                  BehemothUtils.resolveMotorSpeed(0, command));
            }
          } else if (command > 1000 && command <= 2000 && movementController.getMode().equals(MovementMode.NORMAL)) {
            if (command < 1500) {
              movementController.setLeftMotorDirectionAndSpeed(MovementDirection.BACKWARD,
                  BehemothUtils.resolveMotorSpeed(1000, command));
            } else {
              movementController.setLeftMotorDirectionAndSpeed(MovementDirection.FORWARD,
                  BehemothUtils.resolveMotorSpeed(1000, command));
            }
          } else if (command > 2000 && command <= 3000 && movementController.getMode().equals(MovementMode.NORMAL)) {
            if (command < 2500) {
              snatcherController.moveDown(200, 50);
            } else {
              snatcherController.moveUp(200, 50);
            }
          } else if (command > 3000 && command <= 4000) {
            if (movementController.getMode().equals(MovementMode.NORMAL)) {
              movementController.calibrateLightSensor(BehemothConsts.LIGHT_SENSOR_LOW_VALUE, BehemothConsts.LIGHT_SENSOR_HIGH_VALUE);
              movementController.startLineFollowing();
              movementController.setSpeed(300);
            } else {
              movementController.stopLineFollowing();
            }
          } else if (command > 4000 && command <= 5000) {
            movementController.calibrateBlack();
          } else if (command > 5000) {
            movementController.calibrateWhite();
          } else {
            System.out.println("Unknown command");
            byte[] flushbytes = new byte[1024];
            dataInputStream.readFully(flushbytes);
          }
        }
      } catch (IOException e) {
        System.out.println("Error communicating with controller");
      }
    }

  }

  private SnatcherController snatcherController;
  private MovementController movementController;
  private DataInputStream dataInputStream;

}
