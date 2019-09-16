package program1.View;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainView extends JFrame {
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem item1, item2, item3;
	private MenuListener menuListener;
	
	public MainView() {}
	public MainView(String s, int x, int y, int w, int h) {
		init(s);
		setLocation(x,y);
		setSize(w,h);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	void init(String s) {
		setTitle(s);
		menubar = new JMenuBar();
		menu = new JMenu("Database operation");
		item1 = new JMenuItem("Display records");
		item2 = new JMenuItem("Update records");
		item3 = new JMenuItem("Insert records");
		menuListener = new MenuListener();
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menubar.add(menu);
		setJMenuBar(menubar);
		
		// add menu item listener
		item1.addActionListener(menuListener);
		item2.addActionListener(menuListener);
		item3.addActionListener(menuListener);
	}
	
	public static void main(String args[]) {
		// get screenSize
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		new MainView("MainView",(int)screenSize.getWidth()/2-175,(int)screenSize.getHeight()/2-125,380,250);
	}
	
	private class MenuListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			if(e.getSource() == item1) {
				new DisplayRecordsDialog("DisplayRecordsDialog",(int)screenSize.getWidth()/2-175,(int)screenSize.getHeight()/2-125,380,250);
			}
			else if(e.getSource() == item2) {
				new ModifyRecordsDialog("ModifyRecordsDialog",(int)screenSize.getWidth()/2-175,(int)screenSize.getHeight()/2-125,700,250);
			}
			else if(e.getSource() == item3) {
				new InsertRecordsDialog("InsertRecordsDialog",(int)screenSize.getWidth()/2-175,(int)screenSize.getHeight()/2-125,700,250);
			}
			
		}
		
	}
}
