package r3qu13m.mei.lib;

import java.io.File;
import java.util.Optional;

public enum DataType {
	CONFIG("config"), MOD("mods"), COREMOD("coremods"), JAR(null);
	;

	private String dir;

	private DataType(final String par1Dir) {
		this.dir = par1Dir;
	}

	public Optional<File> getDestDir(final File basePath) {
		if (this.dir == null) {
			return Optional.empty();
		}
		return Optional.of(new File(basePath, this.dir));
	}
}
