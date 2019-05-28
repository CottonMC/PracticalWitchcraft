package io.github.cottonmc.witchcraft.deity;

public interface DeityReload {
	/**
	 * Deities' opinions of individual players can change based on various stats.
	 * Since some stats are based on data-driven info, this is applied on resource reload.
	 * Call deity.addFavors() and deity.addDisfavors() to add in stats.
	 * @param deity The deity this reload is being performed for.
	 */
	void reload(Deity deity);
}
