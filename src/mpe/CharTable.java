package mpe;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class CharTable {

    private static int posX = 0,  posY = 0;
    private static int var;
    private static int TABLE_WIDTH;
    private static int TABLE_HEIGHT;
    private static final String tables = "_01234\"56789/BSIJD.ZCL-[;&$?*#:%&\\| ";

    public static void draw(Graphics g, Font f, int CanvasWidth, int CanvasHeight, int variant) {
        var = variant;
        TABLE_WIDTH = (byte)  6;
        TABLE_HEIGHT = (byte) 6;
        int cellW = f.stringWidth("W") + 8;
        int w = cellW * TABLE_WIDTH;
        int cellH = f.getHeight() + 2;
        int h = cellH * TABLE_HEIGHT;
        int widthOffset = (CanvasWidth - w) / 2;
        int heightOffset = (CanvasHeight - h) / 2;
        g.setColor(0x64aad2);
        g.fillRect(widthOffset+2, heightOffset+2, w, h);
        g.setColor(0xc8ffff);
        g.fillRect(widthOffset, heightOffset, w, h);
        g.setColor(0x000000);
        for (int i = 0; i < TABLE_WIDTH * TABLE_HEIGHT; i++) {
            g.drawString("" + tables.charAt(i), widthOffset + (i % TABLE_WIDTH) * cellW + (cellW / 2), heightOffset + i / TABLE_HEIGHT * cellH, Graphics.HCENTER + Graphics.TOP);
        }
        g.drawRect(widthOffset + posX * cellW - 1, heightOffset + posY * cellH - 1, cellW, cellH);
    }

    public static void right() {
        if (posX < TABLE_WIDTH - 1) {
            posX++;
        } else {
            posX = 0;
        }
    }

    public static void left() {
        if (posX > 0) {
            posX--;
        } else {
            posX = TABLE_WIDTH - 1;
        }
    }

    public static void down() {
        if (posY < TABLE_HEIGHT - 1) {
            posY++;
        } else {
            posY = 0;
        }
    }

    public static void up() {
        if (posY > 0) {
            posY--;
        } else {
            posY = TABLE_HEIGHT - 1;
        }
    }

    public static String select() {
        return "" + tables.charAt(posY * TABLE_WIDTH + posX);
    }

    public static void reset() {
        posX = posY = 0;
    }
}
