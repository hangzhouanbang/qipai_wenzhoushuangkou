package com.anbang.qipai.wenzhoushuangkou.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String WENZHOUSHUANGKOUGAMEROOM = "wenzhouShuangkouGameRoom";

	@Input
	SubscribableChannel wenzhouShuangkouGameRoom();
}
