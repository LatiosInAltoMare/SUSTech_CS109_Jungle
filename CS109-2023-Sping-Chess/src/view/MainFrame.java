package view;
import controller.GameController;
import controller.User;
import model.Chessboard;
import model.Step;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame{
    private ArrayList<User> users=new ArrayList<User>();
    private final int WIDTH;
    private final int HEIGTH;
    private User userRed;
    private User userBlue;
    private ChessGameFrame chessGameFrame;
//    public AudioPlayer music;
//    public MainFrame(int width, int height) throws IOException, ClassNotFoundException {
//        setUsers(deserializeUsers("data/保存的用户.txt"));
//        setTitle("斗兽棋游戏大厅"); //设置标题
//        this.WIDTH = width;
//        this.HEIGTH = height;
//        setSize(WIDTH, HEIGTH);
//
//        setLocationRelativeTo(null); // Center the window.
//        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
//
//        ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
//
//        setLayout(new BorderLayout());
//        JLabel background=new JLabel(new ImageIcon("resources/IMG_2042.JPG"));
//        add(background);
//        background.setLayout(new FlowLayout());
//
//        addStartButton();
//        addNewUserButton();
//        addRankButton();
//        addExitButton();
//
//
//    }
public MainFrame(int width, int height) throws IOException, ClassNotFoundException {
    setUsers(deserializeUsers("data/保存的用户.txt"));
    boolean AIexist=false;
    boolean RandomAIexist=false;
    int i=1;
    while(i<=users.size()){
        if(users.get(i-1).getName().equals("AIPlayer")){
            AIexist=true;
        }
        if(users.get(i-1).getName().equals("RandomAIPlayer")){
            RandomAIexist=true;
        }
        i++;
    }
    if(AIexist&&!RandomAIexist) {
        User ai1=new User("RandomAIPlayer");
        ai1.setScore(999999999);
        users.add(ai1);
    }else if(!AIexist&&RandomAIexist){
        User ai2=new User("AIPlayer");
        ai2.setScore(999999999);
        users.add(ai2);
    }else if(!AIexist){
        User ai1=new User("RandomAIPlayer");
        ai1.setScore(999999999);

        User ai2=new User("AIPlayer");
        ai2.setScore(999999999);

        users.add(ai1);
        users.add(ai2);
    }

    setTitle("斗兽棋游戏大厅"); //设置标题
    this.WIDTH = width;
    this.HEIGTH = height;
    setSize(WIDTH, HEIGTH);

    setLocationRelativeTo(null); // Center the window.
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了

    setLayout(null);

    //ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
    addStartButton();
    addNewUserButton();
    addRankButton();
    addExitButton();


        JLabel background = new JLabel(new ImageIcon(new File("CS109-2023-Sping-Chess/resources/IMG_2042.JPG").getAbsolutePath()));
        background.setBounds(0, 0, WIDTH, HEIGTH);
        add(background);


}

    private void addStartButton() {
        JButton button = new JButton("Game Start !");
        button.setLocation(150, 110);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            ChessGameFrame chessGameFrame=new ChessGameFrame(1100,810);
            //GameController gameController = new GameController(chessGameFrame.getChessboardComponent(), new Chessboard());
            chessGameFrame.setVisible(true);
//            ChooseToGoFrame chooseToGoFrame=new ChooseToGoFrame(500,500);
//            chooseToGoFrame.setVisible(true);
            ChooseToGo chooseToGo=new ChooseToGo();
//            JComboBox<String> chooseRedUser=new JComboBox<String>();
//            chooseRedUser.setSize(200,60);
//            chooseRedUser.setLocation(180,150);
//            chooseRedUser.setVisible(true);
//            add(chooseRedUser);
//            JComboBox<String> chooseBlueUser=new JComboBox<String>();
//            chooseBlueUser.setSize(200,60);
//            chooseBlueUser.setLocation(180,150);
//            chooseBlueUser.setVisible(true);
//            add(chooseBlueUser);
            ArrayList<String> userName=new ArrayList<>();
            int i=1;
            while(i<= users.size()){
                userName.add(users.get(i-1).getName());
                i++;
            }

            for(String item:userName){
                chooseToGo.chooseRedUser.addItem(item);
            }

            for(String item:userName){
                chooseToGo.chooseBlueUser.addItem(item);
            }

            chooseToGo.confirm.addActionListener(e1 ->{
                    userRed=users.get(chooseToGo.chooseRedUser.getSelectedIndex());
                    userBlue=users.get(chooseToGo.chooseBlueUser.getSelectedIndex());
                    if(userRed.equals(userBlue)){
//                        JLabel jLabel=new JLabel("不能自己和自己玩！！！");
                        chooseToGo.addLabel1();
                    }else {
                        GameController g0 = new GameController(chessGameFrame.getChessboardComponent(), new Chessboard(), userRed, userBlue);
                        chessGameFrame.getChessboardComponent().clear(chessGameFrame.getChessboardComponent().getGameController().getModel());
                        chessGameFrame.getChessboardComponent().getGameController().getTimer1().cancel();
                        chessGameFrame.getChessboardComponent().getGameController().getTimer1().purge();
                        chessGameFrame.getChessboardComponent().getGameController().timer.setVisible(false);

                        GameController g1 = new GameController(chessGameFrame.getChessboardComponent(), new Chessboard(), userRed, userBlue);
                        chooseToGo.getFrame().setVisible(false);
                        g1.timer.setFont(new Font("楷体", Font.BOLD, 20));
                        g1.timer.setSize(200,60);
                        g1.timer.setLocation(890,81+570);
                        chessGameFrame.add(g1.timer);
//                        JLabel showCurrentUser=new JLabel(String.format("当前回合：%s 倒计时：", g1.getCurrentPlayer()));
//                        showCurrentUser.setFont(new Font("Rockwell", Font.BOLD, 20));
//                        showCurrentUser.setSize(200,60);
//                        showCurrentUser.setLocation(810,81+420);
//                        chessGameFrame.add(showCurrentUser);
                        //chessGameFrame.add(g1.showCurrentUser());
//                        chessGameFrame.add(g1.showRedUser());
//                        chessGameFrame.add(g1.showBlueUser());

                        chessGameFrame.add(g1.showRedUser());
                        chessGameFrame.add(g1.showBlueUser());
                        g1.CountDown();//按下确认之后游戏开始
                        chessGameFrame.add(g1.showCurrentUser1());
                        chessGameFrame.add(g1.showCurrentUser2());
                        chessGameFrame.add(g1.showCurrentUser3());
                        chessGameFrame.pauseTimer(g1);

//                        JLabel background=new JLabel(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\IMG_2043.JPG"));
//                        background.setBounds(0, 0, chessGameFrame.getWidth(), chessGameFrame.getHeight());
//                        chessGameFrame.add(background);
                        chessGameFrame.paintBG(new File("CS109-2023-Sping-Chess/resources/IMG_2043.JPG").getAbsolutePath());
                        chessGameFrame.repaint();
                    }

            });

        });
        add(button);
    }




    private void addNewUserButton() {
        JButton button = new JButton("New User");
        button.setLocation(150, 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            String name = JOptionPane.showInputDialog(this,"新建用户名称");
            boolean sameName=false;
            int i=1;
            while(i<=users.size()){
                if(name.equals(users.get(i-1).getName())){
                    sameName=true;
                    break;
                }
                i++;
            }
            if(!sameName) {
                //String name = JOptionPane.showInputDialog(this,"新建用户名称");
                users.add(new User(name));
//                try {
//                    File directory = new File("User");
//                    if (!directory.exists()) {
//                        directory.mkdirs();
//                    }
//                    serializeUsers(users, "data/保存的用户.txt");
//                }
//                catch (IOException e1) {
//                    e1.printStackTrace();
//                }
            }else{
                JOptionPane.showMessageDialog(this,"用户不可重名！！");
            }
        });
        add(button);
    }
    public void addExitButton(){
        JButton button = new JButton("EXIT");
        button.setLocation(150, 320);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            try {
                    File directory = new File("data");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    serializeUsers(users, "data/保存的用户.txt");
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            System.exit(0);
        });
        add(button);
    }
    public  ArrayList<User> deserializeUsers(String filePath) throws IOException, ClassNotFoundException {
        File file=new File("data/保存的用户.txt");
        if(file.exists()) {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<User> users = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
            return users;
        }else{
            return new ArrayList<User>();
        }
    }
    public  void serializeUsers(ArrayList<User> users, String filePath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(users);
        out.close();
        fileOut.close();
        File file = new File(filePath);
//        String absolutePath = file.getAbsolutePath();
        System.out.println("Serialized users are saved in " + filePath);
//        System.out.println("Serialized data is saved in " + absolutePath);
    }
    private void addRankButton() {
        JButton button = new JButton("Rank");
        button.setLocation(150, 250);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {

            ArrayList<User> exceptAI=new ArrayList<User>();
            int i=1;

            while(i<=users.size()){
                if(!users.get(i-1).getName().equals("AIPlayer")&&!users.get(i-1).getName().equals("RandomAIPlayer")){
                    exceptAI.add(users.get(i-1));
                }
                i++;
            }
            int[] winNum =new int[exceptAI.size()];
            int[] userNum=new int[exceptAI.size()];

            int temp1;
            i=1;
            while(i<=exceptAI.size()){
                winNum[i-1]=exceptAI.get(i-1).getWin();
                userNum[i-1]=i-1;
                i++;
            }
            for(int j=0;j<exceptAI.size()-1;j++){
                for(int k=0;k<exceptAI.size()-1;k++){
                    if(winNum[k]<winNum[k+1]){
                        temp1=winNum[k];
                        winNum[k]=winNum[k+1];
                        winNum[k+1]=temp1;
                        temp1=userNum[k];
                        userNum[k]=userNum[k+1];
                        userNum[k+1]=temp1;
                    }
                }
            }
            StringBuilder Ranking;
            Ranking = new StringBuilder("Rank of all users:\n");
            i=1;
            while(i<=exceptAI.size()){
                Ranking.append(String.format("No.%d %s 胜利:%d 积分:%d\n", i, exceptAI.get(userNum[i - 1]).getName(), exceptAI.get(userNum[i - 1]).getWin(),exceptAI.get(userNum[i - 1]).getScore()));
                i++;
            }
            JOptionPane.showMessageDialog(this,Ranking);
        });
        add(button);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
