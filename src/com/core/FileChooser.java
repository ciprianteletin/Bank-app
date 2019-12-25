package com.core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class FileChooser {
    private JFrame display;
    private JButton chooseFile,print;
    private JLabel path,pathName;
    private JTextField name;
    private JFileChooser chooser;
    private JPanel choosePanel,printPanel,pathPanel;
    private String IBAN,fullname,bank,type,oldPath;

    public FileChooser(String IBAN,String fullname,String bank,String type){
        this.IBAN=IBAN;
        this.fullname=fullname;
        this.bank=bank;
        this.type=type;
        display=new JFrame("Print PDF");
        display.getContentPane().setBackground(new Color(56,56,56));
        display.setSize(500,170);
        display.setLayout(new GridLayout(3,1));
        display.setLocationRelativeTo(null);
        display.setVisible(true);
        display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.makePathPanel();
        this.makeChoosePanel();
        this.makePrintPanel();
        this.makeFileChooser();
        this.nameListener();
        this.makePDF();
    }

    private void makePathPanel(){
        pathPanel=new JPanel();
        pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pathPanel.setBackground(new Color(56,56,56));
        path=new JLabel("Path: ");
        path.setForeground(new Color(255,255,255));
        pathPanel.add(path);
        pathName=new JLabel();
        pathName.setForeground(new Color(255,255,255));
        pathPanel.add(pathName);
        pathPanel.setPreferredSize(new Dimension(550,100));
        display.add(pathPanel);
    }

    private void makeChoosePanel(){
        choosePanel=new JPanel();
        choosePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        choosePanel.setBackground(new Color(56,56,56));
        choosePanel.setSize(550,100);
        name=new JTextField();
        name.setPreferredSize(new Dimension(440,35));
        name.setEnabled(false);
        chooseFile=new JButton();
        chooseFile.setIcon(new ImageIcon("/home/cipri/Downloads/floppy-disk.png"));
        chooseFile.setBorderPainted(false);
        chooseFile.setFocusPainted(false);
        chooseFile.setBackground(new Color(56,56,56));
        chooseFile.setForeground(new Color(255,255,255));
        chooseFile.setPreferredSize(new Dimension(45,35));
        choosePanel.add(chooseFile);
        choosePanel.add(name);
        display.add(choosePanel);
    }

    private void makePrintPanel(){
        printPanel=new JPanel();
        printPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        printPanel.setBackground(new Color(56,56,56));
        print=new JButton("Print");
        print.setBorder(new LineBorder(Color.BLACK));
        print.setFocusPainted(false);
        print.setPreferredSize(new Dimension(75,50));
        print.setForeground(new Color(255,255,255));
        print.setBackground(new Color(56,56,56));
        printPanel.add(print);
        print.setEnabled(false);
        printPanel.setPreferredSize(new Dimension(550,125));
        display.add(printPanel);
    }

    private void makeFileChooser(){
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooser=new JFileChooser();
                chooser.setCurrentDirectory(new File("~"));
                chooser.setDialogTitle("Save file");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result=chooser.showOpenDialog(null);
                if(result==JFileChooser.APPROVE_OPTION) {
                    pathName.setText(chooser.getSelectedFile().getAbsolutePath()+"/");
                    oldPath=pathName.getText();
                    name.setEnabled(true);
                }
            }
        });
    }

    private void nameListener(){
        name.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c==8);
                else
                    keyEvent.consume();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                char c=keyEvent.getKeyChar();
                if((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c==8){
                    if(name.getText().length()>0){
                        print.setEnabled(true);
                        String pth=pathName.getText();
                        pathName.setText(oldPath+name.getText());
                    }else{
                        pathName.setText(oldPath);
                        print.setEnabled(false);
                    }
                }else
                    keyEvent.consume();
            }
        });
    }

    private void makePDF(){
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PDF pdf=new PDF(IBAN,fullname,bank,type,pathName.getText());
                pdf.createDoc();
                JOptionPane.showMessageDialog(null,"Your pdf is ready to be printed!");
                display.dispose();
            }
        });
    }
}
