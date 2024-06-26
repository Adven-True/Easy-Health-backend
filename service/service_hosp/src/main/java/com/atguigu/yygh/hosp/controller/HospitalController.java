package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	//
	@GetMapping("list/{page}/{limit}")
	public Result HospitalList(@PathVariable("page") Integer page,
							   @PathVariable("limit") Integer limit,
							   HospitalQueryVo hospitalQueryVo){
		Page<Hospital> hospitalPage = hospitalService.selectHospitalPage(page,limit,hospitalQueryVo);

		return Result.ok(hospitalPage);
	}
	//
	@ApiOperation(value = "updateHospOnline")
	@GetMapping("updateHospStatus/{id}/{status}")
	public Result updateHospStatus(@PathVariable("id") String id,@PathVariable("status") Integer status){
		hospitalService.updateStatus(id,status);
		return Result.ok();
	}


	@ApiOperation("hospDetail")
	@GetMapping("showHospDetail/{id}")
	public Result showHospDetail(@PathVariable("id") String id){
	  	Map<String, Object> hospital = hospitalService.getHospById(id);
		return Result.ok(hospital);
	}



}
