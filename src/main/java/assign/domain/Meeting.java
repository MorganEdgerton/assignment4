package assign.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "meeting")
@Entity
@Table( name = "meetings" )
public class Meeting {
	
	private Long id;

    private String name;
    private int year = -1;
    private Project project;
    
    public Meeting() {
    	// this form used by Hibernate
    }
    
    public Meeting(String name, int year) {
    	// for application use, to create new assignment
    	this.name = name;
    	this.year = year;
    }
    
    public Meeting(String name, int year, Long providedId) {
    	// for application use, to create new assignment
    	this.name = name;
    	this.year = year;
    	this.id = providedId;
    }    
    
    @Id    
	//@GeneratedValue(generator="increment")    
	//@GenericGenerator(name="increment", strategy = "increment")
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }


	@Column(name = "MEETING_YEAR")
    public int getYear() {
		return year;
    }

    public void setYear(int year) {
		this.year = year;
    }
    
    @ManyToOne
    @JoinColumn(name="project_id")
    public Project getProject() { // property named course available on this object
    	return this.project;
    }
    
    
    
    public void setProject(Project p) {
    	this.project = p;
    }

    public String getName() {
		return name;
    }

    public void setName(String name) {
		this.name = name;
    }
}
