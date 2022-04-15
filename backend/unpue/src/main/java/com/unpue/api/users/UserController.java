package com.unpue.api.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "���� API", tags = {"User"})
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@PostMapping()
    @ApiOperation(value = "ȸ�� ����", notes = "<strong>���̵�� �н�����</strong>�� ���� ȸ������ �Ѵ�.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ȸ������ ����"),
            @ApiResponse(code = 401, message = "ȸ������ ����"),
            @ApiResponse(code = 500, message = "���� ����")
    })
    public ResponseEntity<Map<String, Object>> userAdd(){

     
        Map<String, Object> resultMap = new HashMap<>();
        
        resultMap.put("message", "success");
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
	
}
