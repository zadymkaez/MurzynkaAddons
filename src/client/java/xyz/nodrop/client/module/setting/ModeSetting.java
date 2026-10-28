package xyz.nodrop.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ModeSetting extends Setting<String> {
	private final String[] modes;

	/** Pierwszy podany tryb jest domyslny. */
	public ModeSetting(String name, String... modes) {
		super(name, modes[0]);
		this.modes = modes;
	}

	public String[] getModes() { return modes; }
	public boolean is(String mode) { return value.equalsIgnoreCase(mode); }

	public void cycle(boolean forward) {
		int idx = 0;
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].equals(value)) { idx = i; break; }
		}
		idx = (idx + (forward ? 1 : -1) + modes.length) % modes.length;
		value = modes[idx];
	}

	@Override
	public void set(String v) {
		for (String m : modes) {
			if (m.equalsIgnoreCase(v)) { value = m; return; }
		}
	}

	@Override
	public JsonElement toJson() { return new JsonPrimitive(value); }

	@Override
	public void fromJson(JsonElement json) { set(json.getAsString()); }
}
