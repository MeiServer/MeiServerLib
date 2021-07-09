package r3qu13m.mei.lib;

public enum OperationType {
	ADD, DELETE, IDENTITY

	;
	;

	public OperationType composite(final OperationType other) {
		if (other == IDENTITY) {
			return this;
		}
		if (this == IDENTITY) {
			return other;
		}
		if (this == ADD && other == DELETE || this == DELETE && other == ADD) {
			return IDENTITY;
		}
		return this;
	}

	public OperationType inverse() {
		switch (this) {
		case ADD:
			return DELETE;
		case DELETE:
			return ADD;
		case IDENTITY:
		default:
			return IDENTITY;
		}
	}
}
