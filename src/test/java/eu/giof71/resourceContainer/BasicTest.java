package eu.giof71.resourceContainer;

import org.junit.Assert;
import org.junit.Test;

import eu.giof71.resourceContainer.simple.SimpleResourceContainer;

public class BasicTest {

	private static final String RES_NAME_01 = "res.01";
	private static final String RES_NAME_02 = "res.02";

	class MyItem {
		private final int a;
		private final String b;
		
		public MyItem(int a, String b) {
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
	public void insertOne() {
		ResourceContainer rc = new SimpleResourceContainer();
		rc.put(new MyItem(1, "Res01-1"), RES_NAME_01, MyItem.class);
		Assert.assertTrue(rc.size() == 1);
		Assert.assertTrue(rc.sizeOf(MyItem.class) == 1);
		Assert.assertTrue(rc.sizeOf(RES_NAME_01) == 1);
		
		rc.put(new MyItem(1, "Res02-1"), RES_NAME_02, MyItem.class);
		Assert.assertTrue(rc.size() == 2);
		Assert.assertTrue(rc.sizeOf(MyItem.class) == 2);
		Assert.assertTrue(rc.sizeOf(RES_NAME_01) == 1);
		Assert.assertTrue(rc.sizeOf(RES_NAME_02) == 1);
	}
	
}
