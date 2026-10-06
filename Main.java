import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection con;
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/hospital";
        String user = "root";
        String password = "nihal123..";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            System.out.println("Welcome To XXX Super Speciality Hospital");
            while (true) {
                System.out.println("\n1. View Doctors\n2. Add Patients\n3. View Patients\n4. Appointment\n5. Add Doctor\n6. Viewappointment\n7.  Exit");
                System.out.print("Select an option: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        viewDoctorDetails();
                        break;
                    case 2:
                        addPatients();
                        break;
                    case 3:
                        viewPatientDetails();
                        break;
                    case 4:
                        appointment();
                        break;
                    case 5:
                        addDoctor();break;
                    case 6:
                        viewappointment();
                        break;
                   case 7:
                        System.out.println("Thank you for using the Hospital System.");
                        sc.close();
                        con.close();
                        System.exit(0);
                    default:
                        System.out.println("Entered Incorrect Option, Please try again....");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewDoctorDetails() {
        try {
            String query = "SELECT * FROM doctors";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("+------------+----------------+----------------------+--------------------+-------------+");
            System.out.println("|    ID      |      NAME      |   Specialization     |   Qualification    | Experience  |");
            System.out.println("+------------+----------------+----------------------+--------------------+-------------+");

            while (rs.next()) {
                int did = rs.getInt("did");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                String education = rs.getString("education");
                int experience = rs.getInt("experience");

                System.out.printf("| %-10d | %-14s | %-20s | %-18s | %-11d |\n",
                        did, name, specialization, education, experience);
            }

            System.out.println("+------------+----------------+----------------------+--------------------+-------------+");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPatients() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Your Name: ");
        String Name = sc.next();
        System.out.print("Enter Your Age: ");
        int Age = sc.nextInt();
        System.out.print("Enter Your Gender: ");
        String Gender = sc.next();
        System.out.print("Enter Your Phone No: ");
        String PhNo = sc.next();

        try {
            String s = "INSERT INTO patients(name, age, gender, phn) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(s);
            preparedStatement.setString(1, Name);
            preparedStatement.setInt(2, Age);
            preparedStatement.setString(3, Gender);
            preparedStatement.setString(4, PhNo);

            int ar = preparedStatement.executeUpdate();
            if (ar > 0)
                System.out.println("Patient added successfully.");
            else
                System.out.println("Failed to add patient.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addDoctor() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Doctor id: ");
            int did = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter Specialization details: ");
            String specialization = sc.nextLine();

            System.out.print("Enter Qualification details: ");
            String education = sc.nextLine();

            System.out.print("Enter your experience time period: ");
            String experience = sc.nextLine();

            String query = "INSERT INTO doctors(did,name, specialization, education, experience) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, did);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, specialization);
            preparedStatement.setString(4, education);
            preparedStatement.setString(5, experience);

            preparedStatement.executeUpdate();
            System.out.println("Doctor Details successfully saved!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void viewPatientDetails() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("Patients: ");
            System.out.println("+--------------+--------------------+-------+------------+-----------------+");
            System.out.println("|  Patient ID  |        Name        |  Age  |   Gender   | Contact details |");
            System.out.println("+--------------+--------------------+-------+------------+-----------------+");

            while (rs.next()) {
                int id = rs.getInt("id"); // Make sure column is named 'id' in your table
                int age = rs.getInt("age");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String phn = rs.getString("phn");
                System.out.printf("| %-12d | %-18s | %-5d | %-10s |%-17s|\n", id, name, age, gender,phn);
            }

            System.out.println("+--------------+--------------------+-------+------------+-----------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void appointment() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Book an Appointment\n2. Cancel the Appointment");
        System.out.print("Enter your Choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter Patient ID: ");
                String pid = sc.next();
                System.out.print("Enter Doctor ID: ");
                String did = sc.next();

                String checkPatient = "SELECT id FROM patients WHERE id = ?";
                String checkDoctor = "SELECT did FROM doctors WHERE did = ?";

                try {
                    PreparedStatement ps1 = con.prepareStatement(checkPatient);
                    ps1.setString(1, pid);
                    ResultSet rs1 = ps1.executeQuery();

                    PreparedStatement ps2 = con.prepareStatement(checkDoctor);
                    ps2.setString(1, did);
                    ResultSet rs2 = ps2.executeQuery();

                    if (rs1.next() && rs2.next()) {
                        while (true) {
                            System.out.print("Enter Date (YYYY-MM-DD): ");
                            String date = sc.next();

                            String checkAvailability = "SELECT COUNT(*) FROM appointment WHERE did = ? AND date = ?";
                            PreparedStatement checkStmt = con.prepareStatement(checkAvailability);
                            checkStmt.setString(1, did);
                            checkStmt.setString(2, date);
                            ResultSet rs3 = checkStmt.executeQuery();

                            if (rs3.next()) {
                                int count = rs3.getInt(1);
                                if (count == 0) {
                                    String insertAppointment = "INSERT INTO appointment (pid, did, date) VALUES (?, ?, ?)";
                                    PreparedStatement insertStmt = con.prepareStatement(insertAppointment);
                                    insertStmt.setString(1, pid);
                                    insertStmt.setString(2, did);
                                    insertStmt.setString(3, date);

                                    int result = insertStmt.executeUpdate();
                                    if (result > 0)
                                        System.out.println("✅ Appointment Booked Successfully.");
                                    else
                                        System.out.println("❌ Failed to Book Appointment.");
                                    break;
                                } else {
                                    System.out.println("❌ Doctor is already booked on this date.");
                                    System.out.println("Do you want to choose another date?\n1. YES\n2. NO");
                                    int retry = sc.nextInt();
                                    if (retry == 1) {
                                        continue;
                                    } else {
                                        System.out.println("Appointment booking cancelled.");
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("❌ Invalid Patient ID or Doctor ID.");
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error Booking Appointment:");
                    e.printStackTrace();
                }
                break;

            case 2:
                System.out.print("Enter Patient ID to Cancel Appointment: ");
                String cancelPid = sc.next();

                String deleteAppointment = "DELETE FROM appointment WHERE pid = ?";
                try (PreparedStatement psDelete = con.prepareStatement(deleteAppointment)) {
                    psDelete.setString(1, cancelPid);
                    int rowsAffected = psDelete.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("✅ Appointment Cancelled Successfully.");
                    } else {
                        System.out.println("❌ No Appointment Found to Cancel.");
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error Cancelling Appointment:");
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("❌ Invalid Choice. Please Enter 1 or 2.");
        }
    }

    public static void viewappointment(){
        String q = "SELECT * FROM appointment";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            System.out.println("OPPOINTMENTS: ");
            System.out.println("+--------------+--------------------+----------------+");
            System.out.println("|  Patient ID  |     DOCTOR   ID    |      DATE      |");
            System.out.println("+--------------+--------------------+----------------+");

            while (rs.next()) {
                int pid = rs.getInt("pid"); // Make sure column is named 'id' in your table
                int did = rs.getInt("did");
                String date = rs.getString("date");
                System.out.printf("| %-12d | %-18d | %-14s |\n", pid, did, date);
            }

            System.out.println("+--------------+--------------------+----------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
