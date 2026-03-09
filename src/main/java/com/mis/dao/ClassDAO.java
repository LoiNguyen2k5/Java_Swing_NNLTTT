package com.mis.dao;

import com.mis.entity.SchoolClass;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDAO {

    public List<SchoolClass> getAllClasses() {
        List<SchoolClass> classes = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            classes = session.createQuery("FROM SchoolClass", SchoolClass.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public void addClass(SchoolClass sc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(sc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    public void updateClass(SchoolClass sc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    public void deleteClass(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SchoolClass sc = session.get(SchoolClass.class, id);
            if (sc != null) {
                session.remove(sc);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

}