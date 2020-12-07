import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ProjectMain extends JFrame {
	Vector<String> columnNames;
	Vector<Vector<String>> rowData;
	
	public JTextField textfieldSearch;
	public JTextField textfieldDivision;
	public JTextField textfieldSubject;
	public JTextField textfieldGotCredit;
	public JTextField textfieldSubCredit;
	
	private String[] hopetext = {"���̼� ����", "��� ����"};
	private JRadioButton [] hoperadio = new JRadioButton [2];
	public int hopenum;
	
	JButton button_search;
	JButton button_add;
	JButton button_modify;
	JButton button_delete;
	
	static int Sel = 0, BeforeValue = 0;
	static int GichoSum = 0, JeontamSum = 0, JeoninSum = 0,  JNUSum = 0;
	static int GyoeSum = 0, GyosoSum = 0, GyosilSum = 0, MajorSum = 0, GeneralSum = 0;
	
	public void SendMassage() {
		
	}

   
	public ProjectMain() {
		Vector<String> v =new Vector<String>();
		//frame �̸�
		setTitle("���� �̼� ����");
		
		//�Ҹ����

		Container container = getContentPane();

		//Layout ����
		container.setLayout(new FlowLayout());

		//���̺� �����
		CourseTable gichoTable = new CourseTable("���ʱ���(A)", 8);
		CourseTable jeontamTable = new CourseTable("����Ž��(B)", 10);
		CourseTable jeoninTable = new CourseTable("���α���(T)", 10);
		CourseTable JNUTable = new CourseTable("JNUƯ��ȭ����(J)", 10);
		CourseTable gyoeTable = new CourseTable("�����̷�", 12);
		CourseTable gyosoTable = new CourseTable("�����Ҿ�", 6);
		CourseTable gyosilTable = new CourseTable("�����ǽ�", 4);
		CourseTable majorTable = new CourseTable("����", 75);
		CourseTable generalTable = new CourseTable("�Ϲݼ��� / OCU", 0);
		
		//�ҿ� ���̺� �߰�
		container.add(gichoTable.jsp);
		container.add(jeontamTable.jsp);
		container.add(jeoninTable.jsp);
		container.add(JNUTable.jsp);
		container.add(gyoeTable.jsp);
		container.add(gyosoTable.jsp);
		container.add(gyosilTable.jsp);
		container.add(majorTable.jsp);
		container.add(generalTable.jsp);
		
		//���� ��ư �����
		JPanel radioPanel1 = new JPanel();
		ButtonGroup hopeg = new ButtonGroup();
	      for(int i = 0; i < hoperadio.length; i++) {
	         hoperadio[i] = new JRadioButton(hopetext[i]);
	         hopeg.add(hoperadio[i]);
	         radioPanel1.add(hoperadio[i]);
	         hoperadio[i].addItemListener(new HopeMyItemListener());
	      }
		hoperadio[0].setSelected(true);
	    
		//���� ��ư �߰�
		JLabel radiochoice = new JLabel("�̼�   ������   �������ּ��� : ");
		container.add(radiochoice);
	    container.add(radioPanel1);
	    
	    //�˻�â �߰�
	    JLabel labelSearch = new JLabel("   /   ����   �̸���   �Է��� �ּ���  :  ");	    
		textfieldSearch = new JTextField(15);
		container.add(labelSearch);
		container.add(textfieldSearch);	
		button_search = new JButton("�˻�");
		container.add(button_search);
		
		JLabel labelline = new JLabel("================================================================================== �˻� ��� =================================================================================");
		container.add(labelline);
		//�ؽ�Ʈ �� �����
		JPanel textlabel = new JPanel();
		
		JLabel labelDivsion=new JLabel("�о�");
		JLabel labelSubject=new JLabel("�����");
		JLabel labelGotCredit=new JLabel("�̼�����");
		JLabel labelSubCredit=new JLabel("�����������");
		
		//�ؽ�Ʈ�ʵ� �����
		textfieldDivision = new JTextField(10);
		textfieldSubject = new JTextField(15);
		textfieldGotCredit = new JTextField(7);
		textfieldSubCredit = new JTextField(7);
		
		//�ҿ� �ý�Ʈ�� �� �ؽ�Ʈ�ʵ� �߰�
		textlabel.add(labelDivsion);
		textlabel.add(textfieldDivision);
		textlabel.add(labelSubject);
		textlabel.add(textfieldSubject);
		textlabel.add(labelGotCredit);
		textlabel.add(textfieldGotCredit);
		textlabel.add(labelSubCredit);
		textlabel.add(textfieldSubCredit);
		
		container.add(textlabel);
		
		//��ư �����
		button_add=new JButton("�߰�");
		button_modify=new JButton("����");
		button_delete=new JButton("����");

		//�ҿ� ��ư�߰�
		container.add(button_add);
		container.add(button_modify);
		container.add(button_delete);
		
		setSize(1250, 850);						//������ũ��
		setResizable(false);
		setVisible(true);							//ȭ�鿡 ������ ���
		setDefaultCloseOperation(EXIT_ON_CLOSE);	//������ �����츦 ������ ���α׷� ����
		
		
//*******************************************************************************
//									�̺�Ʈ������ �߰�
//*******************************************************************************
//case 	1 = ���ʱ��� 
//		2 = ����Ž��
//		3 = ���α���
//		4 = JNUƯ��ȭ����
//		5 = �����̷�
//		6 = �����Ҿ�
//		7 = �����ǽ�
//		8 = ����
//		9 = �Ϲݼ���

//*** �˻� ��ư ***
		button_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sub = textfieldSearch.getText();
				String deSel;
				//DB���� ������ �˻�
				JDBC db = new JDBC(sub);
				
				//�ؽ�Ʈ�ʵ忡 �����ִ� �� �����ֱ�.
				textfieldSearch.setText("");
				textfieldDivision.setText("");
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
				
				//�ؽ�Ʈ�ʵ忡 �� �ֱ�
				textfieldDivision.setText(db.rDivision);
				textfieldSubject.setText(db.rSubject);
				if(hopenum == 1) {
					textfieldGotCredit.setText(db.rCredit);
					textfieldSubCredit.setText("0");
				}
				else {
					textfieldGotCredit.setText("0");
					textfieldSubCredit.setText(db.rCredit);
				}

				//data�� ���� table ��ġ ���ϱ�
				deSel = db.rDivision;
				if(deSel.contentEquals("���ʱ���"))
					Sel = 1;
				else if(deSel.contentEquals("����Ž������"))
					Sel = 2;
				else if(deSel.contentEquals("���α���"))
					Sel = 3;
				else if(deSel.contentEquals("JNUƯ��ȭ����"))
					Sel = 4;
				else if(deSel.contentEquals("�����̷�"))
					Sel = 5;
				else if(deSel.contentEquals("�����Ҿ�"))
					Sel = 6;
				else if(deSel.contentEquals("�����ǽ�")|| deSel.contentEquals("��������"))
					Sel = 7;
				else if(deSel.contentEquals("����")|| deSel.contentEquals("��������"))
					Sel = 8;
				else if(deSel.contentEquals("�Ϲݼ���")|| deSel.contentEquals("����"))
					Sel = 9;
				else
					System.out.println("���� ���� ���� ����");
			}
		});
		
		
		
