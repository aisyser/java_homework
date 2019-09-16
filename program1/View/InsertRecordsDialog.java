package program1.View;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import program1.Controller.Database;
import program1.Model.Student;

public class InsertRecordsDialog extends JDialog{
	private JLabel tip;
	private JTable table;
	private JButton button;
	private String [] columnName;
	private String [][] data;
	private DefaultTableModel update_table;
	private Database database;
	private JPanel panel;
	private JDialog This;
	
	public InsertRecordsDialog() {}
	public InsertRecordsDialog(String s, int x, int y, int w, int h) {
		setData();
		init(s);
		setLocation(x,y);
		setSize(w,h);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void init(String s) {
		This = this;
		setTitle(s);
		tip = new JLabel("Insert record");
		tip.setSize(new Dimension(40,20));
		update_table = new DefaultTableModel(data, columnName);
		table = new JTable(update_table);
		
		button = new JButton("Insert records");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(e.getSource() == button) {
					
					if(isLegal()) {
						Student student = new Student();
						student.setId((String)table.getValueAt(0,0).toString().trim());
						student.setName((String)table.getValueAt(0,1).toString().trim());
						student.setSex((String)table.getValueAt(0,2).toString().trim());					
						student.setAge(Integer.parseInt(table.getValueAt(0,3).toString().trim()));
						student.setSdept((String)table.getValueAt(0,4).toString().trim());
						
						// insert a student information to mysql
						try {
							Database.connect();
						} catch (SQLException e1) {
							System.out.println("Failed to connect to mysql.");
						}
						database = new Database();
						if(!database.isExist(student.getId())) {
							if(database.insert(student)) {
								JOptionPane.showConfirmDialog(This, "Insert Successfully!", "Insert records", JOptionPane.OK_OPTION);
							}
						}
						else {
							JOptionPane.showConfirmDialog(This, "Failed to insert, this id is exist!", "Insert records", JOptionPane.OK_OPTION);
						}
						
						try {
							database.disconnect();
						} catch (SQLException e1) {
							System.out.println("Failed to disconnect to mysql.");
						}
						
					}
					
				}
			}
			
		});
		panel = new JPanel();
		panel.add(tip);
		Box box = Box.createHorizontalBox();
		box.setPreferredSize(new Dimension(420,40));
		box.add(new JScrollPane(table));
		panel.add(box);
		panel.add(button);
		add(panel);
	}
	
	public void setData() {
		
		// connect mysql
		try {
			Database.connect();
			
		} catch (SQLException e) {
			System.out.println("Failed to connect to mysql.");
		}
		database = new Database();
		ArrayList<Student> students = (ArrayList)database.query();
		columnName = database.getColumnName();
		data = new String[1][columnName.length];
		for(int i=0; i<columnName.length; i++) {
			data[0][i] = new String();
		}
		try {

			Database.disconnect();	
		} catch (SQLException e) {
			System.out.println("Failed to disconnect to mysql.");
		}
	}
	
	public boolean isLegal() {
		boolean legalStatus = false;
		boolean isEmpty = false;
		
		for(int i=0;i<table.getColumnCount();i++) {
			if(table.getValueAt(0, i).toString().trim().equals("")) {
				System.out.println(table.getColumnName(i)+" should not be empty!");
				JOptionPane.showConfirmDialog(this, table.getColumnName(i)+" should not be empty!", "Insert records", JOptionPane.OK_OPTION);
				isEmpty = true;
				break;
			}
		}
		if(!isEmpty) {
			try {
				String age = (String)table.getValueAt(0,3).toString().trim();
				Integer.parseInt(age);
				legalStatus = true;
			} catch(NumberFormatException ee) {
				System.err.println("Age should be integer!");
				JOptionPane.showConfirmDialog(this, "Age should be integer!", "Insert records", JOptionPane.OK_OPTION);
			}
		}
		
		return legalStatus;
	}
	
}
