package lugge.scripts.rs3.luckykebbit.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Paint {

    public int huntedKebbits;
    public int huntedKebbitsPerHour;
    public final Font MAIN_FONT = new Font("Arial", 1, 12);

    public Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public void drawMouse(Graphics2D g, Point p) {
        int x = (int) p.getX();
        int y = (int) p.getY();

        g.fillOval(x - 10, y, 25, 4);
        g.fillOval(x, y - 10, 4, 25);

        //g.drawLine(x, y - 10, x, y + 10);
        //g.drawLine(x - 10, y, x + 10, y);
    }
}
