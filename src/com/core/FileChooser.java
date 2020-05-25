package com.core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * Clasa FileChooser este folosita in momentul apasarii butonului de print(imprimanta mai exact) din cadrul interfetei grafice
 * ce afiseaza IBAN-ul si datele cardului curent;
 */
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
        choosePanel.setPreferredSize(new Dimension(550,100));
        name=new JTextField();
        name.setPreferredSize(new Dimension(400,35));
        name.setEnabled(false);
        name.setBackground(new Color(255,50,50));
        chooseFile=new JButton();
        chooseFile.setIcon(new ImageIcon("D:\\Aplicatie Bancara\\src\\imagini\\floppy-disk.png"));
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

    /**
     * Creearea unui FileChooser pentru a permite utilizatorului sa aleaga locul unde se va salva fisierul
     * Pentru acest FileChooser am ales actiunea de selectare doar a directoarelor(nu sunt necesare fisierele)
     */
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
                    pathName.setText(chooser.getSelectedFile().getAbsolutePath()+"\\");
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
    /**
    Metoda pentru verificarea existentei unui fisier cu numele selectat de utilizator, capacitatea de a-l suprascrie, cat si crearea
     documentului propriu-zis prin instantierea unui obiect de tip PDF si apelarea metodei createDoc().
     */
    private void makePDF(){
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File f=new File(pathName.getText()+".pdf");
                boolean create=true;
                if(f.exists()){
                    int result=JOptionPane.showConfirmDialog(null,"The file already exists!Do " +
                            "you want to override it?","Existent file",JOptionPane.YES_NO_OPTION);
                    if(result==JOptionPane.YES_OPTION){
                        if(!f.delete()){
                            JOptionPane.showMessageDialog(null,"Failed to delete the actual file," +
                                    " please choose another name for your PDF");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Choose another name!");
                        create=false;
                    }
                }
                if(create) {
                    PDF pdf = new PDF(IBAN, fullname, bank, type, pathName.getText()+".pdf");
                    pdf.createDoc();
                    JOptionPane.showMessageDialog(null, "Your pdf is ready to be printed!");
                    display.dispose();
                }
            }
        });
    }
}
