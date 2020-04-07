package org.openmrs.module.api.translators.impl;

import lombok.AccessLevel;
import lombok.Setter;
import org.hl7.fhir.r4.model.Appointment;
import org.openmrs.module.fhir2.api.translators.AppointmentTranslator;
import org.springframework.stereotype.Component;

@Component
@Setter(AccessLevel.PACKAGE)
public class AppointmentTranslatorImpl implements AppointmentTranslator<org.openmrs.module.appointments.model.Appointment> {

    @Override
    public Appointment toFhirResource(org.openmrs.module.appointments.model.Appointment appointment) {
        Appointment fhirAppointment  = new Appointment();
        fhirAppointment.setId(appointment.getUuid());

        return fhirAppointment;
    }

    @Override
    public org.openmrs.module.appointments.model.Appointment toOpenmrsType(org.openmrs.module.appointments.model.Appointment appointment, Appointment fhirAppointment) {
        appointment.setUuid(fhirAppointment.getId());

        return appointment;
    }
}
