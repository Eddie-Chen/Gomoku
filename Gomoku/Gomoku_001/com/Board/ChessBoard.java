package com.Board;

import com.Point.Point;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/*五子棋-棋盤 繼承 JPanel & MouseListener*/
public class ChessBoard extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	/* 棋盤初始值 */
	public static int MARGIN = 30;// 四周邊距
	public static int GRID_SPAN = 30;// 網格的間距
	public static int ROWS = 15;// 棋盤 行數
	public static int COLS = 15;// 棋盤 列數

	Point[] chessList = new Point[(ROWS + 1) * (COLS + 1)];// 初始化每個數組為null
	boolean isBack = true;// 默認開始是黑棋先下
	boolean gameOver = false;// 遊戲是否結束
	int chessCount;// 當前棋盤的棋子個數
	int xIndex, yIndex;// 當前剛下棋子的索引

	// 建構子
	public ChessBoard() {
		setBackground(Color.LIGHT_GRAY);// 設置背景顏色為灰色
		addMouseListener(this);// 添加事件監聽器
		addMouseMotionListener(new MouseMotionListener() {// 匿名內部類

			@Override
			// 處理滑鼠移動事件
			public void mouseMoved(MouseEvent e) {
				// 將鼠標單擊的坐標位置轉化為網格索引
				int x1 = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
				int y1 = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;

				// 落在棋盤外，不能下；遊戲已經結束，不能下；x，y位置已經有棋子存在，不能下
				if (x1 < 0 || x1 > ROWS || y1 < 0 || y1 > COLS || gameOver || findChess(x1, y1)) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));// 滑鼠設置成默認鼠標
				} else {
					setCursor(new Cursor(Cursor.HAND_CURSOR));// 滑鼠設定小手圖案
				}
			}

			@Override
			// 處理滑鼠拖曳事件
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	/* 繪製棋盤 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);// 畫棋盤
		g.setColor(Color.YELLOW);// 棋線設定為黃色

		for (int i = 0; i <= ROWS; i++) {// 畫橫線
			g.drawLine(MARGIN, MARGIN + i * GRID_SPAN, MARGIN + COLS * GRID_SPAN, MARGIN + i * GRID_SPAN);

		}
		for (int i = 0; i <= COLS; i++) {// 畫直線
			g.drawLine(MARGIN + i * GRID_SPAN, MARGIN, MARGIN + i * GRID_SPAN, MARGIN + ROWS * GRID_SPAN);
		}

		/* 設定棋子 chessList point.class */
		for (int i = 0; i < chessCount; i++) {
			int xPos = chessList[i].getX() * GRID_SPAN + MARGIN;// 棋盤交叉線的x坐標
			int yPos = chessList[i].getY() * GRID_SPAN + MARGIN;// 棋盤交叉線的y坐標
			g.setColor(chessList[i].getColor());// 設置棋子顏色
			g.fillOval(xPos - Point.DIAMETER / 2, yPos - Point.DIAMETER / 2, Point.DIAMETER, Point.DIAMETER);

			if (i == chessCount - 1) {
				g.setColor(Color.red);// 標記最後一個棋子外框為紅色
				g.drawRect(xPos - Point.DIAMETER / 2, yPos - Point.DIAMETER / 2, Point.DIAMETER, Point.DIAMETER);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {// 鼠標按鍵在組件上按下時調用
		if (gameOver)// 遊戲已經結束，不能下
			return;
		String colorName = isBack ? "黑棋" : "白棋";
		xIndex = (e.getX() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;
		yIndex = (e.getY() - MARGIN + GRID_SPAN / 2) / GRID_SPAN;// 將鼠標單擊的坐標位置轉化為網格索引
		if (xIndex < 0 || xIndex > ROWS || yIndex < 0 || yIndex > COLS)// 棋子落在棋盤外，不能下
			return;
		if (findChess(xIndex, yIndex))// x,y位置已經有棋子存在，不能下
			return;

		Point ch = new Point(xIndex, yIndex, isBack ? Color.black : Color.white);
		chessList[chessCount++] = ch;
		repaint();// 通知系統重新繪制
		if (isWin()) {
			String msg = String.format("恭喜，%s贏啦~", colorName);
			JOptionPane.showMessageDialog(this, msg);
			gameOver = true;
		} else if (chessCount == (COLS + 1) * (ROWS + 1)) {
			String msg = String.format("棋鼓相當，棒棒噠~");
			JOptionPane.showMessageDialog(this, msg);
			gameOver = true;
		}
		isBack = !isBack;
	}

	@Override
	public void mouseClicked(MouseEvent e) {// 鼠標按鍵在組件上單擊(按下並釋放)時調用
	}

	@Override
	public void mouseReleased(MouseEvent e) {//// 鼠標按鍵在組件上釋放時調用
	}

	@Override
	public void mouseEntered(MouseEvent e) {// 鼠標進入組件時調用
	}

	@Override
	public void mouseExited(MouseEvent e) {// 鼠標離開組件時調用
	}

	private boolean findChess(int x, int y) {
		for (Point c : chessList) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}

	/* 判斷贏方 */
	private boolean isWin() {
		int continueCount = 1;// 棋子數量初始值
		// 橫向尋找
		// 向左
		for (int x = xIndex - 1; x >= 0; x--) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, yIndex, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		// 橫向尋找
		// 向右
		for (int x = xIndex + 1; x <= ROWS; x++) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, yIndex, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		// 累積數量大於等於五，即表示此方獲勝
		if (continueCount >= 5) {
			return true;
		} else {
			continueCount = 1;
		}

		// 縱向尋找
		// 向上
		for (int y = yIndex - 1; y >= 0; y--) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(xIndex, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		// 縱向尋找
		// 向下
		for (int y = yIndex + 1; y <= ROWS; y++) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(xIndex, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		// 累積數量大於等於五，即表示此方獲勝
		if (continueCount >= 5) {
			return true;
		} else {
			continueCount = 1;
		}
		
		
		// 右下尋找
		for (int x = xIndex + 1, y = yIndex - 1; y >= 0 && x <= COLS; x++, y--) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		
		// 左上尋找
		for (int x = xIndex - 1, y = yIndex + 1; y <= ROWS && x >= 0; x--, y++) {
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}

		// 判斷記錄數大於等於五，即表示此方獲勝
		if (continueCount >= 5) {
			return true;
		} else {
			continueCount = 1;
		}

		// 左下尋找
		for (int x = xIndex - 1, y = yIndex - 1; y >= 0 && x >= 0; x--, y--) {// 左下尋找
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}

		// 右上尋找
		for (int x = xIndex + 1, y = yIndex + 1; y <= ROWS && x <= COLS; x++, y++) {// 右上尋找
			Color c = isBack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				continueCount++;
			} else {
				break;
			}
		}
		if (continueCount >= 5) {// 判斷記錄數大於等於五，即表示此方獲勝
			return true;
		} else {
			continueCount = 1;
		}
		return false;
	}

	// 取得棋子，實現一黑一白下子
	private Point getChess(int xIndex, int yIndex, Color color) {
		for (Point c : chessList) {
			if (c != null && c.getX() == xIndex && c.getY() == yIndex && c.getColor() == color)
				return c;
		}
		return null;
	}

	// 重新開始
	public void restartGame() {
		for (int i = 0; i < chessList.length; i++)
			chessList[i] = null;
		/* 恢復遊戲相關的變量值 */
		isBack = true;
		gameOver = false;// 遊戲是否結束
		chessCount = 0;// 當前棋盤的棋子個數
		repaint();
	}

	// 悔棋
	public void goback() {
		if (chessCount == 0)
			return;
		chessList[chessCount - 1] = null;
		chessCount--;
		if (chessCount > 0) {
			xIndex = chessList[chessCount - 1].getX();
			yIndex = chessList[chessCount - 1].getY();
		}
		isBack = !isBack;
		repaint();
	}

	// Dimension:視窗大小
	public Dimension getPreferredSize() {
		return new Dimension(MARGIN * 2 + GRID_SPAN * COLS, MARGIN * 2 + GRID_SPAN * ROWS);
	}

}