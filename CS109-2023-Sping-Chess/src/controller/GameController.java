package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/

public class GameController extends JComponent implements GameListener,Serializable{
    private List<Step> steps;
    private User userRed;
    private User userBlue;
    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;
    private DataInput objectIn;
//    public AudioPlayer music;
    public JLabel timer=new JLabel();
    private int time;
    private Timer timer1;
    private final CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];
    private int turns=0;
    private JLabel showCurrentUser1=new JLabel();
    private JLabel showCurrentUser2=new JLabel();
    private JLabel showCurrentUser3=new JLabel();

    public boolean isMovable=true;
    public boolean undoable=true;


    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public JLabel getTimer() {
        return timer;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    private ChessboardPoint selectedPoint;

    public GameController(ChessboardComponent view, Chessboard model,User userRed,User userBlue) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        this.steps=new ArrayList<Step>();
        this.userBlue=userBlue;
        this.userRed=userRed;
        timer1=new Timer();
        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }
    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
            }
        }
    }
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;

        if(timer.isVisible()){
            setTime(10);
        }else{
            setTime(9999999);
        }
        showCurrentUser1.setText(String.format("当前回合：%s",currentPlayer));
        showCurrentUser2.setText(String.format("回合数：%d",turns));
        showCurrentUser3.setText("倒计时：");

    }
    public void CountDown(){
            time = 10;
            timer1.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    if(currentPlayer==PlayerColor.BLUE&&userBlue.getName().equals("AIPlayer")){
                        AIplay();
                    }else if(currentPlayer==PlayerColor.RED&&userRed.getName().equals("AIPlayer")) {
                        AIplay();
                    }else if(currentPlayer==PlayerColor.RED&&userRed.getName().equals("RandomAIPlayer")){
                        AIplay1();
                    }else if(currentPlayer==PlayerColor.BLUE&&userBlue.getName().equals("RandomAIPlayer")){
                        AIplay1();
                    }
                    else {
                        if (time > 0) {
                            time--;
                            timer.setText(String.valueOf(time));
                        } else {
                            AIplay();
                        }
                    }
                }
            }, 0, 1000);
    }
    private int win() {
        if(model.judgeDens1Cell()|findplayerpiece(PlayerColor.RED).size()==0)
        {
            userBlue.setWin(userBlue.getWin()+1);
            userBlue.setScore(userBlue.getScore()+200);
            isMovable=false;
            undoable=false;
            return 1;//蓝方胜
        }else if(model.judgeDens2Cell()|findplayerpiece(PlayerColor.BLUE).size()==0)
        {
            userRed.setWin(userRed.getWin()+1);
            userRed.setScore(userRed.getScore()+200);
            isMovable=false;
            undoable=false;
            return 2;//红方胜
        }else{
            return 0;
        }
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) //选中空地判断移动
    {
        if(isMovable) {
            if (selectedPoint != null && model.isValidMove(selectedPoint, point)) //可以移动到point
            {
                cancelpainted(findavailablecapture(selectedPoint));
                cancelpainted(findavailablemove(selectedPoint));

                record(currentPlayer, selectedPoint, point);//记录移动之前的src和dest的状态

                model.moveChessPiece(selectedPoint, point);//移动之后判断有没有进陷阱

                ChessComponent temp = view.removeChessComponentAtGrid(selectedPoint);

                steps.get(turns - 1).setSrcComponent(temp);
                view.setChessComponentAtGrid(point, temp);
                swapColor();
                selectedPoint = null;
                view.repaint();
                AudioPlayer player = new AudioPlayer("CS109-2023-Sping-Chess/resources/鸡.wav");
                player.play1();

                steps.get(turns - 1).setModel(model.clone());

                int flag = win();
                if (flag == 1) {
                    view.getGameController().getTimer1().cancel();
                    view.getGameController().getTimer1().purge();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "蓝方胜！积分+200！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else if (flag == 2) {
                    view.getGameController().getTimer1().cancel();
                    view.getGameController().getTimer1().purge();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "红方胜！积分+200！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            }
        }
    }

    public void onPlayerClickChessPiece(ChessboardPoint point, view.ChessComponent component) //选中棋子判断捕捉
    {
        if(isMovable) {
            if (selectedPoint == null) //selectedpoint是本来选中的点
            {
                if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                    selectedPoint = point;

                    component.setSelected(true);
                    component.repaint();

                    print(findavailablemove(selectedPoint));
                    print(findavailablecapture(selectedPoint));

                    getpainted(findavailablecapture(selectedPoint));
                    getpainted(findavailablemove(selectedPoint));

                }
            } else if (selectedPoint.equals(point))//放下棋子
            {
                cancelpainted(findavailablecapture(selectedPoint));
                cancelpainted(findavailablemove(selectedPoint));
                selectedPoint = null;

                component.setSelected(false);
                component.repaint();
            }
            else {
                boolean flag = model.realcapture(selectedPoint, point);

                if (flag)//移动了那就把这个record留下
                {
                    cancelpainted(findavailablecapture(selectedPoint));
                    cancelpainted(findavailablemove(selectedPoint));

                    record(currentPlayer, selectedPoint, point);

                    model.captureChessPiece(selectedPoint, point);
                    ChessComponent temp1 = view.removeChessComponentAtGrid(point);//拿掉原来占着坑的棋子
                    steps.get(turns - 1).setDestComponent(temp1);

                    ChessComponent temp2 = view.removeChessComponentAtGrid(selectedPoint);
                    steps.get(turns - 1).setSrcComponent(temp2);

                    view.setChessComponentAtGrid(point, temp2);

                    swapColor();
                    selectedPoint = null;
                    view.repaint();
                    AudioPlayer player = new AudioPlayer(new File("CS109-2023-Sping-Chess/resources/你干嘛.wav").getAbsolutePath());
                    player.play1();
                    steps.get(turns - 1).setModel(model.clone());

                    int flag1 = win();
                    if (flag1 == 1) {
//                    view.clear(view.getGameController().getModel());
                        view.getGameController().getTimer1().cancel();
                        view.getGameController().getTimer1().purge();
                        view.getGameController().timer.setVisible(false);
                        isMovable=false;
                        undoable=false;
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "蓝方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                        });
                    } else if (flag1 == 2) {
//                    view.clear(view.getGameController().getModel());
                        view.getGameController().getTimer1().cancel();
                        view.getGameController().getTimer1().purge();
                        view.getGameController().timer.setVisible(false);
                        isMovable=false;
                        undoable=false;
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "红方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                        });
                    }
                }
            }
        }
    }
    private void record(PlayerColor color,ChessboardPoint src,ChessboardPoint dest)
    {
        turns++;

        Step step=new Step(color,src,dest,userRed,userBlue);
        step.setSrcpiece(model.getChessPieceAt(src));//存储移动之前双方棋子
        step.setDestpiece(model.getChessPieceAt(dest));
        steps.add(step);
    }
    public void undo()//撤回当前回合step
    {
        if (undoable) {
            if (currentPlayer.equals(PlayerColor.RED)) {

                if (userBlue.getScore() >= 50) {

                    if (turns > 0) {
                        userBlue.setScore(userBlue.getScore() - 50);
                        Step undoStep = steps.get(turns - 1);//得到上一步执行之后的结果，也就是这一步的初态
                        //如果移动合理
                        ChessboardPoint src = undoStep.getSrc();
                        ChessboardPoint dest = undoStep.getDest();

                        ChessPiece beforesrc = undoStep.getSrcpiece();
                        ChessPiece beforedest = undoStep.getDestpiece();

                        model.setChessPiece(src, beforesrc);
                        model.setChessPiece(dest, beforedest);

                        view.removeChessComponentAtGrid(dest);//拿掉原来占着坑的棋子

                        if (undoStep.getDestComponent() != null) {
                            view.setChessComponentAtGrid(dest, undoStep.getDestComponent());
                        }

                        if (undoStep.getSrcComponent() != null) {
                            view.setChessComponentAtGrid(src, undoStep.getSrcComponent());
                        }
                        swapColor();

                        selectedPoint = null;
                        if(userBlue.getName().equals("AIPlayer")||userBlue.getName().equals("RandomAIPlayer")){
                            showBlueUser.setText(String.format("蓝方用户：%s", userBlue.getName()));
                        }
                        view.repaint();
                        turns--;
                        steps.remove(undoStep);
                    }
                } else {
                    if(userBlue.getName().equals("AIPlayer")||userBlue.getName().equals("RandomAIPlayer")){
                        showBlueUser.setText(String.format("蓝方用户：%s", userBlue.getName()));
                    }
                    JOptionPane.showMessageDialog(this, "积分不足，无法悔棋！");
                    view.repaint();
                }
            } else if (currentPlayer.equals(PlayerColor.BLUE)) {
                if (userRed.getScore() >= 50) {

                    if (turns > 0) {
                        userRed.setScore(userRed.getScore() - 50);
                        Step undoStep = steps.get(turns - 1);//得到上一步执行之后的结果，也就是这一步的初态
                        ChessboardPoint src = undoStep.getSrc();
                        ChessboardPoint dest = undoStep.getDest();

                        ChessPiece beforesrc = undoStep.getSrcpiece();
                        ChessPiece beforedest = undoStep.getDestpiece();

                        model.setChessPiece(src, beforesrc);
                        model.setChessPiece(dest, beforedest);

                        view.removeChessComponentAtGrid(dest);//拿掉原来占着坑的棋子

                        if (undoStep.getDestComponent() != null) {
                            view.setChessComponentAtGrid(dest, undoStep.getDestComponent());
                        }

                        if (undoStep.getSrcComponent() != null) {
                            view.setChessComponentAtGrid(src, undoStep.getSrcComponent());
                        }
                        swapColor();

                        selectedPoint = null;

                        turns--;
                        steps.remove(undoStep);

                        if(userRed.getName().equals("AIPlayer")||userRed.getName().equals("RandomAIPlayer")){
                            showRedUser.setText(String.format("红方用户：%s", userRed.getName()));
                        }
                        view.repaint();

                    }
                    setTime(10);
                    timer.setVisible(true);

                } else {
                    if(userRed.getName().equals("AIPlayer")||userRed.getName().equals("RandomAIPlayer")){
                        showRedUser.setText(String.format("红方用户：%s", userRed.getName()));
                    }
                    JOptionPane.showMessageDialog(this, "积分不足，无法悔棋！");
                    view.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(this, "已是初始棋盘！");
            }
        }
    }
    public void setModel(Chessboard model) {
        this.model = model;
    }

    public Chessboard getModel() {
        return model;
    }

    public ChessboardComponent getView() {
        return view;
    }


public void save() {

    List<Step> objectsToSerialize = steps;

    try {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {

            String selectedpath = fileChooser.getSelectedFile().getPath();
            serializeSteps(steps,selectedpath);

        }
    }
    catch (IOException e) {
        e.printStackTrace();
    }
}
    public List<Step> loadGameFromFile(String path) {
        try {
            List<Step> deserializedSteps = deserializeSteps(path);

            setTime(10);
            return deserializedSteps;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public  void serializeSteps(List<Step> steps, String filePath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(steps);
        out.close();
        fileOut.close();
        File file = new File(filePath);
        System.out.println("Serialized data is saved in " + filePath);
    }

    public  List<Step> deserializeSteps(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        List<Step> steps = (List<Step>) in.readObject();
        in.close();
        fileIn.close();
        return steps;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public User getUserBlue() {
        return userBlue;
    }

    public User getUserRed() {
        return userRed;
    }

    public void setUserBlue(User userBlue) {
        this.userBlue = userBlue;
    }

    public void setUserRed(User userRed) {
        this.userRed = userRed;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public List<ChessboardPoint> findavailablemove(ChessboardPoint point)
    {
        List<ChessboardPoint> availablemove=new ArrayList<ChessboardPoint>();
                int i=0;
                while(i<9){
                    int j=0;
                    while(j<7){
                        ChessboardPoint temp = new ChessboardPoint(i, j);
                        if(getModel().isValidMove(point,temp)&&model.getChessPieceAt(temp)==null&&!temp.equals(point))
                        {
                            availablemove.add(temp);
                        }
                        j++;
                    }
                    i++;
                }
                return availablemove;
    }
    public List<ChessboardPoint> findavailablecapture(ChessboardPoint point)
    {
        List<ChessboardPoint> availablecapture=new ArrayList<ChessboardPoint>();
        int i=0;
        while(i<9){
            int j=0;
            while(j<7){
                ChessboardPoint temp = new ChessboardPoint(i, j);
                if(model.getChessPieceAt(temp)!=null&&model.realcapture(point,temp)&&!temp.equals(point))
                {
                    availablecapture.add(temp);
                }
                j++;
            }
            i++;
        }
        return availablecapture;
    }

    public void print(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            System.out.println("("+chessboardPoints.get(i).getRow()+","+chessboardPoints.get(i).getCol()+")"+"is available to move!");
        }
    }
    public List<ChessboardPoint> findplayerpiece(PlayerColor playerColor)
    {
        List<ChessboardPoint> chessboardPoints=new ArrayList<ChessboardPoint>();
        for(int i=0;i<9;i++)
        {
            for (int j=0;j<7;j++)
            {
                ChessboardPoint temp=new ChessboardPoint(i,j);
                if(getModel().getChessPieceAt(temp)!=null)
                {
                    if(getModel().getChessPieceAt(temp).getOwner().equals(playerColor))
                    {
                        chessboardPoints.add(temp);
                    }
                }
            }
        }
        return chessboardPoints;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public void AIplay()
    {
        AIplayer ai=new AIplayer(this);
        if(selectedPoint!=null) {
            cancelpainted(findavailablecapture(selectedPoint));
            cancelpainted(findavailablemove(selectedPoint));
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).setSelected(false);
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).repaint();
            selectedPoint = null;
        }
            ChessboardPoint selectedPoint = ai.agressiveSelect();//在全棋子中随机选择一个点
            print(findavailablemove(selectedPoint));
            print(findavailablecapture(selectedPoint));
            this.setSelectedPoint(selectedPoint);//设置为选中点

            ChessboardPoint destination = ai.AggresiveMove(selectedPoint);//选到的棋子随机移动

            if (this.getModel().getChessPieceAt(destination) != null)//有东西
            {
                this.onPlayerClickChessPiece(destination, (ChessComponent) view.getGridComponentAt(destination).getComponents()[0]);
            } else {

                this.onPlayerClickCell(destination, view.getGridComponentAt(destination));
            }
    }
    public void AIplay1()
    {
        AIplayer ai=new AIplayer(this);
        if(selectedPoint!=null) {
            cancelpainted(findavailablecapture(selectedPoint));
            cancelpainted(findavailablemove(selectedPoint));
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).setSelected(false);
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).repaint();
            selectedPoint = null;
        }
        ChessboardPoint selectedPoint = ai.agressiveSelect();//在全棋子中随机选择一个点
        print(findavailablemove(selectedPoint));
        print(findavailablecapture(selectedPoint));
        this.setSelectedPoint(selectedPoint);//设置为选中点

        ChessboardPoint destination = ai.randomMove(selectedPoint);//选到的棋子随机移动

        if (this.getModel().getChessPieceAt(destination) != null)//有东西
        {
            this.onPlayerClickChessPiece(destination, (ChessComponent) view.getGridComponentAt(destination).getComponents()[0]);
        } else {

            this.onPlayerClickCell(destination, view.getGridComponentAt(destination));
        }

    }
    public void setSelectedPoint(ChessboardPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public JLabel showCurrentUser1(){
        showCurrentUser1.setText(String.format("当前回合：%s",currentPlayer));
        showCurrentUser1.setFont(new Font("宋体", Font.BOLD, 20));
        showCurrentUser1.setSize(400,60);
        showCurrentUser1.setLocation(810,81+470);
        return showCurrentUser1;
    }
    public JLabel showCurrentUser2(){
        showCurrentUser2.setText(String.format("回合数：%d",turns));
        showCurrentUser2.setFont(new Font("宋体", Font.BOLD, 20));
        showCurrentUser2.setSize(400,60);
        showCurrentUser2.setLocation(810,81+520);
        return showCurrentUser2;
    }
    public JLabel showCurrentUser3(){
//        JLabel showCurrentUser=new JLabel();
        showCurrentUser3.setText("倒计时：");
        showCurrentUser3.setFont(new Font("宋体", Font.BOLD, 20));
        showCurrentUser3.setSize(400,60);
        showCurrentUser3.setLocation(810,81+570);
        return showCurrentUser3;
    }
    public void getpainted(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).setIsValid(true);
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).repaint();
        }
    }
    public void cancelpainted(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).setIsValid(false);
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).repaint();
        }
    }
    public Timer getTimer1() {
        return timer1;
    }

    public List<Step> getSteps() {
        return steps;
    }
    JLabel showRedUser=new JLabel();
    public JLabel showRedUser(){
        if(userRed.getName().equals("RandomAIPlayer")||userRed.getName().equals("AIPlayer")) {
            showRedUser.setText(String.format("红方用户：%s", userRed.getName()));
            showRedUser.setFont(new Font("宋体", Font.BOLD, 20));
            showRedUser.setSize(400, 30);
            showRedUser.setLocation(160, 20);
            return showRedUser;
        }else{
            showRedUser.setText(String.format("红方用户：%s 积分：%d", userRed.getName(),userRed.getScore()));
            showRedUser.setFont(new Font("宋体", Font.BOLD, 20));
            showRedUser.setSize(400, 30);
            showRedUser.setLocation(160, 20);
            return showRedUser;
        }
    }
    JLabel showBlueUser=new JLabel();
    public JLabel showBlueUser(){
        if(userBlue.getName().equals("RandomAIPlayer")||userBlue.getName().equals("AIPlayer")) {
            showBlueUser.setText(String.format("蓝方用户：%s", userBlue.getName()));
            showBlueUser.setFont(new Font("宋体", Font.BOLD, 20));
            showBlueUser.setSize(400, 30);
            showBlueUser.setLocation(160, 50);
            return showBlueUser;
        }else{
            showBlueUser.setText(String.format("蓝方用户：%s 积分：%d", userBlue.getName(),userBlue.getScore()));
            showBlueUser.setFont(new Font("宋体", Font.BOLD, 20));
            showBlueUser.setSize(400,30);
            showBlueUser.setLocation(160,50);
            return showBlueUser;
        }

    }
}
