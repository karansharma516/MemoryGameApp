/**
 * @Author:Karan Sharma & Chandan Dadral
 * @Version: April 17, 2015 
 * @Description :It is an Android app which helps to sharp memory. In this app 8 pairs of 
 * 				random cards provided on screen facing backward. User has to match 2 pairs of cards.
 * 				Timer of the game starts when user clicks on first card and score is given on top of screen
 */
package ca.georgiancollege.memorygameapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;


public class MainActivity extends Activity {

      // INSTANCE VARIABLES ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private int _firstCard, _secondCard;
	private CountDownTimer timer;
	private TextView _timeText;
	private TextView _scoreText;
	private int _scores; 
	private TextView _messagebox;
	private boolean _firstSelection;

	Handler timerHandler = new Handler();
	
	
	// DECLARE ALL THE IMAGE BUTTONS
		private ImageButton _imageButton1,_imageButton2, _imageButton3, _imageButton4,
	    _imageButton5, _imageButton6, _imageButton7, _imageButton8,
	    _imageButton9, _imageButton10, _imageButton11, _imageButton12,
		_imageButton13, _imageButton14, _imageButton15, _imageButton16;
		
        // ADDED ALL THE IMAGE BUTTONS TO THE ARRAY
		private ImageButton[] _imageButtons = { _imageButton1,_imageButton2, _imageButton3, _imageButton4,
	    _imageButton5, _imageButton6, _imageButton7, _imageButton8,
	    _imageButton9, _imageButton10, _imageButton11, _imageButton12,
		_imageButton13, _imageButton14, _imageButton15, _imageButton16};
	
        // ADDED ALL THE IMAGE BUTTON IDS TO AN ARRAY
		private int _imageIds[] = {R.id.imageButton1, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4,
   		R.id.imageButton5, R.id.imageButton6, R.id.imageButton7, R.id.imageButton8,
		R.id.imageButton9, R.id.imageButton10, R.id.imageButton11, R.id.imageButton12,
		R.id.imageButton13, R.id.imageButton14, R.id.imageButton15, R.id.imageButton16};
		
		HashMap<Integer, String> grid = new HashMap<Integer, String>();

         // PUBLIC METHODS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         /**
          * This is the main method of the class
	  *
	  */
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			// initialize variables +++++++++++++++++++++++++++++++++++
			this._firstSelection = true;
			this._firstCard = -1;
			this._secondCard = -2;
			this._scores = 0;
			
            // get the displayMessage and assign it to the variable
			 this._messagebox = (TextView) findViewById(R.id.pickTextView);
			// get the scoreText and assign it to the variable
			 this._scoreText = (TextView) findViewById(R.id.scoreTextView);
			// get the timeText and assign it to the variable
			 this._timeText = (TextView) findViewById(R.id.timeTextView);
             // set the Score to 0
			 _scoreText.setText("Score: 0");
			// set the time to 30 seconds 
			 this._timeText.setText("Timer: 30");
			 
			Iterator<String> iterator = pickEightCards().iterator();
			//initialize the key
			int key=0;
			// build my Hash map which I call "grid"
			while (iterator.hasNext()) {
				String value = (String) iterator.next();
				grid.put(key, value);
				key++;
			}

