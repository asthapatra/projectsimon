import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Simon
{
	private JFrame mainFrame;			
	private JPanel displayPanel;
	private JPanel keyPanel;
	private JPanel infoPanel;
	private JPanel ctrlPanel;
	
	private JPanel colorPanel[];
	private JButton keyButton[];
	private JButton continueButton;
	private JButton quitButton;
	private JTextArea infoField;
	private JTextArea infoField2;
	private JTextArea infoField3;
	private JTextArea infoField4;
	
	private final int ColorNum = 4;
	private final int Red = 0;
	private final int Blue = 1;
	private final int Green = 2;
	private final int Yellow = 3;

	private Vector<Integer> colorSeq;
	private Vector<Integer> inputSeq;
	private int colorSeqLength;
	private volatile int currentInput;
	
	private volatile boolean continueFlag;
	
	private static final String START_GAME_MESSAGE = "How to play: It will display a certain color pattern and the main objective is to remember the pattern."
			+ " If it is right, click continue to increase the difficulty. ";
	private static final String START_GAME_MESSAGE2 = "		PATTERN GAME: by Astha Patra		";
	private static final String START_GAME_MESSAGE3 = "	How to play: The patterns will display a certain color pattern based on the level";
	private static final String START_GAME_MESSAGE4 = "	";
	private static final String WIN_GAME_MESSAGE = "Yes you got it right! ";
	private static final String LOSE_GAME_MESSAGE = "Sorry, you lose !";
	private static final String GAME_LEVEL_MESSAGE = "Current Game level is :";
		
	
	public Simon()
	{
		draw();
		setFunction();
		colorSeq = new Vector<Integer>();
		inputSeq = new Vector<Integer>();
		colorSeqLength = ColorNum;
		currentInput = 0;
		continueFlag = false;
	}

	private void draw(){
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initDisplayPanel();
		initKeyPanel();
		initInfoPanel();
		
		Container contentPane = mainFrame.getContentPane();
		contentPane.add(displayPanel, BorderLayout.NORTH);
		contentPane.add(keyPanel, BorderLayout.CENTER);
		contentPane.add(infoPanel, BorderLayout.SOUTH);
		
		mainFrame.pack();
		mainFrame.setVisible(true);		
	}
	
	private void initDisplayPanel(){
		displayPanel = new JPanel();
		colorPanel = new JPanel[ColorNum];
		
		displayPanel.setLayout(new GridLayout(1,4));
		
		for(int color = 0; color < ColorNum; color++)
		{
			colorPanel[color] = new JPanel();
			displayPanel.add(colorPanel[color]);
		}
	}
	
	private void initKeyPanel(){
		keyPanel = new JPanel();
		keyPanel.setLayout(new GridLayout(1,4));
		
		keyButton = new JButton[ColorNum];
		
		keyButton[Red] = new JButton("Red");
		keyButton[Blue] = new JButton("Blue");
		keyButton[Green] = new JButton("Green");
		keyButton[Yellow] = new JButton("Yellow");
		          
		for(int color = 0; color < ColorNum; color++)
		{
			keyPanel.add(keyButton[color]);
		}
	}

	private void initInfoPanel(){
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoField = new JTextArea(10,25);
		infoField2 = new JTextArea(10,25);
		infoField3 = new JTextArea(10,25);
		infoField4 = new JTextArea(10,25);
		infoField.setText(START_GAME_MESSAGE);
		infoField2.setText(START_GAME_MESSAGE2);
		infoField3.setText(START_GAME_MESSAGE3);
		infoField4.setText(START_GAME_MESSAGE4);
		
		
		initCtrlPanel();
		

		infoPanel.add(infoField, BorderLayout.CENTER);
		infoPanel.add(infoField2, BorderLayout.NORTH);
		infoPanel.add(ctrlPanel, BorderLayout.SOUTH);		
	}
	
	private void initCtrlPanel(){
		ctrlPanel = new JPanel();
		
		continueButton = new JButton("Continue");		
		quitButton = new JButton("Quit");
	
		ctrlPanel.add(continueButton);
		ctrlPanel.add(quitButton);	
	}


	
	private void setFunction(){
		
		for(int color = 0; color < ColorNum; color++){
			keyButton[color].addActionListener(new keyClickListener(color));
		}
		
		continueButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				continueFlag = true;
				infoField.setText(GAME_LEVEL_MESSAGE + colorSeqLength);
			}	
		});
		
		quitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				System.exit(0);					
			}	
		});
	}


	
	public void play(){	
		while(true){			
			DisableKeyButton();
			waitForCtrl();
			displayColorSeq(colorSeqLength);
			System.out.println("Display over");
			EnableKeyButton();	
			
			input();
			showResult();
			DisableKeyButton();
		}
		
		
	}
		
	private void generateColorSeq(int seqLength){

		Random rd = new Random();
			colorSeq.clear();
		for(int i = 0; i< seqLength; i++){
			int a = rd.nextInt(ColorNum)%ColorNum;
			colorSeq.add(i,a);
			//System.out.println("The "+i+"th color is "+a );
		}
	}
	
	private void showColorSeq(int seqLength){
		try {
			for(int i = 0; i < seqLength; i++){
				for(int color = 0; color < ColorNum; color++){
					colorPanel[color].setBackground(Color.gray);
					
				}
			
				
				switch(colorSeq.elementAt(i)){
				case Red:
					colorPanel[Red].setBackground(Color.red);
					break;
				case Blue:
					colorPanel[Blue].setBackground(Color.blue);
					break;
				case Green:
					colorPanel[Green].setBackground(Color.green);
					break;
				case Yellow:
					colorPanel[Yellow].setBackground(Color.yellow);
					break;
				}

				Thread.sleep(300);
				
				//colorPanel[colorSeq.elementAt(i)].setBackground(Color.gray);
				Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void displayColorSeq(int seqLength){		
		generateColorSeq(seqLength);
		showColorSeq(seqLength);		
	}
	
	private boolean compare(Vector<Integer> colorSeq, Vector<Integer> inputSeq, int length){
		boolean result = true;
		
		for(int i = 0; i< length; i++){
			if(colorSeq.elementAt(i)!= inputSeq.elementAt(i))
			{
				result = false;
				break;
			}
		}
		return result;
	}
	
	private void showResult(){
		if(compare(colorSeq, inputSeq, colorSeqLength)){
			// show  
			infoField.setText(WIN_GAME_MESSAGE);
			colorSeqLength++;	
		}
		else{
			infoField.setText(LOSE_GAME_MESSAGE);
			colorSeqLength = ColorNum;	
		}	
	}
	
	private void input(){
		currentInput =0;
		inputSeq.clear();
		while(currentInput < colorSeqLength){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void DisableKeyButton(){
		for(int color = 0; color < ColorNum; color++){
			keyButton[color].setEnabled(false);
		}
		continueButton.setEnabled(true);
		quitButton.setEnabled(true);
	}
	
	public void EnableKeyButton(){
		for(int color = 0; color < ColorNum; color++){
			keyButton[color].setEnabled(true);
		}
		continueButton.setEnabled(false);
		quitButton.setEnabled(false);
		continueFlag = false;
	}
	
	private void waitForCtrl(){
		while(!continueFlag){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	

	class keyClickListener implements ActionListener{
		public int colorInfo = 0 ;
		keyClickListener(int color){
			colorInfo = color;			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			inputSeq.add(currentInput, colorInfo);
			currentInput++;	
		}	
	}
	
	
}
