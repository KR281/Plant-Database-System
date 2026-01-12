// Physical location of data: C:\ProgramData\MySQL\MySQL Server 8.4\Data
package plants_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public static Connection connection;
    public static String file = "C:location"; //Location of plant images
    
    public static void createConnection() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/plants";
        String username = "user";
        String password = "password";
        connection = DriverManager.getConnection(url, username, password);        
    }    
    
    public static Connection getConnection() throws SQLException {
        return connection;
    }
    
    public static String createFilepath(String filename){
        
        return file +"/"+filename;
    }
    
    public static Boolean testPlant(String name) throws SQLException{
        String query = "SELECT Plant_id "+
                       "FROM Plants "+
                       "WHERE name = '"+ name+"'";
        ResultSet result = executeStatement(query);
        if (result.next()){
            return true;
        }
        return false;
    }
    
    public static ResultSet getButtons(String orderBy) throws SQLException{
        String query = "SELECT p.name AS Plant_Name, "+
                       "COALESCE(MIN(pp.filename), 'placeholder.png') AS filepath " +
                       "FROM Plants p " +
                       "LEFT JOIN plant_pics pp USING (plant_id) ";
// Dont need to recall the data. Should already be in the hashmap. PLEASE UPDATE LATER       
        switch (orderBy) {
            case "Min PH":
                query +=  "GROUP BY p.name, p.PH_Start "+
                          " ORDER BY p.PH_Start ASC";
                break;
            case "Max PH":
                query += "GROUP BY p.name, p.PH_END "+
                          " ORDER BY p.PH_END DESC";
                break;
            case "Name (DESC)":
                query += "GROUP BY p.name"+
                         " ORDER BY p.name DESC";
                break;
            default:
                query += "GROUP BY p.name"+
                         " ORDER BY p.name ASC";
                break;
     }
        //System.out.println("Executing query: "+ query);
        return executeStatement(query);
    }

    public static ResultSet getNames() throws SQLException{
        String query = "SELECT Name FROM Plants";
        return executeStatement(query);
    }
    
    public static ResultSet selectAll(String plant) throws SQLException{
        String query = "SELECT " +
                        "p.Name AS Plant_Name, " +
                        "p.Nickname AS Nickname, " +
                        "p.PH_Start AS PH_Start, " +
                        "p.PH_End AS PH_End, " +
                        "CASE WHEN p.Frost = 1 THEN 'YES' ELSE 'NO' END AS Frost, " +
                        "CASE WHEN p.Perennial = 1 THEN 'YES' ELSE 'NO' END AS Perennial, " +
                        "p.Edible AS Edible, " +
                        "f.Feed_Type AS Feed_Type, " +
                        "p.Feed_Time AS Feed_Time, " +
                        "p.Mulch AS Mulch, " +
                        "GROUP_CONCAT(DISTINCT ls.Level_Desc ORDER BY ls.Level_Desc ASC) AS Sunlight_Levels, " +
                        "GROUP_CONCAT(DISTINCT lw.Level_Desc ORDER BY lw.Level_Desc ASC) AS Water_Levels, " +
                        "GROUP_CONCAT(DISTINCT pr.Prune_type ORDER BY pr.Prune_type ASC) AS Pruning_Types " +
                        "FROM Plants p " +
                        "LEFT JOIN Feed f ON p.Feed_id = f.Feed_id " +
                        "LEFT JOIN Plant_Levels pl ON p.Plant_id = pl.Plant_id " +
                        "LEFT JOIN Level_Descriptions ls ON pl.Level_id = ls.Level_id AND ls.Category = 'Sunlight' " +
                        "LEFT JOIN Plant_Levels plw ON p.Plant_id = plw.Plant_id " +
                        "LEFT JOIN Level_Descriptions lw ON plw.Level_id = lw.Level_id AND lw.Category = 'Water' " +
                        "LEFT JOIN Plant_Pruning pp ON p.Plant_id = pp.Plant_id " +
                        "LEFT JOIN Pruning pr ON pp.Prune_id = pr.Prune_id " +
                        "WHERE p.Name = '" + plant + "' " + // Plant variable goes here
                        "GROUP BY p.Plant_id, p.Name, p.Nickname, p.PH_Start, p.PH_End, p.Frost, p.Perennial, " +
                        "p.Edible, f.Feed_Type, p.Feed_Time, p.Mulch";
        return executeStatement(query);
    }
    
    public static ResultSet singleImage(String plant) throws SQLException{
        String query = "SELECT filename" +
                       "FROM plant_pics" +
                       "WHERE plant_id = (SELECT plant_id FROM plants WHERE name ='" + plant+"'"+
                       "LIMIT 1";
        return executeStatement(query);
    }
    
    public static ResultSet allImages(String plant) throws SQLException{
        String query = "SELECT filename "+
                       "FROM plant_pics "+
                       "WHERE plant_id =(SELECT plant_id FROM plants WHERE name ='" + plant + "')";
        System.out.println("Executing query: "+query);
        return executeStatement(query);
    }
    
    public static ResultSet executeStatement(String query) throws SQLException{
        Statement message = connection.createStatement();
        return message.executeQuery(query);
    }
    public static String defaultImage(){
        return createFilepath("placeholder.png");
    }

}