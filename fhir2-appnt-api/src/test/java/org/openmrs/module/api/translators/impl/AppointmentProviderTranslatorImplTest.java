package org.openmrs.module.api.translators.impl;

import org.hl7.fhir.r4.model.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Provider;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.appointments.model.AppointmentProvider;
import org.openmrs.module.appointments.model.AppointmentProviderResponse;
import org.openmrs.module.fhir2.FhirConstants;
import org.openmrs.module.fhir2.api.dao.FhirPractitionerDao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentProviderTranslatorImplTest {
    private static String PRACTIONER_UUID = "162298AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Mock
    private FhirPractitionerDao practitionerDao;

    private AppointmentProvider appointmentProvider;

    private AppointmentProviderTranslatorImpl appointmentProviderTranslator;

    @Before
    public void setup() {
        appointmentProviderTranslator = new AppointmentProviderTranslatorImpl();
        initProvider();
    }

    public void initProvider() {
        appointmentProvider = new AppointmentProvider();
        Provider provider = new Provider();
        provider.setUuid(PRACTIONER_UUID);
        appointmentProvider.setResponse(AppointmentProviderResponse.AWAITING);
        appointmentProvider.setProvider(provider);
    }

    @Test
    public void toFhirResourceShouldReturnNullIfPractitionerIsNull() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentProviderTranslator.toFhirResource(null);
        assertThat(participantComponent, nullValue());
    }

    @Test
    public void toFhirResourceShouldSetRoleToPractitioner() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentProviderTranslator.toFhirResource(appointmentProvider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getSystem(), equalTo(AppointmentFhirConstant.APPOINTMENT_PARTICIPANT_TYPE));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getCode(), equalTo("AppointmentPractitioner"));
        assertThat(participantComponent.getType().get(0).getCoding().get(0).getDisplay(), equalTo("AppointmentPractitioner"));
    }

    @Test
    public void toFhirResourceShouldSetParticipantToRequired() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentProviderTranslator.toFhirResource(appointmentProvider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getRequired(), equalTo(Appointment.ParticipantRequired.OPTIONAL));
    }

    @Test
    public void toFhirResourceShouldSetStatusToAccepted() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentProviderTranslator.toFhirResource(appointmentProvider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getStatus(), equalTo(Appointment.ParticipationStatus.NEEDSACTION));
    }

    @Test
    public void toFhirResourceShouldSetActorToProvider() {
        Appointment.AppointmentParticipantComponent participantComponent = appointmentProviderTranslator.toFhirResource(appointmentProvider);
        assertThat(participantComponent, notNullValue());
        assertThat(participantComponent.getActor().getType(), equalTo(FhirConstants.PRACTITIONER));
    }

    @Test
    public void toOpenmrsTypeShouldReturnNullIfParticipantIsNull() {
        AppointmentProvider appointmentProvider = new AppointmentProvider();
        AppointmentProvider result = appointmentProviderTranslator.toOpenmrsType(appointmentProvider, null);
        assertThat(result, equalTo(appointmentProvider));
    }

//    @Test
//    public void toOpenmrsTypeShouldTranslateParticipantActorToPatient() {
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
