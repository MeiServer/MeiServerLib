package r3qu13m.mei.lib.test;

import java.io.File;
import java.util.UUID;

import org.junit.Test;

import junit.framework.TestCase;
import r3qu13m.mei.lib.DiscordSerializable;
import r3qu13m.mei.lib.structure.DataType;
import r3qu13m.mei.lib.structure.MeiPlayer;

public class TestModels extends TestCase {
	@Test
	public void testDataType() {
		final File dotMC = new File("dot_minecraft_dir");
		TestCase.assertEquals(DataType.CONFIG.getDestDir(dotMC).get(), new File(dotMC, "config"));
		TestCase.assertEquals(DataType.MOD.getDestDir(dotMC).get(), new File(dotMC, "mods"));
		TestCase.assertEquals(DataType.COREMOD.getDestDir(dotMC).get(), new File(dotMC, "coremods"));
		TestCase.assertFalse(DataType.JAR.getDestDir(dotMC).isPresent());

		TestCase.assertEquals(DiscordSerializable.unserialize(DiscordSerializable.serialize(DataType.CONFIG)),
				DataType.CONFIG);
	}

	@Test
	public void testMeiPlayer() {
		final UUID id = UUID.fromString("7c1b94c8-9b23-480f-aa2b-425fe469254d");
		final String serialized = "ACQ3YzFiOTRjOC05YjIzLTQ4MGYtYWEyYi00MjVmZTQ2OTI1NGQABnhvcl96dAASMjc3MzM4MTc0ODMxOTg0NjQx";
		final MeiPlayer player = new MeiPlayer(id, "xor_zt", "277338174831984641");
		TestCase.assertEquals(DiscordSerializable.serialize(player), serialized);
		TestCase.assertEquals(DiscordSerializable.unserialize(serialized, MeiPlayer.class), player);
		TestCase.assertEquals(player.getID(), id);
		TestCase.assertEquals(player.getName(), "xor_zt");
		TestCase.assertEquals(player.getDiscordID(), "277338174831984641");
	}
}
