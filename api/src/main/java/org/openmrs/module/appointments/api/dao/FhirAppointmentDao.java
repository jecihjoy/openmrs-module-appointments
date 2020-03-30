package org.openmrs.module.appointments.api.dao;

import org.openmrs.module.appointments.model.Appointment;

import javax.validation.constraints.NotNull;

public interface FhirAppointmentDao {

    Appointment getAppointmentByUuid(@NotNull String uuid);
}
