package org.openmrs.module.appointments.web.fhir2.providers;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.IdType;
import org.openmrs.module.appointments.api.FhirAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Qualifier("fhirResources")
@Setter(AccessLevel.PACKAGE)
public class BahmniAppointmentFhirResourceProvider implements IResourceProvider {

    Log log = LogFactory.getLog(BahmniAppointmentFhirResourceProvider.class);

    @Autowired
    private FhirAppointmentService appointmentService;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Appointment.class;
    }

    @Read
    @SuppressWarnings("unused")
    public Appointment getAppointmentByUuid(@IdParam @NotNull IdType id) {
        log.error("BahmniAppointmentFhirResourceProvider was called with " + id.getIdPart());
        Appointment appointment = appointmentService.getAppointmentByUuid(id.getIdPart());
        if (appointment == null) {
            throw new ResourceNotFoundException("Could not find medicationRequest with Id " + id.getIdPart());
        }
        return appointment;
    }
}
