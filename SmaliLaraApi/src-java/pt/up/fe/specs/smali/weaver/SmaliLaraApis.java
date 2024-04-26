package pt.up.fe.specs.smali.weaver;

import pt.up.fe.specs.util.providers.ResourceProvider;

import java.util.List;

public class SmaliLaraApis {

    private static final List<ResourceProvider> SMALI_LARA_API = ResourceProvider
            .getResourcesFromEnum(SmaliApiJsResource.class);

    public static List<ResourceProvider> getApis() {
        return SMALI_LARA_API;
    }
}
