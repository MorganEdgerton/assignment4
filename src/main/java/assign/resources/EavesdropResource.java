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
//import assign.services.EavesdropService;
//import assign.services.ProjectService;
//import assign.services.ProjectServiceImpl;
import assign.services.DBLoader;


@Path("/projects")
public class EavesdropResource {
	
	//EavesdropService eavesdropService;
	DBLoader dbLoader;
	
	//CourseStudentService courseStudentService;
	//ProjectService projectService;
	
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
   // @Consumes("application/xml")
	@Consumes("application/xml")
	public Response createProject(Project p) {
		System.out.println("tryna POST"); //REMOVE
		
		if(p ==null){
			System.out.println("p is null");
		}else{
			System.out.println("p is here!");
			System.out.println(p.getName());
			p.setMeetings(null);
		}
		
		//input validation
		if(p.getName().equals("") || p.getDescription().equals("")){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    try {
	    	System.out.println("calling dbLoader.addProject"); //REMOVE
			dbLoader.addProject(p);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    System.out.println("I died right at the end");
	    return Response.created(URI.create(BASE_URI + "/projects/" + p.getId())).build();

	}
	
	@POST
	@Path("/{projectId}/meetings")
   // @Consumes("application/xml")
	@Consumes("application/xml")
	public Response createMeeting(@PathParam("projectId") long projId, Meeting m) {
		System.out.println("tryna POST a meeting");
		
		//input validation
		if(m.getName().equals("") || !(m.getYear()== -1)  ){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		
	    try {
	    	Project p = dbLoader.getProject(projId);
	    	m.setProject(p);
			//dbLoader.addMeeting(m); //TODO: write addMeeting & add projectID param
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    System.out.println("I died right at the end");
	    return Response.created(URI.create(BASE_URI + "/projects/" + m.getId())).build();

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
	
//	@PUT
//	@Path("/{projectId}")
//	@Consumes("application/xml")
//	   public Response updateProject(@PathParam("projectId") int projId, Project update) throws Exception {
//		  System.out.println("tryna PUT");
//		  
//		  //input validation
//		  if(update.getProjName().equals("") || update.getProjDescription().equals("")){
//			  throw new WebApplicationException(Response.Status.BAD_REQUEST);
//		  }
//		  
//		  update.setId(projId);  
//		  Project current = projectService.getProject_correct(projId);
//	    
//		  if (current == null) throw new WebApplicationException(Response.Status.NO_CONTENT);
//		  System.out.println("current= " + current.getProjName() + " DESCRIPTION: " + current.getProjDescription());
//
//		  current.setProjDescription(update.getProjDescription());
//		  projectService.updateProjectDb(current);
//		  
//		  return Response.ok().build(); 
//	 }
	
	
//	@GET
//	@Path("/project")
//	@Produces("application/xml")
//	public StreamingOutput getProject() throws Exception {
//		
//		final Project heat = new Project();
//		heat.setName("%23heat");
//		heat.setLink(new ArrayList<String>());
//		heat.getLink().add("l3");
//		heat.getLink().add("l2");		
//		
//		//throw new WebApplicationException();
//		
//		final NotFound notFound = new NotFound();
//		notFound.setError("Project non-existent-project does not exist");
//		
//	    return new StreamingOutput() {
//	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//	            outputCourses(outputStream, notFound);
//	         }
//	      };
//	     
//	}		
	
//    public Project readProject(int projId, InputStream is) {
//        try {
//           System.out.println("READING PROJECT");
//           DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//           Document doc = builder.parse(is);//not working well
//           Element root = doc.getDocumentElement();
//           System.out.println("doc= " + doc);
//           Project p = new Project();
//           System.out.println("rootname= " + root.getAttribute("id"));
//           if (root.getAttribute("name") != null && root.getAttribute("description") != null){
//        	   p.setId(projId);
//        	   System.out.println("we got root stuff");
//           }
//                         
//           NodeList nodes = root.getChildNodes();
//           for (int i = 0; i < nodes.getLength(); i++) {
//              Element element = (Element) nodes.item(i);
//              if (element.getTagName().equals("name")) {
//                 p.setName(element.getTextContent());
//                 System.out.println("p.name=" + p.getName());
//              }
//              else if (element.getTagName().equals("description")) {
//                 p.setDescription(element.getTextContent());
//                 System.out.println("p.name=" + p.getName());
//              }
//           }
//           return p;
//        }=
//        catch (Exception e) {
//           throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
//        }
//     }
    	
	
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