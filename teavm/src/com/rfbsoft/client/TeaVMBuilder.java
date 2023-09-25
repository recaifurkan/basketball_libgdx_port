package com.rfbsoft.client;

import com.github.xpenatan.gdx.backends.teavm.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import org.teavm.tooling.TeaVMTool;

import java.io.File;
import java.io.IOException;

/**
 * Builds the TeaVM/HTML application.
 */
@SkipClass
public class TeaVMBuilder {
    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
        teaBuildConfiguration.assetsPath.add(new File("../assets"));
        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();
        // You can switch this setting during development:

        // Register any extra classpath assets here:
        // teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/github/xpenatan/vehicle/asset.extension");

        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("com.github.xpenatan.vehicle.reflect");

        TeaVMTool tool = TeaBuilder.config(teaBuildConfiguration);
        tool.setObfuscated(true);
        tool.setMainClass(TeaVMLauncher.class.getName());
        TeaBuilder.build(tool);
    }
}
