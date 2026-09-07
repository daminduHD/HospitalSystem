import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// 4. Patient Visit History
class Visit {
    String visitID, date, doctorName, diagnosis, treatment;
    Visit next;

    public Visit(String id, String d, String doc, String diag, String treat) {
        this.visitID = id; 
        this.date = d; 
        this.doctorName = doc; 
        this.diagnosis = diag; 
        this.treatment = treat; 
        this.next = null;
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

// 1. Patient Records
class Patient {
    int patientID, age;
    String name, contactNumber, medicalCondition;
    VisitHistoryList history; 
    Patient left, right; 

    public Patient(int id, String n, int a, String contact, String condition) {
        this.patientID = id; 
        this.name = n; 
        this.age = a; 
        this.contactNumber = contact; 
        this.medicalCondition = condition;
        this.history = new VisitHistoryList(); 
        this.left = null; 
        this.right = null;
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

    public Patient search(int id) { return searchRec(root, id); }
    
    private Patient searchRec(Patient currentRoot, int id) {
        if (currentRoot == null || currentRoot.patientID == id) return currentRoot;
        if (currentRoot.patientID > id) return searchRec(currentRoot.left, id);
        return searchRec(currentRoot.right, id);
    }

    public void delete(int id) { root = deleteRec(root, id); }

    private Patient deleteRec(Patient currentRoot, int id) {
        if (currentRoot == null) return currentRoot;

        if (id < currentRoot.patientID) {
            currentRoot.left = deleteRec(currentRoot.left, id);
        } else if (id > currentRoot.patientID) {
            currentRoot.right = deleteRec(currentRoot.right, id);
        } else {
            if (currentRoot.left == null) return currentRoot.right;
            else if (currentRoot.right == null) return currentRoot.left;

            Patient successor = getMinNode(currentRoot.right);
            
            currentRoot.patientID = successor.patientID;
            currentRoot.name = successor.name;
            currentRoot.age = successor.age;
            currentRoot.contactNumber = successor.contactNumber;
            currentRoot.medicalCondition = successor.medicalCondition;
            currentRoot.history = successor.history;

            currentRoot.right = deleteRec(currentRoot.right, successor.patientID);
        }
        return currentRoot;
    }

    private Patient getMinNode(Patient currentRoot) {
        while (currentRoot.left != null) {
            currentRoot = currentRoot.left;
        }
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

// 2. Emergency Patient Queue (Queue)
class QueueNode {
    Patient patient; QueueNode next;
    public QueueNode(Patient p) { this.patient = p; this.next = null; }
}

class EmergencyQueue {
    QueueNode front, rear;
    
    public void enqueue(Patient p) {
        QueueNode newNode = new QueueNode(p);
        if (rear == null) { front = rear = newNode; return; }
        rear.next = newNode; rear = newNode;
    }
    
    public Patient dequeue() {
        if (front == null) return null; 
        Patient p = front.patient;
        front = front.next;
        if (front == null) rear = null;
        return p;
    }
}

// 3. Treatment History (Stack)
class StackNode {
    String treatmentRecord; StackNode next;
    public StackNode(String record) { this.treatmentRecord = record; this.next = null; }
}

class TreatmentStack {
    StackNode top;
    
    public void push(String record) {
        StackNode newNode = new StackNode(record);
        newNode.next = top; top = newNode;
    }
    
    public String getStackDisplay() {
        if (top == null) return " No treatments completed yet.\n";
        StringBuilder sb = new StringBuilder();
        StackNode temp = top;
        while (temp != null) {
            sb.append(" ✓ ").append(temp.treatmentRecord).append("\n");
            temp = temp.next;
        }
        return sb.toString();
    }
}

// Main GUI Application
public class HospitalSystem {
    private static PatientBST bst = new PatientBST();
    private static EmergencyQueue queue = new EmergencyQueue();
    private static TreatmentStack stack = new TreatmentStack();
    private static JTextArea displayArea;

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        JFrame frame = new JFrame("Mini Hospital Emergency System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 244, 248));

        // Top Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Mini Hospital Emergency Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Display Area (Center)
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        displayArea.setBackground(new Color(253, 253, 253));
        displayArea.setForeground(new Color(44, 62, 80));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, 0, 15, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Buttons Panel (Left Side)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(10, 1, 10, 8)); 
        buttonPanel.setPreferredSize(new Dimension(260, 0));
        buttonPanel.setBorder(new EmptyBorder(0, 15, 15, 10));
        buttonPanel.setOpaque(false);

        Color btnColor = new Color(52, 152, 219);
        Color textColor = Color.black;

        JButton btnRegister = new JButton("Register Patient");
        JButton btnViewAll = new JButton("View Patients (BST)");
        JButton btnSearch = new JButton("Search Patient");
        JButton btnDelete = new JButton("Delete Patient");
        
        // New Buttons for Visit History (Linked List)
        JButton btnAddVisit = new JButton("Add Patient Visit");
        JButton btnViewVisits = new JButton("View Visit History");
        
        JButton btnEmergency = new JButton("Add to Emergency");
        JButton btnTreat = new JButton("Treat Patient");
        JButton btnHistory = new JButton("View Treatments");
        JButton btnClear = new JButton("Clear Screen");

        styleButton(btnRegister, btnColor, textColor);
        styleButton(btnViewAll, btnColor, textColor);
        styleButton(btnSearch, new Color(155, 89, 182), textColor); 
        styleButton(btnDelete, new Color(231, 76, 60), textColor); 
        
        styleButton(btnAddVisit, new Color(46, 204, 113), textColor);
        styleButton(btnViewVisits, new Color(26, 188, 156), textColor);
        
        styleButton(btnEmergency, new Color(230, 126, 34), textColor); 
        styleButton(btnTreat, new Color(39, 174, 96), textColor);     
        styleButton(btnHistory, btnColor, textColor);
        styleButton(btnClear, new Color(149, 165, 166), textColor);   

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAddVisit);
        buttonPanel.add(btnViewVisits);
        buttonPanel.add(btnEmergency);
        buttonPanel.add(btnTreat);
        buttonPanel.add(btnHistory);
        buttonPanel.add(btnClear);
        frame.add(buttonPanel, BorderLayout.WEST);

        // --- Action Listeners ---
        btnRegister.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                String name = JOptionPane.showInputDialog(frame, "Enter Name:");
                String ageStr = JOptionPane.showInputDialog(frame, "Enter Age:");
                int age = Integer.parseInt(ageStr);
                String contact = JOptionPane.showInputDialog(frame, "Enter Contact:");
                String condition = JOptionPane.showInputDialog(frame, "Enter Condition:");
                
                bst.insert(new Patient(id, name, age, contact, condition));
                displayArea.append("SUCCESS: Patient '" + name + "' registered.\n");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: ID and Age must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnViewAll.addActionListener(e -> {
            displayArea.append("\n=== All Registered Patients ===\n");
            StringBuilder sb = new StringBuilder();
            bst.getInOrder(sb);
            if (sb.length() == 0) displayArea.append(" No patients found.\n");
            else displayArea.append(sb.toString());
            displayArea.append("===============================\n");
        });

        btnSearch.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID to Search:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                
                Patient p = bst.search(id);
                if (p != null) {
                    displayArea.append("\n=== Patient Found ===\n");
                    displayArea.append("ID: " + p.patientID + "\nName: " + p.name + "\nAge: " + p.age + 
                                       "\nContact: " + p.contactNumber + "\nCondition: " + p.medicalCondition + "\n");
                    displayArea.append("=====================\n");
                } else {
                    displayArea.append("ERROR: Patient ID " + id + " not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID to Delete:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                
                Patient p = bst.search(id);
                if (p != null) {
                    bst.delete(id);
                    displayArea.append("DELETED: Patient '" + p.name + "' (ID: " + id + ") removed from system.\n");
                } else {
                    displayArea.append("ERROR: Cannot delete. Patient ID " + id + " not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Linked List Action Listeners ---
        btnAddVisit.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID to add visit:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                
                Patient p = bst.search(id);
                if (p != null) {
                    String vId = JOptionPane.showInputDialog(frame, "Enter Visit ID:");
                    String date = JOptionPane.showInputDialog(frame, "Enter Date (YYYY-MM-DD):");
                    String doc = JOptionPane.showInputDialog(frame, "Enter Doctor Name:");
                    String diag = JOptionPane.showInputDialog(frame, "Enter Diagnosis:");
                    String treat = JOptionPane.showInputDialog(frame, "Enter Treatment:");
                    
                    p.history.addVisit(vId, date, doc, diag, treat);
                    displayArea.append("SUCCESS: Visit added for Patient '" + p.name + "'.\n");
                } else {
                    displayArea.append("ERROR: Patient ID " + id + " not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnViewVisits.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID to view visits:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                
                Patient p = bst.search(id);
                if (p != null) {
                    displayArea.append("\n=== Visit History for " + p.name + " (Linked List) ===\n");
                    displayArea.append(p.history.getHistoryDisplay());
                    displayArea.append("====================================================\n");
                } else {
                    displayArea.append("ERROR: Patient ID " + id + " not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Queue & Stack Action Listeners
        btnEmergency.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(frame, "Enter Patient ID for Emergency Queue:");
                if (idStr == null || idStr.trim().isEmpty()) return;
                int id = Integer.parseInt(idStr);
                
                Patient p = bst.search(id);
                if (p != null) {
                    queue.enqueue(p);
                    displayArea.append(" ALERT: Patient '" + p.name + "' added to Emergency Queue.\n");
                } else {
                    displayArea.append(" ERROR: Patient ID " + id + " not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnTreat.addActionListener(e -> {
            Patient treated = queue.dequeue();
            if (treated != null) {
                String record = "Treated ID: " + treated.patientID + " | Name: " + treated.name + " | Issue: " + treated.medicalCondition;
                stack.push(record);
                displayArea.append(" TREATED: " + record + "\n");
            } else {
                displayArea.append(" INFO: The Emergency Queue is empty.\n");
            }
        });

        btnHistory.addActionListener(e -> {
            displayArea.append("\n=== Emergency Treatment Stack (LIFO) ===\n");
            displayArea.append(stack.getStackDisplay());
            displayArea.append("========================================\n");
        });

        btnClear.addActionListener(e -> displayArea.setText(""));

        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
        displayArea.append("System Initialized...\nSelect an operation from the menu to begin.\n\n");
    }
}