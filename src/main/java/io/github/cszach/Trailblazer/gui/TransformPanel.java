package io.github.cszach.Trailblazer.gui;

import java.awt.Rectangle;
import java.awt.MouseInfo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TransformPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  private double translateX = 0;
  private double translateY = 0;
  private double scale = 1.0;
  private double rotate = 0.0;
  private Rectangle focus;

  // For mouse drags
  private int prevMouseX;
  private int prevMouseY;
  private boolean isRotating = false;

  public TransformPanel() {
    super();

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

  public void drag(int x, int y) {
    this.translateX += x;
    this.translateY += y;

    this.repaint();
  }

  public void rotate(double angle) {
    this.rotate += angle;

    this.repaint();
  }

  public void zoom(double factor, double targetX, double targetY) {
    if (this.scale * factor < 0) {
      return;
    }

    double prevScaleX = this.scale;
    double prevScaleY = this.scale;

    this.scale *= factor;

    double scaleQuotientX = this.scale / prevScaleX;
    double scaleQuotientY = this.scale / prevScaleY;

    this.translateX = scaleQuotientX * this.translateX + (1 - scaleQuotientX) * targetX;
    this.translateY = scaleQuotientY * this.translateY + (1 - scaleQuotientY) * targetY;
  }

  public void center() {
    double currentScale = this.scale;

    this.zoom(1.0 / currentScale, 0, 0);

    this.translateX = -this.focus.getX() + (this.getWidth() - this.focus.getWidth()) / 2.0;
    this.translateY = -this.focus.getY() + (this.getHeight() - this.focus.getHeight()) / 2.0;

    this.zoom(currentScale, this.getWidth() / 2.0, this.getHeight() / 2.0);

    this.repaint();
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

    if (!isRotating) {
      this.drag(x - prevMouseX, y - prevMouseY);
    } else {
      this.rotate((y - prevMouseY) * Math.PI / 500.0);
    }

    this.prevMouseX = x;
    this.prevMouseY = y;
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    int unitsToScroll = e.getUnitsToScroll();

    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
    Point panelLocation = this.getLocationOnScreen();

    double mouseX = mouseLocation.getX() - panelLocation.getX();
    double mouseY = mouseLocation.getY() - panelLocation.getY();

    this.zoom((this.scale + unitsToScroll / -10.0) / this.scale, mouseX, mouseY);

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

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.translate(this.translateX, this.translateY);
    g2d.scale(this.scale, this.scale);
    g2d.rotate(this.rotate, this.focus.getX() + this.focus.getWidth() / 2.0, this.focus.getY() + this.focus.getHeight() / 2.0);
  }
}
