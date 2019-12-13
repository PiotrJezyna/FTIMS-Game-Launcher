package fgl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dao {
    private static SessionFactory factory;

    public static void main(String[] args) {

        try {
            factory = new Configuration().configure().buildSessionFactory();
            System.out.println("Success!");
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction tx = null;

        List users = new ArrayList();
        try {
            tx = session.beginTransaction();
            users = session.createQuery("FROM User").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
            User user = (User) iterator.next();
            System.out.print("First name: " + user.getName());
            System.out.print("  Last name: " + user.getSurname());
            System.out.print("  Email: " + user.getEmail());
            System.out.print("  Type: " + user.getType().getName());
            System.out.print("  Created games: ");
            for (Game i : user.getGames())
                System.out.print(i.getTitle() + ", ");
            System.out.print("  Installed games: ");
            for (Game i : user.getInstalledGames())
                System.out.print(i.getTitle() + ", ");
            System.out.println();
        }
/*
        try {
            tx = session.beginTransaction();
            List games = session.createQuery("FROM Game").list();
            for (Iterator iterator = games.iterator(); iterator.hasNext(); ) {
                Game game = (Game) iterator.next();
                System.out.print("Title: " + game.getTitle());
                System.out.print(" Genre: " + game.getGenres().get(0).getName());
                System.out.print("  Author: " + game.getUser().getName() + " " + game.getUser().getSurname());
                System.out.println("  Playcount: " + game.getUserCount());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }*/
    }
}
