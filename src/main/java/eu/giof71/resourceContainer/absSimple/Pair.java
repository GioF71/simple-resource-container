package eu.giof71.resourceContainer.absSimple;

final class Pair<F, S> {
	
	private final F first;
	private final S second;
	
	static <FT, ST> Pair<FT, ST> valueOf(FT first, ST second) {
		return new Pair<>(first, second);
	}
	
	F getFirst() {
		return first;
	}

	S getSecond() {
		return second;
	}

	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
}

