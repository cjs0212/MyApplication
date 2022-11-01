package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class AudioFromVideo {
    //비디오, 오디오 코덱 초기화
    private String audio, videopath;
    private MediaCodec audioDeCoder;
    private MediaCodec audioEncoder;
    private MediaCodec audioEncoder2;
    private MediaExtractor audioExtractor,audioExtractor2, videoExtractor ;
    private MediaFormat audioformat;
    private MediaFormat videoformat;
    private String audiomime, videomime;
    private MediaMuxer muxer;
    private static final int MAX_BUFFER_SIZE = 256 * 1024;
    private int videoTrackIndex = -1;
    private int audioTrackIndex = -1;
    int videoExtractedFrameCount = 0;
    long lastPresentationTimeVideoExtractor = 0;

    //비디오 추출 끝났는지 확인함
    boolean videoExtractorDone = false;
    boolean audioExtractorDone = false;
    boolean audioDecoderDone = false;
    boolean audioEncoderDone = false;
    int sampleRate = 44100;
    //형식 변경에 대한 알림을 받으면 디코더에서 이를 가져옵니다.
    MediaFormat decoderOutputAudioFormat = null;
    MediaFormat encoderOutputAudioFormat = null;
    MediaFormat outputFormat = null;

    public AudioFromVideo(String srcVideo, String destAudio, String destVideo) {
        this.audio = destAudio;
        this.videopath = srcVideo;
        audioExtractor = new MediaExtractor();
        audioExtractor2 = new MediaExtractor();
        videoExtractor = new MediaExtractor();
        audioDeCoder = null;
        audioEncoder = null;
        muxer = null;

        init(destVideo);
    }

    public void init(String destVideo) {
        try {
            //videopath를 통하여 추출
            audioExtractor.setDataSource(videopath);
            videoExtractor.setDataSource(videopath);
            audioExtractor2.setDataSource(videopath);

            audioformat = audioExtractor.getTrackFormat(1);
            videoformat = videoExtractor.getTrackFormat(0);


            audioExtractor.selectTrack(1);
            videoExtractor.selectTrack(0);

            audiomime = audioformat.getString(MediaFormat.KEY_MIME);
            videomime = videoformat.getString(MediaFormat.KEY_MIME); //

            //디코더 초기화
            Log.d("테스트", audiomime);
            audioDeCoder = MediaCodec.createDecoderByType(audiomime);
            audioDeCoder.configure(audioformat, null, null, 0);
            audioDeCoder.start();

            //인코더 초기화
            outputFormat = MediaFormat.createAudioFormat(audiomime, 44100, 2);
            outputFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            outputFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_STEREO);
            outputFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 2);
            outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128000);
            //outputFormat.setInteger(MediaFormat.KEY_PCM_ENCODING, AudioFormat.CHANNEL_IN_STEREO);


            audioEncoder2 = MediaCodec.createEncoderByType(audiomime);
            audioEncoder2.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            audioEncoder2.start();



            audioEncoder = MediaCodec.createEncoderByType(audiomime);
            audioEncoder.configure(audioformat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            audioEncoder.start();


            muxer = new MediaMuxer(destVideo, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

        new AudioService(audioDeCoder, audioExtractor, audio).start();
    }

    private class AudioService extends Thread {
        private MediaCodec mMediaCodec;
        private MediaExtractor mMediaExtractor;
        private String destFile;

        AudioService(MediaCodec amc, MediaExtractor ame, String destFile) {
            mMediaCodec = amc;
            mMediaExtractor = ame;
            this.destFile = destFile;
        }

        public void run() {
            try {

                //파일 아웃풋, 버퍼정보,  muxer 2가지 트랙 초기화
                OutputStream os = new FileOutputStream(new File(destFile)); //.pcm파일을 출력하게됨
                long count = 0;
                MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo(); //인코딩, 디코딩을 하기 위해서는 항상 버퍼 인포가 필요함
                ByteBuffer videoInputBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
                //음성과 영상 데이터 포맷(형식)을 muxer에 추가함
                videoTrackIndex = muxer.addTrack(videoformat);
                audioTrackIndex = muxer.addTrack(audioEncoder2.getOutputFormat());
                Log.d("", "muxer: adding audio track." + audioTrackIndex);
                muxer.start();
                Log.d("", "muxer: starting");


                /** 1. 비디오 추출 및 muxer에 추가 (이부분은 비디오만이라 볼필요 없을거임 */
                while (!videoExtractorDone) {
                    try {
                        videoBufferInfo.size = videoExtractor.readSampleData(videoInputBuffer, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (videoBufferInfo.size < 0) {
                        videoBufferInfo.size = 0;
                        videoExtractorDone = true;
                    } else {
                        videoBufferInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
                        videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                        lastPresentationTimeVideoExtractor = videoBufferInfo.presentationTimeUs;
                        int videoflag = videoExtractor.getSampleFlags();
                        Log.d("add video track", "");
                        muxer.writeSampleData(videoTrackIndex, videoInputBuffer, videoBufferInfo); //muxer에 추가
                        videoExtractor.advance(); //다음 추출
                        videoExtractedFrameCount++;
                    }
                }
                /** 1. 비디오 추출 끝 */

                /** 2. 오디오 추출 및 디코딩 */
                while (true) {
                    int inputIndex = mMediaCodec.dequeueInputBuffer(0);
                    if (inputIndex == -1) {
                        continue;
                    }
                    //MediaCodec을 초기화 하기 위해서는 MediaFormat을 알아야함
                    //MediaFormat을 MediaExtractor를 이용하여 값을 가져옴
                    //코덱버퍼에 필요한 정보들 (sampleSize, presentationTime, flag) 가져옴
                    //mMediaCodec은 디코더 코덱임
                    int sampleSize = mMediaExtractor.readSampleData(mMediaCodec.getInputBuffer(inputIndex), 0); //오디오 사이즈 추출
                    if (sampleSize == -1) break;    //만약 다 읽었거나 파일 내용이 없는 경우(리턴값 -1) 종료
                    long presentationTime = mMediaExtractor.getSampleTime();
                    int flag = mMediaExtractor.getSampleFlags();
                    mMediaExtractor.advance();  //익스트랙터에서 추출할 내용을 모두 추출했으므로 다음으로 넘어감
                    mMediaCodec.queueInputBuffer(inputIndex, 0, sampleSize, presentationTime, flag);    //코덱 버퍼에 추출한 내용들 input
                    //여기까지 인풋

                    //코덱에 들어갔다 나오면서 디코딩 파일이 출력됨 아웃풋 과정
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo(); //버퍼 인포 초기화
                    //dequeueOutputBuffer는 아까 dequeueInputBuffer 함수에서 넣어둔 정보를 가져오는 역할
                    int outputIndex = mMediaCodec.dequeueOutputBuffer(info, 0);
                    if (outputIndex >= 0) { //outputIndex가 더 읽은게 없으면 -1을 리턴함

                        ByteBuffer bb = mMediaCodec.getOutputBuffer(outputIndex); //바이트버퍼 bb에 디코딩 데이터를 입력
                        byte[] data = new byte[info.size]; //버퍼 size 만큼 data 확보
                        bb.get(data); //데이터를 get 할 때 마다 position이 바뀐다.
                        // 버퍼 size 만큼 공간 확보한 다음 position을 바꿔줄라고 하는 과정같음

                        short[] audioShort = byteArrayToShortArray(data);
                        double[] audioDouble = short2double(audioShort);
                        short[] sAudio = doubleArrayToShortArray(audioDouble);
                        byte[] enhancedAudio = short2byte(sAudio);

                        count += data.length; //잘 되고있는지 확인하기 위한 count 이것도 필요 없는 내용

                        os.write(enhancedAudio); //파일 아웃풋

                        Log.i("Sample Size", "" + sampleSize);
                        Log.i("write", "" + count);
                        Log.i("Output Index", "" + outputIndex);
                        Log.i("Presentation Time", "" + presentationTime);

                        mMediaCodec.releaseOutputBuffer(outputIndex, false); //버퍼를 코덱으로 반환
                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        decoderOutputAudioFormat = audioDeCoder.getOutputFormat();
                    }
                }
                Log.i("write", "done");
                //여기서 디코딩 + .pcm파일로 추출이 끝남
                Log.d("", "--------------------------------------------------------------------");
                Log.d("", "------------------------- 디코딩 종료 --------------------------------");
                Log.d("", "--------------------------------------------------------------------");












                //인코딩
                try {
                    File inputFile = new File(destFile);    //디코딩된 파일을(.pcm) 읽어옴
                    FileInputStream fis = new FileInputStream(inputFile);
                    DataInputStream dis = new DataInputStream(fis);





                    ByteBuffer[] codecInputBuffers = audioEncoder2.getInputBuffers();
                    ByteBuffer[] codecOutputBuffers = audioEncoder2.getOutputBuffers();

                    MediaCodec.BufferInfo outBuffInfo = new MediaCodec.BufferInfo();

                    byte[] tempBuffer = new byte[4096];
                    boolean hasMoreData = true;
                    double presentationTimeUs = 0;
                    int audioTrackIdx = 0;
                    int totalBytesRead = 0;
                    int percentComplete;

                    do {

                        int inputBufIndex = 0;
                        while (inputBufIndex != -1 && hasMoreData) {
                            inputBufIndex = audioEncoder2.dequeueInputBuffer(0); //인코더 인덱스 추출

                            if (inputBufIndex >= 0) {
                                ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
                                dstBuf.clear();

                                int bytesRead = dis.read(tempBuffer, 0, dstBuf.limit());
                                if (bytesRead == -1) { // -1 implies EOS
                                    hasMoreData = false;
                                    audioEncoder2.queueInputBuffer(inputBufIndex, 0, 0, (long) presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                } else {
                                    totalBytesRead += bytesRead;
                                    dstBuf.put(tempBuffer, 0, bytesRead);
                                    audioEncoder2.queueInputBuffer(inputBufIndex, 0, bytesRead, (long) presentationTimeUs, 0);
                                    //presentationTimeUs = 1000000l * (totalBytesRead/2) / sampleRate;
                                    presentationTimeUs = 0;
                                    /*presentationTimeUs = audioExtractor2.getSampleTime();
                                    audioExtractor2.advance();*/
                                }
                            }
                        }

                        // Drain audio
                        int outputBufIndex = 0;
                        while (outputBufIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
                            //디코딩 때와 같이 input 데이터를 output시킴
                            outputBufIndex = audioEncoder2.dequeueOutputBuffer(outBuffInfo, 0);
                            if (outputBufIndex >= 0) {
                                ByteBuffer encodedData = codecOutputBuffers[outputBufIndex];
                                encodedData.position(outBuffInfo.offset);
                                encodedData.limit(outBuffInfo.offset + outBuffInfo.size);

                                if ((outBuffInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0 && outBuffInfo.size != 0) {
                                    audioEncoder2.releaseOutputBuffer(outputBufIndex, false);
                                } else {

                                    muxer.writeSampleData(audioTrackIndex, codecOutputBuffers[outputBufIndex], outBuffInfo);
                                    audioEncoder2.releaseOutputBuffer(outputBufIndex, false);
                                }
                            } else if (outputBufIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                encoderOutputAudioFormat = audioEncoder2.getOutputFormat();
                                Log.v("AUDIO", "Output format changed - " + encoderOutputAudioFormat);
                            } else if (outputBufIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                                Log.e("AUDIO", "Output buffers changed during encode!");
                            } else if (outputBufIndex != MediaCodec.INFO_TRY_AGAIN_LATER){
                                Log.e("AUDIO", "Unknown return code from dequeueOutputBuffer - " + outputBufIndex);
                            }
                        }
                        percentComplete = (int) Math.round(((float) totalBytesRead / (float) inputFile.length()) * 100.0);
                        Log.v("AUDIO", "Conversion % - " + percentComplete);
                    } while (outBuffInfo.flags != MediaCodec.BUFFER_FLAG_END_OF_STREAM);

                    fis.close();
                    os.flush();
                    os.close();
                    muxer.stop();
                    muxer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



    public static short[] byteArrayToShortArray(byte[] byteArray) {
        int nlengthInSamples = byteArray.length / 2;
        short[] audioData = new short[nlengthInSamples];

        for (int i = 0; i < nlengthInSamples; i++) {
            short MSB = (short) byteArray[2 * i + 1];
            short LSB = (short) byteArray[2 * i];
            audioData[i] = (short) (MSB << 8 | (255 & LSB));
        }

        return audioData;
    }

    private double[] short2double(short[] shortData) {
        int size = shortData.length;
        double[] doubleData = new double[size];
        for (int i = 0; i < size; i++) {
            doubleData[i] = shortData[i] / 32768.0;
        }
        return doubleData;
    }

    private short[] doubleArrayToShortArray(double[] doubleArray) {
        short[] shortArray = new short[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            shortArray[i] = (short) (doubleArray[i] * 32768.0);
        }
        return shortArray;
    }

    private byte[] short2byte(short[] sData) {
        byte[] bytes = new byte[sData.length * 2];
        for (int i = 0; i < sData.length; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
        }
        return bytes;
    }
}