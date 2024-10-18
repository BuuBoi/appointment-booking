package com.dut.doctorcare.dao.iface;

import com.dut.doctorcare.dao.iface.common.GenericDao;
import com.dut.doctorcare.dao.iface.common.SoftDeleteDao;
import com.dut.doctorcare.model.Patient;

public interface PatientDao extends GenericDao<Patient>, SoftDeleteDao<Patient> {
    // Implement additional Patient-specific methods here if needed
}
