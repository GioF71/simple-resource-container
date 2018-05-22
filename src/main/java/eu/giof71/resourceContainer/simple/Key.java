package eu.giof71.resourceContainer.simple;

final class Key<T> {
	
	private final String name;
	private final Class<T> resourceType;

	static <U> Key<U> valueOf(String name, Class<U> resourceType) {
		return new Key<>(name, resourceType);
	}

	private Key(String name, Class<T> resourceType) {
		this.name = name;
		this.resourceType = resourceType;
	}

	public final String getName() {
		return name;
	}

	public final Class<T> getResourceType() {
		return resourceType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((resourceType == null) ? 0 : resourceType.getCanonicalName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key<?> other = (Key<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resourceType == null) {
			if (other.resourceType != null)
				return false;
		} else if (!resourceType.equals(other.resourceType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Key [name=").append(name).append(", resourceType=").append(resourceType).append("]");
		return builder.toString();
	}

}
