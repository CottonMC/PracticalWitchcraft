package io.github.cottonmc.witchcraft.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.cottonmc.repackage.blue.endless.jankson.annotation.Nullable;
import io.github.cottonmc.witchcraft.spell.ModifierPattern;
import io.github.cottonmc.witchcraft.spell.Spell;
import io.github.cottonmc.witchcraft.spell.SpellPatterns;
import io.github.cottonmc.witchcraft.spell.TargeterPattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.TextureCache;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpellTextureCache {

	@Environment(EnvType.CLIENT)
	static class Entry {
		public long lastRequestTimeMillis;
		public Identifier filename;

		private Entry() {
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Manager {
		private final Map<String, Entry> cacheMap = Maps.newLinkedHashMap();
		private final Identifier filename;
		private final String baseDir;
		private final String id;

		public Manager(String id, Identifier filename, String baseDir) {
			this.id = id;
			this.filename = filename;
			this.baseDir = baseDir;
		}

		@Nullable
		public Identifier get(String prefix, Spell spell) {
			if (prefix.isEmpty()) {
				return null;
			} else if (spell != null) {
				prefix = this.id + prefix;
				Entry entry = this.cacheMap.get(prefix);
				if (entry == null) {
					if (this.cacheMap.size() >= 256 && !this.removeOldEntries()) {
						return TextureCache.DEFAULT_BANNER;
					}

					List<TargeterPattern> targeters = spell.getTargeters();
					List<ModifierPattern> modifiers = spell.getModifiers();
					List<String> entries = Lists.newArrayList();

					entries.add(this.baseDir + SpellPatterns.getActionId(spell.getAction()).toString().replace(':', '-') + ".png");

					for (TargeterPattern targeter : targeters) {
						entries.add(this.baseDir + SpellPatterns.getTargeterId(targeter).toString().replace(':', '-') + ".png");
					}

					for (ModifierPattern modifier : modifiers) {
						entries.add(this.baseDir + SpellPatterns.getModifierId(modifier).toString().replace(':', '-') + ".png");
					}

					entry = new Entry();
					entry.filename = new Identifier(prefix);
					MinecraftClient.getInstance().getTextureManager().registerTexture(entry.filename, new SpellTexture(this.filename, entries));
					this.cacheMap.put(prefix, entry);
				}

				entry.lastRequestTimeMillis = SystemUtil.getMeasuringTimeMs();
				return entry.filename;
			} else {
				return MissingSprite.getMissingSpriteId();
			}
		}

		private boolean removeOldEntries() {
			long millis = SystemUtil.getMeasuringTimeMs();
			Iterator itr = this.cacheMap.keySet().iterator();

			Entry entry;
			do {
				if (!itr.hasNext()) {
					return this.cacheMap.size() < 256;
				}

				String name = (String)itr.next();
				entry = this.cacheMap.get(name);
			} while(millis - entry.lastRequestTimeMillis <= 5000L);

			MinecraftClient.getInstance().getTextureManager().destroyTexture(entry.filename);
			itr.remove();
			return true;
		}
	}
}
