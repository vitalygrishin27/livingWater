package hibernateUtils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateSessionFactoryUtil {
    private volatile static HibernateSessionFactoryUtil _instance = new HibernateSessionFactoryUtil();

    private HibernateSessionFactoryUtil() {
    }

    public static HibernateSessionFactoryUtil getInstance() {
        return _instance;
    }

    public static SessionFactory getSession() {
        return new Configuration().configure().buildSessionFactory();
    }
}