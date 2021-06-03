package r3qu13m.mei.lib.test;

import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;
import r3qu13m.mei.lib.structure.DataType;

public class TestModels extends TestCase {
	@Test
	public void testDataType() {
		File dotMC = new File("dot_minecraft_dir");
		TestCase.assertEquals(DataType.CONFIG.getDestDir(dotMC).get(), new File(dotMC, "config"));
		TestCase.assertEquals(DataType.MOD.getDestDir(dotMC).get(), new File(dotMC, "mods"));
		TestCase.assertEquals(DataType.COREMOD.getDestDir(dotMC).get(), new File(dotMC, "coremods"));
		TestCase.assertFalse(DataType.JAR.getDestDir(dotMC).isPresent());
	}
}
