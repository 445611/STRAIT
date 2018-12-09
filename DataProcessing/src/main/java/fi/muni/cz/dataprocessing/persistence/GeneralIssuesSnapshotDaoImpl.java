package fi.muni.cz.dataprocessing.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GeneralIssuesSnapshotDaoImpl implements GeneralIssuesSnapshotDao {
    
    private SessionFactory sessionFactory;
    private Session session;
    
    /**
     * Initialize session factory.
     */
    public GeneralIssuesSnapshotDaoImpl() {
        getSessionFactory();
    }

    @Override
    public List<GeneralIssuesSnapshot> getAllSnapshotsForUserAndRepository(String user, String repository) {
        beginTransaction();
        Query query = session.createQuery("FROM GeneralIssuesSnapshot "
                + "WHERE userName = ? AND repositoryName = ?");
        List<GeneralIssuesSnapshot> result = query.setString(0, user).setString(1, repository).list();
        endTransaction();
        return result;
    }

    @Override
    public GeneralIssuesSnapshot getSnapshotByName(String name) {
        beginTransaction();
        Query query = session.createQuery("FROM GeneralIssuesSnapshot WHERE snapshotName = ?");
        List<GeneralIssuesSnapshot> result = query.setString(0, name).list();
        GeneralIssuesSnapshot snapshot;
        if (result.isEmpty()) {
            snapshot = null;
        } else {
            snapshot = (GeneralIssuesSnapshot) result.get(0);
        }
        endTransaction();
        return snapshot;
    }
    
    @Override
    public void save(GeneralIssuesSnapshot snapshot) {
        beginTransaction();
        session.merge(snapshot);
        endTransaction();
    } 
    
    @Override
    public List<GeneralIssuesSnapshot> getAllSnapshots() {
        beginTransaction();
        Query query = session.createQuery("FROM GeneralIssuesSnapshot");
        List<GeneralIssuesSnapshot> list = query.list();
        endTransaction();
        return list;
    }
    
    private void beginTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }
    
    private void endTransaction() {
        session.getTransaction().commit();
        session.flush();
    }
    
    private void getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
            configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
}