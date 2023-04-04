package kr.service.model.guset;

public enum ProviderType {
	APPLE("APPLE"),
	GOOGLE("GOOGLE");

	private final String name;

	ProviderType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
