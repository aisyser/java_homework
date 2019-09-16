package program1.View;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import program1.Controller.Database;
import program1.Model.Student;

public class ModifyRecordsDialog extends JDialog{
	private JLabel tip;
	private JTable table;
	private JButton button;
	private String [] columnName;
	private String [][] data;
	private DefaultTableModel update_table;
	private Database database;
	private JPanel panel;
	private JDialog This;
	private JTextField id;
	
	public ModifyRecordsDialog() {}
	public ModifyRecordsDialog(String s, int x, int y, int w, int h) {
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
		tip = new JLabel("Modify record");
		tip.setSize(new Dimension(40,20));
		id = new JTextField(10);
		id.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					Student student = new Student();
					student.setId(id.getText().toString().trim());
					if(!student.getId().equals("")) {
						
						try {
							Database.connect();
						} catch (SQLException e1) {
							System.out.println("Failed to connect to mysql.");
						}
						database = new Database();
						
						if(database.query(student)) {
						
							data[0][0] = student.getName();
							data[0][1] = student.getSex();
							data[0][2] = String.valueOf(student.getAge());
							data[0][3] = student.getSdept();	
							update_table.setDataVector(data, columnName);
						}
						else {
							JOptionPane.showConfirmDialog(This, "Id is not exist!", "Modify records", JOptionPane.OK_OPTION);
						}
						// disconnect mysql
						try {
							database.disconnect();
						} catch (SQLException e1) {
							System.out.println("Failed to disconnect to mysql.");
						}
					}
					else {
						JOptionPane.showConfirmDialog(This, "Id should not be empty!", "Modify records", JOptionPane.OK_OPTION);
					}
				}
			}
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		update_table = new DefaultTableModel(data, columnName);
		table = new JTable(update_table);
		
		button = new JButton("Insert records");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == button) {
					
					if(isLegal()) {
						Student student = new Student();
						student.setId(id.getText().toString().trim());
						student.setName((String)table.getValueAt(0,0).toString().trim());
						student.setSex((String)table.getValueAt(0,1).toString().trim());					
						student.setAge(Integer.parseInt(table.getValueAt(0,2).toString().trim()));
						student.setSdept((String)table.getValueAt(0,3).toString().trim());
						
						// insert a student information to mysql
						try {
							Database.connect();
						} catch (SQLException e1) {
							System.out.println("Failed to connect to mysql.");
						}
						database = new Database();
						if(!student.getId().equals("")) {
							if(database.isExist(student.getId())) {
								if(database.update(student)) {
									JOptionPane.showConfirmDialog(This, "Modify Successfully!", "Modify records", JOptionPane.OK_OPTION);
								}
							}
							else {
								JOptionPane.showConfirmDialog(This, "Failed to Modify, this id is exist!", "Modify records", JOptionPane.OK_OPTION);
							}
						}
						else {
							JOptionPane.showConfirmDialog(This, "Id should not be empty!", "Modify records", JOptionPane.OK_OPTION);
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
		box.add(id);
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
		List<String> strArr = Arrays.asList(columnName);
		List<String> arrayList = new ArrayList<String>(strArr);
		arrayList.remove("id");
		columnName = (String[]) arrayList.toArray(new String[arrayList.size()]);
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
				JOptionPane.showConfirmDialog(this, table.getColumnName(i)+" should not be empty!", "Modify records", JOptionPane.OK_OPTION);
				isEmpty = true;
				break;
			}
		}
		if(!isEmpty) {
			try {
				String age = (String)table.getValueAt(0,2).toString().trim();
				Integer.parseInt(age);
				legalStatus = true;
			} catch(NumberFormatException ee) {
				System.err.println("Age should be integer!");
				JOptionPane.showConfirmDialog(this, "Age should be integer!", "Modify records", JOptionPane.OK_OPTION);
			}
		}
		
		return legalStatus;
	}
}
