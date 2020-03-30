package org.openmrs.module.appointments.api.translators;

import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.fhir2.api.translators.ToFhirTranslator;
import org.openmrs.module.fhir2.api.translators.UpdatableOpenmrsTranslator;

public interface AppointmentTranslator extends ToFhirTranslator<Appointment, org.hl7.fhir.r4.model.Appointment>, UpdatableOpenmrsTranslator<Appointment, org.hl7.fhir.r4.model.Appointment> {

    @Override
    org.hl7.fhir.r4.model.Appointment toFhirResource(Appointment appointment);

    @Override
    Appointment toOpenmrsType(Appointment appointment, org.hl7.fhir.r4.model.Appointment appointment2);
}
