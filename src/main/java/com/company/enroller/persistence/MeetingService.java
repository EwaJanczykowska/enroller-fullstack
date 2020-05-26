package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting ORDER BY LOWER(title)";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    public Meeting findById(long id) {
        return (Meeting) getSession().get(Meeting.class, id);
    }

    public Meeting add(Meeting meeting) {
        Transaction transaction = getSession().beginTransaction();
        getSession().save(meeting);
        transaction.commit();
        return meeting;
    }

    public Meeting updateMeeting(Meeting meeting) {
        Transaction transaction = getSession().beginTransaction();
        getSession().update(meeting);
        transaction.commit();
        return meeting;
    }

    public void delete(Meeting meeting) {
        Transaction transaction = getSession().beginTransaction();
        getSession().delete(meeting);
        transaction.commit();
    }

    public Collection<Meeting> findMeetings(String title, String description) {
        if (title==null) {
            title = "";
        }
        if (description==null) {
            description = "";
        }
        String hql = "FROM Meeting WHERE title LIKE :titleParam AND description LIKE :descriptionParam ORDER BY LOWER(title)";
        Query query = getSession().createQuery(hql);
        query.setString("titleParam", "%" + title + "%");
        query.setString("descriptionParam", "%" + description + "%");
        return query.list();
    }

    public Collection<Meeting> findByParticipant(String participant) {
        String hql = "SELECT m FROM Meeting AS m INNER JOIN m.participants AS mp WHERE mp.login = :participantParam";
        Query query = getSession().createQuery(hql);
        query.setString("participantParam", participant);
        return query.list();
    }

    private Session getSession() {
        return connector.getSession();
    }
}
