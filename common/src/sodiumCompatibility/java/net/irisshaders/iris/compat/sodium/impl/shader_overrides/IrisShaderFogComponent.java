package net.irisshaders.iris.compat.sodium.impl.shader_overrides;

import com.mojang.blaze3d.systems.RenderSystem;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat4v;

public class IrisShaderFogComponent {
	private final GlUniformFloat4v uFogColor;
	private final GlUniformFloat uFogStart;
	private final GlUniformFloat uFogEnd;

	public IrisShaderFogComponent(ShaderBindingContextExt context) {
		this.uFogColor = context.bindUniformIfPresent("iris_FogColor", GlUniformFloat4v::new);
		this.uFogStart = context.bindUniformIfPresent("iris_FogStart", GlUniformFloat::new);
		this.uFogEnd = context.bindUniformIfPresent("iris_FogEnd", GlUniformFloat::new);
	}

	public void setup() {
		if (this.uFogColor != null) {
			this.uFogColor.set(new float[] { RenderSystem.getShaderFog().red(), RenderSystem.getShaderFog().green(), RenderSystem.getShaderFog().blue(), RenderSystem.getShaderFog().alpha()});
		}

		if (this.uFogStart != null) {
			this.uFogStart.setFloat(RenderSystem.getShaderFog().start());
		}

		if (this.uFogEnd != null) {
			this.uFogEnd.setFloat(RenderSystem.getShaderFog().end());
		}
	}
}
