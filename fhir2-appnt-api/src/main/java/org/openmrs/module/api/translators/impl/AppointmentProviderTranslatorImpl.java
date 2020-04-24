package org.openmrs.module.api.translators.impl;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.api.translators.AppointmentParticipantTranslator;
import org.openmrs.module.appointments.model.AppointmentProvider;
import org.openmrs.module.appointments.model.AppointmentProviderResponse;
import org.openmrs.module.fhir2.api.dao.FhirPractitionerDao;
import org.openmrs.module.fhir2.api.translators.impl.AbstractReferenceHandlingTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentProviderTranslatorImpl extends AbstractReferenceHandlingTranslator implements AppointmentParticipantTranslator<AppointmentProvider> {

    @Autowired
    private FhirPractitionerDao practitionerDao;

    @Override
    public Appointment.AppointmentParticipantComponent toFhirResource(AppointmentProvider appointmentProvider) {
        if (appointmentProvider == null || appointmentProvider.getProvider() == null) {
            return null;
        }
        Appointment.AppointmentParticipantComponent participant = new Appointment.AppointmentParticipantComponent();
        CodeableConcept role = new CodeableConcept();
        role.addCoding(new Coding(AppointmentFhirConstant.APPOINTMENT_PARTICIPANT_TYPE, "AppointmentPractitioner", "AppointmentPractitioner"));
        participant.setRequired(Appointment.ParticipantRequired.OPTIONAL);
        participant.setActor(createPractitionerReference(appointmentProvider.getProvider()));
        participant.addType(role);

        switch (appointmentProvider.getResponse()) {
            case ACCEPTED:
                return participant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
            case TENTATIVE:
                return participant.setStatus(Appointment.ParticipationStatus.TENTATIVE);
            case AWAITING:
                return participant.setStatus(Appointment.ParticipationStatus.NEEDSACTION);
            case REJECTED:
                return participant.setStatus(Appointment.ParticipationStatus.DECLINED);
            case CANCELLED:
                return participant.setStatus(Appointment.ParticipationStatus.NULL);
        }

        return participant;
    }

    @Override
    public AppointmentProvider toOpenmrsType(Appointment.AppointmentParticipantComponent appointmentParticipantComponent) {
        return toOpenmrsType(new AppointmentProvider(), appointmentParticipantComponent);
    }

    @Override
    public AppointmentProvider toOpenmrsType(AppointmentProvider appointmentProvider, Appointment.AppointmentParticipantComponent appointmentParticipantComponent) {
        if (appointmentParticipantComponent == null) {
            return appointmentProvider;
        }
        appointmentProvider.setProvider(practitionerDao.getProviderByUuid(getReferenceId(appointmentParticipantComponent.getActor())));
        switch (appointmentParticipantComponent.getStatus()) {
            case ACCEPTED:
                appointmentProvider.setResponse(AppointmentProviderResponse.ACCEPTED);
            case TENTATIVE:
                appointmentProvider.setResponse(AppointmentProviderResponse.TENTATIVE);
            case NEEDSACTION:
                appointmentProvider.setResponse(AppointmentProviderResponse.AWAITING);
            case DECLINED:
                appointmentProvider.setResponse(AppointmentProviderResponse.REJECTED);
            case NULL:
                appointmentProvider.setResponse(AppointmentProviderResponse.CANCELLED);
        }
        return appointmentProvider;
    }
}
