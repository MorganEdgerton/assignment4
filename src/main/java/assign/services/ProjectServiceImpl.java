//package assign.services;
//
//import java.io.InputStream;
//import java.sql.Array;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import javax.sql.DataSource;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Response;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import assign.domain.Project;
//
//public class ProjectServiceImpl implements ProjectService {
//
//	String dbURL = "";
//	String dbUsername = "";
//	String dbPassword = "";
//	DataSource ds;
//
//	// DB connection information would typically be read from a config file.
//	public ProjectServiceImpl(String dbUrl, String username, String password) {
//		this.dbURL = dbUrl;
//		this.dbUsername = username;
//		this.dbPassword = password;
//		
//		ds = setupDataSource();
//	}
//	
//	public DataSource setupDataSource() {
//        BasicDataSource ds = new BasicDataSource();
//        ds.setUsername(this.dbUsername);
//        ds.setPassword(this.dbPassword);
//        ds.setUrl(this.dbURL);
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        return ds;
//    }
//	
//	public Project addProject(Project p) throws Exception {
//		Connection conn = ds.getConnection();
//		
//		String insert = "INSERT INTO projects1(name, description) VALUES(?, ?)";
//		PreparedStatement stmt = conn.prepareStatement(insert,
//                Statement.RETURN_GENERATED_KEYS);
//		
//		stmt.setString(1, p.getName());
//		stmt.setString(2, p.getDescription());
//		
//		int affectedRows = stmt.executeUpdate();
//
//        if (affectedRows == 0) {
//        	throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//        
//        ResultSet generatedKeys = stmt.getGeneratedKeys();
//        if (generatedKeys.next()) {
//        	p.setId(generatedKeys.getInt(1));
//        	System.out.println("projectId: " + p.getId());
//        }
//        else {
//        	throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//        
//        // Close the connection
//        conn.close();
//        
//		return p;
//	}
//	
//	public Project updateProjectDb(Project p) throws Exception {
//		Connection conn = ds.getConnection();
//		
//		String update = "UPDATE projects1 SET description=? WHERE projects1.project_id=?";
//		PreparedStatement stmt = conn.prepareStatement(update,
//                Statement.RETURN_GENERATED_KEYS);
//		
//		stmt.setString(1, p.getDescription());
//		stmt.setInt(2, p.getId());
//		
//		
//		int affectedRows = stmt.executeUpdate();
//
//        if (affectedRows == 0) {
//            throw new WebApplicationException(Response.Status.NO_CONTENT);
//        }
//        
//        // Close the connection
//        conn.close();
//        
//		return p;
//	}
//
//
//    public Project getProject_correct(int projId) throws Exception {
//    	System.out.println("GETTING PROJECT");
//    	String query = "select * from projects1 where project_id=?";
//    	Connection conn = ds.getConnection();
//    	PreparedStatement s = conn.prepareStatement(query);
//    	s.setString(1, String.valueOf(projId));
//
//    	ResultSet r = s.executeQuery();
//	
//    	if (!r.next()) {
//    		System.out.println("NULL RESULTSET");
//    		return null;
//    	}
//	
//    	Project p = new Project();
//    	p.setDescription(r.getString("description"));
//    	p.setName(r.getString("name"));
//    	p.setId(r.getInt("project_id"));
//    	return p;
//    }
//    
//    
//    public Boolean deleteProjectDb(int projId) throws Exception {
//    
//    	System.out.println("DELETING PROJECT");
//    	String query = "delete from projects1 where project_id=?";
//    	Connection conn = ds.getConnection();
//    	PreparedStatement s = conn.prepareStatement(query);
//    	s.setString(1, String.valueOf(projId));
//    	
//    	int r = s.executeUpdate();
//    	System.out.println("r= " + r);
// 
//    	if(r==0){
//    		return false;
//    	}
//    	return true;
//    	
//    }
//    
//    
//
//}
