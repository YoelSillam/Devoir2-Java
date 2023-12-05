package sio.devoir2graphiques.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class GraphiqueController
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public GraphiqueController() {
        cnx = ConnexionBDD.getCnx();
    }
    // A vous de jouer
    public TreeMap<Integer, Integer> GetDataGraphique1() {
        TreeMap<Integer, Integer> datas = new TreeMap<>();

        try {
            ps = cnx.prepareStatement("SELECT employe.ageEmp, AVG(employe.salaireEmp) AS moyenneSalaire\n" +
                    "FROM employe\n" +
                    "GROUP BY employe.ageEmp \n" +
                    "ORDER BY employe.ageEmp ASC;\n");

            rs = ps.executeQuery();

            while (rs.next()) {
                datas.put(rs.getInt(1), rs.getInt(2));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return datas;
    }
    public HashMap<String, ArrayList<String>> GetDataGraphique2() {
        HashMap<String, ArrayList<String>> datas = new HashMap<>();

        // Écrire la requête
        try {
            ps = cnx.prepareStatement("SELECT '10-19' AS ageRange, sexe, COUNT(*) AS nombre\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 10 AND 19\n" +
                    "GROUP BY sexe\n" +
                    "\n" +
                    "UNION\n" +
                    "\n" +
                    "SELECT '+50' AS ageRange, sexe, COUNT(*) AS nombre\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp >= 50\n" +
                    "GROUP BY sexe\n" +
                    "\n" +
                    "UNION\n" +
                    "\n" +
                    "SELECT '20-29' AS ageRange, sexe, COUNT(*) AS nombre\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 20 AND 29\n" +
                    "GROUP BY sexe\n" +
                    "\n" +
                    "UNION\n" +
                    "\n" +
                    "SELECT '30-39' AS ageRange, sexe, COUNT(*) AS nombre\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 30 AND 39\n" +
                    "GROUP BY sexe\n" +
                    "\n" +
                    "UNION\n" +
                    "\n" +
                    "SELECT '40-49' AS ageRange, sexe, COUNT(*) AS nombre\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 40 AND 49\n" +
                    "GROUP BY sexe\n" +
                    "\n" +
                    "ORDER BY ageRange, sexe;\n");
            // Exécuter
            rs = ps.executeQuery();
            // Récupérer les données
            while (rs.next()) {
                String ageRange = rs.getString(1);
                String sexe = rs.getString(2);
                String nombre = rs.getString(3);

                if (!datas.containsKey(ageRange)) {
                    ArrayList<String> ageRangeData = new ArrayList<>();
                    ageRangeData.add(sexe);
                    ageRangeData.add(nombre);
                    datas.put(ageRange, ageRangeData);
                } else {
                    datas.get(ageRange).add(sexe);
                    datas.get(ageRange).add(nombre);
                }
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return datas;
    }
    public HashMap<String,Integer> GetDataGraphique3(){
        HashMap<String,Integer> datas = new HashMap<>();

        //ecrire la requête
        try {
            ps = cnx.prepareStatement("SELECT\n" +
                    "  sexe,\n" +
                    "  COUNT(*) AS nombre,\n" +
                    "  CONCAT(ROUND((COUNT(*) / (SELECT COUNT(*) FROM employe)) * 100, 2), '%') AS pourcentage\n" +
                    "FROM\n" +
                    "  employe\n" +
                    "GROUP BY\n" +
                    "  sexe;");
            //exuter
            rs=  ps.executeQuery();
            //récuperer les données
            while (rs.next())
            {datas.put(rs.getString(1),rs.getInt(2));
            }
            rs.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return datas;

    }
}
