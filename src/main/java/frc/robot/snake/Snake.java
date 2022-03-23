package frc.robot.snake;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Snake extends SubsystemBase {
  /**
   * The piece at the front representing the head
   */
  private Piece head;
  /**
   * The piece at the end representing the tail
   */
  private Piece tail;
  /**
   * The pieces representing the snake's body
   */
  private final List<Piece> body = new ArrayList<>();

  /**
   * The current direction to move in
   */
  private Direction dir;

  public Snake(Piece head) {
    this.head = head;
    this.tail = new Piece(new Coord(head.pos.x - 1, head.pos.y), head.dir);
    this.dir = head.dir;
  }

  public Direction getDir() {
    return this.dir;
  }

  public void setDir(Direction dir) {
    this.dir = dir;
  }

  public Piece getHead() {
    return head;
  }

  public Piece getTail() {
    return tail;
  }

  public List<Piece> getBody() {
    return body;
  }

  /**
   * Whether the head will be in position {@code pos} the next tick
   */
  public boolean willCollide(Coord pos) {
    return pos.equals(head.move(this.dir).pos);
  }

  /**
   * Move the snake one step
   * @param ateApple If true, size will grow by one
   */
  public void move(boolean ateApple) {
    body.add(0, head);

    this.head = head.move(this.dir);

    if (!ateApple) {
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
