package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		
		JFrame window=new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Adventure");
		
		GamePanel gamePanel=new GamePanel();
		window.add(gamePanel);
		
		//Causes this Window to be sized to fit the preferred size and layouts of its subcomponents(=GamePanel)
		//pack()会根据子组件的首选大小(preferred size)自动调整窗口大小，使其刚好能容纳所有内容
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}
