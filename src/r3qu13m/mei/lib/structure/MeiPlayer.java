package r3qu13m.mei.lib.structure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import r3qu13m.mei.lib.DiscordSerializable;

public class MeiPlayer implements DiscordSerializable {
	private UUID id;
	private String name;
	private String discordId;

	public MeiPlayer(final UUID par1Id, final String par2Name, final String par3DiscordId) {
		this.id = par1Id;
		this.name = par2Name;
		this.discordId = par3DiscordId;
	}

	protected MeiPlayer() {
		this(null, null, null);
	}

	public UUID getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDiscordID() {
		return this.discordId;
	}

	@Override
	public void serialize(final DataOutputStream dos) throws IOException {
		dos.writeUTF(this.id.toString());
		dos.writeUTF(this.name);
		dos.writeUTF(this.discordId);
	}

	@Override
	public void unserialize(final DataInputStream dis) throws IOException {
		this.id = UUID.fromString(dis.readUTF());
		this.name = dis.readUTF();
		this.discordId = dis.readUTF();
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof MeiPlayer)) {
			return false;
		}
		final MeiPlayer player = (MeiPlayer) other;
		return player.getID().equals(this.id) && player.getName().equals(this.name)
				&& player.getDiscordID().equals(this.discordId);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
