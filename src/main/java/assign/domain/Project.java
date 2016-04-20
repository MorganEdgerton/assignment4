package assign.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "projects" )
public class Project {
	
	private int id;
    private String projName;
    private String projDescription;
    private Set<Meeting> meetings;

    public Project() {
    	// this form used by Hibernate
    }
    
    public Project(String projName) {
    	this.projName = projName;
    }
    
    @Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
    public int getId() {
		return id;
    }

    private void setId(int id) { //TODO: Does int work instead of Long??
		this.id = id;
    }
    
    @Column(name="project")
    public String getProjName() {
		return projName;
    }

    public void setProjName(String projName) {
		this.projName = projName;
    }
    
    @Column(name="project")
    public String getProjDescription() {
		return projName;
    }

    public void setProjDescription(String projDescription) {
		this.projDescription = projDescription;
    }
    
    @OneToMany(mappedBy="project")
    @Cascade({CascadeType.DELETE})
    public Set<Meeting> getMeetings() {
    	return this.meetings;
    }
    
    public void setMeetings(Set<Meeting> meetings) {
    	this.meetings = meetings;
    }
}