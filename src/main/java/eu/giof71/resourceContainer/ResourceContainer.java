package eu.giof71.resourceContainer;

import java.util.List;

public interface ResourceContainer {
	void clear();
	int size();
	int sizeOf(String resourceName);
	int sizeOf(Class<?> resourceType);
	boolean contains(String resourceName, Class<?> resourceType);
	Object put(Object resource, String name);
	<T> T put(T resource, Class<? extends T> clazz);
	<T> T put(T resource, String resourceName, Class<T> resourceType);
	<T> T get(String resourceName, Class<T> resourceType);
	<T> T get(Class<T> resourceType);
	Object get(String resourceName);
	
	<T> List<T> getList(Class<T> resourceType);
	List<Object> getList(String resourceName);
}
