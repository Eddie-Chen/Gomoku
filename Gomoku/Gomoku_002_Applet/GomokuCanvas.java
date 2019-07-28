import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//繼承 Canvas 類別 (class) 通常用作繪圖的畫布
//繼承 ActionListner and MouseListener
public class GomokuCanvas extends Canvas implements ActionListener, MouseListener {
	//field
	Button resignButton;//current player can resign by this button
	Button newGameButton;//start new game button when the game is finish

	Label message;//display the message

	int[][] board;//my go board

	static final int EMPTY = 0;//Empty
	static final int WHITE = 1;//white player
	static final int BLACK = 2;//black player

	boolean gameInProgress; //Is a game currently in progress?

	int currentPlayer;//whose turn? white or black?

	int win_r1, win_c1, win_r2, win_c2;
	//(win_r1, win_c1)and(win_r2_win_c2) to determined the player getting five or more pieces.
	//if no five pieces in the row then the win_r1 value is -1
	//the values are set in the count method.
	//the value of win_r1 is tested in the paint()method.

	//Constructor:
	//1. create the buttons and label.
	//2. Listen for clicks
	//3. create the board and start the first game
	public GomokuCanvas() 
	{
		
		setBackground(Color.lightGray);
		addMouseListener(this);
		setFont(new Font("serif", Font.BOLD, 14));

		resignButton = new Button("Resign");
		resignButton.addActionListener(this);
		newGameButton = new Button("New Game");
		newGameButton.addActionListener(this);
		message = new Label("", Label.CENTER);
		board = new int[13][13];//gomoku board 13*13

		doNewGame();//start the first game

	}

