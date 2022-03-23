// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Constants.*;

/** An example command that uses an example subsystem. */
public class SnakeCommand extends CommandBase {
  private Piece head;
  private Direction currDir;
  private Coord apple;
  private double lastTime = Double.NaN;
  /**
   * The pieces representing the snake's body
   */
  private final List<Piece> body = new ArrayList<>();
  private final Field2d field;
  private final GenericHID joystick;

  /**
   * @param field The field object on which to display the game
   */
  public SnakeCommand(Field2d field, GenericHID joystick) {
    this.field = field;
    this.joystick = joystick;
  }

  @Override
  public void initialize() {
    this.head = new Piece(
        new Coord(FIELD_WIDTH / 2, FIELD_HEIGHT / 2),
        Direction.RIGHT);
    this.apple = newApple();
    currDir = head.dir;

    // Get that pesky robot out of sight
    field.setRobotPose(new Pose2d(new Translation2d(-9, -9), new Rotation2d()));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    var currTime = Timer.getFPGATimestamp();
    var newDir = povToDir(joystick.getPOV());
    currDir = newDir.filter(dir -> !dir.opposite(currDir)).orElse(currDir);

    this.redraw();
    
    // Update elements after a pause
    if (Double.isNaN(lastTime) || currTime - lastTime > PAUSE) {
      this.lastTime = currTime;

      body.add(0, head);
      
      this.head = head.move(currDir);

      if (apple.equals(head.pos)) {
        this.apple = newApple();
      } else {
        body.remove(body.size() - 1);
      }
    }
  }

  /**
   * Draw the poses for the snake head, body, and apple
   */
  private void redraw() {
    field.getObject(HEAD_NAME).setPose(head.getPose());
    field
      .getObject(APPLE_NAME)
      .setPose(SnakeGame.toPose(apple, Direction.RIGHT));
    field
        .getObject(BODY_NAME)
        .setPoses(
            body.stream()
                .map(Piece::getPose)
                .collect(Collectors.toList()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  /**
   * Generate a random apple that isn't on the snake
   */
  private Coord newApple() {
    int x = (int) (Math.random() * FIELD_WIDTH);
    int y = (int) (Math.random() * FIELD_HEIGHT);
    var coord = new Coord(x, y);
    if (coord.equals(head.pos)) {
      return newApple();
    }
    for (var piece : body) {
      if (coord.equals(piece.pos)) {
        return newApple();
      }
    }
    return coord;
  }

  /**
   * Convert a POV to a direction for the head to move in
   * 
   * @param pov One of 0, 90, 180, 270 or -1
   * @return The Direction wrapped in an Optional (empty if POV is -1)
   */
  private Optional<Direction> povToDir(int pov) {
    switch (pov) {
      case 0:
        return Optional.of(Direction.UP);
      case 90:
        return Optional.of(Direction.RIGHT);
      case 180:
        return Optional.of(Direction.DOWN);
      case 270:
        return Optional.of(Direction.LEFT);
      default:
        return Optional.empty();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