	    	// set up the on click event for every image button
	    	for(int i=0;i<_imageButtons.length;i++){
	    		final int index = i ; // declaring local variable as int
	    		// linking each image button to its Id
	    		this._imageButtons[i] = (ImageButton)findViewById(this._imageIds[i]);
	    		// set up the click event listener for each image button
	    		this._imageButtons[i].setOnClickListener(new OnClickListener(){
	    			@Override
	    			public void onClick(View v){
	    				String nameCard = grid.get(index);
	    				// set the background color of the image button cyan
	    				_imageButtons[index].setBackgroundColor(Color.rgb(0, 255, 255));
	    				// check if cards have been picked 
	    				if(_firstCard == -1 || _secondCard == -2){
	    		_imageButtons[index].setImageResource(getResources().getIdentifier(nameCard, "drawable", getPackageName()));	
	    				}
	    				pickCard(index); // call the pickCard method
	    			}
	    			
	    			
	    		});  
	    	}//end for loop
	    	
	    	
		}//on create end here
		
		/**
		 * This method displays the card and turn on the timer when first card has been selected
		 * @param card
		 */
		private void pickCard(int card){
			// check if the card has been selected and starts the timer
			if(this._firstSelection) {
				
				//set firstSelection to false
				this._firstSelection = false;
				
				//timer starts at 30000 milliseconds, decrementing after every 1000
				this.timer = new CountDownTimer(30000, 1000) {

					//onTick update the timerTextView and check how many seconds are remaining
				     public void onTick(long millisUntilFinished) {
				    	 _timeText.setText("Timer: " + millisUntilFinished / 1000);
				     } //method onTick ends

				     //when the timer finishes
				     public void onFinish() {
				    	 finishGame();
				     } //method onFinish ends
				  }.start(); //start the timer
			} //if ends
			
			// if the first card has been selected
			if(this._firstCard == -1){	
				this._firstCard = card;			
			}
			
			// check that second card has been selected
			else if (this._firstCard != card && this._secondCard == -2){
				this._secondCard = card;
				//timer starts at 1000 milliseconds, decrementing after every 1000 and calls the cardsFlip method
			 this.timer =  new CountDownTimer(1000, 1000){
					public void onTick(long millisUnitFinished)
					{
					
					}
				// this method call the cardFlip method and flip back the cards if its not same	
					public void onFinish(){	
						cardsFlip();
					}
				}.start();
			}
			this._messagebox.setText("Now Pick Another Card");
		}

	/**
	 * This method hides the card uf they are same other wise turn it back to backCard
	 */
		public void cardsFlip(){
			// check if cards are same 
			if(this.grid.get(this._firstCard) == this.grid.get(_secondCard)){
				this._imageButtons[this._firstCard].setVisibility(View.INVISIBLE);
				this._imageButtons[this._secondCard].setVisibility(View.INVISIBLE);
				// turn the label to right if cards are same
				this._messagebox.setText("Right!");
				// this increase the score with the increment of 1
				this._scores++;
				// this shows the current score to the text 
				this._scoreText.setText("Score: "+this._scores);
				checkScore();
			}
			else{
				// this changes the background color to the white and turn the card to the cardback if its not same
				this._imageButtons[this._firstCard].setBackgroundColor(Color.rgb(255, 255, 255));
				this._imageButtons[this._secondCard].setBackgroundColor(Color.rgb(255, 255, 255));
				this._imageButtons[this._firstCard].setImageResource(R.drawable.cardback);
				this._imageButtons[this._secondCard].setImageResource(R.drawable.cardback);
			    // displays the message below if its not same	
				this._messagebox.setText("Wrong! Pick again…");
			}	
			this._secondCard = -2;
			this._firstCard = -1;
			}
		/**
		 * This method checks the score and shows the message text to Won when score reaches 8
		 */
		private void checkScore(){
			if (this._scores==8)
			{
				this._messagebox.setText("You Won");		
			}
		}
		
		/**
		 * This method displays the alert dialog box when the timer runs out 
		 * and ask the user to play again
		 */
		public void finishGame(){
			 this._timeText.setText("Timer: 0");
			 
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);

			    builder.setTitle("Game Over");
			    builder.setMessage("Final Score:" + this._scores);
			    builder.setCancelable(false);
			    builder.setPositiveButton("Play Again?", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing but close the dialog
			        	
			        	Intent i = new Intent(MainActivity.this, MainActivity.class);
			            startActivity(i);
			       }

			    });

			    AlertDialog alert = builder.create();
			    alert.show();
			 
		}//end finishGame
		
		// pick eight cards out of a 52 card deck and shuffle 2 copies of each into a 16 member ArrayList
		private static ArrayList<String> pickEightCards() {
			Random random = new Random();
			ArrayList<String> deckOfCards = new ArrayList<String>();
			String[] cardSuit={"c","d","h","s"};
			ArrayList<String> cardSelected = new ArrayList<String>();
			String card;
			
			// create ArrayList of cards
			for (String suit : cardSuit) 
				for (int index = 1; index < 14; index++) 
					deckOfCards.add("card_"+index+suit);

			// remove eight random cards and put them into another list
			for (int index = 0; index < 8; index++) {
				card = deckOfCards.remove(random.nextInt(deckOfCards.size()));
				// add the same random card to the new list twice
				cardSelected.add(card);
				cardSelected.add(card);
			} // end for
			
			// shuffle the card list
			Collections.shuffle(cardSelected);
			
			return cardSelected;
		} // end pickEightCards method
	
	
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}