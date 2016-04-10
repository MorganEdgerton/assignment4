package assign.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "course")
public class Course {
	
	private String name;
	private String department;
	String course_num;
	int course_id;
	
	public String getName() {
		return name;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCourseNum() {
		return course_num;
	}
	
	public void setCourseNum(String course_num) {
		this.course_num = course_num;
	}
	
	public int getCourseId() {
		return course_id;
	}
	
	public void setCourseId(int course_id) {
		this.course_id = course_id;
	}
}