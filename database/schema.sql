
CREATE DATABASE IF NOT EXISTS smart_hospital;

USE smart_hospital;

CREATE TABLE IF NOT EXISTS patients (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(255),
    blood_group VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT,
    phone VARCHAR(20),
    specialization VARCHAR(100) NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(30) DEFAULT 'SCHEDULED',
    reason VARCHAR(255),

    FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id),

    FOREIGN KEY (doctor_id)
        REFERENCES doctors(doctor_id)
);

CREATE TABLE IF NOT EXISTS medical_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT,
    diagnosis TEXT,
    record_date DATE NOT NULL,

    FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id),

    FOREIGN KEY (doctor_id)
        REFERENCES doctors(doctor_id),

    FOREIGN KEY (appointment_id)
        REFERENCES appointments(appointment_id)
);

CREATE TABLE IF NOT EXISTS treatments (
    treatment_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,
    description VARCHAR(500) NOT NULL,
    cost DECIMAL(10,2) DEFAULT 0.00,

    FOREIGN KEY (record_id)
        REFERENCES medical_records(record_id)
);

CREATE TABLE IF NOT EXISTS medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock_quantity INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,
    prescription_date DATE NOT NULL,
    instructions TEXT,

    FOREIGN KEY (record_id)
        REFERENCES medical_records(record_id)
);

CREATE TABLE IF NOT EXISTS prescription_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,
    medicine_id INT NOT NULL,
    dosage VARCHAR(100),
    duration VARCHAR(100),
    quantity INT NOT NULL DEFAULT 1,

    FOREIGN KEY (prescription_id)
        REFERENCES prescriptions(prescription_id),

    FOREIGN KEY (medicine_id)
        REFERENCES medicines(medicine_id)
);

CREATE TABLE IF NOT EXISTS bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    bill_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) DEFAULT 'UNPAID',

    FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id)
);

CREATE TABLE IF NOT EXISTS bill_items (
    bill_item_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (bill_id)
        REFERENCES bills(bill_id)
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) DEFAULT 'COMPLETED',

    FOREIGN KEY (bill_id)
        REFERENCES bills(bill_id)
);
