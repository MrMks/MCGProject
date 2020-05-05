package moe.gensoukyo.mcgproject.common.feature.musicplayer;

public interface IValidateCallBack {
    void onValidated(String hash, boolean valid, int tickLength);
}