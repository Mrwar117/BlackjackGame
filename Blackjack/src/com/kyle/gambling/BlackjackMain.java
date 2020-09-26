package com.kyle.gambling;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BlackjackMain extends JFrame{
	private static final long serialVersionUID = 1819244246643281528L;
	private int chipStack= 25000;
	private int currentBet= 0;
	private JTextField textField;
	private boolean inHand = false;
	private JTextArea console;
	private int[] set = {1,2,3,4,5,6,7,8,9,10,10,10,10};
	private ArrayList<Integer> playDeck = new ArrayList<Integer>();
	private ArrayList<Integer> baseDeck = new ArrayList<Integer>();
	private ArrayList<Integer> backDeck = new ArrayList<Integer>();
	private ArrayList<Integer> playerHand = new ArrayList<Integer>();
	private ArrayList<Integer> dealerHand = new ArrayList<Integer>();
	private String[] commands = {"bet","deal","hit","stay","chips"};
	public static void main(String[] args) {
		new BlackjackMain();
	}
	public BlackjackMain(){
		for(int y=0;y<24;y++) {
			for(int x=0;x<13;x++) {
				baseDeck.add(set[x]);
				
			}
		}
		backDeck = baseDeck;
		System.out.println("Base Deck"+baseDeck);
		this.setSize(600, 650);
		this.setTitle("BlackJack");
		this.setLocation(450,250);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textField = new JTextField();
		textField.setEditable(false);
		textField.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event){
						int comint = 57;
						for(int x =0;x<commands.length;x++) {
							if (textField.getText().split(Pattern.quote(" "))[0].equalsIgnoreCase(commands[x])) {
								System.out.println("Command issued case "+x+" "+commands[x]);
								comint = x;
							}
						}
						switch (comint) {
							case 0:
								System.out.println("case 0");
								int x=0;
								try {
								x = Integer.parseInt(textField.getText().split(Pattern.quote(" "))[1]);
								}catch(Exception e) {
									e.printStackTrace();
									print("unable to process integer");
									textField.setText("");
									return;
								}
								if(x<=chipStack) {
									currentBet=x;
									print("Bet set to "+currentBet);
									print("You can change your bet or type Deal to play!");
									textField.setText("");
									return;
								}
								print("You do not have enough chips to make that bet!");
								print("Current Chipstack - "+chipStack);
								textField.setText("");
								return;
							case 1:
								System.out.println("case 1");
								if(inHand) {
									print("You are already in a hand.");
									textField.setText("");
									return;
								}
								if(currentBet==0) {
									print("Make a bet first with 'Bet _____'");
									textField.setText("");
									return;
								}
								inHand = true;
								shuffleDeck();
								dealHand();
								chipStack = chipStack-currentBet;
								print("Dealer Shows "+dealerHand.get(0));
								print("You Have "+playerHand.get(0)+"-"+playerHand.get(1)+" Total Value = "+checkHand(playerHand));
								textField.setText("");
								return;
							case 2:
								System.out.println("case 2");
								if(!inHand) {
									print("You are not in a hand!");
									textField.setText("");
									return;
								}
								if(checkHand(playerHand)>21) {
									print("You are already bust!");
									textField.setText("");
									return;
								}
								hitHand(0);
								print("Hit! You Have "+playerHand+" Total Value = "+checkHand(playerHand));
								if(checkHand(playerHand)>21) {
									stayHand();
									textField.setText("");
									return;
								}
								textField.setText("");
								return;
							case 3:
								System.out.println("case 3");
								if(!inHand) {
									print("You are not in a hand!");
									textField.setText("");
									return;
								}
								stayHand();
								textField.setText("");
								return;
							case 4:
								System.out.println("Case 4");
								print("Your stack currently stands at :"+chipStack);
								textField.setText("");
								return;
							default:
								System.out.println("DEFAULT");
								print("You enter bad command");
								return;
						}
						}
					});
		add(textField, BorderLayout.SOUTH);
		console = new JTextArea();
		console.setEditable(false);
		print("Welcome to Blackjack!");
		print("Type Bet _____ to begin! Current Chipstack - "+chipStack);
		console.setLineWrap(true);
		add(new JScrollPane(console), BorderLayout.CENTER);
		textField.setEditable(true);
		this.setVisible(true);
	}
	
	public void shuffleDeck() {
		baseDeck = backDeck;
		for(int x =0;x<baseDeck.toArray().length;x++) {
			int y = ThreadLocalRandom.current().nextInt(0,baseDeck.toArray().length);
			playDeck.add(baseDeck.get(y));
			baseDeck.remove(y);
			
		}
		System.out.println("Post Shuffle "+playDeck);
	}
	public void dealHand() {
		playerHand.add(playDeck.get(0));
		playDeck.remove(0);
		dealerHand.add(playDeck.get(0));
		playDeck.remove(0);
		playerHand.add(playDeck.get(0));
		playDeck.remove(0);
		dealerHand.add(playDeck.get(0));
		playDeck.remove(0);
		}
	public void hitHand(int hand) {
		switch(hand) {
		case 0:
			playerHand.add(playDeck.get(0));
			playDeck.remove(0);
			return;
		case 1:
			dealerHand.add(playDeck.get(0));
			playDeck.remove(0);
			return;
		}
		
	}
	public void stayHand() {
		int playerValue = checkHand(playerHand);
		boolean playerBust = false;
		boolean dealerBust = false;
		if(playerValue<=21) {
		print("You stay on "+playerValue);
		}else {
			print("You bust on "+playerValue);
			playerBust=true;
		}
		while(checkHand(dealerHand)<17) {
			hitHand(1);
			print("Dealer hits, Dealer now has "+dealerHand+" Total value - "+checkHand(dealerHand));
			if(checkHand(dealerHand)>=17) {
				break;
			}
		}
		int dealerValue = checkHand(dealerHand);
		if(dealerValue<=21) {
			print("Dealer stays on "+dealerValue);
			}else {
				print("Dealer busts on "+dealerValue);
				dealerBust=true;
			}
		if(playerBust) {
			currentBet = 0;
			inHand = false;
			clearHands();
			return;
		}
		if(dealerBust && !playerBust) {
			if(playerValue ==21 && playerHand.size()==2) {
				print("Blackjack pays 3:2");
				chipStack = chipStack+(currentBet*2);
				chipStack = chipStack+(currentBet/2);
				currentBet = 0;
				inHand = false;
				clearHands();
				return;
			}
			print("You win with "+playerValue);
			chipStack = chipStack+(currentBet*2);
			currentBet =0;
			inHand = false;
			clearHands();
			return;
		}
		if(playerValue>dealerValue) {
			print("You win with "+playerValue);
			chipStack = chipStack+(currentBet*2);
			currentBet =0;
			inHand = false;
			clearHands();
			return;
		}
		if(playerValue ==dealerValue) {
			print("Push on "+playerValue);
			chipStack = chipStack+currentBet;
			currentBet =0;
			inHand = false;
			clearHands();
			return;
		}
		if(playerValue<dealerValue) {
			print("You lose, "+playerValue+" to "+dealerValue);
			currentBet =0;
			inHand = false;
			clearHands();
			return;
		}
		
	}
	public void clearHands() {
		playerHand.clear();
		dealerHand.clear();
	}
	public int checkHand(ArrayList<Integer> hand) {
		int handValue=0;
		int softAces = 0;
		for(int x=0;x<hand.size();x++) {
			if(hand.get(x) == 1 && (handValue+11)<=21) {
				handValue = handValue+11;
				softAces++;
				continue;
			}
			if(softAces>0&&handValue+hand.get(x)>21 ) {
				handValue=handValue-10;
				handValue=handValue+hand.get(x);
				softAces--;
				continue;
			}
			handValue=handValue+hand.get(x);
			
		}
		return handValue;
		
	}
	public void print(String str) {
		console.append(str+" \n");
	}
	
}
