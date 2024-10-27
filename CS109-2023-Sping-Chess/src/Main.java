import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;
import view.MainFrame;

import javax.swing.*;
import java.io.*;

public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = null;
            try {
                mainFrame = new MainFrame(500, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
//            try {
//                mainFrame =
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
            //建好的mainframe里面有Chesboardcomponet chessboardcomponent需要新建
            //GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            mainFrame.setVisible(true);
        });
    }
}
