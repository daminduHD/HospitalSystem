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

class Visit {
    String visitID, date, doctorName, diagnosis, treatment;
    Visit next;

    public Visit(String id, String d, String doc, String diag, String treat) {
        this.visitID = id; this.date = d; this.doctorName = doc; 
        this.diagnosis = diag; this.treatment = treat; this.next = null;
    }
}

class VisitHistoryList {
    Visit head;
    
    public void addVisit(String id, String date, String doc, String diag, String treat) {
        Visit newVisit = new Visit(id, date, doc, diag, treat);
        if (head != null) newVisit.next = head;
        head = newVisit;
    }

    public String getHistoryDisplay() {
        if (head == null) return " No previous visits recorded.\n";
        StringBuilder sb = new StringBuilder();
        Visit temp = head;
        while (temp != null) {
            sb.append(String.format(" - [Visit ID: %s] Date: %s | Dr. %s | %s | %s\n",
                    temp.visitID, temp.date, temp.doctorName, temp.diagnosis, temp.treatment));
            temp = temp.next;
        }
        return sb.toString();
    }
}

class PatientBST {
    Patient root;

    public void insert(Patient p) { root = insertRec(root, p); }
    
    private Patient insertRec(Patient currentRoot, Patient p) {
        if (currentRoot == null) return p;
        if (p.patientID < currentRoot.patientID) currentRoot.left = insertRec(currentRoot.left, p);
        else if (p.patientID > currentRoot.patientID) currentRoot.right = insertRec(currentRoot.right, p);
        return currentRoot;
    }

    public void getInOrder(StringBuilder sb) { inOrderRec(root, sb); }
    
    private void inOrderRec(Patient currentRoot, StringBuilder sb) {
        if (currentRoot != null) {
            inOrderRec(currentRoot.left, sb);
            sb.append(String.format(" [ID: %-4d] %-15s | %s\n", currentRoot.patientID, currentRoot.name, currentRoot.medicalCondition));
            inOrderRec(currentRoot.right, sb);
        }
    }
}