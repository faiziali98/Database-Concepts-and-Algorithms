package hw4databse;

import java.sql.*;
import java.util.ArrayList;

public class Executer {

	public Statement rs;
	
	public Executer(Statement rs) {
		this.rs = rs;
	}
	
	public void addEmployee(Employee e) throws Exception {
		ArrayList<Integer> employees = e.employees;
		String sql = "INSERT INTO employee " +
				"(eid, name, salary) VALUES " +
				"(" + e.id + ", '" + e.name + "', " + e.salary + ")";
		rs.executeUpdate(sql);
		for (int i = 0; i < employees.size(); i++){
			addWorkfor(e.id, employees.get(i));
		}
		System.out.println("done add");
	}
	
	public void addWorkfor(Integer employee, Integer manager) throws Exception {
		String sql = "INSERT INTO worksfor " +
				"(eid, mid) VALUES " +
				"(" + employee + ", " + manager +")";
		rs.executeUpdate(sql);
	}
	
	public void delete_employee(String e) throws Exception {
		String sql = "DELETE FROM employee " +
					 "WHERE eid = " + e;
		rs.executeUpdate(sql);
		System.out.println("done delete");
	}
	
	public void averageSalary() throws Exception {
		String sql = "SELECT AVG(salary) " +
				"FROM employee ";
		
		ResultSet r = rs.executeQuery(sql);
		if(r.next()){
			System.out.println("Average Salary = " + r.getInt(1));
		}else {
			System.out.println(-1);
		}
	}
	
	public ArrayList<Integer> get_ids(String id, boolean flag) throws Exception{
		String sql = "SELECT eid " +
				"FROM worksfor " +
				"WHERE mid = " + id;
		ResultSet r = rs.executeQuery(sql);
		ArrayList<Integer> employees = new ArrayList<Integer>();
		while (r.next()){
			employees.add(r.getInt(1));
		}
		if (flag) {
			ArrayList<Integer> eEmployees = new ArrayList<Integer>();
			for (Integer x : employees){
				eEmployees.addAll(get_ids(x.toString(),true));
			}
			
			employees.addAll(eEmployees);
		}
		return employees;
	}
	
	public String arrTOstring(ArrayList<Integer> ids) {
		String list = "";
		for (Integer id : ids) {
			list = list + id.toString() + ", ";
		}
		list = list.substring(0, list.length() - 2);
		return list;
	}
	
	public void getNames(ArrayList<Integer> ids) throws Exception {
		String list = arrTOstring(ids);
		String sql = "SELECT name " +
				"FROM employee " +
				"WHERE eid IN (" + list + ")";
		ResultSet r = rs.executeQuery(sql);
		
		while(r.next()){
			System.out.println(r.getString(1));
		}
	}
	
	public void getsalary(ArrayList<Integer> ids) throws Exception {
		String list = arrTOstring(ids);
		String sql = "SELECT salary " +
				"FROM employee " +
				"WHERE eid IN (" + list + ")";
		
		ResultSet r = rs.executeQuery(sql);
		Integer sum = 0;
		Integer tot = 0;
		
		while(r.next()){
			sum = sum + r.getInt(1);
			tot += 1;
		}
		
		System.out.println(sum/tot);
	}
	
	public void namesOFUNDEREmployees(String id) throws Exception {
		System.out.println("Employees under: "+id);
		ArrayList<Integer> arr = get_ids(id,true);
		if (!arr.isEmpty()) {
			getNames(arr);
		}else {
			System.out.println("None");
		}
	}
	
	public void avgSalUnder(String id) throws Exception {
		System.out.println("Average salary of employee under: "+id);
		ArrayList<Integer> arr = get_ids(id,false);
		if (!arr.isEmpty()) {
			getsalary(arr);
		}else {
			System.out.println(0);
		}
	}
	
	public void moreThanOneManager() throws Exception {
		System.out.println("Employees with more than one manager: ");
		String sql = "SELECT DISTINCT name "+
					 "from employee e "+
					 "where e.eid in (SELECT wf.eid FROM worksfor AS wf "+
						             "GROUP BY wf.eid "+
						             "HAVING count(*) > 1)";

		ResultSet r = rs.executeQuery(sql);
		boolean flag = true; 
		
		while (r.next()){
			flag = false;
			System.out.println(r.getString(1));
		}
		
		if (flag) {
			System.out.println("No employees with more than one manager");
		}
	}
}
