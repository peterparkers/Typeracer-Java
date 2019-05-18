import java.awt.*;
import java.awt.event.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;

public class TypeRacer extends JFrame{
    JButton button = null;
    JPanel p = null;
    JTextField input = null;
    JLabel scoreLabel = null;
    JLabel missedLabel = null;
    JLabel gameOver = null;
    int score = 0;
    int missed = 0;
    ArrayList<String> wordlist;
    WordRun word1 = null;
    WordRun word2 = null;
    WordRun word3 = null;
    WordRun word4 = null;
    private ServerSocket server = null;
    private Socket socket = null;
    boolean running = true;
    public ObjectInputStream inStream = null;
    public Property property = null;
    
    public TypeRacer(){
        super("TypeRacerServer");
        setSize(500,500);
        setLayout(new BorderLayout());
        
        button = new JButton("START GAME");
        button.setPreferredSize(new Dimension(200,200));
        button.addActionListener(new ButtonHandler());
        button.setFont(new Font("Monospaced", Font.PLAIN, 40));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        add(button, BorderLayout.NORTH);
        
        p = new DrawPanel();
        p.setPreferredSize(new Dimension(300,300));
        p.setBackground( Color.BLACK );
        add(p, BorderLayout.CENTER);
        
        gameOver = new JLabel("GAME OVER");
        gameOver.setFont(new Font("Monospaced", Font.PLAIN, 40));
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
        gameOver.setVerticalAlignment(SwingConstants.CENTER);
        gameOver.setForeground(Color.BLACK);
        gameOver.setVisible(false);
        p.add(gameOver, BorderLayout.NORTH);
        
        JPanel p2=new JPanel();
        p2.setLayout(new BorderLayout());
        
        input = new JTextField(20);
        input.getDocument().addDocumentListener(new ListenText());
        p2.add(input, BorderLayout.NORTH);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalAlignment(SwingConstants.CENTER);
        p2.add(scoreLabel, BorderLayout.CENTER);
        
        missedLabel = new JLabel("Missed: 0");
        missedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        missedLabel.setVerticalAlignment(SwingConstants.CENTER);
        p2.add(missedLabel, BorderLayout.SOUTH);
        add(p2,BorderLayout.SOUTH);
        
        wordlist = new ArrayList<String>();
        wordlist.add(0, "hello");
        wordlist.add(1, "dog");
        wordlist.add(2, "what");
        wordlist.add(3, "cat");
        wordlist.add(4, "thai");
        wordlist.add(5, "mickey");
        wordlist.add(6, "holyshit");
        wordlist.add(7, "jarvis");
        wordlist.add(8, "batman");
        wordlist.add(9, "aboard");
        wordlist.add(10, "roger");
        
        word1 = new WordRun();
        word2 = new WordRun();
        word3 = new WordRun();
        word4 = new WordRun();
        
        word1.str = null;
        word2.str = null;
        word3.str = null;
        word4.str = null;
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        show();
    }
    class DrawPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            
            Font f = new Font("Arial", Font.BOLD, 20);
            FontMetrics fm = this.getFontMetrics(f);
            g2.setColor(Color.WHITE);
            g2.setFont(f);
            if(word1.str != null){
                g2.drawString(word1.str, word1.x, word1.y);
            }
            if(word2.str != null){
                g2.drawString(word2.str, word2.x, word2.y);
            }
            if(word3.str != null){
                g2.drawString(word3.str, word3.x, word3.y);
            }
            if(word4.str != null){
                g2.drawString(word4.str, word4.x, word4.y);
            }
        }
    }
    class WordRun implements Runnable{
        String str = null;
        int x = 1, y = 2;
        
        public void run(){
            try{
                Random rand = null;
                while(running){
                	// If missed 10 times
//                	if (socket != null) {
//                		inStream = new ObjectInputStream(socket.getInputStream());
//                        property = (Property) inStream.readObject();
//                	}
                	if (missed == 4) {
                    	running = false;
                    	p.setBackground( Color.WHITE );
                    	p.add(scoreLabel, BorderLayout.SOUTH);
                    	p.add(missedLabel, BorderLayout.SOUTH);
                    	input.setVisible(false);
                    	gameOver.setVisible(true);
//                    	System.out.println("Object received = " + property.getMissed());
//                    	socket.close();
                    }
                    if(x == 0 || (str == null)){
                    	rand = new Random();
                    	Thread.currentThread().sleep(rand.nextInt(500));
                    	str = wordlist.get(rand.nextInt(11));
                        if (x == 0){
	                        missed++;
                            missedLabel.setText("Missed: " + missed);
                        }
                        x = p.getWidth();
                        
                        // if word position out of Y-axis
                        do{
                            y = rand.nextInt(p.getHeight());
                        }while(y > p.getHeight() - 100);
                        
	                    input.setText("");
                    }
                    else{
                    	if(score < 20) {
                    		Thread.currentThread().sleep(30 - score);
                    	}
                    	else {
                    		Thread.currentThread().sleep(10);
                    	}
                        x -= 1;
                    }
                    repaint();
                }
            }catch(Exception e){
            	
            }    
        }
    }
    class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getActionCommand() == "START GAME"){
				try {
					server = new ServerSocket(4445);
	                System.out.println("Waiting for a client ...");
	                socket = server.accept();
					if (socket != null) {
		                Thread t1 = new Thread(word1);
		                Thread t2 = new Thread(word2);
		                Thread t3 = new Thread(word3);
		                Thread t4 = new Thread(word4);
		                t1.start();
		                t2.start();
		                t3.start();
		                t4.start();
		                button.setVisible(false);
		                System.out.println("connect");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        }
    }
    class ListenText implements DocumentListener{
        public void changedUpdate(DocumentEvent e) {
        }
        public void removeUpdate(DocumentEvent e) {    
        }
        public void insertUpdate(DocumentEvent e) {
        	// Check input from user is it equal to word
            if(input.getText().equals(word1.str)){
                word1.str = null;
                word1.x = p.getWidth();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word2.str)){
                word2.str = null;
                word2.x = p.getHeight();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word3.str)){
                word3.str = null;
                word3.x = p.getHeight();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word4.str)){
                word4.str=null;
                word4.x = p.getHeight();
                score++;
                scoreLabel.setText("Score: " + score);
            }
        }
    }
    public static void main(String[] args){
        TypeRacer game = new TypeRacer();
        
    }
}