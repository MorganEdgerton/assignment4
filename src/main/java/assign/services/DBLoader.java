package assign.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import assign.domain.Meeting;
import assign.domain.Project;

import java.util.logging.*;

public class DBLoader {
	private SessionFactory sessionFactory;
	
	Logger logger;
	
	public DBLoader() {
		// A SessionFactory is set up once for an application
		System.out.println("Gonna config in DBLoader");
		sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
        System.out.println("I configed");
        
        logger = Logger.getLogger("EavesdropReader");
	}
	
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData.");
	}
	
	public Long addMeeting(String name, int year) throws Exception { //get projId into meeting creation
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long meetingId = null;
		try {
			tx = session.beginTransaction();
			Meeting newMeeting = new Meeting(name, year); 
			session.save(newMeeting);
		    meetingId = newMeeting.getId();
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return meetingId;
	}
	
	public Long addProject(Project p) throws Exception {
		System.out.println("in DBLoader.addProject()");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long projId = null;
		try {
			
			tx = session.beginTransaction();
			session.save(p);
		    projId = (long) p.getId();
		    System.out.println("Long projId = "+ projId );
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				System.out.println("here");
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return projId;
	}
	
//	public Long addMeetingAndProject(String title, String projectTitle) throws Exception {
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//		Long meetingId = null;
//		try {
//			tx = session.beginTransaction();
//			Meeting newMeeting = new Meeting( title, new Date() );
//			Project proj = new Proj(projectTitle);
//			newMeeting.setProject(proj);
//			session.save(proj);
//			session.save(newMeeting);
//		    meetingId = newMeeting.getId();
//		    tx.commit();
//		} catch (Exception e) {
//			if (tx != null) {
//				tx.rollback();
//				throw e;
//			}
//		}
//		finally {
//			session.close();			
//		}
//		return meetingId;
//	}
//	
	public Long addMeetingsToProject(Meeting m) throws Exception { //TODO: fix meeting param
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(m);
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return m.getId();
	}
//	
	public List<Meeting> getMeetingsForAProject(Long projId) throws Exception { //TRY: projId to id
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Meeting where project_id=" + projId; // BAD PRACTICE
		List<Meeting> meetings = session.createQuery(query).list();		
		return meetings;
	}
	
	public List<Object[]> getMeetingsForAProject(String projectName) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Meeting a join a.project c where c.projectName = :cname";		
				
		List<Object[]> meetings = session.createQuery(query).setParameter("cname", projectName).list();
		
		return meetings;
	}
	
	public Meeting getMeeting(String name) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Meeting.class).
        		add(Restrictions.eq("name", name));
		
		List<Meeting> meetings = criteria.list();
		
		if (meetings.size() > 0) {
			return meetings.get(0);			
		} else {
			return null;
		}
	}
//	
	public Project getProject(Long projId) throws Exception {

		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("id", projId));
		
		List<Project> projects = criteria.list();
		
		if (projects.size() > 0) {
			System.out.println("projects.get(0) " + projects.get(0));
			session.close();
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}  
	
	public Long updateMeeting(Meeting current, Meeting update) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(current);
			current.setName(update.getName());
			current.setYear(update.getYear());
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return current.getId();
	}

	public void deleteProject(Long projId) throws Exception {
		
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Project c where c.id = :projId";		
				
		Project p = (Project)session.createQuery(query).setParameter("projId", projId).list().get(0);
		
        session.delete(p);

        session.getTransaction().commit();
        session.close();		
	}
//	
//	
	public Meeting getMeeting(Long meetingId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Meeting.class).
        		add(Restrictions.eq("id", meetingId));
		
		List<Meeting> meetings = criteria.list();
		
		return meetings.get(0);		
	}
}
