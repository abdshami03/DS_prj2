
public class SLL {
	private S_Node head;

	public SLL() {
		head = null;
	}

	public void insert(Student student) {
		S_Node newNode = new S_Node(student);

		if (head == null) {
			head = newNode;
			head.setNext(head);
		} else {
			S_Node curr = head;
			S_Node prev = null;

			do {
				if (curr.getStudent().getAdm_mark() < student.getAdm_mark()) {
					break;
				}
				prev = curr;
				curr = curr.getNext();
			} while (curr != head);

			if (prev == null) {
				newNode.setNext(head);

				S_Node temp = head;
				while (temp.getNext() != head) {
					temp = temp.getNext();
				}
				temp.setNext(newNode);
				head = newNode;
			} else {
				newNode.setNext(curr);
				prev.setNext(newNode);
			}
		}
	}

	public boolean delete(int id) {
		if (head == null) {
			return false;
		}

		S_Node curr = head;
		S_Node prev = null;

		do {
			if (curr.getStudent().getSid() == id) {
				if (curr == head) {
					if (head.getNext() == head) {
						head = null;
					} else {
						S_Node temp = head;
						while (temp.getNext() != head) {
							temp = temp.getNext();
						}
						temp.setNext(head.getNext());
						head = head.getNext();
					}
				} else {
					prev.setNext(curr.getNext());
				}
				return true;
			}
			prev = curr;
			curr = curr.getNext();
		} while (curr != head);

		return false;
	}

	public boolean update(int id, String name, double tawjihiGrade, double placementTestGrade, String major) {
		if (tawjihiGrade < 0 || tawjihiGrade > 100 || placementTestGrade < 0 || placementTestGrade > 100) {
			throw new IllegalArgumentException("Grades must be between 0 and 100.");
		}

		S_Node curr = head;
		if (curr == null) {
			return false;
		}

		do {
			if (curr.getStudent().getSid() == id) {
				curr.getStudent().setName(name);
				curr.getStudent().setT_grade(tawjihiGrade);
				curr.getStudent().setPt_grade(placementTestGrade);
				curr.getStudent().setSt_major(major);
				return true;
			}
			curr = curr.getNext();
		} while (curr != head);

		return false;
	}

	public Student search(int id) {
		if (head == null) {
			return null;
		}

		S_Node curr = head;
		do {
			if (curr.getStudent().getSid() == id) {
				return curr.getStudent();
			}
			curr = curr.getNext();
		} while (curr != head);

		return null;
	}

	public S_Node getHead() {
		return head;
	}

}
