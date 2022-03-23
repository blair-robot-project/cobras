// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.snake;

import java.util.ArrayList;
import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Constants.*;

/** An example command that uses an example subsystem. */
public class SnakeCommand extends CommandBase {
  /**
   * The Snake "subsystem" on which to run the game
   */
  private final Snake snake;
  private Coord apple;
  private double lastTime = Double.NaN;
  private final Field2d field;
  private final GenericHID joystick;

  /**
   * @param field The field object on which to display the game
   */
  public SnakeCommand(Snake snake, Field2d field, GenericHID joystick) {
    this.field = field;
    this.joystick = joystick;
    this.snake = snake;
    addRequirements(snake);
  }

  @Override
  public void initialize() {
    this.apple = newApple();

    // Get that pesky robot and message out of sight
    field.setRobotPose(new Pose2d(new Translation2d(-9, -9), new Rotation2d()));
    field
      .getObject(GAME_OVER_NAME)
      .setPose(new Pose2d(new Translation2d(-9, -9), new Rotation2d()));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    var currTime = Timer.getFPGATimestamp();
    var newDir = povToDir(joystick.getPOV());
    newDir.filter(dir -> !dir.opposite(snake.getDir())).ifPresent(snake::setDir);

    this.redraw();

    // Update elements after a pause
    if (Double.isNaN(lastTime) || currTime - lastTime > PAUSE) {
      this.lastTime = currTime;

      var ateApple = snake.willCollide(apple);
      snake.move(ateApple);
      if (ateApple) {
        this.apple = newApple();
      }
    }
  }

  /**
   * Draw the poses for the snake head, body, and apple
   */
  private void redraw() {
    field.getObject(HEAD_NAME).setPose(snake.getHead().getPose());
    field.getObject(TAIL_NAME).setPose(snake.getTail().getPose());
    field
        .getObject(APPLE_NAME)
        .setPose(new Piece(apple, Direction.RIGHT).getPose());

    var bodyPoses = new ArrayList<Pose2d>();
    var curvePoses = new ArrayList<Pose2d>();

    var prev = snake.getHead();
    for (var piece : snake.getBody()) {
      bodyPoses.add(piece.getPose());
      if (prev.dir != piece.dir) {
        // Draw a curvy part to make it look smoother
        double prevAngle = prev.dir.angle;
        double newAngle = piece.dir.angle;
        double curveAngle;
        if (prevAngle == 0 && newAngle == 270 || prevAngle == 90 && newAngle == 180) {
          curveAngle = 0;
        } else if (prevAngle == 90 && newAngle == 0 || prevAngle == 180 && newAngle == 270) {
          curveAngle = 90;
        } else if (prevAngle == 0 && newAngle == 90 || prevAngle == 270 && newAngle == 180) {
          curveAngle = 270;
        } else {
          curveAngle = 180;
        }
        curvePoses.add(Piece.getPose(piece.pos, curveAngle));
      }
      prev = piece;
    }
    field.getObject(CURVE_NAME).setPoses(curvePoses);
    field.getObject(BODY_NAME).setPoses(bodyPoses);
  }

  @Override
  public void end(boolean interrupted) {
    if (!interrupted) {
      field.getObject(GAME_OVER_NAME).setPose(CENTER.getPose());
      this.redraw();
    }
  }

  /**
   * Generate a random apple that isn't on the snake
   */
  private Coord newApple() {
    int x = (int) (Math.random() * FIELD_WIDTH);
    int y = (int) (Math.random() * FIELD_HEIGHT);
    var coord = new Coord(x, y);
    if (coord.equals(snake.getHead().pos)) {
      return newApple();
    }
    for (var piece : snake.getBody()) {
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
        return Optional.of(Direction.RIGHT);
      case 90:
        return Optional.of(Direction.UP);
      case 180:
        return Optional.of(Direction.LEFT);
      case 270:
        return Optional.of(Direction.DOWN);
      default:
        return Optional.empty();
    }
  }

  @Override
  public boolean isFinished() {
    var movedHead = snake.getHead().move(snake.getDir());
    if (movedHead.pos.x < 0
        || movedHead.pos.y < 0
        || movedHead.pos.x > FIELD_WIDTH
        || movedHead.pos.y > FIELD_HEIGHT) {
      System.out.println("Hit edge of field (pos = " + movedHead.pos + ")");
      return true;
    }

    if (snake.willCollide(snake.getTail().pos)) {
      System.out.println("Hit tail");
      return true;
    }
    for (var piece : snake.getBody()) {
      if (snake.willCollide(piece.pos)) {
        System.out.println("Hit body part (" + piece.pos + ")");
        return true;
      }
    }

    return false;
  }
}
