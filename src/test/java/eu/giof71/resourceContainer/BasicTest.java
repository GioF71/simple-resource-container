package eu.giof71.resourceContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import eu.giof71.resourceContainer.simple.SimpleResourceContainer;
import eu.giof71.resourceContainer.structure.Pair;

public class BasicTest {

	class AbsType{

		private final int a;
		private final String b;

		public AbsType() {
			this.a = new Random().nextInt(1000);
			this.b = UUID.randomUUID().toString();
		}

		int getA() {
			return a;
		}

		String getB() {
			return b;
		}
	}

	class Type01 extends AbsType {

		public Type01() {
			super();
		}
		
	}

	class Type02 extends AbsType {

		public Type02() {
			super();
		}
		
	}

	class Type03 extends AbsType {

		public Type03() {
			super();
		}
		
	}
	
	private <T> boolean contains(List<Pair<String, T>> listOfPair, Object item) {
		for (Pair<String, T> current : listOfPair) {
			if (current.getSecond().equals(item)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void insertAndVerify() {
		final SimpleResourceContainer rc = new SimpleResourceContainer();
		final String name01 = UUID.randomUUID().toString();
		final String name02 = UUID.randomUUID().toString();

		Type01 item1 = rc.put(new Type01(), name01, Type01.class);
		Assert.assertEquals(rc.size(), 1);
		Assert.assertEquals(rc.sizeOf(Type01.class), 1);
		Assert.assertEquals(rc.sizeOf(name01), 1);
		Assert.assertTrue(rc.getList(name01).contains(item1));
		Assert.assertTrue(contains(rc.getList(Type01.class), item1));

		rc.put(new Type01(), name02, Type01.class);
		Assert.assertEquals(rc.size(), 2);
		Assert.assertEquals(rc.sizeOf(Type01.class), 2);
		Assert.assertEquals(rc.sizeOf(name01), 1);
		Assert.assertEquals(rc.sizeOf(name02), 1);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(name01).size(), 1);
		Assert.assertEquals(rc.getList(name02).size(), 1);

		rc.put(new Type02(), name02, Type02.class);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(Type02.class).size(), 1);
		Assert.assertEquals(rc.getList(name01).size(), 1);
		Assert.assertEquals(rc.getList(name02).size(), 2);
	}

	@Test
	public void removeOne() {
		final SimpleResourceContainer c = new SimpleResourceContainer();
		final Type01 item = c.put(new Type01(), "1", Type01.class);
		final Type01 removed = c.remove("1", Type01.class);

		Assert.assertSame(item, removed);
	}

	@Test
	public void insertOneReplaceOne() {
		final SimpleResourceContainer c = new SimpleResourceContainer();
		final String resourceName = UUID.randomUUID().toString();
		c.put(new Type01(), resourceName, Type01.class);

		final Type01 newItem = new Type01();
		c.put(newItem, resourceName, Type01.class);

		final Type01 checkReplaced = c.get(resourceName, Type01.class);
		Assert.assertSame(newItem, checkReplaced);

		Assert.assertEquals(1, c.size());
		Assert.assertEquals(1, c.sizeOf(Type01.class));
		Assert.assertEquals(1, c.sizeOf(resourceName));
	}
	
	private <T> T insert(
					T o, 
					String resourceName, 
					Class<T> resourceType, 
					SimpleResourceContainer c, 
					List<Pair<Pair<String, Class<?>>, Object>> checkList) {
		T putResult = c.put(o, resourceName, resourceType);
		checkList.add(Pair.valueOf(Pair.valueOf(resourceName, resourceType), o));
		return putResult;
	}
	
	@Test
	public void insertMultiReplaceOne() {
		final SimpleResourceContainer c = new SimpleResourceContainer();
		final String resourceName01 = UUID.randomUUID().toString();
		
		List<Pair<Pair<String, Class<?>>, Object>> checkList = new ArrayList<>();
		
		insert(new Type01(), resourceName01, Type01.class, c, checkList);
		insert(new Type03(), resourceName01, Type03.class, c, checkList);
		insert(new Type02(), resourceName01, Type02.class, c, checkList);

		final String resourceName02 = UUID.randomUUID().toString();
		
		insert(new Type03(), resourceName02, Type03.class, c, checkList);
		Type01 toBeUpdated = c.put(new Type01(), resourceName02, Type01.class);
		insert(new Type02(), resourceName02, Type02.class, c, checkList);
		
		Type01 updated = c.put(new Type01(), resourceName02, Type01.class);
		Type01 retrieved = c.get(resourceName02, Type01.class);
		
		Assert.assertSame(updated, retrieved);
		Assert.assertNotSame(toBeUpdated, updated);
		
		for (Pair<Pair<String, Class<?>>, Object> checkListItem : checkList) {
			String resName = checkListItem.getFirst().getFirst();
			Class<?> resType = checkListItem.getFirst().getSecond();
			Object o = checkListItem.getSecond();
			Assert.assertSame(o, c.get(resName, resType));
			
			Assert.assertTrue(c.getList(resName).contains(o));
			Assert.assertTrue(contains(c.getList(resType), o));
		}
	}
}
