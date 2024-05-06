package com.hanamilink.ble;

import java.util.Arrays;

public class CMD {

    // 帧头
// 定义帧头
    private static final byte FRAME_HEADER = (byte) 0x8A;

    // 读取芯片信息命令
    private static final byte CMD_READ_CHIP_INFO = (byte) 0xA1;
    private static final byte CMD_CONFIRM_READ_CHIP_INFO = (byte) 0x01;
    private static final byte CMD_NO_OPERATION = (byte) 0x00;

    // 均衡模式及数值设置命令及参数
    private static final byte CMD_EQ_MODE_AND_VALUES = (byte) 0xA0;
    private static final byte EQ_MODE_VALUE = (byte) 0x01;
    private static final byte EQ1_MIN_VALUE = (byte) 0;
    private static final byte EQ1_MAX_VALUE = (byte) 24;
    private static final byte EQ2_MIN_VALUE = (byte) 0;
    private static final byte EQ2_MAX_VALUE = (byte) 24;

    // 音量命令及参数
    private static final byte CMD_VOLUME = (byte) 0xA9;
    private static final byte VOLUME_MIN_VALUE = (byte) 0;
    private static final byte VOLUME_MAX_VALUE = (byte) 40;

    // 低音和高音命令
    private static final byte CMD_BASS = (byte) 0xAA;
    private static final byte CMD_TREBLE = (byte) 0xAB;

    // 麦克风魔法效果命令及参数
    private static final byte CMD_MIC_MAGIC_EFFECT = (byte) 0xB0;
    private static final byte MIC_MAGIC_EFFECT_OFF = (byte) 0x00;
    private static final byte MIC_MAGIC_EFFECT_CHILDREN = (byte) 0x01;
    private static final byte MIC_MAGIC_EFFECT_FEMALE = (byte) 0x02;
    private static final byte MIC_MAGIC_EFFECT_MALE = (byte) 0x03;
    private static final byte MIC_MAGIC_EFFECT_ELECTRONIC = (byte) 0x04;
    private static final byte MIC_MAGIC_EFFECT_MAGIC = (byte) 0x05;

    // 麦克风回声效果命令及参数
    private static final byte CMD_MIC_ECHO_EFFECT = (byte) 0xB1;
    private static final byte MIC_ECHO_EFFECT_OFF = (byte) 0x00;
    private static final byte MIC_ECHO_EFFECT_ON = (byte) 0x01;
    private static final byte MIC_ECHO_ATTENUATION_MIN = (byte) 0;
    private static final byte MIC_ECHO_ATTENUATION_MAX = (byte) 32;
    private static final byte MIC_ECHO_INTERVAL_MIN = (byte) 0;
    private static final byte MIC_ECHO_INTERVAL_MAX = (byte) 32;


    // 其他命令和参数省略...

    // 模拟设备的通信
    public static byte[] sendCommand(byte[] command) {
        // 模拟发送数据给设备，并接收设备返回的数据
        // 实际应用中，需要根据实际情况进行调整，可能需要使用网络或串口通信等
        // 这里简单地返回一个假的响应数据
        return new byte[]{(byte) 0xAA, (byte) 0x00, (byte) 0xEE}; // 示例数据
    }

    // 创建数据包
    public static byte[] createPacket(byte command, byte[] data) {
        int len = 2 + data.length; // 2表示命令字段和数据长度字段的长度
        byte[] packet = new byte[len + 3]; // 加3是为了帧头、数据长度和命令字段
        packet[0] = FRAME_HEADER; // 帧头
        packet[1] = (byte) len; // 数据长度
        packet[2] = command; // 命令字段
        System.arraycopy(data, 0, packet, 3, data.length); // 拷贝数据
        packet[len + 2] = calculateChecksum(packet); // 计算并添加校验和
        return packet;
    }

    // 计算校验和
    private static byte calculateChecksum(byte[] packet) {
        // 计算校验和：将帧头之后的所有字节相加
        int sum = 0;
        for (int i = 1; i < packet.length - 1; i++) {
            sum += packet[i];
        }
        return (byte) (sum & 0xFF);
    }

    // 解析数据包
    public static void parsePacket(byte[] packet) {
        byte command = packet[2]; // 获取命令字节
        byte[] data = Arrays.copyOfRange(packet, 3, packet.length - 1); // 获取数据部分，不包括校验和
        byte checksum = packet[packet.length - 1]; // 获取校验和
        // 检查校验和是否正确
        byte calculatedChecksum = calculateChecksum(packet);
        if (checksum != calculatedChecksum) {
            System.out.println("Checksum error!");
            return;
        }
        // 输出解析结果
        System.out.println("Received command: 0x" + String.format("%02X", command));
        System.out.println("Received data: " + Arrays.toString(data));
    }

    // 主方法用于测试
    public static void main(String[] args) {
        // 示例：创建一个读取芯片全部信息的命令包
        byte[] commandPacket = createPacket(CMD_READ_CHIP_INFO, new byte[]{CMD_CONFIRM_READ_CHIP_INFO});
        System.out.println("Created command packet: " + Arrays.toString(commandPacket));

        // 示例：发送命令并接收响应
        byte[] response = sendCommand(commandPacket);
        System.out.println("Received response: " + Arrays.toString(response));

        // 示例：解析接收到的响应数据包
        parsePacket(response);
    }
}
