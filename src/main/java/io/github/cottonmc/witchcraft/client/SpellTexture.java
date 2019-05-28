package io.github.cottonmc.witchcraft.client;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class SpellTexture extends AbstractTexture {
	private static final Logger LOGGER = LogManager.getLogger();
	private final Identifier filename;
	private final List<String> patternNames;

	public SpellTexture(Identifier filename, List<String> patterns) {
		this.filename = filename;
		this.patternNames = patterns;
	}


	@Override
	public void load(ResourceManager manager) throws IOException {
		//TODO: this can probably be cleaned up a lot since I'm not exactly gonna be doing any color math, also there are WAY too many throwables
		try {
			Resource fileRes = manager.getResource(this.filename);
			Throwable t1 = null;

			try {
				NativeImage fileImage = NativeImage.fromInputStream(fileRes.getInputStream());
				Throwable t2 = null;

				try {
					NativeImage convertedImage = new NativeImage(fileImage.getWidth(), fileImage.getHeight(), false);
					Throwable t3 = null;

					try {
						convertedImage.copyFrom(fileImage);

						for(int i = 0; i < 17 && i < this.patternNames.size(); ++i) {
							String name = this.patternNames.get(i);
							if (name != null) {
								Resource patternRes = manager.getResource(new Identifier(name));
								Throwable t4 = null;

								try {
									NativeImage patternImage = NativeImage.fromInputStream(patternRes.getInputStream());
									Throwable t5 = null;

									try {
										if (patternImage.getWidth() == convertedImage.getWidth() && patternImage.getHeight() == convertedImage.getHeight()) {
											for(int j = 0; j < patternImage.getHeight(); ++j) {
												for(int k = 0; k < patternImage.getWidth(); ++k) {
													int patternColor = patternImage.getPixelRGBA(k, j);
													if ((patternColor & -16777216) != 0) {
														int convertedColor = (patternColor & 255) << 24 & -16777216;
														convertedImage.blendPixel(k, j, convertedColor);
													}
												}
											}
										}
									} catch (Throwable t) {
										t5 = t;
										throw t;
									} finally {
										if (patternImage != null) {
											if (t5 != null) {
												try {
													patternImage.close();
												} catch (Throwable t) {
													t5.addSuppressed(t);
												}
											} else {
												patternImage.close();
											}
										}

									}
								} catch (Throwable t) {
									t4 = t;
									throw t;
								} finally {
									if (patternRes != null) {
										if (t4 != null) {
											try {
												patternRes.close();
											} catch (Throwable t) {
												t4.addSuppressed(t);
											}
										} else {
											patternRes.close();
										}
									}

								}
							}
						}

						TextureUtil.prepareImage(this.getGlId(), convertedImage.getWidth(), convertedImage.getHeight());
						GlStateManager.pixelTransfer(3357, 3.4028235E38F);
						convertedImage.upload(0, 0, 0, false);
						GlStateManager.pixelTransfer(3357, 0.0F);
					} catch (Throwable t) {
						t3 = t;
						throw t;
					} finally {
						if (convertedImage != null) {
							if (t3 != null) {
								try {
									convertedImage.close();
								} catch (Throwable t) {
									t3.addSuppressed(t);
								}
							} else {
								convertedImage.close();
							}
						}

					}
				} catch (Throwable t) {
					t2 = t;
					throw t;
				} finally {
					if (fileImage != null) {
						if (t2 != null) {
							try {
								fileImage.close();
							} catch (Throwable t) {
								t2.addSuppressed(t);
							}
						} else {
							fileImage.close();
						}
					}

				}
			} catch (Throwable t) {
				t1 = t;
				throw t;
			} finally {
				if (fileRes != null) {
					if (t1 != null) {
						try {
							fileRes.close();
						} catch (Throwable t) {
							t1.addSuppressed(t);
						}
					} else {
						fileRes.close();
					}
				}

			}
		} catch (IOException e) {
			LOGGER.error("Couldn't load layered color mask image", e);
		}
	}
}
