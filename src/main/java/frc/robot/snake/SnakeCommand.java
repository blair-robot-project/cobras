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
import edu.wpi.first.wpilibj2.command.button.POVButton;

import static frc.robot.Constants.*;

/** An example command that uses an example subsystem. */
public class SnakeCommand extends CommandBase {
  private Piece head;
  private Piece tail;
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
    this.tail = new Piece(
        new Coord(FIELD_WIDTH / 2 - 1, FIELD_HEIGHT / 2),
        Direction.RIGHT);
    this.apple = newApple();
    this.currDir = head.dir;

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
        // Remove the last piece to give the illusion of motion
        var last = body.remove(body.size() - 1);
        // Make the tail follow the last piece
        if (body.isEmpty()) {
          this.tail = new Piece(last.pos, head.dir);
        } else {
          this.tail = new Piece(last.pos, body.get(body.size() - 1).dir);
        }
      }
    }
  }

  /**
   * Draw the poses for the snake head, body, and apple
   */
  private void redraw() {
    field.getObject(HEAD_NAME).setPose(head.getPose());
    field.getObject(TAIL_NAME).setPose(tail.getPose());
    field
        .getObject(APPLE_NAME)
        .setPose(new Piece(apple, Direction.RIGHT).getPose());

    var bodyPoses = new ArrayList<Pose2d>();
    var curvePoses = new ArrayList<Pose2d>();

    var prev = head;
    for (var piece : body) {
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

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
