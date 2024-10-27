package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private Color background;
    private boolean isValid;
    private ImageIcon backgroundEntered;
    private boolean showImage;

    public CellComponent(Color background, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
        this.isValid = false; // 默认值为 false
        showImage=false;

        // Load the images
        backgroundEntered = new ImageIcon(new File("CS109-2023-Sping-Chess/resources/选中草地.png").getAbsolutePath());

//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent f) {
//                System.out.println("y");
//                showImage=true;
//                repaint();
//
//            }
//            public void mouseExited(MouseEvent f) {
//                System.out.println("n");
//                showImage = false;
//                repaint();
//                // You can add more tasks here
//            }
//
//        });
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
        repaint(); // 每次修改 isValid 的值时都要重新绘制
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
//        g.setColor(background);
//        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);




        if(background.equals(Color.YELLOW))
        {
            g.drawImage(new ImageIcon(new File("CS109-2023-Sping-Chess/resources/陷阱.png").getAbsolutePath()).getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if (background.equals(Color.MAGENTA))
        {
            g.drawImage(new ImageIcon(new File("CS109-2023-Sping-Chess/resources/兽穴.png").getAbsolutePath()).getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if(background.equals(Color.LIGHT_GRAY))
        {
            g.drawImage(new ImageIcon(new File("CS109-2023-Sping-Chess/resources/草地.png").getAbsolutePath()).getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if(background.equals(Color.CYAN))
        {
            g.drawImage(new ImageIcon(new File("CS109-2023-Sping-Chess/resources/河流.png").getAbsolutePath()).getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        if(showImage) {
            g.drawImage(backgroundEntered.getImage(), 0, 0, getWidth(), getHeight(), this);
        }


        if (isValid) {
//            g.setColor(Color.MAGENTA); // 当 isValid 为 true 时，边框颜色为红色
//            g.drawRect(1, 1, this.getWidth()-10, this.getHeight()-10);
            Graphics2D g2 = (Graphics2D) g; // 转换 Graphics 到 Graphics2D
            g2.setColor(Color.MAGENTA); // 设置颜色

            float strokeWidth = 5.0f; // 设置线条宽度
            g2.setStroke(new BasicStroke(strokeWidth)); // 设置 Stroke

            // 画矩形
            g2.drawRect(1, 1, this.getWidth()-2, this.getHeight()-2);
        }

    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }
}
