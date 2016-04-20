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
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mysql.fabric.xmlrpc.base.Array;


//import com.restfully.shop.domain.*;  //?? original= com.restufully.shop.domain.Customer

import assign.domain.NotFound;
import assign.domain.Project;
import assign.services.EavesdropService;
import assign.services.ProjectService;
import assign.services.ProjectServiceImpl;

@Path("/projects")
public class EavesdropResource {
	
	EavesdropService eavesdropService;
	
	//CourseStudentService courseStudentService;
	ProjectService projectService;
	
	Array allIDs;
	
	String password;
	String username;
	String dburl;	
	
	private static final String BASE_URI = "http://localhost:3306/eavesdrop_projects";
	
	public EavesdropResource(@Context ServletContext servletContext) {
		dburl = servletContext.getInitParameter("DBURL");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.projectService = new ProjectServiceImpl(dburl, username, password);
	
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
		return this.eavesdropService.getData();		
	}	
	
	
	@POST
    @Consumes("application/xml")
	public Response createProject(Project p) {
		System.out.println("tryna POST");
		
		//input validation
		if(p.getProjName().equals("") || p.getProjDescription().equals("")){
			System.out.println("yeh yeh!");
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    try {
			projectService.addProject(p);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	    System.out.println("I died right at the end");
	    return Response.created(URI.create(BASE_URI + "/projects/" + p.getId())).build();

	}
	
	
	@GET
	@Path("/{projectId}")
	@Produces("application/xml")
	public StreamingOutput getProject(@PathParam("projectId") String projectId
			) throws Exception {
		
		System.out.println("projectID "+ projectId);
		
		Project p = new Project();
	    int projectID = Integer.parseInt(projectId);
	    System.out.println("GET DAT PROJECT");
	    p = projectService.getProject_correct(projectID);
	    
	    if((p = projectService.getProject_correct(projectID))==null){
	    	System.out.println("p is null");
	        throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	    else{	    
		
	    	System.out.println("p ID " +p.getId() + " p NAME " + p.getProjName() + " p DESCRIPTION " + p.getProjDescription());
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
		
		int projectID = Integer.parseInt(projectId);
		if(!projectService.deleteProjectDb(projectID)){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}	
		
		return Response.ok().build();
		
	}
	
	@PUT
	@Path("/{projectId}")
	@Consumes("application/xml")
	   public Response updateProject(@PathParam("projectId") int projId, Project update) throws Exception {
		  System.out.println("tryna PUT");
		  
		  //input validation
		  if(update.getProjName().equals("") || update.getProjDescription().equals("")){
			  throw new WebApplicationException(Response.Status.BAD_REQUEST);
		  }
		  
		  update.setId(projId);  
		  Project current = projectService.getProject_correct(projId);
	    
		  if (current == null) throw new WebApplicationException(Response.Status.NO_CONTENT);
		  System.out.println("current= " + current.getProjName() + " DESCRIPTION: " + current.getProjDescription());

		  current.setProjDescription(update.getProjDescription());
		  projectService.updateProjectDb(current);
		  
		  return Response.ok().build(); 
	 }
	
	
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
//        }
//        catch (Exception e) {
//           throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
//        }
//     }
    	
	
	  protected void outputProject(OutputStream os, Project proj) throws IOException { //TODO: implement getId() and getProj() and uncomment
	      PrintStream writer = new PrintStream(os);
	      writer.println("<project id=\"" + proj.getId() + "\">");
	      writer.println("   <name>" + proj.getProjName() + "</name>");
	      writer.println("   <description>" + proj.getProjDescription() + "</description>");
	      writer.println("</project>");
	   }
	  
}