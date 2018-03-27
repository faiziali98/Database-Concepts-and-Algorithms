package hw4databse;

import java.util.ArrayList;
public class Employee  {
	
	public Integer id;
	public String name = "";
	public Integer salary = 0;
	ArrayList<Integer> employees = new ArrayList<Integer>();
	
	public Employee(String[] tok) {
		id = new Integer(tok[1]);
		name = tok[2];
		salary = new Integer(tok[3]);
		for(int i = 4; i < tok.length; i++){
			if (Integer.parseInt(tok[i])!=0) {
				employees.add(new Integer(tok[i]));
			}
		}
	}

}
