package xyz.nodrop.client.module;

public enum Category {
	MOVEMENT("Movement"),
	RENDER("Render"),
	MISC("Misc"),
	CLIENT("Client");

	public final String displayName;

	Category(String displayName) {
		this.displayName = displayName;
	}
}
