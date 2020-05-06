package moe.gensoukyo.mcgproject.cilent.gui;

import moe.gensoukyo.mcgproject.common.feature.musicplayer.IStream;

import java.io.IOException;

public interface IValidateStream extends IStream {
    int getStreamSize() throws IOException;
}
