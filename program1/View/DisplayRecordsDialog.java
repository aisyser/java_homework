package program1.View;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import program1.Controller.Database;
import program1.Model.Student;

public class DisplayRecordsDialog extends JDialog {
	private JButton button;
	private String [] columnName;
	private String [][] data;
	private DefaultTableModel update_table;
	private JTable table;
	private Database database;
	public DisplayRecordsDialog() {}
	public DisplayRecordsDialog(String s, int x, int y, int w, int h) {

		init(s);
		setLocation(x,y);
		setSize(w,h);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void init(String s) {
		setTitle(s);
		button = new JButton("Display records");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == button) {
					setData();
				}
			}
			
			
		});
		update_table = new DefaultTableModel(data, columnName);
		table = new JTable(update_table);
		// update_table.setDataVector(data,columns);
		add(button, BorderLayout.NORTH);
		add(new JScrollPane(table));
		
		
		
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
		data = new String[students.size()][columnName.length];
		for(int i=0; i < students.size(); i++) {
			Student student = students.get(i);
			data[i][0] = student.getId();
			data[i][1] = student.getName();
			data[i][2] = student.getSex();
			data[i][3] = String.valueOf(student.getAge());
			data[i][4] = student.getSdept();	
		}
		update_table.setDataVector(data, columnName);
		try {

			Database.disconnect();	
		} catch (SQLException e) {
			System.out.println("Failed to disconnect to mysql.");
		}
	}
	
}
