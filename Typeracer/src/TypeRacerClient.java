import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;

public class TypeRacerClient extends JFrame{
    JButton button = null;
    JPanel p = null;
    JTextField input = null;
    JLabel scoreLabel = null;
    JLabel missedLabel = null;
    JLabel gameOver = null;
    int score = 0;
    int missed = 0;
    ArrayList<String> vocabulary;
    GameRunning word1 = null;
    GameRunning word2 = null;
    GameRunning word3 = null;
    GameRunning word4 = null;
    int i = 0;
    private static final String FILENAME = "E:\\Back-End\\Java\\Eclipse\\eclipse\\Typeracer\\src\\words.txt";
    Property tmp = null;
    private Socket socket = null;
    boolean running = true;
    public ObjectOutputStream outputStream = null;
    
    public TypeRacerClient(){
        super("TypeRacerClient");
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
        
        p = new GameBoard();
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
        input.getDocument().addDocumentListener(new CheckWord());
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
        
        vocabulary = new ArrayList<String>();
        
        word1 = new GameRunning();
        word2 = new GameRunning();
        word3 = new GameRunning();
        word4 = new GameRunning();
        
        word1.st = null;
        word2.st = null;
        word3.st = null;
        word4.st = null;
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        show();
    }
    class GameBoard extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            
            Font f = new Font("Arial", Font.BOLD, 20);
            FontMetrics fm = this.getFontMetrics(f);
            g2.setColor(Color.WHITE);
            g2.setFont(f);
            if(word1.st != null){
                g2.drawString(word1.st, word1.x, word1.y);
            }
            if(word2.st != null){
                g2.drawString(word2.st, word2.x, word2.y);
            }
            if(word3.st != null){
                g2.drawString(word3.st, word3.x, word3.y);
            }
            if(word4.st != null){
                g2.drawString(word4.st, word4.x, word4.y);
            }
        }
    }
    
    class GameRunning implements Runnable{
        String st = null;
        int x = 1, y = 2;
        
        public void run(){
            try{
                Random rand = null;
                while(running){
                	// If missed 10 times
                	if (missed == 4) {
                    	running = false;
                    	try {
        					outputStream = new ObjectOutputStream(socket.getOutputStream());
        					Property property = new Property(missed, score);
        					System.out.println("Object to be written " + property);
        					outputStream.writeObject(property);
        				} catch (IOException e1) {
        					e1.printStackTrace();
        				}
                    	p.setBackground( Color.WHITE );
                    	input.setVisible(false);
                    	gameOver.setVisible(true);
                    }
                    if(x <= 0 || (st == null)){
                    	rand = new Random();
                    	Thread.currentThread().sleep(rand.nextInt(500));
                    	st = vocabulary.get(rand.nextInt(19));
                        if (x <= 0){
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
//            if(e.getActionCommand() == "START GAME"){
                Thread t1 = new Thread(word1);
                Thread t2 = new Thread(word2);
                Thread t3 = new Thread(word3);
                Thread t4 = new Thread(word4);
                t1.start();
                t2.start();
                t3.start();
                t4.start();
                button.setVisible(false);
                System.out.println("Connected");
                try {
					socket = new Socket("127.0.0.1", 4445);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//            }
        }
    }
    class CheckWord implements DocumentListener{
        public void changedUpdate(DocumentEvent e) {
        }
        public void removeUpdate(DocumentEvent e) {    
        }
        public void insertUpdate(DocumentEvent e) {
        	// Check input from user is it equal to word
            if(input.getText().equals(word1.st)){
                word1.st = null;
                word1.x = p.getWidth();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word2.st)){
                word2.st = null;
                word2.x = p.getHeight();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word3.st)){
                word3.st = null;
                word3.x = p.getHeight();
                score++;
                scoreLabel.setText("Score: " + score);
            }
            if(input.getText().equals(word4.st)){
                word4.st=null;
                word4.x = p.getHeight();
                score++;
            }
        }
    }
    public void showReadFile(){
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
            	vocabulary.add(i, sCurrentLine);
            	i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args){
        TypeRacerClient game = new TypeRacerClient();
        game.showReadFile();
    }
}