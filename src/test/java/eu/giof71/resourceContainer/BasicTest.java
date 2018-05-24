package eu.giof71.resourceContainer;

import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import eu.giof71.resourceContainer.simple.SimpleResourceContainer;

public class BasicTest {

	private static final String RES_NAME_01 = "res.01";
	private static final String RES_NAME_02 = "res.02";

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
		SimpleResourceContainer rc = new SimpleResourceContainer();
		rc.put(new Type01(), RES_NAME_01, Type01.class);
		Assert.assertEquals(rc.size(), 1);
		Assert.assertEquals(rc.sizeOf(Type01.class), 1);
		Assert.assertEquals(rc.sizeOf(RES_NAME_01), 1);
		
		rc.put(new Type01(), RES_NAME_02, Type01.class);
		Assert.assertEquals(rc.size(), 2);
		Assert.assertEquals(rc.sizeOf(Type01.class), 2);
		Assert.assertEquals(rc.sizeOf(RES_NAME_01), 1);
		Assert.assertEquals(rc.sizeOf(RES_NAME_02), 1);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(RES_NAME_01).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_02).size(), 1);
		
		rc.put(new Type02(), RES_NAME_02, Type02.class);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(Type02.class).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_01).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_02).size(), 2);
	}
}
