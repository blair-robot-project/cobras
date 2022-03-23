package frc.robot.snake;

import java.util.Optional;

/**
 * A direction that the snake can go in
 */
public enum Direction {
  RIGHT(0),
  UP(90),
  LEFT(180),
  DOWN(270);

  /**
   * The angle that the snake's head really goes in, in degrees
   */
  public final double angle;

  private Direction(double angle) {
    this.angle = angle;
  }

  /**
   * Try to get a Direction from an angle in degrees
   * 
   * @param angle An angle that's one of [0, 90, 180, 270] if the result is to be
   *              nonempty
   * @return A direction corresponding to the given angle wrapped in an
   *         {@code Optional}, or {@code Optional.empty()} if the angle's invalid
   */
  public static Optional<Direction> fromAngle(int angle) {
    switch (angle) {
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

  /**
   * Whether this direction is opposite the other
   */
  public boolean opposite(Direction other) {
    switch (other) {
      case LEFT:
        return other == RIGHT;
      case RIGHT:
        return other == LEFT;
      case UP:
        return other == DOWN;
      default:
        return other == UP;
    }
  }
}
