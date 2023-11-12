package com.goreckia.game.plane;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements ActionListener, WindowListener
{
    private JLabel ipaddressLabel;
    private JLabel portLabel;
    private static JLabel scoreLabel;
    private JTextField ipaddressText;
    private JTextField portText;
    private JButton registerButton;
    private JPanel registerPanel;
    public static JPanel gameStatusPanel;
    private Client client;
    private Tank clientTank;
    private static int score;
    int width;
    int height;
    boolean isRunning;
    private GameBoardPanel boardPanel;
    private SoundManger soundManger;

    public ClientGUI() {
        this.width = 790;
        this.height = 580;
        this.isRunning = true;
        ClientGUI.score = 0;
        this.setTitle("Multiple clients Tank Fire");
        this.setSize(this.width, this.height);
        this.setLocation(60, 100);
        this.getContentPane().setBackground(Color.BLACK);
        this.setDefaultCloseOperation(3);
        this.setLayout(null);
        this.addWindowListener(this);
        (this.registerPanel = new JPanel()).setBackground(Color.BLUE);
        this.registerPanel.setSize(200, 140);
        this.registerPanel.setBounds(560, 50, 200, 140);
        this.registerPanel.setLayout(null);
        (ClientGUI.gameStatusPanel = new JPanel()).setBackground(Color.BLUE);
        ClientGUI.gameStatusPanel.setSize(200, 300);
        ClientGUI.gameStatusPanel.setBounds(560, 210, 200, 311);
        ClientGUI.gameStatusPanel.setLayout(null);
        (this.ipaddressLabel = new JLabel("IP address: ")).setBounds(10, 25, 70, 25);
        (this.portLabel = new JLabel("Port: ")).setBounds(10, 55, 50, 25);
        (ClientGUI.scoreLabel = new JLabel("Score : 0")).setBounds(10, 90, 100, 25);
        (this.ipaddressText = new JTextField("localhost")).setBounds(90, 25, 100, 25);
        (this.portText = new JTextField("11111")).setBounds(90, 55, 100, 25);
        (this.registerButton = new JButton("Register")).setBounds(60, 100, 90, 25);
        this.registerButton.addActionListener(this);
        this.registerButton.setFocusable(true);
        this.registerPanel.add(this.ipaddressLabel);
        this.registerPanel.add(this.portLabel);
        this.registerPanel.add(this.ipaddressText);
        this.registerPanel.add(this.portText);
        this.registerPanel.add(this.registerButton);
        ClientGUI.gameStatusPanel.add(ClientGUI.scoreLabel);
        this.client = Client.getGameClient();
        this.clientTank = new Tank();
        this.boardPanel = new GameBoardPanel(this.clientTank, this.client, false);
        this.getContentPane().add(this.registerPanel);
        this.getContentPane().add(ClientGUI.gameStatusPanel);
        this.getContentPane().add(this.boardPanel);
        this.setVisible(true);
    }

    public static int getScore() {
        return ClientGUI.score;
    }

    public static void setScore(final int scoreParametar) {
        ClientGUI.score += scoreParametar;
        ClientGUI.scoreLabel.setText("Score : " + ClientGUI.score);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object obj = e.getSource();
        if (obj == this.registerButton) {
            this.registerButton.setEnabled(false);
            try {
                this.client.register(this.ipaddressText.getText(), Integer.parseInt(this.portText.getText()), this.clientTank.getXposition(), this.clientTank.getYposition());
                this.soundManger = new SoundManger();
                this.boardPanel.setGameStatus(true);
                this.boardPanel.repaint();
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                new ClientGUI.ClientRecivingThread(this.client.getSocket()).start();
                this.registerButton.setFocusable(false);
                this.boardPanel.setFocusable(true);
            }
            catch (IOException ex2) {
                JOptionPane.showMessageDialog(this, "The Server is not running, try again later!", "Tanks 2D Multiplayer Game", 1);
                System.out.println("The Server is not running!");
                this.registerButton.setEnabled(true);
            }
        }
    }

    @Override
    public void windowOpened(final WindowEvent e) {
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        Client.getGameClient().sendToServer(new Protocol().ExitMessagePacket(this.clientTank.getTankID()));
    }

    @Override
    public void windowClosed(final WindowEvent e) {
    }

    @Override
    public void windowIconified(final WindowEvent e) {
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
    }

    @Override
    public void windowActivated(final WindowEvent e) {
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
    }

    public class ClientRecivingThread extends Thread
    {
        Socket clientSocket;
        DataInputStream reader;

        public ClientRecivingThread(final Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.reader = new DataInputStream(clientSocket.getInputStream());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (ClientGUI.this.isRunning) {
                String sentence = "";
                try {
                    sentence = this.reader.readUTF();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (sentence.startsWith("ID")) {
                    final int id = Integer.parseInt(sentence.substring(2));
                    ClientGUI.this.clientTank.setTankID(id);
                    System.out.println("My ID= " + id);
                }
                else if (sentence.startsWith("NewClient")) {
                    final int pos1 = sentence.indexOf(44);
                    final int pos2 = sentence.indexOf(45);
                    final int pos3 = sentence.indexOf(124);
                    final int x = Integer.parseInt(sentence.substring(9, pos1));
                    final int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
                    final int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
                    final int id2 = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));
                    if (id2 == ClientGUI.this.clientTank.getTankID()) {
                        continue;
                    }
                    ClientGUI.this.boardPanel.registerNewTank(new Tank(x, y, dir, id2));
                }
                else if (sentence.startsWith("Update")) {
                    final int pos1 = sentence.indexOf(44);
                    final int pos2 = sentence.indexOf(45);
                    final int pos3 = sentence.indexOf(124);
                    final int x = Integer.parseInt(sentence.substring(6, pos1));
                    final int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
                    final int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
                    final int id2 = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));
                    if (id2 == ClientGUI.this.clientTank.getTankID()) {
                        continue;
                    }
                    ClientGUI.this.boardPanel.getTank(id2).setXpoistion(x);
                    ClientGUI.this.boardPanel.getTank(id2).setYposition(y);
                    ClientGUI.this.boardPanel.getTank(id2).setDirection(dir);
                    ClientGUI.this.boardPanel.repaint();
                }
                else if (sentence.startsWith("Shot")) {
                    final int id = Integer.parseInt(sentence.substring(4));
                    if (id == ClientGUI.this.clientTank.getTankID()) {
                        continue;
                    }
                    ClientGUI.this.boardPanel.getTank(id).Shot();
                }
                else if (sentence.startsWith("Reset")) {
                    Integer.parseInt(sentence.substring(5));
                }
                else if (sentence.startsWith("Remove")) {
                    final int id = Integer.parseInt(sentence.substring(6));
                    if (id == ClientGUI.this.clientTank.getTankID()) {
                        final int response = JOptionPane.showConfirmDialog(null, "Sorry, You are loss. Do you want to try again ?", "Tanks 2D Multiplayer Game", 2);
                        if (response == 0) {
                            ClientGUI.this.setVisible(false);
                            ClientGUI.this.dispose();
                            new ClientGUI();
                        }
                        else {
                            System.exit(0);
                        }
                    }
                    else {
                        ClientGUI.this.boardPanel.removeTank(id);
                    }
                }
                else {
                    if (!sentence.startsWith("Exit")) {
                        continue;
                    }
                    final int id = Integer.parseInt(sentence.substring(4));
                    if (id == ClientGUI.this.clientTank.getTankID()) {
                        continue;
                    }
                    ClientGUI.this.boardPanel.removeTank(id);
                }
            }
            try {
                this.reader.close();
                this.clientSocket.close();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
