package common;

public interface Component {

	void fire();
	Component getNext();
	Component setNext(Component next);
}
