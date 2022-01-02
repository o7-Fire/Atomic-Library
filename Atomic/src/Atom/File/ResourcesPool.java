package Atom.File;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourcesPool {

    public Set<Resolver> resolvers = new HashSet<>();


    public ResourcesPool() {
        resolvers.add(new ClasspathResolver());
        resolvers.add(new FileResolver());
    }

    public interface Resource {
        default URL getURL() {
            return null;
        }

        InputStream stream() throws IOException;

        default byte[] readAllBytes() throws IOException {
            return FileUtility.readAllBytes(stream());
        }

        default String readString() throws IOException {
            return readString(Charset.defaultCharset());
        }

        default String readString(Charset charset) throws IOException {
            return new String(readAllBytes(), charset);
        }

        default List<String> readAllString(Charset charset) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream(), charset))) {
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            }
        }
    }

    @FunctionalInterface
    public static interface Resolver {
        Resource resolve(String name);
    }

    public static class FileResource extends URLResource {
        public final File file;

        public FileResource(File file) {
            file = file.getAbsoluteFile();
            if (!file.exists()) {
                throw new IllegalArgumentException("File not found: " + file);
            }
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException ignored) {

            }
            this.file = file;
        }

        @Override
        public InputStream stream() throws IOException {
            try (InputStream in = new FileInputStream(file)) {
                return in;
            }
        }
    }

    public static class URLResource implements Resource {
        public URL url;

        protected URLResource() {
        }

        public URLResource(URL url) {
            this.url = url;
        }

        @Override
        public URL getURL() {
            return url;
        }

        @Override
        public InputStream stream() throws IOException {
            return url.openStream();
        }
    }

    public static class ClasspathResolver implements Resolver {
        public static ClasspathResolver INSTANCE = new ClasspathResolver();
        public ClassLoader classLoader;

        public ClasspathResolver() {
            classLoader = getClass().getClassLoader();
        }

        public ClasspathResolver(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }


        public static Resource get(String path) {
            return INSTANCE.resolve(path);
        }

        @Override
        //dont forget / at start
        public Resource resolve(String name) {
            URL u = classLoader.getResource(name);
            if (u == null) return null;
            return new URLResource(u);
        }
    }

    public static class FileResolver implements Resolver {
        public final File parent;

        public FileResolver() {
            this(FileUtility.cwd());
        }

        public FileResolver(File dir) {
            this.parent = dir;
        }

        @Override
        public Resource resolve(String name) {
            return null;
        }
    }
}
