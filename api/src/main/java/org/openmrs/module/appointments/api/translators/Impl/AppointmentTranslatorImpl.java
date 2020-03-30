package org.openmrs.module.appointments.api.translators.Impl;

import org.hl7.fhir.r4.model.Appointment;
import org.openmrs.module.appointments.api.translators.AppointmentTranslator;

public class AppointmentTranslatorImpl implements AppointmentTranslator {

    @Override
    public Appointment toFhirResource(org.openmrs.module.appointments.model.Appointment appointment) {
        Appointment appnt  = new Appointment();
        appnt.setId(appointment.getUuid());
        return appnt;
    }

    @Override
    public org.openmrs.module.appointments.model.Appointment toOpenmrsType(org.openmrs.module.appointments.model.Appointment appointment, Appointment appointment2) {
        return null;
    }
}
