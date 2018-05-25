package eu.giof71.resourceContainer;

import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import eu.giof71.resourceContainer.simple.SimpleResourceContainer;

public class BasicTest {

	class Type01 {

		private final int a;
		private final String b;

		public Type01() {
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

	class Type02 {

		private final int a;
		private final String b;

		public Type02() {
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

	@Test
	public void insertAndVerify() {
		final SimpleResourceContainer rc = new SimpleResourceContainer();
		final String name01 = UUID.randomUUID().toString();
		final String name02 = UUID.randomUUID().toString();

		rc.put(new Type01(), name01, Type01.class);
		Assert.assertEquals(rc.size(), 1);
		Assert.assertEquals(rc.sizeOf(Type01.class), 1);
		Assert.assertEquals(rc.sizeOf(name01), 1);

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
	public void replaceOne() {
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
}
