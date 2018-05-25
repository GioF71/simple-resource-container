package eu.giof71.resourceContainer.absSimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import eu.giof71.resourceContainer.CannotFindUniqueResource;
import eu.giof71.resourceContainer.ResourceContainer;

public abstract class AbsResourceContainer<ResourceName> implements ResourceContainer<ResourceName> {

	private final Map<Key<ResourceName, ?>, Object> map = new HashMap<>();
	private final Map<ResourceName, List<Pair<Key<ResourceName, ?>, Object>>> byName = new HashMap<>();
	private final Map<Class<?>, List<Pair<Key<ResourceName, ?>, Object>>> byType = new HashMap<>();

	private final Function<Class<?>, ResourceName> nameExtractor;

	protected AbsResourceContainer(Function<Class<?>, ResourceName> nameExtractor) {
		this.nameExtractor = nameExtractor;
	}

	@Override
	public final void clear() {
		map.clear();
		byName.clear();
		byType.clear();
	}

	@Override
	public final int size() {
		return map.size();
	}

	@Override
	public final int sizeOf(ResourceName resourceName) {
		return Optional.ofNullable(byName.get(resourceName)).map(List::size).orElse(0);
	}

	@Override
	public final int sizeOf(Class<?> resourceType) {
		return Optional.ofNullable(byType.get(resourceType)).map(List::size).orElse(0);
	}

	@Override
	public final boolean contains(ResourceName resourceName, Class<?> resourceType) {
		return map.containsKey(Key.valueOf(resourceName, resourceType));
	}

	@Override
	public final <T> T put(T resource, Class<T> resourceType) {
		if (nameExtractor != null) {
			return put(resource, nameExtractor.apply(resourceType), resourceType);
		} else {
			throw new UnsupportedOperationException(
						String.format("Cannot extract resourceName from %s [%s] (extractor not provided)", 
								Class.class.getSimpleName(), 
								resourceType.getSimpleName()));
		}
	}

	@Override
	public final <T> T put(T resource, ResourceName resourceName, Class<T> resourceType) {
		Key<ResourceName, T> key = Key.valueOf(resourceName, resourceType);
		if (!contains(resourceName, resourceType)) {
			map.put(key, resource);
			getOrCreateList(resourceName).add(Pair.valueOf(key, resource));
			getOrCreateList(resourceType).add(Pair.valueOf(key, resource));
		} else {
			remove(resourceName, resourceType);
			return put(resource, resourceName, resourceType);
		}
		return resource;
	}

	private <ListKeyType> List<Pair<Key<ResourceName, ?>, Object>> getOrCreateList(ListKeyType listKey,
			Function<AbsResourceContainer<ResourceName>, Map<ListKeyType, List<Pair<Key<ResourceName, ?>, Object>>>> listGetter) {
		Map<ListKeyType, List<Pair<Key<ResourceName, ?>, Object>>> map = listGetter.apply(this);
		List<Pair<Key<ResourceName, ?>, Object>> list = map.get(listKey);
		if (list == null) {
			list = new ArrayList<>();
			map.put(listKey, list);
		}
		return list;
	}

	private List<Pair<Key<ResourceName, ?>, Object>> getOrCreateList(ResourceName name) {
		return getOrCreateList(name, p -> p.byName);
	}

	private List<Pair<Key<ResourceName, ?>, Object>> getOrCreateList(Class<?> type) {
		return getOrCreateList(type, p -> p.byType);
	}

	@Override
	public final <T> T get(ResourceName resourceName, Class<T> resourceType) {
		Object resource = map.get(Key.valueOf(resourceName, resourceType));
		return resourceType.cast(resource);
	}

	@Override
	public final <T> List<T> getList(Class<T> resourceType) {
		List<Pair<Key<ResourceName, ?>, Object>> list = byType.get(resourceType);
		if (list != null) {
			List<T> itemList = new ArrayList<>();
			for (Pair<Key<ResourceName, ?>, Object> current : list) {
				itemList.add(resourceType.cast(current.getSecond()));
			}
			return Collections.unmodifiableList(itemList);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public final List<Object> getList(ResourceName resourceName) {
		List<Pair<Key<ResourceName, ?>, Object>> list = byName.get(resourceName);
		if (list != null) {
			List<Object> itemList = new ArrayList<>();
			for (Pair<Key<ResourceName, ?>, Object> current : list) {
				itemList.add(current.getSecond());
			}
			return Collections.unmodifiableList(itemList);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public final <T> T get(Class<T> resourceClass) {
		List<Pair<Key<ResourceName, ?>, Object>> list = byType.get(resourceClass);
		if (list != null && list.size() == 1) {
			return resourceClass.cast(list.get(0).getSecond());
		} else if (list.size() > 1) {
			throw new CannotFindUniqueResource(
					String.format("More than one item found for resourceType [%s]", resourceClass.getSimpleName()));
		} else {
			return null;
		}
	}

	@Override
	public final Object get(ResourceName resourceName) {
		List<Pair<Key<ResourceName, ?>, Object>> list = byName.get(resourceName);
		if (list != null && list.size() == 1) {
			return list.get(0).getSecond();
		} else if (list.size() > 1) {
			throw new CannotFindUniqueResource(
					String.format("More than one item found for resourceName [%s]", resourceName));
		} else {
			return null;
		}
	}
	
	private int indexOf(List<Pair<Key<ResourceName, ?>, Object>> list, ResourceName resourceName) {
		int index = -1;
		for (int i = 0; index == -1 && i < list.size(); ++i) {
			Pair<Key<ResourceName, ?>, Object> current = list.get(i);
			if (current.getFirst().getName().equals(resourceName)) {
				index = i;
			}
		}
		return index;
	}

	private int indexOf(List<Pair<Key<ResourceName, ?>, Object>> list, Class<?> resourceType) {
		int index = -1;
		for (int i = 0; index == -1 && i < list.size(); ++i) {
			Pair<Key<ResourceName, ?>, Object> current = list.get(i);
			if (current.getFirst().getResourceType().equals(resourceType)) {
				index = i;
			}
		}
		return index;
	}

	@Override
	public final <T> T remove(ResourceName resourceName, Class<T> resourceType) {
		Key<ResourceName, T> key = Key.valueOf(resourceName, resourceType);
		Object item = map.get(key);
		if (item != null) {
			List<Pair<Key<ResourceName, ?>, Object>> listOfNames = byType.get(resourceType);
			int rmIndexFromListOfNames = indexOf(listOfNames, resourceName);
			List<Pair<Key<ResourceName, ?>, Object>> listOfTypes = byName.get(resourceName);
			int rmIndexFromListOfTypes = indexOf(listOfTypes, resourceType);
			if (rmIndexFromListOfNames != -1 && rmIndexFromListOfTypes != -1) {
				// ok to remove
				listOfNames.remove(rmIndexFromListOfNames);
				listOfTypes.remove(rmIndexFromListOfTypes);
				map.remove(key);
			} else {
				// corruption - cannot remove
				throw new RuntimeException("Corrupted data structures");
			}
		}
		return resourceType.cast(item);
	}
}
