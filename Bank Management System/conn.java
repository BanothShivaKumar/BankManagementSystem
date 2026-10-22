import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class conn {
    public Connection c;
    public Statement s;

    public conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = new Properties();
            File propFile = new File("db.properties");
            if (propFile.exists()) {
                try (InputStream is = new FileInputStream(propFile)) {
                    props.load(is);
                }
            }

            String host = getPropOrEnv(props, "db.host", "DB_HOST", "mysql-2494552c-shivakumarnayak2005-012f.i.aivencloud.com");
            String port = getPropOrEnv(props, "db.port", "DB_PORT", "13685");
            String db = getPropOrEnv(props, "db.name", "DB_NAME", "defaultdb");
            String user = getPropOrEnv(props, "db.user", "DB_USER", "avnadmin");
            String pass = getPropOrEnv(props, "db.password", "DB_PASSWORD", "");
            String ssl = getPropOrEnv(props, "db.ssl", "DB_SSL", "true");

            boolean connected = false;

            if (pass != null && !pass.trim().isEmpty()) {
                try {
                    String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?sslmode=REQUIRED";
                    if ("false".equalsIgnoreCase(ssl)) {
                        url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true";
                    }
                    c = DriverManager.getConnection(url, user, pass);
                    s = c.createStatement();
                    connected = true;
                } catch (Exception ex) {
                    System.err.println("Could not connect to configured cloud database (" + host + "): " + ex.getMessage());
                }
            }

            // Fallback to local database if cloud connection not configured or failed
            if (!connected) {
                try {
                    c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankmanagementsystem", "root", "shivakumar");
                    s = c.createStatement();
                    connected = true;
                    System.out.println("Connected to local MySQL database.");
                } catch (Exception ex) {
                    System.err.println("Local database connection also failed: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPropOrEnv(Properties props, String propKey, String envKey, String defaultVal) {
        String val = props.getProperty(propKey);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        val = System.getenv(envKey);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        return defaultVal;
    }

    public static void main(String[] args) {
        conn con = new conn();
        if (con.c != null && con.s != null) {
            System.out.println("Database connected successfully!");
        } else {
            System.out.println("Database connection failed!");
        }
    }
}
