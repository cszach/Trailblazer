import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TransformPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  private AffineTransform transform;
  private double currentRotation;
  private Rectangle focus;

  // For mouse drags
  private int prevMouseX;
  private int prevMouseY;
  private boolean isRotating = false;

  // For mouse scrolls
  private int totalUnitsScrolled = 0;

  public TransformPanel() {
    super();

    this.transform = new AffineTransform();
    this.currentRotation = 0.0;
    this.focus = new Rectangle();

    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    this.addMouseWheelListener(this);
    this.addKeyListener(this);

    this.setFocusable(true);
  }

  public Rectangle getFocus() {
    return this.focus;
  }

  public void setFocus(Rectangle focus) {
    this.focus = focus;
  }

  public AffineTransform getTransform() {
    return this.transform;
  }

  public void setTransform(AffineTransform transform) {
    this.transform = transform;
  }

  public void center() {
    // First, make sure translations are set to 0
    this.transform.translate(-this.transform.getTranslateX(), -this.transform.getTranslateY());
    // Then center
    this.transform.translate(-this.focus.getX() + (this.getWidth() - this.focus.getWidth() * this.transform.getScaleX()) / 2.0, -this.focus.getY() + (this.getHeight() - this.focus.getHeight() * this.transform.getScaleY()) / 2.0);
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    this.prevMouseX = e.getX();
    this.prevMouseY = e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    double rotationCenterX = this.focus.getX() + this.focus.getWidth() / 2.0;
    double rotationCenterY = this.focus.getY() + this.focus.getHeight() / 2.0;

    if (!isRotating) {
      // Since translation is relative, first rotate back to the original
      // rotation (no rotation), then translate, then rotate back to the
      // previous angle.
      this.transform.rotate(-currentRotation, rotationCenterX, rotationCenterY);
      this.transform.translate(x - prevMouseX, y - prevMouseY);
      this.transform.rotate(currentRotation, rotationCenterX, rotationCenterY);
    } else {
      double angle = (y - prevMouseY) * Math.PI / 500;
      this.transform.rotate(angle, rotationCenterX, rotationCenterY);
      this.currentRotation += angle;
    }

    this.repaint();

    this.prevMouseX = x;
    this.prevMouseY = y;
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int unitsToScroll = e.getUnitsToScroll();

    double currentScaleX = this.transform.getScaleX();
    double currentScaleY = this.transform.getScaleY();

    this.totalUnitsScrolled += unitsToScroll;

    double newScaleX = 1.0 + this.totalUnitsScrolled / -10.0;
    double newScaleY = 1.0 + this.totalUnitsScrolled / -10.0;

    // this.transform.translate(this.focus.getWidth() / -2.0, this.focus.getHeight() / -2.0);
    // this.transform.translate(scaleCenterX, scaleCenterX);

    this.repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      this.isRotating = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      this.isRotating = false;
    }
  }
}
