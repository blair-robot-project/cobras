// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final double BLOCK_SIZE = 0.7;

  public static final int FIELD_WIDTH = 20;
  public static final int FIELD_HEIGHT = 10;

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
   * The name for the apple pose in Field2d
   */
  public static final String APPLE_NAME = "Apple";
}
