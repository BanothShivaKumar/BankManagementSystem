import java.sql.Statement;

public class SetupDatabase {
    public static void main(String[] args) {
        System.out.println("Connecting to database...");
        conn connection = new conn();

        if (connection.c == null || connection.s == null) {
            System.err.println("Database connection failed. Please verify your connection credentials in db.properties or environment variables.");
            return;
        }

        String[] tableQueries = {
            "CREATE TABLE IF NOT EXISTS signup (" +
            "formno VARCHAR(30), " +
            "name VARCHAR(100), " +
            "fathername VARCHAR(100), " +
            "dob VARCHAR(50), " +
            "gender VARCHAR(20), " +
            "email VARCHAR(100), " +
            "martialstatus VARCHAR(30), " +
            "address VARCHAR(255), " +
            "city VARCHAR(100), " +
            "state VARCHAR(100), " +
            "pincode VARCHAR(30)" +
            ");",

            "CREATE TABLE IF NOT EXISTS signupone (" +
            "religion VARCHAR(50), " +
            "category VARCHAR(50), " +
            "income VARCHAR(50), " +
            "education VARCHAR(50), " +
            "occupation VARCHAR(50), " +
            "pan VARCHAR(50), " +
            "aadhar VARCHAR(50), " +
            "seniorcitizen VARCHAR(20), " +
            "existingaccount VARCHAR(20)" +
            ");",

            "CREATE TABLE IF NOT EXISTS signuptwo (" +
            "formno VARCHAR(30), " +
            "accounttype VARCHAR(50), " +
            "cardnumber VARCHAR(30), " +
            "pin VARCHAR(10), " +
            "facility VARCHAR(255)" +
            ");",

            "CREATE TABLE IF NOT EXISTS login (" +
            "formno VARCHAR(30), " +
            "cardnumber VARCHAR(30), " +
            "pin VARCHAR(10)" +
            ");",

            "CREATE TABLE IF NOT EXISTS bank (" +
            "pin VARCHAR(10), " +
            "date VARCHAR(50), " +
            "type VARCHAR(30), " +
            "amount VARCHAR(30)" +
            ");"
        };

        String[] tableNames = {"signup", "signupone", "signuptwo", "login", "bank"};

        try (Statement stmt = connection.c.createStatement()) {
            for (int i = 0; i < tableQueries.length; i++) {
                stmt.executeUpdate(tableQueries[i]);
                System.out.println("Table '" + tableNames[i] + "' checked/created successfully.");
            }
            System.out.println("\nAll tables setup completed successfully!");
        } catch (Exception ex) {
            System.err.println("Error creating tables: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
