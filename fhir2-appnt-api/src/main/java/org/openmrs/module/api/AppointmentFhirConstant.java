package org.openmrs.module.api;

import org.openmrs.module.fhir2.FhirConstants;

public class AppointmentFhirConstant extends FhirConstants {
    public static final String APPOINTMENT_PARTICIPANT_TYPE = HL7_FHIR_VALUE_SET_PREFIX
            + "/encounter-participant-type";

    public static final String OPENMRS_FHIR_EXT_HEALTH_CARE_SERVICE= "https://fhir.openmrs.org/ext/health-care-service";
}
