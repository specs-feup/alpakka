package pt.up.fe.specs.alpakka.weaver;

import pt.up.fe.specs.util.providers.ResourceProvider;

import java.util.List;

public class AlpakkaLaraApis {

    private static final List<ResourceProvider> SMALI_LARA_API = ResourceProvider
            .getResourcesFromEnum(AlpakkaApiJsResource.class);

    public static List<ResourceProvider> getApis() {
        return SMALI_LARA_API;
    }
}
