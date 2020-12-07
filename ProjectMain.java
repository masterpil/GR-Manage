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
	
	private String[] hopetext = {"기이수 과목", "희망 과목"};
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
		//frame 이름
		setTitle("졸업 이수 학점");
		
		//팬만들기

		Container container = getContentPane();

		//Layout 설정
		container.setLayout(new FlowLayout());

		//테이블 만들기
		CourseTable gichoTable = new CourseTable("기초교양(A)", 8);
		CourseTable jeontamTable = new CourseTable("전공탐색(B)", 10);
		CourseTable jeoninTable = new CourseTable("전인교양(T)", 10);
		CourseTable JNUTable = new CourseTable("JNU특성화교양(J)", 10);
		CourseTable gyoeTable = new CourseTable("교직이론", 12);
		CourseTable gyosoTable = new CourseTable("교직소양", 6);
		CourseTable gyosilTable = new CourseTable("교육실습", 4);
		CourseTable majorTable = new CourseTable("전공", 75);
		CourseTable generalTable = new CourseTable("일반선택 / OCU", 0);
		
		//팬에 테이블 추가
		container.add(gichoTable.jsp);
		container.add(jeontamTable.jsp);
		container.add(jeoninTable.jsp);
		container.add(JNUTable.jsp);
		container.add(gyoeTable.jsp);
		container.add(gyosoTable.jsp);
		container.add(gyosilTable.jsp);
		container.add(majorTable.jsp);
		container.add(generalTable.jsp);
		
		//라디오 버튼 만들기
		JPanel radioPanel1 = new JPanel();
		ButtonGroup hopeg = new ButtonGroup();
	      for(int i = 0; i < hoperadio.length; i++) {
	         hoperadio[i] = new JRadioButton(hopetext[i]);
	         hopeg.add(hoperadio[i]);
	         radioPanel1.add(hoperadio[i]);
	         hoperadio[i].addItemListener(new HopeMyItemListener());
	      }
		hoperadio[0].setSelected(true);
	    
		//라디오 버튼 추가
		JLabel radiochoice = new JLabel("이수   구분을   선택해주세요 : ");
		container.add(radiochoice);
	    container.add(radioPanel1);
	    
	    //검색창 추가
	    JLabel labelSearch = new JLabel("   /   과목   이름을   입력해 주세요  :  ");	    
		textfieldSearch = new JTextField(15);
		container.add(labelSearch);
		container.add(textfieldSearch);	
		button_search = new JButton("검색");
		container.add(button_search);
		
		JLabel labelline = new JLabel("================================================================================== 검색 결과 =================================================================================");
		container.add(labelline);
		//텍스트 라벨 만들기
		JPanel textlabel = new JPanel();
		
		JLabel labelDivsion=new JLabel("분야");
		JLabel labelSubject=new JLabel("과목명");
		JLabel labelGotCredit=new JLabel("이수학점");
		JLabel labelSubCredit=new JLabel("희망과목학점");
		
		//텍스트필드 만들기
		textfieldDivision = new JTextField(10);
		textfieldSubject = new JTextField(15);
		textfieldGotCredit = new JTextField(7);
		textfieldSubCredit = new JTextField(7);
		
		//팬에 택스트라벨 및 텍스트필드 추가
		textlabel.add(labelDivsion);
		textlabel.add(textfieldDivision);
		textlabel.add(labelSubject);
		textlabel.add(textfieldSubject);
		textlabel.add(labelGotCredit);
		textlabel.add(textfieldGotCredit);
		textlabel.add(labelSubCredit);
		textlabel.add(textfieldSubCredit);
		
		container.add(textlabel);
		
		//버튼 만들기
		button_add=new JButton("추가");
		button_modify=new JButton("수정");
		button_delete=new JButton("삭제");

		//팬에 버튼추가
		container.add(button_add);
		container.add(button_modify);
		container.add(button_delete);
		
		setSize(1250, 850);						//프레임크기
		setResizable(false);
		setVisible(true);							//화면에 프레임 출력
		setDefaultCloseOperation(EXIT_ON_CLOSE);	//프레임 윈도우를 닫으면 프로그램 종료
		
		
//*******************************************************************************
//									이벤트리스너 추가
//*******************************************************************************
//case 	1 = 기초교양 
//		2 = 전공탐색
//		3 = 전인교양
//		4 = JNU특성화교양
//		5 = 교직이론
//		6 = 교직소양
//		7 = 교육실습
//		8 = 전공
//		9 = 일반선택

