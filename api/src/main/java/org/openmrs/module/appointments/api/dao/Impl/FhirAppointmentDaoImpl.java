package org.openmrs.module.appointments.api.dao.Impl;

import org.hibernate.SessionFactory;
import org.openmrs.module.appointments.api.dao.FhirAppointmentDao;
import org.openmrs.module.appointments.model.Appointment;

import static org.hibernate.criterion.Restrictions.eq;

public class FhirAppointmentDaoImpl implements FhirAppointmentDao {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Appointment getAppointmentByUuid(String uuid) {
        return (Appointment) sessionFactory.getCurrentSession().createCriteria(Appointment.class).add(eq("uuid", uuid))
                .uniqueResult();
    }
}
