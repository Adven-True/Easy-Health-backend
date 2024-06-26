package com.atguigu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DictListener extends AnalysisEventListener<DictEeVo> {

	private DictMapper dictMapper;

	public DictListener(DictMapper dictMapper) {
		this.dictMapper = dictMapper;
	}

	//from 2nd row, one by one

	@Override
	public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
		//add to database
		Dict dict = new Dict();
		BeanUtils.copyProperties(dictEeVo,dict);
		dictMapper.insert(dict);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {

	}
}
