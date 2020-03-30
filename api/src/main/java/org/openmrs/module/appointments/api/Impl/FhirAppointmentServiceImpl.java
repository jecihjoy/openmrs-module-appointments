package org.openmrs.module.appointments.api.Impl;

import org.hl7.fhir.r4.model.Appointment;
import org.openmrs.module.appointments.api.FhirAppointmentService;
import org.openmrs.module.appointments.api.dao.FhirAppointmentDao;
import org.openmrs.module.appointments.api.translators.AppointmentTranslator;

public class FhirAppointmentServiceImpl implements FhirAppointmentService {

    private AppointmentTranslator appointmentTranslator;

    private FhirAppointmentDao fhirAppointmentDao;

    public AppointmentTranslator getAppointmentTranslator() {
        return appointmentTranslator;
    }

    public void setAppointmentTranslator(AppointmentTranslator appointmentTranslator) {
        this.appointmentTranslator = appointmentTranslator;
    }

    public FhirAppointmentDao getFhirAppointmentDao() {
        return fhirAppointmentDao;
    }

    public void setFhirAppointmentDao(FhirAppointmentDao fhirAppointmentDao) {
        this.fhirAppointmentDao = fhirAppointmentDao;
    }

    @Override
    public Appointment getAppointmentByUuid(String uuid) {
        return appointmentTranslator.toFhirResource(fhirAppointmentDao.getAppointmentByUuid(uuid));
    }
}
