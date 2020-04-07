package org.openmrs.module.api.Impl;

import lombok.AccessLevel;
import lombok.Setter;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.fhir2.api.FhirAppointmentService;
import org.openmrs.module.fhir2.api.dao.FhirAppointmentDao;
import org.openmrs.module.fhir2.api.translators.AppointmentTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
@Transactional
@Setter(AccessLevel.PACKAGE)
public class FhirBahmniAppointmentServiceImpl implements FhirAppointmentService {

    @Autowired
    private AppointmentTranslator<Appointment> appointmentTranslator;

    @Autowired
    private FhirAppointmentDao<Appointment> fhirAppointmentDao;

    @Override
    public org.hl7.fhir.r4.model.Appointment getAppointmentByUuid(String uuid) {
        return appointmentTranslator.toFhirResource(fhirAppointmentDao.getAppointmentByUuid(uuid));
    }
}
