package eu.giof71.resourceContainer;

import java.util.List;

public final class UnmodifiableResourceContainer<ResourceName> 
	implements ResourceContainer<ResourceName> {

	private static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException(
			String.format("Cannot modify a %s", UnmodifiableResourceContainer.class.getSimpleName()));
	private final ResourceContainer<ResourceName> c;
	
	UnmodifiableResourceContainer(ResourceContainer<ResourceName> container) {
		this.c = container;
	}
	
	private static final UnsupportedOperationException unsupported() {
		return UNSUPPORTED_OPERATION_EXCEPTION;
	}
	
	@Override
	public void clear() {
		throw unsupported();
	}

	@Override
	public int size() {
		return c.size();
	}

	@Override
	public int sizeOf(ResourceName resourceName) {
		return c.sizeOf(resourceName);
	}

	@Override
	public int sizeOf(Class<?> resourceType) {
		return c.sizeOf(resourceType);
	}

	@Override
	public boolean contains(ResourceName resourceName, Class<?> resourceType) {
		return c.contains(resourceName, resourceType);
	}

	@Override
	public Object put(Object resource, ResourceName name) {
		throw unsupported();
	}

	@Override
	public <T> T put(T resource, Class<? extends T> clazz) {
		throw unsupported();
	}

	@Override
	public <T> T put(T resource, ResourceName resourceName, Class<T> resourceType) {
		throw unsupported();
	}

	@Override
	public <T> T get(ResourceName resourceName, Class<T> resourceType) {
		return c.get(resourceName, resourceType);
	}

	@Override
	public <T> T get(Class<T> resourceType) {
		return c.get(resourceType);
	}

	@Override
	public Object get(ResourceName resourceName) {
		return c.get(resourceName);
	}

	@Override
	public <T> List<T> getList(Class<T> resourceType) {
		return c.getList(resourceType);
	}

	@Override
	public List<Object> getList(ResourceName resourceName) {
		return c.getList(resourceName);
	}

}
