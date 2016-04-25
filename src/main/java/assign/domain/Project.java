package assign.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "project")
@Entity
@Table( name = "projects" )
public class Project {
	
	private Long id;
    private String name;
    private String description;
    private Set<Meeting> meetings;

    public Project() {
    	// this form used by Hibernate
    }
    
    public Project(String name) {
    	System.out.println("Project constructor 1 - name");
    	this.name = name;
    	//this.meetings = new HashSet<Meeting>();
    }
    
    public Project(String name, String description, Long providedId) {
    	// for application use, to create new assignment
    	System.out.println("Project constructor 2 - name, desc, providedID");
    	this.name = name;
    	this.description = description;
    	this.id = providedId;
    	//this.meetings = new HashSet<Meeting>();
    }   
    
    @Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
		return id;
    }

    public void setId(Long id) {
		this.id = id;
    }
    
    @Column(name="project")
    public String getName() {
		return name;
    }

    public void setName(String name) {
		this.name = name;
    }
    
    @Column(name="projDescription")
    public String getDescription() {
		return description;
    }

    public void setDescription(String description) {
		this.description = description;
    }
    
    @OneToMany(fetch=FetchType.EAGER, mappedBy="project")
    @Cascade({CascadeType.DELETE})
    public Set<Meeting> getMeetings() {
    	System.out.println("In Project.getMeetings()");
    	System.out.println("this.meetings= " + this.meetings.toString());
    	if(this.meetings == null){ //TODO: remove
    		System.out.println("meetings == null");
    	}
    	return this.meetings;
    }
    
    public void setMeetings(Set<Meeting> meetings) {
    	System.out.println("setMeetings"); //remove
    	if(meetings != null){
    		this.meetings = meetings;
    	}
    	else{
    		System.out.println("no meetings"); //remove
    		this.meetings = new HashSet<Meeting>();
    	}
    }
}