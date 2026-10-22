-- Database Schema for Bank Management System
-- Compatible with local MySQL and Aiven Cloud MySQL (defaultdb)

SET SESSION sql_require_primary_key = 0;

CREATE TABLE IF NOT EXISTS signup (
    formno VARCHAR(30),
    name VARCHAR(100),
    fathername VARCHAR(100),
    dob VARCHAR(50),
    gender VARCHAR(20),
    email VARCHAR(100),
    martialstatus VARCHAR(30),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS signupone (
    religion VARCHAR(50),
    category VARCHAR(50),
    income VARCHAR(50),
    education VARCHAR(50),
    occupation VARCHAR(50),
    pan VARCHAR(50),
    aadhar VARCHAR(50),
    seniorcitizen VARCHAR(20),
    existingaccount VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS signuptwo (
    formno VARCHAR(30),
    accounttype VARCHAR(50),
    cardnumber VARCHAR(30),
    pin VARCHAR(10),
    facility VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS login (
    formno VARCHAR(30),
    cardnumber VARCHAR(30),
    pin VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS bank (
    pin VARCHAR(10),
    date VARCHAR(50),
    type VARCHAR(30),
    amount VARCHAR(30)
);