//*** �߰� ��ư *** 
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�ؽ�Ʈ�ʵ忡 �ִ� ������ ���Ϳ� ����
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				//�о� table ������ table ������Ʈ
				switch(Sel) {
				case 0:
					System.out.println("�о߼��� 0");
					break;
				case 1:
					gichoTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gichoTable.table.updateUI();
					GichoSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gichoTable.table.setValueAt(GichoSum, 0, 3);
					break;
				case 2:
					jeontamTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					jeontamTable.table.updateUI();
					JeontamSum += Integer.parseInt(got) + Integer.parseInt(sub);
					jeontamTable.table.setValueAt(JeontamSum, 0, 3);
					break;
				case 3:
					jeoninTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					jeoninTable.table.updateUI();
					JeoninSum += Integer.parseInt(got) + Integer.parseInt(sub);
					jeoninTable.table.setValueAt(JeoninSum, 0, 3);
					break;
				case 4:
					JNUTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					JNUTable.table.updateUI();
					JNUSum += Integer.parseInt(got) + Integer.parseInt(sub);
					JNUTable.table.setValueAt(JNUSum, 0, 3);
					break;
				case 5:
					gyoeTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyoeTable.table.updateUI();
					GyoeSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyoeTable.table.setValueAt(GyoeSum, 0, 3);
					break;
				case 6:
					gyosoTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyosoTable.table.updateUI();
					GyosoSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyosoTable.table.setValueAt(GyosoSum, 0, 3);
					break;
				case 7:
					gyosilTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyosilTable.table.updateUI();
					GyosilSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyosilTable.table.setValueAt(GyosilSum, 0, 3);
					break;
				case 8:
					majorTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					majorTable.table.updateUI();
					MajorSum += Integer.parseInt(got) + Integer.parseInt(sub);
					majorTable.table.setValueAt(MajorSum, 0, 3);
					break;
				case 9:
					generalTable.rowData.add(v);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					generalTable.table.updateUI();
					GeneralSum += Integer.parseInt(got) + Integer.parseInt(sub);
					generalTable.table.setValueAt(GeneralSum, 0, 3);
					break;
				default:
					System.out.println("�о߼��� ����");	
				}
				//�߰��Ŀ� �ؽ�Ʈ�ʵ� Ŭ����
				textfieldDivision.setText("");
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
			}
		});
		
