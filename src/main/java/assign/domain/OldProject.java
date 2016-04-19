package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;



@XmlRootElement(name = "oldproject")
@XmlAccessorType(XmlAccessType.FIELD)
public class OldProject {
	@XmlElement
	private String name;

	@XmlElement
	private String description;
	
	int projId;
		
	public void setId(int id){
		this.projId = id;
	}
	
	public int getId(){
		return this.projId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
//	public List<String> getLink() {
//        return link;
//    }
// 
//    public void setLink(List<String> link) {
//        this.link = link;
//    }
    
    public void setDescription(String desc){
    	this.description = desc;
    }
    
    public String getDescription(){
    	return this.description;
    }
    
    
}
