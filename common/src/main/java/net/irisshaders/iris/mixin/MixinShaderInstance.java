package net.irisshaders.iris.mixin;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.gl.GLDebug;
import net.irisshaders.iris.gl.blending.DepthColorStorage;
import net.irisshaders.iris.pipeline.ShaderRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.pipeline.programs.ExtendedShader;
import net.irisshaders.iris.pipeline.programs.FallbackShader;
import net.irisshaders.iris.mixinterface.ShaderInstanceInterface;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.lwjgl.opengl.KHRDebug;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CompiledShaderProgram.class)
public abstract class MixinShaderInstance implements ShaderInstanceInterface {
	@Unique
	private static final ImmutableSet<String> ATTRIBUTE_LIST = ImmutableSet.of("Position", "Color", "Normal", "UV0", "UV1", "UV2");

	@Unique
	private CompiledShaderProgram lastAppliedShader;

	private static boolean shouldOverrideShaders() {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {
			return ((ShaderRenderingPipeline) pipeline).shouldOverrideShaders();
		} else {
			return false;
		}
	}

	@Shadow
	@Final
	private int programId;

	// TODO IMS 24w34a
	/*@Redirect(method = "<init>*", require = 1, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;glBindAttribLocation(IILjava/lang/CharSequence;)V"))
	public void iris$redirectBindAttributeLocation(int i, int j, CharSequence charSequence) {
		if (((Object) this) instanceof ExtendedShader && ATTRIBUTE_LIST.contains(charSequence)) {
			Uniform.glBindAttribLocation(i, j, "iris_" + charSequence);
		} else {
			Uniform.glBindAttribLocation(i, j, charSequence);
		}
	}*/

	@Inject(method = "apply", at = @At("HEAD"))
	private void iris$lockDepthColorState(CallbackInfo ci) {
		if (lastAppliedShader != null) {
			lastAppliedShader.clear();
		}

		lastAppliedShader = ((CompiledShaderProgram) (Object) this);
	}

	@Inject(method = "apply", at = @At("TAIL"))
	private void onTail(CallbackInfo ci) {
		if (((Object) this) instanceof ExtendedShader || ((Object) this) instanceof FallbackShader || !shouldOverrideShaders()) {
			DepthColorStorage.unlockDepthColor();
			return;
		}

		DepthColorStorage.disableDepthColor();
	}

	@Inject(method = "clear", at = @At("HEAD"))
	private void iris$unlockDepthColorState(CallbackInfo ci) {
		lastAppliedShader = null;

		if (((Object) this) instanceof ExtendedShader || ((Object) this) instanceof FallbackShader || !shouldOverrideShaders()) {
			return;
		}

		DepthColorStorage.unlockDepthColor();
	}
}