//*** ���� ��ư *** 
//�����ϰ� ���� row data�� ���콺 Ŭ�� �̺�Ʈ�� �̹� �ؽ�Ʈ�ʵ忡 ����Ǿ� �ִ�.
//�����ϰ� ���� �ؽ�Ʈ�ʵ带 ������ �� ������ư�� ������.
		button_modify.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//�ؽ�Ʈ �ʵ忡 �ִ� ������ vector�� ����
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				//���õ� table��ġ�� �ٽ� ����
				int selection = 0;	//���̺��� �� ��ȣ�� ����
				
				switch(Sel) {
				case 0:
					System.out.println("�о߼��� 0");
					break;
				case 1:
					selection = gichoTable.table.getSelectedRow();
					gichoTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gichoTable.table.updateUI();
					GichoSum = GichoSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gichoTable.table.setValueAt(GichoSum, 0, 3);
					break;
				case 2:
					selection = jeontamTable.table.getSelectedRow();
					jeontamTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					jeontamTable.table.updateUI();
					JeontamSum = JeontamSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					jeontamTable.table.setValueAt(JeontamSum, 0, 3);
					break;
				case 3:
					selection = jeoninTable.table.getSelectedRow();
					jeoninTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					jeoninTable.table.updateUI();
					JeoninSum = JeoninSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					jeoninTable.table.setValueAt(JeoninSum, 0, 3);
					break;
				case 4:
					selection = JNUTable.table.getSelectedRow();
					JNUTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					JNUTable.table.updateUI();
					JNUSum = JNUSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					JNUTable.table.setValueAt(JNUSum, 0, 3);
					break;
				case 5:
					selection = gyoeTable.table.getSelectedRow();
					gyoeTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyoeTable.table.updateUI();
					GyoeSum = GyoeSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyoeTable.table.setValueAt(GyoeSum, 0, 3);
					break;
				case 6:
					selection = gyosoTable.table.getSelectedRow();
					gyosoTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyosoTable.table.updateUI();
					GyosoSum = GyosoSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyosoTable.table.setValueAt(GyosoSum, 0, 3);
					break;
				case 7:
					selection = gyosilTable.table.getSelectedRow();
					gyosilTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					gyosilTable.table.updateUI();
					GyosilSum = GyosilSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyosilTable.table.setValueAt(GyosilSum, 0, 3);
					break;
				case 8:
					selection = majorTable.table.getSelectedRow();
					majorTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					majorTable.table.updateUI();
					MajorSum = MajorSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					majorTable.table.setValueAt(MajorSum, 0, 3);
					break;
				case 9:
					selection = generalTable.table.getSelectedRow();
					generalTable.rowData.setElementAt(v,selection);//�ؽ�Ʈ �ʵ忡 �ִ� ���� �߰��ϱ�.
					generalTable.table.updateUI();
					GeneralSum = GeneralSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					generalTable.table.setValueAt(GeneralSum, 0, 3);
					break;
				default:
					System.out.println("�о߼��� ����");
				}
				textfieldDivision.setText("");//�����Ŀ�, �ؽ�Ʈ�ʵ忡 �����ִ� �� �����ֱ�.
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
			}
		});
		
