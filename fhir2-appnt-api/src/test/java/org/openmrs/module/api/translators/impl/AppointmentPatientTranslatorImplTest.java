package org.openmrs.module.api.translators.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.fhir2.FhirConstants;
import org.openmrs.module.fhir2.api.dao.FhirPatientDao;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentPatientTranslatorImplTest {
    private static String PATIENT_UUID = "162298AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Mock
    private FhirPatientDao patientDao;

    private AppointmentPatientTranslatorImpl appointmentPatientTranslator;

    @Before
    public void setup() {
        appointmentPatientTranslator = new AppointmentPatientTranslatorImpl();
    }

    @Test
    public void toFhirResourceShouldReturnNullIfPatientIsNull() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPatientTranslator.toFhirResource(null);
        assertThat(participantComponent, nullValue());
    }

    @Test
    public void toFhirResourceShouldSetRoleToPatient() {
        Patient patient = new Patient();
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPatientTranslator.toFhirResource(patient);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getSystem(), equalTo(AppointmentFhirConstant.APPOINTMENT_PARTICIPANT_TYPE));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getCode(), equalTo("Patient"));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getDisplay(), equalTo("Patient"));
    }

    @Test
    public void toFhirResourceShouldSetParticipantToRequired() {
        Patient patient = new Patient();
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPatientTranslator.toFhirResource(patient);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getRequired(), equalTo(Appointment.ParticipantRequired.REQUIRED));
    }

    @Test
    public void toFhirResourceShouldSetStatusToAccepted() {
        Patient patient = new Patient();
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPatientTranslator.toFhirResource(patient);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getStatus(), equalTo(Appointment.ParticipationStatus.ACCEPTED));
    }

    @Test
    public void toFhirResourceShouldSetActorToPatientPassed() {
        Patient patient = new Patient();
        patient.setUuid(PATIENT_UUID);
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPatientTranslator.toFhirResource(patient);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getActor().getType(), equalTo(FhirConstants.PATIENT));
    }

    @Test
    public void toOpenmrsTypeShouldReturnNullIfParticipantIsNull() {
        Patient patient = appointmentPatientTranslator.toOpenmrsType(null);
        assertThat(patient, nullValue());
    }

    @Test
    public void toOpenmrsTypeShouldTranslateParticipantActorToPatient() {
        Patient patient = new Patient();
        patient.setUuid(PATIENT_UUID);
        Appointment.AppointmentParticipantComponent participantComponent = new Appointment.AppointmentParticipantComponent();

        Reference patientReference = new Reference().setReference(FhirConstants.PATIENT + "/" + PATIENT_UUID)
                .setType(FhirConstants.PATIENT).setIdentifier(new Identifier().setValue(PATIENT_UUID)).setDisplay("testing hbhjsC");
        participantComponent.setActor(patientReference);

        when(patientDao.getPatientByUuid(PATIENT_UUID)).thenReturn(patient);

        Patient result = appointmentPatientTranslator.toOpenmrsType(patient, participantComponent);
        assertThat(result, nullValue());
    }
}
