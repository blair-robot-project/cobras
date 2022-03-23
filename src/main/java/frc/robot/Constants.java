// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.snake.Piece;
import frc.robot.snake.Coord;
import frc.robot.snake.Direction;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final double BLOCK_SIZE = 0.7;

  public static final int FIELD_WIDTH = 20;
  public static final int FIELD_HEIGHT = 10;

  /**
   * The place in the center of the field, with a rotation of 0 degs
   */
  public static final Piece CENTER =
    new Piece(
      new Coord(FIELD_WIDTH / 2, FIELD_HEIGHT / 2),
      Direction.RIGHT);

  /**
   * How many seconds the game should pause before moving
   */
  public static final double PAUSE = 0.23;

  /**
   * The name for the head pose in Field2d
   */
  public static final String HEAD_NAME = "SnakeHead";
  /**
   * The name for the body poses in Field2d
   */
  public static final String BODY_NAME = "SnakeBody";
  /**
   * The name for the tail pose in Field2d
   */
  public static final String TAIL_NAME = "SnakeTail";
  /**
   * The name for the curve poses in Field2d
   */
  public static final String CURVE_NAME = "SnakeCurve";
  /**
   * The name for the apple in Field2d
   */
  public static final String APPLE_NAME = "Apple";
  /**
   * The name for the Game Over message in Field2d
   */
  public static final String GAME_OVER_NAME = "GameOver";
}
