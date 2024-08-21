package net.irisshaders.iris.pipeline.programs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.pipeline.ShaderRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.GameRenderer;

public class ShaderAccess {
	public static VertexFormat IE_FORMAT = VertexFormat.builder()
		.add("Position", VertexFormatElement.POSITION)
		.add("Color", VertexFormatElement.COLOR)
		.add("UV0", VertexFormatElement.UV0)
		.add("Normal", VertexFormatElement.NORMAL)
		.padding(1)
		.build();

	public static CompiledShaderProgram getParticleTranslucentShader() {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {
			CompiledShaderProgram override = ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShaderKey.PARTICLES_TRANS);

			if (override != null) {
				return override;
			}
		}

		return Minecraft.getInstance().getShaderManager().getProgram(CoreShaders.PARTICLE);
	}

	public static CompiledShaderProgram getIEVBOShader() {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {

			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.IE_COMPAT_SHADOW : ShaderKey.IE_COMPAT);
		}

		return null;
	}

    public static CompiledShaderProgram getMekanismFlameShader() {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {

			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.MEKANISM_FLAME_SHADOW : ShaderKey.MEKANISM_FLAME);
		}

		return Minecraft.getInstance().getShaderManager().getProgram(CoreShaders.POSITION_TEX_COLOR);
    }

    public static CompiledShaderProgram getMekasuitShader() {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {
			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.SHADOW_ENTITIES_CUTOUT : ShaderKey.ENTITIES_TRANSLUCENT);
		}

		return Minecraft.getInstance().getShaderManager().getProgram(CoreShaders.RENDERTYPE_ENTITY_CUTOUT);
    }
}
