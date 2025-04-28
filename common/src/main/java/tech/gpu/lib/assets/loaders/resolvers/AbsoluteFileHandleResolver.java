package tech.gpu.lib.assets.loaders.resolvers;

import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.assets.loaders.FileHandleResolver;
import tech.gpu.lib.files.FileHandle;

public class AbsoluteFileHandleResolver implements FileHandleResolver {
    @Override
    public FileHandle resolve(String fileName) {
        return ApplicationEnvironment.files.absolute(fileName);
    }
}
