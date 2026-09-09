package framework.config;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Browser-specific but driver-independent execution preferences. */
public record BrowserOptions(
        boolean headless,
        boolean maximizeWindow,
        int windowWidth,
        int windowHeight,
        List<String> arguments,
        Path downloadDirectory) {
    public BrowserOptions {
        if (windowWidth < 0 || windowHeight < 0) throw new IllegalArgumentException("Window dimensions must not be negative");
        arguments = List.copyOf(Objects.requireNonNull(arguments, "arguments must not be null"));
        downloadDirectory = Objects.requireNonNull(downloadDirectory, "downloadDirectory must not be null");
    }
    public boolean hasWindowSize() { return windowWidth > 0 && windowHeight > 0; }
}
