package com.unpeu.service.iface;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.unpeu.domain.entity.Message;
import com.unpeu.domain.entity.Present;
import com.unpeu.domain.request.MessagePostReq;
import com.unpeu.domain.request.PresentPostReq;

public interface IMessageService {

	List<Message> getMessageListByUserId(Long userId);
}
