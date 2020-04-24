package org.openmrs.module.api.translators.impl;

import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.StringType;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.module.api.AppointmentFhirConstant;
import org.openmrs.module.api.translators.AppointmentSpecialityTranslator;
import org.openmrs.module.api.translators.HealthCareServiceTranslator;
import org.openmrs.module.appointments.model.AppointmentServiceDefinition;
import org.openmrs.module.appointments.model.AppointmentServiceType;
import org.openmrs.module.appointments.model.AppointmentStatus;
import org.openmrs.module.appointments.model.ServiceWeeklyAvailability;
import org.openmrs.module.appointments.model.Speciality;
import org.openmrs.module.fhir2.api.dao.FhirLocationDao;
import org.openmrs.module.fhir2.api.translators.impl.AbstractReferenceHandlingTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Component
public class HealthCareServiceTranslatorImpl extends AbstractReferenceHandlingTranslator implements HealthCareServiceTranslator {

    private Integer appointmentServiceId;
    private String name;
    private String description;
    private Speciality speciality;
    private Time startTime; //
    private Time endTime;   //
    private Integer maxAppointmentsLimit;
    private Integer durationMins;
    private Location location;
    private String  color;
    private AppointmentStatus initialAppointmentStatus;
    private Set<ServiceWeeklyAvailability> weeklyAvailability; //
    private Set<AppointmentServiceType> serviceTypes; //

    @Autowired
    private AppointmentSpecialityTranslator specialityTranslator;

    @Autowired
    private FhirLocationDao locationDao;

    @Override
    public HealthcareService toFhirResource(AppointmentServiceDefinition appointmentService) {
        if (appointmentService == null) {
            return null;
        }
        HealthcareService service = new HealthcareService();
        service.setId(appointmentService.getUuid());
        service.setName(appointmentService.getName());
        service.setComment(appointmentService.getDescription());
        service.addSpecialty(specialityTranslator.toFhirResource(appointmentService.getSpeciality()));
        service.addLocation(createLocationReference(appointmentService.getLocation()));

        HealthcareService.HealthcareServiceAvailableTimeComponent availableTimeComponent = new HealthcareService.HealthcareServiceAvailableTimeComponent();
        availableTimeComponent.setAvailableStartTime(appointmentService.getStartTime().toString());
        availableTimeComponent.setAvailableEndTime(appointmentService.getEndTime().toString());
        service.addAvailableTime(availableTimeComponent);

        addHealthCareServiceExtension(service, "maxAppointmentsLimit", appointmentService.getMaxAppointmentsLimit().toString());
        addHealthCareServiceExtension(service, "durationMins", appointmentService.getDurationMins().toString());
        addHealthCareServiceExtension(service, "color", appointmentService.getColor());
        addHealthCareServiceExtension(service, "initialAppointmentStatus", appointmentService.getInitialAppointmentStatus().toString());

        return null;
    }

    @Override
    public AppointmentServiceDefinition toOpenmrsType(HealthcareService healthcareService) {
        return toOpenmrsType(new AppointmentServiceDefinition(), healthcareService);
    }

    @Override
    public AppointmentServiceDefinition toOpenmrsType(AppointmentServiceDefinition appointmentServiceDefinition, HealthcareService healthcareService) {
        if (healthcareService == null) {
            return appointmentServiceDefinition;
        }
        appointmentServiceDefinition.setUuid(healthcareService.getId());
        appointmentServiceDefinition.setName(healthcareService.getName());
        appointmentServiceDefinition.setDescription(healthcareService.getComment());
        if (healthcareService.hasSpecialty()) {
            appointmentServiceDefinition.setSpeciality(specialityTranslator.toOpenmrsType(healthcareService.getSpecialty().get(0)));
        }
        appointmentServiceDefinition.setLocation(locationDao.getLocationByUuid(getReferenceId(healthcareService.getLocation().get(0))));

        if (healthcareService.hasAvailableTime()) {
//            appointmentServiceDefinition.setStartTime(new Time(""));

        }

        getAppointmentServiceExtension(healthcareService).ifPresent(ext -> ext.getExtension()
                .forEach(e -> addHealthCareServiceComponent(appointmentServiceDefinition, e.getUrl(), ((StringType) e.getValue()).getValue())));


        return null;
    }



    public void addHealthCareServiceComponent(@NotNull AppointmentServiceDefinition serviceDefinition, @NotNull String url, @NotNull String value) {
        if (value == null || url == null || !url.startsWith(AppointmentFhirConstant.OPENMRS_FHIR_EXT_HEALTH_CARE_SERVICE + "#")) {
            return;
        }
        String val = url.substring(url.lastIndexOf('#') + 1);
        switch (val) {
            case "maxAppointmentsLimit":
                serviceDefinition.setMaxAppointmentsLimit(Integer.valueOf(val));
                break;
            case "durationMins":
                serviceDefinition.setDurationMins(Integer.valueOf(val));
                break;
            case "color":
                serviceDefinition.setColor(val);
                break;
            case "initialAppointmentStatus":
                serviceDefinition.setInitialAppointmentStatus(AppointmentStatus.valueOf(val));
                break;
        }
    }

    private void addHealthCareServiceExtension(@NotNull HealthcareService service, @NotNull java.lang.String extensionProperty,
                                      @NotNull String value) {
        if (value == null) {
            return;
        }

        getAppointmentServiceExtension(service)
                .orElseGet(() -> service.addExtension().setUrl(AppointmentFhirConstant.OPENMRS_FHIR_EXT_HEALTH_CARE_SERVICE))
                .addExtension(AppointmentFhirConstant.OPENMRS_FHIR_EXT_HEALTH_CARE_SERVICE + "#" + extensionProperty, new StringType(value));
    }

    private Optional<Extension> getAppointmentServiceExtension(@NotNull HealthcareService service) {
        return Optional.ofNullable(service.getExtensionByUrl(AppointmentFhirConstant.OPENMRS_FHIR_EXT_HEALTH_CARE_SERVICE));

    }
}
