package eu.giof71.resourceContainer;

public interface ResourceContainer {
	void clear();
	int size();
	int sizeOf(String name);
	int sizeOf(Class<?> type);
	boolean contains(String resourceName, Class<?> clazz);
	Object put(Object resource, String name);
	<T> T put(T resource, Class<? extends T> clazz);
	<T> Object put(T resource, String name, Class<? extends T> clazz);
	<T> T get(String resourceName, Class<T> clazz);
	<T> T get(Class<T> resourceClass);
}
