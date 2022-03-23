package frc.robot.snake;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

public class SnakeGame {
  /**
   * Get the pose in meters with a piece's position and direction to draw on the Field2d
   */
  public static Pose2d toPose(Coord coord, Direction dir) {
    return new Pose2d(
      new Translation2d(
        coord.x * Constants.BLOCK_SIZE, coord.y * Constants.BLOCK_SIZE),
      Rotation2d.fromDegrees(dir.angle));
  }
}
