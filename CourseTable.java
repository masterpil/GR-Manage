import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public  class CourseTable{
	JTable table;
	JScrollPane jsp;
	Vector<String> columnNames;
	Vector<Vector<String>> rowData;

	CourseTable(String name, int standard){
		columnNames = new Vector<String>();
		rowData = new Vector<Vector<String>>();
		
		columnNames.add(name);
		columnNames.add("");
		columnNames.add("");
		columnNames.add("");
		
		table = new JTable(rowData, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(400, 200));
		
		Vector<String> v =new Vector<String>();
		v.add("기준학점");
		v.add(standard+"");
		v.add("이수학점");
		v.add("0");
		rowData.add(v);
		table.updateUI();

		jsp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
}
