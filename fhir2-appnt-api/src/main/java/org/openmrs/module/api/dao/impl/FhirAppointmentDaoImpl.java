package org.openmrs.module.api.dao.impl;

import lombok.AccessLevel;
import org.hibernate.SessionFactory;
import lombok.Setter;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.fhir2.api.dao.FhirAppointmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hibernate.criterion.Restrictions.eq;

@Component
@Setter(AccessLevel.PACKAGE)
public class FhirAppointmentDaoImpl implements FhirAppointmentDao<Appointment> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Appointment getAppointmentByUuid(String uuid) {
        return (Appointment) sessionFactory.getCurrentSession().createCriteria(Appointment.class).add(eq("uuid", uuid))
                .uniqueResult();
    }
}
