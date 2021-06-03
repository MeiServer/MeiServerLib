package r3qu13m.mei.lib.test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import junit.framework.TestCase;
import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.MeiServerLib;
import r3qu13m.mei.lib.structure.DataType;
import r3qu13m.mei.lib.structure.DistributeFile;
import r3qu13m.mei.lib.structure.MeiPlayer;
import r3qu13m.mei.lib.structure.Mod;
import r3qu13m.mei.lib.structure.ModPack;

public class TestModels extends TestCase {
	private static void setUUID(final Class<?> cls, final Object obj, final String fieldName, final UUID id) {
		try {
			final Field f = cls.getDeclaredField(fieldName);
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

		for (final DataType v : DataType.values()) {
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
		final DistributeFile df = new DistributeFile(DataType.MOD, "EE2_for_147_v0.9.zip",
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

	@Test
	public void testMod() {
		final UUID id = UUID.fromString("7c1b94c8-9b23-480f-aa2b-425fe469254d");
		final DistributeFile file1 = new DistributeFile(DataType.MOD, "EE2", "hash", "http://example.com/hogefuga.zip");
		final DistributeFile file2 = new DistributeFile(DataType.MOD, "RP2_1", "hash",
				"http://example.com/hogefuga.zip");
		final DistributeFile file3 = new DistributeFile(DataType.MOD, "RP2_2", "hash",
				"http://example.com/hogefuga.zip");

		final Map<UUID, DistributeFile> dfMap = new HashMap<>();
		dfMap.put(file1.getID(), file1);
		dfMap.put(file2.getID(), file2);
		dfMap.put(file3.getID(), file3);
		MeiServerLib.instance().setDistributeFileMap(dfMap::get);

		final Mod ee2 = new Mod("EE2", "v0.9", Arrays.asList(file1));
		TestModels.setUUID(Mod.class, ee2, "id", id);

		TestCase.assertEquals(ee2.getID(), id);
		TestCase.assertEquals(ee2.getModName(), "EE2");
		TestCase.assertEquals(ee2.getVersion(), "v0.9");
		TestCase.assertEquals(ee2.getFiles(), Arrays.asList(file1));

		final Mod rp2 = new Mod("RP2", "v1", new ArrayList<>());
		TestCase.assertEquals(rp2.getFiles(), Arrays.asList());

		rp2.addFile(file2);
		rp2.addFile(file3);
		TestCase.assertEquals(rp2.getFiles(), Arrays.asList(file2, file3));
		TestCase.assertTrue(rp2.hasFile(file2));
		TestCase.assertTrue(rp2.hasFile(file3));

		rp2.addFile(file3);
		TestCase.assertEquals(rp2.getFiles(), Arrays.asList(file2, file3));

		rp2.removeFile(file3);
		TestCase.assertEquals(rp2.getFiles(), Arrays.asList(file2));
		TestCase.assertFalse(rp2.hasFile(file3));

		rp2.removeFile(file2);
		TestCase.assertEquals(rp2.getFiles(), Arrays.asList());
		TestCase.assertFalse(rp2.hasFile(file2));

		rp2.addFile(file2);
		rp2.addFile(file3);
		TestCase.assertEquals(DiscordSerializable.unserialize(DiscordSerializable.serialize(rp2), Mod.class), rp2);
	}

	@Test
	public void testModPack() {
		final UUID id = UUID.fromString("7c1b94c8-9b23-480f-aa2b-425fe469254d");
		final DistributeFile file1 = new DistributeFile(DataType.MOD, "EE2", "hash", "http://example.com/hogefuga.zip");
		final DistributeFile file2 = new DistributeFile(DataType.MOD, "RP2_1", "hash",
				"http://example.com/hogefuga.zip");
		final DistributeFile file3 = new DistributeFile(DataType.MOD, "RP2_2", "hash",
				"http://example.com/hogefuga.zip");

		final Map<UUID, DistributeFile> dfMap = new HashMap<>();
		dfMap.put(file1.getID(), file1);
		dfMap.put(file2.getID(), file2);
		dfMap.put(file3.getID(), file3);
		MeiServerLib.instance().setDistributeFileMap(dfMap::get);

		final Mod ee2 = new Mod("EE2", "v0.9", Arrays.asList(file1));
		final Mod rp2 = new Mod("RP2", "v1", Arrays.asList(file2, file3));

		final Map<UUID, Mod> modMap = new HashMap<>();
		modMap.put(ee2.getID(), ee2);
		modMap.put(rp2.getID(), rp2);
		MeiServerLib.instance().setModMap(modMap::get);

		final ModPack pack = new ModPack(Arrays.asList(ee2), 1);
		TestModels.setUUID(ModPack.class, pack, "id", id);

		TestCase.assertEquals(pack.getID(), id);
		TestCase.assertEquals(pack.getVersion(), 1);
		TestCase.assertEquals(pack.getMods(), Arrays.asList(ee2));
		TestCase.assertTrue(pack.has(ee2));
		TestCase.assertFalse(pack.has(rp2));

		final ModPack pack2 = pack.with(rp2);
		TestCase.assertEquals(pack2.getVersion(), 2);
		TestCase.assertEquals(pack2.getMods(), Arrays.asList(ee2, rp2));
		TestCase.assertTrue(pack2.has(ee2));
		TestCase.assertTrue(pack2.has(rp2));

		final ModPack pack3 = pack2.without(ee2);
		TestCase.assertEquals(pack3.getVersion(), 3);
		TestCase.assertEquals(pack3.getMods(), Arrays.asList(rp2));
		TestCase.assertFalse(pack3.has(ee2));
		TestCase.assertTrue(pack3.has(rp2));
	}
}
