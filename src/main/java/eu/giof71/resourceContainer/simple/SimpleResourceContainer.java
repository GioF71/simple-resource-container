package eu.giof71.resourceContainer.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import eu.giof71.resourceContainer.ResourceContainer;

public class SimpleResourceContainer implements ResourceContainer {
	
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
	public int sizeOf(String name) {
		return Optional.ofNullable(byName.get(name)).map(List::size).orElse(0);
	}

	@Override
	public int sizeOf(Class<?> type) {
		return Optional.ofNullable(byType.get(type)).map(List::size).orElse(0);
	}

	public boolean contains(String resourceName, Class<?> clazz) {
		return map.containsKey(Key.valueOf(resourceName, clazz));
	}

	public Object put(Object resource, String name) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

	public <T> T put(T resource, Class<? extends T> clazz) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

	public <T> Object put(T resource, String name, Class<? extends T> clazz) {
		Key key = Key.valueOf(name, clazz);
		if (!contains(name, clazz)) {
			map.put(key, resource);
			getOrCreateList(name).add(Pair.valueOf(clazz, resource));
			getOrCreateList(clazz).add(Pair.valueOf(name, resource));
		} else {
			// update is not supported yet
			throw new UnsupportedOperationException("Update not supported"); 
		}
		return resource;
	}

	private <ListKeyType, FirstType> List<Pair<FirstType, Object>> getOrCreate(ListKeyType listKey, Function<SimpleResourceContainer, Map<ListKeyType, List<Pair<FirstType, Object>>>> listGetter) {
		Map<ListKeyType, List<Pair<FirstType, Object>>> map = listGetter.apply(this);
		List<Pair<FirstType, Object>> list = map.get(listKey);
		if (list == null) {
			list = new ArrayList<>();
			map.put(listKey, list);
		}
		return list;
	}


	private List<Pair<Class<?>, Object>> getOrCreateList(String name) {
		return getOrCreate(name, p -> p.byName);
	}

	private List<Pair<String, Object>> getOrCreateList(Class<?> type) {
		return getOrCreate(type, p -> p.byType);
//		List<Pair<String, Object>> list = byType.get(type);
//		if (list == null) {
//			list = new ArrayList<>();
//			byType.put(type, list);
//		}
//		return list;
	}

	public <T> T get(String resourceName, Class<T> clazz) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

	public <T> T get(Class<T> resourceClass) {
		throw new UnsupportedOperationException("incomplete implementation");
	}

}
