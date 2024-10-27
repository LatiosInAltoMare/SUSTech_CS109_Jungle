package view;


import controller.GameController;
import model.*;
import view.Components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class represents the checkerboard component object on the panel
 */
public class ChessboardComponent extends JComponent implements Serializable {
    private  CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];
    private final int CHESS_SIZE;
    private final Set<ChessboardPoint> riverCell = new HashSet<>();//设置河水点集 用hash码存储
    private final Set<ChessboardPoint> dens1Cell = new HashSet<>();
    private final Set<ChessboardPoint> dens2Cell = new HashSet<>();
    private final Set<ChessboardPoint> trap1Cell = new HashSet<>();
    private final Set<ChessboardPoint> trap2Cell = new HashSet<>();

    private ChessGameFrame chessGameFrame;
    private GameController gameController;

    public ChessboardComponent(int chessSize) {
        CHESS_SIZE = chessSize;
        int width = CHESS_SIZE * 7;
        int height = CHESS_SIZE * 9;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);// Allow mouse events to occur
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        System.out.printf("chessboard width, height = [%d : %d], chess size = %d\n", width, height, CHESS_SIZE);

        initiateGridComponents();//初始化棋盘内容
    }


    /**
     * This method represents how to initiate ChessComponent
     * according to Chessboard information
     */
    public void initiateChessComponent(Chessboard chessboard) {
        Cell[][] grid = chessboard.getGrid();//把chessboard的private cell二维数组名为grid

        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                // TODO: Implement the initialization checkerboard
                if (grid[i][j].getPiece() != null) {
//                    removeChessComponentAtGrid(new ChessboardPoint(i,j));//
                    ChessPiece chessPiece = grid[i][j].getPiece();
                    System.out.println(chessPiece.getOwner());
                    if(grid[i][j].getPiece().getName().equals("Elephant")) {
                        gridComponents[i][j].add(
                                new ElephantChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Lion")){
                        gridComponents[i][j].add(
                                new LionChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Tiger")) {
                        gridComponents[i][j].add(
                                new TigerChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Leopard")) {
                        gridComponents[i][j].add(
                                new LeopardChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Wolf")) {
                        gridComponents[i][j].add(
                                new WolfChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Dog")) {
                        gridComponents[i][j].add(
                                new DogChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Cat")) {
                        gridComponents[i][j].add(
                                new CatChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }else if(grid[i][j].getPiece().getName().equals("Rat")) {
                        gridComponents[i][j].add(
                                new MouseChessComponent(
                                        chessPiece.getOwner(),
                                        CHESS_SIZE));
                    }
                }
            }
        }

    }

    public void initiateGridComponents()//初始化
    {

        riverCell.clear();
        dens1Cell.clear();
        dens2Cell.clear();
        trap1Cell.clear();
        trap2Cell.clear();

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


        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint temp = new ChessboardPoint(i, j);//chessboardpoint在i行j列 大致猜测为棋子位置
                CellComponent cell;
                if (riverCell.contains(temp))//画河的颜色的棋子
                {
                    cell = new CellComponent(Color.CYAN, calculatePoint(i, j), CHESS_SIZE);
                    this.add(cell);
                }
                else //画非河流颜色的棋子
                {
                    if(dens1Cell.contains(temp))//兽穴
                    {
                        cell = new CellComponent(Color.MAGENTA, calculatePoint(i, j), CHESS_SIZE);
                        this.add(cell);
                    }
                    else if (trap1Cell.contains(temp))//陷阱
                    {
                        cell = new CellComponent(Color.YELLOW, calculatePoint(i, j), CHESS_SIZE);
                        this.add(cell);
                    }
                    else if(dens2Cell.contains(temp))//兽穴
                    {
                        cell = new CellComponent(Color.MAGENTA, calculatePoint(i, j), CHESS_SIZE);
                        this.add(cell);
                    }
                    else if (trap2Cell.contains(temp))//陷阱
                    {
                        cell = new CellComponent(Color.YELLOW, calculatePoint(i, j), CHESS_SIZE);
                        this.add(cell);
                    }
                    else//陆地
                    {
                        cell = new CellComponent(Color.LIGHT_GRAY, calculatePoint(i, j), CHESS_SIZE);
                        this.add(cell);
                    }
                }
                //calculatepoint是计算位置画上背景图层
                gridComponents[i][j] = cell;
            }
        }
    }

    public void registerController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setChessComponentAtGrid(ChessboardPoint point, ChessComponent chess) {
        getGridComponentAt(point).add(chess);
    }

    public ChessComponent removeChessComponentAtGrid(ChessboardPoint point) {
        // Note re-validation is required after remove / removeAll.
        ChessComponent chess = (ChessComponent) getGridComponentAt(point).getComponents()[0];
        getGridComponentAt(point).removeAll();
        getGridComponentAt(point).revalidate();
        chess.setSelected(false);
        return chess;
    }

    public CellComponent getGridComponentAt(ChessboardPoint point) {
        return gridComponents[point.getRow()][point.getCol()];
    }

    private ChessboardPoint getChessboardPoint(Point point) {
        System.out.println("[" + point.y/CHESS_SIZE +  ", " +point.x/CHESS_SIZE + "] Clicked");
        return new ChessboardPoint(point.y/CHESS_SIZE, point.x/CHESS_SIZE);
    }
    public Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    //鼠标点击事件
    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            JComponent clickedComponent = (JComponent) getComponentAt(e.getX(), e.getY());//拿到的是jcomponent类型

            if (clickedComponent.getComponentCount() == 0) //无子
                //需判断是否是河流
            {
                System.out.print("None chess here and ");

                gameController.onPlayerClickCell(getChessboardPoint(e.getPoint()), (CellComponent) clickedComponent);
            }
            else
                //需判断可否吞噬
            {
                System.out.print("One chess here and ");

                gameController.onPlayerClickChessPiece(getChessboardPoint(e.getPoint()), (ChessComponent) clickedComponent.getComponents()[0]);
            }
            System.out.printf("Now it is turn %d\n",getGameController().getTurns());
        }

        if(e.getID()==MouseEvent.MOUSE_ENTERED)
        {
            CellComponent enteredComponent = (CellComponent) ((JComponent) getComponentAt(e.getX(), e.getY()));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent f) {
                    System.out.println("y");
                    enteredComponent.setShowImage(true);
                    enteredComponent.repaint();

                }
                public void mouseExited(MouseEvent f) {
                    System.out.println("n");
                    enteredComponent.setShowImage(false);
                    enteredComponent.repaint();
                    // You can add more tasks here
                }

            });

        }
    }





    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public void clear(Chessboard chessboard)
    {
        Cell[][] grid = chessboard.getGrid();
        for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                if(grid[i][j].getPiece()!=null) {
                    removeChessComponentAtGrid(new ChessboardPoint(i, j));
                    repaint();
                }
            }
        }
    }

    public ChessGameFrame getChessGameFrame() {
        return chessGameFrame;
    }

    public CellComponent getGridComponents(int i,int j) {
        return gridComponents[i][j];
    }
}
