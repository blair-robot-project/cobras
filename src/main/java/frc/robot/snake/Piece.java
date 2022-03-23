package frc.robot.snake;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

/**
 * A piece of the snake's body
 */
public class Piece {
  /**
   * The current position of the piece
   */
  public final Coord pos;
  /**
   * The direction it will move in next time
   */
  public final Direction dir;

  public Piece(Coord pos, Direction dir) {
    this.pos = pos;
    this.dir = dir;
  }

  /**
   * Get the pose corresponding to this piece's position and direction
   */
  public Pose2d getPose() {
    return Piece.getPose(pos, dir.angle);
  }

  /**
   * Get the pose corresponding to a piece's position and direction
   */
  public static Pose2d getPose(Coord pos, double angle) {
    return new Pose2d(
      new Translation2d(
        pos.x * Constants.BLOCK_SIZE + Constants.BLOCK_SIZE / 2,
        pos.y * Constants.BLOCK_SIZE + Constants.BLOCK_SIZE / 2),
      Rotation2d.fromDegrees(angle));
  }

  /**
   * Move this piece in some direction
   */
  public Piece move(Direction dir) {
    int x = pos.x;
    int y = pos.y;
    switch (dir) {
      case RIGHT:
        return new Piece(new Coord(x + 1, y), dir);
      case UP:
        return new Piece(new Coord(x, y + 1), dir);
      case LEFT:
        return new Piece(new Coord(x - 1, y), dir);
      default:
        return new Piece(new Coord(x, y - 1), dir);
    }
  }
}
