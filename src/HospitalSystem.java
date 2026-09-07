class Patient {
    int patientID, age;
    String name, contactNumber, medicalCondition;
    Patient left, right;

    public Patient(int id, String n, int a, String contact, String condition) {
        this.patientID = id; 
        this.name = n; 
        this.age = a; 
        this.contactNumber = contact; 
        this.medicalCondition = condition;
        this.left = null; 
        this.right = null;
    }
}

public class HospitalSystem {
    public static void main(String[] args) {
    }
}