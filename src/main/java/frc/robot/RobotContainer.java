// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.snake.SnakeCommand;

public class RobotContainer {
  private final Field2d field = new Field2d();

  private final Joystick joystick = new Joystick(0);

  private final SnakeCommand snakeCmd = new SnakeCommand(field, joystick);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();
    SmartDashboard.putData(field);
  }

  private void configureButtonBindings() {
  }

  public Command getTeleopCommand() {
    return snakeCmd;
  }
}
