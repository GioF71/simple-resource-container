package eu.giof71.resourceContainer;

import java.util.List;

public interface ResourceContainer<ResourceName> {
	
	void clear();
	
	int size();
	int sizeOf(ResourceName resourceName);
	int sizeOf(Class<?> resourceType);
	
	boolean contains(ResourceName resourceName, Class<?> resourceType);
	
	<T> T put(T resource, Class<T> resourceType);
	<T> T put(T resource, ResourceName resourceName, Class<T> resourceType);
	
	<T> T get(ResourceName resourceName, Class<T> resourceType);
	<T> T get(Class<T> resourceType);
	Object get(ResourceName resourceName);
	
	<T> T remove(ResourceName resourceName, Class<T> resourceType);
	
	<T> List<T> getList(Class<T> resourceType);
	List<Object> getList(ResourceName resourceName);
}
