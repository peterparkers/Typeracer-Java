import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;

public class TypeRacerServer extends JFrame{
    JButton button = null;
    JPanel p = null;
    JTextField input = null;
    JLabel scoreLabel = null;
    JLabel missedLabel = null;
    JLabel gameOver = null;
    JLabel gameStart = null;
    int score = 0;
    int missed = 0;
    private ServerSocket serverSocket = null;
	private Socket socket = null;
	private ObjectInputStream inStream = null;
//	private int count = 0;
    
    public TypeRacerServer(){
        super("TypeRacerServer");
        setSize(500,500);
        setLayout(new BorderLayout());
        
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(300,300));
        p.setBackground( Color.BLACK );
        add(p, BorderLayout.NORTH);
        
        gameStart = new JLabel("GAME");
        gameStart.setFont(new Font("Monospaced", Font.PLAIN, 40));
        gameStart.setHorizontalAlignment(SwingConstants.CENTER);
        gameStart.setVerticalAlignment(SwingConstants.CENTER);
        gameStart.setForeground(Color.WHITE);
        gameStart.setVisible(true);
        p.add(gameStart, BorderLayout.CENTER);
        
        gameOver = new JLabel("GAME OVER");
        gameOver.setFont(new Font("Monospaced", Font.PLAIN, 40));
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
        gameOver.setVerticalAlignment(SwingConstants.CENTER);
        gameOver.setForeground(Color.WHITE);
        gameOver.setVisible(false);
        p.add(gameOver, BorderLayout.NORTH);
        
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(200,180));
        p2.setBackground( Color.BLACK );
        
        scoreLabel = new JLabel("Score: 0 ");
        scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setVisible(true);
        p2.add(scoreLabel, BorderLayout.NORTH);
        
        missedLabel = new JLabel("Missed: 0");
        missedLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        missedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        missedLabel.setVerticalAlignment(SwingConstants.CENTER);
        missedLabel.setForeground(Color.WHITE);
        missedLabel.setVisible(true);
        p2.add(missedLabel, BorderLayout.CENTER);
        add(p2,BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        show();
    }
    public void communicate() {
//    	while(count < 4) {
			try {
				serverSocket = new ServerSocket(4445);
				socket = serverSocket.accept();
				gameStart.setText("GAME is Running");
				inStream = new ObjectInputStream(socket.getInputStream());
				Property property = (Property) inStream.readObject();
				
//				if (property.getMissed() < 4) {
					System.out.println("Object received = " + property);
					scoreLabel.setText("Score: " + property.getScore());
					missedLabel.setText("Missed: " + property.getMissed());
//				}
				if (property.getMissed() == 4) {
					gameOver.setVisible(true);
					gameStart.setVisible(false);
//					socket.close();
				}
//				count++;
			
			} catch (SocketException se) {
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException cn) {
				cn.printStackTrace();
			}
//    	}
	}
    public static void main(String[] args){
        TypeRacerServer game = new TypeRacerServer();
        game.communicate();
    }
}