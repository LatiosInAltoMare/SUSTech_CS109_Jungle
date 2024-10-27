package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard implements Serializable {
    private Cell[][] grid;
    private final Set<ChessboardPoint> riverCell = new HashSet<>();//设置河水点集 用hash码存储
    private final Set<ChessboardPoint> dens1Cell = new HashSet<>();
    private final Set<ChessboardPoint> dens2Cell = new HashSet<>();
    private final Set<ChessboardPoint> trap1Cell = new HashSet<>();
    private final Set<ChessboardPoint> trap2Cell = new HashSet<>();



    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19

        init();
        initGrid();
        initPieces();
    }
    private void init()
    {
        riverCell.add(new ChessboardPoint(3,1));
        riverCell.add(new ChessboardPoint(3,2));
        riverCell.add(new ChessboardPoint(4,1));
        riverCell.add(new ChessboardPoint(4,2));
        riverCell.add(new ChessboardPoint(5,1));
        riverCell.add(new ChessboardPoint(5,2));

        riverCell.add(new ChessboardPoint(3,4));
        riverCell.add(new ChessboardPoint(3,5));
        riverCell.add(new ChessboardPoint(4,4));
        riverCell.add(new ChessboardPoint(4,5));
        riverCell.add(new ChessboardPoint(5,4));
        riverCell.add(new ChessboardPoint(5,5));

        dens1Cell.add(new ChessboardPoint(0,3));
        dens2Cell.add(new ChessboardPoint(8,3));

        trap1Cell.add(new ChessboardPoint(0,2));
        trap1Cell.add(new ChessboardPoint(0,4));
        trap1Cell.add(new ChessboardPoint(1,3));

        trap2Cell.add(new ChessboardPoint(7,3));
        trap2Cell.add(new ChessboardPoint(8,2));
        trap2Cell.add(new ChessboardPoint(8,4));

    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void initPieces() {
        //
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].removePiece();
            }
        }

        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant",8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant",8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE,"Lion",7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED,"Lion",7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE,"Tiger",6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED,"Tiger",6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE,"Leopard",5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED,"Leopard",5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE,"Wolf",4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED,"Wolf",4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE,"Dog",3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED,"Dog",3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE,"Cat",2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED,"Cat",2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE,"Rat",1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED,"Rat",1));


    }

    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    public int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece)//把在point点的设成chesspiece
    {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) //搞懂
    {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        setChessPiece(dest, removeChessPiece(src));
        //先判断胜利
        ifTrap(getChessPieceAt(dest),dest);
        //把终点设置成被remove之前的src 顺便remove掉src
    }

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        // TODO: Finish the method.
        else
        {
            ChessPiece c1=getChessPieceAt(src);
            ChessPiece c2=getChessPieceAt(dest);
            if(c1.canCapture(c2))
            {
                //把c1移动到c2
                    setChessPiece(dest, removeChessPiece(src));
                    //先判断胜利
                    ifTrap(getChessPieceAt(dest),dest);
//                    return true;//如果吃掉了就返回true
                //现在c2剩下了什么呢
            }
        }

    }
    public boolean realcapture(ChessboardPoint src, ChessboardPoint dest)
    {
        return (!isValidCapture(src, dest))&(getChessPieceAt(src).canCapture(getChessPieceAt(dest)));
        //捕杀方式合法而且等级判断正确
    }

    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest)
    {
        if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null)
        {
            return false;
        }
        else//起点时有子的 终点没子
        {
            if(dens1Cell.contains(dest)&&getChessPieceAt(src).getOwner().equals(PlayerColor.RED))//红方进自家兽穴的移动是不被允许的
            {
                return false;
            }
            else if(dens2Cell.contains(dest)&&getChessPieceAt(src).getOwner().equals(PlayerColor.BLUE))//蓝方想进自家兽穴的移动是不被允许的
            {
                return false;
            }
            else
            {
                int distance = calculateDistance(src, dest);
                ChessPiece c1 = getChessPieceAt(src);
                if (!c1.getName().equals("Rat") && riverCell.contains(dest))//不是老鼠 点河的任何一块都不行
                {
                    return false;
                }
                else if (c1.getName().equals("Lion") || c1.getName().equals("Tiger"))//狮和虎能跳河
                {
                    //路上全是河流
                    int x1 = src.getCol();
                    int y1 = src.getRow();
                    int x2 = dest.getCol();
                    int y2 = dest.getRow();
                    if ((!riverCell.contains(src)) && (!riverCell.contains(dest)))//两端是陆地
                    {
                        if (x1 == x2 && Math.abs(y1 - y2) == 4) {
                            for (int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++) {
                                ChessboardPoint routine = new ChessboardPoint(i, x1);

                                if (getChessPieceAt(routine) != null) //路上有东西
                                {
                                    if(!riverCell.contains(routine))
                                    {
                                        return false;
                                    }
                                    if (getChessPieceAt(routine).getName().equals("Rat")) {
                                        return false;
                                    }
                                }
                                else
                                {
                                    if(!riverCell.contains(routine))
                                    {
                                        return false;
                                    }
                                }

                            }
//                            ifTrap(getChessPieceAt(src),dest);
                            return true;
                        } else if (y1 == y2 && Math.abs(x1 - x2) == 3) {
                            for (int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++) {
                                ChessboardPoint routine = new ChessboardPoint(y1, i);
                                if (getChessPieceAt(routine) != null) {
                                    if(!riverCell.contains(routine))
                                    {
                                        return false;
                                    }

                                    if (getChessPieceAt(routine).getName().equals("Rat")) {
                                        return false;
                                    }
                                }
                                else
                                {
                                    if(!riverCell.contains(routine))
                                    {
                                        return false;
                                    }
                                }

                            }
                            //ifTrap(getChessPieceAt(src),dest);
                            return true;
                        } else
                        {
                            if(distance == 1)
                            {
                                //ifTrap(getChessPieceAt(src),dest);
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    }
                    else//一边是陆地一边是河
                    {
                        return false;
                    }
                }
                else
                {
                    if(distance == 1)
                    {
                        //ifTrap(getChessPieceAt(src),dest);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) //true是抓不了
    {
        // TODO:Fix this method
        //合法移动
        ChessPiece c1=getChessPieceAt(src);
        ChessPiece c2=getChessPieceAt(dest);
        if(calculateDistance(src, dest) == 1)//面对面
        {
            //水中抓陆地不可以  陆地抓水中也不行 水中抓水中可以 陆地抓陆地可以
            if(riverCell.contains(src)&&(!riverCell.contains(dest)))
            {
                return true;
            }
            else if(riverCell.contains(dest)&&(!riverCell.contains(src)))
            {
                return true;
            }
            else
            {

                return false;
            }
        }
        else if(c1.getName().equals("Lion")||c1.getName().equals("Tiger"))//特殊情况 狮虎跳杀
        {
            //狮子和老虎的跳杀 在可杀情况返回false
            int x1=src.getCol();int y1=src.getRow();
            int x2=dest.getCol();int y2=dest.getRow();
            if((!riverCell.contains(src))&&(!riverCell.contains(dest)))//两端都是陆地
                //中间是陆地什么都没有也能跳杀
            {

                if(x1==x2&&Math.abs(y1-y2)==4)//竖着跳河
                {
                    for (int i=Math.min(y1,y2)+1;i<Math.max(y1,y2);i++)
                    {
                        ChessboardPoint routine=new ChessboardPoint(i,x1);
                        if(getChessPieceAt(routine)!=null)
                        {
                            if(getChessPieceAt(routine).getName().equals("Rat"))//有rat就不能抓
                            {
                                return true;
                            }
                            if(!riverCell.contains(routine))
                            {
                                return true;
                            }

                        }
                        else//没有棋子
                        {
                            if(!riverCell.contains(routine))
                            {
                                return true;
                            }
                        }
                    }

                    return false;//没有rat就能抓
                }
                else if(y1==y2&&Math.abs(x1-x2)==3)
                {
                    for (int i=Math.min(x1,x2)+1;i<Math.max(x1,x2);i++)
                    {
                        ChessboardPoint routine=new ChessboardPoint(y1,i);
                        if(getChessPieceAt(routine)!=null)
                        {
                            if(!riverCell.contains(routine))
                            {
                                return true;
                            }
                            if(getChessPieceAt(routine).getName().equals("Rat"))//有rat就不能抓
                            {
                                return true;
                            }

                        }
                        else
                        {
                            if(!riverCell.contains(routine))
                            {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            }
        }
        return true;
    }
    public Set<ChessboardPoint> getTrap1Cell() {
        return trap1Cell;
    }

    public Set<ChessboardPoint> getTrap2Cell() {
        return trap2Cell;
    }

    private void ifTrap(ChessPiece chessPiece,ChessboardPoint chessboardPoint)
    {
        //如果在陷阱设成0
        if(chessPiece.getOwner().equals(PlayerColor.BLUE)&&trap1Cell.contains(chessboardPoint))
        {
            chessPiece.setRank(0);
        }
        else if(chessPiece.getOwner().equals(PlayerColor.RED)&&trap2Cell.contains(chessboardPoint))
        {
            chessPiece.setRank(0);
        }
        //如果不在陷阱就
        else{
            switch(chessPiece.getName()){
                case "Rat":
                    chessPiece.setRank(1);
                    break;
                case "Cat":
                    chessPiece.setRank(2);
                    break;
                case "Dog":
                    chessPiece.setRank(3);
                    break;
                case "Wolf":
                    chessPiece.setRank(4);
                    break;
                case "Leopard":
                    chessPiece.setRank(5);
                    break;
                case "Tiger":
                    chessPiece.setRank(6);
                    break;
                case "Lion":
                    chessPiece.setRank(7);
                    break;
                case "Elephant":
                    chessPiece.setRank(8);
                    break;
            }
        }
    }

    public boolean judgeDens1Cell() {
        return grid[0][3].getPiece()!=null;//不是空的返回true
    }

    public boolean judgeDens2Cell() {
        return grid[8][3].getPiece()!=null;
    }


    public Chessboard clone()
    {
        Chessboard chessboardclone=new Chessboard();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                chessboardclone.grid[i][j].removePiece();
            }
        }
        //上面是一个什么都没有的空棋盘，你现在要把当前chessboard里面的东西给到这个空棋盘里面
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if(this.grid[i][j].getPiece()!=null) {
                    chessboardclone.grid[i][j].setPiece(this.grid[i][j].getPiece().clone());
                }
            }
        }
        return chessboardclone;

    }
}
