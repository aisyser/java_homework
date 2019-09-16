package program1.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import program1.Model.Student;

public class Database {
	
	static Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private ResultSetMetaData metaData = null;
	private String [] columnName;
	// connect to mysql
	public static void connect() throws SQLException {
		
		DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
		connection = DriverManager.getConnection("jdbc:mysql://47.97.180.216:3306/program1","root","helloworld");
		
	}
	
	// disconnect to mysql
	public static void disconnect() throws SQLException {
		if(connection != null) {
			connection.close();
		}
	}
	
	// return columnName
	public String[] getColumnName() {
		if(columnName == null) {
			System.out.println("please query!");
			return null;
		}
		return columnName;
	}
	
	// query all students information and table columns
	public List<Student> query() {
		List<Student> students= new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement("select * from student");
			resultSet = preparedStatement.executeQuery();
			// save columnName
			metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			columnName = new String[columnCount];
			for(int i=1; i <= columnCount; i++) {
				columnName[i-1] = metaData.getColumnName(i);
			}
			while(resultSet.next()) {
				
				Student student = new Student();
				student.setId(resultSet.getString(1));
				student.setName(resultSet.getString(2));
				student.setSex(resultSet.getString(3));
				student.setAge(resultSet.getInt(4));
				student.setSdept(resultSet.getString(5));
				students.add(student);
			}

		} catch (SQLException e) {
			System.out.println("Failed to query all students information!");
		} finally {
			close();
		}
		return students;
	}
	
	// query a student information
	public boolean query(Student student) {
		boolean status = false;
		String id = student.getId();
		try {
			
			preparedStatement = connection.prepareStatement("select * from student where id=?");
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				student.setName(resultSet.getString(2));
				student.setSex(resultSet.getString(3));
				student.setAge(resultSet.getInt(4));
				student.setSdept(resultSet.getString(5));
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to query student information with "+id+" id!");
		} finally {
			close();
		}
		return status;
	}
	//	is the student exist?
	public boolean isExist(String id) {
		boolean existStatus = false;
		try {
			
			preparedStatement = connection.prepareStatement("select * from student where id=?");
			preparedStatement.setObject(1, id);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				existStatus = true;
			}		
		} catch (SQLException e) {
			System.out.println("Failed to identify student information!");
		} finally {
			close();
		}
		
		return existStatus;
	}
	
	//	insert a student information
	public boolean insert(Student student) {
		boolean insertStatus = false;
		try {
			
			preparedStatement = connection.prepareStatement("insert into student values(?,?,?,?,?)");
			preparedStatement.setObject(1, student.getId());
			preparedStatement.setObject(2, student.getName());
			preparedStatement.setObject(3, student.getSex());
			preparedStatement.setObject(4, student.getAge());
			preparedStatement.setObject(5, student.getSdept());
			int rowCount = preparedStatement.executeUpdate();
			if(rowCount>0) {
				insertStatus = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to insert student information!");
		}
		return insertStatus;
	}
	
	//	update a student information
	public boolean update(Student student) {
		boolean updateStatus = false;
		String id = student.getId();
		try {
			
			preparedStatement = connection.prepareStatement("update student set name=?, sex=?, age=?, sdept=? where id=?");		
			preparedStatement.setObject(1, student.getName());
			preparedStatement.setObject(2, student.getSex());
			preparedStatement.setObject(3, student.getAge());
			preparedStatement.setObject(4, student.getSdept());
			preparedStatement.setObject(5, student.getId());
			int rowCount = preparedStatement.executeUpdate();
			if(rowCount>0) {
				updateStatus = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to update student information with "+id+" id!");
		} finally {
			close();
		}
		return updateStatus;
	}
	
	private void close() {
		
			try {
				if(resultSet != null) {
					resultSet.close();
				}
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				
			} catch (SQLException e) {
				System.out.println("Failed to close resultSet or preparedStatement!");
			}
		
	}
//	public static void main(String args[]) throws SQLException {
//		Database.connect();
//		Database db = new Database();
//		ArrayList<Student> students = (ArrayList)db.query();
//		for(Student student : students) {
//			System.out.println(student.getId()+" "+student.getName()+" "+student.getSex()+" "+student.getAge()+" "+student.getSdept());
//		}
		
//		Student student = new Student();
//		student.setId("a");
//		if(db.query(student)){
//			System.out.println(student.getId()+" "+student.getName()+" "+student.getSex()+" "+student.getAge()+" "+student.getSdept());
//		}
		
		
//		Student student = new Student();
//		student.setId("n");
//		student.setName("jiuyue");
//		student.setSex("female");
//		student.setAge(20);
//		student.setSdept("math");
//		if(db.insert(student)) {
//			System.out.println("Succeed in inserting a student information!");
//		}
//		Student student = new Student();
//		student.setId("f");
//		student.setName("zhoubo");
//		student.setSex("male");
//		student.setAge(21);
//		student.setSdept("math");
//		if(db.update(student)) {
//			System.out.println("Succeed in updating a student information!");
//		}
//		Database.disconnect();
//	}
}