//*** ���� ��ư ***
//���콺�� �����ϰ� ���� ���� ������ �����̴�. 
		button_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//�ؽ�Ʈ �ʵ忡 �ִ� ������ vector�� ����
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
//				if(subject.equals("")) {
//					System.out.println("���� ����� �����ϼ���");
//				}
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				int selection = 0;
				if(subject.equals("")) System.out.println("���� ����� �����ϼ���");
				else {

					switch(Sel) {
					case 0:
						System.out.println("�о߼��� 0");
						break;
					case 1:
						selection = gichoTable.table.getSelectedRow();
						gichoTable.rowData.remove(selection);// �ش� �� ����
						gichoTable.table.updateUI();
						GichoSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gichoTable.table.setValueAt(GichoSum, 0, 3);
						break;
					case 2:
						selection = jeontamTable.table.getSelectedRow();
						jeontamTable.rowData.remove(selection);// �ش� �� ����
						jeontamTable.table.updateUI();
						JeontamSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						jeontamTable.table.setValueAt(JeontamSum, 0, 3);
						break;
					case 3:
						selection = jeoninTable.table.getSelectedRow();
						jeoninTable.rowData.remove(selection);// �ش� �� ����
						jeoninTable.table.updateUI();
						JeoninSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						jeoninTable.table.setValueAt(JeoninSum, 0, 3);
						break;
					case 4:
						selection = JNUTable.table.getSelectedRow();
						JNUTable.rowData.remove(selection);// �ش� �� ����
						JNUTable.table.updateUI();
						JNUSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						JNUTable.table.setValueAt(JNUSum, 0, 3);
						break;
					case 5:
						selection = gyoeTable.table.getSelectedRow();
						gyoeTable.rowData.remove(selection);// �ش� �� ����
						gyoeTable.table.updateUI();
						GyoeSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyoeTable.table.setValueAt(GyoeSum, 0, 3);
						break;
					case 6:
						selection = gyosoTable.table.getSelectedRow();
						gyosoTable.rowData.remove(selection);// �ش� �� ����
						gyosoTable.table.updateUI();
						GyosoSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyosoTable.table.setValueAt(GyosoSum, 0, 3);
						break;
					case 7:
						selection = gyosilTable.table.getSelectedRow();
						gyosilTable.rowData.remove(selection);// �ش� �� ����
						gyosilTable.table.updateUI();
						GyosilSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyosilTable.table.setValueAt(GyosilSum, 0, 3);
						break;
					case 8:
						selection = majorTable.table.getSelectedRow();
						majorTable.rowData.remove(selection);// �ش� �� ����
						majorTable.table.updateUI();
						MajorSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						majorTable.table.setValueAt(MajorSum, 0, 3);
						break;
					case 9:
						selection = generalTable.table.getSelectedRow();
						generalTable.rowData.remove(selection);// �ش� �� ����
						generalTable.table.updateUI();
						GeneralSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						majorTable.table.setValueAt(GeneralSum, 0, 3);
						break;
					default:
						System.out.println("�о߼��� ����");
					}
					textfieldDivision.setText("");//�ؽ�Ʈ�ʵ忡 �����ִ� �� �����ֱ�.
					textfieldSubject.setText("");
					textfieldGotCredit.setText("");
					textfieldSubCredit.setText("");
				}
			}
		});
		
//*** ���콺 �̺�Ʈ***
//���ʱ���
		gichoTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�
				Sel = 1;
				int selection=gichoTable.table.getSelectedRow();
				Vector<String> vc=gichoTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//����Ž��
		jeontamTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�
				Sel = 2;
				int selection=jeontamTable.table.getSelectedRow();
				Vector<String> vc=jeontamTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//���α���
		jeoninTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 3;
				int selection=jeoninTable.table.getSelectedRow();
				Vector<String> vc=jeoninTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//JNUƯ��ȭ ����
		JNUTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 4;
				int selection=JNUTable.table.getSelectedRow();
				Vector<String> vc=JNUTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//�����̷�		
		gyoeTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 5;
				int selection=gyoeTable.table.getSelectedRow();
				Vector<String> vc=gyoeTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//�����Ҿ�
		gyosoTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 6;
				int selection=gyosoTable.table.getSelectedRow();
				Vector<String> vc=gyosoTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//�����ǽ�
		gyosilTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 7;
				int selection=gyosilTable.table.getSelectedRow();
				Vector<String> vc=gyosilTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));
			}
		});
//����
		majorTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 8;
				int selection=majorTable.table.getSelectedRow();
				Vector<String> vc=majorTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));

			}
		});
//�Ϲݼ���		
		generalTable.table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			//���콺�� ������ ���� ������ ������ �ؽ�Ʈ�ʵ忡 ����ϱ�	
				Sel = 9;
				int selection=generalTable.table.getSelectedRow();
				Vector<String> vc=generalTable.rowData.get(selection);
				
				textfieldDivision.setText(vc.get(0));
				textfieldSubject.setText(vc.get(1));
				textfieldGotCredit.setText(vc.get(2));
				textfieldSubCredit.setText(vc.get(3));
				
				BeforeValue = Integer.parseInt(vc.get(2)) + Integer.parseInt(vc.get(3));

			}
		});
		
	}
	
	public static void main(String[] args) {
		//new ExcelReadPractice();
		//JDBC db = new JDBC(sub);
		ProjectMain p = new ProjectMain();
	}
	
	   class HopeMyItemListener implements ItemListener{
		      public void itemStateChanged(ItemEvent e) {
		         if(e.getStateChange() == ItemEvent.DESELECTED)
		            return;
		         if(hoperadio[0].isSelected())
		            hopenum = 1;
		         else
		            hopenum = 2;

		      }
		   }
	   
}