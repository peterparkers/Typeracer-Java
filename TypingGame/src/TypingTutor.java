import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;

public class TypingTutor extends JFrame{
    JButton b=null;
    JPanel p=null;
    JTextField tf=null;
    JLabel l=null;
    JLabel l2=null;
    int score=0;
    int displayed=0;
    ArrayList<String> wordlist;
    WordRun word1=null;
    WordRun word2=null;
    WordRun word3=null;
    
    public TypingTutor(){
        super("Typing Tutor");
        setSize(400,400);
        setLayout(new BorderLayout());
        
        b=new JButton("Start");
        b.addActionListener(new ButtonHandler());
        add(b,BorderLayout.NORTH);
        
        p=new DrawPanel();
        p.setPreferredSize(new Dimension(300,300));
        add(p,BorderLayout.CENTER);
        
        JPanel p2=new JPanel();
        p2.setLayout(new FlowLayout());
        
        tf=new JTextField(10);
        tf.getDocument().addDocumentListener(new ListenText());
        p2.add(tf);

        l=new JLabel("0");
        p2.add(l);
        
        l2=new JLabel("0");
        p2.add(l2);
        
        add(p2,BorderLayout.SOUTH);
        
        wordlist=new ArrayList<String>();
        wordlist.add(0,"hello");
        wordlist.add(1,"dog");
        wordlist.add(2,"what");
        wordlist.add(3,"adsfasdf");
        wordlist.add(4,"thammasat");
        wordlist.add(5,"mickey");
        wordlist.add(6,"holyshit");
        wordlist.add(7,"jarvis");
        wordlist.add(8,"batman");
        wordlist.add(9,"aboard");
        wordlist.add(10, "MynameisPhurinatPuekkham");
        
        word1=new WordRun();
        word2=new WordRun();
        word3=new WordRun();
        
        word1.st=null;
        word2.st=null;
        word3.st=null;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        show();
    }
    class DrawPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            
            Font f=new Font("Monospaced",Font.BOLD,14);
            FontMetrics fm=this.getFontMetrics(f);
            g2.setColor(Color.BLACK);
            g2.setFont(f);
            if(word1.st!=null){
                g2.drawString(word1.st,word1.posx,word1.posy);
            }
            if(word2.st!=null){
                g2.drawString(word2.st,word2.posx,word2.posy);
            }
            if(word3.st!=null){
                g2.drawString(word3.st,word3.posx,word3.posy);
            }
        }
    }
    class WordRun implements Runnable{
        String st=null;
        int posx;
        float posy;
        
        public void run(){
            try{
                Random rand=null;
                while(true){
                    if((st==null)||((int)(posy)==p.getHeight())){
                        rand=new Random();
                        Thread.currentThread().sleep(rand.nextInt(1000));
                        st=wordlist.get(rand.nextInt(11));
                        posy=0;
                        do{
                            posx=rand.nextInt(p.getWidth());
                        }while(posx > (p.getWidth()-100));
	                        tf.setText("");
	                        displayed++;
	                        l2.setText(displayed+"");
                    }
                    else{
                        Thread.currentThread().sleep(20);
                        posy += 0.9;
                    } 
                    repaint();
                }
            }catch(Exception e){
            }    
        }
    }
    class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getActionCommand()=="Start"){
                Thread t1=new Thread(word1);
                Thread t2=new Thread(word2);
                Thread t3=new Thread(word3);
                t1.start();
                t2.start();
                t3.start();
            }
        }
    }
    class ListenText implements DocumentListener{
        public void changedUpdate(DocumentEvent e) {
        }
        public void removeUpdate(DocumentEvent e) {    
        }
        public void insertUpdate(DocumentEvent e) {
            if(tf.getText().equals(word1.st)){
                word1.st=null;
                word1.posy=0;
                score++;
                l.setText(score+"");
            }
            if(tf.getText().equals(word2.st)){
                word2.st=null;
                word2.posy=0;
                score++;
                l.setText(score+"");
            }
            if(tf.getText().equals(word3.st)){
                word3.st=null;
                word3.posy=0;
                score++;
                l.setText(score+"");
            }
        }
    }
    public static void main(String[] args){
        TypingTutor ap=new TypingTutor();
    }
}