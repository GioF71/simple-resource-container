package eu.giof71.resourceContainer.structure;

public final class Pair<F, S> {
	
	private final F first;
	private final S second;
	
	public static <FT, ST> Pair<FT, ST> valueOf(FT first, ST second) {
		return new Pair<>(first, second);
	}
	
	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
}

