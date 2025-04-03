public class MLL {
	private M_Node head;

	public M_Node getHead() {
		return head;
	}

	public void setHead(M_Node head) {
		this.head = head;
	}

	public MLL() {
		head = null;
	}

	public void insert(Major major) {
		M_Node newNode = new M_Node(major);

		if (head == null) {
			head = newNode;
			head.setNext(head);
			head.setPrev(head);
		} else {
			M_Node curr = head;
			M_Node prev = null;

			do {
				if (curr.getMajor().getName().equalsIgnoreCase(major.getName())) {
					throw new IllegalArgumentException("Major already exists.");
				}
				if (curr.getMajor().compareTo(major) > 0) {
					break;
				}
				prev = curr;
				curr = curr.getNext();
			} while (curr != head);

			if (prev == null) {
				newNode.setNext(head);
				newNode.setPrev(head.getPrev());
				head.getPrev().setNext(newNode);
				head.setPrev(newNode);
				head = newNode;
			} else {
				newNode.setNext(curr);
				newNode.setPrev(prev);
				prev.setNext(newNode);
				curr.setPrev(newNode);
			}
		}
	}

	public boolean delete(String name) {
		if (head == null) {
			return false;
		}

		M_Node curr = head;

		do {
			if (curr.getMajor().getName().equalsIgnoreCase(name)) {
				if (curr == head) {
					if (head.getNext() == head) {
						head = null;
					} else {
						head.getPrev().setNext(head.getNext());
						head.getNext().setPrev(head.getPrev());
						head = head.getNext();
					}
				} else {
					curr.getPrev().setNext(curr.getNext());
					curr.getNext().setPrev(curr.getPrev());
				}
				return true;
			}
			curr = curr.getNext();
		} while (curr != head);

		return false;
	}

	public Major search(String name) {
		if (head == null) {
			return null;
		}

		M_Node curr = head;
		do {
			if (curr.getMajor().getName().equalsIgnoreCase(name)) {
				return curr.getMajor();
			}
			curr = curr.getNext();
		} while (curr != head);

		return null;
	}

}
