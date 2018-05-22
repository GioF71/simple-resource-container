package eu.giof71.resourceContainer.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import eu.giof71.resourceContainer.CannotFindUniqueResource;
import eu.giof71.resourceContainer.ResourceContainer;

public final class SimpleResourceContainer implements ResourceContainer {
	
	private Map<Key, Object> map = new HashMap<>();
	private Map<String, List<Pair<Class<?>, Object>>> byName = new HashMap<>();
	private Map<Class<?>, List<Pair<String, Object>>> byType = new HashMap<>();

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}

	@Override
	public int sizeOf(String resourceName) {
		return Optional.ofNullable(byName.get(resourceName)).map(List::size).orElse(0);
	}

	@Override
	public int sizeOf(Class<?> resourceType) {
		return Optional.ofNullable(byType.get(resourceType)).map(List::size).orElse(0);
	}

	public boolean contains(String resourceName, Class<?> resourceType) {
		return map.containsKey(Key.valueOf(resourceName, resourceType));
	}

	public Object put(Object resource, String name) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

	public <T> T put(T resource, Class<? extends T> clazz) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

	public <T> T put(T resource, String resourceName, Class<T> resourceType) {
		Key key = Key.valueOf(resourceName, resourceType);
		if (!contains(resourceName, resourceType)) {
			map.put(key, resource);
			getOrCreateList(resourceName).add(Pair.valueOf(resourceType, resource));
			getOrCreateList(resourceType).add(Pair.valueOf(resourceName, resource));
		} else {
			// update is not supported yet
			throw new UnsupportedOperationException("Update not supported"); 
		}
		return resource;
	}

	private <ListKeyType, FirstType> 
	List<Pair<FirstType, Object>> getOrCreateList(
			ListKeyType listKey, 
			Function<SimpleResourceContainer, Map<ListKeyType, List<Pair<FirstType, Object>>>> listGetter) {
		Map<ListKeyType, List<Pair<FirstType, Object>>> map = listGetter.apply(this);
		List<Pair<FirstType, Object>> list = map.get(listKey);
		if (list == null) {
			list = new ArrayList<>();
			map.put(listKey, list);
		}
		return list;
	}

	private List<Pair<Class<?>, Object>> getOrCreateList(String name) {
		return getOrCreateList(name, p -> p.byName);
	}

	private List<Pair<String, Object>> getOrCreateList(Class<?> type) {
		return getOrCreateList(type, p -> p.byType);
	}

	public <T> T get(String resourceName, Class<T> resourceType) {
		Object resource = map.get(Key.valueOf(resourceName, resourceType));
		return resourceType.cast(resource);
	}

	@Override
	public <T> List<T> getList(Class<T> resourceType) {
		List<Pair<String, Object>> list = byType.get(resourceType);
		if (list != null) {
			List<T> itemList = new ArrayList<>();
			for (Pair<String, Object> current : list) {
				itemList.add(resourceType.cast(current.getSecond()));
			}
			return itemList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Object> getList(String resourceName) {
		List<Pair<Class<?>, Object>> list = byName.get(resourceName);
		if (list != null) {
			List<Object> itemList = new ArrayList<>();
			for (Pair<Class<?>, Object> current : list) {
				itemList.add(current.getSecond());
			}
			return itemList;
		} else {
			return Collections.emptyList();
		}
	}

	public <T> T get(Class<T> resourceClass) {
		List<Pair<String, Object>> list = byType.get(resourceClass);
		if (list != null && list.size() == 1) {
			return resourceClass.cast(list.get(0).getSecond());
		} else if (list.size() > 1) {
			throw new CannotFindUniqueResource(
					String.format(
							"More than one item found for resourceType [%s]", 
							resourceClass.getSimpleName()));
		} else {
			return null;
		}
	}

	@Override
	public Object get(String resourceName) {
		List<Pair<Class<?>, Object>> list = byName.get(resourceName);
		if (list != null && list.size() == 1) {
			return list.get(0).getSecond();
		} else if (list.size() > 1) {
			throw new CannotFindUniqueResource(
					String.format(
							"More than one item found for resourceName [%s]", 
							resourceName));
		} else {
			return null;
		}
	}
}
