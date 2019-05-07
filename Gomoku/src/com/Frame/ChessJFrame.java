package com.Frame;

import com.Board.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/*
 * 框架+啟動
 */
public class ChessJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private ChessBoard chessBoard;// 棋盤
	private Panel toolbar;// Menu Bar
	private JButton startButton;// Start
	private JButton backButton;// Return
	private JButton exitButton;// Exit

	/* 建構子 */
	public ChessJFrame() {
		setTitle("單機版五子棋");// 標題
		chessBoard = new ChessBoard();// 初始化棋盤

		ItemListener lis = new ItemListener();// 初始化按鈕event and 監聽器內部類別

		/* Menu Bar 實例化 */
		toolbar = new Panel();
		startButton = new JButton("重新開始");// Start Button實例化，設置名稱:重新開始
		backButton = new JButton("悔棋");// return Button實例化，設置名稱:悔棋
		exitButton = new JButton("結束");// Exit Button實例化，設置名稱:結束

		toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));// 用FlowLayout布局，設定Menu Bar位置
		// 按鈕加入Menu Bar
		toolbar.add(backButton);
		toolbar.add(startButton);
		toolbar.add(exitButton);

		// 三個按鈕事件註冊ActionListener
		startButton.addActionListener(lis);
		backButton.addActionListener(lis);
		exitButton.addActionListener(lis);

		// 將Menu Bar布局到界面南方也就是下面
		add(toolbar, BorderLayout.SOUTH);
		// 棋盤加入視窗中
		add(chessBoard);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 設置界面關閉事件

		pack();// 使視窗擴展至足以容納元件
	}

	/* 按鈕監聽事件 */
	private class ItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();// 取得事件
			if (obj == startButton) {
				System.out.println("新局重新開始");// 重新開始
				// JFiveFrame.this內部類引用外部類
				chessBoard.restartGame();
			} else if (obj == exitButton) {
				System.exit(0);// 結束應用程序
			} else if (obj == backButton) {
				System.out.println("歐歐!悔棋了");// 悔棋
				chessBoard.goback();
			}
		}
	}

	public static void main(String[] args) {
		ChessJFrame f = new ChessJFrame();// 實例化主框架
		f.setVisible(true);// 顯示主框架
		f.setResizable(false);// 不可調整尺寸
	}
}