package assign.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.mapping.Set;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mysql.fabric.xmlrpc.base.Array;

import assign.domain.Meeting;

//import com.restfully.shop.domain.*;  //?? original= com.restufully.shop.domain.Customer

import assign.domain.Project;
import assign.services.DBLoader;


@Path("/projects")
public class EavesdropResource {
	
	DBLoader dbLoader;
	
	Array allIDs;
	
	String password;
	String username;
	String dburl;	
	
	private static final String BASE_URI = "http://localhost:3306/eavesdrop_projects";
	
	public EavesdropResource(@Context ServletContext servletContext) {
		System.out.println("Creating eavesdropResource");
		dbLoader = new DBLoader();
	
	}
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dburl);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);		
		return "Hello world " + dburl + " " + username + " " + password;		
	}
	
	@GET
	@Path("/helloeavesdrop")
	@Produces("text/html")
	public String helloEavesdrop() {
		System.out.println("in hello");
		return "helloEavesdrop";		
	}	
	
	
	@POST
	@Consumes("application/xml")
	public Response createProject(Project p) {	
			
		//check for 'spaces only'
		String pName = p.getName().replaceAll("\\s","");
		String pDescription = p.getDescription().replaceAll("\\s","");
		
		//input validation
		if(pName.equals("") || pDescription.equals("")){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    try {
	    	p.setMeetings(null);
	    	dbLoader.addProject(p);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    return Response.created(URI.create(BASE_URI + "/projects/" + p.getId())).build();
	
	}
	
	@POST
	@Path("/{projectId}/meetings")
	@Consumes("application/xml")
	public Response createMeeting(@PathParam("projectId") long projId, Meeting m) {

		//check for 'spaces only'
		String mName = m.getName().replaceAll("\\s","");
		//String mYear = m.getYear()..replaceAll("\\s","");
		
		//input validation
		if(mName.equals("") || (m.getYear()== -1)  ){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
	    try {
	    	Project p = dbLoader.getProject(projId);
	    	//if(p != null){
	    		m.setProject(p);
	    		dbLoader.addMeetingsToProject(m);
//	    	}else{
//	    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
//	    	}
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    return Response.created(URI.create(BASE_URI + "/projects/" + projId + "/meetings/" + m.getId())).build();

	}
	
	@GET
	@Path("/{projectId}")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("projectId") Long projectId
			) throws Exception {
		
		System.out.println("projectID "+ projectId);
		
		Project p = new Project();
	    Long projectID = Long.valueOf(projectId).longValue();
	    System.out.println("GET DAT PROJECT");
	    
	    
	    
	    if(( p = dbLoader.getProject(projectId))==null){
	    	System.out.println("p is null");
	        throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	    else{	    
		
	    	System.out.println("p ID " +p.getId() + " p NAME " + p.getName() + " p DESCRIPTION " + p.getDescription());
//	    	final java.util.Set<Meeting> meetings = p.getMeetings();
//	    	if(meetings != null){
//	    		System.out.println("Got meetings! ");
//	    		for(Meeting m : meetings){
//	    			System.out.println("m.getName() = " + m.getName());
//	    		}
//	    	}
	    	final Project outputProject = p; //hack
	
	    	return new StreamingOutput() {
	    		public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	    			outputProject(outputStream, outputProject);
	    		}
	    	};
	    }
	}	

	@DELETE
	@Path("/{projectId}")
	@Produces("application/xml")
	public Response deleteProject(@PathParam("projectId") String projectId) throws Exception {
		System.out.println("projectID "+ projectId);
		
		Long projectID =  (long) Integer.parseInt(projectId);
		dbLoader.deleteProject(projectID);
//		if(!dbLoader.deleteProject(projectID)){ //TODO: make bad return state
//			throw new WebApplicationException(Response.Status.NOT_FOUND);
//		}	
		
		return Response.ok().build();
		
	}
	
	@PUT
	@Path("/{projectId}/meetings/{meetingId}")
	@Consumes("application/xml")
	   public Response updateMeeting(@PathParam("projectId") Long projId, @PathParam("meetingId") Long meetingId, Meeting update) throws Exception {
		  System.out.println("tryna PUT a Meeting");
		  //TODO: get meeting ID and give ID
		  //input validation
		  if(update.getName().equals("") || (update.getYear() == -1) ){
			  throw new WebApplicationException(Response.Status.BAD_REQUEST);
		  }
		  
		  Project p = dbLoader.getProject(projId);
		  Meeting current =  dbLoader.getMeeting(meetingId);
		  if (p == null || current == null) throw new WebApplicationException(Response.Status.NO_CONTENT);

		  update.setProject(p);
		  
	      dbLoader.updateMeeting(current, update);		  
	  
		  return Response.ok().build(); 
	 }
    	
	
	  protected void outputProject(OutputStream os, Project proj) throws IOException { //TODO: implement getId() and getProj() and uncomment
	      PrintStream writer = new PrintStream(os);
	      writer.println("<project id=\"" + proj.getId() + "\">");
	      writer.println("   <name>" + proj.getName() + "</name>");
	      writer.println("   <description>" + proj.getDescription() + "</description>");
	   
	      if(proj.getMeetings() != null){
	    	  System.out.println("printing meetings xml");
	    	  writer.println("   <meetings>");
	    	  for(Meeting m : proj.getMeetings()){ 
		    	  writer.println("      <meeting id=\"" + m.getId() + "\">");
		    	  writer.println("         <name>" + m.getName() + "</name>");
		    	  writer.println("         <year>" + m.getYear() + "</year>");
		    	  writer.println("      </meeting>");
		      } 
	    	  writer.println("   </meetings>");
	      }
	      
	      writer.println("</project>");
	      
	      
	   }
	  
}