	// If no game is in progress, show an error message. 
	// Otherwise, find the row and column that the Player clicked and 
	// call doClickSquare() to handle it.
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if (gameInProgress == false) {
			message.setText("Click\"New Game\" to start a new Game.");
		} else {
			int column = (e.getX() - 2) / 13;
			int row = (e.getY() - 2) / 13;
			if (column >= 0 && column < 13 && row > 0 & row < 13) {
				doClickSquare(row, column);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) //respond the click issue
	{
		Object src=e.getSource();
		if(src==newGameButton) //Start the new game button
		{
			doNewGame();//run doNewGame method
		}
		else if(src==resignButton)//player give up button
		{
			doResign();//run doResign method
		}
	}

	
	void doNewGame() //start new game
	{
		//Check is there any game still continues,first?
		if (gameInProgress == true) 
		{
			message.setText("Finish the current game first");
			return;
		}
		//if gameInProgress=false then run the code at below
		for (int row = 0; row < 13; row++) {
			for (int column = 0; column < 13; column++) {
				board[row][column] = EMPTY;//gomoku board set 0
				currentPlayer = BLACK;//black player go first at every new game
				message.setText("BLACK: make your move.");
				gameInProgress = true;//game is not finish
				newGameButton.setEnabled(false);//new game button can't click
				resignButton.setEnabled(true);//resign button can click
				win_r1 = -1;//Set the value indicates that no red line is to be drawn.
				repaint();//
			}
		}
	}

	//player resign method
	void doResign() 
	{
		//Check is there any game still continues,first?
		if (gameInProgress == true) {
			message.setText("Finish the Current game first");
			return;
		}
		//if white player click resign then black player win
		if (currentPlayer == WHITE) {
			message.setText("WHITE resigns. BLACK wins");
		} else {//otherwise white player win
			message.setText("BLACK resigns. WHITE wins");
		}
		
		newGameButton.setEnabled(true);//new game button can click
		resignButton.setEnabled(false);//resign button can't click
		gameInProgress = false; //the game is finish
	}
	
	//Game Over method , when game is finish will received the String str value 
	void GameOver(String str) 
	{
		
		message.setText(str);//print str information
		newGameButton.setEnabled(true);//new game button can click
		resignButton.setEnabled(false);//resign button can't click
		gameInProgress = false; //the game is finish
	}

	//Identifier when a player clicks on the board in the specified row and column. 
	void doClickSquare(int row, int column) 
	{
		// Check that the player has clicked on an empty square. 
		// If row & column are not 0, then show an error message.
		if (board[row][column] != EMPTY) {
			//If the current player is black = true
			if (currentPlayer == BLACK) {
				message.setText("BLACK: Please click an empty square.");
			} else {//Otherwise 
				message.setText("WHITE: Please click an empty square.");
			}
			return;
		}

		
		//Check if the board is full or if the move is a winning move. 
		//If so, the game ends. If not, then it's the other user's turn.
		board[row][column] = currentPlayer;// Make the move. 
		Graphics g = getGraphics();
		drawPiece(g, currentPlayer, row, column);
		g.dispose();//release system and stop drawing

		//First, check who is the winner
		if (winner(row, column)) {
			if (currentPlayer == WHITE) {
				GameOver("WHITE wins the game!");
			} else {
				GameOver("BLACK wins the game!");
			}
			Graphics w = getGraphics();
			drawWinLine(w);//draw the red line
			w.dispose();//release system and stop drawing
			return;
		}

		boolean emptySpace = false;//check the board is full or not?
		//run for loop to check is there any EMPTY
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				//if have EMPTY then is true, can continuance the game
				if (board[i][j] == EMPTY) {
					emptySpace = true;
				}
				//but if emptyspace is false, then game finish
				if (emptySpace == false) {
					GameOver("The game ends in a draw.");
				}
				return;
			}
		}
		/* Continue the game. It's the other player's turn. */
		if (currentPlayer == BLACK) {
			currentPlayer = BLACK;
			message.setText("Black:make your move");
		} else {
			currentPlayer = WHITE;
			message.setText("White:make your move");
		}
	}

	// counting the number in a line in each of the four possible directions from (row,column). 
	// If there are 5 squares or more in a row in any direction, then player is won.
	private boolean winner(int row, int column) {
		//count (int player, int row, int column, int dirX, int dirY)
		//counting the pieces number from 4 directions  
		if (count(board[row][column], row, column, 1, 0) >= 5) {
			return true;
		}
		if (count(board[row][column], row, column, 0, 1) >= 5) {
			return true;
		}
		if (count(board[row][column], row, column, 1, -1) >= 5) {
			return true;
		}
		if (count(board[row][column], row, column, 1, 1) >= 5) {
			return true;
		}
		/* When we get to this point, we know that the game is not won. 
		 * The value of win_r1, which was changed in the count()method, 
		 * has to be reset to -1, to avoid drawing a red line on the board. 
		 * */
		win_r1 = -1;
		return false;
	}

	// Counts the number of the player's pieces starting at square (row,column) 
	// and extending along the direction specified by (dirX,dirY). 
	// It is assumed that the player has a piece at (row,column). 
	// This method looks at the squares (row + dirX, column+dirY),
	// (row + 2*dirX, column + 2*dirY), ... until it hits a square that is
	// off the board or is not occupied by one of the players pieces.
	// It counts the squares that are occupied by the player's pieces.
	// Furthermore, it sets (win_r1,win_c1) to mark last position where
	// it saw one of the player's pieces. Then, it looks in the
	// opposite direction, at squares (row - dirX, column-dirY),
	// (row - 2*dirX, column - 2*dirY), ... and does the same thing.
	// Except, this time it sets (win_r2,win_c2) to mark the last piece.
	// Note: The values of dirX and dirY must be 0, 1, or -1. At least
	// one of them must be non-zero.
	private int count(int player, int row, int column, int dirX, int dirY) {
		int ct = 1;// Number of pieces in a row belonging to the player.
		int r, c;// A row and column to be examined
		
		// Look at board in specified direction.
		r = row + dirX;
		c = column + dirY;
		
		
		while (r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player) {
			ct++;
			// Go on to next square in this direction
			r += dirX;
			c += dirY;
		}
		
		win_r1 = r - dirX;// The next-to-last square looked at.
		win_c1 = c - dirY;// (The LAST one looked at was off the board or did not contain one of the player's pieces.
		r = row - dirX;// Look in the opposite direction.
		c = column + dirY;
		
		
		while (r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player) {
			ct++;
			// Go on to next square in this direction
			r -= dirX;
			c -= dirY;
		}
		
		win_r2 = r + dirX;
		win_c2 = c + dirY;

		// At this point, (win_r1,win_c1) and (win_r2,win_c2) mark the endpoints291
		// of the line of pieces belonging to the player
		return ct;
	}

	/* Draw a two-pixel black border around the edges of the canvas,
	 * and draw grid lines in darkGray. */
	public void paint(Graphics g) {
		g.setColor(Color.darkGray);
		for (int i = 1; i < 13; i++) {
			g.drawLine(1 + 13 * i, 0, 1 + 13 * i, getSize().height);
			g.drawLine(0, 1 + 13 * i, getSize().width, 1 + 13 * i);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		g.drawRect(1, 1, getSize().width - 3, getSize().height - 3);
		
		/* Draw the pieces that are on the board. */
		for (int row = 0; row < 13; row++) {
			for (int column = 0; column < 13; column++) {
				if (board[row][column] != EMPTY) {
					drawPiece(g, board[row][column], row, column);
				}
			}
		}
		/* If the game has been won, then win_r1 >= 0. Draw a line to mark
		 * the five winning pieces. */
		if (win_r1 >= 0) {
			drawWinLine(g);
		}
	}

	private void drawPiece(Graphics g, int piece, int row, int col) {
		// Draw a piece in the square at (row,col). The
		// color is specified330// by the piece
		// parameter, which should be either BLACK or
		// WHITE.331
		if (piece == WHITE) {
			g.setColor(Color.white);
		} else {
			g.setColor(Color.black);
			g.fillOval(3 + 13 * col, 3 + 13 * row, 10, 10);
		}
	}

	// Draw a 2-pixel wide red line from the middle of the square at
	// (win_r1,win_c1) to the middle of the square at (win_r2,win_c2).
	// This routine is called to mark the 5 pieces that won the game
	// The values of the variables are set in the count() method.
	private void drawWinLine(Graphics g) {
		g.setColor(Color.red);
		g.drawLine(8 + 13 * win_c1, 8 + 13 * win_r1, 8 + 13 * win_c2, 8 + 13 * win_r2);
		if (win_r1 == win_r2) {
			g.drawLine(8 + 13 * win_c1, 7 + 13 * win_r1, 8 + 13 * win_c2, 7 + 13 * win_r2);
		} else {
			g.drawLine(7 + 13 * win_c1, 8 + 13 * win_r1, 7 + 13 * win_c2, 8 + 13 * win_r2);
		}
	}

	public Dimension getPreferedSize() {
		// Specify desired size for this component. Note:
		// the size MUST be 172 by 172
		return new Dimension(172, 172);
	}

	public Dimension getMinimumSize() {
		return new Dimension(172, 172);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
