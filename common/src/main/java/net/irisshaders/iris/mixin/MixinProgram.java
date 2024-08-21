package net.irisshaders.iris.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.irisshaders.iris.gl.program.Program;
import net.irisshaders.iris.gl.shader.ShaderCompileException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Mixin(Program.class)
public class MixinProgram {

}
