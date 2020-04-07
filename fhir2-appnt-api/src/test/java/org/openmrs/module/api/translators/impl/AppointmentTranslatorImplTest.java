package org.openmrs.module.api.translators.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.appointments.model.Appointment;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentTranslatorImplTest {

    private static String APPOINTMENT_UUID = "162298AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    private AppointmentTranslatorImpl appointmentTranslator;

    private Appointment appointment;

    @Before
    public void setUp() {
        appointmentTranslator = new AppointmentTranslatorImpl();
        appointment = new Appointment();
    }

    @Test
    public void toFhirResourceShouldTranslateUuidToId() {
        appointment.setUuid(APPOINTMENT_UUID);
        org.hl7.fhir.r4.model.Appointment result = appointmentTranslator.toFhirResource(appointment);
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(APPOINTMENT_UUID));
    }

    @Test
    public void toOpenmrsTypeShouldTranslateIdToUuid() {
        org.hl7.fhir.r4.model.Appointment appointment = new org.hl7.fhir.r4.model.Appointment();
        appointment.setId(APPOINTMENT_UUID);

        Appointment result = appointmentTranslator.toOpenmrsType(new Appointment(), appointment);
        assertThat(result, notNullValue());
        assertThat(result.getUuid(), equalTo(APPOINTMENT_UUID));
    }

}
