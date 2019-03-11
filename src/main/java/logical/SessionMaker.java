package logical;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class SessionMaker {
    private volatile static SessionMaker _instance = new SessionMaker();

    private SessionMaker() {
    }

    public static SessionMaker getInstance() {
        return _instance;
    }

    public static SessionFactory getSession() {
        return new Configuration().configure().buildSessionFactory();
    }
}