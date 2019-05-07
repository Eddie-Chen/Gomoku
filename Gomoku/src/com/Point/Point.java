package com.Point;

import java.awt.*;
/* 棋子類別，棋盤位子，顏色，大小*/
public class Point {
	private int x;// 棋子在棋盤中的x索引值
	private int y;// 棋子在棋盤中的y索引值
	private Color color;// 顏色
	public static int DIAMETER = 30;// 棋子直徑大小

	public Point(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	// 取得棋子的x索引值
	public int getX() {
		return x;
	}

	// 取得棋子的y索引值
	public int getY() {
		return y;
	}

	// 取得棋子顏色
	public Color getColor() {
		return color;
	}
}