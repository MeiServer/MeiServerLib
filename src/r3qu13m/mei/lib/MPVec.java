package r3qu13m.mei.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.internal.ws.RealWebSocket.Streams;
import r3qu13m.mei.lib.structure.DistributeFile;
import r3qu13m.mei.lib.structure.Mod;
import r3qu13m.mei.lib.structure.ModPack;

public class MPVec {
	private Map<UUID, OperationType> packDiff;

	public MPVec(ModPack pack) {
		if (pack == null) {
			throw new RuntimeException("`pack` must not null");
		}
		List<DistributeFile> files = pack.getFiles();
		this.packDiff = new HashMap<>();

		files.forEach(x -> {
			packDiff.put(x.getID(), OperationType.ADD);
		});
	}

	public MPVec(ModPack from, ModPack to) {
		if (to == null) {
			throw new RuntimeException("`to` must not null");
		}

		List<DistributeFile> toFiles = to.getFiles();
		this.packDiff = new HashMap<>();

		if (from == null) {
			toFiles.forEach(x -> {
				packDiff.put(x.getID(), OperationType.ADD);
			});
		} else {
			List<DistributeFile> fromFiles = from.getFiles();
			List<DistributeFile> targetFiles = new ArrayList<>();
			Stream.concat(toFiles.stream(), fromFiles.stream().filter(x -> !toFiles.contains(x))).forEach(x -> {
				UUID id = x.getID();
				boolean a = fromFiles.contains(x);
				boolean b = toFiles.contains(x);
				OperationType type = OperationType.IDENTITY;
				if (!a & b) {
					type = OperationType.ADD;
				} else if (a & !b) {
					type = OperationType.DELETE;
				}
				packDiff.put(id, type);
			});
		}
	}

	private MPVec(final Map<UUID, OperationType> map) {
		this.packDiff = map;
	}

	public OperationType getType(final UUID file) {
		if (this.packDiff.containsKey(file)) {
			return this.packDiff.get(file);
		}
		return OperationType.IDENTITY;
	}

	public MPVec computeDifference(MPVec left, MPVec right) {
		Set<UUID> files = left.packDiff.keySet();
		files.addAll(right.packDiff.keySet());
		files.forEach(x -> {
			packDiff.put(x, left.getType(x).add(right.getType(x)));
		});
		return new MPVec(packDiff);
	}
}
