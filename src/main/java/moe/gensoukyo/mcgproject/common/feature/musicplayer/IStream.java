package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import java.io.IOException;
import java.io.InputStream;

public interface IStream {
    InputStream openStream() throws IOException;
}
