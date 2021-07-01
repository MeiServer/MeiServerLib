package r3qu13m.mei.lib;

public enum OperationType {
	ADD, DELETE, IDENTITY
	
	;;
	
	public OperationType add(final OperationType other) {
		if (other == IDENTITY) {
			return this;
		}
		if (this == IDENTITY) {
			return other;
		}
		if ((this == ADD && other == DELETE) || (this == DELETE && other == ADD)) {
			return IDENTITY;
		}
		return this;
	}
}
