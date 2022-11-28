package Home;



import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
    
    static Connection con;
    public static Connection getConnection(){
     try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/MUZE", "root", "");
            System.out.println("Success");
        }
        catch (Exception ex) {
            System.out.println("Exception"+ex);
        }
        return con;
        
    }
}
