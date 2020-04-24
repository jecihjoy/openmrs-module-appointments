package org.openmrs.module.api.translators.impl;

import ca.uhn.hl7v2.model.v25.segment.LOC;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.openmrs.Patient;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.api.translators.AppointmentParticipantTranslator;
import org.openmrs.module.fhir2.api.dao.FhirPatientDao;
import org.openmrs.module.fhir2.api.translators.impl.AbstractReferenceHandlingTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentPatientTranslatorImpl extends AbstractReferenceHandlingTranslator implements AppointmentParticipantTranslator<Patient> {

    @Autowired
    private FhirPatientDao patientDao;

    @Override
    public Appointment.AppointmentParticipantComponent toFhirResource(Patient patient) {
        if (patient == null) {
            return null;
        }

        Appointment.AppointmentParticipantComponent participant = new Appointment.AppointmentParticipantComponent();
        CodeableConcept role = new CodeableConcept();
        role.addCoding(new Coding(AppointmentFhirConstant.APPOINTMENT_PARTICIPANT_TYPE, "Patient", "Patient"));
        participant.setRequired(Appointment.ParticipantRequired.REQUIRED);
        participant.setActor(createPatientReference(patient));
        participant.addType(role);
        participant.setStatus(Appointment.ParticipationStatus.ACCEPTED);

        return participant;
    }

    @Override
    public Patient toOpenmrsType(Appointment.AppointmentParticipantComponent appointmentParticipantComponent) {
        return toOpenmrsType(new Patient(), appointmentParticipantComponent);
    }

    @Override
    public Patient toOpenmrsType(Patient patient, Appointment.AppointmentParticipantComponent appointmentParticipantComponent) {
        if (appointmentParticipantComponent == null) {
            return patient;
        }
        Log log = LogFactory.getLog(AppointmentPatientTranslatorImpl.class);
        log.error("DISPLAY");
        log.error(appointmentParticipantComponent.getActor().getDisplay());
        return null;
//        return patientDao.getPatientByUuid(getReferenceId(appointmentParticipantComponent.getActor()));
    }
}
