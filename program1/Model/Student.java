package program1.Model;

public class Student {
	private String id;
	private String name;
	private String sex;
	private int age; // Greater than zero.
	private String sdept;
	//	constructor
	public Student(String id, String name, String sex, int age, String sdept) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.sdept = sdept;
	}
	
	public Student() {super();};
	
	// id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	// name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	// sex
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	// age
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	// sdept
	public String getSdept() {
		return sdept;
	}

	public void setSdept(String sdept) {
		this.sdept = sdept;
	}

}
