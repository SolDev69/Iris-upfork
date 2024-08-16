package net.irisshaders.iris.uniforms;

import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.iris.gl.state.FogMode;
import net.irisshaders.iris.gl.uniform.DynamicUniformHolder;
import net.minecraft.client.renderer.FogParameters;
import org.joml.Vector4f;

import static net.irisshaders.iris.gl.uniform.UniformUpdateFrequency.PER_FRAME;

/**
 * Internal Iris uniforms that are not directly accessible by shaders.
 */
public class IrisInternalUniforms {
	private IrisInternalUniforms() {
		// no construction
	}

	public static void addFogUniforms(DynamicUniformHolder uniforms, FogMode fogMode) {
		uniforms
			.uniform4f(PER_FRAME, "iris_FogColor", () -> {
				FogParameters fog = RenderSystem.getShaderFog();
				return new Vector4f(fog.red(), fog.green(), fog.blue(), fog.alpha());
			});

		uniforms.uniform1f(PER_FRAME, "iris_FogStart", () -> RenderSystem.getShaderFog().start())
			.uniform1f(PER_FRAME, "iris_FogEnd", () -> RenderSystem.getShaderFog().end());

		uniforms.uniform1f("iris_FogDensity", () -> {
			// ensure that the minimum value is 0.0
			return Math.max(0.0F, CapturedRenderingState.INSTANCE.getFogDensity());
		}, notifier -> {
		});

		uniforms.uniform1f("iris_currentAlphaTest", CapturedRenderingState.INSTANCE::getCurrentAlphaTest, notifier -> {
		});

		// Optifine compatibility
		uniforms.uniform1f("alphaTestRef", CapturedRenderingState.INSTANCE::getCurrentAlphaTest, notifier -> {
		});
	}
}
