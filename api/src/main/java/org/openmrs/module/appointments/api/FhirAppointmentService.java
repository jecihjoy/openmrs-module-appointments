package org.openmrs.module.appointments.api;

import org.hl7.fhir.r4.model.Appointment;

import javax.validation.constraints.NotNull;

public interface FhirAppointmentService {

    Appointment getAppointmentByUuid(@NotNull String uuid);
}
