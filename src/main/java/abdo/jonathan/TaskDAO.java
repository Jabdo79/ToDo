package abdo.jonathan;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * Created by Jonathan Abdo
 * Task Data Access Object
 */
public class TaskDAO {
    private static SessionFactory factory;

    private static void setupFactory() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Configuration configuration = new Configuration();

        // hibernate configuration file
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Task.class);

        // Since version 4.x, service registry is being used
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        // Create session factory instance
        factory = configuration.buildSessionFactory(serviceRegistry);
    }

    //Get all tasks from db
    //If empty return null
    public static List<Task> getAllTasks(){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task");
        List<Task> results = query.list();
        hibernateSession.close();

        if (results.isEmpty())
            return null;

        return results;
    }

    //Get tasks that match the chosen status (active/true or completed/false)
    //If empty return null
    public static List<Task> getTasksByStatus(boolean active){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task WHERE active = :active");
        List<Task> results = query.setParameter("active", active).list();
        hibernateSession.close();

        if (results.isEmpty())
            return null;

        return results;
    }

    //Check for task by description, ignoring case to prevent duplicates
    public static boolean containsTask(String description){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task WHERE lower(description) = :description ");
        List<Task> results = query.setParameter("description", description.toLowerCase()).list();
        hibernateSession.close();

        if (results.isEmpty())
            return false;

        return true;
    }

    //Get task from db by id, return null if it doesn't exist
    public static Task getTask(int id){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task WHERE id = :id ");
        List<Task> results = query.setParameter("id", id).list();
        hibernateSession.close();

        if (results.isEmpty())
            return null;

        return results.get(0);
    }

    //Add task to db
    public static void addTask(Task task) {
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        hibernateSession.getTransaction().begin();

        hibernateSession.save(task);

        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    //Update task in db (used for status change)
    public static void updateTask(Task task){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        hibernateSession.getTransaction().begin();

        hibernateSession.update(task);

        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    //Remove task from db
    public static void removeTask(Task task){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        hibernateSession.getTransaction().begin();

        hibernateSession.delete(task);

        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    //Remove all tasks where active is false(completed)
    //Return true if items were deleted, false otherwise
    public static boolean removeCompletedTasks(){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task WHERE active = false ");
        List<Task> results = query.list();

        if(!results.isEmpty()) {
            hibernateSession.getTransaction().begin();
            for (Task task : results) {
                hibernateSession.delete(task);
            }
            hibernateSession.getTransaction().commit();
            hibernateSession.close();
            return true;
        }else
            return false;
    }

    //Get tasks where active is true and return size(count) of results
    public static int getActiveTaskCount(){
        if (factory == null)
            setupFactory();

        Session hibernateSession = factory.openSession();
        Query query = hibernateSession.createQuery("FROM Task WHERE active = true ");
        List<Task> results = query.list();

        return results.size();
    }
}