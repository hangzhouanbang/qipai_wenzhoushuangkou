package com.anbang.qipai.wenzhoushuangkou.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface WenzhouShuangkouGameSource {
	@Output
	MessageChannel wenzhouShuangkouGame();
}
