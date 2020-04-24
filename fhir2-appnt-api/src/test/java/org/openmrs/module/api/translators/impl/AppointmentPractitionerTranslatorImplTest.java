package org.openmrs.module.api.translators.impl;

import org.hl7.fhir.r4.model.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Provider;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.fhir2.FhirConstants;
import org.openmrs.module.fhir2.api.dao.FhirPractitionerDao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentPractitionerTranslatorImplTest {
    private static String PATIENT_UUID = "162298AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Mock
    private FhirPractitionerDao practitionerDao;

    private AppointmentPractitionerTranslatorImpl  appointmentPractitionerTranslator;

    @Before
    public void setup() {
        appointmentPractitionerTranslator = new AppointmentPractitionerTranslatorImpl();
    }

    @Test
    public void toFhirResourceShouldReturnNullIfPractitionerIsNull() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPractitionerTranslator.toFhirResource(null);
        assertThat(participantComponent, nullValue());
    }

    @Test
    public void toFhirResourceShouldSetRoleToPractioner() {
        Provider provider = new Provider();
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPractitionerTranslator.toFhirResource(provider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getSystem(), equalTo(AppointmentFhirConstant.APPOINTMENT_PARTICIPANT_TYPE));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getCode(), equalTo("Practitioner"));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getDisplay(), equalTo("Practitioner"));
    }

    @Test
    public void toFhirResourceShouldSetParticipantToRequired() {
        Provider provider = new Provider();
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPractitionerTranslator.toFhirResource(provider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getRequired(), equalTo(Appointment.ParticipantRequired.OPTIONAL));
    }

    @Test
    public void toFhirResourceShouldSetActorToPractitioner() {
        Provider provider = new Provider();
        provider.setUuid(PATIENT_UUID);
        Appointment.AppointmentParticipantComponent participantComponent = appointmentPractitionerTranslator.toFhirResource(provider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getActor().getType(), equalTo(FhirConstants.PRACTITIONER));
    }

    @Test
    public void toOpenmrsTypeShouldReturnNullIfParticipantIsNull() {
        Provider provider = new Provider();
        Provider result = appointmentPractitionerTranslator.toOpenmrsType(provider,null);
        assertThat(result, equalTo(provider));
    }

//    @Test
//    public void toOpenmrsTypeShouldTranslateParticipantActorToPractitioner() {
//        Patient patient = new Patient();
//        patient.setUuid(PATIENT_UUID);
//        Appointment.AppointmentParticipantComponent participantComponent = new Appointment.AppointmentParticipantComponent();
//
//        Reference patientReference = new Reference().setReference(FhirConstants.PATIENT + "/" + PATIENT_UUID)
//                .setType(FhirConstants.PATIENT).setIdentifier(new Identifier().setValue(PATIENT_UUID));
//        participantComponent.setActor(patientReference);
//
//        when(patientDao.getPatientByUuid(PATIENT_UUID)).thenReturn(patient);
//
//        Patient result = appointmentPatientTranslator.toOpenmrsType(patient, participantComponent);
//        assertThat(result, notNullValue());
//    }
}
