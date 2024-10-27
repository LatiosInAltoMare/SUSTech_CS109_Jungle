package controller;

import model.ChessPiece;
import model.Chessboard;
import model.ChessboardPoint;
import model.PlayerColor;
import view.ChessboardComponent;

import java.util.ArrayList;
import java.util.List;

public class AIplayer  {
    private GameController gameController;
    private List<ChessboardPoint> points;
    private PlayerColor playerColor;
    private boolean aggressive;
    private boolean win;
    public AIplayer(GameController gameController)//构造器里面要能读到model里的
    {
        setGameController(gameController);
        playerColor=gameController.getCurrentPlayer();
        setPoints(gameController.findplayerpiece(playerColor));
    }
    public ChessboardPoint select(List<ChessboardPoint> points)//在points里随机选择一个棋子移动
    {
        int i=(int)(Math.random()*points.size());
        while(randomMove(points.get(i))==null)
        {
            i=(int)(Math.random()*points.size());
        }
        return points.get(i);
    }
    public ChessboardPoint agressiveSelect()
    {
        //在目前这个player里面找到所有能移动的棋子 然后判断其侵略程度
        List<ChessboardPoint> forwardmovesrc=new ArrayList<ChessboardPoint>();
        List<ChessboardPoint> forwardcapturesrc=new ArrayList<ChessboardPoint>();
        List<ChessboardPoint> forwardmove=new ArrayList<ChessboardPoint>();
        List<ChessboardPoint> forwardcapture=new ArrayList<ChessboardPoint>();
        for(int i=0;i<points.size();i++)
        {
            ChessboardPoint temp1=points.get(i);
            List<ChessboardPoint> move=gameController.findavailablemove(temp1);
            if(move.size()>0)//如果能走就考虑是不是aggressive的
            {
                for(int j=0;j<move.size();j++)
                {
                    ChessboardPoint temp2=move.get(j);
                    if(Aggressive(temp1,temp2))
                    {
                        forwardmove.add(temp2);
                        forwardmovesrc.add(temp1);
                    }
                }
            }
            //不能走就啥也不干走下一个去
        }
        for(int i=0;i<points.size();i++)
        {
            ChessboardPoint temp1=points.get(i);
            List<ChessboardPoint> move=gameController.findavailablecapture(temp1);
            if(move.size()>0)//如果能走就考虑是不是aggressive的
            {
                for(int j=0;j<move.size();j++)
                {
                    ChessboardPoint temp2=move.get(j);
                    if(Aggressive(temp1,temp2))
                    {
                        forwardcapture.add(temp2);
                        forwardcapturesrc.add(temp1);
                    }
                }
            }
            //不能走就啥也不干走下一个去
        }
        if(forwardcapture.size()>0)//
        {
            return select1(forwardcapturesrc);//这个是棋子向前捕食的位置
        }
        else if(forwardmove.size()>0)
        {
            return select1(forwardmovesrc);//随机选择一个可以向前移动的棋子
        }
        else
        {
            return select1(points);//这些棋子只能左右行走
        }
    }
    public ChessboardPoint select1(List<ChessboardPoint> points)
    {
        return points.get((int)(Math.random()*points.size()));
    }
    public ChessboardPoint randomMove(ChessboardPoint src)
    {
        //随机棋子
        List<ChessboardPoint> availablemove=gameController.findavailablemove(src);
        List<ChessboardPoint> availablecapture=gameController.findavailablecapture(src);
        //随机最终方向
        int index;
        index = (int)(Math.random()*(availablemove.size()+availablecapture.size()));
        if(index<availablemove.size())//就移动
        {
            return availablemove.get(index);
        }
        else
        {
            return availablecapture.get(index-availablemove.size());
        }
    }
    public ChessboardPoint AggresiveMove(ChessboardPoint src)
    {
        //src中必然包含一个向前的，否则
        List<ChessboardPoint> availablemove=gameController.findavailablemove(src);
        List<ChessboardPoint> availablecapture=gameController.findavailablecapture(src);
        for(int i=0;i<availablecapture.size();i++)
        {
            if(Aggressive(src,availablecapture.get(i)))
            {
                return availablecapture.get(i);//只能有一个，找到就退出
            }
        }
        for(int i=0;i<availablemove.size();i++)
        {
            if(Aggressive(src,availablemove.get(i)))
            {
                return availablemove.get(i);//只能有一个，找到就退出
            }
        }
        if(availablecapture.size()>0)//如果都不侵略那就以捕食优先
        {
            return select1(availablecapture);
        }
        else
        {
            return select1(availablemove);
        }
    }

    public boolean Aggressive(ChessboardPoint src,ChessboardPoint dest) {
        if(playerColor.equals(PlayerColor.BLUE))
        {
            return dest.getRow()<src.getRow();//blue往行小的地方走
        }
        else
        {
            return dest.getRow()>src.getRow();//red往行大的地方走
        }
    }

    public void setPoints(List<ChessboardPoint> points) {
        this.points = points;
    }
    //要能够读取model里面每一个类


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public List<ChessboardPoint> getPoints() {
        return points;
    }
}
