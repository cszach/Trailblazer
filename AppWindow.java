import javax.swing.JFrame;

/** The main application's GUI frame */
public class AppWindow extends JFrame {
  /**
   * Initializes the application's window frame.
   *
   * @param width the (initial) width of the frame.
   * @param width the (initial) height of the frame.
   */
  public AppWindow(int width, int height) {
    super();

    this.setSize(width, height);
  }

  /** Makes the window frame visible. */
  public void display() {
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}
