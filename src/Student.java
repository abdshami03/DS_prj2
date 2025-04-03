public class Student implements Comparable<Student> {
	private int sid;
	private String name;
	private double t_grade;
	private double pt_grade;
	private double adm_mark;
	private String st_major;

	public Student(int sid, String name, double t_grade, double pt_grade, String st_major) {
		this.sid = sid;
		this.name = name;
		this.t_grade = t_grade;
		this.pt_grade = pt_grade;
		this.st_major = st_major;
		this.adm_mark = 0.0;
	}

	public int getSid() {
		return sid;
	}

	public String getName() {
		return name;
	}

	public double getT_grade() {
		return t_grade;
	}

	public double getPt_grade() {
		return pt_grade;
	}

	public double getAdm_mark() {
		return adm_mark;
	}

	public String getSt_major() {
		return st_major;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setT_grade(double t_grade) {
		this.t_grade = t_grade;
	}

	public void setPt_grade(double pt_grade) {
		this.pt_grade = pt_grade;
	}

	public void setAdm_mark(double adm_mark) {
		this.adm_mark = adm_mark;
	}

	public void setSt_major(String st_major) {
		this.st_major = st_major;
	}

	@Override
	public int compareTo(Student other) {
		return Double.compare(other.adm_mark, this.adm_mark);
	}
}