//*** 검색 버튼 ***
		button_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sub = textfieldSearch.getText();
				String deSel;
				//DB에서 데이터 검색
				JDBC db = new JDBC(sub);
				
				//텍스트필드에 남아있는 값 지워주기.
				textfieldSearch.setText("");
				textfieldDivision.setText("");
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
				
				//텍스트필드에 값 넣기
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

				//data를 넣을 table 위치 정하기
				deSel = db.rDivision;
				if(deSel.contentEquals("기초교양"))
					Sel = 1;
				else if(deSel.contentEquals("전공탐색교양"))
					Sel = 2;
				else if(deSel.contentEquals("전인교양"))
					Sel = 3;
				else if(deSel.contentEquals("JNU특성화교양"))
					Sel = 4;
				else if(deSel.contentEquals("교직이론"))
					Sel = 5;
				else if(deSel.contentEquals("교직소양"))
					Sel = 6;
				else if(deSel.contentEquals("교육실습")|| deSel.contentEquals("교직과목"))
					Sel = 7;
				else if(deSel.contentEquals("전공")|| deSel.contentEquals("전공선택"))
					Sel = 8;
				else if(deSel.contentEquals("일반선택")|| deSel.contentEquals("교양"))
					Sel = 9;
				else
					System.out.println("과목 영역 구분 에러");
			}
		});
		
		
		
//*** 추가 버튼 *** 
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//텍스트필드에 있는 내용을 벡터에 저장
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				//분야 table 선택후 table 업데이트
				switch(Sel) {
				case 0:
					System.out.println("분야선택 0");
					break;
				case 1:
					gichoTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					gichoTable.table.updateUI();
					GichoSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gichoTable.table.setValueAt(GichoSum, 0, 3);
					break;
				case 2:
					jeontamTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					jeontamTable.table.updateUI();
					JeontamSum += Integer.parseInt(got) + Integer.parseInt(sub);
					jeontamTable.table.setValueAt(JeontamSum, 0, 3);
					break;
				case 3:
					jeoninTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					jeoninTable.table.updateUI();
					JeoninSum += Integer.parseInt(got) + Integer.parseInt(sub);
					jeoninTable.table.setValueAt(JeoninSum, 0, 3);
					break;
				case 4:
					JNUTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					JNUTable.table.updateUI();
					JNUSum += Integer.parseInt(got) + Integer.parseInt(sub);
					JNUTable.table.setValueAt(JNUSum, 0, 3);
					break;
				case 5:
					gyoeTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					gyoeTable.table.updateUI();
					GyoeSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyoeTable.table.setValueAt(GyoeSum, 0, 3);
					break;
				case 6:
					gyosoTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					gyosoTable.table.updateUI();
					GyosoSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyosoTable.table.setValueAt(GyosoSum, 0, 3);
					break;
				case 7:
					gyosilTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					gyosilTable.table.updateUI();
					GyosilSum += Integer.parseInt(got) + Integer.parseInt(sub);
					gyosilTable.table.setValueAt(GyosilSum, 0, 3);
					break;
				case 8:
					majorTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					majorTable.table.updateUI();
					MajorSum += Integer.parseInt(got) + Integer.parseInt(sub);
					majorTable.table.setValueAt(MajorSum, 0, 3);
					break;
				case 9:
					generalTable.rowData.add(v);//텍스트 필드에 있는 값을 추가하기.
					generalTable.table.updateUI();
					GeneralSum += Integer.parseInt(got) + Integer.parseInt(sub);
					generalTable.table.setValueAt(GeneralSum, 0, 3);
					break;
				default:
					System.out.println("분야선택 오류");	
				}
				//추가후에 텍스트필드 클리어
				textfieldDivision.setText("");
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
			}
		});
		
//*** 수정 버튼 *** 
//수정하고 싶은 row data가 마우스 클릭 이벤트로 이미 텍스트필드에 저장되어 있다.
//수정하고 싶은 텍스트필드를 수정한 후 수정버튼을 누른다.
		button_modify.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//텍스트 필드에 있는 내용을 vector에 저장
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				//선택된 table위치에 다시 저장
				int selection = 0;	//테이블의 열 번호를 저장
				
				switch(Sel) {
				case 0:
					System.out.println("분야선택 0");
					break;
				case 1:
					selection = gichoTable.table.getSelectedRow();
					gichoTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					gichoTable.table.updateUI();
					GichoSum = GichoSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gichoTable.table.setValueAt(GichoSum, 0, 3);
					break;
				case 2:
					selection = jeontamTable.table.getSelectedRow();
					jeontamTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					jeontamTable.table.updateUI();
					JeontamSum = JeontamSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					jeontamTable.table.setValueAt(JeontamSum, 0, 3);
					break;
				case 3:
					selection = jeoninTable.table.getSelectedRow();
					jeoninTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					jeoninTable.table.updateUI();
					JeoninSum = JeoninSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					jeoninTable.table.setValueAt(JeoninSum, 0, 3);
					break;
				case 4:
					selection = JNUTable.table.getSelectedRow();
					JNUTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					JNUTable.table.updateUI();
					JNUSum = JNUSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					JNUTable.table.setValueAt(JNUSum, 0, 3);
					break;
				case 5:
					selection = gyoeTable.table.getSelectedRow();
					gyoeTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					gyoeTable.table.updateUI();
					GyoeSum = GyoeSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyoeTable.table.setValueAt(GyoeSum, 0, 3);
					break;
				case 6:
					selection = gyosoTable.table.getSelectedRow();
					gyosoTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					gyosoTable.table.updateUI();
					GyosoSum = GyosoSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyosoTable.table.setValueAt(GyosoSum, 0, 3);
					break;
				case 7:
					selection = gyosilTable.table.getSelectedRow();
					gyosilTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					gyosilTable.table.updateUI();
					GyosilSum = GyosilSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					gyosilTable.table.setValueAt(GyosilSum, 0, 3);
					break;
				case 8:
					selection = majorTable.table.getSelectedRow();
					majorTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					majorTable.table.updateUI();
					MajorSum = MajorSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					majorTable.table.setValueAt(MajorSum, 0, 3);
					break;
				case 9:
					selection = generalTable.table.getSelectedRow();
					generalTable.rowData.setElementAt(v,selection);//텍스트 필드에 있는 값을 추가하기.
					generalTable.table.updateUI();
					GeneralSum = GeneralSum - BeforeValue + Integer.parseInt(got) + Integer.parseInt(sub);
					generalTable.table.setValueAt(GeneralSum, 0, 3);
					break;
				default:
					System.out.println("분야선택 오류");
				}
				textfieldDivision.setText("");//수정후에, 텍스트필드에 남아있는 값 지워주기.
				textfieldSubject.setText("");
				textfieldGotCredit.setText("");
				textfieldSubCredit.setText("");
			}
		});
		
