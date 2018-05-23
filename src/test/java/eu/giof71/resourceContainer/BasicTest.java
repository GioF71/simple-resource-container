package eu.giof71.resourceContainer;

import org.junit.Assert;
import org.junit.Test;

import eu.giof71.resourceContainer.simple.SimpleResourceContainer;

public class BasicTest {

	private static final String RES_NAME_01 = "res.01";
	private static final String RES_NAME_02 = "res.02";

	class Type01 {
		
		private final int a;
		private final String b;
		
		public Type01(int a, String b) {
			this.a = a;
			this.b = b;
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
		
		public Type02(int a, String b) {
			this.a = a;
			this.b = b;
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
		ResourceContainer<String> rc = new SimpleResourceContainer<>();
		rc.put(new Type01(1, "Res01-1"), RES_NAME_01, Type01.class);
		Assert.assertEquals(rc.size(), 1);
		Assert.assertEquals(rc.sizeOf(Type01.class), 1);
		Assert.assertEquals(rc.sizeOf(RES_NAME_01), 1);
		
		rc.put(new Type01(2, "Res02-1"), RES_NAME_02, Type01.class);
		Assert.assertEquals(rc.size(), 2);
		Assert.assertEquals(rc.sizeOf(Type01.class), 2);
		Assert.assertEquals(rc.sizeOf(RES_NAME_01), 1);
		Assert.assertEquals(rc.sizeOf(RES_NAME_02), 1);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(RES_NAME_01).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_02).size(), 1);
		
		rc.put(new Type02(2, "Res02-2"), RES_NAME_02, Type02.class);

		Assert.assertEquals(rc.getList(Type01.class).size(), 2);
		Assert.assertEquals(rc.getList(Type02.class).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_01).size(), 1);
		Assert.assertEquals(rc.getList(RES_NAME_02).size(), 2);
	}
	
}
