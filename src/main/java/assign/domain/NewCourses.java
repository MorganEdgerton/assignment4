package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "newcourses")
public class NewCourses {

    private List<NewCourse> newCourses = null;
 
    public List<NewCourse> getCourses() {
        return newCourses;
    }
 
    public void setCourses(List<NewCourse> newCourses) {
        this.newCourses = newCourses;
    }	
}
