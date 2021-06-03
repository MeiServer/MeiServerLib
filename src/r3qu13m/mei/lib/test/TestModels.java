package r3qu13m.mei.lib.test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.Test;

import junit.framework.TestCase;
import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.structure.DataType;
import r3qu13m.mei.lib.structure.DistributeFile;
import r3qu13m.mei.lib.structure.MeiPlayer;

public class TestModels extends TestCase {
	private static void setUUID(Class<?> cls, Object obj, String fieldName, UUID id) {
		try {
			Field f = cls.getDeclaredField(fieldName);
			if (f != null) {
				f.setAccessible(true);
				f.set(obj, id);
			}
		} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testDataType() {
		final File dotMC = new File("dot_minecraft_dir");
		TestCase.assertEquals(DataType.CONFIG.getDestDir(dotMC).get(), new File(dotMC, "config"));
		TestCase.assertEquals(DataType.MOD.getDestDir(dotMC).get(), new File(dotMC, "mods"));
		TestCase.assertEquals(DataType.COREMOD.getDestDir(dotMC).get(), new File(dotMC, "coremods"));
		TestCase.assertFalse(DataType.JAR.getDestDir(dotMC).isPresent());

		for (DataType v : DataType.values()) {
			TestCase.assertEquals(DiscordSerializable.unserialize(DiscordSerializable.serialize(v), DataType.class), v);
		}
	}

	@Test
	public void testMeiPlayer() {
		final UUID id = UUID.fromString("7c1b94c8-9b23-480f-aa2b-425fe469254d");
		final MeiPlayer player = new MeiPlayer("xor_zt", "277338174831984641");
		TestModels.setUUID(MeiPlayer.class, player, "id", id);

		TestCase.assertEquals(player.getID(), id);
		TestCase.assertEquals(player.getName(), "xor_zt");
		TestCase.assertEquals(player.getDiscordID(), "277338174831984641");

		TestCase.assertEquals(DiscordSerializable.unserialize(DiscordSerializable.serialize(player), MeiPlayer.class),
				player);

		final String serialized = "ACQ3YzFiOTRjOC05YjIzLTQ4MGYtYWEyYi00MjVmZTQ2OTI1NGQABnhvcl96dAASMjc3MzM4MTc0ODMxOTg0NjQx";
		TestCase.assertEquals(DiscordSerializable.serialize(player), serialized);
		TestCase.assertEquals(DiscordSerializable.unserialize(serialized, MeiPlayer.class), player);
	}

	@Test
	public void testDistributeFile() {
		final UUID id = UUID.fromString("7c1b94c8-9b23-480f-aa2b-425fe469254d");
		DistributeFile df = new DistributeFile(DataType.MOD, "EE2_for_147_v0.9.zip",
				"7d0758f0ede7edb7d23a014405891c5b09e77615", "http://example.com/mods/EE2_for_147_v0.9.zip");
		TestModels.setUUID(DistributeFile.class, df, "id", id);

		TestCase.assertEquals(df.getID(), id);
		TestCase.assertEquals(df.getType(), DataType.MOD);
		TestCase.assertEquals(df.getName(), "EE2_for_147_v0.9.zip");
		TestCase.assertEquals(df.getHash(), "7d0758f0ede7edb7d23a014405891c5b09e77615");
		TestCase.assertEquals(df.getURL().toString(), "http://example.com/mods/EE2_for_147_v0.9.zip");

		TestCase.assertEquals(DiscordSerializable.unserialize(DiscordSerializable.serialize(df), DistributeFile.class),
				df);
	}
}
