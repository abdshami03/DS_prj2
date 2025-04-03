
public class S_Node {
	private Student student;
	private S_Node next;

	public S_Node(Student student) {
		this.student = student;
		this.next = null;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public S_Node getNext() {
		return next;
	}

	public void setNext(S_Node next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "S_Node [student=" + student + ", next=" + next + "]";
	}

}