//*** 삭제 버튼 ***
//마우스로 삭제하고 싶은 열을 선택한 상태이다. 
		button_delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//텍스트 필드에 있는 내용을 vector에 저장
				String division=textfieldDivision.getText();
				String subject=textfieldSubject.getText();
				String got=textfieldGotCredit.getText();
				String sub=textfieldSubCredit.getText();
				
//				if(subject.equals("")) {
//					System.out.println("삭제 대상을 선택하세요");
//				}
				
				Vector<String> v =new Vector<String>();
				v.add(division);
				v.add(subject);
				v.add(got);
				v.add(sub);
				
				int selection = 0;
				if(subject.equals("")) System.out.println("삭제 대상을 선택하세요");
				else {

					switch(Sel) {
					case 0:
						System.out.println("분야선택 0");
						break;
					case 1:
						selection = gichoTable.table.getSelectedRow();
						gichoTable.rowData.remove(selection);// 해당 열 삭제
						gichoTable.table.updateUI();
						GichoSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gichoTable.table.setValueAt(GichoSum, 0, 3);
						break;
					case 2:
						selection = jeontamTable.table.getSelectedRow();
						jeontamTable.rowData.remove(selection);// 해당 열 삭제
						jeontamTable.table.updateUI();
						JeontamSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						jeontamTable.table.setValueAt(JeontamSum, 0, 3);
						break;
					case 3:
						selection = jeoninTable.table.getSelectedRow();
						jeoninTable.rowData.remove(selection);// 해당 열 삭제
						jeoninTable.table.updateUI();
						JeoninSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						jeoninTable.table.setValueAt(JeoninSum, 0, 3);
						break;
					case 4:
						selection = JNUTable.table.getSelectedRow();
						JNUTable.rowData.remove(selection);// 해당 열 삭제
						JNUTable.table.updateUI();
						JNUSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						JNUTable.table.setValueAt(JNUSum, 0, 3);
						break;
					case 5:
						selection = gyoeTable.table.getSelectedRow();
						gyoeTable.rowData.remove(selection);// 해당 열 삭제
						gyoeTable.table.updateUI();
						GyoeSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyoeTable.table.setValueAt(GyoeSum, 0, 3);
						break;
					case 6:
						selection = gyosoTable.table.getSelectedRow();
						gyosoTable.rowData.remove(selection);// 해당 열 삭제
						gyosoTable.table.updateUI();
						GyosoSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyosoTable.table.setValueAt(GyosoSum, 0, 3);
						break;
					case 7:
						selection = gyosilTable.table.getSelectedRow();
						gyosilTable.rowData.remove(selection);// 해당 열 삭제
						gyosilTable.table.updateUI();
						GyosilSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						gyosilTable.table.setValueAt(GyosilSum, 0, 3);
						break;
					case 8:
						selection = majorTable.table.getSelectedRow();
						majorTable.rowData.remove(selection);// 해당 열 삭제
						majorTable.table.updateUI();
						MajorSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						majorTable.table.setValueAt(MajorSum, 0, 3);
						break;
					case 9:
						selection = generalTable.table.getSelectedRow();
						generalTable.rowData.remove(selection);// 해당 열 삭제
						generalTable.table.updateUI();
						GeneralSum -= (Integer.parseInt(got) + Integer.parseInt(sub));
						majorTable.table.setValueAt(GeneralSum, 0, 3);
						break;
					default:
						System.out.println("분야선택 오류");
					}
					textfieldDivision.setText("");//텍스트필드에 남아있는 값 지워주기.
					textfieldSubject.setText("");
					textfieldGotCredit.setText("");
					textfieldSubCredit.setText("");
				}
			}
		});
		
//*** 마우스 이벤트***
//기초교양
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기
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
//전공탐색
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기
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
//전인교양
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//JNU특성화 교양
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//교직이론		
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//교직소양
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//교육실습
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//전공
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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
//일반선택		
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
			//마우스로 선택한 행의 정보를 각각의 텍스트필드에 출력하기	
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