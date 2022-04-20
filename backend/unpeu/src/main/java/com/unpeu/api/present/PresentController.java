package com.unpeu.api.present;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unpeu.config.media.MediaService;
import com.unpeu.domain.entity.Message;
import com.unpeu.domain.entity.Present;
import com.unpeu.domain.entity.User;
import com.unpeu.domain.request.MessagePostReq;
import com.unpeu.domain.request.PresentPostReq;
import com.unpeu.domain.response.BaseResponseBody;
import com.unpeu.domain.response.PresentPeekPostRes;
import com.unpeu.service.iface.IPresentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Present 관련 기능")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PresentController {
	public static final Logger logger = LoggerFactory.getLogger(PresentController.class);

	private IPresentService presentService;
	private MediaService mediaService;

	@Autowired
	public PresentController(IPresentService presentService, MediaService mediaService) {
		Assert.notNull(presentService, "presentService 객체가 반드시 필요!");
		this.presentService = presentService;
		this.mediaService = mediaService;
	}

	@ApiOperation(value = "선물 등록 Controller")
	@RequestMapping(value = "/present", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Present createPresent(@Valid @ModelAttribute PresentPostReq present) {
		logger.info("createPresent - 호출");
		// Access Token을 받으면 PresentPostReq userId 항목에 id 집어넣기

		// 이미지 url 변경
		String url = mediaService.save(present.getPresentImg());
		logger.info("media Saved Url : " + url);
		present.setPresentImgUrl(url);
		return this.presentService.createPresent(present);
	}

	@ApiOperation(value = "선물 수정 Controller")
	@RequestMapping(value = "/present/{presentId}", method = RequestMethod.PUT, consumes = {
		MediaType.MULTIPART_FORM_DATA_VALUE})
	public Present updatePresent(@Valid @PathVariable("presentId") Long presentId,
		@ModelAttribute PresentPostReq present) {
		logger.info("updatePresent - 호출");
		if (present.getPresentImg() != null) {
			String url = mediaService.save(present.getPresentImg());
			present.setPresentImgUrl(url);
		}
		return this.presentService.updatePresent(presentId, present);
	}

	@ApiOperation(value = "선물 삭제 Controller")
	@RequestMapping(value = "/present/{presentId}", method = RequestMethod.DELETE)
	public ResponseEntity<? extends BaseResponseBody> deletePresent(@Valid @PathVariable("presentId") Long presentId) {
		logger.info("deletePresent - 호출");
		this.presentService.deletePresent(presentId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Delete Present Success"));
	}

	@ApiOperation(value = "선물 리스트 조회 Controller")
	@RequestMapping(value = "/present/{userId}", method = RequestMethod.GET)
	public List<Present> getPresent(@Valid @PathVariable("userId") Long userId) {
		logger.info("getPresent - 호출");
		return presentService.getPresentListByUserId(userId);
	}

	@ApiOperation(value = "선물 & 메세지 보내기 Controller")
	@RequestMapping(value = "/present/message", method = RequestMethod.POST)
	public Message sendMessageAndPresent(@Valid @RequestBody MessagePostReq message) {
		logger.info("sendMessageAndPresent - 호출");
		return presentService.sendMessageAndPresent(message);
	}

	@ApiOperation(value = "엿보기 Controller")
	@RequestMapping(value = "/present/meesage/money", method = RequestMethod.POST)
	public ResponseEntity<? extends BaseResponseBody> peekMoney() {
		logger.info("peekMoney - 호출");
		User user = new User();
		String money = presentService.peekMoney(/**userId**/ 1l);
		return ResponseEntity.status(200).body(PresentPeekPostRes.of(200, "Success Peek Money", money));
	}

}
