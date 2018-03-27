package hw4databse;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Mainprog {
	public static Executer ex;
	
	public static void execute_line(String line) throws Exception {
		String[] tokens = line.split(" ");
		int code = Integer.parseInt(tokens[0]);
		
		switch(code){
		case 1:
			ex.delete_employee(tokens[1]);
			break;
		case 2:
			Employee e = new Employee(tokens);
			ex.addEmployee(e);
			break;
		case 3:
			ex.averageSalary();
			break;
		case 4:
			ex.namesOFUNDEREmployees(tokens[1]);
			break;
		case 5:
			ex.avgSalUnder(tokens[1]);
			break;
		case 6:
			ex.moreThanOneManager();
			break;
		}
		System.out.println();
	}
	
	public static void open_file(String path) throws Exception {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		String line;

		while ((line = textReader.readLine()) != null) {
		    execute_line(line);
		}
		textReader.close();
	}
	
	public static void clear_db(Statement stmt) throws Exception {
		String sql = "DELETE FROM employee";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM worksfor";
		stmt.executeUpdate(sql);
	}
	
	public static void create_db(Statement stmt) throws Exception {
		String sql = "CREATE TABLE `employee` (" +
				  "`eid` int(11) NOT NULL," +
				  "`name` varchar(20) DEFAULT NULL,"+
				  "`salary` int(11) DEFAULT NULL,"+
				  "PRIMARY KEY (`eid`)"+
				  ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE `worksfor` ("+
				  "`eid` int(11) NOT NULL,"+
				  "`mid` int(11) DEFAULT NULL,"+
				  "KEY `eid_idx` (`eid`),"+
				  "KEY `mid_idx` (`mid`),"+
				  "CONSTRAINT `eid` FOREIGN KEY (`eid`) REFERENCES `employee` (`eid`) ON DELETE CASCADE ON UPDATE CASCADE,"+
				  "CONSTRAINT `mid` FOREIGN KEY (`mid`) REFERENCES `employee` (`eid`) ON DELETE CASCADE ON UPDATE CASCADE"+
				") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		stmt.executeUpdate(sql);
	}
	
	public static void drop_db(Statement stmt) throws Exception {
		String sql = "DROP TABLE `employee_database`.`worksfor`";
		stmt.executeUpdate(sql);
		sql = "DROP TABLE `employee_database`.`employee`";
		stmt.executeUpdate(sql);
	}

	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_database", "root", "faiziali@456");
			Statement stmt = con.createStatement();
			create_db(stmt);
			ex = new Executer(stmt);
			open_file("transfile.txt");
			drop_db(stmt);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
