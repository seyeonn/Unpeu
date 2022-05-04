package com.unpeu.api.present;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unpeu.config.auth.UnpeuUserDetails;
import com.unpeu.domain.entity.Message;
import com.unpeu.domain.entity.User;
import com.unpeu.domain.request.MessagePostReq;
import com.unpeu.service.iface.IMessageService;
import com.unpeu.service.iface.IUserService;

import org.springframework.security.core.Authentication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.jetbrains.annotations.NotNull;

@Api("Message 관련 기능")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
	public static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	private final IUserService userService;
	private final IMessageService messageService;

	/**
	 * userId에 따라 메세지 목록을 조회한다.
	 * @param userId
	 * @return
	 */
	@ApiOperation(value = "메세지 목록 조회 Controller")
	@RequestMapping(value = "/message/list/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getMessageListByUserId(@NotNull @PathVariable("userId") Long userId) {
		logger.info("getMessageListByUserId - 호출");
		Map<String, Object> resultMap = new HashMap<>();
		List<Message> newMessage = messageService.getMessageListByUserId(userId);
		resultMap.put("Message", newMessage);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "메세지 상세 조회 Controller")
	@RequestMapping(value = "/message/{messageId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getMessage(@NotNull @PathVariable("messageId") Long messageId) {
		logger.info("getMessage - 호출");
		Map<String, Object> resultMap = new HashMap<>();
		Message newMessage = messageService.getMessage(messageId);
		resultMap.put("Message", newMessage);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
	}


	@ApiOperation(value = "메세지 초기화 Controller")
	@RequestMapping(value = "/message", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteAllMessageByUserId(@ApiIgnore @NotNull Authentication authentication) {
		logger.info("deleteAllMessageByUserId - 호출");
		// test 시 userId : 1로 설정하고 테스트 했음
		UnpeuUserDetails userDetails = (UnpeuUserDetails)authentication.getDetails();
		User user = userDetails.getUser();
		Map<String, Object> resultMap = new HashMap<>();
		messageService.deleteAllMessageByUserId(user.getId());
		resultMap.put("Message", "Delete Messages Success");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value = "메세지 다이어리에 저장 Controller")
	@RequestMapping(value = "/message/messageToDiary", method = RequestMethod.POST/*, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}*/)
	public ResponseEntity<Map<String, Object>> saveMessage(@ApiIgnore @NotNull Authentication authentication,
			@Valid @RequestBody @NotNull List<MessagePostReq> messages) {
		logger.info("saveMessage - 호출");
		// test 시 userId : 1로 설정하고 테스트 했음
		UnpeuUserDetails userDetails = (UnpeuUserDetails)authentication.getDetails();
		User user = userDetails.getUser();
		// message 내용을 board에 저장
		messageService.saveMessage(user.getId(), messages);
		// message 내용 삭제
		messageService.deleteAllMessageByUserId(user.getId());
		return new ResponseEntity<Map<String, Object>>(HttpStatus.ACCEPTED);
	}

}