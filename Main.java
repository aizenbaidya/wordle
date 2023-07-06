import java.awt.Font;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        final int WIDTH = 416;
        final int HEIGHT = 488;
        StdDraw.setTitle("Wordle");
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(HEIGHT, 0);
        StdDraw.enableDoubleBuffering();
        StdDraw.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        Wordle wordle = new Wordle();
        while (true) {
            if (wordle.isGameOver()) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                    wordle = new Wordle();
                    // Clear keys typed buffer
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.nextKeyTyped();
                    }
                }
            } else {
                StdDraw.clear(StdDraw.BLACK);
                wordle.update();
                wordle.draw();
                StdDraw.show();
                StdDraw.pause(17);
            }
        }
    }
}
