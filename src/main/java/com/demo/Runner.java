package com.demo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.view.MovieSearchPanel;

public class Runner {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				MovieSearchPanel mainFrame = new MovieSearchPanel();
				mainFrame.pack();
				mainFrame.setVisible(true);
			}
		});
		
		context.close();
	}
}
