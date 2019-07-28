import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

//呼叫applet類別的 “Gomoku_001”
public class Gomoku extends Applet {

	public void init() {
		setLayout(null); //set the layout by myself
		setBackground(new Color(0, 150, 0));//set background dark-green color

		//GoMokuCanvas class
		GomokuCanvas board = new GomokuCanvas();
		/*create the button: newGameButton, resignButton and Label message*/
		add(board);
		//new Game Button
		board.newGameButton.setBackground(Color.lightGray);
		add(board.newGameButton);

		//resign Button 
		board.resignButton.setBackground(Color.lightGray);
		add(board.resignButton);

		//Label message
		board.message.setForeground(Color.green);
		board.message.setFont(new Font("Serif", Font.BOLD, 14));
		add(board.message);

		/*set the position and button size*/
		board.setBounds(16, 16, 172, 162);
		board.newGameButton.setBounds(210, 60, 100, 30);
		board.resignButton.setBounds(210, 120, 100, 30);
		board.message.setBounds(0, 200, 330, 30);
	}
}

