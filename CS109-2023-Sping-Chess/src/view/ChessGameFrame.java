package view;

import controller.AudioPlayer;
import controller.GameController;
import model.Chessboard;

import model.PlayerColor;
import model.Step;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame{
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;
    public AudioPlayer music;
    public int loadedPicture=1;

    private ChessboardComponent chessboardComponent;
    private JLabel background;
    public void paintBG(String filename)
    {
        clearBG();
        background=new JLabel(new ImageIcon(filename));
        background.setBounds(0, 0, getWidth(), getHeight());
        add(background);
    }

    public void clearBG() {
        if (background != null) {
            remove(background);
        }
        background = new JLabel();
        background.setBounds(0, 0, getWidth(), getHeight());
        add(background);
        repaint();
        revalidate();
    }

    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;
        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        playbackButton();
        clickTime=0;
        addChessboard();//在这
        addRemakeButton();
        addUndoButton();
        addSaveButton();
        addLoadButton();
        addMuteButton();
        ChangeBG();
        this.music=new AudioPlayer(new File("CS109-2023-Sping-Chess/resources/只因你太美.wav").getAbsolutePath());
        music.play();
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() //该操作初始化了chessboardcomponent
    {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);//构造chessboardcomponent并设置了size
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);//没有找到这个函数？
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        JLabel statusLabel = new JLabel("");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addRemakeButton() {
        JButton button = new JButton("RE!");
        button.setLocation(HEIGTH, HEIGTH / 10 );
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {

            chessboardComponent.clear(chessboardComponent.getGameController().getModel());
            chessboardComponent.getGameController().getTimer1().cancel();
            chessboardComponent.getGameController().getTimer1().purge();
            chessboardComponent.getGameController().timer.setVisible(false);
            chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
            chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
            chessboardComponent.getGameController().showCurrentUser3().setVisible(false);

            chessboardComponent.getGameController().showBlueUser().setVisible(false);
            chessboardComponent.getGameController().showRedUser().setVisible(false);
            GameController g1=new GameController(chessboardComponent,new Chessboard(),chessboardComponent.getGameController().getUserRed(),chessboardComponent.getGameController().getUserBlue());

            g1.timer.setFont(new Font("楷体", Font.BOLD, 20));
            g1.timer.setSize(200, 60);
            g1.timer.setLocation(890, 81 + 570);

            this.add(g1.timer);
            this.add(g1.showCurrentUser1());
            this.add(g1.showCurrentUser2());
            this.add(g1.showCurrentUser3());
            g1.CountDown();
            this.add(g1.timer);
            this.add(g1.showBlueUser());
            this.add(g1.showRedUser());

            JOptionPane.showMessageDialog(this, "棋盘已重置！", "提示", JOptionPane.INFORMATION_MESSAGE);

            clickTime=0;
            this.button.setVisible(false);

            playbackButton();
            add(this.button);
            this.button.setText("回放当前棋局");
            paintBG(new File("CS109-2023-Sping-Chess/resources/背景1.png").getAbsolutePath());

            this.repaint();
        });

        add(button);
    }

    private void addUndoButton() {
        JButton button = new JButton("UNDO");
        button.setLocation(HEIGTH, HEIGTH / 10 +60);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
                chessboardComponent.getGameController().undo();

                if (chessboardComponent.getGameController().getUserBlue().getName().equals("AIPlayer") && chessboardComponent.getGameController().getCurrentPlayer().equals(PlayerColor.BLUE)) {
                    chessboardComponent.getGameController().undo();

                    chessboardComponent.getGameController().showBlueUser().setText(String.format("蓝方用户：%s", chessboardComponent.getGameController().getUserBlue().getName()));
                    chessboardComponent.getGameController().showRedUser().setText(String.format("红方用户：%s 积分：%d", chessboardComponent.getGameController().getUserRed().getName(), chessboardComponent.getGameController().getUserRed().getScore()));

                } else if (chessboardComponent.getGameController().getUserRed().getName().equals("AIPlayer") && chessboardComponent.getGameController().getCurrentPlayer().equals(PlayerColor.RED)) {
                    chessboardComponent.getGameController().undo();

                    chessboardComponent.getGameController().showRedUser().setText(String.format("红方用户：%s", chessboardComponent.getGameController().getUserRed().getName()));
                    chessboardComponent.getGameController().showBlueUser().setText(String.format("蓝方用户：%s 积分：%d", chessboardComponent.getGameController().getUserBlue().getName(), chessboardComponent.getGameController().getUserBlue().getScore()));

                } else if (chessboardComponent.getGameController().getUserRed().getName().equals("RandomAIPlayer") && chessboardComponent.getGameController().getCurrentPlayer().equals(PlayerColor.RED)) {

                    chessboardComponent.getGameController().undo();

                    chessboardComponent.getGameController().showRedUser().setText(String.format("红方用户：%s", chessboardComponent.getGameController().getUserRed().getName()));
                    chessboardComponent.getGameController().showBlueUser().setText(String.format("蓝方用户：%s 积分：%d", chessboardComponent.getGameController().getUserBlue().getName(), chessboardComponent.getGameController().getUserBlue().getScore()));
                } else if (chessboardComponent.getGameController().getUserBlue().getName().equals("RandomAIPlayer") && chessboardComponent.getGameController().getCurrentPlayer().equals(PlayerColor.BLUE)) {
                    chessboardComponent.getGameController().undo();

                    chessboardComponent.getGameController().showBlueUser().setText(String.format("蓝方用户：%s", chessboardComponent.getGameController().getUserBlue().getName()));
                    chessboardComponent.getGameController().showRedUser().setText(String.format("红方用户：%s 积分：%d", chessboardComponent.getGameController().getUserRed().getName(), chessboardComponent.getGameController().getUserRed().getScore()));

                } else {
                    chessboardComponent.getGameController().showRedUser().setText(String.format("红方用户：%s 积分：%d", chessboardComponent.getGameController().getUserRed().getName(), chessboardComponent.getGameController().getUserRed().getScore()));
                    chessboardComponent.getGameController().showBlueUser().setText(String.format("蓝方用户：%s 积分：%d", chessboardComponent.getGameController().getUserBlue().getName(), chessboardComponent.getGameController().getUserBlue().getScore()));

                }
            repaint();
        });
        add(button);
    }
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().save();
            JOptionPane.showMessageDialog(this, "保存", "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        add(this.button);
        button.addActionListener(e -> {
            System.out.println("Click load");

            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            String path = fileChooser.getSelectedFile().getPath();
            List<Step> loadsteps=chessboardComponent.getGameController().loadGameFromFile(path);
            if(!Objects.equals(path.substring(path.length() - 4), ".txt")){
                JOptionPane.showMessageDialog(this,"文件类型错误！");
            }else if(loadsteps.get(0).getModel().getGrid().length!=9||loadsteps.get(0).getModel().getGrid()[0].length!=7){
                JOptionPane.showMessageDialog(this,"棋盘大小错误！");
            }else {

                boolean CorrectChessPiece = true;
                int[] allChessPiece = new int[16];
                int i = 1;
                int j;
                while (i <= 9) {
                    j = 1;
                    while (j <= 7) {
                        if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece() != null) {
                            if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getOwner().equals(PlayerColor.RED)) {
                                switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
                                    case "Elephant":
                                        allChessPiece[7]++;
                                        break;
                                    case "Lion":
                                        allChessPiece[6]++;
                                        break;
                                    case "Tiger":
                                        allChessPiece[5]++;
                                        break;
                                    case "Leopard":
                                        allChessPiece[4]++;
                                        break;
                                    case "Wolf":
                                        allChessPiece[3]++;
                                        break;
                                    case "Dog":
                                        allChessPiece[2]++;
                                        break;
                                    case "Cat":
                                        allChessPiece[1]++;
                                        break;
                                    case "Rat":
                                        allChessPiece[0]++;
                                        break;
                                }
                            } else if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                                switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
                                    case "Elephant":
                                        allChessPiece[15]++;
                                        break;
                                    case "Lion":
                                        allChessPiece[14]++;
                                        break;
                                    case "Tiger":
                                        allChessPiece[13]++;
                                        break;
                                    case "Leopard":
                                        allChessPiece[12]++;
                                        break;
                                    case "Wolf":
                                        allChessPiece[11]++;
                                        break;
                                    case "Dog":
                                        allChessPiece[10]++;
                                        break;
                                    case "Cat":
                                        allChessPiece[9]++;
                                        break;
                                    case "Rat":
                                        allChessPiece[8]++;
                                        break;
                                }
                            }
                        }
                        j++;
                    }
                    i++;
                }
                i = 1;
                while (i <= 16) {
                    if (allChessPiece[i - 1] != 1) {
                        CorrectChessPiece = false;
                    }
                    i++;
                }

                if (!CorrectChessPiece) {
                    JOptionPane.showMessageDialog(this, "棋子错误！");
                }else if(loadsteps.get(loadsteps.size() - 1).getColor()==null) {
                    JOptionPane.showMessageDialog(this, "缺少行棋方！");
                }else {
                    i=1;

                    if(loadsteps.get(0).getModel().calculateDistance(loadsteps.get(0).getSrc(),loadsteps.get(0).getDest())==1) {
                        while (i <= loadsteps.size() - 1) {
                            if (loadsteps.get(i - 1).getModel().getChessPieceAt(loadsteps.get(i).getDest()) == null) {
                                if (!loadsteps.get(i - 1).getModel().isValidMove(loadsteps.get(i).getSrc(), loadsteps.get(i).getDest())) {
                                    break;
                                }
                            } else {
                                if (!loadsteps.get(i - 1).getModel().realcapture(loadsteps.get(i).getSrc(), loadsteps.get(i).getDest())) {
                                    break;
                                }
                            }
                            i++;
                        }
                    }
                    if(i<=loadsteps.size()-1){
                        JOptionPane.showMessageDialog(this, "行棋步骤错误！");
                    }else if (loadsteps.size() > 0) {

                        chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                        chessboardComponent.getGameController().getTimer1().cancel();
                        chessboardComponent.getGameController().getTimer1().purge();
                        chessboardComponent.getGameController().timer.setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser3().setVisible(false);

                        chessboardComponent.getGameController().showRedUser().setVisible(false);
                        chessboardComponent.getGameController().showBlueUser().setVisible(false);
                        GameController g1 = new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel(), loadsteps.get(loadsteps.size() - 1).getUserRed(), loadsteps.get(loadsteps.size() - 1).getUserBlue());

                        g1.setTurns(loadsteps.size());
                        g1.setSteps(loadsteps);
                        g1.timer.setFont(new Font("楷体", Font.BOLD, 20));
                        g1.timer.setSize(400, 60);
                        g1.timer.setLocation(890,81+570);
                        g1.CountDown();
                        this.add(g1.timer);
                        this.add(g1.showCurrentUser1());
                        this.add(g1.showCurrentUser2());
                        this.add(g1.showCurrentUser3());
                        this.add(g1.showRedUser());
                        this.add(g1.showBlueUser());

                        clickTime=0;
                    playbackButton();
                    this.button.setText("回放当前棋局");
                    add(this.button);
                        paintBG(new File("CS109-2023-Sping-Chess/resources/IMG_2043.JPG").getAbsolutePath());

                        this.repaint();
                    }
                }
            }

        });
    }
    private void addMuteButton()
    {
        JButton button = new JButton("Mute");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            this.getMusic().stop();
            JOptionPane.showMessageDialog(this, "已静音", "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        add(button);

    }
    private void ChangeBG()
    {
        JComboBox<String> changeBG = new JComboBox<String>();
        changeBG.setVisible(true);
        changeBG.setSize(200,60);
        changeBG.setLocation(HEIGTH,HEIGTH/10+300);
        changeBG.addItem("主题一");
        changeBG.addItem("主题二");

        changeBG.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JLabel background;
                    if (changeBG.getSelectedIndex() == 0) {
                        paintBG(new File("CS109-2023-Sping-Chess/resources/IMG_2043.JPG").getAbsolutePath());
                    loadedPicture=1;
                    } else {
                        paintBG(new File("CS109-2023-Sping-Chess/resources/背景1.png").getAbsolutePath());
                    loadedPicture=2;
                    }
                    repaint();
                    revalidate();
                }
            }
        });
        add(changeBG);
    }

    private int clickTime=0;
    JButton button = new JButton("回放当前棋局");
    private void playbackButton()
    {
        JButton button = new JButton("回放当前棋局");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("楷体", Font.BOLD, 20));
        button.addActionListener((e) -> {
            Backward();
            List<Step> stepList=new ArrayList<>();
            int i=1;
            while(i<=chessboardComponent.getGameController().getSteps().size()){
                stepList.add(chessboardComponent.getGameController().getSteps().get(i-1));
                i++;
            }

            if(clickTime<=stepList.size()-1) {
                chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                chessboardComponent.getGameController().getTimer1().cancel();
                chessboardComponent.getGameController().getTimer1().purge();
                chessboardComponent.getGameController().timer.setVisible(false);
                chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
                chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
                chessboardComponent.getGameController().showCurrentUser3().setVisible(false);

                chessboardComponent.getGameController().showRedUser().setVisible(false);
                chessboardComponent.getGameController().showBlueUser().setVisible(false);
                GameController g2 = new GameController(chessboardComponent, stepList.get(clickTime).getModel(), stepList.get(clickTime).getUserRed(), stepList.get(clickTime).getUserBlue());
                g2.isMovable=false;

                g2.setTurns(clickTime + 1);
                g2.setSteps(stepList);


                PlayerColor color;
                if(clickTime%2==1) {
                    color = PlayerColor.BLUE;
                }else{
                    color = PlayerColor.RED;
                }
                this.add(g2.showCurrentUser1());

                this.add(g2.showCurrentUser2());
                g2.showCurrentUser1().setText(String.format("当前回合：%s",color));
                if(loadedPicture==1){
                    paintBG(new File("CS109-2023-Sping-Chess/resources/背景1.png").getAbsolutePath());
                }else{
                    paintBG(new File("CS109-2023-Sping-Chess/resources/IMG_2043.JPG").getAbsolutePath());
                }
                this.repaint();
                clickTime++;

            }else{
                this.button.setText("回放当前棋局");
                JOptionPane.showMessageDialog(this, "已是最新一步！", "提示", JOptionPane.INFORMATION_MESSAGE);
                this.button.repaint();
            }

        });
        add(button);
    }
    public void Backward(){
        JButton button = new JButton("上一步");
        button.setLocation(HEIGTH, HEIGTH / 10 + 420);
        button.setSize(200, 60);
        button.setFont(new Font("楷体", Font.BOLD, 20));
        button.addActionListener((e) -> {

            if(clickTime>=2) {
                clickTime--;
                List<Step> stepList = new ArrayList<>();
                int i = 1;
                while (i <= chessboardComponent.getGameController().getSteps().size()) {
                    stepList.add(chessboardComponent.getGameController().getSteps().get(i - 1));
                    i++;
                }

                if (clickTime <= stepList.size() - 1) {
                    chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                    chessboardComponent.getGameController().getTimer1().cancel();
                    chessboardComponent.getGameController().getTimer1().purge();
                    chessboardComponent.getGameController().timer.setVisible(false);
                    chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
                    chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
                    chessboardComponent.getGameController().showCurrentUser3().setVisible(false);

                    chessboardComponent.getGameController().showRedUser().setVisible(false);
                    chessboardComponent.getGameController().showBlueUser().setVisible(false);

                    GameController g2 = new GameController(chessboardComponent, stepList.get(clickTime - 1).getModel(), stepList.get(clickTime - 1).getUserRed(), stepList.get(clickTime - 1).getUserBlue());
                    g2.setTurns(clickTime);
                    g2.setSteps(stepList);
                    PlayerColor color;
                    if (clickTime % 2 == 1) {
                        color = PlayerColor.BLUE;
                    } else {
                        color = PlayerColor.RED;
                    }
                    this.add(g2.showCurrentUser1());

                    this.add(g2.showCurrentUser2());
                    g2.showCurrentUser1().setText(String.format("当前回合：%s", color));
                    if(loadedPicture==1){
                        paintBG(new File("CS109-2023-Sping-Chess/resources/背景1.png").getAbsolutePath());
                    }else{
                        paintBG(new File("CS109-2023-Sping-Chess/resources/IMG_2043.JPG").getAbsolutePath());
                    }

                    this.repaint();
                }
            }

        });
        add(button);
    }
public void pauseTimer(GameController gameController){

    JButton button = new JButton("取消倒计时");
    button.setLocation(HEIGTH, HEIGTH / 10-60 );
    button.setSize(200, 60);
    button.setFont(new Font("宋体", Font.BOLD, 20));
    button.addActionListener((e) -> {
        chessboardComponent.getGameController().setTime(999999);
        chessboardComponent.getGameController().getTimer().setVisible(false);
    });

    add(button);

}
    public AudioPlayer getMusic() {
        return music;
    }
}
