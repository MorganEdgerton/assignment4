package assign.services;

import java.io.InputStream;

import com.mysql.fabric.xmlrpc.base.Array;

import assign.domain.Project;


public interface ProjectService {

	public Project addProject(Project p) throws Exception;
	
	public Project getProject_correct(int projId) throws Exception;
	
	public Boolean deleteProjectDb(int projId) throws Exception;
	
	public java.sql.Array getIDs() throws Exception;
	
	public Project updateProjectDb(Project p) throws Exception;
	
}
