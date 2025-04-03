public class Major implements Comparable<Major> {
	private String name;
	private double acptGrade;
	private double tawjihiWeight;
	private double ptWeight;
	private SLL students;

	public Major(String name, double acptGrade, double tawjihiWeight, double ptWeight) {
		this.name = name;
		this.acptGrade = acptGrade;
		this.tawjihiWeight = tawjihiWeight;
		this.ptWeight = ptWeight;
		this.setStudents(new SLL()); 
	}

	public void addStudent(Student student) {
		students.insert(student);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAcptGrade() {
		return acptGrade;
	}

	public void setAcptGrade(double acptGrade) {
		this.acptGrade = acptGrade;
	}

	public double getTawjihiWeight() {
		return tawjihiWeight;
	}

	public void setTawjihiWeight(double tawjihiWeight) {
		this.tawjihiWeight = tawjihiWeight;
	}

	public double getPtWeight() {
		return ptWeight;
	}

	public void setPtWeight(double ptWeight) {
		this.ptWeight = ptWeight;
	}

	@Override
	public int compareTo(Major other) {
		return this.name.compareToIgnoreCase(other.name);
	}

	public SLL getStudents() {
		return students;
	}

	public void setStudents(SLL students) {
		this.students = students;
	}
}
