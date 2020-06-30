import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	Connection conn;
	Statement stmt = null;
	
	String rDivision;
	String rSubject;
	String rCredit;
	
	public JDBC (String sub) {
		StringBuilder sb  = new StringBuilder();
		String sql = sb.append("select * from " + "schedule" + " where")
				.append(" ����� = '")
				.append(sub)
				.append("';").toString();
			
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schedule?serverTimezone=UTC","root","8888");
			System.out.println("DB ���� �Ϸ�");
			stmt = conn.createStatement();
			ResultSet srs = stmt.executeQuery(sql);
			while(srs.next()) {
			rDivision = srs.getString("��������");
			rSubject = srs.getString("�����");
			rCredit = srs.getString("����");
			}
			System.out.println(rDivision);
			System.out.println(rSubject);
			System.out.println(rCredit);
			
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC ����̹� �ε� ����");
		} catch (SQLException e) {
			System.out.println("SQL ���� ����" + e.getMessage());
		}
	}
	
}
