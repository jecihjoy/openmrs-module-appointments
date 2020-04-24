package org.openmrs.module.api.translators.impl;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.openmrs.module.api.translators.AppointmentSpecialityTranslator;
import org.openmrs.module.appointments.model.Speciality;
import org.springframework.stereotype.Component;

@Component
public class AppointmentSpecialityTranslatorImpl  implements AppointmentSpecialityTranslator {

    @Override
    public CodeableConcept toFhirResource(Speciality speciality) {
        if (speciality == null) {
            return null;
        }

        CodeableConcept code = new CodeableConcept();
        code.addCoding(new Coding("Set http://hl7.org/fhir/ValueSet/c80-practice-codes",speciality.getUuid(), speciality.getName()));

        return code;
    }

    @Override
    public Speciality toOpenmrsType(CodeableConcept codeableConcept) {
        return toOpenmrsType(new Speciality(), codeableConcept);
    }

    @Override
    public Speciality toOpenmrsType(Speciality speciality, CodeableConcept codeableConcept) {
        if (codeableConcept == null) {
            return null;
        }

        if (codeableConcept.hasCoding()) {
            speciality.setUuid(codeableConcept.getCoding().get(0).getCode());
            speciality.setName(codeableConcept.getCoding().get(0).getDisplay());
        }

        return speciality;
    }
}
