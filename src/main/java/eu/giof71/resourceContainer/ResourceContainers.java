package eu.giof71.resourceContainer;

public final class ResourceContainers {
	
	private ResourceContainers() {
	}
	
	public static <N> 
	UnmodifiableResourceContainer<N> unmodifiableResourceContainer(ResourceContainer<N> resourceContainer) {
		return new UnmodifiableResourceContainer<>(resourceContainer);
	}
}
