package com.dut.doctorcare.dao.impl;

import com.dut.doctorcare.dao.AbstractSoftDeleteHibernateDao;
import com.dut.doctorcare.dao.iface.ScheduleDao;
import com.dut.doctorcare.model.Schedule;
import com.dut.doctorcare.model.Shifts;
import org.hibernate.LockMode;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class HibernateScheduleDao extends AbstractSoftDeleteHibernateDao<Schedule> implements ScheduleDao {
    public HibernateScheduleDao() {
        super(Schedule.class);
    }
    @Override
    public List<Schedule> findAllExpiredSchedules(LocalDate today) {
        String query = "SELECT s FROM Schedule s WHERE s.date < :today AND s.isActive = true AND s.status = :status";
        return getCurrentSession().createQuery(query, Schedule.class)
                .setParameter("today", today)
                .setParameter("status", Schedule.Status.EMPTY)
                .setLockMode("s", LockMode.PESSIMISTIC_READ)
                .getResultList();
    }

    @Override
    public List<Schedule> findAllByDate(LocalDate date) {
        String query = "SELECT s FROM Schedule s WHERE s.date = :date AND s.isActive = true";
        return getCurrentSession().createQuery(query, Schedule.class)
                .setParameter("date", date)
                .getResultList();
    }
    @Override
    public void deleteAll(List<Schedule> schedules) {
        schedules.forEach(schedule -> {
            getCurrentSession().remove(schedule);
            getCurrentSession().flush();
        });
    }
}
