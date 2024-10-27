package model;
import controller.User;
import view.*;

import java.io.Serializable;
public class Step implements Serializable{
    //这个是来存储对象属性的序列
    //这个对象要实现对每一次移动过程的记录
    //对象里要有两个chessboardpoint记录起点和终点
    //要记录turn
    //要记录playercolor

    private Chessboard model;

    private ChessboardPoint src;
    private ChessPiece srcpiece;
    private ChessComponent srcComponent;

    private ChessboardPoint dest;
    private ChessPiece destpiece;
    private ChessComponent destComponent;
    private PlayerColor color;
    private User userRed;
    private User userBlue;

    public ChessComponent getDestComponent() {
        return destComponent;
    }

    public ChessComponent getSrcComponent() {
        return srcComponent;
    }

    public void setDestComponent(ChessComponent destComponent) {
        this.destComponent = destComponent;
    }

    public void setSrcComponent(ChessComponent srcComponent) {
        this.srcComponent = srcComponent;
    }

    public Step(PlayerColor color, ChessboardPoint src, ChessboardPoint dest, User userRed ,User userBlue)//存在
    {
        this.color=color;
        this.src=src;
        this.dest=dest;
        this.userRed=userRed;
        this.userBlue=userBlue;
    }

    public ChessboardPoint getDest() {
        return dest;
    }

    public ChessboardPoint getSrc() {
        return src;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public void setDest(ChessboardPoint dest) {
        this.dest = dest;
    }

    public void setSrc(ChessboardPoint src) {
        this.src = src;
    }

    public void setSrcpiece(ChessPiece srcpiece) {
        this.srcpiece = srcpiece;
    }
    //我们的想法是
    //记录移动的起点和终点 然后通过判断终点里有没有chesspiece来确定是一次捕食还是移动
    //这又涉及到regret
    // 如果是被捕食了
    // 要实现复活 这时候就可以通过在原先位置新建一个chesspiece
    //如果是普通的移动
    //那就可以通过movechesspiece来实现


    public void setDestpiece(ChessPiece destpiece) {
        this.destpiece = destpiece;
    }

    public ChessPiece getDestpiece() {
        return destpiece;
    }

    public ChessPiece getSrcpiece() {
        return srcpiece;
    }

//    public boolean Check()//检查每一步对不对 完了还要检查各个步对不对
//    {
//        return;
//    }


    public Chessboard getModel() {
        return model;
    }

    public void setModel(Chessboard model) {
        this.model = model;
    }



    public PlayerColor getColor() {
        return color;
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
}
