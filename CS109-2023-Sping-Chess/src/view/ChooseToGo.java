package view;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChooseToGo {
    public int playerColor;
//    public JButton red=new JButton("RED");
//    public JButton blue=new JButton("BLUE");
    public JFrame frame=new JFrame();
    public JComboBox<String> chooseRedUser=new JComboBox<String>();
    public JComboBox<String> chooseBlueUser=new JComboBox<String>();
    public JButton confirm=new JButton("Confirm");
    public ChooseToGo() {

//        red.setLocation(150,130);
//        red.setFont(new Font("Rockwell", Font.BOLD, 20));
//        red.setSize(200,60);
//        blue.setLocation(150,200);
//        blue.setFont(new Font("Rockwell", Font.BOLD, 20));
//        blue.setSize(200,60);
//        frame.add(red);
//        frame.add(blue);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        JLabel label=new JLabel("Please choose user");
        label.setSize(200,60);
        label.setFont(new Font("Rockwell", Font.BOLD, 20));
        label.setLocation(150,80);
        frame.add(label);
        //frame.setAlwaysOnTop(true);
        JLabel label1=new JLabel("RED");
        label1.setSize(200,60);
        label1.setFont(new Font("Rockwell", Font.BOLD, 20));
        label1.setLocation(100,150);
        frame.add(label1);
        JLabel label2=new JLabel("BLUE");
        label2.setSize(200,60);
        label2.setFont(new Font("Rockwell", Font.BOLD, 20));
        label2.setLocation(100,220);
        frame.add(label2);
        chooseRedUser.setSize(200,60);
        chooseRedUser.setLocation(180,150);
        chooseRedUser.setVisible(true);
        frame.add(chooseRedUser);
        chooseBlueUser.setSize(200,60);
        chooseBlueUser.setLocation(180,220);
        chooseBlueUser.setVisible(true);
        frame.add(chooseBlueUser);
        confirm.setVisible(true);
        confirm.setSize(200,60);
        confirm.setLocation(150,290);
        frame.add(confirm);
//        confirm.addActionListener(e -> {
//            frame.setVisible(false);
//        });
//        ArrayList<String> userName=new ArrayList<>();
//        int i=1;
//        while(i<= users.size()){
//            userName.add(users.get(i-1).getName());
//            i++;
//        }
//        for(String item:userName){
//            chooseRedUser.addItem(item);
//        }
//        chooseRedUser.addActionListener((e1 ->
//                userRed=users.get(chooseRedUser.getSelectedIndex())));
//        for(String item:userName){
//            chooseBlueUser.addItem(item);
//        }
//        chooseBlueUser.addActionListener((e1 ->
//                userBlue=users.get(chooseBlueUser.getSelectedIndex())));

    }
    public void addLabel1(){
        JLabel jLabel=new JLabel("DON'T PLAY WITH YOURSELF !!!");
        jLabel.setSize(400,60);
        jLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        jLabel.setLocation(100,340);
        frame.add(jLabel);
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }
}
