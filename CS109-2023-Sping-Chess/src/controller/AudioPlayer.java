package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;

public class AudioPlayer {
    private Clip audioClip;
    private String filePath;

    public AudioPlayer(String filePath) {
        this.filePath = filePath;
    }

    public void play() {
        try {
            // 打开音频文件
            File audioFile = new File(filePath);

            // 获取音频输入流
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // 获取音频剪辑
            audioClip = AudioSystem.getClip();

            // 打开音频剪辑，并将音频输入流加载到剪辑中
            audioClip.open(audioStream);

            audioClip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    audioClip.setFramePosition(0); // 将音频剪辑的帧位置重置到开始
                    audioClip.start(); // 重新开始播放
                }
            });

            // 播放音频
            audioClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void play1() {
        try {
            // 打开音频文件
            File audioFile = new File(filePath);

            // 获取音频输入流
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // 获取音频剪辑
            audioClip = AudioSystem.getClip();

            // 打开音频剪辑，并将音频输入流加载到剪辑中
            audioClip.open(audioStream);

//            audioClip.addLineListener(e -> {
//                if (e.getType() == LineEvent.Type.STOP) {
//                    audioClip.setFramePosition(0); // 将音频剪辑的帧位置重置到开始
//                    audioClip.start(); // 重新开始播放
//                }
//            });

            // 播放音频
            audioClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            audioClip.close();
        }
    }
}

