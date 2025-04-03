
public class M_Node {
	private Major major;
	private M_Node prev;
	private M_Node next;

	public M_Node(Major major) {
		this.major = major;
		this.prev = null;
		this.next = null;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public M_Node getPrev() {
		return prev;
	}

	public void setPrev(M_Node prev) {
		this.prev = prev;
	}

	public M_Node getNext() {
		return next;
	}

	public void setNext(M_Node next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "M_Node [major=" + major + ", prev=" + prev + ", next=" + next + "]";
	}
}
