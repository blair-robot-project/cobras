package frc.robot.snake;

/**
 * A Cartesian coordinate on the Snake game grid.
 */
public class Coord {
  /**
   * The x position, going from 0 (left) to whatever the field width is
   */
  public final int x;
  /**
   * The y position, going from 0 (bottom) to whatever the field height is
   */
  public final int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coord) {
      var other = (Coord) obj;
      return other.x == this.x && other.y == this.y;
    }
    return false;
  }
}
